<template>
  <div class="space-y-5">

    <!-- HEADER -->
    <div>
      <h1 class="text-[20px] font-semibold text-ink tracking-tight">Candidatures reçues</h1>
      <p class="text-[13px] text-[#9C9A92] mt-0.5">Gérez les candidatures pour vos offres</p>
    </div>

    <!-- SÉLECTEUR D'OFFRE -->
    <div class="bg-white border border-[#EBEBE5] rounded-xl p-4">
      <div v-if="loadingOffers" class="animate-pulse flex items-center gap-3">
        <div class="h-9 bg-[#F4F4ED] rounded-lg flex-1"></div>
      </div>
      <div v-else-if="offers.length === 0" class="text-[13px] text-[#9C9A92]">
        Aucune offre publiée. <RouterLink to="/client/offers" class="text-ink font-medium hover:underline">Créer une offre</RouterLink>
      </div>
      <div v-else class="flex flex-wrap gap-2">
        <button v-for="offer in offers" :key="offer.id"
                @click="selectOffer(offer)"
                class="flex items-center gap-2 px-3 py-2 rounded-lg text-[12px] font-medium border transition"
                :class="selectedOffer?.id === offer.id
                  ? 'bg-ink text-white border-ink'
                  : 'bg-white text-[#5F5E5A] border-[#EBEBE5] hover:border-[#D3D1C7] hover:text-ink'">
          <span class="truncate max-w-[160px]">{{ offer.title }}</span>
          <span class="text-[10px] opacity-70">{{ offer.applicationsCount }}</span>
          <span class="w-1.5 h-1.5 rounded-full flex-shrink-0"
                :class="offer.status === 'OPEN' ? 'bg-green-400' : 'bg-[#D3D1C7]'"></span>
        </button>
      </div>
    </div>

    <!-- CANDIDATURES -->
    <template v-if="selectedOffer">

      <!-- sous-header -->
      <div class="flex items-center justify-between">
        <div>
          <p class="text-[14px] font-semibold text-ink">{{ selectedOffer.title }}</p>
          <p class="text-[12px] text-[#9C9A92] mt-0.5">
            {{ applications.length }} candidature{{ applications.length !== 1 ? 's' : '' }}
            · {{ pendingCount }} en attente
          </p>
        </div>
        <!-- filter tabs -->
        <div class="flex gap-1 bg-[#F4F4ED] p-1 rounded-lg">
          <button v-for="tab in appTabs" :key="tab.value" @click="appTab = tab.value"
                  class="text-[11px] font-medium px-2.5 py-1 rounded-md transition"
                  :class="appTab === tab.value ? 'bg-white text-ink shadow-sm' : 'text-[#73726C] hover:text-ink'">
            {{ tab.label }}
          </button>
        </div>
      </div>

      <!-- skeleton -->
      <div v-if="loadingApps" class="space-y-3">
        <div v-for="i in 3" :key="i" class="bg-white border border-[#EBEBE5] rounded-xl p-5 animate-pulse">
          <div class="flex items-start gap-3">
            <div class="w-10 h-10 bg-[#F4F4ED] rounded-full flex-shrink-0"></div>
            <div class="flex-1 space-y-2">
              <div class="h-3.5 bg-[#F4F4ED] rounded w-1/4"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-3/4"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-1/2"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- liste -->
      <div v-else-if="filteredApps.length > 0" class="space-y-3">
        <div v-for="app in filteredApps" :key="app.id"
             class="bg-white border border-[#EBEBE5] rounded-xl p-5 transition"
             :class="app.status === 'PENDING' ? 'hover:border-[#D3D1C7]' : 'opacity-80'">

          <div class="flex items-start gap-4">
            <!-- avatar -->
            <div class="w-10 h-10 bg-[#F0EFE6] border border-[#EBEBE5] rounded-full flex items-center justify-center text-[13px] font-semibold text-ink flex-shrink-0">
              #{{ String(app.freelancerId).slice(-3) }}
            </div>

            <div class="flex-1 min-w-0">
              <div class="flex items-center justify-between gap-3">
                <p class="text-[13px] font-semibold text-ink">Candidat #{{ app.freelancerId }}</p>
                <span class="text-[11px] font-semibold px-2.5 py-0.5 rounded-full border flex-shrink-0"
                      :class="statusStyle(app.status).pill">
                  {{ statusStyle(app.status).label }}
                </span>
              </div>

              <!-- cover letter -->
              <div class="mt-2">
                <p v-if="!expanded.has(app.id)" class="text-[12px] text-[#73726C] line-clamp-2">
                  {{ app.coverLetter || 'Aucune lettre de motivation' }}
                </p>
                <p v-else class="text-[12px] text-[#73726C] whitespace-pre-wrap">
                  {{ app.coverLetter || 'Aucune lettre de motivation' }}
                </p>
                <button v-if="app.coverLetter && app.coverLetter.length > 120"
                        @click="toggleExpand(app.id)"
                        class="text-[11px] text-ink font-medium mt-1 hover:underline">
                  {{ expanded.has(app.id) ? 'Réduire' : 'Lire la suite' }}
                </button>
              </div>

              <!-- meta -->
              <div class="flex flex-wrap gap-x-4 gap-y-1 mt-3 text-[11px] text-[#9C9A92]">
                <span v-if="app.proposedBudget">Budget proposé : <strong class="text-ink">{{ app.proposedBudget }} DH</strong></span>
                <span v-if="app.proposedDays">Délai : <strong class="text-ink">{{ app.proposedDays }} jour{{ app.proposedDays > 1 ? 's' : '' }}</strong></span>
                <span>Reçue {{ timeAgo(app.createdAt) }}</span>
              </div>

              <!-- actions -->
              <div v-if="app.status === 'PENDING'" class="flex gap-2 mt-4">
                <button @click="doAccept(app)" :disabled="actionId === app.id"
                        class="text-[12px] font-medium bg-ink hover:bg-[#1A1A18] text-white px-4 py-1.5 rounded-lg transition disabled:opacity-50">
                  {{ actionId === app.id ? '...' : 'Accepter' }}
                </button>
                <button @click="doReject(app)" :disabled="actionId === app.id"
                        class="text-[12px] font-medium text-red-600 border border-[#EBEBE5] hover:border-red-200 hover:bg-red-50 px-4 py-1.5 rounded-lg transition disabled:opacity-50">
                  Refuser
                </button>
              </div>
            </div>
          </div>

        </div>
      </div>

      <!-- empty -->
      <div v-else class="bg-white border border-[#EBEBE5] rounded-xl p-10 text-center">
        <p class="text-[14px] font-semibold text-ink">
          {{ appTab === 'all' ? 'Aucune candidature' : `Aucune candidature ${appTabLabel}` }}
        </p>
        <p class="text-[12px] text-[#9C9A92] mt-1">Les candidatures apparaîtront ici au fil du temps</p>
      </div>

    </template>

    <!-- NO OFFER SELECTED -->
    <div v-else-if="!loadingOffers && offers.length > 0" class="bg-white border border-[#EBEBE5] rounded-xl p-10 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
          <path stroke-linecap="round" stroke-linejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">Sélectionnez une offre</p>
      <p class="text-[12px] text-[#9C9A92] mt-1">Choisissez une offre ci-dessus pour voir ses candidatures</p>
    </div>

    <!-- TOAST -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2" name="toast">
      <div v-if="toast.message"
           class="fixed bottom-5 left-1/2 -translate-x-1/2 text-[13px] font-medium px-4 py-2.5 rounded-xl shadow-lg z-50"
           :class="toast.type === 'error' ? 'bg-red-600 text-white' : 'bg-ink text-white'">
        {{ toast.message }}
      </div>
    </Transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()

