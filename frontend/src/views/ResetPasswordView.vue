<template>
  <div class="min-h-screen bg-cream flex items-center justify-center px-6 py-12">
    <div class="w-full max-w-md">

      <!-- logo centré -->
      <RouterLink to="/" class="flex items-center gap-2 justify-center mb-10">
        <svg class="w-9 h-9 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
          <circle cx="12" cy="12" r="10"/>
          <circle cx="12" cy="12" r="4" fill="#F7FAF4"/>
        </svg>
        <span class="text-xl font-bold text-ink">FreelanceMa</span>
      </RouterLink>

      <!-- carte principale -->
      <div class="bg-white rounded-3xl p-8 border border-gray-100">

        <!-- ÉTAT TOKEN INVALIDE -->
        <div v-if="tokenInvalid" class="text-center">

          <div class="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg class="w-8 h-8 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
          </div>

          <h1 class="text-2xl font-bold text-ink mb-3">Lien invalide</h1>
          <p class="text-ink-soft text-sm leading-relaxed mb-8">
            Ce lien est expiré ou invalide. Demandez un nouveau lien de réinitialisation.
          </p>

          <RouterLink to="/forgot-password"
                      class="inline-block bg-ink text-white px-6 py-3 rounded-full font-semibold text-sm hover:bg-brand-500 transition">
            Demander un nouveau lien
          </RouterLink>

        </div>

        <!-- ÉTAT SUCCÈS -->
        <div v-else-if="success" class="text-center">

          <div class="w-16 h-16 bg-brand-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg class="w-8 h-8 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
            </svg>
          </div>

          <h1 class="text-2xl font-bold text-ink mb-3">Mot de passe modifié</h1>
          <p class="text-ink-soft text-sm leading-relaxed mb-8">
            Votre mot de passe a été réinitialisé avec succès.
            Vous pouvez maintenant vous connecter.
          </p>

          <RouterLink to="/login/client"
                      class="inline-block bg-brand-500 text-white px-6 py-3 rounded-full font-semibold text-sm hover:bg-brand-600 transition">
            Se connecter
          </RouterLink>

        </div>

        <!-- ÉTAT FORMULAIRE -->
        <div v-else>

          <!-- titre -->
          <div class="mb-8">
            <h1 class="text-2xl font-bold text-ink mb-2">Nouveau mot de passe</h1>
            <p class="text-ink-soft text-sm">
              Choisissez un mot de passe fort pour sécuriser votre compte.
            </p>
          </div>

          <!-- erreur -->
          <div v-if="errorMessage"
               class="bg-red-50 border border-red-200 text-red-700 rounded-xl p-3 mb-4 text-sm">
            {{ errorMessage }}
          </div>

          <div class="space-y-4">

            <!-- nouveau mot de passe -->
            <div>
              <label class="block text-xs font-bold text-ink mb-2 uppercase tracking-wider">
                Nouveau mot de passe
              </label>
              <div class="relative">
                <input
                    v-model="newPassword"
                    :type="showPassword ? 'text' : 'password'"
                    placeholder="Minimum 8 caractères"
                    class="w-full border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3 text-sm outline-none transition pr-10"
                />
                <button @click="showPassword = !showPassword" type="button"
                        class="absolute right-3 top-1/2 -translate-y-1/2 text-ink-soft hover:text-ink">
                  <svg v-if="!showPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                  </svg>
                  <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59"/>
                  </svg>
                </button>
              </div>

              <!-- barre de force -->
              <div v-if="newPassword" class="mt-2">
                <div class="flex gap-1 mb-1">
                  <div v-for="i in 4" :key="i"
                       class="h-1 flex-1 rounded-full transition-all"
                       :class="passwordStrength >= i ? passwordStrengthColor : 'bg-gray-200'">
                  </div>
                </div>
                <p class="text-xs" :class="passwordStrengthTextColor">
                  {{ passwordStrengthLabel }}
                </p>
              </div>
            </div>

            <!-- confirmer -->
            <div>
              <label class="block text-xs font-bold text-ink mb-2 uppercase tracking-wider">
                Confirmer le mot de passe
              </label>
              <input
                  v-model="confirmPassword"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="Répétez le mot de passe"
                  @keyup.enter="submit"
                  class="w-full border-2 rounded-xl px-4 py-3 text-sm outline-none transition"
                  :class="confirmPassword && newPassword !== confirmPassword
                  ? 'border-red-400'
                  : 'border-gray-200 focus:border-brand-500'"
              />
              <p v-if="confirmPassword && newPassword !== confirmPassword"
                 class="text-red-500 text-xs mt-1">
                Les mots de passe ne correspondent pas
              </p>
            </div>

            <!-- bouton -->
            <button
                @click="submit"
                :disabled="loading"
                class="w-full bg-ink text-white py-3.5 rounded-xl font-semibold text-sm hover:bg-brand-500 transition disabled:opacity-50 flex items-center justify-center gap-2 mt-2">
              <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ loading ? 'Modification...' : 'Modifier le mot de passe' }}
            </button>

          </div>

        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const token = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')
const success = ref(false)
const tokenInvalid = ref(false)

// récupérer le token depuis l'URL
onMounted(() => {
  token.value = route.query.token || ''
  if (!token.value) {
    tokenInvalid.value = true
  }
})

// force du mot de passe
const passwordStrength = computed(() => {
  const p = newPassword.value
  if (!p) return 0
  let score = 0
  if (p.length >= 8) score++
  if (/[A-Z]/.test(p)) score++
  if (/[0-9]/.test(p)) score++
  if (/[^A-Za-z0-9]/.test(p)) score++
  return score
})

const passwordStrengthLabel = computed(() => {
  return ['', 'Faible', 'Moyen', 'Bon', 'Excellent'][passwordStrength.value]
})

const passwordStrengthColor = computed(() => {
  return ['', 'bg-red-400', 'bg-yellow-400', 'bg-blue-400', 'bg-brand-500'][passwordStrength.value]
})

const passwordStrengthTextColor = computed(() => {
  return ['', 'text-red-500', 'text-yellow-600', 'text-blue-500', 'text-brand-500'][passwordStrength.value]
})

const submit = async () => {
  errorMessage.value = ''

  if (!newPassword.value) {
    errorMessage.value = 'Le mot de passe est obligatoire'
    return
  }

  if (newPassword.value.length < 8) {
    errorMessage.value = 'Minimum 8 caractères'
    return
  }

  if (newPassword.value !== confirmPassword.value) {
    errorMessage.value = 'Les mots de passe ne correspondent pas'
    return
  }

  loading.value = true

  try {
    await axios.post(`${API_URL}/api/auth/reset-password`, {
      token: token.value,
      newPassword: newPassword.value,
    })

    success.value = true

  } catch (err) {
    const msg = err.response?.data?.message || 'Token invalide ou expiré'

    if (msg.includes('expiré') || msg.includes('invalide')) {
      tokenInvalid.value = true
    } else {
      errorMessage.value = msg
    }
  } finally {
    loading.value = false
  }
}
</script>