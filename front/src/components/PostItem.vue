<script setup lang="ts">
import type { Post } from '@/types/Post';
import PostMenu from './PostMenu.vue';
import CommentSection from '@/components/CommentSection.vue'
import { usePostStore } from '@/store/post';
import { useUserStore } from '@/store/user';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useAppStore } from '@/store/app';
import ConfirmDialog from './ConfirmDialog.vue';
import { useRouter } from 'vue-router';
import AuthorAvatar from './AuthorAvatar.vue';


const props = defineProps<{
    post: Post
}>()


const postStore = usePostStore()
const { meLiked, likeCount, commentCount } = storeToRefs(postStore)
const {currentUser} = useUserStore()

function addLike(){
   if (currentUser){
      let thisPost = postStore.posts.find(p => p.id === props.post.id)
      postStore.addLike(props.post.id)
      .then(resp => {
         if (resp){
            thisPost?.likes.push(currentUser)
         }
         else {
            let likers = thisPost?.likes.filter(u => u.id !== currentUser.id)
            if (thisPost && likers) thisPost.likes = likers
         }
      })
   }
}

const appStore = useAppStore()
const deleteReq = ref(false)

function remove(){
    postStore.delete(props.post.id)
        .catch(() => {
            appStore.$patch({ erorrs: {
                message: 'failed to delete a post',
                active: true
            }})
        })
}

const router = useRouter()
function showProfile(){
   router.push(`/profile/${props.post.author.username}`)
}
</script>


<template>
   <v-card width="80%">

      <v-card-item>
         <v-card-title>
            <span @click="showProfile" style="cursor: grabbing;" class="text-body-1">
               {{ post.author.username }}
            </span>
         </v-card-title>
         <v-card-subtitle>{{ post.created }}</v-card-subtitle>

         <template v-slot:prepend>
            <AuthorAvatar
               :author="post.author"
               @click="showProfile"
               size="56" style="cursor: grabbing;"
            />
         </template>

         <template v-slot:append>
            <PostMenu :post="post" @delete="deleteReq = true"/>
         </template>
      </v-card-item>

      <ConfirmDialog v-model="deleteReq" eventName="deletePost" @deletePost="remove"/>

      <div class="d-block">
         <div class="d-flex justify-center">
            <v-img id="post-img" class="rounded flex-0-1"
               cover
               width="70%"
               :src="post.imageUrl">
            </v-img>
         </div>
         
         <v-card-text class="text-body-1">{{ post.content }}</v-card-text>
      </div>

      <v-card-actions>
         <v-container>
            <v-row class="d-flex align-center">
               <v-btn @click="addLike" :icon="meLiked(post.id) ? 'mdi-heart' : 'mdi-heart-outline'"
                  color="error">
               </v-btn>
                  {{ likeCount(post.id) }}

               <div class="mx-5">
                  <v-icon>mdi-comment-text-multiple</v-icon>
                  {{ commentCount(post.id) }}
               </div>
            </v-row>

            <v-row>
               <CommentSection :post="post"/>
            </v-row>
         </v-container>
      </v-card-actions>

   </v-card>
</template>

<style scoped>
#post-img:hover {
   border-style: solid;
   border-color: #9C27B0;
   cursor: pointer;
}
</style>