const offers       = ref([])
const applications = ref([])
const selectedOffer = ref(null)
const loadingOffers = ref(true)
const loadingApps   = ref(false)
const actionId      = ref(null)
const toast         = ref({ message: '', type: 'success' })
const appTab        = ref('all')
const expanded      = ref(new Set())

const appTabs = [
  { value: 'all',                 label: 'Toutes'     },
  { value: 'PENDING',             label: 'En attente' },
  { value: 'ACCEPTED',            label: 'Acceptées'  },
  { value: 'AWAITING_VALIDATION', label: 'À valider'  },
  { value: 'REJECTED',            label: 'Refusées'   },
]

const pendingCount   = computed(() => applications.value.filter(a => a.status === 'PENDING').length)
const appTabLabel    = computed(() => appTabs.find(t => t.value === appTab.value)?.label.toLowerCase() || '')
const filteredApps   = computed(() => {
  if (appTab.value === 'all') return applications.value
  return applications.value.filter(a => a.status === appTab.value)
})

const token   = () => localStorage.getItem('token')
const headers = () => ({ Authorization: `Bearer ${token()}` })

const loadOffers = async () => {
  loadingOffers.value = true
  try {
    const { data } = await axios.get('http://localhost:8080/api/offers/my', { headers: headers() })
    offers.value = data
    // auto-select from query param
    const qId = route.query.offer ? Number(route.query.offer) : null
    const target = qId ? data.find(o => o.id === qId) : null
    if (target) selectOffer(target)
  } finally { loadingOffers.value = false }
}

