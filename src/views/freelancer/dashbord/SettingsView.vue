<template>
  <div class="max-w-4xl mx-auto space-y-6">

    <!-- EN-TÊTE -->
    <div>
      <h1 class="text-xl font-bold text-ink">Paramètres</h1>
      <p class="text-[13px] text-[#73726C] mt-0.5">Gérez votre compte et vos préférences</p>
    </div>

    <!-- LOADING -->
    <div v-if="loading" class="flex justify-center py-16">
      <div class="w-6 h-6 border-2 border-brand-500 border-t-transparent rounded-full animate-spin"></div>
    </div>

    <div v-else class="space-y-4">

      <!-- ===== BLOC 1 : INFORMATIONS PERSONNELLES ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5]">
          <h2 class="text-[14px] font-bold text-ink">Informations personnelles</h2>
        </div>
        <div class="p-6 space-y-4">

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">Prénom</label>
              <div class="px-3 py-2.5 bg-[#FAFAF7] border border-[#EBEBE5] rounded-lg text-[13px] text-ink">
                {{ form.firstName || '—' }}
              </div>
            </div>
            <div>
              <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">Nom</label>
              <div class="px-3 py-2.5 bg-[#FAFAF7] border border-[#EBEBE5] rounded-lg text-[13px] text-ink">
                {{ form.lastName || '—' }}
              </div>
            </div>
          </div>

          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">Email</label>
            <div class="flex items-center gap-2">
              <div class="flex-1 px-3 py-2.5 bg-[#FAFAF7] border border-[#EBEBE5] rounded-lg text-[13px] text-ink">
                {{ form.email }}
              </div>
              <span class="text-[11px] text-[#9C9A92] bg-[#F4F4ED] px-2 py-1 rounded-md whitespace-nowrap">
                Non modifiable
              </span>
            </div>
          </div>

          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">Téléphone</label>
            <input
                v-model="form.phone"
                type="tel"
                placeholder="+212 6 12 34 56 78"
                class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition max-w-sm"
            />
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">Langue</label>
              <select v-model="form.language"
                      class="w-full text-[13px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-2.5 outline-none focus:border-brand-500 transition">
                <option value="fr">🇫🇷 Français</option>
                <option value="en">🇬🇧 English</option>
                <option value="ar">🇲🇦 العربية</option>
              </select>
            </div>
            <div>
              <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">Devise</label>
              <select v-model="form.currency"
                      class="w-full text-[13px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-2.5 outline-none focus:border-brand-500 transition">
                <option value="MAD">🇲🇦 DH (MAD)</option>
                <option value="EUR">🇪🇺 € (EUR)</option>
                <option value="USD">🇺🇸 $ (USD)</option>
              </select>
            </div>
          </div>

          <div class="flex justify-end pt-2">
            <button @click="saveAccount" :disabled="savingAccount"
                    class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-4 py-2 rounded-lg transition disabled:opacity-50">
              <svg v-if="savingAccount" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ savingAccount ? 'Enregistrement...' : 'Enregistrer' }}
            </button>
          </div>

        </div>
      </div>

      <!-- ===== BLOC 2 : SÉCURITÉ ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5]">
          <h2 class="text-[14px] font-bold text-ink">Sécurité</h2>
        </div>
        <div class="p-6">

          <div class="flex items-center justify-between">
            <div class="flex items-center gap-3">
              <div class="w-9 h-9 bg-[#F4F4ED] rounded-lg flex items-center justify-center">
                <svg class="w-4 h-4 text-[#5F5E5A]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z"/>
                </svg>
              </div>
              <div>
                <div class="text-[13px] font-semibold text-ink">Mot de passe</div>
                <div class="text-[12px] text-[#73726C]">••••••••••••</div>
              </div>
            </div>
            <button @click="showPasswordModal = true"
                    class="text-[12px] font-semibold text-ink bg-[#F4F4ED] hover:bg-[#EBEBE5] px-3 py-1.5 rounded-lg transition">
              Modifier
            </button>
          </div>

        </div>
      </div>

      <!-- ===== BLOC 3 : CONFIDENTIALITÉ ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5]">
          <h2 class="text-[14px] font-bold text-ink">Confidentialité</h2>
        </div>
        <div class="p-6 space-y-4">

          <!-- publicProfile -->
          <div class="flex items-center justify-between">
            <div>
              <div class="text-[13px] font-semibold text-ink">Profil public</div>
              <div class="text-[12px] text-[#73726C] mt-0.5">Visible par tous les clients</div>
            </div>
            <button @click="privacy.publicProfile = !privacy.publicProfile"
                    class="relative w-10 h-5 rounded-full transition-colors flex-shrink-0"
                    :class="privacy.publicProfile ? 'bg-brand-500' : 'bg-[#D3D1C7]'">
              <span class="absolute top-0.5 left-0.5 w-4 h-4 bg-white rounded-full shadow-sm transition-transform"
                    :class="privacy.publicProfile ? 'translate-x-5' : 'translate-x-0'"></span>
            </button>
          </div>

          <!-- showEarnings (freelancer only) -->
          <div v-if="form.role === 'FREELANCER'" class="flex items-center justify-between">
            <div>
              <div class="text-[13px] font-semibold text-ink">Afficher mes gains</div>
              <div class="text-[12px] text-[#73726C] mt-0.5">Visible sur votre profil public</div>
            </div>
            <button @click="privacy.showEarnings = !privacy.showEarnings"
                    class="relative w-10 h-5 rounded-full transition-colors flex-shrink-0"
                    :class="privacy.showEarnings ? 'bg-brand-500' : 'bg-[#D3D1C7]'">
              <span class="absolute top-0.5 left-0.5 w-4 h-4 bg-white rounded-full shadow-sm transition-transform"
                    :class="privacy.showEarnings ? 'translate-x-5' : 'translate-x-0'"></span>
            </button>
          </div>

          <!-- showOnlineStatus -->
          <div class="flex items-center justify-between">
            <div>
              <div class="text-[13px] font-semibold text-ink">Statut en ligne</div>
              <div class="text-[12px] text-[#73726C] mt-0.5">Les clients voient si vous êtes connecté</div>
            </div>
            <button @click="privacy.showOnlineStatus = !privacy.showOnlineStatus"
                    class="relative w-10 h-5 rounded-full transition-colors flex-shrink-0"
                    :class="privacy.showOnlineStatus ? 'bg-brand-500' : 'bg-[#D3D1C7]'">
              <span class="absolute top-0.5 left-0.5 w-4 h-4 bg-white rounded-full shadow-sm transition-transform"
                    :class="privacy.showOnlineStatus ? 'translate-x-5' : 'translate-x-0'"></span>
            </button>
          </div>

          <div class="flex justify-end pt-2">
            <button @click="savePrivacy" :disabled="savingPrivacy"
                    class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-4 py-2 rounded-lg transition disabled:opacity-50">
              <svg v-if="savingPrivacy" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ savingPrivacy ? 'Enregistrement...' : 'Enregistrer' }}
            </button>
          </div>

        </div>
      </div>

      <!-- ===== BLOC 4 : ZONE DANGEREUSE ===== -->
      <div class="bg-white rounded-xl border border-red-200 overflow-hidden">
        <div class="px-6 py-4 border-b border-red-100 bg-red-50/50">
          <h2 class="text-[14px] font-bold text-red-700">Zone dangereuse</h2>
        </div>
        <div class="p-6 space-y-4">

          <!-- supprimer le compte → DELETE /api/settings/account -->
          <div class="flex items-start justify-between gap-6">
            <div>
              <div class="text-[13px] font-semibold text-ink">Supprimer mon compte</div>
              <div class="text-[12px] text-[#73726C] mt-0.5">
                Toutes vos données seront supprimées définitivement. Action irréversible.
              </div>
              <p v-if="confirmDelete" class="text-[12px] text-red-600 font-medium mt-2">
                ⚠️ Cliquez encore une fois pour confirmer.
              </p>
            </div>
            <button @click="handleDelete" :disabled="deletingAccount"
                    class="text-[12px] font-semibold px-3 py-1.5 rounded-lg transition flex-shrink-0 disabled:opacity-50"
                    :class="confirmDelete
                      ? 'bg-red-600 hover:bg-red-700 text-white'
                      : 'border border-red-300 text-red-600 hover:bg-red-50'">
              <svg v-if="deletingAccount" class="w-3.5 h-3.5 animate-spin inline mr-1" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ deletingAccount ? 'Suppression...' : confirmDelete ? 'Confirmer' : 'Supprimer' }}
            </button>
          </div>

        </div>
      </div>

    </div>

    <!-- ===== TOAST SUCCÈS ===== -->
    <div v-if="toast.show"
         class="fixed bottom-6 right-6 z-50 px-4 py-3 rounded-xl shadow-xl text-[13px] font-medium flex items-center gap-2 transition-all"
         :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
      <svg v-if="toast.type === 'success'" class="w-4 h-4 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
      </svg>
      <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
      </svg>
      {{ toast.message }}
    </div>

    <!-- ===== MODAL MOT DE PASSE ===== -->
    <div v-if="showPasswordModal"
         class="fixed inset-0 bg-ink/50 z-50 flex items-center justify-center p-4"
         @click.self="closeModal">

      <div class="bg-white rounded-2xl w-full max-w-md p-6 shadow-2xl">

        <div class="flex items-center justify-between mb-5">
          <h2 class="text-[15px] font-bold text-ink">Changer le mot de passe</h2>
          <button @click="closeModal" class="p-1.5 hover:bg-[#F4F4ED] rounded-md">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div v-if="modalError"
             class="bg-red-50 border border-red-200 text-red-700 rounded-lg p-3 mb-4 text-[12px]">
          {{ modalError }}
        </div>

        <div class="space-y-4">

          <!-- currentPassword -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              Mot de passe actuel
            </label>
            <div class="relative">
              <input v-model="pwdForm.current"
                     :type="showPwd.current ? 'text' : 'password'"
                     placeholder="Votre mot de passe actuel"
                     class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition pr-10"/>
              <button @click="showPwd.current = !showPwd.current" type="button"
                      class="absolute right-3 top-1/2 -translate-y-1/2 text-[#9C9A92]">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- newPassword -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              Nouveau mot de passe
            </label>
            <div class="relative">
              <input v-model="pwdForm.new"
                     :type="showPwd.new ? 'text' : 'password'"
                     placeholder="Minimum 8 caractères"
                     class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition pr-10"/>
              <button @click="showPwd.new = !showPwd.new" type="button"
                      class="absolute right-3 top-1/2 -translate-y-1/2 text-[#9C9A92]">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- confirm -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              Confirmer
            </label>
            <input v-model="pwdForm.confirm"
                   :type="showPwd.new ? 'text' : 'password'"
                   placeholder="Répétez le nouveau mot de passe"
                   @keyup.enter="changePassword"
                   class="w-full border rounded-lg px-3 py-2.5 text-[13px] outline-none transition"
                   :class="pwdForm.confirm && pwdForm.new !== pwdForm.confirm
                     ? 'border-red-400' : 'border-[#EBEBE5] focus:border-brand-500'"/>
            <p v-if="pwdForm.confirm && pwdForm.new !== pwdForm.confirm"
               class="text-red-500 text-[11px] mt-1">
              Les mots de passe ne correspondent pas
            </p>
          </div>

        </div>

        <div class="flex gap-3 mt-6">
          <button @click="closeModal"
                  class="flex-1 py-2.5 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-lg transition">
            Annuler
          </button>
          <button @click="changePassword" :disabled="savingPassword"
                  class="flex-1 py-2.5 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
            <svg v-if="savingPassword" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ savingPassword ? 'Modification...' : 'Confirmer' }}
          </button>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081'

