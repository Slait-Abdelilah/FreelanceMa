<template>
  <div class="pb-8">

    <!-- ── En-tête ──────────────────────────────────────────────── -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-xl font-bold text-ink">Missions actives</h1>
        <p class="text-sm text-ink-soft mt-0.5">
          {{ loading ? '…' : `${allMissions.length} mission${allMissions.length > 1 ? 's' : ''}` }}
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

    <!-- ── Chips filtres ─────────────────────────────────────────── -->
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
      <div v-for="i in 3" :key="i"
           class="bg-white border border-[#EBEBE5] rounded-xl flex overflow-hidden animate-pulse">
        <div class="w-1 bg-[#E5E5E0] flex-shrink-0"></div>
        <div class="flex-1 px-5 py-4 space-y-2.5">
          <div class="h-3.5 bg-[#F4F4ED] rounded w-1/2"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-1/3"></div>
          <div class="h-1.5 bg-[#F4F4ED] rounded-full w-full mt-3"></div>
          <div class="flex gap-2 pt-1">
            <div class="h-7 w-28 bg-[#F4F4ED] rounded-lg"></div>
            <div class="h-7 w-28 bg-[#F4F4ED] rounded-lg"></div>
          </div>
        </div>
        <div class="px-5 py-4 flex items-start">
          <div class="h-6 w-20 bg-[#F4F4ED] rounded-full"></div>
        </div>
      </div>
    </div>

    <!-- ── État vide ─────────────────────────────────────────────── -->
    <div v-else-if="filteredMissions.length === 0"
         class="bg-white border border-[#EBEBE5] rounded-xl py-16 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
        </svg>
      </div>
      <p class="text-sm font-semibold text-ink mb-1">Aucune mission</p>
      <p class="text-xs text-[#9C9A92] mb-5">
        {{ selectedStatus === 'ALL'
          ? 'Postulez à des offres pour démarrer vos premières missions.'
          : 'Aucune mission avec ce statut.' }}
      </p>
      <RouterLink v-if="selectedStatus === 'ALL'" to="/freelancer/explore"
                  class="inline-flex items-center gap-1.5 text-xs font-medium text-brand-600
                         border border-brand-200 hover:bg-brand-50 px-4 py-2 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- ── Liste ─────────────────────────────────────────────────── -->
    <div v-else class="space-y-2">
      <div v-for="mission in filteredMissions" :key="mission.id"
           class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden
                  hover:shadow-sm hover:border-[#D1D1CB] transition">
        <div class="flex">

          <!-- Bande statut gauche -->
          <div class="w-1 flex-shrink-0" :class="statusStripe(mission.status)"></div>

          <!-- Contenu -->
          <div class="flex-1 min-w-0 px-5 py-4">

            <!-- Titre + badge -->
            <div class="flex items-start gap-4">
              <div class="flex-1 min-w-0">
                <p class="text-sm font-semibold text-ink leading-snug">
                  {{ mission.offerTitle || 'Mission #' + mission.offerId }}
                </p>
                <div class="flex flex-wrap items-center gap-x-3 gap-y-1 mt-1.5">
                  <span class="flex items-center gap-1 text-xs text-[#9C9A92]">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                    </svg>
                    {{ mission.status === 'COMPLETED'
                        ? 'Terminée le ' + formatDate(mission.completedAt)
                        : 'Acceptée le ' + formatDate(mission.createdAt) }}
                  </span>
                  <span v-if="mission.proposedBudget"
                        class="flex items-center gap-1 text-xs font-semibold text-brand-600">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"/>
                    </svg>
                    {{ Number(mission.proposedBudget).toLocaleString('fr-MA') }} DH
                  </span>
                  <span v-if="mission.proposedDays && mission.status === 'ACCEPTED'"
                        class="flex items-center gap-1 text-xs"
                        :class="progressPercent(mission) >= 90 ? 'text-red-500' : progressPercent(mission) >= 70 ? 'text-amber-500' : 'text-[#9C9A92]'">
                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                    {{ daysElapsed(mission.createdAt) }} / {{ mission.proposedDays }}j
                  </span>
                </div>
              </div>

              <!-- Badge statut -->
              <span class="flex-shrink-0 text-xs font-semibold px-2.5 py-1 rounded-full"
                    :class="statusBadge(mission.status)">
                {{ statusLabel(mission.status) }}
              </span>
            </div>

            <!-- Barre de progression (En cours uniquement) -->
            <div v-if="mission.status === 'ACCEPTED' && mission.proposedDays" class="mt-3">
              <div class="h-1 bg-[#F4F4ED] rounded-full overflow-hidden">
                <div class="h-full rounded-full transition-all duration-500"
                     :class="progressPercent(mission) >= 90 ? 'bg-red-400'
                           : progressPercent(mission) >= 70 ? 'bg-amber-400'
                           : 'bg-brand-500'"
                     :style="{ width: Math.min(progressPercent(mission), 100) + '%' }">
                </div>
              </div>
            </div>

            <!-- Bandeau validation -->
            <div v-if="mission.status === 'AWAITING_VALIDATION'"
                 class="mt-3 flex items-center gap-2 text-xs text-amber-700
                        bg-amber-50 border border-amber-100 rounded-lg px-3 py-2">
              <svg class="w-3.5 h-3.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              Le client doit valider votre travail. Le paiement sera libéré dès validation.
            </div>

            <!-- Actions -->
            <div class="flex items-center gap-2 mt-3 pt-3 border-t border-[#F0F0EA]">

              <RouterLink v-if="mission.status === 'ACCEPTED'"
                          to="/freelancer/messages"
                          class="flex items-center gap-1.5 text-xs text-[#73726C] hover:text-ink
                                 border border-[#EBEBE5] hover:border-[#C4C3BC] bg-white
                                 px-3 py-1.5 rounded-lg transition">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
                </svg>
                Contacter le client
              </RouterLink>

              <button v-if="mission.status === 'COMPLETED' && !hasReview(mission.id)"
                      @click="openReview(mission)"
                      class="flex items-center gap-1.5 text-xs text-amber-600
                             border border-amber-200 bg-amber-50 hover:bg-amber-100
                             px-3 py-1.5 rounded-lg transition">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"/>
                </svg>
                Laisser un avis
              </button>

              <button v-if="mission.status === 'ACCEPTED'"
                      @click="completeMission(mission)"
                      :disabled="completing === mission.id"
                      class="ml-auto flex items-center gap-1.5 text-xs font-semibold
                             bg-brand-500 hover:bg-brand-600 text-white
                             px-3 py-1.5 rounded-lg transition disabled:opacity-50">
                <svg v-if="completing === mission.id" class="w-3 h-3 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
                </svg>
                <svg v-else class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 13l4 4L19 7"/>
                </svg>
                {{ completing === mission.id ? 'En cours…' : 'Marquer terminé' }}
              </button>

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

    <!-- ── Modal avis ─────────────────────────────────────────────── -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0"
                leave-active-class="transition duration-150" leave-to-class="opacity-0">
      <div v-if="reviewModal.open"
           class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4"
           @click.self="reviewModal.open = false">
        <div class="bg-white rounded-xl shadow-xl w-full max-w-md p-6" @click.stop>
          <h3 class="text-base font-bold text-ink mb-1">Évaluer le client</h3>
          <p class="text-sm text-[#73726C] mb-5">{{ reviewModal.mission?.offerTitle }}</p>

          <!-- Étoiles -->
          <div class="mb-5">
            <p class="text-xs font-semibold text-ink mb-2">Note (obligatoire)</p>
            <div class="flex gap-2">
              <button v-for="star in 5" :key="star"
                      @click="reviewForm.rating = star"
                      class="text-3xl transition-transform hover:scale-110 focus:outline-none">
                <span :class="star <= reviewForm.rating ? 'text-amber-400' : 'text-[#EBEBE5]'">★</span>
              </button>
            </div>
            <p class="text-xs text-[#9C9A92] mt-1">{{ ratingLabel(reviewForm.rating) }}</p>
          </div>

          <!-- Commentaire -->
          <div class="mb-5">
            <label class="text-xs font-semibold text-ink block mb-1.5">Commentaire (optionnel)</label>
            <textarea v-model="reviewForm.comment" rows="3"
                      placeholder="Décrivez votre expérience de collaboration..."
                      class="w-full text-sm text-ink border border-[#EBEBE5] rounded-lg px-3 py-2.5
                             resize-none focus:outline-none focus:border-ink transition
                             placeholder:text-[#9C9A92]"></textarea>
          </div>

          <div class="flex gap-2">
            <button @click="reviewModal.open = false"
                    class="flex-1 py-2.5 text-sm font-medium border border-[#EBEBE5]
                           hover:bg-[#F4F4ED] rounded-lg transition">
              Annuler
            </button>
            <button @click="submitReview"
                    :disabled="!reviewForm.rating || submittingReview"
                    class="flex-1 py-2.5 text-sm font-semibold bg-ink hover:bg-[#1A1A18]
                           text-white rounded-lg transition disabled:opacity-50">
              {{ submittingReview ? 'Envoi…' : "Envoyer l'avis" }}
            </button>
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
const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token') || sessionStorage.getItem('token')}` } })

const loading          = ref(true)
const completing       = ref(null)
const submittingReview = ref(false)
const selectedStatus   = ref('ALL')
const applications     = ref([])
const myReviews        = ref(new Set())
const toast            = ref({ show: false, message: '', type: 'success' })
const reviewModal      = ref({ open: false, mission: null })
const reviewForm       = ref({ rating: 0, comment: '' })

const statusFilters = [
  {
    value: 'ALL',                label: 'Toutes',
    dot: 'bg-[#C4C3BC]',
    activeBg: 'bg-ink',         activeText: 'text-white',       activeBorder: 'border-ink',
    countBg: 'bg-white/20 text-white',
  },
  {
    value: 'ACCEPTED',           label: 'En cours',
    dot: 'bg-brand-500',
    activeBg: 'bg-brand-50',    activeText: 'text-brand-700',   activeBorder: 'border-brand-200',
    countBg: 'bg-brand-100 text-brand-700',
  },
  {
    value: 'AWAITING_VALIDATION',label: 'En validation',
    dot: 'bg-amber-400',
    activeBg: 'bg-amber-50',    activeText: 'text-amber-700',   activeBorder: 'border-amber-200',
    countBg: 'bg-amber-100 text-amber-700',
  },
  {
    value: 'COMPLETED',          label: 'Terminées',
    dot: 'bg-[#C4C3BC]',
    activeBg: 'bg-[#F4F4ED]',   activeText: 'text-[#5F5E5A]',  activeBorder: 'border-[#EBEBE5]',
    countBg: 'bg-[#EBEBE5] text-[#73726C]',
  },
]

const allMissions = computed(() =>
  applications.value.filter(a =>
    ['ACCEPTED', 'AWAITING_VALIDATION', 'COMPLETED'].includes(a.status)
  )
)

const filteredMissions = computed(() =>
  selectedStatus.value === 'ALL'
    ? allMissions.value
    : allMissions.value.filter(a => a.status === selectedStatus.value)
)

const getCountByStatus = (status) =>
  status === 'ALL'
    ? allMissions.value.length
    : allMissions.value.filter(a => a.status === status).length

const statusLabel = (s) => ({
  ACCEPTED:            'En cours',
  AWAITING_VALIDATION: 'En validation',
  COMPLETED:           'Terminée',
}[s] || s)

const statusBadge = (s) => ({
  ACCEPTED:            'bg-brand-50 text-brand-700',
  AWAITING_VALIDATION: 'bg-amber-50 text-amber-700',
  COMPLETED:           'bg-[#F4F4ED] text-[#5F5E5A]',
}[s] || 'bg-[#F4F4ED] text-[#73726C]')

const statusStripe = (s) => ({
  ACCEPTED:            'bg-brand-500',
  AWAITING_VALIDATION: 'bg-amber-400',
  COMPLETED:           'bg-[#C4C3BC]',
}[s] || 'bg-[#DDDDD6]')

const hasReview = (applicationId) => myReviews.value.has(applicationId)

const daysElapsed = (iso) => {
  if (!iso) return 0
  return Math.floor((Date.now() - new Date(iso)) / 86400000)
}

const progressPercent = (mission) => {
  if (!mission.proposedDays) return 0
  return Math.round((daysElapsed(mission.createdAt) / mission.proposedDays) * 100)
}

const formatDate = (iso) => {
  if (!iso) return '—'
  const d = new Date(iso)
  const months = ['jan', 'fév', 'mars', 'avr', 'mai', 'juin', 'juil', 'août', 'sep', 'oct', 'nov', 'déc']
  return `${d.getDate()} ${months[d.getMonth()]} ${d.getFullYear()}`
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 4000)
}

const loadMissions = async () => {
  loading.value = true
  try {
    const [{ data }, { data: reviewed }] = await Promise.all([
      axios.get(`${API_URL}/api/applications/my`, headers()),
      axios.get(`${API_URL}/api/reviews/my`, headers()),
    ])
    applications.value = data || []
    myReviews.value = new Set(reviewed)
  } catch {
    showToast('Impossible de charger les missions', 'error')
  } finally {
    loading.value = false
  }
}

const completeMission = async (mission) => {
  completing.value = mission.id
  try {
    const { data } = await axios.put(`${API_URL}/api/applications/${mission.id}/complete`, {}, headers())
    const idx = applications.value.findIndex(a => a.id === mission.id)
    if (idx !== -1) applications.value[idx] = data
    showToast('Travail soumis ! En attente de validation par le client.')
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la mise à jour', 'error')
  } finally {
    completing.value = null
  }
}

const openReview = (mission) => {
  reviewModal.value = { open: true, mission }
  reviewForm.value = { rating: 0, comment: '' }
}

const submitReview = async () => {
  if (!reviewForm.value.rating) return
  submittingReview.value = true
  try {
    await axios.post(`${API_URL}/api/reviews`, {
      applicationId: reviewModal.value.mission.id,
      rating: reviewForm.value.rating,
      comment: reviewForm.value.comment || null,
    }, headers())
    myReviews.value.add(reviewModal.value.mission.id)
    reviewModal.value.open = false
    showToast('Avis envoyé, merci !')
  } catch (err) {
    showToast(err.response?.data?.message || "Erreur lors de l'envoi", 'error')
  } finally {
    submittingReview.value = false
  }
}

const ratingLabel = (r) =>
  ['', 'Très mauvais', 'Mauvais', 'Correct', 'Bien', 'Excellent'][r] || ''

onMounted(loadMissions)
</script>
