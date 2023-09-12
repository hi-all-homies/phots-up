<script setup lang="ts">
import { useRoute } from 'vue-router';

const route = useRoute()

</script>


<template>
    profile of {{ route.params.username }}
</template>