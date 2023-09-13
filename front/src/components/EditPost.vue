<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { usePostStore } from '@/store/post';
import { storeToRefs } from 'pinia';
import { onMounted } from 'vue';
import { ref } from 'vue';
import { useDisplay } from 'vuetify'

const { mobile } = useDisplay()

const rules = {
    length: (val: string) => val.length >= 5 && val.length <= 600 || 'Min length - 5, max - 600'
}
const validated = ref(false)
const loading = ref(false)


const appStore = useAppStore()
const postStore = usePostStore()
const { editDialog } = storeToRefs(appStore)
const content = ref('')

onMounted(() => {
    appStore.$subscribe(state => {
        if (editDialog.value.active) content.value = editDialog.value.content
    })
})

function close(){
    appStore.$patch({ editDialog: {
        active: false,
        postId: -1,
        content: ''
    }})
}

function edit(){
    postStore.update(appStore.editDialog.postId, content.value)
        .then(() => close())
        .catch(() => {
            appStore.$patch({ erorrs: {
                active: true,
                message: 'failed to update a post'
            }})
        })
}
</script>


<template>
    <v-dialog class="mx-auto" v-model="appStore.editDialog.active" :fullscreen="mobile" persistent transition="dialog-top-transition">
        <v-card>

            <v-card-item title="Editing"></v-card-item>
            
            <v-card-text>
                <v-form @submit.prevent v-model="validated">

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
                <v-btn @click="close">
                    close
                </v-btn>
                
                <v-btn
                    @click="edit"
                    :disabled="!validated"
                    color="primary" :loading="loading">
                    save changes
                </v-btn>
            </v-card-actions>

        </v-card>
    </v-dialog>
</template>