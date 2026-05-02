<template>
  <div class="min-h-screen bg-white flex">

    <!-- ============ CÔTÉ GAUCHE ============ -->
    <div class="hidden lg:flex lg:w-1/2 bg-ink text-white flex-col justify-between p-12 relative overflow-hidden">

      <!-- formes décoratives -->
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

      <!-- contenu central -->
      <div class="relative z-10">
        <div class="text-7xl font-bold leading-none mb-6">
          Activez<br>
          votre <span class="italic text-brand-500">compte</span>
        </div>
        <p class="text-gray-400 text-lg leading-relaxed mb-8 max-w-sm">
          Une dernière étape avant de profiter de FreelanceMa. Entrez le code reçu par email.
        </p>

        <!-- email envoyé -->
        <div class="bg-white/5 border border-white/10 rounded-2xl p-5 flex items-center gap-3">
          <div class="w-10 h-10 bg-brand-500/20 rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
            </svg>
          </div>
          <div class="text-sm">
            <div class="font-semibold">Code envoyé à</div>
            <div class="text-gray-400 text-xs">{{ email }}</div>
          </div>
        </div>
      </div>

      <!-- sécurité -->
      <div class="relative z-10 flex items-center gap-2 text-xs text-gray-500">
        <svg class="w-4 h-4 text-brand-500" fill="currentColor" viewBox="0 0 20 20">
          <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2z" clip-rule="evenodd"/>
        </svg>
        Connexion sécurisée — Code valide 15 minutes
      </div>

    </div>

    <!-- ============ CÔTÉ DROIT ============ -->
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

        <!-- ÉTAT SUCCÈS -->
        <div v-if="verified" class="text-center">
          <div class="w-20 h-20 bg-brand-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <svg class="w-10 h-10 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
            </svg>
          </div>
          <h1 class="text-3xl font-bold text-ink mb-3">Compte activé !</h1>
          <p class="text-ink-soft mb-8">Redirection vers votre tableau de bord...</p>
          <div class="w-8 h-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto"></div>
        </div>

        <!-- ÉTAT FORMULAIRE -->
        <div v-else>

          <!-- titre -->
          <div class="mb-8">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Dernière étape</div>
            <h1 class="text-3xl font-bold text-ink mb-2">Vérifiez votre email</h1>
            <p class="text-ink-soft text-sm">
              Nous avons envoyé un code à 6 chiffres à
              <span class="font-semibold text-ink">{{ email }}</span>
            </p>
          </div>

          <!-- message erreur -->
          <div v-if="errorMessage"
               class="bg-red-50 border border-red-200 text-red-700 rounded-xl p-3 mb-4 text-sm flex items-center gap-2">
            <svg class="w-4 h-4 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
            </svg>
            {{ errorMessage }}
          </div>

          <!-- 6 champs de code -->
          <div class="mb-6">
            <label class="block text-xs font-bold text-ink mb-3 uppercase tracking-wider">
              Code de vérification
            </label>
            <div class="flex gap-2 justify-between">
              <input
                  v-for="(digit, i) in codeDigits"
                  :key="i"
                  :ref="el => codeInputs[i] = el"
                  v-model="codeDigits[i]"
                  type="text"
                  maxlength="1"
                  inputmode="numeric"
                  @input="onCodeInput(i, $event)"
                  @keydown.backspace="onBackspace(i, $event)"
                  @paste="onPaste($event)"
                  class="w-full h-14 border-2 rounded-xl text-center text-2xl font-bold outline-none transition bg-white"
                  :class="codeDigits[i]
                  ? 'border-brand-500 text-ink'
                  : 'border-gray-200 focus:border-brand-500'"
              />
            </div>
          </div>

          <!-- renvoyer le code -->
          <div class="text-center text-sm text-ink-soft mb-6">
            <span v-if="resendCountdown > 0">
              Code non reçu ? Renvoyer dans <span class="font-semibold text-ink">{{ resendCountdown }}s</span>
            </span>
            <span v-else>
              Code non reçu ?
              <button @click="resendCode"
                      class="text-brand-500 font-semibold hover:underline ml-1">
                Renvoyer le code
              </button>
            </span>
          </div>

          <!-- bouton valider -->
          <button
              @click="verifyCode"
              :disabled="loading || !isCodeComplete"
              class="w-full bg-ink text-white py-3.5 rounded-xl font-semibold text-sm hover:bg-brand-500 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2 group">
            <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <span>{{ loading ? 'Vérification...' : 'Activer mon compte' }}</span>
            <svg v-if="!loading && isCodeComplete" class="w-4 h-4 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </button>

          <!-- liens bas -->
          <div class="mt-6 pt-6 border-t border-gray-200 flex items-center justify-between text-sm">
            <RouterLink to="/login/client"
                        class="text-ink-soft hover:text-ink flex items-center gap-1">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
              </svg>
              Connexion
            </RouterLink>
            <RouterLink to="/register/client"
                        class="text-brand-500 font-semibold hover:underline">
              Modifier l'email
            </RouterLink>
          </div>

        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const email = ref('')