const router = useRouter()

// ===== ÉTAT =====
const loading = ref(true)
const savingAccount = ref(false)
const savingPrivacy = ref(false)
const savingPassword = ref(false)
const deletingAccount = ref(false)
const confirmDelete = ref(false)
const showPasswordModal = ref(false)
const modalError = ref('')

// ===== TOAST =====
const toast = ref({ show: false, message: '', type: 'success' })

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4000)
}

// ===== FORMULAIRE PRINCIPAL =====
// GET /api/settings → UserSettingsDTO
const form = ref({
  email: '',
  firstName: '',
  lastName: '',
  phone: '',
  language: 'fr',
  currency: 'MAD',
  role: '',
})

// ===== CONFIDENTIALITÉ =====
// PUT /api/settings/privacy → UpdatePrivacyRequest
const privacy = ref({
  publicProfile: true,
  showEarnings: false,
  showOnlineStatus: true,
})

// ===== MOT DE PASSE =====
// PUT /api/settings/password → UpdatePasswordRequest
const pwdForm = ref({ current: '', new: '', confirm: '' })
const showPwd = ref({ current: false, new: false })

// ===== AXIOS HEADER =====
const headers = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
})

// ===== CHARGER → GET /api/settings =====
const loadSettings = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/settings`, headers())

    form.value = {
      email: data.email || '',
      firstName: data.firstName || '',
      lastName: data.lastName || '',
      phone: data.phone || '',
      language: data.language || 'fr',
      currency: data.currency || 'MAD',
      role: data.role || '',
    }

    privacy.value = {
      publicProfile: data.publicProfile ?? true,
      showEarnings: data.showEarnings ?? false,
      showOnlineStatus: data.showOnlineStatus ?? true,
    }

  } catch (err) {
    console.error('loadSettings error:', err)
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    form.value.email = user.email || ''
    form.value.role = user.role || ''
    showToast('Impossible de charger les paramètres', 'error')
  } finally {
    loading.value = false
  }
}

// ===== SAUVEGARDER COMPTE → PUT /api/settings/account =====
// body : UpdateAccountRequest { phone, language, currency }
const saveAccount = async () => {
  savingAccount.value = true
  try {
    await axios.put(
        `${API_URL}/api/settings/account`,
        {
          phone: form.value.phone,
          language: form.value.language,
          currency: form.value.currency,
        },
        headers()
    )
    showToast('Informations enregistrées !')
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la sauvegarde', 'error')
  } finally {
    savingAccount.value = false
  }
}

const changePassword = async () => {
  modalError.value = ''

  if (!pwdForm.value.current) {
    modalError.value = 'Entrez votre mot de passe actuel'
    return
  }
  if (pwdForm.value.new.length < 8) {
    modalError.value = 'Minimum 8 caractères'
    return
  }
  if (pwdForm.value.new !== pwdForm.value.confirm) {
    modalError.value = 'Les mots de passe ne correspondent pas'
    return
  }

  savingPassword.value = true
  try {
    await axios.put(
        `${API_URL}/api/settings/password`,
        {
          currentPassword: pwdForm.value.current,
          newPassword: pwdForm.value.new,
        },
        headers()
    )
    showToast('Mot de passe modifié !')
    closeModal()
  } catch (err) {
    modalError.value = err.response?.data?.message || 'Mot de passe actuel incorrect'
  } finally {
    savingPassword.value = false
  }
}

// ===== SAUVEGARDER CONFIDENTIALITÉ → PUT /api/settings/privacy =====
// body : UpdatePrivacyRequest { publicProfile, showEarnings, showOnlineStatus }
const savePrivacy = async () => {
  savingPrivacy.value = true
  try {
    await axios.put(
        `${API_URL}/api/settings/privacy`,
        {
          publicProfile: privacy.value.publicProfile,
          showEarnings: privacy.value.showEarnings,
          showOnlineStatus: privacy.value.showOnlineStatus,
        },
        headers()
    )
    showToast('Préférences enregistrées !')
  } catch (err) {
    showToast('Erreur lors de la sauvegarde', 'error')
  } finally {
    savingPrivacy.value = false
  }
}

const handleDelete = async () => {

  if (!confirmDelete.value) {
    confirmDelete.value = true
    setTimeout(() => { confirmDelete.value = false }, 5000)
    return
  }

  deletingAccount.value = true
  try {

    await axios.delete(
        `${API_URL}/api/settings/account`,
        headers()
    )
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    window.location.href = '/'   // ← utilise window.location au lieu de router.push
  } catch (err) {
    console.error('Delete error:', err)
    showToast(
        err.response?.data?.message || 'Erreur lors de la suppression',
        'error'
    )
    confirmDelete.value = false
    deletingAccount.value = false
  }
}
// ===== FERMER MODAL =====
const closeModal = () => {
  showPasswordModal.value = false
  modalError.value = ''
  pwdForm.value = { current: '', new: '', confirm: '' }
  showPwd.value = { current: false, new: false }
}

onMounted(() => {
  loadSettings()
})
</script>