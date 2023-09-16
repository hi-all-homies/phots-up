<script setup lang="ts">
import { useAuthorStore } from '@/store/author';
import { storeToRefs } from 'pinia';
import { onBeforeMount } from 'vue';
import { useRoute, onBeforeRouteUpdate } from 'vue-router';
import UpdateAvatar from '@/components/UpdateAvatar.vue';
import { useDisplay } from 'vuetify/lib/framework.mjs';
import { computed } from 'vue';
import { useUserStore } from '@/store/user';
import { useAppStore } from '@/store/app';
import SubscriptionList from '@/components/SubscriptionList.vue';
import { ref } from 'vue';

const route = useRoute()

const authorStore = useAuthorStore()
const { author, initials, avatarBgColor, hasSubscriber } = storeToRefs(authorStore)


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
const { currentUser, isAuthenticated } = storeToRefs(userStore)
const appStore = useAppStore()

function subscribe(){
    userStore.subscribe(author.value?.username as string)
        .then(val => {
            if (author.value && currentUser.value){

                if (val){
                    author.value?.subscribers.push(currentUser.value)
                }
                else {
                    let ind = author.value?.subscribers
                        .findIndex(s => s.id === currentUser.value?.id)
                
                    author.value?.subscribers.splice(ind, 1)
                }
            }
        })
        .catch(() => {
            appStore.$patch({ erorrs: {
                active: true,
                message: 'failed to subscribe'
            }})
        })
}

const subscribersDialog = ref(false)
const subscriptionsDialog = ref(false)
</script>


<template>
    <v-container>
        <v-row class="d-flex justify-center py-2">
            <span class="text-h6 mx-4">
                {{ author?.username }}
            </span>
            <v-btn v-if="isAuthenticated" :disabled="author?.id === currentUser?.id" size="small"
                @click="subscribe" color="primary" append-icon="mdi-progress-star">
                {{ hasSubscriber(currentUser?.username) ? 'unsubscribe' : 'subscribe' }}
            </v-btn>
        </v-row>

        <v-row class="d-flex justify-center py-2">
            <v-avatar :size="avaSize" :color="avatarBgColor">
                <v-img v-if="author?.avatarUrl" :src="author?.avatarUrl"></v-img>
                <span v-else class="text-h4">{{ initials }}</span>
            </v-avatar>
        </v-row>

        <UpdateAvatar/>

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