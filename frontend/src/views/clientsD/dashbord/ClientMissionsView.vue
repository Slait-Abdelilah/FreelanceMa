<template>
  <div class="space-y-5">

    <!-- HEADER -->
    <div>
      <h1 class="text-[20px] font-semibold text-ink tracking-tight">Missions</h1>
      <p class="text-[13px] text-[#9C9A92] mt-0.5">Suivez vos missions en cours et terminées</p>
    </div>

    <!-- TABS -->
    <div class="flex gap-1 bg-[#F4F4ED] p-1 rounded-lg w-fit">
      <button v-for="tab in tabs" :key="tab.value" @click="activeTab = tab.value"
              class="text-[12px] font-medium px-3 py-1 rounded-md transition relative"
              :class="activeTab === tab.value ? 'bg-white text-ink shadow-sm' : 'text-[#73726C] hover:text-ink'">
        {{ tab.label }}
        <span v-if="tab.count > 0" class="ml-1 text-[10px]"
              :class="tab.value === 'awaiting' ? 'text-amber-500 font-bold' : 'text-[#9C9A92]'">
          ({{ tab.count }})
        </span>
        <!-- Dot indicator for awaiting tab -->
        <span v-if="tab.value === 'awaiting' && tab.count > 0 && activeTab !== 'awaiting'"
              class="absolute -top-0.5 -right-0.5 w-2 h-2 bg-amber-400 rounded-full"></span>
      </button>
    </div>

    <!-- SKELETON -->
    <div v-if="loading" class="space-y-3">
      <div v-for="i in 3" :key="i" class="bg-white border border-[#EBEBE5] rounded-xl p-5 animate-pulse">
        <div class="flex items-start gap-4">
          <div class="w-10 h-10 bg-[#F4F4ED] rounded-full flex-shrink-0"></div>
          <div class="flex-1 space-y-2">
            <div class="h-4 bg-[#F4F4ED] rounded w-2/5"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-3/4"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- MISSIONS LIST -->
    <div v-else-if="filteredMissions.length > 0" class="space-y-3">
      <div v-for="m in filteredMissions" :key="m.id"
           class="bg-white border rounded-xl p-5 transition"
           :class="m.status === 'AWAITING_VALIDATION'
             ? 'border-amber-200 hover:border-amber-300'
             : 'border-[#EBEBE5] hover:border-[#D3D1C7]'">

        <div class="flex items-start gap-4">
          <!-- avatar freelancer -->
          <div class="w-10 h-10 bg-[#F0EFE6] border border-[#EBEBE5] rounded-full flex items-center justify-center text-[12px] font-semibold text-ink flex-shrink-0">
            #{{ String(m.freelancerId).slice(-3) }}
          </div>

          <div class="flex-1 min-w-0">
            <div class="flex items-start justify-between gap-3">
              <div class="min-w-0">
                <p class="text-[14px] font-semibold text-ink truncate">{{ m.offerTitle }}</p>
                <p class="text-[12px] text-[#73726C] mt-0.5">Freelancer #{{ m.freelancerId }}</p>
              </div>
              <span class="text-[11px] font-semibold px-2.5 py-1 rounded-full border flex-shrink-0 flex items-center gap-1"
                    :class="statusClass(m.status)">
                {{ statusLabel(m.status) }}
              </span>
            </div>

            <!-- meta -->
            <div class="flex flex-wrap items-center gap-x-4 gap-y-1 mt-3 text-[11px] text-[#9C9A92]">
              <span v-if="m.proposedBudget" class="flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33"/>
                </svg>
                Budget : <strong class="text-ink ml-0.5">{{ Number(m.proposedBudget).toLocaleString('fr-MA') }} DH</strong>
              </span>
              <span v-if="m.proposedDays" class="flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
                {{ m.proposedDays }} jour{{ m.proposedDays > 1 ? 's' : '' }}
              </span>
              <span>Candidature reçue {{ timeAgo(m.createdAt) }}</span>
              <span v-if="m.status === 'COMPLETED' && m.completedAt">
                · Terminée le {{ formatDate(m.completedAt) }}
              </span>
              <span v-if="m.status === 'AWAITING_VALIDATION' && m.completedAt">
                · Livrée le {{ formatDate(m.completedAt) }}
              </span>
            </div>

            <!-- AWAITING_VALIDATION action -->
            <div v-if="m.status === 'AWAITING_VALIDATION'"
                 class="mt-4 p-3 bg-amber-50 border border-amber-200 rounded-lg">
              <p class="text-[12px] text-amber-800 font-medium mb-2">
                Le freelancer a soumis son travail. Vérifiez et validez pour libérer le paiement.
              </p>
              <div class="flex gap-2">
                <button @click="validateMission(m)"
                        :disabled="validating === m.id"
                        class="flex items-center gap-1.5 text-[12px] font-semibold bg-ink text-white px-4 py-2 rounded-lg hover:bg-[#1A1A18] disabled:opacity-50 transition">
                  <svg v-if="validating === m.id" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
                  </svg>
                  <svg v-else class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                  </svg>
                  {{ validating === m.id ? 'Validation...' : 'Valider la mission' }}
                </button>
              </div>
            </div>

            <!-- COMPLETED: show review button -->
            <div v-if="m.status === 'COMPLETED'" class="mt-3 pt-3 border-t border-[#EBEBE5] flex items-center justify-between">
              <RouterLink :to="`/client/applications?offer=${m.offerId}`"
                          class="text-[12px] font-medium text-[#5F5E5A] hover:text-ink transition">
                Voir les candidatures →
              </RouterLink>
              <button v-if="!hasReview(m.id)"
                      @click="openReview(m)"
                      class="text-[11px] font-semibold text-amber-600 border border-amber-200 bg-amber-50 hover:bg-amber-100 px-2.5 py-1 rounded-full transition">
                Laisser un avis
              </button>
              <span v-else class="text-[11px] text-green-600 flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                </svg>
                Avis envoyé
              </span>
            </div>

            <!-- ACCEPTED: link to applications -->
            <div v-if="m.status === 'ACCEPTED'" class="mt-3 pt-3 border-t border-[#EBEBE5]">
              <RouterLink :to="`/client/applications?offer=${m.offerId}`"
                          class="text-[12px] font-medium text-[#5F5E5A] hover:text-ink transition">
                Voir les candidatures →
              </RouterLink>
            </div>

            <!-- category badge -->
            <div v-if="m.offerCategory && m.status !== 'AWAITING_VALIDATION'" class="mt-2.5">
              <span class="text-[10px] font-medium text-[#73726C] bg-[#F4F4ED] border border-[#EBEBE5] px-2 py-0.5 rounded-full">
                {{ categoryLabel(m.offerCategory) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- EMPTY -->
    <div v-else-if="!loading" class="bg-white border border-[#EBEBE5] rounded-xl p-12 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">
        {{ emptyMessage }}
      </p>
      <p class="text-[12px] text-[#9C9A92] mt-1">{{ emptySubMessage }}</p>
      <RouterLink v-if="activeTab === 'active'" to="/client/applications"
                  class="mt-4 inline-block text-[13px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
        Voir les candidatures
      </RouterLink>
    </div>

    <!-- TOAST -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast.show"
           class="fixed bottom-6 right-6 z-50 px-4 py-3 rounded-lg border text-[13px] font-medium flex items-center gap-2"
           :class="toast.type === 'success' ? 'bg-ink text-white border-transparent' : 'bg-white text-red-600 border-red-200'">
        <svg v-if="toast.type === 'success'" class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 13l4 4L19 7"/>
        </svg>
        {{ toast.message }}
      </div>
    </Transition>

    <!-- REVIEW MODAL -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0"
                leave-active-class="transition duration-150" leave-to-class="opacity-0">
      <div v-if="reviewModal.open" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm p-4"
           @click.self="reviewModal.open = false">
        <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 scale-95"
                    leave-active-class="transition duration-150" leave-to-class="opacity-0 scale-95">
          <div v-if="reviewModal.open" class="bg-white rounded-2xl shadow-2xl w-full max-w-md p-6">
            <h3 class="text-[16px] font-bold text-ink mb-1">Évaluer le freelancer</h3>
            <p class="text-[13px] text-[#73726C] mb-5">{{ reviewModal.mission?.offerTitle }}</p>

            <div class="mb-5">
              <p class="text-[12px] font-medium text-ink mb-2">Note (obligatoire)</p>
              <div class="flex gap-2">
                <button v-for="star in 5" :key="star"
                        @click="reviewForm.rating = star"
                        class="text-3xl transition-transform hover:scale-110 focus:outline-none">
                  <span :class="star <= reviewForm.rating ? 'text-amber-400' : 'text-[#EBEBE5]'">★</span>
                </button>
              </div>
              <p class="text-[11px] text-[#9C9A92] mt-1">{{ ratingLabel(reviewForm.rating) }}</p>
            </div>

            <div class="mb-5">
              <label class="text-[12px] font-medium text-ink block mb-1.5">Commentaire (optionnel)</label>
              <textarea v-model="reviewForm.comment" rows="3"
                        placeholder="Décrivez la qualité du travail fourni..."
                        class="w-full text-[13px] text-ink border border-[#EBEBE5] rounded-lg px-3 py-2.5 resize-none focus:outline-none focus:border-ink transition placeholder:text-[#9C9A92]"></textarea>
            </div>

            <div class="flex gap-3">
              <button @click="reviewModal.open = false"
                      class="flex-1 text-[13px] font-medium text-[#73726C] border border-[#EBEBE5] rounded-lg py-2.5 hover:bg-[#F4F4ED] transition">
                Annuler
              </button>
              <button @click="submitReview"
                      :disabled="!reviewForm.rating || submittingReview"
                      class="flex-1 text-[13px] font-semibold bg-ink text-white rounded-lg py-2.5 hover:bg-[#1A1A18] disabled:opacity-50 transition">
                {{ submittingReview ? 'Envoi...' : 'Envoyer l\'avis' }}
              </button>
            </div>
          </div>
        </Transition>
      </div>
    </Transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const loading   = ref(true)
const missions  = ref([])
const activeTab = ref('active')
const validating = ref(null)
const submittingReview = ref(false)
const myReviews = ref(new Set())
const toast = ref({ show: false, message: '', type: 'success' })
const reviewModal = ref({ open: false, mission: null })
const reviewForm = ref({ rating: 0, comment: '' })

const tabs = computed(() => [
  { value: 'active',    label: 'En cours',   count: missions.value.filter(m => m.status === 'ACCEPTED').length },
  { value: 'awaiting',  label: 'À valider',  count: missions.value.filter(m => m.status === 'AWAITING_VALIDATION').length },
  { value: 'completed', label: 'Terminées',  count: missions.value.filter(m => m.status === 'COMPLETED').length },
])

const filteredMissions = computed(() => {
  if (activeTab.value === 'active')    return missions.value.filter(m => m.status === 'ACCEPTED')
  if (activeTab.value === 'awaiting')  return missions.value.filter(m => m.status === 'AWAITING_VALIDATION')
  if (activeTab.value === 'completed') return missions.value.filter(m => m.status === 'COMPLETED')
  return missions.value
})

const emptyMessage = computed(() => {
  if (activeTab.value === 'active')    return 'Aucune mission en cours'
  if (activeTab.value === 'awaiting')  return 'Aucune mission en attente de validation'
  return 'Aucune mission terminée'
})

const emptySubMessage = computed(() => {
  if (activeTab.value === 'active')    return 'Acceptez des candidatures pour démarrer des missions'
  if (activeTab.value === 'awaiting')  return 'Les missions soumises par les freelancers apparaîtront ici'
  return 'Les missions validées apparaîtront ici'
})

const token   = () => localStorage.getItem('token')
const headers = () => ({ Authorization: `Bearer ${token()}` })

const load = async () => {
  loading.value = true
  try {
    const { data: offers } = await axios.get(`${API_URL}/api/offers/my`, { headers: headers() })
    const results = await Promise.allSettled(
      offers.map(o =>
        axios.get(`${API_URL}/api/offers/${o.id}/applications`, { headers: headers() })
          .then(r => r.data.map(app => ({
            ...app,
            offerTitle:    o.title,
            offerCategory: o.category,
            offerId:       o.id,
          })))
      )
    )
    const all = []
    results.forEach(r => { if (r.status === 'fulfilled') all.push(...r.value) })
    missions.value = all
      .filter(a => ['ACCEPTED', 'AWAITING_VALIDATION', 'COMPLETED'].includes(a.status))
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))

    // Auto-switch to awaiting tab if there are missions to validate
    if (missions.value.some(m => m.status === 'AWAITING_VALIDATION')) {
      activeTab.value = 'awaiting'
    }
  } catch { /* silencieux */ } finally { loading.value = false }
}

