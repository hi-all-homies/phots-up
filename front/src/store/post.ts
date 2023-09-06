import { defineStore } from 'pinia'
import type { Post } from '@/types/Post'

export const usePostStore = defineStore('post', {
  state: () => ({
    posts: [] as Post[],
    currentPost: {} as Post
  }),

  getters: {},

  actions: {}
})