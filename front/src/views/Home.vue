<script lang="ts" setup>
import PostList from '@/components/PostList.vue';
import type { Post } from '@/types/Post';

//TODO write posts store
let posts: Post[] = []

</script>


<template>
  <PostList :posts="posts"/>
</template>