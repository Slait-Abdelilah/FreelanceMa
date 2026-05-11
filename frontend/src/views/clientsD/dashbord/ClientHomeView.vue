<template>
  <div class="space-y-6">

    <!-- HEADER -->
    <div class="flex items-start justify-between">
      <div>
        <h1 class="text-[20px] font-semibold text-ink tracking-tight">Vue d'ensemble</h1>
        <p class="text-[13px] text-[#9C9A92] mt-0.5">{{ today }}</p>
      </div>
      <RouterLink to="/client/offers?new=true"
                  class="flex items-center gap-1.5 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-medium px-4 py-2 rounded-lg transition">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
        </svg>
        Publier une offre
      </RouterLink>
    </div>

    <!-- STAT CARDS -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-3">

      <div v-for="stat in stats" :key="stat.label"
           class="bg-white border border-[#EBEBE5] rounded-xl p-4">
        <div v-if="loading" class="space-y-2 animate-pulse">
          <div class="h-3 bg-[#F4F4ED] rounded w-2/3"></div>
          <div class="h-7 bg-[#F4F4ED] rounded w-1/2"></div>
        </div>
        <template v-else>
          <p class="text-xs text-[#9C9A92]">{{ stat.label }}</p>
          <p class="text-[26px] font-semibold text-ink mt-0.5 tabular-nums">{{ stat.value }}</p>
          <p v-if="stat.sub" class="text-[11px] mt-1" :class="stat.subColor || 'text-[#9C9A92]'">{{ stat.sub }}</p>
        </template>
      </div>

    </div>

    <!-- GRID : offres récentes + candidatures récentes -->
    <div class="grid lg:grid-cols-2 gap-4">

      <!-- MES OFFRES RÉCENTES -->
      <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
        <div class="px-5 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
          <p class="text-[14px] font-semibold text-ink">Mes offres récentes</p>
          <RouterLink to="/client/offers" class="text-[12px] text-[#9C9A92] hover:text-ink transition">Voir tout</RouterLink>
        </div>

        <div v-if="loading" class="divide-y divide-[#EBEBE5]">
          <div v-for="i in 4" :key="i" class="px-5 py-3.5 flex items-center gap-3 animate-pulse">
            <div class="w-8 h-8 bg-[#F4F4ED] rounded-lg flex-shrink-0"></div>
            <div class="flex-1 space-y-1.5">
              <div class="h-3 bg-[#F4F4ED] rounded w-3/4"></div>
              <div class="h-2.5 bg-[#F4F4ED] rounded w-1/2"></div>
            </div>
          </div>
        </div>

        <div v-else-if="recentOffers.length === 0" class="px-5 py-10 text-center">
          <p class="text-[13px] text-[#9C9A92]">Aucune offre publiée</p>
          <RouterLink to="/client/offers?new=true" class="text-[12px] text-ink font-medium mt-1 inline-block hover:underline">
            Publier ma première offre
          </RouterLink>
        </div>

        <div v-else class="divide-y divide-[#EBEBE5]">
          <div v-for="offer in recentOffers" :key="offer.id" class="px-5 py-3.5 flex items-center gap-3 hover:bg-[#FAFAF7] transition">
            <div class="w-8 h-8 rounded-lg flex-shrink-0 flex items-center justify-center border border-[#EBEBE5] bg-[#F4F4ED]">
              <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z"/>
              </svg>
            </div>
            <div class="flex-1 min-w-0">
              <p class="text-[13px] font-medium text-ink truncate">{{ offer.title }}</p>
              <p class="text-[11px] text-[#9C9A92] mt-0.5">
                {{ offer.applicationsCount }} candidature{{ offer.applicationsCount !== 1 ? 's' : '' }}
                · {{ timeAgo(offer.createdAt) }}
              </p>
            </div>
            <span class="text-[10px] font-semibold px-2 py-0.5 rounded-full flex-shrink-0"
                  :class="offer.status === 'OPEN'
                    ? 'bg-green-50 text-green-600 border border-green-100'
                    : 'bg-[#F4F4ED] text-[#9C9A92] border border-[#EBEBE5]'">
              {{ offer.status === 'OPEN' ? 'Ouverte' : 'Clôturée' }}
            </span>
          </div>
        </div>
      </div>

      <!-- CANDIDATURES RÉCENTES -->
      <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
        <div class="px-5 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
          <p class="text-[14px] font-semibold text-ink">Candidatures récentes</p>
          <RouterLink to="/client/applications" class="text-[12px] text-[#9C9A92] hover:text-ink transition">Voir tout</RouterLink>
        </div>

        <div v-if="loading" class="divide-y divide-[#EBEBE5]">
          <div v-for="i in 4" :key="i" class="px-5 py-3.5 flex items-center gap-3 animate-pulse">
            <div class="w-8 h-8 bg-[#F4F4ED] rounded-full flex-shrink-0"></div>
            <div class="flex-1 space-y-1.5">
              <div class="h-3 bg-[#F4F4ED] rounded w-2/3"></div>
              <div class="h-2.5 bg-[#F4F4ED] rounded w-1/3"></div>
            </div>
          </div>
        </div>

        <div v-else-if="recentApplications.length === 0" class="px-5 py-10 text-center">
          <p class="text-[13px] text-[#9C9A92]">Aucune candidature reçue</p>
          <p class="text-[11px] text-[#9C9A92] mt-1">Les candidatures apparaîtront ici</p>
        </div>

        <div v-else class="divide-y divide-[#EBEBE5]">
          <div v-for="app in recentApplications" :key="app.id"
               class="px-5 py-3.5 flex items-center gap-3 hover:bg-[#FAFAF7] transition">
            <div class="w-8 h-8 bg-[#F0EFE6] border border-[#EBEBE5] rounded-full flex items-center justify-center text-[11px] font-semibold text-ink flex-shrink-0">
              {{ String(app.freelancerId).slice(-2) }}
            </div>
            <div class="flex-1 min-w-0">
              <p class="text-[13px] font-medium text-ink truncate">{{ app.offerTitle }}</p>
              <p class="text-[11px] text-[#9C9A92] mt-0.5">
                {{ app.proposedBudget ? app.proposedBudget + ' DH' : '—' }}
                · {{ timeAgo(app.createdAt) }}
              </p>
            </div>
            <span class="text-[10px] font-semibold flex-shrink-0" :class="statusStyle(app.status).text">
              {{ statusStyle(app.status).label }}
            </span>
          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const loading = ref(true)
