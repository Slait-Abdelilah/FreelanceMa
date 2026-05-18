<template>
  <div>
    <!-- message erreur global -->
    <div v-if="errorMessage" class="bg-red-50 border-2 border-red-200 text-red-700 rounded-2xl p-4 mb-6 text-sm font-medium">
      {{ errorMessage }}
    </div>

    <div class="space-y-4">
      <!-- prénom + nom -->
      <div class="grid grid-cols-2 gap-3">
        <div>
          <label class="block text-xs font-bold text-ink mb-1.5">Prénom *</label>
          <input v-model="form.firstName" type="text" placeholder="Mohamed"
            class="w-full bg-white border-2 rounded-xl px-4 py-3 text-sm outline-none transition"
            :class="errors.firstName ? 'border-red-400 focus:border-red-500' : 'border-gray-200 focus:border-brand-500'"/>
          <p v-if="errors.firstName" class="text-red-500 text-xs mt-1">{{ errors.firstName }}</p>
        </div>
        <div>
          <label class="block text-xs font-bold text-ink mb-1.5">Nom *</label>
          <input v-model="form.lastName" type="text" placeholder="Alaoui"
            class="w-full bg-white border-2 rounded-xl px-4 py-3 text-sm outline-none transition"
            :class="errors.lastName ? 'border-red-400 focus:border-red-500' : 'border-gray-200 focus:border-brand-500'"/>
          <p v-if="errors.lastName" class="text-red-500 text-xs mt-1">{{ errors.lastName }}</p>
        </div>
      </div>

      <!-- email -->
      <div>
        <label class="block text-xs font-bold text-ink mb-1.5">Email *</label>
        <input v-model="form.email" type="email" placeholder="vous@exemple.ma"
          class="w-full bg-white border-2 rounded-xl px-4 py-3 text-sm outline-none transition"
          :class="errors.email ? 'border-red-400 focus:border-red-500' : 'border-gray-200 focus:border-brand-500'"/>
        <p v-if="errors.email" class="text-red-500 text-xs mt-1">{{ errors.email }}</p>
      </div>

      <!-- mot de passe -->
      <div>
        <label class="block text-xs font-bold text-ink mb-1.5">Mot de passe *</label>
        <div class="relative">
          <input v-model="form.password" :type="showPassword ? 'text' : 'password'" placeholder="Minimum 8 caractères"
            class="w-full bg-white border-2 rounded-xl px-4 py-3 text-sm outline-none transition pr-10"
            :class="errors.password ? 'border-red-400 focus:border-red-500' : 'border-gray-200 focus:border-brand-500'"/>
          <button @click="showPassword = !showPassword" type="button"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-ink-soft hover:text-ink transition">
            <svg v-if="!showPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
            </svg>
            <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"/>
            </svg>
          </button>
        </div>
        <div v-if="form.password" class="mt-2">
          <div class="flex gap-1 mb-1">
            <div v-for="i in 4" :key="i" class="h-1 flex-1 rounded-full transition-all"
                 :class="passwordStrength >= i ? passwordStrengthColor : 'bg-gray-200'"></div>
          </div>
          <p class="text-xs" :class="passwordStrengthTextColor">{{ passwordStrengthLabel }}</p>
        </div>
        <p v-if="errors.password" class="text-red-500 text-xs mt-1">{{ errors.password }}</p>
      </div>

      <!-- téléphone (CLIENT seulement) -->
      <div v-if="role === 'CLIENT'">
        <label class="block text-xs font-bold text-ink mb-1.5">Téléphone</label>
        <div class="flex gap-2">
          <div class="bg-white border-2 border-gray-200 rounded-xl px-3 py-3 text-sm flex items-center gap-1 text-ink-soft">
            🇲🇦 +212
          </div>
          <input v-model="form.phone" type="tel" placeholder="6 12 34 56 78"
            class="flex-1 bg-white border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3 text-sm outline-none transition"/>
        </div>
      </div>

      <!-- type de client (CLIENT seulement) -->
      <div v-if="role === 'CLIENT'">
        <label class="block text-xs font-bold text-ink mb-1.5">Type de client *</label>
        <div class="grid grid-cols-2 gap-2">
          <button @click="form.clientType = 'company'" type="button"
                  :class="form.clientType === 'company' ? 'border-brand-500 bg-brand-50 text-brand-700' : 'border-gray-200 hover:border-gray-300'"
                  class="border-2 rounded-xl p-3 text-left transition">
            <div class="text-xl mb-1">🏢</div>
            <div class="text-xs font-bold">Entreprise</div>
          </button>
          <button @click="form.clientType = 'individual'" type="button"
                  :class="form.clientType === 'individual' ? 'border-brand-500 bg-brand-50 text-brand-700' : 'border-gray-200 hover:border-gray-300'"
                  class="border-2 rounded-xl p-3 text-left transition">
            <div class="text-xl mb-1">👤</div>
            <div class="text-xs font-bold">Particulier</div>
          </button>
        </div>
      </div>

      <!-- CGU -->
      <label class="flex items-start gap-3 cursor-pointer">
        <input v-model="form.acceptTerms" type="checkbox" class="mt-0.5 w-4 h-4 accent-brand-500"/>
        <span class="text-xs text-ink-soft leading-relaxed">
          J'accepte les <a href="#" class="text-brand-500 font-semibold hover:underline">Conditions d'utilisation</a>
          et la <a href="#" class="text-brand-500 font-semibold hover:underline">Politique de confidentialité</a>
        </span>
      </label>
      <p v-if="errors.acceptTerms" class="text-red-500 text-xs -mt-2">{{ errors.acceptTerms }}</p>

      <!-- bouton submit -->
      <button @click="submit" :disabled="loading"
        class="w-full bg-ink text-white py-4 rounded-xl font-semibold hover:bg-brand-500 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2">
        <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
        </svg>
        {{ loading ? 'Création du compte...' : (role === 'CLIENT' ? 'Créer mon compte client' : 'Créer mon compte freelancer') }}
      </button>
    </div>

    <!-- séparateur -->
    <div class="flex items-center gap-3 my-6">
      <div class="flex-1 h-px bg-gray-200"></div>
      <span class="text-xs text-ink-soft font-medium">ou</span>
      <div class="flex-1 h-px bg-gray-200"></div>
    </div>

    <div class="text-center">
      <p class="text-sm text-ink-soft">
        <template v-if="role === 'CLIENT'">
          Vous êtes freelancer ?
          <RouterLink to="/register/freelancer" class="text-brand-500 font-semibold hover:underline ml-1">Créez un compte freelancer</RouterLink>
        </template>
        <template v-else>
          Vous êtes client ?
          <RouterLink to="/register/client" class="text-brand-500 font-semibold hover:underline ml-1">Créez un compte client</RouterLink>
        </template>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { registerClient, registerFreelancer } from '@/api/auth'

