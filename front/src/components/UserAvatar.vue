<script setup lang="ts">
import { useUserStore } from '@/store/user';
import { storeToRefs } from 'pinia';

const { currentUser, isAuthenticated, initials, avatarBgColor } = storeToRefs(useUserStore())

withDefaults(defineProps<{ avatarSize?: string }>(), {
    avatarSize: 'x-large'
})

</script>


<template>
    <div v-if="isAuthenticated">
        <span class="ma-3">{{ currentUser?.username }}</span>

        <v-avatar :size="avatarSize" :color="avatarBgColor">

            <v-img v-if="currentUser?.avatarUrl" alt="avatar" :src="currentUser.avatarUrl"></v-img>
            
            <span v-else>{{ initials }}</span>
        </v-avatar>
    </div>
</template>