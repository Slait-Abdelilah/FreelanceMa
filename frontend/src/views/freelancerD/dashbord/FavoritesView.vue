<template>
  <div class="space-y-6">

    <!-- HEADER -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-[20px] font-semibold text-ink tracking-tight">Favoris</h1>
        <p class="text-[13px] text-[#9C9A92] mt-0.5">
          {{ loading ? '…' : `${favorites.length} offre${favorites.length !== 1 ? 's' : ''} sauvegardée${favorites.length !== 1 ? 's' : ''}` }}
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

    <!-- TABS catégories (dynamiques) -->
    <div v-if="!loading && favorites.length > 0" class="flex gap-1 bg-[#F4F4ED] p-1 rounded-lg w-fit flex-wrap">
      <button @click="selectedCategory = 'ALL'"
              class="text-[13px] font-medium px-3.5 py-1.5 rounded-md transition"
              :class="selectedCategory === 'ALL' ? 'bg-white text-ink shadow-sm' : 'text-[#73726C] hover:text-ink'">
        Toutes
        <span class="ml-1 text-[11px] text-[#9C9A92]">({{ favorites.length }})</span>
      </button>
      <button v-for="cat in availableCategories" :key="cat.value"
              @click="selectedCategory = cat.value"
              class="text-[13px] font-medium px-3.5 py-1.5 rounded-md transition"
              :class="selectedCategory === cat.value ? 'bg-white text-ink shadow-sm' : 'text-[#73726C] hover:text-ink'">
        {{ cat.label }}
        <span class="ml-1 text-[11px] text-[#9C9A92]">({{ favorites.filter(f => f.category === cat.value).length }})</span>
      </button>
    </div>

    <!-- SKELETON -->
    <div v-if="loading" class="space-y-3">
      <div v-for="i in 4" :key="i" class="bg-white border border-[#EBEBE5] rounded-xl p-6 animate-pulse">
        <div class="flex items-start justify-between gap-4">
          <div class="space-y-2.5 flex-1">
            <div class="flex gap-1.5">
              <div class="h-5 w-16 bg-[#F4F4ED] rounded-full"></div>
              <div class="h-5 w-14 bg-[#F4F4ED] rounded-full"></div>
            </div>
            <div class="h-4 bg-[#EBEBE5] rounded w-3/5"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-full"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-4/5"></div>
          </div>
          <div class="text-right space-y-1.5">
            <div class="h-5 w-24 bg-[#EBEBE5] rounded ml-auto"></div>
            <div class="h-3 w-14 bg-[#F4F4ED] rounded ml-auto"></div>
          </div>
        </div>
        <div class="flex gap-2 mt-5 pt-4 border-t border-[#F4F4ED]">
          <div class="h-8 w-28 bg-[#F4F4ED] rounded-lg ml-auto"></div>
          <div class="h-8 w-28 bg-[#F4F4ED] rounded-lg"></div>
        </div>
      </div>
    </div>

    <!-- EMPTY -->
    <div v-else-if="filteredFavorites.length === 0"
         class="bg-white border border-[#EBEBE5] rounded-xl p-14 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">
        {{ selectedCategory === 'ALL' ? 'Aucun favori' : 'Aucune offre dans cette catégorie' }}
      </p>
      <p class="text-[13px] text-[#9C9A92] mt-1">
        {{ selectedCategory === 'ALL' ? 'Sauvegardez des offres depuis Explorer pour les retrouver ici.' : 'Essayez une autre catégorie.' }}
      </p>
      <RouterLink v-if="selectedCategory === 'ALL'" to="/freelancer/explore"
                  class="inline-block mt-5 text-[13px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- LISTE -->
    <div v-else class="space-y-3">
      <div v-for="fav in filteredFavorites" :key="fav.favoriteId"
           class="bg-white border border-[#EBEBE5] rounded-xl p-6 hover:border-[#D3D1C7] transition">

        <!-- Titre + budget -->
        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 min-w-0">
            <div class="flex items-center gap-2 mb-2">
              <span v-if="fav.category" class="text-[11px] font-semibold px-2.5 py-0.5 rounded-full"
                    :class="getCategoryClass(fav.category)">
                {{ getCategoryLabel(fav.category) }}
              </span>
              <span v-if="fav.budgetType" class="text-[11px] text-[#9C9A92] border border-[#EBEBE5] px-2.5 py-0.5 rounded-full">
                {{ fav.budgetType === 'FIXED' ? 'Prix fixe' : 'Horaire' }}
              </span>
            </div>
            <p class="text-[17px] font-semibold text-ink leading-snug">{{ fav.title }}</p>
            <p class="text-[13px] text-[#73726C] mt-1.5 line-clamp-2 leading-relaxed">{{ fav.description }}</p>
          </div>

          <div class="text-right flex-shrink-0">
            <p class="text-[15px] font-bold text-ink tabular-nums">{{ formatBudget(fav.budgetMin, fav.budgetMax) }}</p>
            <p class="text-[12px] text-[#9C9A92] mt-0.5">budget client</p>
          </div>
        </div>

        <!-- Compétences -->
        <div v-if="fav.requiredSkills" class="flex flex-wrap gap-1.5 mt-3.5">
          <span v-for="skill in getSkillTags(fav.requiredSkills).slice(0, 5)" :key="skill"
                class="text-[12px] border border-[#EBEBE5] text-[#5F5E5A] px-2.5 py-1 rounded-md">
            {{ skill }}
          </span>
          <span v-if="getSkillTags(fav.requiredSkills).length > 5" class="text-[12px] text-[#9C9A92] self-center">
            +{{ getSkillTags(fav.requiredSkills).length - 5 }}
          </span>
        </div>

        <!-- Footer -->
        <div class="flex items-center gap-3 mt-4 pt-4 border-t border-[#EBEBE5]">
          <div class="flex items-center gap-4 text-[12px] text-[#9C9A92] flex-1 min-w-0">
            <span class="flex items-center gap-1.5">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/>
              </svg>
              {{ fav.applicationsCount ?? 0 }} candidat{{ (fav.applicationsCount ?? 0) > 1 ? 's' : '' }}
            </span>
            <span v-if="fav.deadline" class="flex items-center gap-1.5">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
              </svg>
              {{ formatDate(fav.deadline) }}
            </span>
            <span class="flex items-center gap-1.5">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"/>
              </svg>
              Sauvegardé {{ timeAgo(fav.savedAt) }}
            </span>
          </div>

          <div class="flex items-center gap-2">
            <button @click="remove(fav)" :disabled="removing === fav.favoriteId"
                    class="p-2 text-[#C4C3BC] hover:text-red-500 hover:bg-red-50 rounded-lg transition disabled:opacity-40">
              <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                <path d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z"/>
              </svg>
            </button>
            <button @click="openApplyModal(fav)"
                    class="flex items-center gap-2 text-[13px] font-semibold bg-ink hover:bg-[#1A1A18] text-white px-4 py-2 rounded-lg transition">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"/>
              </svg>
              Postuler
            </button>
          </div>
        </div>

      </div>
    </div>

    <!-- MODAL POSTULER -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="applyTarget" class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4"
           @click.self="applyTarget = null">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl w-full max-w-lg" @click.stop>

          <div class="flex items-start justify-between px-6 py-4 border-b border-[#EBEBE5]">
            <div>
              <p class="text-[15px] font-semibold text-ink">Postuler</p>
              <p class="text-[13px] text-[#9C9A92] mt-0.5 truncate max-w-xs">{{ applyTarget.title }}</p>
            </div>
            <button @click="applyTarget = null" class="p-1.5 hover:bg-[#F4F4ED] rounded-md transition">
              <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
          </div>

          <div class="p-6 space-y-4">
            <div v-if="applyError" class="flex items-center gap-2 bg-red-50 border border-red-100 text-red-600 rounded-xl p-3.5 text-[13px]">
              <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              {{ applyError }}
            </div>

            <div>
              <label class="block text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">Lettre de motivation</label>
              <textarea v-model="applyForm.coverLetter" rows="4"
                        placeholder="Présentez-vous et expliquez pourquoi vous êtes le bon candidat..."
                        class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[13px] text-ink outline-none transition resize-none placeholder:text-[#C4C3BC]">
              </textarea>
            </div>

            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="block text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">Votre devis (DH)</label>
                <input v-model="applyForm.proposedBudget" type="number"
                       :placeholder="applyTarget.budgetMin ? String(applyTarget.budgetMin) : 'Ex : 2 000'"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[13px] text-ink outline-none transition"/>
              </div>
              <div>
                <label class="block text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">Délai (jours)</label>
                <input v-model="applyForm.proposedDays" type="number" min="1" placeholder="Ex : 14"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[13px] text-ink outline-none transition"/>
              </div>
            </div>
          </div>

          <div class="flex gap-3 px-6 pb-6">
            <button @click="applyTarget = null"
                    class="flex-1 py-2.5 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] rounded-lg transition">
              Annuler
            </button>
            <button @click="submitApplication" :disabled="applying"
                    class="flex-1 py-2.5 text-[13px] font-semibold bg-ink hover:bg-[#1A1A18] text-white rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
              <svg v-if="applying" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ applying ? 'Envoi…' : 'Envoyer ma candidature' }}
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
const removing         = ref(null)
const applying         = ref(false)
const selectedCategory = ref('ALL')
const favorites        = ref([])
const applyTarget      = ref(null)
const applyError       = ref('')
const toast            = ref({ show: false, message: '', type: 'success' })
const applyForm        = ref({ coverLetter: '', proposedBudget: null, proposedDays: null })

