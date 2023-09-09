<script lang="ts" setup>
import AppBar from './views/AppBar.vue';
import AppFooter from './views/AppFooter.vue';
import SideBar from './views/SideBar.vue';
import { useAuthorStore } from '@/store/author';
import { onMounted } from 'vue';

const { getUser} = useAuthorStore()
onMounted(getUser)

</script>


<template>
  <v-app>
    <AppBar/>

    <SideBar/>

    <v-main>
      <router-view />
    </v-main>

    <AppFooter/>
  </v-app>
</template>