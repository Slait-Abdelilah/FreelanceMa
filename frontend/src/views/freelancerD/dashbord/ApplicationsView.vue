<template>
  <div class="max-w-5xl mx-auto space-y-5">

    <!-- EN-TÊTE -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-xl font-bold text-ink">Mes candidatures</h1>
        <p class="text-[13px] text-[#73726C] mt-0.5">
          {{ applications.length }} candidature{{ applications.length > 1 ? 's' : '' }} au total
        </p>
      </div>
      <router-link to="/freelancer/explore"
                   class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-4 py-2 rounded-lg transition">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
        </svg>
        Explorer les missions
      </router-link>
    </div>

    <!-- FILTRES PAR STATUT -->
    <div class="flex items-center gap-2 flex-wrap">
      <button
          v-for="filter in statusFilters"
          :key="filter.value"
          @click="selectedStatus = filter.value"
          class="flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-[12px] font-semibold transition"
          :class="selectedStatus === filter.value
          ? 'bg-ink text-white'
          : 'bg-white border border-[#EBEBE5] text-[#5F5E5A] hover:border-brand-500 hover:text-brand-600'"
      >
        <span>{{ filter.icon }}</span>
        <span>{{ filter.label }}</span>
        <span class="text-[10px] font-bold px-1.5 py-0.5 rounded-full"
              :class="selectedStatus === filter.value
                ? 'bg-white/20 text-white'
                : 'bg-[#F4F4ED] text-[#73726C]'">
          {{ getCountByStatus(filter.value) }}
        </span>
      </button>
    </div>

    <!-- LOADING -->
    <div v-if="loading" class="flex justify-center py-16">
      <div class="w-6 h-6 border-2 border-brand-500 border-t-transparent rounded-full animate-spin"></div>
    </div>

    <!-- LISTE VIDE -->
    <div v-else-if="filteredApplications.length === 0"
         class="bg-white rounded-xl border border-dashed border-[#EBEBE5] py-16 text-center">
      <div class="w-14 h-14 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-7 h-7 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
        </svg>
      </div>
      <h3 class="text-[15px] font-bold text-ink mb-1">
        {{ selectedStatus === 'ALL' ? 'Aucune candidature' : 'Aucune candidature ' + getStatusLabel(selectedStatus).toLowerCase() }}
      </h3>
      <p class="text-[13px] text-[#73726C] mb-5 max-w-xs mx-auto">
        {{ selectedStatus === 'ALL'
          ? 'Explorez les missions disponibles et postulez pour trouver votre prochaine mission'
          : 'Pas de candidature avec ce statut pour le moment' }}
      </p>
      <router-link to="/freelancer/explore"
                   class="inline-flex items-center gap-2 bg-ink text-white text-[13px] font-semibold px-4 py-2 rounded-lg hover:bg-[#1A1A18] transition">
        Trouver des missions
      </router-link>
    </div>

    <!-- LISTE CANDIDATURES -->
    <div v-else class="space-y-3">
      <div v-for="app in filteredApplications" :key="app.id"
           class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden hover:border-brand-200 hover:shadow-sm transition">

        <div class="p-5">
          <div class="flex items-start gap-4">

            <!-- icône catégorie -->
            <div class="w-10 h-10 rounded-lg flex items-center justify-center flex-shrink-0 text-lg"
                 :class="getCategoryBg(app.offerCategory)">
              {{ getCategoryIcon(app.offerCategory) }}
            </div>

            <!-- infos -->
            <div class="flex-1 min-w-0">

              <!-- titre + statut -->
              <div class="flex items-start justify-between gap-3 mb-1">
                <h3 class="text-[14px] font-bold text-ink leading-snug line-clamp-1">
                  {{ app.offerTitle }}
                </h3>
                <span class="text-[11px] font-bold px-2.5 py-1 rounded-full flex-shrink-0"
                      :class="getStatusClass(app.status)">
                  {{ getStatusIcon(app.status) }} {{ getStatusLabel(app.status) }}
                </span>
              </div>

              <!-- catégorie -->
              <span class="inline-block text-[11px] font-semibold px-2 py-0.5 rounded-md mb-2"
                    :class="getCategoryClass(app.offerCategory)">
                {{ getCategoryLabel(app.offerCategory) }}
              </span>

              <!-- détails de la candidature -->
              <div class="grid grid-cols-2 md:grid-cols-4 gap-3 mt-3">

                <div class="bg-[#FAFAF7] rounded-lg p-2.5">
                  <div class="text-[10px] text-[#9C9A92] font-medium mb-0.5">Mon devis</div>
                  <div class="text-[13px] font-bold text-ink">
                    {{ app.proposedBudget ? formatAmount(app.proposedBudget) + ' DH' : '—' }}
                  </div>
                </div>

                <div class="bg-[#FAFAF7] rounded-lg p-2.5">
                  <div class="text-[10px] text-[#9C9A92] font-medium mb-0.5">Budget client</div>
                  <div class="text-[13px] font-bold text-ink">
                    {{ formatBudget(app.offerBudgetMin, app.offerBudgetMax) }}
                  </div>
                </div>

                <div class="bg-[#FAFAF7] rounded-lg p-2.5">
                  <div class="text-[10px] text-[#9C9A92] font-medium mb-0.5">Délai proposé</div>
                  <div class="text-[13px] font-bold text-ink">
                    {{ app.proposedDays ? app.proposedDays + ' jours' : '—' }}
                  </div>
                </div>

                <div class="bg-[#FAFAF7] rounded-lg p-2.5">
                  <div class="text-[10px] text-[#9C9A92] font-medium mb-0.5">Postulé le</div>
                  <div class="text-[13px] font-bold text-ink">
                    {{ formatDate(app.createdAt) }}
                  </div>
                </div>

              </div>

            </div>
          </div>

          <!-- lettre de motivation (expandable) -->
          <div v-if="app.coverLetter" class="mt-4 pt-4 border-t border-[#EBEBE5]">
            <button @click="toggleCoverLetter(app.id)"
                    class="flex items-center gap-1.5 text-[12px] font-semibold text-[#73726C] hover:text-ink transition">
              <svg class="w-3.5 h-3.5 transition-transform"
                   :class="expandedLetters.includes(app.id) ? 'rotate-180' : ''"
                   fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7"/>
              </svg>
              {{ expandedLetters.includes(app.id) ? 'Masquer' : 'Voir ma lettre de motivation' }}
            </button>

            <div v-if="expandedLetters.includes(app.id)"
                 class="mt-3 p-3 bg-[#FAFAF7] rounded-lg text-[13px] text-[#5F5E5A] leading-relaxed whitespace-pre-line">
              {{ app.coverLetter }}
            </div>
          </div>

          <!-- actions -->
          <div class="flex items-center justify-between mt-4 pt-4 border-t border-[#EBEBE5]">

            <div class="text-[11px] text-[#9C9A92]">
              {{ timeAgo(app.createdAt) }}
            </div>

            <div class="flex items-center gap-2">

              <!-- voir l'offre -->
              <button @click="viewOffer(app.offerId)"
                      class="text-[12px] font-semibold text-ink bg-[#F4F4ED] hover:bg-[#EBEBE5] px-3 py-1.5 rounded-lg transition">
                Voir l'offre
              </button>

              <!-- retirer la candidature (seulement si PENDING) -->
              <button v-if="app.status === 'PENDING'"
                      @click="confirmWithdraw(app)"
                      class="text-[12px] font-semibold text-red-600 border border-red-200 hover:bg-red-50 px-3 py-1.5 rounded-lg transition">
                Retirer
              </button>

            </div>
          </div>

        </div>

      </div>
    </div>

    <!-- TOAST -->
    <div v-if="toast.show"
         class="fixed bottom-6 right-6 z-50 px-4 py-3 rounded-xl shadow-xl text-[13px] font-medium flex items-center gap-2"
         :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
      <svg v-if="toast.type === 'success'" class="w-4 h-4 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
      </svg>
      <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
      </svg>
      {{ toast.message }}
    </div>

    <!-- MODAL CONFIRMATION RETRAIT -->
    <div v-if="showWithdrawModal"
         class="fixed inset-0 bg-ink/50 z-50 flex items-center justify-center p-4"
         @click.self="showWithdrawModal = false">

      <div class="bg-white rounded-2xl w-full max-w-sm p-6 shadow-2xl">

        <div class="w-12 h-12 bg-amber-100 rounded-xl flex items-center justify-center mx-auto mb-4">
          <svg class="w-6 h-6 text-amber-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"/>
          </svg>
        </div>

        <h3 class="text-[16px] font-bold text-ink text-center mb-1">
          Retirer cette candidature ?
        </h3>
        <p class="text-[13px] text-[#73726C] text-center mb-1">
          {{ appToWithdraw?.offerTitle }}
        </p>
        <p class="text-[12px] text-[#9C9A92] text-center mb-5">
          Cette action est irréversible.
        </p>

        <div class="flex gap-3">
          <button @click="showWithdrawModal = false"
                  class="flex-1 py-2.5 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-lg transition">
            Annuler
          </button>
          <button @click="withdrawApplication" :disabled="withdrawing"
                  class="flex-1 py-2.5 bg-red-600 hover:bg-red-700 text-white text-[13px] font-semibold rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
            <svg v-if="withdrawing" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ withdrawing ? 'Retrait...' : 'Confirmer' }}
          </button>
        </div>

      </div>
    </div>

    <!-- MODAL DÉTAIL OFFRE -->
    <div v-if="selectedOffer"
         class="fixed inset-0 bg-ink/50 z-50 flex items-center justify-center p-4"
         @click.self="selectedOffer = null">

      <div class="bg-white rounded-2xl w-full max-w-lg shadow-2xl max-h-[80vh] overflow-y-auto">

        <div class="sticky top-0 bg-white px-6 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
          <h2 class="text-[15px] font-bold text-ink">Détail de l'offre</h2>
          <button @click="selectedOffer = null" class="p-1.5 hover:bg-[#F4F4ED] rounded-md">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div v-if="loadingOffer" class="flex justify-center py-10">
          <div class="w-5 h-5 border-2 border-brand-500 border-t-transparent rounded-full animate-spin"></div>
        </div>

        <div v-else-if="selectedOffer" class="p-6 space-y-4">

          <div>
            <span class="inline-block text-[10px] font-bold px-2 py-0.5 rounded-full mb-2"
                  :class="getCategoryClass(selectedOffer.category)">
              {{ getCategoryLabel(selectedOffer.category) }}
            </span>
            <h3 class="text-[16px] font-bold text-ink mb-2">{{ selectedOffer.title }}</h3>
            <p class="text-[13px] text-[#5F5E5A] leading-relaxed">
              {{ selectedOffer.description }}
            </p>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="bg-[#FAFAF7] rounded-lg p-3">
              <div class="text-[11px] text-[#9C9A92] mb-0.5">Budget</div>
              <div class="text-[14px] font-bold text-ink">
                {{ formatBudget(selectedOffer.budgetMin, selectedOffer.budgetMax) }} DH
              </div>
            </div>
            <div class="bg-[#FAFAF7] rounded-lg p-3">
              <div class="text-[11px] text-[#9C9A92] mb-0.5">Deadline</div>
              <div class="text-[14px] font-bold text-ink">
                {{ selectedOffer.deadline ? formatDate(selectedOffer.deadline) : '—' }}
              </div>
            </div>
          </div>

          <div v-if="selectedOffer.requiredSkills">
            <div class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">
              Compétences requises
            </div>
            <div class="flex flex-wrap gap-2">
              <span v-for="skill in getSkillTags(selectedOffer.requiredSkills)"
                    :key="skill"
                    class="text-[12px] bg-brand-50 text-brand-700 px-2.5 py-1 rounded-md font-medium">
                {{ skill }}
              </span>
            </div>
          </div>

          <div class="text-[12px] text-[#9C9A92]">
            {{ selectedOffer.applicationsCount }} candidat(s) · Publiée {{ timeAgo(selectedOffer.createdAt) }}
          </div>

        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// ===== ÉTAT =====
