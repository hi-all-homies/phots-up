<script lang="ts" setup>
import { useUserStore } from '@/store/user';
import AppBar from '@/components/AppBar.vue';
import AppFooter from '@/components/AppFooter.vue';
import SideBar from '@/components/SideBar.vue';
import { onMounted } from 'vue';
import AddPost from './components/AddPost.vue';

const { getUser } = useUserStore()
onMounted(() => {
  getUser()
    .then(() => console.log('authneticated'))
    .catch(() => console.log('error'))
})

</script>


<template>
  <v-app>
    <AppBar/>

    <SideBar/>

    <v-main>
      <router-view />
      <AddPost/>
    </v-main>

    <AppFooter/>
  </v-app>
</template>