const props = defineProps({
  role: { type: String, required: true },
})

const router = useRouter()

const showPassword = ref(false)
const loading = ref(false)
const errorMessage = ref('')

const form = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  phone: '',
  clientType: 'company',
  acceptTerms: false,
})

const errors = ref({ firstName: '', lastName: '', email: '', password: '', acceptTerms: '' })

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
  } else if (form.value.password.length < 8) {
    errors.value.password = 'Minimum 8 caractères'
    valid = false
  }
  if (!form.value.acceptTerms) { errors.value.acceptTerms = 'Vous devez accepter les conditions'; valid = false }
  return valid
}

const submit = async () => {
  errorMessage.value = ''
  if (!validate()) return
  loading.value = true

  const emailToVerify = form.value.email

  try {
    const payload = {
      firstName: form.value.firstName,
      lastName: form.value.lastName,
      email: form.value.email,
      password: form.value.password,
      ...(props.role === 'CLIENT' && { phone: form.value.phone, clientType: form.value.clientType }),
    }

    if (props.role === 'CLIENT') {
      await registerClient(payload)
    } else {
      await registerFreelancer(payload)
    }

    const fromParam = props.role === 'FREELANCER' ? 'freelancer' : undefined
    router.push({ path: '/verify-account', query: { email: emailToVerify, ...(fromParam && { from: fromParam }) } })

  } catch (err) {
    errorMessage.value = err.response?.data?.message || err.response?.data || 'Une erreur est survenue'
  } finally {
    loading.value = false
  }
}
</script>