const loading = ref(true)
const withdrawing = ref(false)
const loadingOffer = ref(false)
const showWithdrawModal = ref(false)
const appToWithdraw = ref(null)
const selectedOffer = ref(null)
const selectedStatus = ref('ALL')
const expandedLetters = ref([])
const toast = ref({ show: false, message: '', type: 'success' })

// ===== DONNÉES =====
// GET http://localhost:8080/api/applications/my → List<ApplicationDTO>
const applications = ref([])

// ===== FILTRES STATUT =====
const statusFilters = [
  { value: 'ALL', label: 'Toutes', icon: '📋' },
  { value: 'PENDING', label: 'En attente', icon: '⏳' },
  { value: 'ACCEPTED', label: 'Acceptées', icon: '✅' },
  { value: 'REJECTED', label: 'Refusées', icon: '❌' },
  { value: 'WITHDRAWN', label: 'Retirées', icon: '↩️' },
]

// ===== COMPUTED =====
const filteredApplications = computed(() => {
  if (selectedStatus.value === 'ALL') return applications.value
  return applications.value.filter(a => a.status === selectedStatus.value)
})

const getCountByStatus = (status) => {
  if (status === 'ALL') return applications.value.length
  return applications.value.filter(a => a.status === status).length
}

// ===== AXIOS =====
const authHeaders = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
})

