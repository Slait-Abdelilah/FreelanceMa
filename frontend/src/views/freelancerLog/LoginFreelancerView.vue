<template>
  <div class="min-h-screen bg-cream flex">

    <!-- ============ CÔTÉ GAUCHE ============ -->
    <div class="hidden lg:flex lg:w-1/2 bg-ink text-white flex-col justify-between p-12 relative overflow-hidden">

      <div class="absolute top-0 right-0 w-96 h-96 bg-brand-500/20 rounded-full blur-3xl"></div>
      <div class="absolute bottom-0 left-0 w-96 h-96 bg-brand-500/10 rounded-full blur-3xl"></div>

      <RouterLink to="/" class="flex items-center gap-2.5 relative z-10">
        <svg class="w-9 h-9 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
          <circle cx="12" cy="12" r="10"/>
          <circle cx="12" cy="12" r="4" fill="#001E00"/>
        </svg>
        <span class="text-xl font-bold">FreelanceMa</span>
      </RouterLink>

      <div class="relative z-10">
        <div class="text-7xl font-bold leading-none mb-6">
          Bon retour<br>
          <span class="italic text-brand-500">freelancer</span>
        </div>
        <p class="text-gray-400 text-lg leading-relaxed mb-12 max-w-sm">
          Consultez vos missions, candidatures et revenus depuis votre tableau de bord.
        </p>

        <!-- activités -->
        <div class="space-y-3">
          <div v-for="(a, i) in activities" :key="i"
               class="flex items-center gap-3 bg-white/5 border border-white/10 rounded-2xl p-4 animate-fade-up"
               :style="{ animationDelay: (i * 0.1) + 's' }">
            <div class="text-2xl">{{ a.icon }}</div>
            <div class="flex-1">
              <div class="text-sm font-semibold">{{ a.title }}</div>
              <div class="text-xs text-gray-400">{{ a.time }}</div>
            </div>
            <span class="text-xs font-bold px-2 py-1 rounded-full"
                  :class="a.type === 'success' ? 'bg-brand-500/20 text-brand-400' : 'bg-white/10 text-gray-400'">
              {{ a.badge }}
            </span>
          </div>
        </div>
      </div>

      <div class="relative z-10 flex items-center gap-3">
        <div class="w-10 h-10 bg-brand-500/20 rounded-full flex items-center justify-center">
          <svg class="w-5 h-5 text-brand-500" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2z" clip-rule="evenodd"/>
          </svg>
        </div>
        <div>
          <div class="text-sm font-semibold">Connexion sécurisée SSL</div>
          <div class="text-xs text-gray-400">Vos données sont protégées</div>
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

        <div v-if="errorMessage"
             class="bg-red-50 border border-red-200 text-red-700 rounded-2xl p-4 mb-6 text-sm font-medium flex items-center gap-2">
          <svg class="w-4 h-4 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
          </svg>
          {{ errorMessage }}
        </div>

        <div class="space-y-4">

          <!-- email -->
          <div>
            <label class="block text-xs font-bold text-ink mb-1.5 uppercase tracking-wider">Email</label>
            <input
                v-model="form.email"
                type="email"
                placeholder="vous@exemple.ma"
                @keyup.enter="submit"
                class="w-full bg-white border-2 rounded-2xl px-4 py-3.5 text-sm outline-none transition"
                :class="errors.email ? 'border-red-400' : 'border-gray-200 focus:border-brand-500'"
            />
            <p v-if="errors.email" class="text-red-500 text-xs mt-1">{{ errors.email }}</p>
          </div>

          <!-- mot de passe -->
          <div>
            <div class="flex items-center justify-between mb-1.5">
              <label class="text-xs font-bold text-ink uppercase tracking-wider">Mot de passe</label>
              <RouterLink to="/forgot-password" class="text-xs text-brand-500 font-semibold hover:underline">
                Mot de passe oublié ?
              </RouterLink>
            </div>
            <div class="relative">
              <input
                  v-model="form.password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="Votre mot de passe"
                  @keyup.enter="submit"
                  class="w-full bg-white border-2 rounded-2xl px-4 py-3.5 text-sm outline-none transition pr-12"
                  :class="errors.password ? 'border-red-400' : 'border-gray-200 focus:border-brand-500'"
              />
              <button @click="showPassword = !showPassword" type="button"
                      class="absolute right-3 top-1/2 -translate-y-1/2 text-ink-soft hover:text-ink">
                <svg v-if="!showPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                </svg>
                <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M3 3l3.59 3.59"/>
                </svg>
              </button>
            </div>
            <p v-if="errors.password" class="text-red-500 text-xs mt-1">{{ errors.password }}</p>
          </div>

          <!-- rester connecté -->
          <label class="flex items-center gap-3 cursor-pointer">
            <input v-model="form.rememberMe" type="checkbox" class="w-4 h-4 accent-brand-500"/>
            <span class="text-sm text-ink-soft">Rester connecté pendant 30 jours</span>
          </label>

          <!-- bouton -->
          <button
              @click="submit"
              :disabled="loading"
              class="w-full bg-ink text-white py-4 rounded-2xl font-semibold hover:bg-brand-500 transition disabled:opacity-50 flex items-center justify-center gap-2 group mt-2">
            <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <span>{{ loading ? 'Connexion...' : 'Se connecter' }}</span>
            <svg v-if="!loading" class="w-4 h-4 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </button>

        </div>

        <div class="flex items-center gap-3 my-6">
          <div class="flex-1 h-px bg-gray-200"></div>
          <span class="text-xs text-ink-soft">ou</span>
          <div class="flex-1 h-px bg-gray-200"></div>
        </div>

        <div class="text-center">
          <p class="text-sm text-ink-soft">
            Vous êtes client ?
            <RouterLink to="/login/client" class="text-brand-500 font-semibold hover:underline ml-1">Connexion client</RouterLink>
          </p>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')

