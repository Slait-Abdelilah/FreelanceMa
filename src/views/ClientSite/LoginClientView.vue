<template>
  <div class="min-h-screen bg-cream flex">

    <!-- ============ CÔTÉ GAUCHE ============ -->
    <div class="hidden lg:flex lg:w-1/2 bg-ink text-white flex-col justify-between p-12 relative overflow-hidden">

      <div class="absolute top-0 right-0 w-96 h-96 bg-brand-500/20 rounded-full blur-3xl"></div>
      <div class="absolute bottom-0 left-0 w-96 h-96 bg-brand-500/10 rounded-full blur-3xl"></div>

      <!-- logo -->
      <RouterLink to="/" class="flex items-center gap-2.5 relative z-10">
        <svg class="w-9 h-9 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
          <circle cx="12" cy="12" r="10"/>
          <circle cx="12" cy="12" r="4" fill="#001E00"/>
        </svg>
        <span class="text-xl font-bold">FreelanceMa</span>
      </RouterLink>

      <!-- contenu -->
      <div class="relative z-10">
        <div class="text-7xl font-bold leading-none mb-6">
          Bon retour<br><span class="italic text-brand-500">parmi nous</span>
        </div>
        <p class="text-gray-400 text-lg leading-relaxed mb-12 max-w-sm">
          Accédez à votre tableau de bord et gérez vos projets en cours.
        </p>

        <!-- activité récente -->
        <div class="space-y-3">
          <div v-for="(activity, i) in activities" :key="i"
               class="bg-white/5 border border-white/10 rounded-2xl p-4 flex items-center gap-3 animate-fade-up"
               :style="{ animationDelay: (i * 0.1) + 's' }">
            <div class="text-2xl">{{ activity.icon }}</div>
            <div>
              <div class="text-sm font-semibold">{{ activity.title }}</div>
              <div class="text-xs text-gray-400">{{ activity.time }}</div>
            </div>
            <div class="ml-auto">
              <span class="text-xs font-bold px-2 py-1 rounded-full"
                    :class="activity.type === 'success' ? 'bg-brand-500/20 text-brand-400' : 'bg-white/10 text-gray-400'">
                {{ activity.badge }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- sécurité -->
      <div class="relative z-10 flex items-center gap-3">
        <div class="w-10 h-10 bg-brand-500/20 rounded-full flex items-center justify-center">
          <svg class="w-5 h-5 text-brand-500" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd"/>
          </svg>
        </div>
        <div>
          <div class="text-sm font-semibold">Connexion sécurisée SSL</div>
          <div class="text-xs text-gray-400">Vos données sont chiffrées et protégées</div>
        </div>
      </div>

    </div>

    <!-- ============ CÔTÉ DROIT — FORMULAIRE ============ -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 lg:p-12">
      <div class="w-full max-w-md animate-fade-up">

        <!-- logo mobile -->
        <RouterLink to="/" class="flex items-center gap-2 mb-8 lg:hidden">
          <svg class="w-8 h-8 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
            <circle cx="12" cy="12" r="10"/>
            <circle cx="12" cy="12" r="4" fill="white"/>
          </svg>
          <span class="font-bold text-ink">FreelanceMa</span>
        </RouterLink>

        <!-- header -->
        <div class="mb-8">
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Espace Client</div>
          <h1 class="text-3xl font-bold text-ink mb-2">Connectez-vous</h1>
          <p class="text-ink-soft">
            Pas encore de compte ?
            <RouterLink to="/register/client" class="text-brand-500 font-semibold hover:underline ml-1">S'inscrire gratuitement</RouterLink>
          </p>
        </div>

        <!-- message erreur -->
        <div v-if="errorMessage"
             class="bg-red-50 border border-red-200 text-red-700 rounded-2xl p-4 mb-6 text-sm font-medium flex items-center gap-2 animate-fade-up">
          <svg class="w-4 h-4 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
          </svg>
          {{ errorMessage }}
        </div>

        <!-- FORMULAIRE -->
        <div class="space-y-4">

          <!-- email -->
          <div>
            <label class="block text-xs font-bold text-ink mb-1.5 uppercase tracking-wider">Email</label>
            <div class="relative">
              <input
                  v-model="form.email"
                  type="email"
                  placeholder="vous@entreprise.ma"
                  @focus="focusedField = 'email'"
                  @blur="focusedField = ''"
                  @keyup.enter="submit"
                  class="w-full border-2 rounded-2xl px-4 py-3.5 text-sm outline-none transition-all duration-200"
                  :class="errors.email
                  ? 'border-red-400'
                  : focusedField === 'email'
                    ? 'border-brand-500 ring-4 ring-brand-500/10 bg-white'
                    : 'border-gray-200 bg-white'"
              />
            </div>
            <p v-if="errors.email" class="text-red-500 text-xs mt-1 animate-fade-up">{{ errors.email }}</p>
          </div>

          <!-- mot de passe -->
                <div>
            <div class="flex items-center justify-between mb-1.5">
              <label class="text-xs font-bold text-ink uppercase tracking-wider">Mot de passe</label>
              <RouterLink to="/forgot-password" class="text-xs text-brand-500 font-semibold hover:underline">
                Mot de passe oublié ?
              </RouterLink>            </div>
            <div class="relative">
              <input
                  v-model="form.password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="Votre mot de passe"
                  @focus="focusedField = 'password'"
                  @blur="focusedField = ''"
                  @keyup.enter="submit"
                  class="w-full border-2 rounded-2xl px-4 py-3.5 text-sm outline-none transition-all duration-200 pr-12"
                  :class="errors.password
                  ? 'border-red-400'
                  : focusedField === 'password'
                    ? 'border-brand-500 ring-4 ring-brand-500/10 bg-white'
                    : 'border-gray-200 bg-white'"
              />
              <button @click="showPassword = !showPassword" type="button"
                      class="absolute right-3 top-1/2 -translate-y-1/2 text-ink-soft hover:text-ink transition">
                <svg v-if="!showPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                </svg>
                <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7"/>
                </svg>
              </button>
            </div>
            <p v-if="errors.password" class="text-red-500 text-xs mt-1 animate-fade-up">{{ errors.password }}</p>
          </div>

          <!-- rester connecté -->
          <label class="flex items-center gap-3 cursor-pointer">
            <input v-model="form.rememberMe" type="checkbox" class="w-4 h-4 accent-brand-500"/>
            <span class="text-sm text-ink-soft">Rester connecté pendant 30 jours</span>
          </label>

          <!-- bouton connexion -->
          <button
              @click="submit"
              :disabled="loading"
              class="w-full bg-ink text-white py-4 rounded-2xl font-semibold hover:bg-brand-500 transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 group mt-2">
            <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <span>{{ loading ? 'Connexion en cours...' : 'Se connecter' }}</span>
            <svg v-if="!loading" class="w-4 h-4 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </button>

        </div>

        <!-- séparateur -->
        <div class="flex items-center gap-3 my-6">
          <div class="flex-1 h-px bg-gray-200"></div>
          <span class="text-xs text-ink-soft font-medium">ou</span>
          <div class="flex-1 h-px bg-gray-200"></div>
        </div>

        <!-- lien freelancer -->
        <div class="text-center">
          <p class="text-sm text-ink-soft">
            Vous êtes freelancer ?
            <RouterLink to="/login/freelancer" class="text-brand-500 font-semibold hover:underline ml-1">Connexion freelancer</RouterLink>
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

const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')
const focusedField = ref('')

const form = ref({
  email: '',
  password: '',
  rememberMe: false,
})

const errors = ref({
  email: '',
  password: '',
})

const activities = [
  { icon: '📋', title: 'Nouveau projet publié', time: 'il y a 2 heures', badge: 'Actif', type: 'success' },
  { icon: '💬', title: '3 nouvelles propositions', time: 'il y a 5 heures', badge: 'À voir', type: 'info' },
  { icon: '✅', title: 'Livraison validée', time: 'hier', badge: 'Payé', type: 'success' },
]

// validation
const validate = () => {
  errors.value = { email: '', password: '' }
  let valid = true

  if (!form.value.email.trim()) {
    errors.value.email = "L'email est obligatoire"
    valid = false
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.value.email)) {
    errors.value.email = "Format email invalide"
    valid = false
  }

  if (!form.value.password) {
    errors.value.password = 'Le mot de passe est obligatoire'
    valid = false
  }

  return valid
}

