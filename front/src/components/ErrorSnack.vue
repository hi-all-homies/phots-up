<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { storeToRefs } from 'pinia';

const appStore = useAppStore()
const { erorrs } = storeToRefs(appStore)

function hideSnack(){
    appStore.$patch({ erorrs: { active: false, message: ''} })
}
</script>


<template>
    <v-snackbar v-model="erorrs.active" :timeout="2500" color="error" location="top end">
        {{ erorrs.message }}
        <template v-slot:actions>
            <v-btn icon="mdi-window-close" @click="hideSnack"></v-btn>
        </template>
    </v-snackbar>
</template>