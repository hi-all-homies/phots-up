<script setup lang="ts">
import type { Message } from '@/types/Message';
import MessageItem from './MessageItem.vue';


defineProps<{
    messages?: Message[]
}>()

</script>


<template>
    <v-container>
        <MessageItem v-for="(message,i) in messages" :key="i"
            :message="message"/>
    </v-container>
</template>