const validateMission = async (mission) => {
  validating.value = mission.id
  try {
    const { data } = await axios.put(`${API_URL}/api/applications/${mission.id}/validate`, {}, { headers: headers() })
    const idx = missions.value.findIndex(m => m.id === mission.id)
    if (idx !== -1) missions.value[idx] = { ...missions.value[idx], ...data }
    showToast('Mission validée ! Le paiement a été libéré au freelancer.')
    activeTab.value = 'completed'
    openReview(mission)
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la validation', 'error')
  } finally {
    validating.value = null
  }
}

const hasReview = (applicationId) => myReviews.value.has(applicationId)

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
      comment: reviewForm.value.comment || null
    }, { headers: headers() })
    myReviews.value.add(reviewModal.value.mission.id)
    reviewModal.value.open = false
    showToast('Avis envoyé !')
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de l\'envoi', 'error')
  } finally {
    submittingReview.value = false
  }
}

const ratingLabel = (r) => {
  const labels = { 0: '', 1: 'Très mauvais', 2: 'Mauvais', 3: 'Correct', 4: 'Bien', 5: 'Excellent' }
  return labels[r] || ''
}

const statusLabel = (status) => {
  const map = { ACCEPTED: 'En cours', AWAITING_VALIDATION: 'À valider', COMPLETED: 'Terminée' }
  return map[status] || status
}

