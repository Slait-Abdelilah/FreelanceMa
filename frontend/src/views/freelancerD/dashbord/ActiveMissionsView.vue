<template>
  <div class="max-w-4xl mx-auto space-y-5">

    <!-- EN-TÊTE -->
    <div class="flex items-start justify-between gap-4">
      <div>
        <h1 class="text-xl font-bold text-ink">Missions actives</h1>
        <p class="text-[13px] text-[#73726C] mt-0.5">Vos candidatures acceptées en cours</p>
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
      <div v-for="i in 3" :key="i" class="bg-white rounded-xl border border-[#EBEBE5] p-5 animate-pulse">
        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 space-y-2">
            <div class="h-4 w-2/3 bg-[#EBEBE5] rounded"></div>
            <div class="h-3 w-1/3 bg-[#F4F4ED] rounded"></div>
          </div>
          <div class="h-6 w-20 bg-[#F4F4ED] rounded-full"></div>
        </div>
        <div class="flex gap-4 mt-4">
          <div class="h-3 w-24 bg-[#F4F4ED] rounded"></div>
          <div class="h-3 w-24 bg-[#F4F4ED] rounded"></div>
          <div class="h-3 w-20 bg-[#F4F4ED] rounded"></div>
        </div>
      </div>
    </div>

    <!-- ÉTAT VIDE -->
    <div v-else-if="missions.length === 0"
         class="bg-white rounded-xl border border-dashed border-[#EBEBE5] py-20 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-6 h-6 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
        </svg>
      </div>
      <h3 class="text-[14px] font-semibold text-ink mb-1">Aucune mission active</h3>
      <p class="text-[13px] text-[#73726C] mb-6 max-w-xs mx-auto">
        Postulez à des offres pour démarrer vos premières missions.
      </p>
      <RouterLink to="/freelancer/explore"
                  class="inline-flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-5 py-2.5 rounded-lg transition">
        Explorer les offres
      </RouterLink>
    </div>

    <!-- LISTE DES MISSIONS -->
    <div v-else class="space-y-3">
      <div v-for="mission in missions" :key="mission.id"
           class="bg-white rounded-xl border border-[#EBEBE5] p-5">

        <!-- ligne principale -->
        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 min-w-0">
            <h2 class="text-[14px] font-semibold text-ink truncate">{{ mission.offerTitle }}</h2>
            <p class="text-xs text-[#9C9A92] mt-0.5">
              Acceptée le {{ formatDate(mission.createdAt) }}
              · <span class="text-[#5F5E5A]">{{ daysElapsed(mission.createdAt) }} jours en cours</span>
            </p>
          </div>
          <span class="text-[11px] font-medium px-2.5 py-1 rounded-full bg-green-50 text-green-700 whitespace-nowrap flex-shrink-0">
            En cours
          </span>
        </div>

        <!-- détails -->
        <div class="flex flex-wrap gap-x-6 gap-y-1.5 mt-4">

          <div v-if="mission.proposedBudget" class="flex items-center gap-1.5">
            <svg class="w-3.5 h-3.5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33"/>
            </svg>
            <span class="text-[13px] font-semibold text-ink tabular-nums">{{ mission.proposedBudget.toLocaleString('fr-MA') }} DH</span>
            <span class="text-xs text-[#9C9A92]">proposé</span>
          </div>

          <div v-else-if="mission.offerBudgetMin || mission.offerBudgetMax" class="flex items-center gap-1.5">
            <svg class="w-3.5 h-3.5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33"/>
            </svg>
            <span class="text-[13px] font-semibold text-ink tabular-nums">{{ offerBudget(mission) }}</span>
            <span class="text-xs text-[#9C9A92]">budget</span>
          </div>

          <div v-if="mission.proposedDays" class="flex items-center gap-1.5">
            <svg class="w-3.5 h-3.5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 9v7.5"/>
            </svg>
            <span class="text-[13px] text-ink tabular-nums">{{ mission.proposedDays }} jours</span>
            <span class="text-xs text-[#9C9A92]">délai prévu</span>
          </div>

          <div v-if="mission.offerCategory" class="flex items-center gap-1.5">
            <svg class="w-3.5 h-3.5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9.568 3H5.25A2.25 2.25 0 003 5.25v4.318c0 .597.237 1.17.659 1.591l9.581 9.581c.699.699 1.78.872 2.607.33a18.095 18.095 0 005.223-5.223c.542-.827.369-1.908-.33-2.607L11.16 3.66A2.25 2.25 0 009.568 3z"/>
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 6h.008v.008H6V6z"/>
            </svg>
            <span class="text-[13px] text-[#5F5E5A]">{{ mission.offerCategory }}</span>
          </div>

        </div>

        <!-- barre de progression par rapport au délai -->
        <div v-if="mission.proposedDays" class="mt-4">
          <div class="flex items-center justify-between mb-1.5">
            <span class="text-[11px] text-[#9C9A92]">Progression (délai)</span>
            <span class="text-[11px] font-medium tabular-nums"
                  :class="progressPercent(mission) >= 90 ? 'text-red-500' : progressPercent(mission) >= 70 ? 'text-amber-500' : 'text-[#5F5E5A]'">
              {{ daysElapsed(mission.createdAt) }} / {{ mission.proposedDays }} j
            </span>
          </div>
          <div class="h-1 bg-[#F4F4ED] rounded-full overflow-hidden">
            <div class="h-full rounded-full transition-all duration-500"
                 :class="progressPercent(mission) >= 90 ? 'bg-red-400' : progressPercent(mission) >= 70 ? 'bg-amber-400' : 'bg-green-500'"
                 :style="{ width: Math.min(progressPercent(mission), 100) + '%' }"></div>
          </div>
        </div>

        <!-- actions -->
        <div class="flex items-center justify-between mt-4 pt-4 border-t border-[#EBEBE5]">
          <RouterLink to="/freelancer/messages"
                      class="text-[12px] font-medium text-[#73726C] hover:text-ink transition flex items-center gap-1.5">
            <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
            Contacter le client
          </RouterLink>

          <button @click="completeMission(mission)"
                  :disabled="completing === mission.id"
                  class="flex items-center gap-1.5 text-[12px] font-semibold px-3 py-1.5 rounded-lg border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[#5F5E5A] transition disabled:opacity-50">
            <svg v-if="completing === mission.id" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <svg v-else class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
            </svg>
            {{ completing === mission.id ? 'En cours...' : 'Marquer terminé' }}
          </button>
        </div>

      </div>
    </div>

    <!-- MISSIONS TERMINÉES -->
    <div v-if="!loading && completed.length > 0" class="space-y-3">
      <div class="flex items-center gap-3">
        <div class="flex-1 h-px bg-[#EBEBE5]"></div>
        <span class="text-[11px] text-[#9C9A92] font-medium">Terminées ({{ completed.length }})</span>
        <div class="flex-1 h-px bg-[#EBEBE5]"></div>
      </div>
      <div v-for="mission in completed" :key="mission.id"
           class="bg-white rounded-xl border border-[#EBEBE5] p-5 opacity-60">
        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 min-w-0">
            <h2 class="text-[14px] font-medium text-ink truncate">{{ mission.offerTitle }}</h2>
            <p class="text-xs text-[#9C9A92] mt-0.5">
              Terminée le {{ formatDate(mission.completedAt) }}
            </p>
          </div>
          <span class="text-[11px] font-medium px-2.5 py-1 rounded-full bg-[#F4F4ED] text-[#73726C] whitespace-nowrap flex-shrink-0">
            Terminée
          </span>
        </div>
        <div v-if="mission.proposedBudget || mission.offerBudgetMin" class="mt-3">
          <span class="text-[13px] font-semibold text-ink tabular-nums">
            {{ mission.proposedBudget ? Number(mission.proposedBudget).toLocaleString('fr-MA') + ' DH' : offerBudget(mission) }}
          </span>
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

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