const CATEGORY_META = {
  WEB_DEVELOPMENT:    { label: 'Dev Web' },
  MOBILE_DEVELOPMENT: { label: 'Mobile' },
  DESIGN:             { label: 'Design' },
  MARKETING:          { label: 'Marketing' },
  WRITING:            { label: 'Rédaction' },
  VIDEO:              { label: 'Vidéo' },
  TRANSLATION:        { label: 'Traduction' },
  DATA_SCIENCE:       { label: 'Data Science' },
  OTHER:              { label: 'Autre' },
}

const availableCategories = computed(() => {
  const seen = [...new Set(favorites.value.map(f => f.category).filter(Boolean))]
  return seen.map(cat => ({ value: cat, label: CATEGORY_META[cat]?.label ?? cat }))
})

const filteredFavorites = computed(() =>
  selectedCategory.value === 'ALL'
    ? favorites.value
    : favorites.value.filter(f => f.category === selectedCategory.value)
)

const getCategoryLabel = (cat) => CATEGORY_META[cat]?.label ?? cat

const getCategoryClass = (cat) => ({
  WEB_DEVELOPMENT:    'bg-blue-50 text-blue-700',
  MOBILE_DEVELOPMENT: 'bg-purple-50 text-purple-700',
  DESIGN:             'bg-pink-50 text-pink-700',
  MARKETING:          'bg-orange-50 text-orange-700',
  WRITING:            'bg-green-50 text-green-700',
  VIDEO:              'bg-red-50 text-red-700',
  TRANSLATION:        'bg-yellow-50 text-yellow-700',
  DATA_SCIENCE:       'bg-cyan-50 text-cyan-700',
  OTHER:              'bg-[#F4F4ED] text-[#5F5E5A]',
}[cat] || 'bg-[#F4F4ED] text-[#5F5E5A]')