const selectOffer = async (offer) => {
  selectedOffer.value = offer
  loadingApps.value = true
  appTab.value = 'all'
  expanded.value = new Set()
  try {
    const { data } = await axios.get(`http://localhost:8080/api/offers/${offer.id}/applications`, { headers: headers() })
    applications.value = data
  } finally { loadingApps.value = false }
}

const toggleExpand = (id) => {
  const s = new Set(expanded.value)
  s.has(id) ? s.delete(id) : s.add(id)
  expanded.value = s
}

const doAccept = async (app) => {
  actionId.value = app.id
  try {
    const { data } = await axios.put(`http://localhost:8080/api/applications/${app.id}/accept`, {}, { headers: headers() })
    const idx = applications.value.findIndex(a => a.id === data.id)
    if (idx !== -1) applications.value[idx] = data
    showToast('Candidature acceptée')
  } catch (e) { showToast(e.response?.data?.message || 'Erreur', 'error')
  } finally { actionId.value = null }
}

const doReject = async (app) => {
  actionId.value = app.id
  try {
    const { data } = await axios.put(`http://localhost:8080/api/applications/${app.id}/reject`, {}, { headers: headers() })
    const idx = applications.value.findIndex(a => a.id === data.id)
    if (idx !== -1) applications.value[idx] = data
    showToast('Candidature refusée')
  } catch (e) { showToast(e.response?.data?.message || 'Erreur', 'error')
  } finally { actionId.value = null }
}

const statusStyle = (s) => ({
  PENDING:              { label: 'En attente',    pill: 'bg-amber-50 text-amber-600 border-amber-100'   },
  ACCEPTED:             { label: 'Acceptée',      pill: 'bg-green-50 text-green-600 border-green-100'   },
  REJECTED:             { label: 'Refusée',       pill: 'bg-red-50 text-red-400 border-red-100'         },
  WITHDRAWN:            { label: 'Retirée',       pill: 'bg-[#F4F4ED] text-[#9C9A92] border-[#EBEBE5]' },
  AWAITING_VALIDATION:  { label: 'À valider',     pill: 'bg-amber-50 text-amber-700 border-amber-200'   },
  COMPLETED:            { label: 'Terminée',      pill: 'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]' },
}[s] || { label: s, pill: 'bg-[#F4F4ED] text-[#9C9A92] border-[#EBEBE5]' })

const timeAgo = (iso) => {
  const diff = Date.now() - new Date(iso).getTime()
  const m = Math.floor(diff / 60000)
  if (m < 1) return "à l'instant"
  if (m < 60) return `il y a ${m} min`
  const h = Math.floor(m / 60)
  if (h < 24) return `il y a ${h}h`
  return `il y a ${Math.floor(h / 24)}j`
}

let toastTimer = null
const showToast = (msg, type = 'success') => {
  toast.value = { message: msg, type }
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => { toast.value = { message: '', type: 'success' } }, 3000)
}

onMounted(loadOffers)
</script>
