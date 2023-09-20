import { _GettersTree, defineStore } from 'pinia'
import { http } from '@/plugins/http'
import type { Chat } from '@/types/Chat'
import type { ChatMessages } from '@/types/ChatMessages'


interface ChatState {
    chats: Chat[],
    openedChat: ChatMessages | undefined 
}

interface Actions {
    getUserChats(): Promise<void>,
    getChatWith(username: string): Promise<void>
}

export const useChatStore = defineStore<'chats', ChatState, {}, Actions>('chats', {
    state: () => ({
        chats: [],
        openedChat: undefined
    }),

    actions: {
        async getUserChats(){
            let resp = await http.get('/chats')
            this.chats = resp.data
        },

        async getChatWith(username) {
            let resp = await http.get(`/chats/${username}`)
            this.openedChat = resp.data

            let result = this.chats.find(chat =>
                chat.receiver.username === this.openedChat?.chat.receiver.username)
            
            if (!result){
                this.chats.push(resp.data.chat)
            }
        },
    }
})