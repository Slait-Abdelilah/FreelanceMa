<template>
  <div class="min-h-screen bg-cream flex flex-col items-center justify-center px-6 text-center">
    <div class="text-9xl font-bold text-brand-500 leading-none mb-4">404</div>
    <h1 class="text-3xl font-bold text-ink mb-3">Page introuvable</h1>
    <p class="text-ink-soft text-lg mb-8 max-w-sm">
      La page que vous cherchez n'existe pas ou a été déplacée.
    </p>
    <div class="flex gap-3 flex-wrap justify-center">
      <RouterLink to="/"
        class="bg-ink text-white px-6 py-3 rounded-full font-semibold hover:bg-brand-500 transition">
        Retour à l'accueil
      </RouterLink>
      <button @click="$router.back()"
        class="border-2 border-ink px-6 py-3 rounded-full font-semibold hover:bg-ink hover:text-white transition">
        Page précédente
      </button>
    </div>
  </div>
</template>
