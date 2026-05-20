<template>
  <div class="flex gap-6 min-h-full">

    <!-- ── SIDEBAR NAV ── -->
    <aside class="w-56 flex-shrink-0">
      <nav class="space-y-0.5 sticky top-0">
        <button v-for="s in sections" :key="s.id"
                @click="activeSection = s.id"
                class="w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-left transition-colors text-[13px]"
                :class="activeSection === s.id
                  ? 'bg-white border border-[#EBEBE5] text-ink font-medium shadow-sm'
                  : 'text-[#73726C] hover:bg-white/60 hover:text-ink'">
          <div class="w-7 h-7 rounded-lg flex items-center justify-center flex-shrink-0"
               :class="activeSection === s.id ? 'bg-[#F4F4ED]' : 'bg-transparent'">
            <component :is="s.icon" class="w-3.5 h-3.5" />
          </div>
          {{ s.label }}
        </button>
      </nav>
    </aside>

    <!-- ── CONTENU ── -->
    <div class="flex-1 min-w-0">

      <!-- skeleton global -->
      <div v-if="loadingSettings" class="bg-white border border-[#EBEBE5] rounded-xl p-6 animate-pulse space-y-4">
        <div v-for="i in 4" :key="i" class="space-y-1.5">
          <div class="h-3 bg-[#F4F4ED] rounded w-1/4"></div>
          <div class="h-10 bg-[#F4F4ED] rounded-lg"></div>
        </div>
      </div>

      <template v-else>

        <!-- ═══ COMPTE ═══════════════════════════════════════════════════ -->
        <div v-if="activeSection === 'account'" class="space-y-5">
          <div>
            <h2 class="text-[16px] font-semibold text-ink">Informations du compte</h2>
            <p class="text-[13px] text-[#9C9A92] mt-0.5">Vos informations personnelles et préférences régionales</p>
          </div>

          <div class="bg-white border border-[#EBEBE5] rounded-xl divide-y divide-[#F4F4ED]">

            <!-- Identité (lecture seule) -->
            <div class="px-6 py-5">
              <p class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-4">Identité</p>
              <div class="flex items-center gap-4">
                <div class="w-14 h-14 rounded-xl bg-[#F4F4ED] flex items-center justify-center text-[18px] font-bold text-ink flex-shrink-0">
                  {{ initials }}
                </div>
                <div>
                  <p class="text-[15px] font-semibold text-ink">{{ settings.firstName }} {{ settings.lastName }}</p>
                  <p class="text-[13px] text-[#9C9A92] mt-0.5">{{ settings.email }}</p>
                </div>
              </div>
            </div>

            <!-- Coordonnées -->
            <div class="px-6 py-5 space-y-4">
              <p class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider">Coordonnées</p>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="text-[12px] text-[#73726C] block mb-1.5">Prénom</label>
                  <input :value="settings.firstName || ''" readonly
                         class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-[#73726C] bg-[#FAFAF7] cursor-default"/>
                </div>
                <div>
                  <label class="text-[12px] text-[#73726C] block mb-1.5">Nom</label>
                  <input :value="settings.lastName || ''" readonly
                         class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-[#73726C] bg-[#FAFAF7] cursor-default"/>
                </div>
              </div>
              <div>
                <label class="text-[12px] text-[#73726C] block mb-1.5">Adresse e-mail</label>
                <input :value="settings.email" readonly
                       class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-[#73726C] bg-[#FAFAF7] cursor-default"/>
              </div>
              <div>
                <label class="text-[12px] text-[#73726C] block mb-1.5">Téléphone</label>
                <input v-model="accountForm.phone" type="tel" placeholder="+212 6 00 00 00 00"
                       class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
              </div>
            </div>

            <!-- Préférences -->
            <div class="px-6 py-5 space-y-4">
              <p class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider">Préférences</p>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="text-[12px] text-[#73726C] block mb-1.5">Langue</label>
                  <select v-model="accountForm.language"
                          class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition bg-white">
                    <option value="fr">Français</option>
                    <option value="ar">Arabe</option>
                    <option value="en">Anglais</option>
                  </select>
                </div>
                <div>
                  <label class="text-[12px] text-[#73726C] block mb-1.5">Devise</label>
                  <select v-model="accountForm.currency"
                          class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition bg-white">
                    <option value="MAD">MAD – Dirham</option>
                    <option value="EUR">EUR – Euro</option>
                    <option value="USD">USD – Dollar</option>
                  </select>
                </div>
              </div>
            </div>

            <!-- Bouton -->
            <div class="px-6 py-4 bg-[#FAFAF7] flex justify-end">
              <button @click="saveAccount" :disabled="savingAccount"
                      class="text-[13px] font-medium bg-ink hover:bg-[#1A1A18] text-white px-5 py-2 rounded-lg transition disabled:opacity-50">
                {{ savingAccount ? 'Enregistrement...' : 'Enregistrer les modifications' }}
              </button>
            </div>
          </div>
        </div>

        <!-- ═══ CONFIDENTIALITÉ ══════════════════════════════════════════ -->
        <div v-else-if="activeSection === 'privacy'" class="space-y-5">
          <div>
            <h2 class="text-[16px] font-semibold text-ink">Confidentialité</h2>
            <p class="text-[13px] text-[#9C9A92] mt-0.5">Contrôlez ce que les autres peuvent voir de vous</p>
          </div>

          <div class="bg-white border border-[#EBEBE5] rounded-xl divide-y divide-[#F4F4ED]">

            <div class="px-6 py-5 flex items-center justify-between gap-6">
              <div>
                <p class="text-[13px] font-medium text-ink">Statut en ligne visible</p>
                <p class="text-[12px] text-[#9C9A92] mt-0.5">Les freelancers voient quand vous êtes connecté</p>
              </div>
              <button @click="privacyForm.showOnlineStatus = !privacyForm.showOnlineStatus"
                      class="relative w-10 h-6 rounded-full transition-colors duration-200 flex-shrink-0"
                      :class="privacyForm.showOnlineStatus ? 'bg-ink' : 'bg-[#D3D1C7]'">
                <span class="absolute top-1 left-1 w-4 h-4 bg-white rounded-full shadow transition-transform duration-200"
                      :class="privacyForm.showOnlineStatus ? 'translate-x-4' : 'translate-x-0'"></span>
              </button>
            </div>

            <div class="px-6 py-5 flex items-center justify-between gap-6">
              <div>
                <p class="text-[13px] font-medium text-ink">Profil public</p>
                <p class="text-[12px] text-[#9C9A92] mt-0.5">Permettre aux freelancers de voir votre profil complet</p>
              </div>
              <button @click="privacyForm.publicProfile = !privacyForm.publicProfile"
                      class="relative w-10 h-6 rounded-full transition-colors duration-200 flex-shrink-0"
                      :class="privacyForm.publicProfile ? 'bg-ink' : 'bg-[#D3D1C7]'">
                <span class="absolute top-1 left-1 w-4 h-4 bg-white rounded-full shadow transition-transform duration-200"
                      :class="privacyForm.publicProfile ? 'translate-x-4' : 'translate-x-0'"></span>
              </button>
            </div>

            <div class="px-6 py-4 bg-[#FAFAF7] flex justify-end">
              <button @click="savePrivacy" :disabled="savingPrivacy"
                      class="text-[13px] font-medium bg-ink hover:bg-[#1A1A18] text-white px-5 py-2 rounded-lg transition disabled:opacity-50">
                {{ savingPrivacy ? 'Enregistrement...' : 'Enregistrer' }}
              </button>
            </div>
          </div>
        </div>

        <!-- ═══ SÉCURITÉ ════════════════════════════════════════════════ -->
        <div v-else-if="activeSection === 'security'" class="space-y-5">
          <div>
            <h2 class="text-[16px] font-semibold text-ink">Sécurité</h2>
            <p class="text-[13px] text-[#9C9A92] mt-0.5">Gérez votre mot de passe et la sécurité de votre compte</p>
          </div>

          <div class="bg-white border border-[#EBEBE5] rounded-xl divide-y divide-[#F4F4ED]">

            <div class="px-6 py-5 space-y-4">
              <p class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider">Changer le mot de passe</p>
              <div>
                <label class="text-[12px] text-[#73726C] block mb-1.5">Mot de passe actuel</label>
                <input v-model="passwordForm.currentPassword" type="password" placeholder="••••••••"
                       class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="text-[12px] text-[#73726C] block mb-1.5">Nouveau mot de passe</label>
                  <input v-model="passwordForm.newPassword" type="password" placeholder="8 caractères minimum"
                         class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
                </div>
                <div>
                  <label class="text-[12px] text-[#73726C] block mb-1.5">Confirmer</label>
                  <input v-model="passwordForm.confirmPassword" type="password" placeholder="••••••••"
                         class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"
                         :class="passwordForm.confirmPassword && passwordForm.newPassword !== passwordForm.confirmPassword
                           ? 'border-red-300 focus:border-red-400' : ''"/>
                </div>
              </div>
              <p v-if="passwordForm.confirmPassword && passwordForm.newPassword !== passwordForm.confirmPassword"
                 class="text-[11px] text-red-500">Les mots de passe ne correspondent pas</p>
            </div>

            <div class="px-6 py-4 bg-[#FAFAF7] flex justify-end">
              <button @click="changePassword" :disabled="savingPassword"
                      class="text-[13px] font-medium bg-ink hover:bg-[#1A1A18] text-white px-5 py-2 rounded-lg transition disabled:opacity-50">
                {{ savingPassword ? 'Modification...' : 'Changer le mot de passe' }}
              </button>
            </div>
          </div>
        </div>

        <!-- ═══ ZONE DANGEREUSE ═════════════════════════════════════════ -->
        <div v-else-if="activeSection === 'danger'" class="space-y-5">
          <div>
            <h2 class="text-[16px] font-semibold text-ink">Supprimer le compte</h2>
            <p class="text-[13px] text-[#9C9A92] mt-0.5">Actions irréversibles sur votre compte</p>
          </div>

          <div class="bg-white border border-[#EBEBE5] rounded-xl divide-y divide-[#F4F4ED]">
            <div class="px-6 py-5 flex items-center justify-between gap-6">
              <div>
                <p class="text-[13px] font-medium text-ink">Supprimer le compte</p>
                <p class="text-[12px] text-[#9C9A92] mt-0.5">Toutes vos offres, candidatures et données seront définitivement effacées.</p>
              </div>
              <button @click="showDeleteModal = true"
                      class="flex-shrink-0 text-[12px] font-medium text-[#73726C] border border-[#EBEBE5] hover:border-[#D3D1C7] hover:text-ink px-4 py-2 rounded-lg transition">
                Supprimer le compte
              </button>
            </div>
          </div>
        </div>

      </template>
    </div>

    <!-- ── MODAL SUPPRESSION ── -->
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="showDeleteModal"
           class="fixed inset-0 bg-black/40 z-50 flex items-center justify-center p-4"
           @click.self="showDeleteModal = false">
        <div class="bg-white rounded-xl border border-[#EBEBE5] shadow-xl w-full max-w-[380px]">
          <div class="px-6 pt-6 pb-5">
            <p class="text-[15px] font-semibold text-ink">Supprimer le compte</p>
            <p class="text-[13px] text-[#73726C] mt-2 leading-relaxed">
              Toutes vos offres, candidatures reçues et données seront définitivement supprimées. Cette action est irréversible.
            </p>
          </div>
          <div class="px-6 py-4 border-t border-[#F4F4ED] flex gap-3">
            <button @click="showDeleteModal = false"
                    class="flex-1 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] py-2.5 rounded-lg transition">
              Annuler
            </button>
            <button @click="deleteAccount" :disabled="deletingAccount"
                    class="flex-1 text-[13px] font-medium bg-ink hover:bg-[#2C2B27] text-white py-2.5 rounded-lg transition disabled:opacity-50">
              {{ deletingAccount ? '...' : 'Supprimer' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- TOAST -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast"
           class="fixed bottom-5 left-1/2 -translate-x-1/2 text-white text-[13px] font-medium px-4 py-2.5 rounded-xl shadow-lg z-50 whitespace-nowrap"
           :class="toastType === 'error' ? 'bg-red-600' : 'bg-ink'">
        {{ toast }}
      </div>
    </Transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, defineComponent, h } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const router    = useRouter()
const authStore = useAuthStore()

const activeSection   = ref('account')
const loadingSettings = ref(true)
const savingAccount   = ref(false)
const savingPrivacy   = ref(false)
const savingPassword  = ref(false)
const deletingAccount = ref(false)
const showDeleteModal = ref(false)
const toast           = ref('')
const toastType       = ref('success')

const settings     = ref({})
const accountForm  = ref({ phone: '', language: 'fr', currency: 'MAD' })
const privacyForm  = ref({ showOnlineStatus: true, publicProfile: true })
const passwordForm = ref({ currentPassword: '', newPassword: '', confirmPassword: '' })

const initials = computed(() => {
  const f = settings.value.firstName?.[0]?.toUpperCase() || ''
  const l = settings.value.lastName?.[0]?.toUpperCase() || ''
  return f + l || settings.value.email?.[0]?.toUpperCase() || '?'
})

// inline SVG icon components
const IconUser = defineComponent({ render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24', 'stroke-width': '2' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0' })
]) })
const IconShield = defineComponent({ render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24', 'stroke-width': '2' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M9 12.75L11.25 15 15 9.75m-3-7.036A11.959 11.959 0 013.598 6 11.99 11.99 0 003 9.749c0 5.592 3.824 10.29 9 11.623 5.176-1.332 9-6.03 9-11.622 0-1.31-.21-2.571-.598-3.751h-.152c-3.196 0-6.1-1.248-8.25-3.285z' })
]) })
const IconLock = defineComponent({ render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24', 'stroke-width': '2' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z' })
]) })
const IconDanger = defineComponent({ render: () => h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24', 'stroke-width': '2' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z' })
]) })

const sections = [
  { id: 'account',  label: 'Compte',           icon: IconUser   },
  { id: 'privacy',  label: 'Confidentialité',  icon: IconShield },
  { id: 'security', label: 'Sécurité',         icon: IconLock   },
  { id: 'danger',   label: 'Zone dangereuse',  icon: IconDanger },
]

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
