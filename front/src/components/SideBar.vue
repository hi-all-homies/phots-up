<script setup lang="ts">
import { ws } from '@/plugins/ws';
import { useAppStore } from '@/store/app';
import { useUserStore } from '@/store/user';
import { storeToRefs } from 'pinia';
import { useTheme } from 'vuetify'

const appStore = useAppStore()

const theme = useTheme()

function toggleTheme () {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
const userStore = useUserStore()
const { isAuthenticated, currentUser } = storeToRefs(userStore)

function logOut(){
  userStore.logout()
    .then(() => ws.disconnect())
}
</script>


<template>
<v-navigation-drawer v-model="appStore.sideBarOpen">
  <v-list>

    <v-list-item prepend-icon="mdi-pen-plus" title="new post"
      @click="appStore.togglePostDialog" value="new_post" :disabled="!isAuthenticated">
    </v-list-item>

    <v-list-item prepend-icon="mdi-bookmark-check" title="liked posts"
      to="/liked" value="liked_posts" :disabled="!isAuthenticated">
    </v-list-item>

    <v-list-item prepend-icon="mdi-forum" title="messages"
      to="/messages" value="messages" :disabled="!isAuthenticated">
    </v-list-item>

    <v-list-item prepend-icon="mdi-cog-box" title="profile"
      :to="`/profile/${currentUser?.username}`" value="profile" :disabled="!isAuthenticated">
    </v-list-item>

    <v-list-item v-if="!isAuthenticated" prepend-icon="mdi-login" title="log in"
      to="/login" value="login">
    </v-list-item>

    <v-list-item v-else prepend-icon="mdi-logout-variant" title="log out"
      @click="logOut" value="logout">
    </v-list-item>
    
    <v-list-item prepend-icon="mdi-invert-colors" title="toggle theme"
      @click="toggleTheme" value="toggle_theme">
    </v-list-item>

  </v-list>
</v-navigation-drawer>
</template>