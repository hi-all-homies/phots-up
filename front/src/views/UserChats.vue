<script setup lang="ts">
import ConversationSearch from '@/components/messaging/ConversationSearch.vue'
import { useChatStore } from '@/store/chats';
import { storeToRefs } from 'pinia';
import AuthorAvatar from '@/components/AuthorAvatar.vue';
import { onMounted } from 'vue';


const chatStore = useChatStore()
const { chats } = storeToRefs(chatStore)

onMounted(() => chatStore.getUserChats())

const openChat = (username: string) => {

}
</script>


<template>
    <ConversationSearch/>

    <v-container class="d-flex justify-center">
        
    <v-card class="w-100">
        <v-list>
            <v-list-item v-for="(chat, i) in chats" :key="i" lines="two"
                @click="openChat(chat.receiver.username)">

                <v-list-item-title class="text-overline">
                    {{ chat.receiver.username }}
                </v-list-item-title>

                <template v-slot:prepend>
                    <AuthorAvatar :author="chat.receiver" size="56"/>
                </template>

                <template v-slot:append>
                    <v-btn :to="`/profile/${chat.receiver.username}`" icon="mdi-open-in-new"
                        variant="tonal" color="success">
                    </v-btn>
                </template>
            </v-list-item>
        </v-list>
    </v-card>
    </v-container>
</template>