import { _GettersTree, defineStore } from 'pinia'
import type { Author } from '@/types/Author'
import { http } from '@/plugins/http'
import { avaUtils } from '@/plugins/avatar-utils'

interface AuthorState {
  author: Author | undefined
}

interface Getters extends _GettersTree<AuthorState>{
    initials(): string | undefined,
    avatarBgColor(): string | undefined
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
            return avaUtils.getInitials(this.author)
        },

        avatarBgColor(){
            return avaUtils.getBgColor(this.author)
        }
    },

    actions: {
        async findByUsername(username: string){
            let resp = await http.get(`api/author/${username}`)
            this.author = resp.data
        }
    }
})