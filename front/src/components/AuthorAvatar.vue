<script setup lang="ts">
import type { Author } from '@/types/Author';
import { useAvatarUtils } from '@/composables/avatarUtils';

const props = defineProps<{
    author?: Author,
    username?: string,
    size?: string | number
    style?: any
}>()

const { initials, bgColor } = useAvatarUtils(props.author, props.username)
</script>


<template>
    <v-avatar :color="bgColor" :size="size" :style="style">
        <v-img v-if="author?.avatarUrl" :src="author.avatarUrl" alt="img..."></v-img>
        <span v-else>{{ initials }}</span>
    </v-avatar>
</template>