<script setup lang="ts">
import { useAuthorStore } from '@/store/author';
import { storeToRefs } from 'pinia';
import { onBeforeMount } from 'vue';
import { useRoute, onBeforeRouteUpdate } from 'vue-router';
import UpdateAvatar from '@/components/profile/UpdateAvatar.vue';
import { useDisplay } from 'vuetify/lib/framework.mjs';
import { computed } from 'vue';
import { useUserStore } from '@/store/user';
import SubscriptionList from '@/components/profile/SubscriptionList.vue';
import { ref } from 'vue';
import ProfileActions from '@/components/profile/ProfileActions.vue';

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


const userStore = useUserStore()
const { currentUser } = storeToRefs(userStore)


const subscribersDialog = ref(false)
const subscriptionsDialog = ref(false)
</script>


<template>
    <v-container>
        <v-row>
            <ProfileActions :disabled-btn="currentUser?.id === author?.id"/>
        </v-row>

        <v-row class="d-flex justify-center py-2">
            <v-avatar :size="avaSize" :color="avatarBgColor">
                <v-img v-if="author?.avatarUrl" :src="author?.avatarUrl"></v-img>
                <span v-else class="text-h4">{{ initials }}</span>
            </v-avatar>
        </v-row>

        <UpdateAvatar v-if="author?.id === currentUser?.id"/>

        <v-row class="py-2">

            <v-col class="d-flex justify-center align-center">
                <v-btn @click="subscriptionsDialog=true" append-icon="mdi-open-in-app" color="info" variant="plain">
                    subscriptions
                </v-btn>
                <span class="text-h6 ml-3">{{ author?.subscriptions.length }}</span>
            </v-col>
            <SubscriptionList
                v-model="subscriptionsDialog"
                @close="subscriptionsDialog = false"
                :title="`${author?.username} subscriptions`"
                :authors="author ? author.subscriptions : []"/>

            <v-col class="d-flex justify-center align-center">
                <v-btn @click="subscribersDialog=true" append-icon="mdi-open-in-app" color="success" variant="plain">
                    subscribers
                </v-btn>
                <span class="text-h6 ml-3">{{ author?.subscribers.length }}</span>
            </v-col>
            <SubscriptionList
                v-model="subscribersDialog"
                @close="subscribersDialog = false"
                :title="`${author?.username} subscribers`"
                :authors="author ? author.subscribers : []"/>
        </v-row>
    </v-container>
</template>