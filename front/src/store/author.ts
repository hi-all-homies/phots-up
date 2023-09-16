import { _GettersTree, defineStore } from 'pinia'
import type { Author } from '@/types/Author'
import { http } from '@/plugins/http'
import { useAvatarUtils } from '@/composables/avatarUtils'

interface AuthorState {
  author: Author | undefined
}

interface Getters extends _GettersTree<AuthorState>{
    initials(): string | undefined,
    avatarBgColor(): string | undefined,

    hasSubscriber: (state: AuthorState) => (username: string | undefined) => boolean
}

interface Actions {
    findByUsername(username: string): Promise<void>
}

export const useAuthorStore = defineStore<'author', AuthorState, Getters, Actions>('author',{
    state: () => ({
        author: undefined
    }),

    getters: {
        initials(){
            const { initials } = useAvatarUtils(this.author)
            return initials.value
        },

        avatarBgColor(){
            const { bgColor } = useAvatarUtils(this.author)
            return bgColor.value
        },

        hasSubscriber(state) {
            return username =>
                state.author?.subscribers.find(a => a.username === username) ? true : false
        },
    },

    actions: {
        async findByUsername(username: string){
            let resp = await http.get(`api/author/${username}`)
            this.author = resp.data
        }
    }
})