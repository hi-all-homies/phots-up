<script setup lang="ts">
import type { Post } from '@/types/Post';
import PostMenu from './PostMenu.vue';

defineProps<{
    post: Post
}>()
</script>


<template>
   <v-card  width="80%">

      <v-card-item>
         <v-card-title>
            <span style="cursor: grabbing;" class="text-body-1">
               {{ post.author.username }}
            </span>
         </v-card-title>
         <v-card-subtitle>{{ post.created }}</v-card-subtitle>

         <template v-slot:prepend>
            <v-avatar size="56" style="cursor: grabbing;">
               <v-img v-if="post.author.avatarUrl" :src="post.author.avatarUrl"></v-img>
               <span v-else>
                  {{ post.author.username.substring(0,2).toUpperCase() }}
               </span>
            </v-avatar>

         </template>

         <template v-slot:append>
            <PostMenu :post="post"/>
         </template>
      </v-card-item>

      <div class="d-flex flex-no-wrap justify-space-between">
         <v-img class="mx-4 rounded" width="70%" cover :src="post.imageUrl"></v-img>

         <v-card-text class="text-body-1">{{ post.content }}</v-card-text>
      </div>

      <v-card-actions>
         <v-btn>likes</v-btn>
         <div class="d-flex justify-center w-100">
            <v-btn>comments</v-btn>
         </div>
      </v-card-actions>

   </v-card>
</template>