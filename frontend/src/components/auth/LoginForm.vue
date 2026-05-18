<template>
  <div>
    <!-- message erreur -->
    <div v-if="errorMessage"
         class="bg-red-50 border border-red-200 text-red-700 rounded-2xl p-4 mb-6 text-sm font-medium flex items-center gap-2 animate-fade-up">
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
        <p v-if="errors.email" class="text-red-500 text-xs mt-1 animate-fade-up">{{ errors.email }}</p>
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

    <!-- lien vers l'autre type de compte -->
    <div class="text-center">
      <p class="text-sm text-ink-soft">
        <template v-if="role === 'CLIENT'">
          Vous êtes freelancer ?
          <RouterLink to="/login/freelancer" class="text-brand-500 font-semibold hover:underline ml-1">Connexion freelancer</RouterLink>
        </template>
        <template v-else>
          Vous êtes client ?
          <RouterLink to="/login/client" class="text-brand-500 font-semibold hover:underline ml-1">Connexion client</RouterLink>
        </template>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '@/api/auth'
import { useAuthStore } from '@/stores/authStore'

const props = defineProps({
  role: { type: String, required: true },
})

const router = useRouter()
const authStore = useAuthStore()

const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')
const focusedField = ref('')

const form = ref({ email: '', password: '', rememberMe: false })
const errors = ref({ email: '', password: '' })

const validate = () => {
  errors.value = { email: '', password: '' }
  let valid = true
  if (!form.value.email.trim()) {
    errors.value.email = "L'email est obligatoire"
    valid = false
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.value.email)) {
    errors.value.email = 'Format email invalide'
    valid = false
  }
  if (!form.value.password) {
    errors.value.password = 'Le mot de passe est obligatoire'
    valid = false
  }
  return valid
}

const submit = async () => {
  errorMessage.value = ''
  if (!validate()) return
  loading.value = true

  try {
    const { data } = await loginUser(form.value.email, form.value.password)
    const { token, refreshToken, role, email } = data

    if (role !== props.role) {
      errorMessage.value = 'Email ou mot de passe incorrect'
      return
    }

    authStore.setAuth(token, { email, role }, refreshToken, form.value.rememberMe)
    router.push(role === 'CLIENT' ? '/client/dashboard' : '/freelancer/dashboard')

  } catch (err) {
    const status = err.response?.status
    const errorMsg = err.response?.data?.message || err.response?.data || ''
    const fromParam = props.role === 'FREELANCER' ? 'freelancer' : undefined

    if (errorMsg.includes('PENDING_VERIFICATION') || errorMsg.includes('non activé') || status === 403) {
      router.push({ path: '/verify-account', query: { email: form.value.email, ...(fromParam && { from: fromParam }) } })
      return
    }
    if (errorMsg.includes('suspendu')) {
      errorMessage.value = 'Votre compte a été suspendu. Contactez le support.'
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
