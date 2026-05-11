<template>
  <div class="max-w-6xl mx-auto space-y-6 pb-8">

    <!-- ─── HEADER ─────────────────────────────────────── -->
    <div class="flex items-start justify-between gap-4 pt-1">
      <div>
        <p class="text-xs font-medium text-[#9C9A92] uppercase tracking-widest mb-1">{{ greeting }}</p>
        <h1 class="text-2xl font-bold text-ink">
          {{ profile.firstName || displayName }}
          <span v-if="profile.lastName">{{ profile.lastName }}</span>
        </h1>
        <p class="text-sm text-[#73726C] mt-0.5">
          <span v-if="profile.title">{{ profile.title }}</span>
          <RouterLink v-else to="/freelancer/profile"
                      class="text-brand-600 hover:underline">
            Ajoutez un titre professionnel →
          </RouterLink>
        </p>
      </div>
      <div class="text-right flex-shrink-0">
        <p class="text-xs text-[#9C9A92]">{{ dayName }}</p>
        <p class="text-sm font-semibold text-ink">{{ dayNumber }} {{ monthName }} {{ year }}</p>
        <div class="flex items-center justify-end gap-1.5 mt-1">
          <span class="inline-block w-1.5 h-1.5 rounded-full"
                :class="profile.isAvailable !== false ? 'bg-brand-500' : 'bg-[#9C9A92]'"></span>
          <span class="text-xs text-[#73726C]">
            {{ profile.isAvailable !== false ? 'Disponible' : 'Indisponible' }}
          </span>
        </div>
      </div>
    </div>

    <!-- ─── 4 STATS ─────────────────────────────────────── -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-3">

      <div class="bg-white border border-[#EBEBE5] rounded-xl p-5">
        <p class="text-xs text-[#9C9A92] mb-3">Solde disponible</p>
        <div v-if="loadingWallet" class="h-7 w-24 bg-[#F4F4ED] rounded animate-pulse"></div>
        <p v-else class="text-2xl font-bold text-ink tabular-nums">
          {{ formatAmount(wallet.balance) }}
          <span class="text-sm font-normal text-[#9C9A92]">DH</span>
        </p>
      </div>

      <div class="bg-white border border-[#EBEBE5] rounded-xl p-5">
        <p class="text-xs text-[#9C9A92] mb-3">Total gagné</p>
        <div v-if="loadingWallet" class="h-7 w-24 bg-[#F4F4ED] rounded animate-pulse"></div>
        <p v-else class="text-2xl font-bold text-ink tabular-nums">
          {{ formatAmount(wallet.totalEarned) }}
          <span class="text-sm font-normal text-[#9C9A92]">DH</span>
        </p>
      </div>

      <div class="bg-white border border-[#EBEBE5] rounded-xl p-5">
        <p class="text-xs text-[#9C9A92] mb-3">Candidatures</p>
        <div v-if="loadingApplications" class="h-7 w-12 bg-[#F4F4ED] rounded animate-pulse"></div>
        <p v-else class="text-2xl font-bold text-ink tabular-nums">
          {{ pendingCount }}
          <span class="text-sm font-normal text-[#9C9A92]">en attente</span>
        </p>
      </div>

      <div class="bg-white border border-[#EBEBE5] rounded-xl p-5">
        <p class="text-xs text-[#9C9A92] mb-3">Note moyenne</p>
        <div v-if="loadingProfile" class="h-7 w-16 bg-[#F4F4ED] rounded animate-pulse"></div>
        <p v-else class="text-2xl font-bold text-ink tabular-nums">
          {{ profile.averageRating ? profile.averageRating.toFixed(1) : '—' }}
          <span class="text-sm font-normal text-[#9C9A92]">/ 5</span>
        </p>
      </div>

    </div>

    <!-- ─── MILIEU : TRANSACTIONS + PROFIL ──────────────── -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <!-- transactions récentes -->
      <div class="lg:col-span-2 bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">

        <div class="flex items-center justify-between px-5 py-4 border-b border-[#EBEBE5]">
          <p class="text-sm font-semibold text-ink">Transactions récentes</p>
          <RouterLink to="/freelancer/wallet"
                      class="text-xs text-[#73726C] hover:text-ink transition">
            Voir tout
          </RouterLink>
        </div>

        <!-- squelette -->
        <div v-if="loadingWallet" class="divide-y divide-[#EBEBE5]">
          <div v-for="i in 5" :key="i" class="flex items-center gap-3 px-5 py-3.5 animate-pulse">
            <div class="w-8 h-8 rounded-lg bg-[#F4F4ED] flex-shrink-0"></div>
            <div class="flex-1">
              <div class="h-3 bg-[#F4F4ED] rounded w-40 mb-1.5"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-24"></div>
            </div>
            <div class="h-3 bg-[#F4F4ED] rounded w-16"></div>
          </div>
        </div>

        <!-- état vide -->
        <div v-else-if="transactions.length === 0"
             class="flex flex-col items-center justify-center py-16 text-center">
          <p class="text-sm font-medium text-ink mb-1">Aucune transaction</p>
          <p class="text-xs text-[#9C9A92]">Vos revenus apparaîtront ici après votre première mission</p>
        </div>

        <!-- liste -->
        <div v-else class="divide-y divide-[#EBEBE5]">
          <div v-for="tx in transactions.slice(0, 8)" :key="tx.id"
               class="flex items-center gap-3 px-5 py-3.5 hover:bg-[#FAFAF7] transition">

            <!-- icône type -->
            <div class="w-8 h-8 rounded-lg flex items-center justify-center flex-shrink-0"
                 :class="parseFloat(tx.amount) >= 0 ? 'bg-[#F0FDF4]' : 'bg-[#FEF2F2]'">
              <svg class="w-3.5 h-3.5"
                   :class="parseFloat(tx.amount) >= 0 ? 'text-green-600' : 'text-red-500'"
                   fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      :d="parseFloat(tx.amount) >= 0
                        ? 'M19 14l-7 7m0 0l-7-7m7 7V3'
                        : 'M5 10l7-7m0 0l7 7m-7-7v18'"/>
              </svg>
            </div>

            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium text-ink truncate">{{ tx.description || 'Transaction' }}</p>
              <p class="text-xs text-[#9C9A92]">{{ formatDate(tx.createdAt) }}</p>
            </div>

            <p class="text-sm font-semibold tabular-nums"
               :class="parseFloat(tx.amount) >= 0 ? 'text-green-600' : 'text-red-500'">
              {{ parseFloat(tx.amount) >= 0 ? '+' : '' }}{{ formatAmount(tx.amount) }} DH
            </p>

          </div>
        </div>

      </div>

      <!-- colonne droite -->
      <div class="space-y-4">

        <!-- profil -->
        <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
          <div class="px-5 py-4 border-b border-[#EBEBE5]">
            <p class="text-sm font-semibold text-ink">Profil</p>
          </div>
          <div class="p-5">

            <div class="flex items-center justify-between mb-2">
              <p class="text-xs text-[#73726C]">Complétude</p>
              <p class="text-xs font-bold text-ink">{{ profileCompletion }}%</p>
            </div>
            <div class="w-full h-1.5 bg-[#F4F4ED] rounded-full mb-4">
              <div class="h-full rounded-full bg-brand-500 transition-all duration-700"
                   :style="{ width: profileCompletion + '%' }"></div>
            </div>

            <div class="space-y-2.5">
              <div v-for="item in profileChecklist" :key="item.label"
                   class="flex items-center gap-2.5">
                <svg v-if="item.done" class="w-3.5 h-3.5 text-brand-500 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                </svg>
                <svg v-else class="w-3.5 h-3.5 text-[#D1D1CB] flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm0-2a6 6 0 100-12 6 6 0 000 12z" clip-rule="evenodd"/>
                </svg>
                <span class="text-xs" :class="item.done ? 'text-[#9C9A92] line-through' : 'text-[#5F5E5A]'">
                  {{ item.label }}
                </span>
              </div>
            </div>

            <RouterLink v-if="profileCompletion < 100" to="/freelancer/profile"
                        class="mt-4 flex items-center justify-between w-full text-xs font-medium text-ink hover:text-brand-600 transition pt-4 border-t border-[#EBEBE5]">
              <span>Compléter mon profil</span>
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
              </svg>
            </RouterLink>

          </div>
        </div>

        <!-- stats rapides -->
        <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
          <div class="px-5 py-4 border-b border-[#EBEBE5]">
            <p class="text-sm font-semibold text-ink">Activité</p>
          </div>
          <div class="divide-y divide-[#EBEBE5]">
            <div class="flex items-center justify-between px-5 py-3">
              <p class="text-xs text-[#73726C]">Missions complétées</p>
              <p class="text-sm font-semibold text-ink tabular-nums">{{ profile.completedMissions || 0 }}</p>
            </div>
            <div class="flex items-center justify-between px-5 py-3">
              <p class="text-xs text-[#73726C]">Total candidatures</p>
              <p class="text-sm font-semibold text-ink tabular-nums">{{ applications.length }}</p>
            </div>
            <div class="flex items-center justify-between px-5 py-3">
              <p class="text-xs text-[#73726C]">En attente paiement</p>
              <p class="text-sm font-semibold text-ink tabular-nums">
                {{ formatAmount(wallet.pendingBalance) }} DH
              </p>
            </div>
            <div class="flex items-center justify-between px-5 py-3">
              <p class="text-xs text-[#73726C]">Évaluations reçues</p>
              <p class="text-sm font-semibold text-ink tabular-nums">{{ profile.totalReviews || 0 }}</p>
            </div>
          </div>
        </div>

      </div>
    </div>

    <!-- ─── BAS : CANDIDATURES + OFFRES ────────────────── -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">

      <!-- candidatures récentes -->
      <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">

        <div class="flex items-center justify-between px-5 py-4 border-b border-[#EBEBE5]">
          <p class="text-sm font-semibold text-ink">Candidatures récentes</p>
          <RouterLink to="/freelancer/applications"
                      class="text-xs text-[#73726C] hover:text-ink transition">
            Voir tout
          </RouterLink>
        </div>

        <!-- squelette -->
        <div v-if="loadingApplications" class="divide-y divide-[#EBEBE5]">
          <div v-for="i in 4" :key="i" class="flex items-center gap-3 px-5 py-3.5 animate-pulse">
            <div class="flex-1">
              <div class="h-3 bg-[#F4F4ED] rounded w-48 mb-1.5"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-28"></div>
            </div>
            <div class="h-5 w-16 bg-[#F4F4ED] rounded-full"></div>
          </div>
        </div>

        <!-- état vide -->
        <div v-else-if="recentApplications.length === 0"
             class="flex flex-col items-center justify-center py-12 text-center">
          <p class="text-sm font-medium text-ink mb-1">Aucune candidature</p>
          <p class="text-xs text-[#9C9A92] mb-3">Explorez les missions et postulez</p>
          <RouterLink to="/freelancer/explore"
                      class="text-xs font-medium text-brand-600 border border-brand-200 hover:bg-brand-50 px-3 py-1.5 rounded-lg transition">
            Explorer les offres
          </RouterLink>
        </div>

        <!-- liste -->
        <div v-else class="divide-y divide-[#EBEBE5]">
          <div v-for="app in recentApplications" :key="app.id"
               class="flex items-center gap-3 px-5 py-3.5 hover:bg-[#FAFAF7] transition">
            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium text-ink truncate">
                {{ app.offerTitle || 'Mission #' + app.offerId }}
              </p>
              <p class="text-xs text-[#9C9A92]">{{ formatDate(app.createdAt) }}</p>
            </div>
            <span class="text-xs font-medium px-2.5 py-1 rounded-full whitespace-nowrap"
                  :class="statusBadge(app.status)">
              {{ statusLabel(app.status) }}
            </span>
          </div>
        </div>

      </div>

      <!-- offres récentes -->
      <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">

        <div class="flex items-center justify-between px-5 py-4 border-b border-[#EBEBE5]">
          <p class="text-sm font-semibold text-ink">Offres disponibles</p>
          <RouterLink to="/freelancer/explore"
                      class="text-xs text-[#73726C] hover:text-ink transition">
            Voir tout
          </RouterLink>
        </div>

        <!-- squelette -->
        <div v-if="loadingOffers" class="divide-y divide-[#EBEBE5]">
          <div v-for="i in 4" :key="i" class="px-5 py-3.5 animate-pulse">
            <div class="h-3 bg-[#F4F4ED] rounded w-3/4 mb-1.5"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-1/3"></div>
          </div>
        </div>

        <!-- état vide -->
        <div v-else-if="recentOffers.length === 0"
             class="flex flex-col items-center justify-center py-12 text-center">
          <p class="text-sm font-medium text-ink mb-1">Aucune offre disponible</p>
          <p class="text-xs text-[#9C9A92]">Revenez bientôt</p>
        </div>

        <!-- liste -->
        <div v-else class="divide-y divide-[#EBEBE5]">
          <RouterLink v-for="offer in recentOffers" :key="offer.id"
                      to="/freelancer/explore"
                      class="flex items-start justify-between gap-3 px-5 py-3.5 hover:bg-[#FAFAF7] transition group cursor-pointer">
            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium text-ink truncate group-hover:text-brand-600 transition">
                {{ offer.title }}
              </p>
              <p class="text-xs text-[#9C9A92] mt-0.5">
                {{ formatDate(offer.createdAt) }}
                <span v-if="offer.category"> · {{ offer.category }}</span>
              </p>
            </div>
            <p class="text-sm font-semibold text-ink whitespace-nowrap flex-shrink-0">
              {{ offerBudget(offer) }}
            </p>
          </RouterLink>
        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const authHeaders = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

