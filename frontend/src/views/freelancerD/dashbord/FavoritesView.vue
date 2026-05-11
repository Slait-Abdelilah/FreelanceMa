<template>
  <div class="max-w-4xl mx-auto space-y-5">

    <!-- EN-TÊTE -->
    <div class="flex items-start justify-between gap-4">
      <div>
        <h1 class="text-xl font-bold text-ink">Favoris</h1>
        <p class="text-[13px] text-[#73726C] mt-0.5">
          {{ favorites.length }} offre{{ favorites.length !== 1 ? 's' : '' }} sauvegardée{{ favorites.length !== 1 ? 's' : '' }}
        </p>
      </div>
      <RouterLink to="/freelancer/explore"
                  class="flex items-center gap-1.5 bg-ink hover:bg-[#1A1A18] text-white text-[12px] font-semibold px-3 py-2 rounded-lg transition">
        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
        </svg>
        Explorer
      </RouterLink>
    </div>

    <!-- LOADING SKELETON -->
    <div v-if="loading" class="space-y-3">
      <div v-for="i in 4" :key="i" class="bg-white rounded-xl border border-[#EBEBE5] p-5 animate-pulse">
        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 space-y-2">
            <div class="h-3 w-20 bg-[#F4F4ED] rounded-full"></div>
            <div class="h-4 w-2/3 bg-[#EBEBE5] rounded"></div>
            <div class="h-3 w-full bg-[#F4F4ED] rounded"></div>
          </div>
          <div class="h-8 w-24 bg-[#F4F4ED] rounded-lg"></div>
        </div>
        <div class="flex gap-2 mt-4">
          <div class="h-5 w-16 bg-[#F4F4ED] rounded-full"></div>
          <div class="h-5 w-20 bg-[#F4F4ED] rounded-full"></div>
        </div>
      </div>
    </div>

    <!-- ÉTAT VIDE -->
    <div v-else-if="favorites.length === 0"
         class="bg-white rounded-xl border border-dashed border-[#EBEBE5] py-20 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-6 h-6 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z"/>
        </svg>
      </div>
      <h3 class="text-[14px] font-semibold text-ink mb-1">Aucun favori</h3>
      <p class="text-[13px] text-[#73726C] mb-6 max-w-xs mx-auto">
        Sauvegardez des offres depuis Explorer pour les retrouver ici.
      </p>
      <RouterLink to="/freelancer/explore"
                  class="inline-flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-5 py-2.5 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- LISTE -->
    <div v-else class="space-y-3">
      <div v-for="fav in favorites" :key="fav.favoriteId"
           class="bg-white rounded-xl border border-[#EBEBE5] p-5">

        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 min-w-0">
            <!-- catégorie -->
            <span v-if="fav.category"
                  class="inline-block text-[10px] font-semibold px-2 py-0.5 rounded-full mb-2"
                  :class="getCategoryClass(fav.category)">
              {{ getCategoryLabel(fav.category) }}
            </span>
            <!-- titre -->
            <h2 class="text-[14px] font-semibold text-ink leading-snug">{{ fav.title }}</h2>
            <!-- description -->
            <p class="text-[13px] text-[#73726C] mt-1 line-clamp-2">{{ fav.description }}</p>
          </div>

          <!-- budget -->
          <div class="text-right flex-shrink-0">
            <div class="text-[14px] font-bold text-ink tabular-nums">{{ formatBudget(fav.budgetMin, fav.budgetMax) }}</div>
            <div class="text-[11px] text-[#9C9A92]">{{ fav.budgetType === 'FIXED' ? 'Prix fixe' : 'Horaire' }}</div>
          </div>
        </div>

        <!-- compétences -->
        <div v-if="fav.requiredSkills" class="flex flex-wrap gap-1.5 mt-3">
          <span v-for="skill in getSkillTags(fav.requiredSkills).slice(0, 4)" :key="skill"
                class="text-[11px] border border-[#EBEBE5] text-[#5F5E5A] px-2 py-0.5 rounded">
            {{ skill }}
          </span>
          <span v-if="getSkillTags(fav.requiredSkills).length > 4" class="text-[11px] text-[#9C9A92]">
            +{{ getSkillTags(fav.requiredSkills).length - 4 }}
          </span>
        </div>

        <!-- footer -->
        <div class="flex items-center justify-between mt-4 pt-4 border-t border-[#EBEBE5]">
          <div class="flex items-center gap-4 text-[11px] text-[#9C9A92]">
            <span>{{ fav.applicationsCount ?? 0 }} candidats</span>
            <span v-if="fav.deadline">Deadline : {{ formatDate(fav.deadline) }}</span>
            <span>Sauvegardé {{ timeAgo(fav.savedAt) }}</span>
          </div>

          <div class="flex items-center gap-2">
            <!-- retirer -->
            <button @click="remove(fav)"
                    :disabled="removing === fav.favoriteId"
                    class="p-1.5 hover:bg-[#F4F4ED] rounded-md transition text-[#9C9A92] hover:text-red-500">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z" class="fill-current"/>
              </svg>
            </button>
            <!-- postuler -->
            <button @click="openApplyModal(fav)"
                    class="flex items-center gap-1.5 text-[12px] font-semibold px-3 py-1.5 rounded-lg bg-ink hover:bg-[#1A1A18] text-white transition">
              Postuler
            </button>
          </div>
        </div>

      </div>
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
        <svg v-else class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
        </svg>
        {{ toast.message }}
      </div>
    </Transition>

    <!-- MODAL POSTULER -->
    <div v-if="applyTarget"
         class="fixed inset-0 bg-ink/50 z-50 flex items-center justify-center p-4"
         @click.self="applyTarget = null">
      <div class="bg-white rounded-xl w-full max-w-lg border border-[#EBEBE5]">

        <div class="flex items-start justify-between px-6 py-4 border-b border-[#EBEBE5]">
          <div>
            <h2 class="text-[15px] font-semibold text-ink">Postuler</h2>
            <p class="text-xs text-[#9C9A92] mt-0.5 truncate max-w-xs">{{ applyTarget.title }}</p>
          </div>
          <button @click="applyTarget = null" class="p-1.5 hover:bg-[#F4F4ED] rounded-md transition">
            <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div class="p-6 space-y-4">
          <div v-if="applyError" class="bg-red-50 border border-red-200 text-red-700 rounded-lg p-3 text-[12px]">
            {{ applyError }}
          </div>

          <div>
            <label class="block text-xs text-[#9C9A92] mb-1.5">Lettre de motivation</label>
            <textarea v-model="applyForm.coverLetter" rows="4"
                      placeholder="Présentez-vous et expliquez pourquoi vous êtes le bon candidat..."
                      class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition resize-none"></textarea>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-xs text-[#9C9A92] mb-1.5">Votre devis (DH)</label>
              <input v-model="applyForm.proposedBudget" type="number"
                     :placeholder="applyTarget.budgetMin || ''"
                     class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition"/>
            </div>
            <div>
              <label class="block text-xs text-[#9C9A92] mb-1.5">Délai (jours)</label>
              <input v-model="applyForm.proposedDays" type="number" min="1" placeholder="Ex: 14"
                     class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition"/>
            </div>
          </div>
        </div>

        <div class="flex gap-3 px-6 pb-6">
          <button @click="applyTarget = null"
                  class="flex-1 py-2.5 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-lg transition">
            Annuler
          </button>
          <button @click="submitApplication" :disabled="applying"
                  class="flex-1 py-2.5 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
            <svg v-if="applying" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ applying ? 'Envoi...' : 'Envoyer ma candidature' }}
          </button>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

