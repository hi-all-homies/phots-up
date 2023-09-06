import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  
  state: () => ({
    sideBarOpen: false,
    page: 0
  }),


  getters: {
    getPage(): number{
      return this.page
    }
  },

  
  actions: {
    toggleSideBar(){
      this.sideBarOpen = !this.sideBarOpen
    },

    incrementPage(){
      this.page++
    }
  }
})