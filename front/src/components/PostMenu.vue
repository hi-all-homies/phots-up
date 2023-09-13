<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { usePostStore } from '@/store/post';
import { useUserStore } from '@/store/user';
import type { Post } from '@/types/Post';
import { computed } from 'vue';

const props = defineProps<{
    post: Post
}>()

const { currentUser } = useUserStore()

const isOwner = computed(() =>
    props.post.author.id === currentUser?.id)



const appStore = useAppStore()
const postStore = usePostStore()

function remove(){
    postStore.delete(props.post.id)
        .catch(() => {
            appStore.$patch({ erorrs: {
                message: 'failed to delete a post',
                active: true
            }})
        })
}

function openEditDialog(){
    appStore.$patch({ editDialog: {
        active: true,
        postId: props.post.id,
        content: props.post.content
    }})
}
</script>


<template>
    <v-menu>
        <template v-slot:activator="{ props }">
            <v-btn
                icon="mdi-dots-vertical"
                variant="flat"
                v-bind="props">
            </v-btn>
        </template>

        <v-list>
            <v-list-item @click="openEditDialog" value="edit" :disabled="!isOwner" prepend-icon="mdi-comment-edit-outline">
                <v-list-item-title>edit post</v-list-item-title>
            </v-list-item>

            <v-list-item @click="remove" value="delete" :disabled="!isOwner">
                <v-list-item-title>delete post</v-list-item-title>

                <template v-slot:prepend>
                    <v-icon color="error">mdi-delete-outline</v-icon>
                </template>
            </v-list-item>

        </v-list>
    </v-menu>
</template>