const loading = ref(true)
const removing = ref(null)
const applying = ref(false)
const favorites = ref([])
const applyTarget = ref(null)
const applyError = ref('')
const toast = ref({ show: false, message: '', type: 'success' })
const applyForm = ref({ coverLetter: '', proposedBudget: null, proposedDays: null })

const loadFavorites = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/favorites`, headers())
    favorites.value = data || []
  } catch {
    showToast('Impossible de charger les favoris', 'error')
  } finally {
    loading.value = false
  }
}

const remove = async (fav) => {
  removing.value = fav.favoriteId
  try {
    await axios.post(`${API_URL}/api/favorites/${fav.offerId}/toggle`, {}, headers())
    favorites.value = favorites.value.filter(f => f.favoriteId !== fav.favoriteId)
    showToast('Retiré des favoris')
  } catch {
    showToast('Erreur lors de la suppression', 'error')
  } finally {
    removing.value = null
  }
}

const openApplyModal = (fav) => {
  applyTarget.value = fav
  applyError.value = ''
  applyForm.value = { coverLetter: '', proposedBudget: null, proposedDays: null }
}

const submitApplication = async () => {
  applyError.value = ''
  if (!applyForm.value.coverLetter.trim()) { applyError.value = 'Écrivez une lettre de motivation'; return }
  if (!applyForm.value.proposedBudget || applyForm.value.proposedBudget <= 0) { applyError.value = 'Entrez votre devis'; return }
  if (!applyForm.value.proposedDays || applyForm.value.proposedDays <= 0) { applyError.value = 'Entrez votre délai estimé'; return }

  applying.value = true
  try {
    await axios.post(`${API_URL}/api/applications`, {
      offerId: applyTarget.value.offerId,
      coverLetter: applyForm.value.coverLetter,
      proposedBudget: parseFloat(applyForm.value.proposedBudget),
      proposedDays: parseInt(applyForm.value.proposedDays),
    }, headers())
    showToast('Candidature envoyée !')
    applyTarget.value = null
  } catch (err) {
    applyError.value = err.response?.data?.message || 'Erreur lors de l\'envoi'
  } finally {
    applying.value = false
  }
}

// ── helpers ──
const formatBudget = (min, max) => {
  if (min && max) return `${Number(min).toLocaleString('fr-MA')}–${Number(max).toLocaleString('fr-MA')} DH`
  if (min) return `${Number(min).toLocaleString('fr-MA')} DH`
  if (max) return `${Number(max).toLocaleString('fr-MA')} DH`
  return '—'
}

const formatDate = (d) => {
  if (!d) return '—'
  const date = new Date(d)
  const months = ['jan', 'fév', 'mars', 'avr', 'mai', 'juin', 'juil', 'août', 'sep', 'oct', 'nov', 'déc']
  return `${date.getDate()} ${months[date.getMonth()]} ${date.getFullYear()}`
}

const timeAgo = (iso) => {
  if (!iso) return ''
  const diff = Date.now() - new Date(iso)
  const m = Math.floor(diff / 60000)
  const h = Math.floor(diff / 3600000)
  const d = Math.floor(diff / 86400000)
  if (m < 1) return 'à l\'instant'
  if (m < 60) return `il y a ${m} min`
  if (h < 24) return `il y a ${h}h`
  return `il y a ${d} j`
}

const getSkillTags = (s) => s ? s.split(',').map(t => t.trim()).filter(t => t) : []

const getCategoryLabel = (cat) => ({
  WEB_DEVELOPMENT: 'Dev Web', MOBILE_DEVELOPMENT: 'Mobile', DESIGN: 'Design',
  MARKETING: 'Marketing', WRITING: 'Rédaction', VIDEO: 'Vidéo',
  TRANSLATION: 'Traduction', DATA_SCIENCE: 'Data', OTHER: 'Autre'
}[cat] || cat)

const getCategoryClass = (cat) => ({
  WEB_DEVELOPMENT: 'bg-blue-50 text-blue-700',
  MOBILE_DEVELOPMENT: 'bg-purple-50 text-purple-700',
  DESIGN: 'bg-pink-50 text-pink-700',
  MARKETING: 'bg-orange-50 text-orange-700',
  WRITING: 'bg-green-50 text-green-700',
  VIDEO: 'bg-red-50 text-red-700',
  TRANSLATION: 'bg-yellow-50 text-yellow-700',
  DATA_SCIENCE: 'bg-cyan-50 text-cyan-700',
  OTHER: 'bg-[#F4F4ED] text-[#5F5E5A]',
}[cat] || 'bg-[#F4F4ED] text-[#5F5E5A]')

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 3500)
}

onMounted(() => { loadFavorites() })
</script>
