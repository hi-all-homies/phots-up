<script lang="ts" setup>
import UserAvatar from '@/components/UserAvatar.vue'
import { useAppStore } from '@/store/app';

const appStore = useAppStore()

</script>


<template>
    <v-app-bar elevation="5" scroll-behavior="hide" scroll-threshold="300">

        <v-app-bar-nav-icon @click="appStore.toggleSideBar"></v-app-bar-nav-icon>

        <v-app-bar-title>Actions</v-app-bar-title>

        <template v-slot:append>
            <UserAvatar/>        
        </template>

    </v-app-bar>
</template>