<script setup lang="ts">
import { useAuthorStore } from '@/store/author';
import { storeToRefs } from 'pinia';
import { onBeforeMount } from 'vue';
import { useRoute, onBeforeRouteUpdate } from 'vue-router';
import UpdateAvatar from '@/components/UpdateAvatar.vue';
import { useDisplay } from 'vuetify/lib/framework.mjs';
import { computed } from 'vue';

const route = useRoute()

const authorStore = useAuthorStore()
const { author, initials, avatarBgColor } = storeToRefs(authorStore)


onBeforeMount(() => {
    let username = route.params.username as string
    authorStore.findByUsername(username)
})

onBeforeRouteUpdate((to, from) => {
    if (to.params.username !== from.params.username){
        let username = to.params.username as string
        authorStore.findByUsername(username)
    }
})


const display = useDisplay()

const avaSize = computed(() => {
    return display.lgAndUp.value ? 500 : display.md.value ? 400 : 300
})
</script>


<template>
    <v-container>
        <v-row class="d-flex justify-center py-2">
            <span class="text-h6">
                {{ author?.username }}
            </span>
        </v-row>

        <v-row class="d-flex justify-center py-2">
            <v-avatar :size="avaSize" :color="avatarBgColor">
                <v-img v-if="author?.avatarUrl" :src="author?.avatarUrl"></v-img>
                <span v-else class="text-h4">{{ initials }}</span>
            </v-avatar>
        </v-row>

        <UpdateAvatar/>

        <v-row class="py-2">
            <v-col class="d-flex justify-center">Subcsriptions: {{ author?.subscriptions.length }}</v-col>
            <v-col class="d-flex justify-center">Subscribers: {{ author?.subscribers.length }}</v-col>
        </v-row>
    </v-container>
</template>