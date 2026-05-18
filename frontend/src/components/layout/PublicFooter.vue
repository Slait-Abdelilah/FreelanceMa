<template>
  <footer class="bg-ink text-white py-12">
    <div class="max-w-7xl mx-auto px-6 flex items-center justify-between flex-wrap gap-6">
      <div class="flex items-center gap-2">
        <svg class="w-8 h-8 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
          <circle cx="12" cy="12" r="10"/>
          <circle cx="12" cy="12" r="4" fill="#001E00"/>
        </svg>
        <span class="font-bold">FreelanceMa</span>
      </div>
      <div class="flex gap-6 text-sm text-gray-400">
        <RouterLink to="/how-it-works" class="hover:text-white transition">Comment ça marche</RouterLink>
        <RouterLink to="/categories" class="hover:text-white transition">Catégories</RouterLink>
        <RouterLink to="/faq" class="hover:text-white transition">FAQ</RouterLink>
      </div>
      <div class="text-sm text-gray-400">© 2025 FreelanceMa</div>
    </div>
  </footer>
</template>
