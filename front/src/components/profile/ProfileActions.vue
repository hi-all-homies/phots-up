<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { useAuthorStore } from '@/store/author';
import { useChatStore } from '@/store/chats';
import { useUserStore } from '@/store/user';
import type { Author } from '@/types/Author';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useRouter } from 'vue-router';


defineProps<{
    disabledBtn: boolean
}>()

const authorStore = useAuthorStore()
const { author, hasSubscriber } = storeToRefs(authorStore)

const userStore = useUserStore()
const { currentUser, isAuthenticated } = storeToRefs(userStore)

const appStore = useAppStore()

const loading = ref(false)

function subscribe(){
    loading.value = true
    userStore.subscribe(author.value?.username as string)
        .then(val => {
            if (val){
                author.value?.subscribers.push(currentUser.value as Author)
                }
            else {
                let ind = author.value?.subscribers
                    .findIndex(s => s.id === currentUser.value?.id)
                
                author.value?.subscribers.splice(ind as number, 1)
            }
            loading.value = false
        })
        .catch(() => {
            appStore.$patch({ erorrs: {
                active: true,
                message: 'failed to subscribe'
            }})
            loading.value = false
        })
}


const router = useRouter()
const chatStore = useChatStore()

const beginChat = () => {
    chatStore.getChatWith(author.value?.username as string)
        .then(() => {
            router.push('/messages')
                .then(() => chatStore.$patch({ chatDialog: true}))
        })
        .catch(() => {
            appStore.$patch({ erorrs: {
                active: true,
                message: 'failed to begin a chat'
            }})
        })
}
</script>


<template>
    <div class="d-sm-flex justify-center py-2 w-100">
        <div class="d-flex justify-center">
            <span class="text-h6 mr-3">
                {{ author?.username }}
            </span>
        </div>

        <div v-if="isAuthenticated" class="d-flex justify-center">

            <v-btn
                @click="subscribe"
                :disabled="disabledBtn"
                size="small"
                :loading="loading"
                color="primary"
                append-icon="mdi-progress-star"
            >
                {{ hasSubscriber(currentUser?.username) ? 'unsubscribe' : 'subscribe' }}
            </v-btn>

            <v-btn
                @click="beginChat"
                :disabled="disabledBtn"
                size="small"
                append-icon="mdi-chat"
                :loading="loading"
                color="info"
                class="ml-3"
            >
                begin chat
            </v-btn>
        </div>
    </div>
</template>