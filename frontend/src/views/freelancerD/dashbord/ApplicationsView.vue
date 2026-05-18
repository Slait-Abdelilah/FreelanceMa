<template>
  <div class="pb-8">

    <!-- ── En-tête ──────────────────────────────────────────────── -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-xl font-bold text-ink">Candidatures</h1>
        <p class="text-sm text-ink-soft mt-0.5">
          {{ loading ? '…' : `${applications.length} candidature${applications.length > 1 ? 's' : ''} au total` }}
        </p>
      </div>
      <RouterLink to="/freelancer/explore"
                  class="flex items-center gap-1.5 text-sm font-medium text-ink border border-[#EBEBE5] hover:bg-[#F4F4ED] px-4 py-2 rounded-lg transition">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M21 21l-4.35-4.35M17 11A6 6 0 1 1 5 11a6 6 0 0 1 12 0z"/>
        </svg>
        Explorer les missions
      </RouterLink>
    </div>

    <!-- ── Chips stats ───────────────────────────────────────────── -->
    <div class="flex flex-wrap gap-2 mb-6">
      <button v-for="f in statusFilters" :key="f.value"
              @click="selectedStatus = f.value"
              class="flex items-center gap-2 px-3.5 py-2 rounded-lg text-sm font-medium transition border"
              :class="selectedStatus === f.value
                ? `${f.activeBg} ${f.activeText} ${f.activeBorder}`
                : 'bg-white border-[#EBEBE5] text-[#73726C] hover:text-ink hover:border-[#C4C3BC]'">
        <span class="w-1.5 h-1.5 rounded-full flex-shrink-0" :class="f.dot"></span>
        {{ f.label }}
        <span class="tabular-nums text-xs rounded-full px-1.5 py-0.5"
              :class="selectedStatus === f.value ? f.countBg : 'bg-[#F4F4ED] text-[#9C9A92]'">
          {{ getCountByStatus(f.value) }}
        </span>
      </button>
    </div>

    <!-- ── Chargement ────────────────────────────────────────────── -->
    <div v-if="loading" class="space-y-2">
      <div v-for="i in 5" :key="i"
           class="bg-white border border-[#EBEBE5] rounded-xl flex overflow-hidden animate-pulse">
        <div class="w-1 bg-[#E5E5E0] flex-shrink-0"></div>
        <div class="flex-1 px-5 py-4 space-y-2.5">
          <div class="h-3.5 bg-[#F4F4ED] rounded w-1/2"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-1/3"></div>
          <div class="flex gap-2 pt-1">
            <div class="h-7 w-20 bg-[#F4F4ED] rounded-lg"></div>
            <div class="h-7 w-28 bg-[#F4F4ED] rounded-lg"></div>
          </div>
        </div>
        <div class="px-5 py-4 flex items-start">
          <div class="h-6 w-20 bg-[#F4F4ED] rounded-full"></div>
        </div>
      </div>
    </div>

    <!-- ── État vide ─────────────────────────────────────────────── -->
    <div v-else-if="filteredApplications.length === 0"
         class="bg-white border border-[#EBEBE5] rounded-xl py-16 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
        </svg>
      </div>
      <p class="text-sm font-semibold text-ink mb-1">Aucune candidature</p>
      <p class="text-xs text-[#9C9A92] mb-5">
        {{ selectedStatus === 'ALL'
          ? 'Postulez à des missions pour les voir apparaître ici'
          : 'Aucune candidature avec ce statut' }}
      </p>
      <RouterLink v-if="selectedStatus === 'ALL'" to="/freelancer/explore"
                  class="inline-flex items-center gap-1.5 text-xs font-medium text-brand-600
                         border border-brand-200 hover:bg-brand-50 px-4 py-2 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- ── Liste ─────────────────────────────────────────────────── -->
    <div v-else class="space-y-2">
      <div v-for="app in filteredApplications" :key="app.id"
           class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden
                  hover:shadow-sm hover:border-[#D1D1CB] transition group">
        <div class="flex">

          <!-- Bande statut gauche -->
          <div class="w-1 flex-shrink-0" :class="statusStripe(app.status)"></div>

          <!-- Contenu principal -->
          <div class="flex-1 min-w-0 px-5 py-4">
            <div class="flex items-start gap-4">

              <!-- Titre + meta -->
              <div class="flex-1 min-w-0">
                <p class="text-sm font-semibold text-ink leading-snug">
                  {{ app.offerTitle || 'Mission #' + app.offerId }}
                </p>
                <div class="flex flex-wrap items-center gap-x-3 gap-y-1 mt-1.5">
                  <span class="flex items-center gap-1 text-xs text-[#9C9A92]">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                    </svg>
                    Postulé {{ timeAgo(app.createdAt) }}
                  </span>
                  <span v-if="app.proposedBudget"
                        class="flex items-center gap-1 text-xs font-semibold text-brand-600">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"/>
                    </svg>
                    {{ formatAmount(app.proposedBudget) }} DH proposés
                  </span>
                  <span v-if="app.proposedDays"
                        class="flex items-center gap-1 text-xs text-[#9C9A92]">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                    {{ app.proposedDays }}j estimés
                  </span>
                </div>
              </div>

              <!-- Badge statut -->
              <span class="flex-shrink-0 text-xs font-semibold px-2.5 py-1 rounded-full"
                    :class="statusBadge(app.status)">
                {{ statusLabel(app.status) }}
              </span>
            </div>

            <!-- Actions -->
            <div class="flex items-center gap-2 mt-3 pt-3 border-t border-[#F0F0EA]">
              <button @click="viewOffer(app.offerId)"
                      class="flex items-center gap-1.5 text-xs text-[#73726C] hover:text-ink
                             border border-[#EBEBE5] hover:border-[#C4C3BC] bg-white
                             px-3 py-1.5 rounded-lg transition">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                </svg>
                Voir l'offre
              </button>
              <button v-if="app.coverLetter"
                      @click="toggleLetter(app.id)"
                      class="flex items-center gap-1.5 text-xs text-[#73726C] hover:text-ink
                             px-3 py-1.5 rounded-lg hover:bg-[#F4F4ED] transition">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                </svg>
                {{ expandedLetters.includes(app.id) ? 'Masquer' : 'Lettre de motivation' }}
              </button>
              <button v-if="app.status === 'PENDING'"
                      @click="confirmWithdraw(app)"
                      class="ml-auto flex items-center gap-1.5 text-xs text-red-500
                             hover:text-red-600 hover:bg-red-50 px-3 py-1.5 rounded-lg transition">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M6 18L18 6M6 6l12 12"/>
                </svg>
                Retirer
              </button>
            </div>

            <!-- Lettre de motivation -->
            <div v-if="app.coverLetter && expandedLetters.includes(app.id)"
                 class="mt-3 p-4 bg-[#FAFAF7] border border-[#EBEBE5] rounded-lg
                        text-xs text-[#5F5E5A] leading-relaxed whitespace-pre-line">
              {{ app.coverLetter }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ── Toast ─────────────────────────────────────────────────── -->
    <Transition enter-from-class="opacity-0 translate-y-2"
                enter-active-class="transition duration-200"
                leave-to-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150">
      <div v-if="toast.show"
           class="fixed bottom-6 right-6 z-50 flex items-center gap-2.5 px-4 py-3
                  rounded-xl text-sm font-medium shadow-lg"
           :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
        <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5"
                :d="toast.type === 'success' ? 'M5 13l4 4L19 7' : 'M6 18L18 6M6 6l12 12'"/>
        </svg>
        {{ toast.message }}
      </div>
    </Transition>

    <!-- ── Modal retrait ─────────────────────────────────────────── -->
    <Transition enter-from-class="opacity-0" enter-active-class="transition duration-150"
                leave-to-class="opacity-0" leave-active-class="transition duration-150">
      <div v-if="showWithdrawModal"
           class="fixed inset-0 bg-black/40 z-50 flex items-center justify-center p-4"
           @click.self="showWithdrawModal = false">
        <div class="bg-white rounded-xl w-full max-w-sm p-6 shadow-xl" @click.stop>
          <h3 class="text-base font-bold text-ink mb-1">Retirer cette candidature ?</h3>
          <p class="text-sm text-[#73726C] mb-1 truncate">{{ appToWithdraw?.offerTitle }}</p>
          <p class="text-xs text-[#9C9A92] mb-5">Cette action est irréversible.</p>
          <div class="flex gap-2">
            <button @click="showWithdrawModal = false"
                    class="flex-1 py-2.5 text-sm font-medium border border-[#EBEBE5]
                           hover:bg-[#F4F4ED] rounded-lg transition">
              Annuler
            </button>
            <button @click="withdrawApplication" :disabled="withdrawing"
                    class="flex-1 py-2.5 text-sm font-semibold bg-red-600 hover:bg-red-700
                           text-white rounded-lg transition disabled:opacity-50">
              {{ withdrawing ? 'Retrait…' : 'Confirmer' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- ── Modal détail offre ────────────────────────────────────── -->
    <Transition enter-from-class="opacity-0" enter-active-class="transition duration-150"
                leave-to-class="opacity-0" leave-active-class="transition duration-150">
      <div v-if="selectedOffer !== null"
           class="fixed inset-0 bg-black/40 z-50 flex items-center justify-center p-4"
           @click.self="selectedOffer = null">
        <div class="bg-white rounded-xl w-full max-w-lg shadow-xl max-h-[85vh] flex flex-col"
             @click.stop>
          <div class="flex items-center justify-between px-5 py-4 border-b border-[#EBEBE5]">
            <p class="text-sm font-bold text-ink">Détail de l'offre</p>
            <button @click="selectedOffer = null"
                    class="p-1.5 text-[#9C9A92] hover:text-ink hover:bg-[#F4F4ED] rounded-md transition">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
          </div>
          <div class="overflow-y-auto flex-1">
            <div v-if="loadingOffer" class="p-6 space-y-3 animate-pulse">
              <div class="h-4 bg-[#F4F4ED] rounded w-3/4"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-full"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-2/3"></div>
            </div>
            <div v-else-if="selectedOffer?.id" class="p-5 space-y-5">
              <div>
                <p class="text-base font-bold text-ink mb-2">{{ selectedOffer.title }}</p>
                <p class="text-sm text-[#5F5E5A] leading-relaxed">{{ selectedOffer.description }}</p>
              </div>
              <div class="grid grid-cols-2 gap-3">
                <div class="border border-[#EBEBE5] rounded-lg p-3">
                  <p class="text-xs text-[#9C9A92] mb-1">Budget client</p>
                  <p class="text-sm font-semibold text-ink">
                    {{ formatBudget(selectedOffer.budgetMin, selectedOffer.budgetMax) }} DH
                  </p>
                </div>
                <div class="border border-[#EBEBE5] rounded-lg p-3">
                  <p class="text-xs text-[#9C9A92] mb-1">Deadline</p>
                  <p class="text-sm font-semibold text-ink">
                    {{ selectedOffer.deadline ? formatDate(selectedOffer.deadline) : '—' }}
                  </p>
                </div>
              </div>
              <div v-if="selectedOffer.requiredSkills">
                <p class="text-xs font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">
                  Compétences requises
                </p>
                <div class="flex flex-wrap gap-1.5">
                  <span v-for="skill in getSkillTags(selectedOffer.requiredSkills)" :key="skill"
                        class="text-xs border border-[#EBEBE5] text-[#5F5E5A] px-2.5 py-1 rounded-md">
                    {{ skill }}
                  </span>
                </div>
              </div>
              <p class="text-xs text-[#9C9A92]">
                {{ selectedOffer.applicationsCount || 0 }} candidat(s) · Publiée {{ timeAgo(selectedOffer.createdAt) }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </Transition>

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
const selectedStatus    = ref('ALL')
const expandedLetters   = ref([])
const applications      = ref([])
const toast             = ref({ show: false, message: '', type: 'success' })

const statusFilters = [
  {
    value: 'ALL',       label: 'Toutes',
    dot: 'bg-[#C4C3BC]',
    activeBg: 'bg-ink', activeText: 'text-white', activeBorder: 'border-ink',
    countBg: 'bg-white/20 text-white',
  },
  {
    value: 'PENDING',   label: 'En attente',
    dot: 'bg-amber-400',
    activeBg: 'bg-amber-50', activeText: 'text-amber-700', activeBorder: 'border-amber-200',
    countBg: 'bg-amber-100 text-amber-700',
  },
  {
    value: 'ACCEPTED',  label: 'Acceptées',
    dot: 'bg-brand-500',
    activeBg: 'bg-brand-50', activeText: 'text-brand-700', activeBorder: 'border-brand-200',
    countBg: 'bg-brand-100 text-brand-700',
  },
  {
    value: 'REJECTED',  label: 'Refusées',
    dot: 'bg-red-400',
    activeBg: 'bg-red-50', activeText: 'text-red-700', activeBorder: 'border-red-200',
    countBg: 'bg-red-100 text-red-700',
  },
  {
    value: 'WITHDRAWN', label: 'Retirées',
    dot: 'bg-[#C4C3BC]',
    activeBg: 'bg-[#F4F4ED]', activeText: 'text-[#5F5E5A]', activeBorder: 'border-[#EBEBE5]',
    countBg: 'bg-[#EBEBE5] text-[#73726C]',
  },
]

const filteredApplications = computed(() =>
  selectedStatus.value === 'ALL'
    ? applications.value
    : applications.value.filter(a => a.status === selectedStatus.value)
)

const getCountByStatus = (status) =>
  status === 'ALL'
    ? applications.value.length
    : applications.value.filter(a => a.status === status).length

const statusLabel = (s) => ({
  PENDING:            'En attente',
  ACCEPTED:           'Acceptée',
  REJECTED:           'Refusée',
  WITHDRAWN:          'Retirée',
  AWAITING_VALIDATION:'En validation',
  COMPLETED:          'Terminée',
}[s] || s)

const statusBadge = (s) => ({
  PENDING:            'bg-amber-50 text-amber-700',
  ACCEPTED:           'bg-brand-50 text-brand-700',
  REJECTED:           'bg-red-50 text-red-600',
  WITHDRAWN:          'bg-[#F4F4ED] text-[#73726C]',
  AWAITING_VALIDATION:'bg-blue-50 text-blue-600',
  COMPLETED:          'bg-[#F4F4ED] text-[#5F5E5A]',
}[s] || 'bg-[#F4F4ED] text-[#73726C]')

const statusStripe = (s) => ({
  PENDING:            'bg-amber-400',
  ACCEPTED:           'bg-brand-500',
  REJECTED:           'bg-red-400',
  WITHDRAWN:          'bg-[#DDDDD6]',
  AWAITING_VALIDATION:'bg-blue-400',
  COMPLETED:          'bg-[#C4C3BC]',
}[s] || 'bg-[#DDDDD6]')

const formatAmount = (val) => Number(val || 0).toLocaleString('fr-MA')

const formatBudget = (min, max) => {
  if (!min && !max) return 'À négocier'
  if (min && max)   return `${formatAmount(min)} – ${formatAmount(max)}`
  if (min)          return `Dès ${formatAmount(min)}`
  return `Jusqu'à ${formatAmount(max)}`
}

const formatDate = (iso) => {
  if (!iso) return '—'
  return new Date(iso).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' })
}

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
  setTimeout(() => { toast.value.show = false }, 3500)
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
