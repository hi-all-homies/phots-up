<script setup lang="ts">
import { ref } from 'vue';
import { useAuthorStore } from '@/store/author';
import { useUserStore } from '@/store/user';
import { storeToRefs } from 'pinia';
import { useAppStore } from '@/store/app';

const avatar = ref<File[]>([])
const rules = {
    required:(val: File[]) => val.length > 0 && val[0].size <= 1500000 || 'Max image size - 1,5 mb'
}
const validated = ref(false)
const loading = ref(false)


const appStore = useAppStore()

const authorStore = useAuthorStore()
const { author } = storeToRefs(authorStore)

const userStore = useUserStore()
const { currentUser } = storeToRefs(userStore)

function update(){
    loading.value = true
    userStore.changeAvatar(avatar.value[0])
        .then(() => {
            authorStore.$patch({ author: {
                avatarUrl: currentUser.value?.avatarUrl
            }})
            loading.value = false
            avatar.value = []
        })
        .catch(() => {
            appStore.$patch({erorrs: {
                active: true,
                message: 'failed to change an avatar image'
            }})
            loading.value = false
        })
}
</script>


<template>
    <v-row v-if="currentUser?.id === author?.id">
        <v-col class="d-flex justify-center py-2">
            <v-form v-model="validated" class="w-50">

                <v-file-input variant="outlined" density="compact" label="avatar image"
                    v-model="avatar"
                    :rules="[rules.required]"
                    prepend-icon="mdi-account-circle"
                    accept="image/png, image/jpeg, image/bmp" show-size>
                </v-file-input>

                <div class="d-flex justify-end">
                    <v-btn color="primary" icon="mdi-cloud-upload" size="large" variant="outlined"
                        @click="update"
                        :disabled="!validated"
                        :loading="loading">
                    </v-btn>
                </div>
            </v-form>
        </v-col>
    </v-row>
</template>