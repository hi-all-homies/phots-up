<script lang="ts" setup>
import AppBar from './views/AppBar.vue';
import AppFooter from './views/AppFooter.vue';
import { useAppStore } from './store/app';
import { useTheme } from 'vuetify'

const theme = useTheme()

const appStore = useAppStore()

function toggleTheme () {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

</script>


<template>
  <v-app>
    <AppBar/>


<v-navigation-drawer v-model="appStore.sideBarOpen">
  <v-list>
    <v-list-item title="Navigation drawer">
      <v-btn @click="toggleTheme">toggle theme</v-btn>
    </v-list-item>
  </v-list>
</v-navigation-drawer>


<v-main>
  <router-view />
</v-main>

<AppFooter/>
  </v-app>
</template>