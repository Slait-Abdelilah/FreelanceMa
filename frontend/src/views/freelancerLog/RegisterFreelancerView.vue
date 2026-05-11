<template>
  <div class="min-h-screen bg-cream flex">

    <!-- ============ CÔTÉ GAUCHE — VISUEL ============ -->
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

      <div class="relative z-10">
        <div class="text-7xl font-bold leading-none mb-6">
          Gagnez votre<br>
          <span class="italic text-brand-500">indépendance</span>
        </div>
        <p class="text-gray-400 text-lg leading-relaxed mb-12 max-w-sm">
          Rejoignez plus de 10,000 freelancers qui trouvent des missions de qualité chaque jour sur FreelanceMa.
        </p>

        <!-- avantages -->
        <div class="space-y-3">
          <div class="flex items-center gap-3 bg-white/5 border border-white/10 rounded-2xl p-4">
            <div class="w-10 h-10 bg-brand-500/20 rounded-full flex items-center justify-center text-xl">💼</div>
            <div>
              <div class="font-semibold text-sm">Missions de qualité</div>
              <div class="text-xs text-gray-400">Triées par notre algorithme</div>
            </div>
          </div>
          <div class="flex items-center gap-3 bg-white/5 border border-white/10 rounded-2xl p-4">
            <div class="w-10 h-10 bg-brand-500/20 rounded-full flex items-center justify-center text-xl">💰</div>
            <div>
              <div class="font-semibold text-sm">Paiement garanti</div>
              <div class="text-xs text-gray-400">Grâce au système escrow</div>
            </div>
          </div>
          <div class="flex items-center gap-3 bg-white/5 border border-white/10 rounded-2xl p-4">
            <div class="w-10 h-10 bg-brand-500/20 rounded-full flex items-center justify-center text-xl">📈</div>
            <div>
              <div class="font-semibold text-sm">Profil mis en avant</div>
              <div class="text-xs text-gray-400">Visible par milliers de clients</div>
            </div>
          </div>
        </div>
      </div>

      <!-- testimonial -->
      <div class="relative z-10 bg-white/5 border border-white/10 rounded-2xl p-6">
        <div class="flex gap-1 text-brand-500 mb-3 text-sm">★★★★★</div>
        <p class="text-gray-300 text-sm leading-relaxed mb-4">
          "J'ai quitté mon CDI grâce à FreelanceMa. Je gagne le double en travaillant ce que je veux."
        </p>
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-gradient-to-br from-brand-500 to-brand-700"></div>
          <div>
            <div class="font-bold text-sm">Yasmine Benali</div>
            <div class="text-xs text-gray-400">Designer UI/UX, Casablanca</div>
          </div>
        </div>
      </div>

    </div>

    <!-- ============ CÔTÉ DROIT — FORMULAIRE ============ -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 lg:p-12">
      <div class="w-full max-w-md animate-fade-up">

        <!-- header -->
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

        <!-- message erreur -->
        <div v-if="errorMessage" class="bg-red-50 border-2 border-red-200 text-red-700 rounded-2xl p-4 mb-6 text-sm font-medium">
          {{ errorMessage }}
        </div>

        <!-- FORMULAIRE -->
        <div class="space-y-4">

          <!-- prénom + nom -->
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-xs font-bold text-ink mb-1.5">Prénom *</label>
              <input
                  v-model="form.firstName"
                  type="text"
                  placeholder="Yasmine"
                  class="w-full bg-white border-2 rounded-xl px-4 py-3 text-sm outline-none transition"
                  :class="errors.firstName ? 'border-red-400' : 'border-gray-200 focus:border-brand-500'"
              />
              <p v-if="errors.firstName" class="text-red-500 text-xs mt-1">{{ errors.firstName }}</p>
            </div>
            <div>
              <label class="block text-xs font-bold text-ink mb-1.5">Nom *</label>
              <input
                  v-model="form.lastName"
                  type="text"
                  placeholder="Benali"
                  class="w-full bg-white border-2 rounded-xl px-4 py-3 text-sm outline-none transition"
                  :class="errors.lastName ? 'border-red-400' : 'border-gray-200 focus:border-brand-500'"
              />
              <p v-if="errors.lastName" class="text-red-500 text-xs mt-1">{{ errors.lastName }}</p>
            </div>
          </div>

          <!-- email -->
          <div>
            <label class="block text-xs font-bold text-ink mb-1.5">Email *</label>
            <input
                v-model="form.email"
                type="email"
                placeholder="vous@exemple.ma"
                class="w-full bg-white border-2 rounded-xl px-4 py-3 text-sm outline-none transition"
                :class="errors.email ? 'border-red-400' : 'border-gray-200 focus:border-brand-500'"
            />
            <p v-if="errors.email" class="text-red-500 text-xs mt-1">{{ errors.email }}</p>
          </div>

          <!-- mot de passe -->
          <div>
            <label class="block text-xs font-bold text-ink mb-1.5">Mot de passe *</label>
            <div class="relative">
              <input
                  v-model="form.password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="Minimum 8 caractères"
                  class="w-full bg-white border-2 rounded-xl px-4 py-3 text-sm outline-none transition pr-10"
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

            <!-- force du mot de passe -->
            <div v-if="form.password" class="mt-2">
              <div class="flex gap-1 mb-1">
                <div v-for="i in 4" :key="i"
                     class="h-1 flex-1 rounded-full transition-all"
                     :class="passwordStrength >= i ? passwordStrengthColor : 'bg-gray-200'"></div>
              </div>
              <p class="text-xs" :class="passwordStrengthTextColor">{{ passwordStrengthLabel }}</p>
            </div>
            <p v-if="errors.password" class="text-red-500 text-xs mt-1">{{ errors.password }}</p>
          </div>

          <!-- CGU -->
          <label class="flex items-start gap-3 cursor-pointer">
            <input v-model="form.acceptTerms" type="checkbox" class="mt-0.5 w-4 h-4 accent-brand-500"/>
            <span class="text-xs text-ink-soft leading-relaxed">
              J'accepte les <a href="#" class="text-brand-500 font-semibold hover:underline">Conditions d'utilisation</a> et la <a href="#" class="text-brand-500 font-semibold hover:underline">Politique de confidentialité</a>
            </span>
          </label>
          <p v-if="errors.acceptTerms" class="text-red-500 text-xs -mt-2">{{ errors.acceptTerms }}</p>

          <!-- bouton -->
          <button
              @click="submit"
              :disabled="loading"
              class="w-full bg-ink text-white py-4 rounded-xl font-semibold hover:bg-brand-500 transition disabled:opacity-50 flex items-center justify-center gap-2">
            <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ loading ? 'Création du compte...' : 'Créer mon compte freelancer' }}
          </button>

        </div>

        <!-- séparateur -->
        <div class="flex items-center gap-3 my-6">
          <div class="flex-1 h-px bg-gray-200"></div>
          <span class="text-xs text-ink-soft">ou</span>
          <div class="flex-1 h-px bg-gray-200"></div>
        </div>

        <!-- lien client -->
        <div class="text-center">
          <p class="text-sm text-ink-soft">
            Vous êtes client ?
            <RouterLink to="/register/client" class="text-brand-500 font-semibold hover:underline ml-1">Créez un compte client</RouterLink>
          </p>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')

