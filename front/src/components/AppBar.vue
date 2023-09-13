<script lang="ts" setup>
import UserAvatar from '@/components/UserAvatar.vue'
import { useAppStore } from '@/store/app';

const appStore = useAppStore()

</script>


<template>
    <v-app-bar elevation="5" scroll-behavior="hide" scroll-threshold="150">

        <template v-slot:prepend>
            <v-app-bar-nav-icon @click="appStore.toggleSideBar"></v-app-bar-nav-icon>
            
            <v-app-bar-title>Actions</v-app-bar-title>
                
            <v-btn to="/" prepend-icon="mdi-home" size="large" variant="plain">
                Home
            </v-btn>
        </template>

        <template v-slot:append>
            <UserAvatar/>        
        </template>

    </v-app-bar>
</template>