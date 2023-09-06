import { defineStore } from 'pinia'

export const useAuthorStore = defineStore('author', {
  state: () => ({
    currentUser: {
      username: 'user',
      authenticated: true,
      img: 'https://avatars0.githubusercontent.com/u/9064066?v=4&s=460'
    }
  }),
  
  
  getters: {
    isAuthenticated(): boolean {
      return this.currentUser.authenticated
    },  
    
    getInitials(): string {
      return this.currentUser.username.substring(0,2).toUpperCase()
    },  
    
    getAvaBackColor(): string {
      return this.currentUser.img ? '' : 'success'
    }
  },
  
  
  actions: {

  }
})