// ===== CHARGER MES CANDIDATURES → GET /api/applications/my =====
const loadApplications = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(
        `${API_URL}/api/applications/my`,
        authHeaders()
    )
    applications.value = data
  } catch (err) {
    console.error('loadApplications error:', err)
    showToast('Impossible de charger vos candidatures', 'error')
  } finally {
    loading.value = false
  }
}

// ===== VOIR UNE OFFRE → GET /api/offers/{id} =====
const viewOffer = async (offerId) => {
  loadingOffer.value = true
  selectedOffer.value = {}
  try {
    const { data } = await axios.get(
        `${API_URL}/api/offers/${offerId}`
    )
    selectedOffer.value = data
  } catch (err) {
    showToast('Impossible de charger l\'offre', 'error')
    selectedOffer.value = null
  } finally {
    loadingOffer.value = false
  }
}

// ===== CONFIRMER RETRAIT =====
const confirmWithdraw = (app) => {
  appToWithdraw.value = app
  showWithdrawModal.value = true
}

// ===== RETIRER UNE CANDIDATURE → DELETE /api/applications/{id} =====
const withdrawApplication = async () => {
  withdrawing.value = true
  try {
    await axios.delete(
        `${API_URL}/api/applications/${appToWithdraw.value.id}`,
        authHeaders()
    )

    // mettre à jour localement
    const app = applications.value.find(a => a.id === appToWithdraw.value.id)
    if (app) app.status = 'WITHDRAWN'

    showWithdrawModal.value = false
    showToast('Candidature retirée')

  } catch (err) {
    showToast(
        err.response?.data?.message || 'Erreur lors du retrait',
        'error'
    )
  } finally {
    withdrawing.value = false
  }
}

