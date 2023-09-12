import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  
  state: () => ({
    sideBarOpen: false,
    page: 0,
    pageSize: 7,
    postDialog: false
  }),

  
  actions: {
    toggleSideBar(){
      this.sideBarOpen = !this.sideBarOpen
    },

    togglePostDialog(){
      this.postDialog = !this.postDialog
    },

    incrementPage(){
      this.page++
    }
  }
})