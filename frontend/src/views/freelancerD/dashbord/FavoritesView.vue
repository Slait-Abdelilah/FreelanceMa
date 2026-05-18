<template>
  <div class="pb-8">

    <!-- ── En-tête ──────────────────────────────────────────────── -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-xl font-bold text-ink">Favoris</h1>
        <p class="text-sm text-ink-soft mt-0.5">
          {{ loading ? '…' : `${favorites.length} offre${favorites.length > 1 ? 's' : ''} sauvegardée${favorites.length > 1 ? 's' : ''}` }}
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

    <!-- ── Chips catégories (dynamiques) ────────────────────────── -->
    <div v-if="!loading && favorites.length > 0" class="flex flex-wrap gap-2 mb-6">
      <button @click="selectedCategory = 'ALL'"
              class="flex items-center gap-2 px-3.5 py-2 rounded-lg text-sm font-medium transition border"
              :class="selectedCategory === 'ALL'
                ? 'bg-ink text-white border-ink'
                : 'bg-white border-[#EBEBE5] text-[#73726C] hover:text-ink hover:border-[#C4C3BC]'">
        <span class="w-1.5 h-1.5 rounded-full bg-[#C4C3BC] flex-shrink-0"></span>
        Toutes
        <span class="tabular-nums text-xs rounded-full px-1.5 py-0.5"
              :class="selectedCategory === 'ALL' ? 'bg-white/20 text-white' : 'bg-[#F4F4ED] text-[#9C9A92]'">
          {{ favorites.length }}
        </span>
      </button>
      <button v-for="cat in availableCategories" :key="cat.value"
              @click="selectedCategory = cat.value"
              class="flex items-center gap-2 px-3.5 py-2 rounded-lg text-sm font-medium transition border"
              :class="selectedCategory === cat.value
                ? `${cat.activeBg} ${cat.activeText} ${cat.activeBorder}`
                : 'bg-white border-[#EBEBE5] text-[#73726C] hover:text-ink hover:border-[#C4C3BC]'">
        <span class="w-1.5 h-1.5 rounded-full flex-shrink-0" :class="cat.dot"></span>
        {{ cat.label }}
        <span class="tabular-nums text-xs rounded-full px-1.5 py-0.5"
              :class="selectedCategory === cat.value ? cat.countBg : 'bg-[#F4F4ED] text-[#9C9A92]'">
          {{ favorites.filter(f => f.category === cat.value).length }}
        </span>
      </button>
    </div>

    <!-- ── Chargement ────────────────────────────────────────────── -->
    <div v-if="loading" class="space-y-2">
      <div v-for="i in 4" :key="i"
           class="bg-white border border-[#EBEBE5] rounded-xl flex overflow-hidden animate-pulse">
        <div class="w-1 bg-[#E5E5E0] flex-shrink-0"></div>
        <div class="flex-1 px-5 py-4 space-y-2">
          <div class="flex gap-1.5">
            <div class="h-4 w-16 bg-[#F4F4ED] rounded-full"></div>
            <div class="h-4 w-14 bg-[#F4F4ED] rounded-full"></div>
          </div>
          <div class="h-3.5 bg-[#EBEBE5] rounded w-2/3"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-full"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-4/5"></div>
          <div class="flex gap-1.5 pt-1">
            <div class="h-5 w-14 bg-[#F4F4ED] rounded"></div>
            <div class="h-5 w-20 bg-[#F4F4ED] rounded"></div>
            <div class="h-5 w-12 bg-[#F4F4ED] rounded"></div>
          </div>
        </div>
        <div class="px-5 py-4 flex flex-col items-end gap-2">
          <div class="h-4 w-24 bg-[#EBEBE5] rounded"></div>
          <div class="h-3 w-14 bg-[#F4F4ED] rounded"></div>
        </div>
      </div>
    </div>

    <!-- ── État vide ─────────────────────────────────────────────── -->
    <div v-else-if="filteredFavorites.length === 0"
         class="bg-white border border-[#EBEBE5] rounded-xl py-16 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z"/>
        </svg>
      </div>
      <p class="text-sm font-semibold text-ink mb-1">
        {{ selectedCategory === 'ALL' ? 'Aucun favori' : 'Aucune offre dans cette catégorie' }}
      </p>
      <p class="text-xs text-[#9C9A92] mb-5">
        {{ selectedCategory === 'ALL'
          ? 'Sauvegardez des offres depuis Explorer pour les retrouver ici.'
          : 'Essayez une autre catégorie.' }}
      </p>
      <RouterLink v-if="selectedCategory === 'ALL'" to="/freelancer/explore"
                  class="inline-flex items-center gap-1.5 text-xs font-medium text-brand-600
                         border border-brand-200 hover:bg-brand-50 px-4 py-2 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- ── Liste ─────────────────────────────────────────────────── -->
    <div v-else class="space-y-2">
      <div v-for="fav in filteredFavorites" :key="fav.favoriteId"
           class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden
                  hover:shadow-sm hover:border-[#D1D1CB] transition">
        <div class="flex">

          <!-- Bande catégorie gauche -->
          <div class="w-1 flex-shrink-0" :class="categoryStripe(fav.category)"></div>

          <!-- Contenu -->
          <div class="flex-1 min-w-0 px-5 py-4">

            <!-- Titre + budget -->
            <div class="flex items-start gap-4">
              <div class="flex-1 min-w-0">
                <div class="flex items-center gap-2 mb-1.5">
                  <span v-if="fav.category"
                        class="text-[10px] font-semibold px-2 py-0.5 rounded-full"
                        :class="getCategoryClass(fav.category)">
                    {{ getCategoryLabel(fav.category) }}
                  </span>
                  <span v-if="fav.budgetType"
                        class="text-[10px] text-[#9C9A92] border border-[#EBEBE5] px-2 py-0.5 rounded-full">
                    {{ fav.budgetType === 'FIXED' ? 'Prix fixe' : 'Horaire' }}
                  </span>
                </div>
                <p class="text-sm font-semibold text-ink leading-snug">{{ fav.title }}</p>
                <p class="text-xs text-[#73726C] mt-1 line-clamp-2 leading-relaxed">{{ fav.description }}</p>
              </div>

              <div class="text-right flex-shrink-0">
                <p class="text-sm font-bold text-ink tabular-nums">{{ formatBudget(fav.budgetMin, fav.budgetMax) }}</p>
                <p class="text-[10px] text-[#9C9A92] mt-0.5">budget client</p>
              </div>
            </div>

            <!-- Compétences -->
            <div v-if="fav.requiredSkills" class="flex flex-wrap gap-1.5 mt-3">
              <span v-for="skill in getSkillTags(fav.requiredSkills).slice(0, 5)" :key="skill"
                    class="text-[11px] border border-[#EBEBE5] text-[#5F5E5A] px-2 py-0.5 rounded">
                {{ skill }}
              </span>
              <span v-if="getSkillTags(fav.requiredSkills).length > 5"
                    class="text-[11px] text-[#9C9A92]">
                +{{ getSkillTags(fav.requiredSkills).length - 5 }}
              </span>
            </div>

            <!-- Footer -->
            <div class="flex items-center gap-2 mt-3 pt-3 border-t border-[#F0F0EA]">
              <div class="flex items-center gap-3 text-xs text-[#9C9A92] flex-1 min-w-0">
                <span class="flex items-center gap-1">
                  <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/>
                  </svg>
                  {{ fav.applicationsCount ?? 0 }} candidat{{ (fav.applicationsCount ?? 0) > 1 ? 's' : '' }}
                </span>
                <span v-if="fav.deadline" class="flex items-center gap-1">
                  <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                  </svg>
                  {{ formatDate(fav.deadline) }}
                </span>
                <span class="flex items-center gap-1">
                  <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z"/>
                  </svg>
                  {{ timeAgo(fav.savedAt) }}
                </span>
              </div>

              <div class="flex items-center gap-1.5">
                <button @click="remove(fav)"
                        :disabled="removing === fav.favoriteId"
                        class="p-1.5 text-red-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition
                               disabled:opacity-40">
                  <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z"/>
                  </svg>
                </button>
                <button @click="openApplyModal(fav)"
                        class="flex items-center gap-1.5 text-xs font-semibold
                               bg-ink hover:bg-[#1A1A18] text-white
                               px-3 py-1.5 rounded-lg transition">
                  <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"/>
                  </svg>
                  Postuler
                </button>
              </div>
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

    <!-- ── Modal postuler ─────────────────────────────────────────── -->
    <Transition enter-from-class="opacity-0" enter-active-class="transition duration-150"
                leave-to-class="opacity-0" leave-active-class="transition duration-150">
      <div v-if="applyTarget"
           class="fixed inset-0 bg-black/40 z-50 flex items-center justify-center p-4"
           @click.self="applyTarget = null">
        <div class="bg-white rounded-xl w-full max-w-lg shadow-xl" @click.stop>

          <div class="flex items-start justify-between px-5 py-4 border-b border-[#EBEBE5]">
            <div>
              <p class="text-sm font-bold text-ink">Postuler</p>
              <p class="text-xs text-[#9C9A92] mt-0.5 truncate max-w-xs">{{ applyTarget.title }}</p>
            </div>
            <button @click="applyTarget = null"
                    class="p-1.5 text-[#9C9A92] hover:text-ink hover:bg-[#F4F4ED] rounded-md transition">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
          </div>

          <div class="p-5 space-y-4">
            <div v-if="applyError"
                 class="flex items-center gap-2 bg-red-50 border border-red-200 text-red-700
                        rounded-lg p-3 text-xs">
              <svg class="w-3.5 h-3.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              {{ applyError }}
            </div>

            <div>
              <label class="block text-xs font-semibold text-ink mb-1.5">Lettre de motivation</label>
              <textarea v-model="applyForm.coverLetter" rows="4"
                        placeholder="Présentez-vous et expliquez pourquoi vous êtes le bon candidat..."
                        class="w-full border border-[#EBEBE5] focus:border-ink rounded-lg px-3 py-2.5
                               text-sm outline-none transition resize-none placeholder:text-[#9C9A92]">
              </textarea>
            </div>

            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="block text-xs font-semibold text-ink mb-1.5">Votre devis (DH)</label>
                <input v-model="applyForm.proposedBudget" type="number"
                       :placeholder="applyTarget.budgetMin ? String(applyTarget.budgetMin) : 'Ex : 2 000'"
                       class="w-full border border-[#EBEBE5] focus:border-ink rounded-lg px-3 py-2.5
                              text-sm outline-none transition"/>
              </div>
              <div>
                <label class="block text-xs font-semibold text-ink mb-1.5">Délai (jours)</label>
                <input v-model="applyForm.proposedDays" type="number" min="1" placeholder="Ex : 14"
                       class="w-full border border-[#EBEBE5] focus:border-ink rounded-lg px-3 py-2.5
                              text-sm outline-none transition"/>
              </div>
            </div>
          </div>

          <div class="flex gap-2 px-5 pb-5">
            <button @click="applyTarget = null"
                    class="flex-1 py-2.5 text-sm font-medium border border-[#EBEBE5]
                           hover:bg-[#F4F4ED] rounded-lg transition">
              Annuler
            </button>
            <button @click="submitApplication" :disabled="applying"
                    class="flex-1 py-2.5 text-sm font-semibold bg-ink hover:bg-[#1A1A18]
                           text-white rounded-lg transition disabled:opacity-50
                           flex items-center justify-center gap-2">
              <svg v-if="applying" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ applying ? 'Envoi…' : 'Envoyer ma candidature' }}
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

const loading         = ref(true)
const removing        = ref(null)
const applying        = ref(false)
const selectedCategory = ref('ALL')
const favorites       = ref([])
const applyTarget     = ref(null)
const applyError      = ref('')
const toast           = ref({ show: false, message: '', type: 'success' })
const applyForm       = ref({ coverLetter: '', proposedBudget: null, proposedDays: null })

// ── Catégories présentes dans les favoris (dynamique) ─────────────
const CATEGORY_META = {
  WEB_DEVELOPMENT:    { label: 'Dev Web',     dot: 'bg-blue-400',   activeBg: 'bg-blue-50',   activeText: 'text-blue-700',   activeBorder: 'border-blue-200',   countBg: 'bg-blue-100 text-blue-700' },
  MOBILE_DEVELOPMENT: { label: 'Mobile',      dot: 'bg-purple-400', activeBg: 'bg-purple-50', activeText: 'text-purple-700', activeBorder: 'border-purple-200', countBg: 'bg-purple-100 text-purple-700' },
  DESIGN:             { label: 'Design',      dot: 'bg-pink-400',   activeBg: 'bg-pink-50',   activeText: 'text-pink-700',   activeBorder: 'border-pink-200',   countBg: 'bg-pink-100 text-pink-700' },
  MARKETING:          { label: 'Marketing',   dot: 'bg-orange-400', activeBg: 'bg-orange-50', activeText: 'text-orange-700', activeBorder: 'border-orange-200', countBg: 'bg-orange-100 text-orange-700' },
  WRITING:            { label: 'Rédaction',   dot: 'bg-green-500',  activeBg: 'bg-green-50',  activeText: 'text-green-700',  activeBorder: 'border-green-200',  countBg: 'bg-green-100 text-green-700' },
  VIDEO:              { label: 'Vidéo',       dot: 'bg-red-400',    activeBg: 'bg-red-50',    activeText: 'text-red-700',    activeBorder: 'border-red-200',    countBg: 'bg-red-100 text-red-700' },
  TRANSLATION:        { label: 'Traduction',  dot: 'bg-yellow-400', activeBg: 'bg-yellow-50', activeText: 'text-yellow-700', activeBorder: 'border-yellow-200', countBg: 'bg-yellow-100 text-yellow-700' },
  DATA_SCIENCE:       { label: 'Data',        dot: 'bg-cyan-400',   activeBg: 'bg-cyan-50',   activeText: 'text-cyan-700',   activeBorder: 'border-cyan-200',   countBg: 'bg-cyan-100 text-cyan-700' },
  OTHER:              { label: 'Autre',       dot: 'bg-[#C4C3BC]',  activeBg: 'bg-[#F4F4ED]', activeText: 'text-[#5F5E5A]',  activeBorder: 'border-[#EBEBE5]',  countBg: 'bg-[#EBEBE5] text-[#73726C]' },
}

const availableCategories = computed(() => {
  const seen = [...new Set(favorites.value.map(f => f.category).filter(Boolean))]
  return seen.map(cat => ({ value: cat, ...(CATEGORY_META[cat] || CATEGORY_META.OTHER) }))
})

const filteredFavorites = computed(() =>
  selectedCategory.value === 'ALL'
    ? favorites.value
    : favorites.value.filter(f => f.category === selectedCategory.value)
)

// ── Helpers visuels ───────────────────────────────────────────────
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

const categoryStripe = (cat) => ({
  WEB_DEVELOPMENT:    'bg-blue-400',
  MOBILE_DEVELOPMENT: 'bg-purple-400',
  DESIGN:             'bg-pink-400',
  MARKETING:          'bg-orange-400',
  WRITING:            'bg-green-500',
  VIDEO:              'bg-red-400',
  TRANSLATION:        'bg-yellow-400',
  DATA_SCIENCE:       'bg-cyan-400',
  OTHER:              'bg-[#C4C3BC]',
}[cat] || 'bg-[#DDDDD6]')

// ── Formatage ─────────────────────────────────────────────────────
const formatBudget = (min, max) => {
  if (min && max) return `${Number(min).toLocaleString('fr-MA')}–${Number(max).toLocaleString('fr-MA')} DH`
  if (min)        return `${Number(min).toLocaleString('fr-MA')} DH`
  if (max)        return `${Number(max).toLocaleString('fr-MA')} DH`
  return '—'
}

const formatDate = (d) => {
  if (!d) return '—'
  const date   = new Date(d)
  const months = ['jan', 'fév', 'mars', 'avr', 'mai', 'juin', 'juil', 'août', 'sep', 'oct', 'nov', 'déc']
  return `${date.getDate()} ${months[date.getMonth()]} ${date.getFullYear()}`
}

const timeAgo = (iso) => {
  if (!iso) return ''
  const diff = Date.now() - new Date(iso)
  const m = Math.floor(diff / 60000)
  const h = Math.floor(diff / 3600000)
  const d = Math.floor(diff / 86400000)
  if (m < 1)  return "à l'instant"
  if (m < 60) return `il y a ${m} min`
  if (h < 24) return `il y a ${h}h`
  return `il y a ${d} j`
}

const getSkillTags = (s) => s ? s.split(',').map(t => t.trim()).filter(Boolean) : []

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 3500)
}

// ── API ───────────────────────────────────────────────────────────
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
  if (!applyForm.value.coverLetter.trim())                          { applyError.value = 'Écrivez une lettre de motivation'; return }
  if (!applyForm.value.proposedBudget || applyForm.value.proposedBudget <= 0) { applyError.value = 'Entrez votre devis'; return }
  if (!applyForm.value.proposedDays   || applyForm.value.proposedDays   <= 0) { applyError.value = 'Entrez votre délai estimé'; return }

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