// connexion
const submit = async () => {
  errorMessage.value = ''
  if (!validate()) return
  loading.value = true

  try {
    const response = await axios.post('http://localhost:8081/api/auth/login', {
      email: form.value.email,
      password: form.value.password,

    })

    const token = response.data.token
    const role = response.data.role
    const email = response.data.email

    // ===== VÉRIFICATION DU RÔLE =====
    // si l'utilisateur n'est PAS un client, on bloque la connexion
    if (role !== 'CLIENT') {
      errorMessage.value = 'Email ou mot de passe incorrect'
      loading.value = false
      return
    }

    authStore.setAuth(token, { email, role })
    router.push('/client/dashboard')

  } catch (err) {
    const status = err.response?.status
    const errorMsg = err.response?.data?.message || err.response?.data || ''

    // compte non vérifié
    if (errorMsg.includes('PENDING_VERIFICATION') || errorMsg.includes('non activé')) {
      router.push({
        path: '/verify-account',
        query: { email: form.value.email }
      })
      return
    }

    // compte suspendu
    if (errorMsg.includes('suspendu')) {
      errorMessage.value = 'Votre compte a été suspendu. Contactez le support.'
      return
    }

    // identifiants incorrects
    if (status === 401 || status === 400) {
      errorMessage.value = 'Email ou mot de passe incorrect'
    } else if (status === 403) {
      router.push({
        path: '/verify-account',
        query: { email: form.value.email }
      })
    } else {
      errorMessage.value = errorMsg || 'Une erreur est survenue'
    }
  } finally {
    loading.value = false
  }
}
</script>