<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { useAuthorStore } from '@/store/author';
import { useChatStore } from '@/store/chats';
import type { Author } from '@/types/Author';
import { ref, computed } from 'vue';
import AuthorAvatar from '../AuthorAvatar.vue';


const search = ref('')
const isDisabled = computed(() => search.value.length > 2)
const loading = ref(false)


const appStore = useAppStore()
const authorStore = useAuthorStore()

const foundUsers = ref([] as Author[])
const menuBtn = ref<HTMLButtonElement | null>(null)

const findChats = () => {
    foundUsers.value = []
    loading.value = true

    authorStore.findByUsernameLike(search.value)
        .then(users => {
            foundUsers.value = users
            loading.value = false

            if (foundUsers.value.length > 0){
                menuBtn.value?.click()
            }
        })
        .catch(() => {
            appStore.$patch({erorrs: {
                active: true,
                message: 'failed to find chats'
            }})
            loading.value = false
        })
}


const chatStore = useChatStore()

const openChat = (username: string) => {
    foundUsers.value = []
    chatStore.getChatWith(username)
        .then(() => {
            console.log(JSON.stringify(chatStore.openedChat))
        })
        .catch(() => {
            appStore.$patch({erorrs: {
                active: true,
                message: 'failed to get chat data'
            }})
        })
}
</script>


<template>
    <v-container class="my-3">
    <v-row>
        <v-spacer></v-spacer>
        <v-text-field v-model="search" placeholder="Find new chats by username"
            density="compact" variant="outlined">
        </v-text-field>
        
        <v-btn @click="findChats"
            :disabled="!isDisabled"
            :loading="loading"
            icon="mdi-account-search" flat
            class="mx-2">
        </v-btn>
        <v-spacer></v-spacer>
    </v-row>

    <v-row class="d-flex justify-center">
        <button id="menu-activator" ref="menuBtn"></button>
        <v-menu activator="#menu-activator" location="center">
            
            <v-list>
                <v-list-item v-for="(user, i) in foundUsers" :key="i" :value="i"
                    :title="user.username"
                    @click="openChat(user.username)">

                    <template v-slot:prepend>
                        <AuthorAvatar :author="user"/>
                    </template>

                </v-list-item>
            </v-list>
        </v-menu>
    </v-row>
    </v-container>
</template>