const formatBudget = (min, max) => {
  if (min && max) return `${Number(min).toLocaleString('fr-MA')} – ${Number(max).toLocaleString('fr-MA')} DH`
  if (min)        return `${Number(min).toLocaleString('fr-MA')} DH`
  if (max)        return `${Number(max).toLocaleString('fr-MA')} DH`
  return '—'
}

const formatDate = (d) => {
  if (!d) return '—'
  return new Date(d).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' })
}

const timeAgo = (iso) => {
  if (!iso) return ''
  const diff = Date.now() - new Date(iso)
  const m = Math.floor(diff / 60000)
  const h = Math.floor(diff / 3600000)
  const d = Math.floor(diff / 86400000)
  if (m < 1)  return "à l'instant"
  if (m < 60) return `il y a ${m}min`
  if (h < 24) return `il y a ${h}h`
  return `il y a ${d}j`
}

const getSkillTags = (s) => s ? s.split(',').map(t => t.trim()).filter(Boolean) : []

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 3500)
}

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
  applyError.value  = ''
  applyForm.value   = { coverLetter: '', proposedBudget: null, proposedDays: null }
}

const submitApplication = async () => {
  applyError.value = ''
  if (!applyForm.value.coverLetter.trim())                                       { applyError.value = 'Écrivez une lettre de motivation'; return }
  if (!applyForm.value.proposedBudget || applyForm.value.proposedBudget <= 0)    { applyError.value = 'Entrez votre devis'; return }
  if (!applyForm.value.proposedDays   || applyForm.value.proposedDays   <= 0)    { applyError.value = 'Entrez votre délai estimé'; return }

  applying.value = true
  try {
    await axios.post(`${API_URL}/api/applications`, {
      offerId:        applyTarget.value.offerId,
      coverLetter:    applyForm.value.coverLetter,
      proposedBudget: parseFloat(applyForm.value.proposedBudget),
      proposedDays:   parseInt(applyForm.value.proposedDays),
    }, headers())
    showToast('Candidature envoyée !')
    applyTarget.value = null
  } catch (err) {
    applyError.value = err.response?.data?.message || "Erreur lors de l'envoi"
  } finally {
    applying.value = false
  }
}

onMounted(loadFavorites)
</script>
