<template>
  <div class="space-y-5 max-w-2xl">

    <!-- HEADER -->
    <div>
      <h1 class="text-[20px] font-semibold text-ink tracking-tight">Paramètres</h1>
      <p class="text-[13px] text-[#9C9A92] mt-0.5">Gérez votre compte et vos préférences</p>
    </div>

    <!-- ── INFORMATIONS DU COMPTE ───────────────────────────────────────── -->
    <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
      <div class="px-5 py-4 border-b border-[#EBEBE5]">
        <p class="text-[14px] font-semibold text-ink">Informations du compte</p>
      </div>
      <div class="px-5 py-5 space-y-4">

        <div v-if="loadingSettings" class="space-y-4 animate-pulse">
          <div v-for="i in 4" :key="i" class="space-y-1.5">
            <div class="h-3 bg-[#F4F4ED] rounded w-1/4"></div>
            <div class="h-10 bg-[#F4F4ED] rounded-lg"></div>
          </div>
        </div>

        <template v-else>
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="text-xs text-[#9C9A92] block mb-1.5">Prénom</label>
              <input :value="settings.firstName || '—'" readonly
                     class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-[#73726C] bg-[#FAFAF7] cursor-default"/>
            </div>
            <div>
              <label class="text-xs text-[#9C9A92] block mb-1.5">Nom</label>
              <input :value="settings.lastName || '—'" readonly
                     class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-[#73726C] bg-[#FAFAF7] cursor-default"/>
            </div>
          </div>

          <div>
            <label class="text-xs text-[#9C9A92] block mb-1.5">Adresse e-mail</label>
            <input :value="settings.email" readonly
                   class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-[#73726C] bg-[#FAFAF7] cursor-default"/>
          </div>

          <div>
            <label class="text-xs text-[#9C9A92] block mb-1.5">Téléphone</label>
            <input v-model="accountForm.phone" type="tel" placeholder="+212 6 00 00 00 00"
                   class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="text-xs text-[#9C9A92] block mb-1.5">Langue</label>
              <select v-model="accountForm.language"
                      class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition bg-white">
                <option value="fr">Français</option>
                <option value="ar">Arabe</option>
                <option value="en">Anglais</option>
              </select>
            </div>
            <div>
              <label class="text-xs text-[#9C9A92] block mb-1.5">Devise</label>
              <select v-model="accountForm.currency"
                      class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition bg-white">
                <option value="MAD">MAD – Dirham</option>
                <option value="EUR">EUR – Euro</option>
                <option value="USD">USD – Dollar</option>
              </select>
            </div>
          </div>

          <div class="flex justify-end pt-1">
            <button @click="saveAccount" :disabled="savingAccount"
                    class="text-[13px] font-medium bg-ink hover:bg-[#1A1A18] text-white px-5 py-2 rounded-lg transition disabled:opacity-50">
              {{ savingAccount ? 'Enregistrement...' : 'Enregistrer' }}
            </button>
          </div>
        </template>
      </div>
    </div>

    <!-- ── CONFIDENTIALITÉ ─────────────────────────────────────────────── -->
    <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
      <div class="px-5 py-4 border-b border-[#EBEBE5]">
        <p class="text-[14px] font-semibold text-ink">Confidentialité</p>
      </div>
      <div class="px-5 py-5 space-y-5">

        <div class="flex items-center justify-between">
          <div>
            <p class="text-[13px] font-medium text-ink">Statut en ligne visible</p>
            <p class="text-[11px] text-[#9C9A92] mt-0.5">Afficher quand vous êtes connecté</p>
          </div>
          <button @click="privacyForm.showOnlineStatus = !privacyForm.showOnlineStatus"
                  class="relative w-10 h-6 rounded-full transition-colors duration-200 focus:outline-none flex-shrink-0"
                  :class="privacyForm.showOnlineStatus ? 'bg-ink' : 'bg-[#D3D1C7]'">
            <span class="absolute top-1 left-1 w-4 h-4 bg-white rounded-full shadow transition-transform duration-200"
                  :class="privacyForm.showOnlineStatus ? 'translate-x-4' : 'translate-x-0'"></span>
          </button>
        </div>

        <div class="flex items-center justify-between">
          <div>
            <p class="text-[13px] font-medium text-ink">Profil public</p>
            <p class="text-[11px] text-[#9C9A92] mt-0.5">Permettre aux freelancers de voir votre profil</p>
          </div>
          <button @click="privacyForm.publicProfile = !privacyForm.publicProfile"
                  class="relative w-10 h-6 rounded-full transition-colors duration-200 focus:outline-none flex-shrink-0"
                  :class="privacyForm.publicProfile ? 'bg-ink' : 'bg-[#D3D1C7]'">
            <span class="absolute top-1 left-1 w-4 h-4 bg-white rounded-full shadow transition-transform duration-200"
                  :class="privacyForm.publicProfile ? 'translate-x-4' : 'translate-x-0'"></span>
          </button>
        </div>

        <div class="flex justify-end pt-1">
          <button @click="savePrivacy" :disabled="savingPrivacy"
                  class="text-[13px] font-medium bg-ink hover:bg-[#1A1A18] text-white px-5 py-2 rounded-lg transition disabled:opacity-50">
            {{ savingPrivacy ? 'Enregistrement...' : 'Enregistrer' }}
          </button>
        </div>
      </div>
    </div>

    <!-- ── SÉCURITÉ ────────────────────────────────────────────────────── -->
    <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
      <div class="px-5 py-4 border-b border-[#EBEBE5]">
        <p class="text-[14px] font-semibold text-ink">Sécurité</p>
      </div>
      <div class="px-5 py-5 space-y-4">
        <div>
          <label class="text-xs text-[#9C9A92] block mb-1.5">Mot de passe actuel</label>
          <input v-model="passwordForm.currentPassword" type="password" placeholder="••••••••"
                 class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
        </div>
        <div>
          <label class="text-xs text-[#9C9A92] block mb-1.5">Nouveau mot de passe</label>
          <input v-model="passwordForm.newPassword" type="password" placeholder="8 caractères minimum"
                 class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
        </div>
        <div>
          <label class="text-xs text-[#9C9A92] block mb-1.5">Confirmer le nouveau mot de passe</label>
          <input v-model="passwordForm.confirmPassword" type="password" placeholder="••••••••"
                 class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
        </div>
        <div class="flex justify-end pt-1">
          <button @click="changePassword" :disabled="savingPassword"
                  class="text-[13px] font-medium bg-ink hover:bg-[#1A1A18] text-white px-5 py-2 rounded-lg transition disabled:opacity-50">
            {{ savingPassword ? 'Modification...' : 'Changer le mot de passe' }}
          </button>
        </div>
      </div>
    </div>

    <!-- ── ZONE DANGEREUSE ─────────────────────────────────────────────── -->
    <div class="bg-white border border-red-100 rounded-xl overflow-hidden">
      <div class="px-5 py-4 border-b border-red-100">
        <p class="text-[14px] font-semibold text-red-600">Zone dangereuse</p>
      </div>
      <div class="px-5 py-5">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-[13px] font-medium text-ink">Supprimer le compte</p>
            <p class="text-[11px] text-[#9C9A92] mt-0.5">Action irréversible — toutes vos données seront effacées définitivement.</p>
          </div>
          <button @click="showDeleteModal = true"
                  class="flex-shrink-0 text-[12px] font-medium text-red-600 border border-red-200 hover:bg-red-50 px-4 py-2 rounded-lg transition">
            Supprimer
          </button>
        </div>
      </div>
    </div>

    <!-- MODAL SUPPRESSION DE COMPTE -->
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="showDeleteModal"
           class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4"
           @click.self="showDeleteModal = false">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl p-6 max-w-sm w-full" @click.stop>
          <div class="w-10 h-10 bg-red-50 rounded-xl flex items-center justify-center mb-4">
            <svg class="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"/>
            </svg>
          </div>
          <p class="text-[15px] font-semibold text-ink">Supprimer votre compte ?</p>
          <p class="text-[13px] text-[#73726C] mt-2">
            Toutes vos offres, candidatures reçues et données seront définitivement supprimées. Cette action est irréversible.
          </p>
          <div class="flex gap-3 mt-6">
            <button @click="showDeleteModal = false"
                    class="flex-1 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] py-2.5 rounded-lg transition">
              Annuler
            </button>
            <button @click="deleteAccount" :disabled="deletingAccount"
                    class="flex-1 text-[13px] font-medium bg-red-600 text-white py-2.5 rounded-lg hover:bg-red-700 transition disabled:opacity-50">
              {{ deletingAccount ? '...' : 'Supprimer définitivement' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- TOAST -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast"
           class="fixed bottom-5 left-1/2 -translate-x-1/2 text-white text-[13px] font-medium px-4 py-2.5 rounded-xl shadow-lg z-50"
           :class="toastType === 'error' ? 'bg-red-600' : 'bg-ink'">
        {{ toast }}
      </div>
    </Transition>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const router    = useRouter()
const authStore = useAuthStore()

const loadingSettings = ref(true)
const savingAccount   = ref(false)
const savingPrivacy   = ref(false)
const savingPassword  = ref(false)
const deletingAccount = ref(false)
const showDeleteModal = ref(false)
const toast     = ref('')
const toastType = ref('success')

const settings    = ref({})
const accountForm = ref({ phone: '', language: 'fr', currency: 'MAD' })
const privacyForm = ref({ showOnlineStatus: true, publicProfile: true })
const passwordForm = ref({ currentPassword: '', newPassword: '', confirmPassword: '' })

const token   = () => localStorage.getItem('token') || sessionStorage.getItem('token')
const headers = () => ({ Authorization: `Bearer ${token()}` })

const loadSettings = async () => {
  loadingSettings.value = true
  try {
    const { data } = await axios.get(`${BASE}/api/settings`, { headers: headers() })
    settings.value = data
    accountForm.value.phone    = data.phone || ''
    accountForm.value.language = data.language || 'fr'
    accountForm.value.currency = data.currency || 'MAD'
    privacyForm.value.showOnlineStatus = data.showOnlineStatus ?? true
    privacyForm.value.publicProfile    = data.publicProfile ?? true
  } catch { /* silencieux */ } finally { loadingSettings.value = false }
}

const saveAccount = async () => {
  savingAccount.value = true
  try {
    await axios.put(`${BASE}/api/settings/account`, accountForm.value, { headers: headers() })
    showToast('Informations mises à jour')
  } catch (e) { showToast(e.response?.data?.message || 'Erreur lors de la mise à jour', 'error')
  } finally { savingAccount.value = false }
}

const savePrivacy = async () => {
  savingPrivacy.value = true
  try {
    await axios.put(`${BASE}/api/settings/privacy`, privacyForm.value, { headers: headers() })
    showToast('Préférences de confidentialité sauvegardées')
  } catch (e) { showToast(e.response?.data?.message || 'Erreur lors de la mise à jour', 'error')
  } finally { savingPrivacy.value = false }
}

const changePassword = async () => {
  if (!passwordForm.value.currentPassword) {
    showToast('Entrez votre mot de passe actuel', 'error'); return
  }
  if (passwordForm.value.newPassword.length < 8) {
    showToast('Le nouveau mot de passe doit contenir au moins 8 caractères', 'error'); return
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    showToast('Les mots de passe ne correspondent pas', 'error'); return
  }
  savingPassword.value = true
  try {
    await axios.put(`${BASE}/api/auth/change-password`, {
      currentPassword: passwordForm.value.currentPassword,
      newPassword:     passwordForm.value.newPassword,
    }, { headers: headers() })
    passwordForm.value = { currentPassword: '', newPassword: '', confirmPassword: '' }
    showToast('Mot de passe modifié avec succès')
  } catch (e) { showToast(e.response?.data?.message || 'Mot de passe actuel incorrect', 'error')
  } finally { savingPassword.value = false }
}

const deleteAccount = async () => {
  deletingAccount.value = true
  try {
    await axios.delete(`${BASE}/api/auth/account`, { headers: headers() })
    await authStore.logout()
    router.push('/')
  } catch (e) { showToast(e.response?.data?.message || 'Erreur lors de la suppression', 'error')
  } finally { deletingAccount.value = false }
}

let toastTimer = null
const showToast = (msg, type = 'success') => {
  toast.value = msg
  toastType.value = type
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => { toast.value = '' }, 3500)
}

onMounted(loadSettings)
</script>
