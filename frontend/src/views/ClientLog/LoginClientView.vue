<template>
  <div class="min-h-screen bg-cream flex">

    <!-- ============ CÔTÉ GAUCHE ============ -->
    <div class="hidden lg:flex lg:w-1/2 bg-ink text-white flex-col justify-between p-12 relative overflow-hidden">

      <!-- Dot grid -->
      <div class="absolute inset-0"
           style="background-image: radial-gradient(circle, rgba(20,168,0,0.09) 1px, transparent 1px); background-size: 28px 28px;"></div>

      <!-- Top accent -->
      <div class="absolute top-0 left-0 right-0 h-px bg-gradient-to-r from-transparent via-brand-500/40 to-transparent"></div>

      <!-- Right edge line -->
      <div class="absolute top-0 right-0 bottom-0 w-px bg-white/[0.06]"></div>

      <!-- Logo -->
      <RouterLink to="/" class="flex items-center gap-3 relative z-10">
        <div class="w-9 h-9 rounded-xl bg-brand-500/10 border border-brand-500/20 flex items-center justify-center">
          <svg class="w-[18px] h-[18px] text-brand-100" fill="none" stroke="currentColor" stroke-width="2.2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M13 10V3L4 14h7v7l9-11h-7z"/>
          </svg>
        </div>
        <span class="text-[17px] font-bold tracking-tight">FreelanceMa</span>
      </RouterLink>

      <!-- Main -->
      <div class="relative z-10 space-y-10">
        <div>
          <p class="text-[11px] font-semibold text-brand-100 uppercase tracking-[0.18em] mb-5">Espace Client</p>
          <h2 class="text-[38px] font-extrabold leading-[1.15] tracking-tight mb-4">
            Gérez vos projets,<br>
            <span class="text-brand-100">simplement.</span>
          </h2>
          <p class="text-gray-400 text-[15px] leading-relaxed max-w-[300px]">
            Publiez une offre en quelques minutes et recevez des propositions de freelancers qualifiés.
          </p>
        </div>

        <!-- Features -->
        <div class="space-y-4">
          <div v-for="f in features" :key="f.label" class="flex items-start gap-4">
            <div class="w-8 h-8 rounded-lg bg-brand-500/10 border border-brand-500/15 flex items-center justify-center flex-shrink-0 mt-0.5">
              <svg class="w-4 h-4 text-brand-100" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" :d="f.icon"/>
              </svg>
            </div>
            <div>
              <p class="text-[13.5px] font-semibold text-white leading-snug">{{ f.label }}</p>
              <p class="text-[12px] text-gray-500 mt-0.5">{{ f.sub }}</p>
            </div>
          </div>
        </div>

        <!-- Stats -->
        <div class="grid grid-cols-3 gap-px bg-white/[0.07] rounded-xl overflow-hidden">
          <div v-for="s in stats" :key="s.label" class="bg-white/[0.03] py-4 text-center">
            <div class="text-2xl font-bold text-brand-100 leading-none">{{ s.value }}</div>
            <div class="text-[11px] text-gray-500 mt-1.5">{{ s.label }}</div>
          </div>
        </div>
      </div>

      <!-- Bottom -->
      <div class="relative z-10 flex items-center gap-3 pt-6 border-t border-white/[0.08]">
        <div class="w-8 h-8 rounded-lg bg-white/[0.06] border border-white/[0.1] flex items-center justify-center">
          <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/>
          </svg>
        </div>
        <div>
          <p class="text-[12.5px] font-semibold text-gray-300">Connexion chiffrée SSL 256-bit</p>
          <p class="text-[11.5px] text-gray-600">Vos données sont protégées</p>
        </div>
      </div>

    </div>

    <!-- ============ CÔTÉ DROIT — FORMULAIRE ============ -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 lg:p-12">
      <div class="w-full max-w-md animate-fade-up">

        <RouterLink to="/" class="flex items-center gap-2 mb-8 lg:hidden">
          <svg class="w-8 h-8 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
            <circle cx="12" cy="12" r="10"/>
            <circle cx="12" cy="12" r="4" fill="white"/>
          </svg>
          <span class="font-bold text-ink">FreelanceMa</span>
        </RouterLink>

        <div class="mb-8">
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Espace Client</div>
          <h1 class="text-3xl font-bold text-ink mb-2">Connectez-vous</h1>
          <p class="text-ink-soft">
            Pas encore de compte ?
            <RouterLink to="/register/client" class="text-brand-500 font-semibold hover:underline ml-1">S'inscrire gratuitement</RouterLink>
          </p>
        </div>

        <LoginForm role="CLIENT" />

      </div>
    </div>

  </div>
</template>

<script setup>
import LoginForm from '@/components/auth/LoginForm.vue'

const features = [
  {
    label: '10 000+ freelancers vérifiés',
    sub: 'Filtrés par compétences et évaluations',
    icon: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z',
  },
  {
    label: 'Paiements sécurisés par escrow',
    sub: 'Votre budget est protégé jusqu\'à la validation',
    icon: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z',
  },
  {
    label: 'Suivi des projets en temps réel',
    sub: 'Tableau de bord centralisé et notifications',
    icon: 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z',
  },
]

const stats = [
  { value: '10K+', label: 'Freelancers' },
  { value: '5K+',  label: 'Projets livrés' },
  { value: '98%',  label: 'Satisfaction' },
]
</script>
