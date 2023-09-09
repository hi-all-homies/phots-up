<script setup lang="ts">
import { useAuthorStore } from '@/store/author';
import { storeToRefs } from 'pinia';

const { isAuthenticated,currentUser, getAvaBackColor, getInitials } = storeToRefs(useAuthorStore())

withDefaults(defineProps<{ avatarSize?: string }>(), {
    avatarSize: 'x-large'
})

</script>


<template>
    <div v-if="isAuthenticated">
        <span class="ma-3">{{ currentUser.username }}</span>

        <v-avatar :size="avatarSize" :color="getAvaBackColor">

            <v-img v-if="currentUser.avatarUrl" alt="avatar" :src="currentUser.avatarUrl"></v-img>
            
            <span v-else>{{ getInitials }}</span>
        </v-avatar>
    </div>
</template>