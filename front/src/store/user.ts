import { _GettersTree, defineStore } from 'pinia'
import { http } from '@/plugins/http'
import type { Author } from '@/types/Author'
import { avaUtils } from '@/plugins/avatar-utils'

interface UserState {
  currentUser: Author | undefined
}

interface Getters extends _GettersTree<UserState>{
  isAuthenticated(): boolean,
  initials(): string | undefined,
  avatarBgColor(): string | undefined
}

interface Actions {
  getUser(): Promise<void>,
  changeAvatar(avatar: File): Promise<void>,
  login(username: string, password: string): Promise<void>,
  signUp(username: string, password: string): Promise<void>,
  logout(): Promise<void>

}

export const useUserStore = defineStore<'user', UserState, Getters, Actions>('user', {
  state: () => ({
    currentUser: undefined
  }),
  
  
  getters: {
    isAuthenticated(){
      return this.currentUser !== undefined
    },  
    
    initials(){
      return avaUtils.getInitials(this.currentUser)
    },  
    
    avatarBgColor(){
      return avaUtils.getBgColor(this.currentUser)
    }
  },
  
  
  actions: {
    async getUser(){
      let resp = await http.get('api/author')
      this.currentUser = resp.data
    },

    async changeAvatar(avatar: File){
      let formData = new FormData()
      formData.append('avatar', avatar)

      let resp = await http.patch('api/author/ava', formData, { responseType: 'text' })
      if (this.currentUser){
        this.currentUser.avatarUrl = resp.data
      }
    },

    async login(username: string, password: string){
      let encodedCred = btoa(`${username}:${password}`)
      let header = `Basic ${encodedCred}`

      let resp = await http.get('api/author', { headers: { Authorization: header } })
      this.currentUser = resp.data

      await http.get('csrf')
    },

    async signUp(username: string, password: string){
      const signUpReq = {
        username: username,
        password: password
      }

      await http.post('signup', JSON.stringify(signUpReq),{ headers:
        {'Content-Type': 'application/json'} })
    },

    async logout() {
      await http.post('logout')
      this.currentUser = undefined
    },
  }
})