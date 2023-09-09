<script setup lang="ts">
import type { Post } from '@/types/Post';

const props = defineProps<{
    post: Post
}>()

function edit(){
    console.log('editing post ' + props.post.id)
}

function remove(){
    console.log('deleting post ' + props.post.id)
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
            <v-list-item @click="edit" value="edit" prepend-icon="mdi-comment-edit-outline">
                <v-list-item-title>edit post</v-list-item-title>
            </v-list-item>

            <v-list-item @click="remove" value="delete">
                <v-list-item-title>delete post</v-list-item-title>

                <template v-slot:prepend>
                    <v-icon color="error">mdi-delete-outline</v-icon>
                </template>
            </v-list-item>

        </v-list>
    </v-menu>
</template>