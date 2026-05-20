<template>
  <div class="space-y-6">

    <!-- HEADER -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-[20px] font-semibold text-ink tracking-tight">Mes candidatures</h1>
        <p class="text-[13px] text-[#9C9A92] mt-0.5">
          {{ loading ? '…' : `${applications.length} candidature${applications.length !== 1 ? 's' : ''}` }}
        </p>
      </div>
      <RouterLink to="/freelancer/explore"
                  class="flex items-center gap-1.5 text-[13px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-4.35-4.35M17 11A6 6 0 115 11a6 6 0 0112 0z"/>
        </svg>
        Explorer les offres
      </RouterLink>
    </div>

    <!-- TABS -->
    <div class="flex gap-1 bg-[#F4F4ED] p-1 rounded-lg w-fit">
      <button v-for="tab in tabs" :key="tab.value" @click="activeTab = tab.value"
              class="text-[13px] font-medium px-3.5 py-1.5 rounded-md transition"
              :class="activeTab === tab.value ? 'bg-white text-ink shadow-sm' : 'text-[#73726C] hover:text-ink'">
        {{ tab.label }}
        <span class="ml-1 text-[11px] text-[#9C9A92]">({{ tab.count }})</span>
      </button>
    </div>

    <!-- SKELETON -->
    <div v-if="loading" class="space-y-3">
      <div v-for="i in 4" :key="i" class="bg-white border border-[#EBEBE5] rounded-xl p-5 animate-pulse">
        <div class="flex items-start justify-between gap-4">
          <div class="space-y-2 flex-1">
            <div class="h-4 bg-[#F4F4ED] rounded w-2/5"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-1/3"></div>
          </div>
          <div class="h-6 bg-[#F4F4ED] rounded-full w-24"></div>
        </div>
        <div class="flex gap-3 mt-5 pt-4 border-t border-[#F4F4ED]">
          <div class="h-7 w-24 bg-[#F4F4ED] rounded-md"></div>
          <div class="h-7 w-32 bg-[#F4F4ED] rounded-md"></div>
        </div>
      </div>
    </div>

    <!-- EMPTY -->
    <div v-else-if="filteredApplications.length === 0"
         class="bg-white border border-[#EBEBE5] rounded-xl p-14 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">Aucune candidature</p>
      <p class="text-[12px] text-[#9C9A92] mt-1">
        {{ activeTab === 'ALL' ? 'Postulez à des missions pour les voir apparaître ici' : 'Aucune candidature avec ce statut' }}
      </p>
      <RouterLink v-if="activeTab === 'ALL'" to="/freelancer/explore"
                  class="inline-block mt-5 text-[13px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- LISTE -->
    <div v-else class="space-y-3">
      <div v-for="app in filteredApplications" :key="app.id"
           class="bg-white border border-[#EBEBE5] rounded-xl p-6 hover:border-[#D3D1C7] transition">

        <!-- Ligne principale -->
        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 min-w-0">
            <p class="text-[17px] font-semibold text-ink leading-snug">
              {{ app.offerTitle || 'Offre #' + app.offerId }}
            </p>
            <div class="flex flex-wrap items-center gap-x-5 gap-y-1.5 mt-2.5">
              <span class="flex items-center gap-1.5 text-[13px] text-[#9C9A92]">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                </svg>
                Postulé {{ timeAgo(app.createdAt) }}
              </span>
              <span v-if="app.proposedBudget" class="flex items-center gap-1.5 text-[13px] font-semibold text-ink">
                <svg class="w-4 h-4 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33"/>
                </svg>
                {{ formatAmount(app.proposedBudget) }} DH proposés
              </span>
              <span v-if="app.proposedDays" class="flex items-center gap-1.5 text-[13px] text-[#9C9A92]">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
                {{ app.proposedDays }} jours estimés
              </span>
            </div>
          </div>

          <!-- Badge statut -->
          <span class="flex-shrink-0 text-[13px] font-semibold px-3.5 py-1.5 rounded-full border"
                :class="statusBadge(app.status)">
            {{ statusLabel(app.status) }}
          </span>
        </div>

        <!-- Actions -->
        <div class="flex items-center gap-2 mt-5 pt-4 border-t border-[#EBEBE5]">
          <button @click="viewOffer(app.offerId)"
                  class="flex items-center gap-2 text-[13px] font-medium text-[#5F5E5A] hover:text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
            </svg>
            Voir l'offre
          </button>
          <button v-if="app.coverLetter" @click="toggleLetter(app.id)"
                  class="flex items-center gap-2 text-[13px] font-medium text-[#5F5E5A] hover:text-ink px-4 py-2 rounded-lg hover:bg-[#F4F4ED] transition">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
            </svg>
            {{ expandedLetters.includes(app.id) ? 'Masquer la lettre' : 'Lettre de motivation' }}
          </button>
          <button v-if="app.status === 'PENDING'" @click="confirmWithdraw(app)"
                  class="ml-auto flex items-center gap-2 text-[13px] font-medium text-red-500 hover:text-red-600 hover:bg-red-50 px-4 py-2 rounded-lg transition">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
            </svg>
            Retirer la candidature
          </button>
        </div>

        <!-- Lettre de motivation dépliable -->
        <div v-if="app.coverLetter && expandedLetters.includes(app.id)"
             class="mt-4 p-4 bg-[#FAFAF7] border border-[#EBEBE5] rounded-xl text-[14px] text-[#5F5E5A] leading-relaxed whitespace-pre-line">
          {{ app.coverLetter }}
        </div>
      </div>
    </div>

    <!-- MODAL RETIRER -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="showWithdrawModal" class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4"
           @click.self="showWithdrawModal = false">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl p-6 max-w-sm w-full" @click.stop>
          <p class="text-[15px] font-semibold text-ink">Retirer cette candidature ?</p>
          <p class="text-[13px] text-[#73726C] mt-1.5 truncate font-medium">{{ appToWithdraw?.offerTitle }}</p>
          <p class="text-[12px] text-[#9C9A92] mt-1">Cette action est irréversible.</p>
          <div class="flex gap-3 mt-5">
            <button @click="showWithdrawModal = false"
                    class="flex-1 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] py-2.5 rounded-lg transition">
              Annuler
            </button>
            <button @click="withdrawApplication" :disabled="withdrawing"
                    class="flex-1 text-[13px] font-medium bg-red-600 hover:bg-red-700 text-white py-2.5 rounded-lg transition disabled:opacity-50">
              {{ withdrawing ? '…' : 'Confirmer' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
    </Teleport>

    <!-- MODAL DÉTAIL OFFRE -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="selectedOffer !== null" class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4"
           @click.self="selectedOffer = null">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl w-full max-w-lg max-h-[85vh] flex flex-col" @click.stop>

          <div class="flex items-center justify-between px-6 py-4 border-b border-[#EBEBE5] flex-shrink-0">
            <p class="text-[15px] font-semibold text-ink">Détail de l'offre</p>
            <button @click="selectedOffer = null" class="p-1.5 hover:bg-[#F4F4ED] rounded-md transition">
              <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
          </div>

          <div class="overflow-y-auto flex-1 p-6">
            <div v-if="loadingOffer" class="space-y-3 animate-pulse">
              <div class="h-5 bg-[#F4F4ED] rounded w-3/4"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-full mt-3"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-2/3"></div>
            </div>
            <div v-else-if="selectedOffer?.id" class="space-y-5">
              <div>
                <p class="text-[17px] font-semibold text-ink mb-2">{{ selectedOffer.title }}</p>
                <p class="text-[13px] text-[#5F5E5A] leading-relaxed">{{ selectedOffer.description }}</p>
              </div>
              <div class="grid grid-cols-2 gap-3">
                <div class="bg-[#FAFAF7] border border-[#EBEBE5] rounded-xl p-4">
                  <p class="text-[11px] text-[#9C9A92] mb-1">Budget client</p>
                  <p class="text-[14px] font-semibold text-ink">{{ formatBudget(selectedOffer.budgetMin, selectedOffer.budgetMax) }}</p>
                </div>
                <div class="bg-[#FAFAF7] border border-[#EBEBE5] rounded-xl p-4">
                  <p class="text-[11px] text-[#9C9A92] mb-1">Deadline</p>
                  <p class="text-[14px] font-semibold text-ink">{{ selectedOffer.deadline ? formatDate(selectedOffer.deadline) : '—' }}</p>
                </div>
              </div>
              <div v-if="selectedOffer.requiredSkills">
                <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2.5">Compétences requises</p>
                <div class="flex flex-wrap gap-1.5">
                  <span v-for="skill in getSkillTags(selectedOffer.requiredSkills)" :key="skill"
                        class="text-[12px] border border-[#EBEBE5] text-[#5F5E5A] px-2.5 py-1 rounded-md">
                    {{ skill }}
                  </span>
                </div>
              </div>
              <p class="text-[12px] text-[#9C9A92]">
                {{ selectedOffer.applicationsCount || 0 }} candidat(s) · Publiée {{ timeAgo(selectedOffer.createdAt) }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </Transition>
    </Teleport>

    <!-- TOAST -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast.show"
           class="fixed bottom-5 left-1/2 -translate-x-1/2 text-[13px] font-medium px-4 py-2.5 rounded-xl shadow-lg z-50"
           :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
        {{ toast.message }}
      </div>
    </Transition>
    </Teleport>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const authHeaders = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token') || sessionStorage.getItem('token')}` } })

const loading           = ref(true)
const withdrawing       = ref(false)
const loadingOffer      = ref(false)
const showWithdrawModal = ref(false)
const appToWithdraw     = ref(null)
const selectedOffer     = ref(null)
const activeTab         = ref('ALL')
const expandedLetters   = ref([])
const applications      = ref([])
const toast             = ref({ show: false, message: '', type: 'success' })

const tabs = computed(() => [
  { value: 'ALL',       label: 'Toutes',     count: applications.value.length },
  { value: 'PENDING',   label: 'En attente', count: applications.value.filter(a => a.status === 'PENDING').length },
  { value: 'ACCEPTED',  label: 'Acceptées',  count: applications.value.filter(a => a.status === 'ACCEPTED').length },
  { value: 'REJECTED',  label: 'Refusées',   count: applications.value.filter(a => a.status === 'REJECTED').length },
  { value: 'WITHDRAWN', label: 'Retirées',   count: applications.value.filter(a => a.status === 'WITHDRAWN').length },
])

const filteredApplications = computed(() =>
  activeTab.value === 'ALL'
    ? applications.value
    : applications.value.filter(a => a.status === activeTab.value)
)

const statusLabel = (s) => ({
  PENDING:             'En attente',
  ACCEPTED:            'Acceptée',
  REJECTED:            'Refusée',
  WITHDRAWN:           'Retirée',
  AWAITING_VALIDATION: 'En validation',
  COMPLETED:           'Terminée',
}[s] || s)

const statusBadge = (s) => ({
  PENDING:             'bg-amber-50 text-amber-700 border-amber-100',
  ACCEPTED:            'bg-green-50 text-green-600 border-green-100',
  REJECTED:            'bg-red-50 text-red-600 border-red-100',
  WITHDRAWN:           'bg-[#F4F4ED] text-[#73726C] border-[#EBEBE5]',
  AWAITING_VALIDATION: 'bg-blue-50 text-blue-600 border-blue-100',
  COMPLETED:           'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]',
}[s] || 'bg-[#F4F4ED] text-[#73726C] border-[#EBEBE5]')

const formatAmount = (val) => Number(val || 0).toLocaleString('fr-MA')

const formatBudget = (min, max) => {
  if (!min && !max) return 'À négocier'
  if (min && max)   return `${formatAmount(min)} – ${formatAmount(max)} DH`
  if (min)          return `Dès ${formatAmount(min)} DH`
  return `Jusqu'à ${formatAmount(max)} DH`
}

const formatDate = (iso) =>
  new Date(iso).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' })

const timeAgo = (iso) => {
  if (!iso) return ''
  const diff = Date.now() - new Date(iso).getTime()
  const m = Math.floor(diff / 60000)
  const h = Math.floor(diff / 3600000)
  const d = Math.floor(diff / 86400000)
  if (m < 60)  return `il y a ${m}min`
  if (h < 24)  return `il y a ${h}h`
  if (d < 30)  return `il y a ${d}j`
  return formatDate(iso)
}

const getSkillTags = (skills) =>
  (skills || '').split(',').map(s => s.trim()).filter(Boolean)

const toggleLetter = (id) => {
  const idx = expandedLetters.value.indexOf(id)
  idx === -1 ? expandedLetters.value.push(id) : expandedLetters.value.splice(idx, 1)
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 3000)
}

const loadApplications = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/applications/my`, authHeaders())
    applications.value = data || []
  } catch {
    showToast('Impossible de charger vos candidatures', 'error')
  } finally {
    loading.value = false
  }
}

const viewOffer = async (offerId) => {
  loadingOffer.value = true
  selectedOffer.value = {}
  try {
    const { data } = await axios.get(`${API_URL}/api/offers/${offerId}`)
    selectedOffer.value = data
  } catch {
    showToast("Impossible de charger l'offre", 'error')
    selectedOffer.value = null
  } finally {
    loadingOffer.value = false
  }
}

const confirmWithdraw = (app) => {
  appToWithdraw.value = app
  showWithdrawModal.value = true
}

const withdrawApplication = async () => {
  withdrawing.value = true
  try {
    await axios.delete(`${API_URL}/api/applications/${appToWithdraw.value.id}`, authHeaders())
    const app = applications.value.find(a => a.id === appToWithdraw.value.id)
    if (app) app.status = 'WITHDRAWN'
    showWithdrawModal.value = false
    showToast('Candidature retirée')
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors du retrait', 'error')
  } finally {
    withdrawing.value = false
  }
}

onMounted(loadApplications)
</script>