const codeDigits = ref(['', '', '', '', '', ''])
const codeInputs = ref([])
const loading = ref(false)
const errorMessage = ref('')
const verified = ref(false)
const resendCountdown = ref(60)
let countdownInterval = null

// code complet ?
const isCodeComplete = computed(() => {
  return codeDigits.value.every(d => d !== '')
})

// ============ AU CHARGEMENT ============
onMounted(async () => {

  // récupérer l'email depuis l'URL
  email.value = route.query.email || ''

  // si pas d'email → retour inscription
  if (!email.value) {
    router.push('/register/client')
    return
  }

  // démarrer le countdown
  startCountdown()

  // focus automatique sur le premier champ
  await nextTick()
  codeInputs.value[0]?.focus()
})

// ============ GESTION DES 6 CHAMPS ============

// quand on tape un chiffre
const onCodeInput = (index, event) => {
  // garder seulement les chiffres
  const value = event.target.value.replace(/[^0-9]/g, '')
  codeDigits.value[index] = value

  // passer au champ suivant automatiquement
  if (value && index < 5) {
    codeInputs.value[index + 1]?.focus()
  }

  // auto-submit quand le code est complet
  if (value && index === 5 && isCodeComplete.value) {
    verifyCode()
  }
}

// quand on appuie sur backspace
const onBackspace = (index, event) => {
  // si champ vide → revenir au champ précédent
  if (!codeDigits.value[index] && index > 0) {
    codeInputs.value[index - 1]?.focus()
  }
}

// coller le code (ex: depuis email)
const onPaste = (event) => {
  event.preventDefault()
  const pasted = event.clipboardData
      .getData('text')
      .replace(/[^0-9]/g, '')
      .slice(0, 6)

  pasted.split('').forEach((digit, i) => {
    codeDigits.value[i] = digit
  })

  if (pasted.length === 6) {
    codeInputs.value[5]?.focus()
    // auto-submit après coller un code complet
    verifyCode()
  }
}

// ============ COUNTDOWN ============
const startCountdown = () => {
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

// ============ VÉRIFIER LE CODE ============
const verifyCode = async () => {
  errorMessage.value = ''
  const code = codeDigits.value.join('')

  if (code.length !== 6) {
    errorMessage.value = 'Le code doit contenir 6 chiffres'
    return
  }

  loading.value = true

  try {
    // appel backend
    const response = await axios.post(
        'http://localhost:8081/api/auth/verify-account',
        { email: email.value, code: code }
    )

    // le backend renvoie un token JWT → connexion automatique
    const token = response.data.token
    const userEmail = response.data.email
    const role = response.data.role

    // sauvegarder dans localStorage
    localStorage.setItem('token', token)
    localStorage.setItem('user', JSON.stringify({
      email: userEmail,
      role: role
    }))

    // configurer axios pour les futures requêtes
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`

    // afficher l'écran de succès
    verified.value = true

    // redirection selon le rôle après 2 secondes
    setTimeout(() => {
      if (role === 'CLIENT') {
        router.push('/client/dashboard')
      } else if (role === 'FREELANCER') {
        router.push('/freelancer/dashboard')
      } else {
        router.push('/')
      }
    }, 2000)

  } catch (err) {
    errorMessage.value = err.response?.data?.message || 'Code invalide ou expiré'

    // effacer le code pour retaper
    codeDigits.value = ['', '', '', '', '', '']
    await nextTick()
    codeInputs.value[0]?.focus()

  } finally {
    loading.value = false
  }
}

// ============ RENVOYER LE CODE ============
const resendCode = async () => {
  errorMessage.value = ''

  try {
    await axios.post('http://localhost:8081/api/auth/resend-code', {
      email: email.value
    })

    // redémarrer le countdown
    startCountdown()

    // effacer le code actuel
    codeDigits.value = ['', '', '', '', '', '']
    await nextTick()
    codeInputs.value[0]?.focus()

  } catch (err) {
    errorMessage.value = 'Erreur lors du renvoi du code'
  }
}
</script>