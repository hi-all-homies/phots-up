<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { useAuthorStore } from '@/store/author';
import { storeToRefs } from 'pinia';
import { useTheme } from 'vuetify'

const appStore = useAppStore()

const theme = useTheme()

function toggleTheme () {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

const { isAuthenticated } = storeToRefs(useAuthorStore())


</script>


<template>
<v-navigation-drawer v-model="appStore.sideBarOpen">
  <v-list>

    <v-list-item prepend-icon="mdi-pen-plus" title="new post"
      @click="" value="new_post" :disabled="!isAuthenticated">
    </v-list-item>

    <v-list-item prepend-icon="mdi-bookmark-check" title="liked posts"
      @click="" value="liked_posts" :disabled="!isAuthenticated">
    </v-list-item>

    <v-list-item prepend-icon="mdi-cog-box" title="profile"
      @click="" value="profile" :disabled="!isAuthenticated">
    </v-list-item>

    <v-list-item v-if="!isAuthenticated" prepend-icon="mdi-login" title="log in"
      to="/login" value="login">
    </v-list-item>

    <v-list-item v-else prepend-icon="mdi-logout-variant" title="log out"
      @click="" value="logout">
    </v-list-item>
    
    <v-list-item prepend-icon="mdi-invert-colors" title="toggle theme"
      @click="toggleTheme" value="toggle_theme">
    </v-list-item>

  </v-list>
</v-navigation-drawer>
</template>