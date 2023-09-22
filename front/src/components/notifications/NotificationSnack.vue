<script setup lang="ts">
import AuthorAvatar from '@/components/AuthorAvatar.vue';
import { useChatStore } from '@/store/chats';
import { type Author } from '@/types/Author';
import { ref } from 'vue';
import { useRouter } from 'vue-router';


const isOpen = ref(true)

const props = defineProps<{
    causer: Author,
    message: string,
    snackId: number,
    margin: any
}>()

const emit = defineEmits<{
    (e: 'unstack', id: number): void
}>()

const unstack = (ind: number) =>
    emit('unstack', ind)

const chatStore = useChatStore()
const router = useRouter()
const goTo = (username: string) => {
    chatStore.$patch({ chatDialog: false })
    router.push(`/profile/${username}`)
    unstack(-1)
}
</script>


<template>
    <v-snackbar v-model="isOpen" @update:model-value="unstack(-1)" location="top end"
        :style="margin" color="secondary">
        
        <div class="d-flex align-center">
            <AuthorAvatar :author="causer" style="cursor: grabbing;" @click="goTo(causer.username)"/>
            <span class="mx-2">{{ message }}</span>
        </div>

        <template v-slot:actions>
            <v-btn @click="unstack(snackId)" icon="mdi-window-close" color="error"></v-btn>
        </template>
    </v-snackbar>
</template>