// ── État ────────────────────────────────────
const profile      = ref({})
const wallet       = ref({ balance: 0, totalEarned: 0, pendingBalance: 0 })
const transactions = ref([])
const applications = ref([])
const recentOffers = ref([])

const loadingProfile      = ref(true)
const loadingWallet       = ref(true)
const loadingApplications = ref(true)
const loadingOffers       = ref(true)

// ── Date ────────────────────────────────────
const today    = new Date()
const DAYS     = ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi']
const MONTHS   = ['jan.', 'fév.', 'mars', 'avr.', 'mai', 'juin', 'juil.', 'août', 'sep.', 'oct.', 'nov.', 'déc.']
const MONTHS_L = ['Jan', 'Fév', 'Mars', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc']

const dayName   = DAYS[today.getDay()]
const dayNumber = today.getDate()
const monthName = MONTHS_L[today.getMonth()]
const year      = today.getFullYear()

const greeting = computed(() => {
  const h = today.getHours()
  if (h < 12) return 'Bonjour'
  if (h < 18) return 'Bon après-midi'
  return 'Bonsoir'
})

// ── Nom affiché ─────────────────────────────
const displayName = computed(() => {
  const stored = JSON.parse(localStorage.getItem('user') || '{}')
  const email = stored.email || ''
  const name = email.split('@')[0].replace(/[._]/g, ' ')
  return name.charAt(0).toUpperCase() + name.slice(1)
})

// ── Candidatures ────────────────────────────
const recentApplications = computed(() =>
  [...applications.value]
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 6)
)

