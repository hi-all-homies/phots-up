<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { usePostStore } from '@/store/post';
import { ref } from 'vue';
import { useDisplay } from 'vuetify'

const { mobile } = useDisplay()

const appStore = useAppStore()

const files = ref<File[]>([])
const content = ref('')

const rules = {
    length: (val: string) => val.length >= 5 && val.length <= 600 || 'Min length - 5, max - 600',
    requiredImg: (val: File[]) => val.length > 0 || 'Image field cannot be empty',
    size: (val: File[]) => !val.length || val[0].size <= 3000000 || 'Image cannot be greater than 3mb'
}
const validated = ref(false)


const loading = ref(false)
const postStore = usePostStore()


function save(){
    loading.value = true

    postStore.save(content.value, files.value[0])
        .then(() => {
            loading.value = false
            content.value = ''
            files.value = []
            appStore.$patch({ postDialog: false})
        })
        .catch(() => {
            loading.value = false
            appStore.$patch({ erorrs: {
                active: true,
                message: 'failed to publish a post'
            }})
        })
}
</script>


<template>
    <v-dialog class="mx-auto" v-model="appStore.postDialog" :fullscreen="mobile" persistent transition="dialog-top-transition">
        <v-card>

            <v-card-item title="new post"></v-card-item>
            
            <v-card-text>
                <v-form @submit.prevent v-model="validated">
                    
                    <v-file-input accept="image/png, image/jpeg, image/bmp"
                        prepend-icon="mdi-camera" show-size clearable
                        label="pick an image" variant="outlined" density="compact"
                        :rules="[rules.requiredImg, rules.size]"
                        v-model="files">
                    </v-file-input>

                    <v-textarea placeholder="post content" variant="outlined"
                        prepend-icon="mdi-comment" density="compact"
                        auto-grow counter
                        clearable
                        :rules="[rules.length]"
                        v-model="content">
                    </v-textarea>
                </v-form>
            </v-card-text>
            
            <v-card-actions class="d-flex justify-end">
                <v-btn @click="appStore.togglePostDialog">
                    close
                </v-btn>
                
                <v-btn
                    @click="save"
                    :disabled="!validated"
                    color="primary" :loading="loading">
                    publish
                </v-btn>
            </v-card-actions>

        </v-card>
    </v-dialog>
</template>