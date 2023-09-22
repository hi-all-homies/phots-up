import { _GettersTree, defineStore } from 'pinia'
import type { Post } from '@/types/Post'
import { http } from '@/plugins/http'
import { useUserStore } from './user'

interface PostState {
  posts: Post[],
  currentPost: Post | undefined
}

interface Getters extends _GettersTree<PostState>{
  meLiked: (state: PostState) => (postId: number) => boolean | undefined,
  likeCount: (state: PostState) => (postId: number) => number | undefined,
  commentCount: (state: PostState) => (postId: number) => number | undefined
}

interface Actions {
  findById(id: number): Promise<void>,

  findAll(page: number, size: number): Promise<void>,

  findLiked(page: number, size: number): Promise<void>,

  save(content: string, image: File, shouldInsert?: boolean): Promise<void>,

  addComment(postId: number, content: string): Promise<void>,

  addLike(postId: number): Promise<boolean>,

  update(postId: number, content: string): Promise<void>,

  delete(postId: number): Promise<void>
}

export const usePostStore = defineStore<'post', PostState, Getters, Actions>('post', {
  state: () => ({
    posts: [],
    currentPost: undefined
  }),

  getters: {

    meLiked(state) {
      return (postId) => {
        let result: boolean | undefined
        let { currentUser } = useUserStore()

        if (currentUser){
          let post = state.posts.find(p => p.id === postId)
          let user = post?.likes.filter(u => u.id === currentUser?.id)
          result = user && user.length > 0
        }
        return result
      }
    },

    likeCount(state) {
        return (postId) => state.posts.find(p => p.id === postId)?.likes.length
    },

    commentCount(state) {
        return (postId) => state.posts.find(p => p.id === postId)?.comments.length
    }
  },

  actions: {
    async findById(id) {
      let resp = await http.get(`api/post/${id}`)
      this.currentPost = resp.data
    },


    async findAll(page, size){
      let params = {
        page: page,
        size: size
      }
      let resp = await http.get('api/post', { params: params })
      this.posts.push(...resp.data)
    },


    async findLiked(page, size) {
        let params = {
          page: page,
          size: size
        }

        let resp = await http.get('api/post/liked', { params: params })
        this.posts.push(...resp.data)
    },


    async save(content, image, shouldInsert) {
        let formData = new FormData()
        formData.append('content', content)
        formData.append('image', image)

        let resp = await http.post('api/post', formData)

        if (shouldInsert)
          this.posts.unshift(resp.data)
    },


    async addComment(postId, content) {
      let comment = JSON.stringify( { content: content } )

      let resp = await http.post(`api/post/${postId}/comment`, comment,
        { headers: { 'Content-Type': 'application/json' }})
      
      this.posts.find(post => post.id === postId)?.comments.push(resp.data)
    },


    async addLike(postId) {
        let resp = await http.patch(`api/post/${postId}/like`)
        return resp.data
    },


    async update(postId, content) {
      let updRequest = JSON.stringify({ content: content })

      await http.put(`api/post/${postId}`, updRequest,
          { headers: { 'Content-Type': 'application/json' }})

      let updPost = this.posts.find(p => p.id === postId)
      
      if (updPost) updPost.content = content
    },


    async delete(postId) {
      await http.delete(`api/post/${postId}`)
      let ind = this.posts.findIndex(post => post.id === postId)
      this.posts.splice(ind, 1)
    },
  }
})