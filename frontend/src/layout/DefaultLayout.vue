<script setup>
import AppHeader from '@/components/AppHeader.vue';
</script>

<template>
  <div>
    <div class="wrapper d-flex flex-column min-vh-100">
      <AppHeader />
      <div class="body flex-grow-1 h-svh">
        <router-view />
      </div>
    </div>
  </div>
</template>