// ===== TOGGLE LETTRE =====
const toggleCoverLetter = (id) => {
  const index = expandedLetters.value.indexOf(id)
  if (index === -1) {
    expandedLetters.value.push(id)
  } else {
    expandedLetters.value.splice(index, 1)
  }
}

// ===== HELPERS =====

const formatAmount = (amount) => {
  if (!amount) return '0'
  return parseFloat(amount).toLocaleString('fr-MA')
}

const formatBudget = (min, max) => {
  if (!min && !max) return 'À négocier'
  if (min && max) return `${formatAmount(min)} - ${formatAmount(max)}`
  if (min) return `Dès ${formatAmount(min)}`
  return `Jusqu'à ${formatAmount(max)}`
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  })
}

const timeAgo = (date) => {
  if (!date) return ''
  const diff = Date.now() - new Date(date).getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  if (minutes < 60) return `il y a ${minutes}min`
  if (hours < 24) return `il y a ${hours}h`
  if (days < 7) return `il y a ${days}j`
  return formatDate(date)
}

const getSkillTags = (skills) => {
  if (!skills) return []
  return skills.split(',').map(s => s.trim()).filter(s => s.length > 0)
}

const getStatusLabel = (status) => {
  const labels = {
    PENDING: 'En attente',
    ACCEPTED: 'Acceptée',
    REJECTED: 'Refusée',
    WITHDRAWN: 'Retirée',
  }
  return labels[status] || status
}