const pendingCount = computed(() =>
  applications.value.filter(a => a.status === 'PENDING').length
)

// ── Profil complétude ────────────────────────
const profileChecklist = computed(() => [
  { label: 'Email vérifié',         done: !!profile.value.email },
  { label: 'Titre professionnel',   done: !!profile.value.title },
  { label: 'Bio',                   done: !!profile.value.bio },
  { label: 'Compétences',          done: !!profile.value.skills },
  { label: 'Tarif horaire',         done: !!profile.value.hourlyRate },
  { label: 'Localisation',         done: !!profile.value.location },
])

const profileCompletion = computed(() => {
  const done = profileChecklist.value.filter(i => i.done).length
  return Math.round((done / profileChecklist.value.length) * 100)
})

// ── Helpers ─────────────────────────────────
const formatAmount = (val) =>
  Number(val || 0).toLocaleString('fr-MA', { minimumFractionDigits: 0, maximumFractionDigits: 2 })

const formatDate = (iso) => {
  if (!iso) return '—'
  const d = new Date(iso)
  return `${d.getDate()} ${MONTHS[d.getMonth()]} ${d.getFullYear()}`
}

const statusLabel = (s) =>
  ({ PENDING: 'En attente', ACCEPTED: 'Acceptée', REJECTED: 'Refusée', WITHDRAWN: 'Retirée' }[s] || s)

