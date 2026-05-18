<template>
  <div class="min-h-screen bg-cream flex">

    <!-- ============ CÔTÉ GAUCHE — VISUEL ============ -->
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
          <p class="text-[11px] font-semibold text-brand-100 uppercase tracking-[0.18em] mb-5">Inscription Freelancer</p>
          <h2 class="text-[38px] font-extrabold leading-[1.15] tracking-tight mb-4">
            Lancez votre<br>
            <span class="text-brand-100">carrière freelance.</span>
          </h2>
          <p class="text-gray-400 text-[15px] leading-relaxed max-w-[300px]">
            Rejoignez des milliers de freelancers marocains qui développent leur activité sur FreelanceMa.
          </p>
        </div>

        <!-- Benefits -->
        <div class="space-y-3">
          <div v-for="b in benefits" :key="b.label" class="flex items-start gap-4 bg-white/[0.03] border border-white/[0.07] rounded-xl p-4">
            <div class="w-8 h-8 rounded-lg bg-brand-500/10 border border-brand-500/15 flex items-center justify-center flex-shrink-0">
              <svg class="w-4 h-4 text-brand-100" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" :d="b.icon"/>
              </svg>
            </div>
            <div>
              <p class="text-[13px] font-semibold text-white leading-snug">{{ b.label }}</p>
              <p class="text-[11.5px] text-gray-500 mt-0.5">{{ b.sub }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Testimonial -->
      <div class="relative z-10 border border-white/[0.1] rounded-2xl p-5 bg-white/[0.03]">
        <div class="flex gap-0.5 mb-3">
          <svg v-for="n in 5" :key="n" class="w-3.5 h-3.5 text-brand-100" fill="currentColor" viewBox="0 0 20 20">
            <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
          </svg>
        </div>
        <p class="text-gray-300 text-[13px] leading-relaxed mb-4">
          "En 3 mois sur FreelanceMa, j'ai remplacé mon salaire. La plateforme est sérieuse, les clients aussi."
        </p>
        <div class="flex items-center gap-3">
          <div class="w-9 h-9 rounded-full bg-brand-500/10 border border-brand-500/20 flex items-center justify-center text-[12px] font-bold text-brand-100">
            YB
          </div>
          <div>
            <p class="text-[12.5px] font-semibold text-white">Yasmine Benali</p>
            <p class="text-[11.5px] text-gray-500">Designer UI/UX, Casablanca</p>
          </div>
        </div>
      </div>

    </div>

    <!-- ============ CÔTÉ DROIT — FORMULAIRE ============ -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 lg:p-12">
      <div class="w-full max-w-md animate-fade-up">

        <div class="mb-8">
          <RouterLink to="/" class="flex items-center gap-2 mb-8 lg:hidden">
            <svg class="w-8 h-8 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
              <circle cx="12" cy="12" r="10"/>
              <circle cx="12" cy="12" r="4" fill="white"/>
            </svg>
            <span class="font-bold">FreelanceMa</span>
          </RouterLink>
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Inscription freelancer</div>
          <h1 class="text-3xl font-bold mb-2">Devenez freelancer</h1>
          <p class="text-ink-soft">
            Déjà inscrit ?
            <RouterLink to="/login/freelancer" class="text-brand-500 font-semibold hover:underline">Se connecter</RouterLink>
          </p>
        </div>

        <RegisterForm role="FREELANCER" />

      </div>
    </div>

  </div>
</template>

<script setup>
import RegisterForm from '@/components/auth/RegisterForm.vue'

const benefits = [
  {
    label: 'Accès aux meilleures missions',
    sub: 'Offres filtrées selon vos compétences et disponibilité',
    icon: 'M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z',
  },
  {
    label: 'Paiement sécurisé et garanti',
    sub: 'Escrow intégré — vous êtes payé à chaque livraison',
    icon: 'M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z',
  },
  {
    label: 'Portfolio professionnel inclus',
    sub: 'Mettez en avant vos réalisations et attirez des clients',
    icon: 'M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10',
  },
]
</script>
