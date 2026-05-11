<template>
  <div class="max-w-5xl mx-auto pb-8">

    <!-- ─── HEADER ──────────────────────────────────────────── -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-xl font-bold text-ink">Candidatures</h1>
        <p class="text-sm text-[#9C9A92] mt-0.5">{{ applications.length }} au total</p>
      </div>
      <RouterLink to="/freelancer/explore"
                  class="text-sm font-medium text-ink border border-[#EBEBE5] hover:bg-[#F4F4ED] px-4 py-2 rounded-lg transition">
        Explorer les missions
      </RouterLink>
    </div>

    <!-- ─── ONGLETS STATUT ──────────────────────────────────── -->
    <div class="flex items-center gap-1 border-b border-[#EBEBE5] mb-6">
      <button
        v-for="f in statusFilters" :key="f.value"
        @click="selectedStatus = f.value"
        class="relative pb-3 px-3 text-sm transition"
        :class="selectedStatus === f.value
          ? 'font-semibold text-ink'
          : 'font-medium text-[#9C9A92] hover:text-ink'"
      >
        {{ f.label }}
        <span class="ml-1.5 text-xs tabular-nums"
              :class="selectedStatus === f.value ? 'text-[#73726C]' : 'text-[#C4C3BC]'">
          {{ getCountByStatus(f.value) }}
        </span>
        <span v-if="selectedStatus === f.value"
              class="absolute bottom-0 left-0 right-0 h-0.5 bg-ink rounded-t-full"></span>
      </button>
    </div>

    <!-- ─── CHARGEMENT ──────────────────────────────────────── -->
    <div v-if="loading" class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden divide-y divide-[#EBEBE5]">
      <div v-for="i in 5" :key="i" class="flex items-center gap-4 px-5 py-4 animate-pulse">
        <div class="w-2 h-2 rounded-full bg-[#E5E5E0] flex-shrink-0"></div>
        <div class="flex-1">
          <div class="h-3 bg-[#F4F4ED] rounded w-2/3 mb-2"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-1/3"></div>
        </div>
        <div class="h-5 w-20 bg-[#F4F4ED] rounded-full"></div>
        <div class="h-3 w-16 bg-[#F4F4ED] rounded"></div>
      </div>
    </div>

    <!-- ─── ÉTAT VIDE ────────────────────────────────────────── -->
    <div v-else-if="filteredApplications.length === 0"
         class="bg-white border border-[#EBEBE5] rounded-xl py-16 text-center">
      <p class="text-sm font-semibold text-ink mb-1">Aucune candidature</p>
      <p class="text-xs text-[#9C9A92] mb-4">
        {{ selectedStatus === 'ALL'
          ? 'Postulez à des missions pour les voir apparaître ici'
          : 'Aucune candidature avec ce statut' }}
      </p>
      <RouterLink v-if="selectedStatus === 'ALL'" to="/freelancer/explore"
                  class="text-xs font-medium text-brand-600 border border-brand-200 hover:bg-brand-50 px-4 py-2 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- ─── LISTE ────────────────────────────────────────────── -->
    <div v-else class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">

      <div class="divide-y divide-[#EBEBE5]">
        <div v-for="app in filteredApplications" :key="app.id">

          <!-- ligne principale -->
          <div class="flex items-start gap-4 px-5 py-4 hover:bg-[#FAFAF7] transition">

            <!-- indicateur statut -->
            <div class="flex-shrink-0 mt-1.5">
              <span class="block w-2 h-2 rounded-full" :class="statusDot(app.status)"></span>
            </div>

            <!-- contenu -->
            <div class="flex-1 min-w-0">

              <div class="flex items-start justify-between gap-4">
                <div class="min-w-0">
                  <p class="text-sm font-semibold text-ink truncate">
                    {{ app.offerTitle || 'Mission #' + app.offerId }}
                  </p>
                  <div class="flex items-center gap-3 mt-1 text-xs text-[#9C9A92]">
                    <span>Postulé {{ timeAgo(app.createdAt) }}</span>
                    <span v-if="app.proposedBudget">·  {{ formatAmount(app.proposedBudget) }} DH proposés</span>
                    <span v-if="app.proposedDays">·  {{ app.proposedDays }}j estimés</span>
                  </div>
                </div>

                <div class="flex items-center gap-2 flex-shrink-0">
                  <span class="text-xs font-medium px-2.5 py-1 rounded-full"
                        :class="statusBadge(app.status)">
                    {{ statusLabel(app.status) }}
                  </span>
                </div>
              </div>

              <!-- actions -->
              <div class="flex items-center gap-2 mt-3">
                <button @click="viewOffer(app.offerId)"
                        class="text-xs text-[#73726C] hover:text-ink border border-[#EBEBE5] hover:border-[#D1D1CB] px-3 py-1.5 rounded-md transition">
                  Voir l'offre
                </button>
                <button v-if="app.coverLetter"
                        @click="toggleLetter(app.id)"
                        class="text-xs text-[#73726C] hover:text-ink px-3 py-1.5 rounded-md hover:bg-[#F4F4ED] transition">
                  {{ expandedLetters.includes(app.id) ? 'Masquer la lettre' : 'Lettre de motivation' }}
                </button>
                <button v-if="app.status === 'PENDING'"
                        @click="confirmWithdraw(app)"
                        class="text-xs text-red-500 hover:text-red-700 px-3 py-1.5 rounded-md hover:bg-red-50 transition ml-auto">
                  Retirer
                </button>
              </div>

              <!-- lettre de motivation -->
              <div v-if="app.coverLetter && expandedLetters.includes(app.id)"
                   class="mt-3 p-3 bg-[#FAFAF7] border border-[#EBEBE5] rounded-lg text-xs text-[#5F5E5A] leading-relaxed whitespace-pre-line">
                {{ app.coverLetter }}
              </div>

            </div>
          </div>

        </div>
      </div>

    </div>

    <!-- ─── TOAST ────────────────────────────────────────────── -->
    <Transition enter-from-class="opacity-0 translate-y-2"
                enter-active-class="transition duration-200"
                leave-to-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150">
      <div v-if="toast.show"
           class="fixed bottom-6 right-6 z-50 flex items-center gap-2.5 px-4 py-3 rounded-xl text-sm font-medium shadow-lg"
           :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
        <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5"
                :d="toast.type === 'success' ? 'M5 13l4 4L19 7' : 'M6 18L18 6M6 6l12 12'"/>
        </svg>
        {{ toast.message }}
      </div>
    </Transition>

    <!-- ─── MODAL RETRAIT ────────────────────────────────────── -->
    <Transition enter-from-class="opacity-0" enter-active-class="transition duration-150"
                leave-to-class="opacity-0" leave-active-class="transition duration-150">
      <div v-if="showWithdrawModal"
           class="fixed inset-0 bg-black/40 z-50 flex items-center justify-center p-4"
           @click.self="showWithdrawModal = false">
        <div class="bg-white rounded-xl w-full max-w-sm p-6 shadow-xl"
             @click.stop>
          <h3 class="text-base font-bold text-ink mb-1">Retirer cette candidature ?</h3>
          <p class="text-sm text-[#73726C] mb-1 truncate">{{ appToWithdraw?.offerTitle }}</p>
          <p class="text-xs text-[#9C9A92] mb-5">Cette action est irréversible.</p>
          <div class="flex gap-2">
            <button @click="showWithdrawModal = false"
                    class="flex-1 py-2.5 text-sm font-medium border border-[#EBEBE5] hover:bg-[#F4F4ED] rounded-lg transition">
              Annuler
            </button>
            <button @click="withdrawApplication" :disabled="withdrawing"
                    class="flex-1 py-2.5 text-sm font-semibold bg-red-600 hover:bg-red-700 text-white rounded-lg transition disabled:opacity-50">
              {{ withdrawing ? 'Retrait...' : 'Confirmer' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- ─── MODAL DÉTAIL OFFRE ───────────────────────────────── -->
    <Transition enter-from-class="opacity-0" enter-active-class="transition duration-150"
                leave-to-class="opacity-0" leave-active-class="transition duration-150">
      <div v-if="selectedOffer !== null"
           class="fixed inset-0 bg-black/40 z-50 flex items-center justify-center p-4"
           @click.self="selectedOffer = null">
        <div class="bg-white rounded-xl w-full max-w-lg shadow-xl max-h-[85vh] flex flex-col"
             @click.stop>

          <!-- en-tête modal -->
          <div class="flex items-center justify-between px-5 py-4 border-b border-[#EBEBE5]">
            <p class="text-sm font-bold text-ink">Détail de l'offre</p>
            <button @click="selectedOffer = null"
                    class="p-1.5 text-[#9C9A92] hover:text-ink hover:bg-[#F4F4ED] rounded-md transition">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
          </div>

          <!-- contenu modal -->
          <div class="overflow-y-auto flex-1">

            <div v-if="loadingOffer" class="p-6 space-y-3 animate-pulse">
              <div class="h-4 bg-[#F4F4ED] rounded w-3/4"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-full"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-2/3"></div>
            </div>

            <div v-else-if="selectedOffer && selectedOffer.id" class="p-5 space-y-5">

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
const authHeaders = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

// ── État ────────────────────────────────────
const loading          = ref(true)
const withdrawing      = ref(false)
const loadingOffer     = ref(false)
const showWithdrawModal = ref(false)
const appToWithdraw    = ref(null)
const selectedOffer    = ref(null)
const selectedStatus   = ref('ALL')
const expandedLetters  = ref([])
const applications     = ref([])
const toast            = ref({ show: false, message: '', type: 'success' })

// ── Filtres ─────────────────────────────────
const statusFilters = [
  { value: 'ALL',       label: 'Toutes' },
  { value: 'PENDING',   label: 'En attente' },
  { value: 'ACCEPTED',  label: 'Acceptées' },
  { value: 'REJECTED',  label: 'Refusées' },
  { value: 'WITHDRAWN', label: 'Retirées' },
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

// ── Helpers visuels ─────────────────────────
const statusLabel = (s) =>
  ({ PENDING: 'En attente', ACCEPTED: 'Acceptée', REJECTED: 'Refusée', WITHDRAWN: 'Retirée' }[s] || s)

const statusBadge = (s) => ({
  PENDING:   'bg-amber-50 text-amber-700',
  ACCEPTED:  'bg-green-50 text-green-700',
  REJECTED:  'bg-red-50 text-red-600',
  WITHDRAWN: 'bg-[#F4F4ED] text-[#73726C]',
}[s] || 'bg-[#F4F4ED] text-[#73726C]')

const statusDot = (s) => ({
  PENDING:   'bg-amber-400',
  ACCEPTED:  'bg-green-500',
  REJECTED:  'bg-red-400',
  WITHDRAWN: 'bg-[#C4C3BC]',
}[s] || 'bg-[#C4C3BC]')

// ── Helpers données ─────────────────────────
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

// ── API ─────────────────────────────────────
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