const statusBadge = (s) => ({
  PENDING:   'bg-amber-50 text-amber-700',
  ACCEPTED:  'bg-green-50 text-green-700',
  REJECTED:  'bg-red-50 text-red-600',
  WITHDRAWN: 'bg-[#F4F4ED] text-[#73726C]',
}[s] || 'bg-[#F4F4ED] text-[#73726C]')

const offerBudget = (offer) => {
  if (offer.budgetMin && offer.budgetMax) return `${offer.budgetMin}–${offer.budgetMax} DH`
  if (offer.budgetMin) return `${offer.budgetMin} DH`
  if (offer.budgetMax) return `${offer.budgetMax} DH`
  return '—'
}

// ── Chargement ──────────────────────────────
const loadProfile = async () => {
  loadingProfile.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/profile`, authHeaders())
    profile.value = data
  } catch { profile.value = {} }
  finally { loadingProfile.value = false }
}

const loadWallet = async () => {
  loadingWallet.value = true
  try {
    const [w, t] = await Promise.all([
      axios.get(`${API_URL}/api/wallet`, authHeaders()),
      axios.get(`${API_URL}/api/wallet/transactions`, authHeaders()),
    ])
    wallet.value       = w.data.wallet || {}
    transactions.value = t.data || []
  } catch {
    wallet.value       = { balance: 0, totalEarned: 0, pendingBalance: 0 }
    transactions.value = []
  } finally { loadingWallet.value = false }
}

const loadApplications = async () => {
  loadingApplications.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/applications/my`, authHeaders())
    applications.value = data || []
  } catch { applications.value = [] }
  finally { loadingApplications.value = false }
}

const loadOffers = async () => {
  loadingOffers.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/offers`, { params: { page: 0, size: 6 } })
    recentOffers.value = (data.content || data || []).slice(0, 6)
  } catch { recentOffers.value = [] }
  finally { loadingOffers.value = false }
}

onMounted(() => {
  Promise.allSettled([loadProfile(), loadWallet(), loadApplications(), loadOffers()])
})
</script>