const getStatusIcon = (status) => {
  const icons = {
    PENDING: '⏳',
    ACCEPTED: '✅',
    REJECTED: '❌',
    WITHDRAWN: '↩️',
  }
  return icons[status] || '•'
}

const getStatusClass = (status) => {
  const classes = {
    PENDING: 'bg-amber-50 text-amber-700',
    ACCEPTED: 'bg-brand-50 text-brand-700',
    REJECTED: 'bg-red-50 text-red-700',
    WITHDRAWN: 'bg-gray-100 text-gray-500',
  }
  return classes[status] || 'bg-gray-100 text-gray-500'
}

const getCategoryLabel = (category) => {
  const labels = {
    WEB_DEVELOPMENT: 'Développement Web',
    MOBILE_DEVELOPMENT: 'Mobile',
    DESIGN: 'Design',
    MARKETING: 'Marketing',
    WRITING: 'Rédaction',
    VIDEO: 'Vidéo',
    TRANSLATION: 'Traduction',
    DATA_SCIENCE: 'Data Science',
    OTHER: 'Autre',
  }
  return labels[category] || category || 'Autre'
}

const getCategoryClass = (category) => {
  const classes = {
    WEB_DEVELOPMENT: 'bg-blue-50 text-blue-700',
    MOBILE_DEVELOPMENT: 'bg-purple-50 text-purple-700',
    DESIGN: 'bg-pink-50 text-pink-700',
    MARKETING: 'bg-orange-50 text-orange-700',
    WRITING: 'bg-amber-50 text-amber-700',
    VIDEO: 'bg-red-50 text-red-700',
    TRANSLATION: 'bg-cyan-50 text-cyan-700',
    DATA_SCIENCE: 'bg-green-50 text-green-700',
    OTHER: 'bg-gray-100 text-gray-600',
  }
  return classes[category] || 'bg-gray-100 text-gray-600'
}

const getCategoryBg = (category) => {
  const bgs = {
    WEB_DEVELOPMENT: 'bg-blue-50',
    MOBILE_DEVELOPMENT: 'bg-purple-50',
    DESIGN: 'bg-pink-50',
    MARKETING: 'bg-orange-50',
    WRITING: 'bg-amber-50',
    VIDEO: 'bg-red-50',
    TRANSLATION: 'bg-cyan-50',
    DATA_SCIENCE: 'bg-green-50',
    OTHER: 'bg-gray-100',
  }
  return bgs[category] || 'bg-gray-100'
}

const getCategoryIcon = (category) => {
  const icons = {
    WEB_DEVELOPMENT: '🌐',
    MOBILE_DEVELOPMENT: '📱',
    DESIGN: '🎨',
    MARKETING: '📈',
    WRITING: '✍️',
    VIDEO: '🎬',
    TRANSLATION: '🌍',
    DATA_SCIENCE: '📊',
    OTHER: '💼',
  }
  return icons[category] || '💼'
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4000)
}

onMounted(() => {
  loadApplications()
})
</script>