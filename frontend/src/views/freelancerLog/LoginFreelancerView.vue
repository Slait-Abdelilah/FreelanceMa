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
          <p class="text-[11px] font-semibold text-brand-100 uppercase tracking-[0.18em] mb-5">Espace Freelancer</p>
          <h2 class="text-[38px] font-extrabold leading-[1.15] tracking-tight mb-4">
            Développez votre<br>
            <span class="text-brand-100">activité freelance.</span>
          </h2>
          <p class="text-gray-400 text-[15px] leading-relaxed max-w-[300px]">
            Accédez à vos missions en cours, candidatures et revenus depuis votre tableau de bord.
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

    <!-- ============ CÔTÉ DROIT ============ -->
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
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Espace Freelancer</div>
          <h1 class="text-3xl font-bold text-ink mb-2">Connectez-vous</h1>
          <p class="text-ink-soft">
            Pas encore de compte ?
            <RouterLink to="/register/freelancer" class="text-brand-500 font-semibold hover:underline">S'inscrire gratuitement</RouterLink>
          </p>
        </div>

        <LoginForm role="FREELANCER" />

      </div>
    </div>

  </div>
</template>

<script setup>
import LoginForm from '@/components/auth/LoginForm.vue'

const features = [
  {
    label: 'Missions ciblées sur vos compétences',
    sub: 'Alertes personnalisées selon votre profil',
    icon: 'M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z',
  },
  {
    label: 'Paiement garanti à chaque livraison',
    sub: 'Système escrow — votre travail toujours rémunéré',
    icon: 'M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z',
  },
  {
    label: 'Profil visible par des milliers de clients',
    sub: 'Portfolio, avis, et compétences mis en avant',
    icon: 'M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z',
  },
]

const stats = [
  { value: '10K+', label: 'Freelancers' },
  { value: '3K+',  label: 'Clients actifs' },
  { value: '4.8★', label: 'Note moyenne' },
]
</script>