const form = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  acceptTerms: false,
})

const errors = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  acceptTerms: '',
})

// force mot de passe
const passwordStrength = computed(() => {
  const p = form.value.password
  if (!p) return 0
  let score = 0
  if (p.length >= 8) score++
  if (/[A-Z]/.test(p)) score++
  if (/[0-9]/.test(p)) score++
  if (/[^A-Za-z0-9]/.test(p)) score++
  return score
})

const passwordStrengthLabel = computed(() => ['', 'Faible', 'Moyen', 'Bon', 'Excellent'][passwordStrength.value])
const passwordStrengthColor = computed(() => ['', 'bg-red-400', 'bg-yellow-400', 'bg-blue-400', 'bg-brand-500'][passwordStrength.value])
const passwordStrengthTextColor = computed(() => ['', 'text-red-500', 'text-yellow-600', 'text-blue-500', 'text-brand-500'][passwordStrength.value])

const validate = () => {
  errors.value = { firstName: '', lastName: '', email: '', password: '', acceptTerms: '' }
  let valid = true

  if (!form.value.firstName.trim()) { errors.value.firstName = 'Obligatoire'; valid = false }
  if (!form.value.lastName.trim()) { errors.value.lastName = 'Obligatoire'; valid = false }
  if (!form.value.email.trim()) { errors.value.email = 'Obligatoire'; valid = false }
  else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.value.email)) { errors.value.email = 'Format invalide'; valid = false }
  if (!form.value.password) { errors.value.password = 'Obligatoire'; valid = false }
  else if (form.value.password.length < 8) { errors.value.password = 'Minimum 8 caractères'; valid = false }
  if (!form.value.acceptTerms) { errors.value.acceptTerms = 'Vous devez accepter'; valid = false }

  return valid
}

const submit = async () => {
  errorMessage.value = ''
  if (!validate()) return

  loading.value = true

  // sauvegarder l'email avant réinitialisation
  const emailToVerify = form.value.email

  try {
    await axios.post(`${API_URL}/api/auth/register`, {
      firstName: form.value.firstName,
      lastName: form.value.lastName,
      email: form.value.email,
      password: form.value.password,
      role: 'FREELANCER',
    })

    // rediriger vers vérification
    router.push({
      path: '/verify-account',
      query: { email: emailToVerify, from: 'freelancer' }
    })

  } catch (err) {
    errorMessage.value = err.response?.data?.message || 'Erreur lors de l\'inscription'
  } finally {
    loading.value = false
  }
}
</script>