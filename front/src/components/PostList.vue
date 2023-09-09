<script setup lang="ts">
import type { Post } from '@/types/Post';
import PostItem from './PostItem.vue';

defineProps<{
    posts: Post[]
}>()
</script>


<template>
   <v-container>

    <v-row v-for="post in posts" :key="post.id" justify="center" class="my-4">
        <PostItem :post="post"/>
    </v-row>

   </v-container>
</template>