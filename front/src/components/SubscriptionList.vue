<script setup lang="ts">
import type { Author } from '@/types/Author';
import { useRouter } from 'vue-router';
import AuthorAvatar from './AuthorAvatar.vue';


defineProps<{
    authors: Author[],
    title: string
}>()

const emit = defineEmits<{
    (e: 'close'): void
}>()

const router = useRouter()

function showProfile(username: string){
    router.push(`/profile/${username}`)
    emit('close')
}


</script>


<template>
    <v-dialog width="auto" :scrim="false">
         <v-card>
            <v-card-item :title="title"></v-card-item>

            <v-card-text style="height: 400px;">
                <v-list lines="two">

                    <v-list-item v-for="author in authors" rounded    
                        :title="author.username"
                        @click="showProfile(author.username)"
                        :key="author.id">

                        <template v-slot:prepend>
                            <AuthorAvatar :author="author"/>
                        </template>

                        <template v-slot:append>
                            <v-icon icon="mdi-open-in-new"></v-icon>
                        </template>

                    </v-list-item>
                </v-list>
            </v-card-text>

            <v-card-actions class="d-flex justify-end">
               <v-btn @click="emit('close')">close</v-btn>
            </v-card-actions>
         </v-card>
      </v-dialog>
</template>