const offers  = ref([])
const allApplications = ref([])

const today = new Date().toLocaleDateString('fr-FR', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' })

const recentOffers = computed(() => offers.value.slice(0, 5))
const recentApplications = computed(() => [...allApplications.value]
  .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  .slice(0, 5)
)

const stats = computed(() => {
  const open     = offers.value.filter(o => o.status === 'OPEN').length
  const closed   = offers.value.filter(o => o.status === 'CLOSED').length
  const total    = allApplications.value.length
  const pending  = allApplications.value.filter(a => a.status === 'PENDING').length
  const accepted = allApplications.value.filter(a => a.status === 'ACCEPTED').length

  return [
    { label: 'Offres publiées',       value: offers.value.length, sub: `${open} ouverte${open > 1 ? 's' : ''}`, subColor: 'text-green-600' },
    { label: 'Candidatures reçues',   value: total,    sub: `${pending} en attente`, subColor: pending > 0 ? 'text-amber-600' : 'text-[#9C9A92]' },
    { label: 'Missions actives',      value: accepted, sub: accepted > 0 ? 'En cours' : 'Aucune en cours', subColor: accepted > 0 ? 'text-green-600' : 'text-[#9C9A92]' },
    { label: 'Offres clôturées',      value: closed,   sub: 'Au total', subColor: 'text-[#9C9A92]' },
  ]
})

const token  = () => localStorage.getItem('token')
const headers = () => ({ Authorization: `Bearer ${token()}` })

const load = async () => {
  loading.value = true
  try {
    const { data: myOffers } = await axios.get('http://localhost:8080/api/offers/my', { headers: headers() })
    offers.value = myOffers

    const appsResults = await Promise.allSettled(
      myOffers.map(o => axios.get(`http://localhost:8080/api/offers/${o.id}/applications`, { headers: headers() }))
    )
    const apps = []
    appsResults.forEach(r => { if (r.status === 'fulfilled') apps.push(...r.value.data) })
    allApplications.value = apps
  } catch { /* silencieux */ } finally {
    loading.value = false
  }
}

const timeAgo = (iso) => {
  const diff = Date.now() - new Date(iso).getTime()
  const m = Math.floor(diff / 60000)
  if (m < 1) return "À l'instant"
  if (m < 60) return `Il y a ${m} min`
  const h = Math.floor(m / 60)
  if (h < 24) return `Il y a ${h}h`
  const d = Math.floor(h / 24)
  return `Il y a ${d}j`
}

const statusStyle = (s) => ({
  PENDING:   { label: 'En attente', text: 'text-amber-600' },
  ACCEPTED:  { label: 'Acceptée',   text: 'text-green-600' },
  REJECTED:  { label: 'Refusée',    text: 'text-red-400'   },
  WITHDRAWN: { label: 'Retirée',    text: 'text-[#9C9A92]' },
  COMPLETED: { label: 'Terminée',   text: 'text-[#5F5E5A]' },
}[s] || { label: s, text: 'text-[#9C9A92]' })

onMounted(load)
</script>
