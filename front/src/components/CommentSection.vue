<script setup lang="ts">
import { usePostStore } from '@/store/post';
import type { Post } from '@/types/Post';
import { ref } from 'vue';
import { avaUtils } from '@/plugins/avatar-utils';

const props = defineProps<{
    post: Post
}>()

const comment = ref('')
const rules = {
    required: (val: string) => val && val.length > 4 || 'Min 5 length.'
}
const validated = ref(false)
const loading = ref(false)

const postStore = usePostStore()

function addComment(){
    loading.value = true
    postStore.addComment(props.post.id, comment.value)
        .then(() => {
            loading.value = false
            comment.value = '' })
        //TODO: error handling
        .catch(() => console.log('error at comment'))
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
                        <v-avatar color="warning">
                            {{ avaUtils.getInitials(undefined, item.author) }}
                        </v-avatar>
                    </template>

                    <template v-slot:append>
                        <v-btn color="#C62828" size="small"
                            @click=""
                            icon="mdi-open-in-new">
                        </v-btn>
                    </template>
                </v-list-item>
                </template>
            </v-virtual-scroll>
            </v-row>

            <v-row class="my-3">
                <v-divider></v-divider>
            </v-row>

            <v-form v-model="validated" @submit.prevent>
                <v-row justify-lg="end" align="center" justify-sm="center">
                    <v-col cols="6">

                        <v-text-field v-model="comment"
                            :rules="[rules.required]"
                            placeholder="write a comment"
                            variant="outlined" density="compact">
                        </v-text-field>

                    </v-col>
                    <v-col cols="1">

                        <v-btn @click="addComment"
                            :disabled="!validated"
                            :loading="loading"
                            variant="text" color="primary">
                            Submit
                        </v-btn>

                    </v-col>
                </v-row>
            </v-form>
            </v-container>
        </v-expansion-panel-text>

    </v-expansion-panel>
    </v-expansion-panels>
</template>