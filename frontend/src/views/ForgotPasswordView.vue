<template>
  <div class="min-h-screen bg-white flex">

    <!-- ============ CÔTÉ GAUCHE — VISUEL ============ -->
    <div class="hidden lg:flex lg:w-1/2 bg-ink text-white flex-col justify-between p-12 relative overflow-hidden">

      <!-- éléments décoratifs -->
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

      <!-- contenu central dynamique -->
      <div class="relative z-10">

        <!-- étape 1 -->
        <div v-if="currentStep === 1">
          <div class="text-7xl font-bold leading-none mb-6">
            Mot de<br>
            passe <span class="italic text-brand-500">oublié</span> ?
          </div>
          <p class="text-gray-400 text-lg leading-relaxed mb-8 max-w-sm">
            Pas d'inquiétude. Entrez votre email et nous vous enverrons un code pour réinitialiser votre mot de passe.
          </p>
          <div class="flex items-center gap-3 bg-white/5 border border-white/10 rounded-2xl p-4">
            <div class="w-10 h-10 bg-brand-500/20 rounded-full flex items-center justify-center">
              <svg class="w-5 h-5 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
              </svg>
            </div>
            <div class="text-sm">
              <div class="font-semibold">Code envoyé par email</div>
              <div class="text-gray-400">Valide pendant 15 minutes</div>
            </div>
          </div>
        </div>

        <!-- étape 2 -->
        <div v-if="currentStep === 2">
          <div class="text-7xl font-bold leading-none mb-6">
            Vérifiez<br>
            votre <span class="italic text-brand-500">email</span>
          </div>
          <p class="text-gray-400 text-lg leading-relaxed mb-8 max-w-sm">
            Nous avons envoyé un code à 6 chiffres à votre adresse email.
          </p>
          <div class="bg-white/5 border border-white/10 rounded-2xl p-5">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-wider mb-2">Email envoyé à</div>
            <div class="font-semibold text-lg">{{ email }}</div>
          </div>
        </div>

        <!-- étape 3 -->
        <div v-if="currentStep === 3">
          <div class="text-7xl font-bold leading-none mb-6">
            Nouveau<br>
            <span class="italic text-brand-500">mot de passe</span>
          </div>
          <p class="text-gray-400 text-lg leading-relaxed mb-8 max-w-sm">
            Choisissez un mot de passe fort pour sécuriser votre compte.
          </p>
        </div>

      </div>

      <!-- étapes visuelles -->
      <div class="relative z-10 flex items-center gap-2">
        <div v-for="i in 3" :key="i"
             class="h-1 flex-1 rounded-full transition-all duration-500"
             :class="currentStep >= i ? 'bg-brand-500' : 'bg-white/20'"></div>
      </div>

    </div>

    <!-- ============ CÔTÉ DROIT — FORMULAIRE ============ -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 lg:p-12 bg-cream">
      <div class="w-full max-w-md">

        <!-- logo mobile -->
        <RouterLink to="/" class="flex items-center gap-2 mb-8 lg:hidden">
          <svg class="w-8 h-8 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
            <circle cx="12" cy="12" r="10"/>
            <circle cx="12" cy="12" r="4" fill="white"/>
          </svg>
          <span class="font-bold text-ink">FreelanceMa</span>
        </RouterLink>

        <!-- ============ ÉTAPE 1 : EMAIL ============ -->
        <div v-if="currentStep === 1">

          <!-- retour -->
          <RouterLink to="/login/client"
                      class="inline-flex items-center gap-2 text-ink-soft hover:text-ink text-sm font-medium mb-6 transition">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
            </svg>
            Retour à la connexion
          </RouterLink>

          <!-- titre -->
          <div class="mb-8">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Étape 1 sur 3</div>
            <h1 class="text-3xl font-bold text-ink mb-2">Entrez votre email</h1>
            <p class="text-ink-soft text-sm">Nous vous enverrons un code de vérification.</p>
          </div>

          <!-- erreur -->
          <div v-if="errorMessage"
               class="bg-red-50 border border-red-200 text-red-700 rounded-xl p-3 mb-4 text-sm">
            {{ errorMessage }}
          </div>

          <!-- champ email -->
          <div class="mb-6">
            <label class="block text-xs font-bold text-ink mb-2 uppercase tracking-wider">Email</label>
            <input
                v-model="email"
                type="email"
                placeholder="vous@exemple.ma"
                @keyup.enter="sendCode"
                class="w-full border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3.5 text-sm outline-none transition bg-white"
            />
          </div>

          <!-- bouton -->
          <button
              @click="sendCode"
              :disabled="loading"
              class="w-full bg-ink text-white py-3.5 rounded-xl font-semibold text-sm hover:bg-brand-500 transition disabled:opacity-50 flex items-center justify-center gap-2 group">
            <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <span>{{ loading ? 'Envoi en cours...' : 'Envoyer le code' }}</span>
            <svg v-if="!loading" class="w-4 h-4 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </button>

        </div>

        <!-- ============ ÉTAPE 2 : CODE ============ -->
        <div v-if="currentStep === 2">

          <button @click="currentStep = 1"
                  class="inline-flex items-center gap-2 text-ink-soft hover:text-ink text-sm font-medium mb-6 transition">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
            </svg>
            Modifier l'email
          </button>

          <div class="mb-8">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Étape 2 sur 3</div>
            <h1 class="text-3xl font-bold text-ink mb-2">Code de vérification</h1>
            <p class="text-ink-soft text-sm">
              Entrez le code à 6 chiffres envoyé à <span class="font-semibold text-ink">{{ email }}</span>
            </p>
          </div>

          <div v-if="errorMessage"
               class="bg-red-50 border border-red-200 text-red-700 rounded-xl p-3 mb-4 text-sm">
            {{ errorMessage }}
          </div>

          <!-- champs de code 6 chiffres -->
          <div class="mb-6">
            <label class="block text-xs font-bold text-ink mb-3 uppercase tracking-wider">Code reçu</label>
            <div class="flex gap-2 justify-between">
              <input
                  v-for="(digit, i) in codeDigits"
                  :key="i"
                  :ref="el => codeInputs[i] = el"
                  v-model="codeDigits[i]"
                  type="text"
                  maxlength="1"
                  @input="onCodeInput(i, $event)"
                  @keydown.backspace="onBackspace(i, $event)"
                  @paste="onPaste($event)"
                  class="w-full h-14 border-2 border-gray-200 focus:border-brand-500 rounded-xl text-center text-2xl font-bold outline-none transition bg-white"
              />
            </div>
          </div>

          <!-- renvoyer le code -->
          <div class="text-center text-sm text-ink-soft mb-6">
            <span v-if="resendCountdown > 0">
              Renvoyer le code dans {{ resendCountdown }}s
            </span>
            <button v-else @click="sendCode" class="text-brand-500 font-semibold hover:underline">
              Renvoyer le code
            </button>
          </div>

          <!-- bouton -->
          <button
              @click="verifyCode"
              :disabled="loading || !isCodeComplete"
              class="w-full bg-ink text-white py-3.5 rounded-xl font-semibold text-sm hover:bg-brand-500 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 group">
            <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <span>{{ loading ? 'Vérification...' : 'Vérifier le code' }}</span>
          </button>

        </div>

        <!-- ============ ÉTAPE 3 : NOUVEAU MDP ============ -->
        <div v-if="currentStep === 3">

          <div class="mb-8">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Étape 3 sur 3</div>
            <h1 class="text-3xl font-bold text-ink mb-2">Nouveau mot de passe</h1>
            <p class="text-ink-soft text-sm">Choisissez un mot de passe fort et sécurisé.</p>
          </div>

          <div v-if="errorMessage"
               class="bg-red-50 border border-red-200 text-red-700 rounded-xl p-3 mb-4 text-sm">
            {{ errorMessage }}
          </div>

          <div class="space-y-4">

            <div>
              <label class="block text-xs font-bold text-ink mb-2 uppercase tracking-wider">Nouveau mot de passe</label>
              <div class="relative">
                <input
                    v-model="newPassword"
                    :type="showPassword ? 'text' : 'password'"
                    placeholder="Minimum 8 caractères"
                    class="w-full border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3.5 text-sm outline-none transition bg-white pr-10"
                />
                <button @click="showPassword = !showPassword" type="button"
                        class="absolute right-3 top-1/2 -translate-y-1/2 text-ink-soft">
                  <svg v-if="!showPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                  </svg>
                  <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M3 3l3.59 3.59"/>
                  </svg>
                </button>
              </div>

              <div v-if="newPassword" class="mt-2">
                <div class="flex gap-1 mb-1">
                  <div v-for="i in 4" :key="i"
                       class="h-1 flex-1 rounded-full transition-all"
                       :class="passwordStrength >= i ? passwordStrengthColor : 'bg-gray-200'">
                  </div>
                </div>
                <p class="text-xs" :class="passwordStrengthTextColor">{{ passwordStrengthLabel }}</p>
              </div>
            </div>

            <div>
              <label class="block text-xs font-bold text-ink mb-2 uppercase tracking-wider">Confirmer</label>
              <input
                  v-model="confirmPassword"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="Répétez le mot de passe"
                  @keyup.enter="submitNewPassword"
                  class="w-full border-2 rounded-xl px-4 py-3.5 text-sm outline-none transition bg-white"
                  :class="confirmPassword && newPassword !== confirmPassword
                  ? 'border-red-400'
                  : 'border-gray-200 focus:border-brand-500'"
              />
              <p v-if="confirmPassword && newPassword !== confirmPassword"
                 class="text-red-500 text-xs mt-1">Les mots de passe ne correspondent pas</p>
            </div>

            <button
                @click="submitNewPassword"
                :disabled="loading"
                class="w-full bg-brand-500 text-white py-3.5 rounded-xl font-semibold text-sm hover:bg-brand-600 transition disabled:opacity-50 flex items-center justify-center gap-2 mt-2">
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
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const currentStep = ref(1)
const email = ref('')
const codeDigits = ref(['', '', '', '', '', ''])
const codeInputs = ref([])
const newPassword = ref('')
const confirmPassword = ref('')
const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')
const resendCountdown = ref(0)
let countdownInterval = null

