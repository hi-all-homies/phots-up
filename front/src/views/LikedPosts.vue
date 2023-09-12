<script setup lang="ts">
import PostList from '@/components/PostList.vue';
import { usePostStore } from '@/store/post';
import { watchEffect } from 'vue';
import { useAppStore } from '@/store/app';
import { useHandleScroll } from '@/composables/handleScroll';

const appStore = useAppStore()

const postStore = usePostStore()
const { posts, findLiked }  = postStore

watchEffect(() => findLiked(appStore.page, appStore.pageSize))

const action = () => appStore.incrementPage()

const cleanUp = () => {
    postStore.$reset()
    appStore.$patch({ page: 0 })
}

useHandleScroll(action, cleanUp)
</script>


<template>
    <div class="d-flex justify-center w-100 text-h6 my-2">
        Posts you've liked:
    </div>
    <PostList :posts="posts"/>
</template>
