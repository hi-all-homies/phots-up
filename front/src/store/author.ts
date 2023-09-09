import { defineStore } from 'pinia'
import { http } from '@/plugins/http'
import type { Author } from '@/types/Author'


export const useAuthorStore = defineStore('author', {
  state: () => ({
    currentUser: {} as Author,

    authenticated: false,
    //TODO - separate author store into 2
    requestedAuthor: {} as Author
  }),
  
  
  getters: {
    isAuthenticated(): boolean {
      return this.authenticated
    },  
    
    getInitials(): string {
      return this.currentUser.username.substring(0,2).toUpperCase()
    },  
    
    getAvaBackColor(): string {
      return this.currentUser.avatarUrl ? '' : 'success'
    }
  },
  
  
  actions: {
    async getUser(){
      let resp = await http.get('api/author')
      this.currentUser = resp.data as Author
      this.authenticated = true
    },

    async findByUsername(username: string){
      let resp = await http.get(`api/author/${username}`)
      this.requestedAuthor = resp.data as Author
    },

    async changeAvatar(avatar: File){
      let formData = new FormData()
      formData.append('avatar', avatar)

      let resp = await http.patch('api/author/ava', formData, { responseType: 'text' })
      this.currentUser.avatarUrl = resp.data
    },

    async login(username: string, password: string){
      let encodedCred = btoa(`${username}:${password}`)
      let header = `Basic ${encodedCred}`

      let resp = await http.get('api/author', { headers: {Authorization: header} })
      this.currentUser = resp.data as Author

      await http.get('csrf')
      this.authenticated = true
    },

    async signUp(username: string, password: string){
      const signUpReq = {
        username: username,
        password: password
      }

      await http.post('/signup', JSON.stringify(signUpReq), { headers: {"Content-Type": 'application/json'} })
    }
  }
})