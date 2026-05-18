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
          <p class="text-[11px] font-semibold text-brand-100 uppercase tracking-[0.18em] mb-5">Inscription Client</p>
          <h2 class="text-[38px] font-extrabold leading-[1.15] tracking-tight mb-4">
            Trouvez le bon<br>
            <span class="text-brand-100">freelancer.</span>
          </h2>
          <p class="text-gray-400 text-[15px] leading-relaxed max-w-[300px]">
            De la publication de votre projet à la livraison finale, on vous accompagne à chaque étape.
          </p>
        </div>

        <!-- Steps -->
        <div class="space-y-0">
          <div v-for="(step, i) in steps" :key="i" class="flex gap-4">
            <div class="flex flex-col items-center">
              <div class="w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 text-[12px] font-bold"
                   :class="i === 0 ? 'bg-brand-500 text-white' : 'bg-white/[0.08] border border-white/[0.1] text-gray-400'">
                {{ i + 1 }}
              </div>
              <div v-if="i < steps.length - 1" class="w-px flex-1 my-1.5 bg-white/[0.08]"></div>
            </div>
            <div class="pb-6">
              <p class="text-[13.5px] font-semibold text-white leading-snug">{{ step.label }}</p>
              <p class="text-[12px] text-gray-500 mt-0.5">{{ step.sub }}</p>
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
          "En 48h, j'avais reçu 12 propositions sérieuses. Le freelancer retenu a livré un travail exceptionnel, dans les délais."
        </p>
        <div class="flex items-center gap-3">
          <div class="w-9 h-9 rounded-full bg-brand-500/10 border border-brand-500/20 flex items-center justify-center text-[12px] font-bold text-brand-100">
            HA
          </div>
          <div>
            <p class="text-[12.5px] font-semibold text-white">Hamza Alaoui</p>
            <p class="text-[11.5px] text-gray-500">CEO, Startup Rabat</p>
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
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Inscription client</div>
          <h1 class="text-3xl font-bold mb-2">Créez votre compte</h1>
          <p class="text-ink-soft">Déjà inscrit ? <RouterLink to="/login/client" class="text-brand-500 font-semibold hover:underline">Se connecter</RouterLink></p>
        </div>

        <RegisterForm role="CLIENT" />

      </div>
    </div>

  </div>
</template>

<script setup>
import RegisterForm from '@/components/auth/RegisterForm.vue'

const steps = [
  { label: 'Créez votre compte gratuitement', sub: 'Inscription en moins de 2 minutes' },
  { label: 'Publiez votre projet', sub: 'Décrivez vos besoins et votre budget' },
  { label: 'Recevez des propositions', sub: 'Les freelancers vous contactent directement' },
  { label: 'Validez et payez en sécurité', sub: 'Paiement libéré uniquement à votre validation' },
]
</script>
