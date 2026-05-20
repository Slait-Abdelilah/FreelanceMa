<template>
  <div class="space-y-6">

    <!-- HEADER -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-[20px] font-semibold text-ink tracking-tight">Missions actives</h1>
        <p class="text-[13px] text-[#9C9A92] mt-0.5">
          {{ loading ? '…' : `${allMissions.length} mission${allMissions.length !== 1 ? 's' : ''}` }}
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
      <button v-for="tab in tabs" :key="tab.value" @click="selectedStatus = tab.value"
              class="text-[13px] font-medium px-3.5 py-1.5 rounded-md transition"
              :class="selectedStatus === tab.value ? 'bg-white text-ink shadow-sm' : 'text-[#73726C] hover:text-ink'">
        {{ tab.label }}
        <span class="ml-1 text-[11px] text-[#9C9A92]">({{ getCountByStatus(tab.value) }})</span>
      </button>
    </div>

    <!-- SKELETON -->
    <div v-if="loading" class="space-y-3">
      <div v-for="i in 3" :key="i" class="bg-white border border-[#EBEBE5] rounded-xl p-6 animate-pulse">
        <div class="flex items-start justify-between gap-4">
          <div class="space-y-2.5 flex-1">
            <div class="h-4 bg-[#F4F4ED] rounded w-2/5"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-1/3"></div>
          </div>
          <div class="h-6 bg-[#F4F4ED] rounded-full w-24"></div>
        </div>
        <div class="h-1.5 bg-[#F4F4ED] rounded-full w-full mt-5"></div>
        <div class="flex gap-3 mt-5 pt-4 border-t border-[#F4F4ED]">
          <div class="h-8 w-36 bg-[#F4F4ED] rounded-lg"></div>
          <div class="h-8 w-36 bg-[#F4F4ED] rounded-lg ml-auto"></div>
        </div>
      </div>
    </div>

    <!-- EMPTY -->
    <div v-else-if="filteredMissions.length === 0"
         class="bg-white border border-[#EBEBE5] rounded-xl p-14 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">Aucune mission</p>
      <p class="text-[13px] text-[#9C9A92] mt-1">
        {{ selectedStatus === 'ALL' ? 'Postulez à des offres pour démarrer vos premières missions.' : 'Aucune mission avec ce statut.' }}
      </p>
      <RouterLink v-if="selectedStatus === 'ALL'" to="/freelancer/explore"
                  class="inline-block mt-5 text-[13px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- LISTE -->
    <div v-else class="space-y-3">
      <div v-for="mission in filteredMissions" :key="mission.id"
           class="bg-white border border-[#EBEBE5] rounded-xl p-6 hover:border-[#D3D1C7] transition">

        <!-- Titre + badge -->
        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 min-w-0">
            <p class="text-[17px] font-semibold text-ink leading-snug">
              {{ mission.offerTitle || 'Mission #' + mission.offerId }}
            </p>
            <div class="flex flex-wrap items-center gap-x-5 gap-y-1.5 mt-2.5">
              <span class="flex items-center gap-1.5 text-[13px] text-[#9C9A92]">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                </svg>
                {{ mission.status === 'COMPLETED'
                    ? 'Terminée le ' + formatDate(mission.completedAt)
                    : 'Acceptée le ' + formatDate(mission.createdAt) }}
              </span>
              <span v-if="mission.proposedBudget" class="flex items-center gap-1.5 text-[13px] font-semibold text-ink">
                <svg class="w-4 h-4 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33"/>
                </svg>
                {{ Number(mission.proposedBudget).toLocaleString('fr-MA') }} DH
              </span>
              <span v-if="mission.proposedDays && mission.status === 'ACCEPTED'"
                    class="flex items-center gap-1.5 text-[13px]"
                    :class="progressPercent(mission) >= 90 ? 'text-red-500 font-medium' : progressPercent(mission) >= 70 ? 'text-amber-500' : 'text-[#9C9A92]'">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
                {{ daysElapsed(mission.createdAt) }} / {{ mission.proposedDays }} jours
              </span>
            </div>
          </div>

          <!-- Badge statut -->
          <span class="flex-shrink-0 text-[13px] font-semibold px-3.5 py-1.5 rounded-full border"
                :class="statusBadge(mission.status)">
            {{ statusLabel(mission.status) }}
          </span>
        </div>

        <!-- Barre de progression -->
        <div v-if="mission.status === 'ACCEPTED' && mission.proposedDays" class="mt-4">
          <div class="flex items-center justify-between mb-1.5">
            <span class="text-[12px] text-[#9C9A92]">Progression</span>
            <span class="text-[12px] font-medium"
                  :class="progressPercent(mission) >= 90 ? 'text-red-500' : progressPercent(mission) >= 70 ? 'text-amber-500' : 'text-[#73726C]'">
              {{ Math.min(progressPercent(mission), 100) }}%
            </span>
          </div>
          <div class="h-1.5 bg-[#F4F4ED] rounded-full overflow-hidden">
            <div class="h-full rounded-full transition-all duration-500"
                 :class="progressPercent(mission) >= 90 ? 'bg-red-400'
                       : progressPercent(mission) >= 70 ? 'bg-amber-400'
                       : 'bg-ink'"
                 :style="{ width: Math.min(progressPercent(mission), 100) + '%' }">
            </div>
          </div>
        </div>

        <!-- Bandeau validation -->
        <div v-if="mission.status === 'AWAITING_VALIDATION'"
             class="mt-4 flex items-center gap-2.5 text-[13px] text-amber-700 bg-amber-50 border border-amber-100 rounded-xl px-4 py-3">
          <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
          Le client doit valider votre travail. Le paiement sera libéré dès validation.
        </div>

        <!-- Actions -->
        <div class="flex items-center gap-2 mt-5 pt-4 border-t border-[#EBEBE5]">
          <RouterLink v-if="mission.status === 'ACCEPTED'" to="/freelancer/messages"
                      class="flex items-center gap-2 text-[13px] font-medium text-[#5F5E5A] hover:text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
            Contacter le client
          </RouterLink>

          <button v-if="mission.status === 'COMPLETED' && !hasReview(mission.id)"
                  @click="openReview(mission)"
                  class="flex items-center gap-2 text-[13px] font-medium text-amber-600 border border-amber-200 bg-amber-50 hover:bg-amber-100 px-4 py-2 rounded-lg transition">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"/>
            </svg>
            Laisser un avis
          </button>

          <button v-if="mission.status === 'ACCEPTED'" @click="completeMission(mission)"
                  :disabled="completing === mission.id"
                  class="ml-auto flex items-center gap-2 text-[13px] font-semibold bg-ink hover:bg-[#1A1A18] text-white px-4 py-2 rounded-lg transition disabled:opacity-50">
            <svg v-if="completing === mission.id" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
            </svg>
            {{ completing === mission.id ? 'En cours…' : 'Marquer terminé' }}
          </button>
        </div>
      </div>
    </div>

    <!-- MODAL AVIS -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="reviewModal.open" class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4"
           @click.self="reviewModal.open = false">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl w-full max-w-md p-6" @click.stop>
          <p class="text-[15px] font-semibold text-ink">Évaluer le client</p>
          <p class="text-[13px] text-[#73726C] mt-1 mb-5">{{ reviewModal.mission?.offerTitle }}</p>

          <!-- Étoiles -->
          <div class="mb-5">
            <p class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-3">Note (obligatoire)</p>
            <div class="flex gap-2">
              <button v-for="star in 5" :key="star" @click="reviewForm.rating = star"
                      class="text-4xl transition-transform hover:scale-110 focus:outline-none">
                <span :class="star <= reviewForm.rating ? 'text-amber-400' : 'text-[#EBEBE5]'">★</span>
              </button>
            </div>
            <p class="text-[13px] text-[#9C9A92] mt-2">{{ ratingLabel(reviewForm.rating) }}</p>
          </div>

          <!-- Commentaire -->
          <div class="mb-5">
            <label class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider block mb-2">Commentaire (optionnel)</label>
            <textarea v-model="reviewForm.comment" rows="3"
                      placeholder="Décrivez votre expérience de collaboration..."
                      class="w-full text-[13px] text-ink border border-[#EBEBE5] rounded-xl px-4 py-3 resize-none focus:outline-none focus:border-[#5F5E5A] transition placeholder:text-[#C4C3BC]"></textarea>
          </div>

          <div class="flex gap-3">
            <button @click="reviewModal.open = false"
                    class="flex-1 py-2.5 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] rounded-lg transition">
              Annuler
            </button>
            <button @click="submitReview" :disabled="!reviewForm.rating || submittingReview"
                    class="flex-1 py-2.5 text-[13px] font-semibold bg-ink hover:bg-[#1A1A18] text-white rounded-lg transition disabled:opacity-50">
              {{ submittingReview ? 'Envoi…' : "Envoyer l'avis" }}
            </button>
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

const tabs = [
  { value: 'ALL',                label: 'Toutes' },
  { value: 'ACCEPTED',           label: 'En cours' },
  { value: 'AWAITING_VALIDATION',label: 'En validation' },
  { value: 'COMPLETED',          label: 'Terminées' },
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
  ACCEPTED:            'bg-green-50 text-green-600 border-green-100',
  AWAITING_VALIDATION: 'bg-amber-50 text-amber-700 border-amber-100',
  COMPLETED:           'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]',
}[s] || 'bg-[#F4F4ED] text-[#73726C] border-[#EBEBE5]')

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
  return new Date(iso).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' })
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 3500)
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
