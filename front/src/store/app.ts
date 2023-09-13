import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  
  state: () => ({
    sideBarOpen: false,
    page: 0,
    pageSize: 7,
    postDialog: false,

    editDialog: {
      active: false,
      postId: -1,
      content: ''
    },

    erorrs: {
      message: '',
      active: false
    }
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