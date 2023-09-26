<script setup lang="ts">
import { useChatStore } from '@/store/chats';
import { MutationType, storeToRefs } from 'pinia';
import AuthorAvatar from '../AuthorAvatar.vue';
import { computed, ref, onMounted, ComponentPublicInstance, nextTick} from 'vue';
import { ws } from '@/plugins/ws';
import type { Message } from '@/types/Message';
import { StompSubscription, messageCallbackType } from '@stomp/stompjs';
import MessageList from './MessageList.vue';


const payload = ref('')
const rules = {
    length: (val: string) => val.length > 2 && val.length < 301 || 'Min length - 3, max - 300'
}
const disabled = computed(() => payload.value.length > 2 && payload.value.length < 301)
const loading = ref(false)

const chatStore = useChatStore()
const { chatDialog, openedChat } = storeToRefs(chatStore)

const closeChat = () => {
    payload.value = ''
    chatStore.$patch({ chatDialog: false })
}


const messageList = ref<ComponentPublicInstance | null>(null)
const scrollToBottom = () => {
    nextTick(() => {
        let el = messageList.value?.$el.lastElementChild
        el?.scrollIntoView({behavior: 'smooth'})
    })
}

const msgHandler: messageCallbackType = msg => {
    openedChat.value?.messages.push(JSON.parse(msg.body))
    scrollToBottom()
}

const subscription = ref<StompSubscription>()

onMounted(() => {
    chatStore.$subscribe(mutation => {
        if (mutation.type === MutationType.patchObject){

            if (mutation.payload.chatDialog){
                const chatId = openedChat.value?.chat.chatIdentity
                subscription.value = ws.subscribeToChat(msgHandler, chatId)
                scrollToBottom()
            }
            else {
                subscription.value?.unsubscribe()
            }
        }
    })
})

const sendMessage = () => {
    const currentChat = openedChat.value?.chat

    if (disabled.value && currentChat){

        const message: Message = {
            id: null,
            created: null,
            chatIdentity: currentChat.chatIdentity,
            receiver: currentChat.receiver.username,
            sender: currentChat.sender.username,
            payload: payload.value
        }

        ws.sendMessage(message)
        payload.value = ''
    }
}
</script>


<template>
    <v-dialog v-model="chatDialog" fullscreen persistent scrollable transition="dialog-top-transition">
        <v-card>
           <v-toolbar density="comfortable">
                <v-app-bar-nav-icon @click="closeChat" icon="mdi-arrow-left-bold">
                </v-app-bar-nav-icon>
                <AuthorAvatar :author="openedChat?.chat.receiver"/>
                <v-toolbar-title>
                    <span class="text-body-1">Chat with {{ openedChat?.chat.receiver.username }}</span>
                </v-toolbar-title>
           </v-toolbar>

            <v-card-text>
              <MessageList ref="messageList" :messages="openedChat?.messages"/>
            </v-card-text>

            <v-card-actions>
                <v-text-field v-model="payload"
                    @keyup.enter="sendMessage"
                    :rules="[rules.length]"
                    placeholder="type your message"
                    density="compact"
                    variant="outlined">

                    <template v-slot:append>
                        <v-btn @click="sendMessage"
                            :disabled="!disabled"
                            :loading="loading"
                            append-icon="mdi-send"
                            color="primary">
                            send
                        </v-btn>
                    </template>

                </v-text-field>
            </v-card-actions>

        </v-card>
    </v-dialog>    
</template>