const statusClass = (status) => {
  if (status === 'ACCEPTED')            return 'bg-green-50 text-green-600 border-green-100'
  if (status === 'AWAITING_VALIDATION') return 'bg-amber-50 text-amber-600 border-amber-200'
  return 'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]'
}

const categories = [
  { value: 'WEB_DEVELOPMENT',    label: 'Développement web' },
  { value: 'MOBILE_DEVELOPMENT', label: 'Mobile' },
  { value: 'DESIGN',             label: 'Design' },
  { value: 'MARKETING',          label: 'Marketing' },
  { value: 'WRITING',            label: 'Rédaction' },
  { value: 'VIDEO',              label: 'Vidéo' },
  { value: 'TRANSLATION',        label: 'Traduction' },
  { value: 'DATA_SCIENCE',       label: 'Data Science' },
  { value: 'OTHER',              label: 'Autre' },
]
const categoryLabel = (v) => categories.find(c => c.value === v)?.label || v

const timeAgo = (iso) => {
  const diff = Date.now() - new Date(iso).getTime()
  const d = Math.floor(diff / 86400000)
  if (d === 0) return "aujourd'hui"
  if (d === 1) return "hier"
  return `il y a ${d}j`
}
const formatDate = (iso) => iso ? new Date(iso).toLocaleDateString('fr-FR') : ''

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4500)
}

onMounted(load)
</script>