const form = ref({
  email: '',
  password: '',
  rememberMe: false,
})

const errors = ref({ email: '', password: '' })

const activities = [
  { icon: '🎯', title: 'Nouvelle mission disponible', time: 'il y a 15 min', badge: 'Nouveau', type: 'success' },
  { icon: '💬', title: 'Message d\'un client', time: 'il y a 2h', badge: 'Non lu', type: 'info' },
  { icon: '💰', title: 'Paiement reçu', time: 'hier', badge: '+3500 DH', type: 'success' },
]

const validate = () => {
  errors.value = { email: '', password: '' }
  let valid = true

  if (!form.value.email.trim()) { errors.value.email = "L'email est obligatoire"; valid = false }
  else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.value.email)) { errors.value.email = "Format invalide"; valid = false }
  if (!form.value.password) { errors.value.password = 'Le mot de passe est obligatoire'; valid = false }

  return valid
}

const submit = async () => {
  errorMessage.value = ''
  if (!validate()) return
  loading.value = true

  try {
    const response = await axios.post(`${API_URL}/api/auth/login`, {
      email: form.value.email,
      password: form.value.password,
    })

    const { token, refreshToken, role, email } = response.data

    // ===== VÉRIFICATION DU RÔLE =====
    if (role !== 'FREELANCER') {
      errorMessage.value = 'Email ou mot de passe incorrect'
      loading.value = false
      return
    }

    authStore.setAuth(token, { email, role }, refreshToken)
    router.push('/freelancer/dashboard')

  } catch (err) {
    const status = err.response?.status
    const errorMsg = err.response?.data?.message || err.response?.data || ''

    if (errorMsg.includes('PENDING_VERIFICATION') || errorMsg.includes('non activé')) {
      router.push({
        path: '/verify-account',
        query: { email: form.value.email, from: 'freelancer' }
      })
      return
    }

    if (errorMsg.includes('suspendu')) {
      errorMessage.value = 'Votre compte a été suspendu.'
      return
    }

    if (status === 401 || status === 400) {
      errorMessage.value = 'Email ou mot de passe incorrect'
    } else {
      errorMessage.value = errorMsg || 'Une erreur est survenue'
    }
  } finally {
    loading.value = false
  }
}

</script>