const loading = ref(true)
const completing = ref(null)
const toast = ref({ show: false, message: '', type: 'success' })
const applications = ref([])

const missions = computed(() =>
  applications.value.filter(a => a.status === 'ACCEPTED')
)

const completed = computed(() =>
  applications.value.filter(a => a.status === 'COMPLETED')
    .sort((a, b) => new Date(b.completedAt) - new Date(a.completedAt))
)

const loadMissions = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/applications/my`, headers())
    applications.value = data || []
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
    showToast('Mission marquée comme terminée !')
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la mise à jour', 'error')
  } finally {
    completing.value = null
  }
}

const formatDate = (iso) => {
  if (!iso) return '—'
  const d = new Date(iso)
  const months = ['jan', 'fév', 'mars', 'avr', 'mai', 'juin', 'juil', 'août', 'sep', 'oct', 'nov', 'déc']
  return `${d.getDate()} ${months[d.getMonth()]} ${d.getFullYear()}`
}

const daysElapsed = (iso) => {
  if (!iso) return 0
  return Math.floor((Date.now() - new Date(iso)) / 86400000)
}

const progressPercent = (mission) => {
  if (!mission.proposedDays) return 0
  return Math.round((daysElapsed(mission.createdAt) / mission.proposedDays) * 100)
}

const offerBudget = (m) => {
  if (m.offerBudgetMin && m.offerBudgetMax)
    return `${Number(m.offerBudgetMin).toLocaleString('fr-MA')}–${Number(m.offerBudgetMax).toLocaleString('fr-MA')} DH`
  if (m.offerBudgetMin) return `${Number(m.offerBudgetMin).toLocaleString('fr-MA')} DH`
  if (m.offerBudgetMax) return `${Number(m.offerBudgetMax).toLocaleString('fr-MA')} DH`
  return '—'
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4000)
}

onMounted(() => { loadMissions() })
</script>
