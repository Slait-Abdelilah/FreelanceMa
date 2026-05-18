<template>
  <div class="min-h-screen bg-white text-ink flex flex-col">
    <NavBar :show-pricing="route.meta.showPricing === true" />
    <div class="flex-1">
      <RouterView />
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import NavBar from '@/components/layout/NavBar.vue'

const route = useRoute()
</script>
