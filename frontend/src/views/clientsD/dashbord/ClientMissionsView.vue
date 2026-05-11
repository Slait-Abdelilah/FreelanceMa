<template>
  <div class="space-y-5">

    <!-- HEADER -->
    <div>
      <h1 class="text-[20px] font-semibold text-ink tracking-tight">Missions actives</h1>
      <p class="text-[13px] text-[#9C9A92] mt-0.5">Suivez vos missions en cours et terminées</p>
    </div>

    <!-- TABS -->
    <div class="flex gap-1 bg-[#F4F4ED] p-1 rounded-lg w-fit">
      <button v-for="tab in tabs" :key="tab.value" @click="activeTab = tab.value"
              class="text-[12px] font-medium px-3 py-1 rounded-md transition"
              :class="activeTab === tab.value ? 'bg-white text-ink shadow-sm' : 'text-[#73726C] hover:text-ink'">
        {{ tab.label }}
        <span v-if="tab.count !== null" class="ml-1 text-[10px] text-[#9C9A92]">({{ tab.count }})</span>
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
            <div class="h-3 bg-[#F4F4ED] rounded w-1/3"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- MISSIONS LIST -->
    <div v-else-if="filteredMissions.length > 0" class="space-y-3">
      <div v-for="m in filteredMissions" :key="m.id"
           class="bg-white border border-[#EBEBE5] rounded-xl p-5 hover:border-[#D3D1C7] transition">

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
              <span class="text-[11px] font-semibold px-2.5 py-1 rounded-full border flex-shrink-0"
                    :class="m.status === 'ACCEPTED'
                      ? 'bg-green-50 text-green-600 border-green-100'
                      : 'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]'">
                {{ m.status === 'ACCEPTED' ? 'En cours' : 'Terminée' }}
              </span>
            </div>

            <!-- meta -->
            <div class="flex flex-wrap items-center gap-x-4 gap-y-1 mt-3 text-[11px] text-[#9C9A92]">
              <span v-if="m.proposedBudget" class="flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33"/>
                </svg>
                Budget : <strong class="text-ink ml-0.5">{{ m.proposedBudget }} DH</strong>
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
            </div>

            <!-- category badge -->
            <div v-if="m.offerCategory" class="mt-2.5">
              <span class="text-[10px] font-medium text-[#73726C] bg-[#F4F4ED] border border-[#EBEBE5] px-2 py-0.5 rounded-full">
                {{ categoryLabel(m.offerCategory) }}
              </span>
            </div>

            <!-- link to applications -->
            <div class="mt-3 pt-3 border-t border-[#EBEBE5]">
              <RouterLink :to="`/client/applications?offer=${m.offerId}`"
                          class="text-[12px] font-medium text-[#5F5E5A] hover:text-ink transition">
                Voir toutes les candidatures →
              </RouterLink>
            </div>
          </div>
        </div>

      </div>
    </div>

    <!-- EMPTY -->
    <div v-else class="bg-white border border-[#EBEBE5] rounded-xl p-12 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">
        {{ activeTab === 'active' ? 'Aucune mission en cours' : 'Aucune mission terminée' }}
      </p>
      <p class="text-[12px] text-[#9C9A92] mt-1">
        {{ activeTab === 'active'
          ? 'Acceptez des candidatures pour démarrer des missions'
          : 'Les missions terminées par les freelancers apparaîtront ici' }}
      </p>
      <RouterLink v-if="activeTab === 'active'" to="/client/applications"
                  class="mt-4 inline-block text-[13px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
        Voir les candidatures
      </RouterLink>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const loading   = ref(true)
const missions  = ref([])
const activeTab = ref('active')

const tabs = computed(() => [
  { value: 'active',    label: 'En cours',  count: missions.value.filter(m => m.status === 'ACCEPTED').length },
  { value: 'completed', label: 'Terminées', count: missions.value.filter(m => m.status === 'COMPLETED').length },
])

const filteredMissions = computed(() => {
  if (activeTab.value === 'active')    return missions.value.filter(m => m.status === 'ACCEPTED')
  if (activeTab.value === 'completed') return missions.value.filter(m => m.status === 'COMPLETED')
  return missions.value
})

const token   = () => localStorage.getItem('token')
const headers = () => ({ Authorization: `Bearer ${token()}` })

const load = async () => {
  loading.value = true
  try {
    const { data: offers } = await axios.get('http://localhost:8080/api/offers/my', { headers: headers() })
    const results = await Promise.allSettled(
      offers.map(o =>
        axios.get(`http://localhost:8080/api/offers/${o.id}/applications`, { headers: headers() })
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
      .filter(a => a.status === 'ACCEPTED' || a.status === 'COMPLETED')
      .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  } catch { /* silencieux */ } finally { loading.value = false }
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

onMounted(load)
</script>
