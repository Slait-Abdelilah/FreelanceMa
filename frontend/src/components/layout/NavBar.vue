<template>
  <nav class="border-b border-gray-100 bg-white/95 backdrop-blur sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">

      <!-- Logo -->
      <RouterLink to="/" class="flex items-center gap-2.5">
        <svg class="w-9 h-9 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
          <circle cx="12" cy="12" r="10"/>
          <circle cx="12" cy="12" r="4" fill="white"/>
        </svg>
        <span class="text-xl font-bold">FreelanceMa</span>
      </RouterLink>

      <!-- Liens desktop -->
      <div class="hidden md:flex items-center gap-8">
        <RouterLink to="/how-it-works"
          class="text-sm font-medium hover:text-brand-500 transition"
          active-class="text-brand-500">
          Comment ça marche
        </RouterLink>
        <RouterLink to="/categories"
          class="text-sm font-medium hover:text-brand-500 transition"
          active-class="text-brand-500">
          Catégories
        </RouterLink>
        <RouterLink to="/faq"
          class="text-sm font-medium hover:text-brand-500 transition"
          active-class="text-brand-500">
          FAQ
        </RouterLink>
        <a v-if="showPricing" href="#pricing" class="text-sm font-medium hover:text-brand-500 transition">
          Tarifs
        </a>
      </div>

      <!-- Actions desktop + burger mobile -->
      <div class="flex items-center gap-3">
        <RouterLink to="/login/client" class="hidden md:block text-sm font-semibold hover:text-brand-500 transition">
          Se connecter
        </RouterLink>
        <RouterLink to="/register/client"
          class="hidden md:block bg-ink text-white px-5 py-2.5 rounded-full text-sm font-semibold hover:bg-brand-500 transition">
          {{ ctaLabel }}
        </RouterLink>

        <!-- Burger mobile -->
        <button @click="mobileOpen = !mobileOpen"
                class="md:hidden p-2 rounded-lg hover:bg-gray-100 transition"
                aria-label="Menu">
          <svg v-if="!mobileOpen" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
          <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- Menu mobile -->
    <div v-if="mobileOpen" class="md:hidden border-t border-gray-100 bg-white px-6 py-4 space-y-3">
      <RouterLink to="/how-it-works" @click="mobileOpen = false"
        class="block text-sm font-medium hover:text-brand-500 transition py-1"
        active-class="text-brand-500">
        Comment ça marche
      </RouterLink>
      <RouterLink to="/categories" @click="mobileOpen = false"
        class="block text-sm font-medium hover:text-brand-500 transition py-1"
        active-class="text-brand-500">
        Catégories
      </RouterLink>
      <RouterLink to="/faq" @click="mobileOpen = false"
        class="block text-sm font-medium hover:text-brand-500 transition py-1"
        active-class="text-brand-500">
        FAQ
      </RouterLink>
      <a v-if="showPricing" href="#pricing" @click="mobileOpen = false"
        class="block text-sm font-medium hover:text-brand-500 transition py-1">
        Tarifs
      </a>
      <div class="pt-3 border-t border-gray-100 flex items-center gap-3">
        <RouterLink to="/login/client" @click="mobileOpen = false"
          class="text-sm font-semibold hover:text-brand-500 transition">
          Se connecter
        </RouterLink>
        <RouterLink to="/register/client" @click="mobileOpen = false"
          class="bg-ink text-white px-4 py-2 rounded-full text-sm font-semibold hover:bg-brand-500 transition">
          {{ ctaLabel }}
        </RouterLink>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  showPricing: { type: Boolean, default: false },
  ctaLabel:    { type: String,  default: 'Commencer' },
})

const mobileOpen = ref(false)
</script>
