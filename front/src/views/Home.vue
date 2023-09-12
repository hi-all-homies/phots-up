<script lang="ts" setup>
import PostList from '@/components/PostList.vue';
import { usePostStore } from '@/store/post';
import{ useAppStore } from '@/store/app'
import { watchEffect } from 'vue';
import { useHandleScroll } from '@/composables/handleScroll'

const postStore = usePostStore()
const { posts, findAll } = postStore

const appStore = useAppStore()

watchEffect(() => findAll(appStore.page, appStore.pageSize))

const action = () => appStore.incrementPage()

const cleanUp = () => {
    postStore.$reset()
    appStore.$patch({ page: 0 })
}

useHandleScroll(action, cleanUp)
</script>


<template>
  <PostList :posts="posts"/>
</template>