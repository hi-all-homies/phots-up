<script setup lang="ts">
import { usePostStore } from '@/store/post';
import type { Post } from '@/types/Post';
import { ref } from 'vue';
import { useAppStore } from '@/store/app';
import AuthorAvatar from './AuthorAvatar.vue';

const props = defineProps<{
    post: Post
}>()

const comment = ref('')
const rules = {
    required: (val: string) => val && val.length > 4 && val.length <= 200 || 'Min 5 length, max - 200'
}
const validated = ref(false)
const loading = ref(false)

const postStore = usePostStore()
const appStore = useAppStore()

function addComment(){
    loading.value = true
    postStore.addComment(props.post.id, comment.value)
        .then(() => {
            loading.value = false
            comment.value = ''
        })
        .catch(() => {
            appStore.$patch({erorrs: {
                active: true,
                message: 'failed to leave a comment'
            }})
            loading.value = false
        })
}
</script>


<template>
    <v-expansion-panels>
    <v-expansion-panel>
        <v-expansion-panel-title>
            Comments
            <template v-slot:actions="{ expanded }">
                <v-icon :icon="expanded ? 'mdi-lead-pencil' : 'mdi-comment-plus'"></v-icon>
            </template>
        </v-expansion-panel-title>

        <v-expansion-panel-text>
            <v-container >
            <v-row justify="center">
            <v-virtual-scroll max-height="450" :items="post.comments">
                <template v-slot:default="{ item }">
                
                <v-list-item class="my-2" :title="item.author + ':'" :subtitle="item.created">
                    <span class="text-body-2">{{ item.content }}</span>

                    <template v-slot:prepend>
                        <AuthorAvatar :username="item.author"/>    
                    </template>

                    <template v-slot:append>
                        <v-btn color="#C62828" size="small"
                            :to="`/profile/${item.author}`"
                            icon="mdi-open-in-new">
                        </v-btn>
                    </template>
                </v-list-item>
                </template>
            </v-virtual-scroll>
            </v-row>

            <v-form v-model="validated" @submit.prevent class="mt-7">
                <v-row>
                    <v-text-field v-model="comment"
                        :rules="[rules.required]"
                        placeholder="write a comment"
                        variant="outlined" density="compact">

                        <template v-slot:append>
                            <v-btn @click="addComment"
                                :disabled="!validated"
                                :loading="loading"
                                variant="text" color="primary">
                                Submit
                            </v-btn>
                        </template>
                    </v-text-field>

                </v-row>
            </v-form>
            </v-container>
        </v-expansion-panel-text>

    </v-expansion-panel>
    </v-expansion-panels>
</template>