// code complet ?
const isCodeComplete = computed(() => {
  return codeDigits.value.every(d => d !== '')
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

const passwordStrengthLabel = computed(() => ['', 'Faible', 'Moyen', 'Bon', 'Excellent'][passwordStrength.value])
const passwordStrengthColor = computed(() => ['', 'bg-red-400', 'bg-yellow-400', 'bg-blue-400', 'bg-brand-500'][passwordStrength.value])
const passwordStrengthTextColor = computed(() => ['', 'text-red-500', 'text-yellow-600', 'text-blue-500', 'text-brand-500'][passwordStrength.value])

// ============ ÉTAPE 1 — envoyer le code ============
const sendCode = async () => {
  errorMessage.value = ''

  if (!email.value.trim()) {
    errorMessage.value = "L'email est obligatoire"
    return
  }
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
    errorMessage.value = "Format email invalide"
    return
  }

  loading.value = true

  try {
    await axios.post(`${API_URL}/api/auth/forgot-password`, {
      email: email.value
    })

    // passer à l'étape 2
    currentStep.value = 2
    startResendCountdown()

    // focus sur le premier champ
    await nextTick()
    codeInputs.value[0]?.focus()

  } catch (err) {
    errorMessage.value = err.response?.data?.message || 'Erreur lors de l\'envoi du code'
  } finally {
    loading.value = false
  }
}

// ============ gestion des 6 champs de code ============
const onCodeInput = (index, event) => {
  const value = event.target.value.replace(/[^0-9]/g, '')
  codeDigits.value[index] = value

  if (value && index < 5) {
    codeInputs.value[index + 1]?.focus()
  }
}

const onBackspace = (index, event) => {
  if (!codeDigits.value[index] && index > 0) {
    codeInputs.value[index - 1]?.focus()
  }
}

const onPaste = (event) => {
  event.preventDefault()
  const pasted = event.clipboardData.getData('text').replace(/[^0-9]/g, '').slice(0, 6)
  pasted.split('').forEach((digit, i) => {
    codeDigits.value[i] = digit
  })
  if (pasted.length === 6) {
    codeInputs.value[5]?.focus()
  }
}

// countdown renvoi
const startResendCountdown = () => {
  resendCountdown.value = 60
  if (countdownInterval) clearInterval(countdownInterval)
  countdownInterval = setInterval(() => {
    if (resendCountdown.value > 0) {
      resendCountdown.value--
    } else {
      clearInterval(countdownInterval)
    }
  }, 1000)
}

// ============ ÉTAPE 2 — vérifier le code ============
const verifyCode = async () => {
  errorMessage.value = ''

  const code = codeDigits.value.join('')
  if (code.length !== 6) {
    errorMessage.value = 'Le code doit contenir 6 chiffres'
    return
  }

  loading.value = true

  try {
    // vérifier que le code est valide
    await axios.post(`${API_URL}/api/auth/verify-reset-code`, {
      email: email.value,
      code: code
    })

    // passer à l'étape 3
    currentStep.value = 3

  } catch (err) {
    errorMessage.value = 'Code invalide ou expiré'
    // effacer le code pour retaper
    codeDigits.value = ['', '', '', '', '', '']
    await nextTick()
    codeInputs.value[0]?.focus()
  } finally {
    loading.value = false
  }
}

// ============ ÉTAPE 3 — nouveau mot de passe ============
const submitNewPassword = async () => {
  errorMessage.value = ''

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
      token: codeDigits.value.join(''),
      newPassword: newPassword.value
    })

    // redirection vers login après 1 seconde
    setTimeout(() => {
      router.push('/login/client')
    }, 1000)

  } catch (err) {
    errorMessage.value = err.response?.data?.message || 'Erreur lors de la modification'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // rien au montage
})
</script>