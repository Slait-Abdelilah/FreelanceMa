<template>
  <div class="space-y-5">

    <!-- HEADER -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-[20px] font-semibold text-ink tracking-tight">Mes offres</h1>
        <p class="text-[13px] text-[#9C9A92] mt-0.5">
          {{ offers.length }} offre{{ offers.length !== 1 ? 's' : '' }} publiée{{ offers.length !== 1 ? 's' : '' }}
        </p>
      </div>
      <button @click="openForm(null)"
              class="flex items-center gap-1.5 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-medium px-4 py-2 rounded-lg transition">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
        </svg>
        Nouvelle offre
      </button>
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
      <div v-for="i in 4" :key="i" class="bg-white border border-[#EBEBE5] rounded-xl p-5 animate-pulse">
        <div class="flex items-start justify-between gap-4">
          <div class="space-y-2 flex-1">
            <div class="h-4 bg-[#F4F4ED] rounded w-2/5"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-3/4"></div>
          </div>
          <div class="h-6 bg-[#F4F4ED] rounded-full w-20"></div>
        </div>
        <div class="flex gap-4 mt-4">
          <div class="h-3 bg-[#F4F4ED] rounded w-24"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-20"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-16"></div>
        </div>
      </div>
    </div>

    <!-- LISTE -->
    <div v-else-if="filteredOffers.length > 0" class="space-y-3">
      <div v-for="offer in filteredOffers" :key="offer.id"
           class="bg-white border border-[#EBEBE5] rounded-xl p-5 hover:border-[#D3D1C7] transition group">

        <div class="flex items-start justify-between gap-4">
          <div class="flex-1 min-w-0">
            <div class="flex items-center gap-2 flex-wrap">
              <h3 class="text-[14px] font-semibold text-ink">{{ offer.title }}</h3>
              <span v-if="offer.category" class="text-[10px] font-medium text-[#73726C] bg-[#F4F4ED] border border-[#EBEBE5] px-2 py-0.5 rounded-full">
                {{ categoryLabel(offer.category) }}
              </span>
            </div>
            <p class="text-[12px] text-[#73726C] mt-1.5 line-clamp-2">{{ offer.description }}</p>
          </div>
          <span class="text-[11px] font-semibold px-2.5 py-1 rounded-full flex-shrink-0 border"
                :class="offer.status === 'OPEN'
                  ? 'bg-green-50 text-green-600 border-green-100'
                  : 'bg-[#F4F4ED] text-[#9C9A92] border-[#EBEBE5]'">
            {{ offer.status === 'OPEN' ? 'Ouverte' : 'Clôturée' }}
          </span>
        </div>

        <!-- META -->
        <div class="flex flex-wrap items-center gap-x-4 gap-y-1 mt-3 text-[11px] text-[#9C9A92]">
          <span class="flex items-center gap-1">
            <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z"/>
            </svg>
            {{ offer.applicationsCount }} candidature{{ offer.applicationsCount !== 1 ? 's' : '' }}
          </span>
          <span v-if="offer.budgetMin || offer.budgetMax" class="flex items-center gap-1">
            <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33"/>
            </svg>
            {{ budgetLabel(offer) }}
          </span>
          <span v-if="offer.deadline" class="flex items-center gap-1">
            <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 9v7.5"/>
            </svg>
            Deadline : {{ formatDate(offer.deadline) }}
          </span>
          <span>Publiée {{ timeAgo(offer.createdAt) }}</span>
        </div>

        <!-- ACTIONS -->
        <div class="flex items-center gap-2 mt-4 pt-3.5 border-t border-[#EBEBE5]">
          <RouterLink :to="`/client/applications?offer=${offer.id}`"
                      class="text-[12px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-3 py-1.5 rounded-md transition">
            Voir candidatures
          </RouterLink>
          <button v-if="offer.status === 'OPEN'" @click="openForm(offer)"
                  class="text-[12px] font-medium text-[#5F5E5A] hover:text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-3 py-1.5 rounded-md transition">
            Modifier
          </button>
          <button v-if="offer.status === 'OPEN'" @click="confirmClose(offer)"
                  class="text-[12px] font-medium text-[#5F5E5A] hover:text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-3 py-1.5 rounded-md transition">
            Clôturer
          </button>
          <button @click="confirmDelete(offer)"
                  class="text-[12px] font-medium text-red-500 hover:text-red-600 border border-[#EBEBE5] hover:border-red-200 px-3 py-1.5 rounded-md transition ml-auto">
            Supprimer
          </button>
        </div>
      </div>
    </div>

    <!-- EMPTY -->
    <div v-else class="bg-white border border-[#EBEBE5] rounded-xl p-12 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
          <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">
        {{ activeTab === 'all' ? 'Aucune offre publiée' : `Aucune offre ${activeTab === 'open' ? 'ouverte' : 'clôturée'}` }}
      </p>
      <p class="text-[12px] text-[#9C9A92] mt-1">Publiez votre première offre pour trouver des talents</p>
      <button @click="openForm(null)"
              class="mt-4 text-[13px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
        Créer une offre
      </button>
    </div>

    <!-- ============ PANEL FORM ============ -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0"
                leave-active-class="transition duration-150" leave-to-class="opacity-0">
      <div v-if="formOpen" class="fixed inset-0 bg-ink/30 z-40 flex items-start justify-end" @click.self="formOpen = false">
        <div class="w-full max-w-lg bg-white h-full shadow-2xl flex flex-col overflow-hidden"
             :class="{ 'translate-x-0': formOpen }" @click.stop>

          <!-- form header -->
          <div class="px-6 py-5 border-b border-[#EBEBE5] flex items-center justify-between flex-shrink-0">
            <p class="text-[15px] font-semibold text-ink">{{ editingOffer ? 'Modifier l\'offre' : 'Nouvelle offre' }}</p>
            <button @click="formOpen = false" class="p-1.5 hover:bg-[#F4F4ED] rounded-md transition">
              <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
          </div>

          <!-- form body -->
          <div class="flex-1 overflow-y-auto px-6 py-5 space-y-4">

            <div>
              <label class="text-xs text-[#9C9A92] block mb-1.5">Titre de l'offre *</label>
              <input v-model="form.title" type="text" placeholder="Ex: Développeur Vue.js senior"
                     class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
            </div>

            <div>
              <label class="text-xs text-[#9C9A92] block mb-1.5">Description *</label>
              <textarea v-model="form.description" rows="4" placeholder="Décrivez la mission, les livrables attendus..."
                        class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition resize-none"></textarea>
            </div>

            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="text-xs text-[#9C9A92] block mb-1.5">Catégorie</label>
                <select v-model="form.category"
                        class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition bg-white">
                  <option value="">Sélectionner</option>
                  <option v-for="cat in categories" :key="cat.value" :value="cat.value">{{ cat.label }}</option>
                </select>
              </div>
              <div>
                <label class="text-xs text-[#9C9A92] block mb-1.5">Type de budget</label>
                <select v-model="form.budgetType"
                        class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition bg-white">
                  <option value="FIXED">Fixe</option>
                  <option value="HOURLY">Horaire</option>
                </select>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="text-xs text-[#9C9A92] block mb-1.5">Budget min (DH)</label>
                <input v-model.number="form.budgetMin" type="number" min="0" placeholder="500"
                       class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
              </div>
              <div>
                <label class="text-xs text-[#9C9A92] block mb-1.5">Budget max (DH)</label>
                <input v-model.number="form.budgetMax" type="number" min="0" placeholder="2000"
                       class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
              </div>
            </div>

            <div>
              <label class="text-xs text-[#9C9A92] block mb-1.5">Date limite</label>
              <input v-model="form.deadline" type="date"
                     class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
            </div>

            <div>
              <label class="text-xs text-[#9C9A92] block mb-1.5">Compétences requises</label>
              <input v-model="form.requiredSkills" type="text" placeholder="Vue.js, Spring Boot, MySQL..."
                     class="w-full border border-[#EBEBE5] rounded-lg px-3 py-2.5 text-[13px] text-ink focus:outline-none focus:border-[#5F5E5A] transition"/>
              <p class="text-[11px] text-[#9C9A92] mt-1">Séparées par des virgules</p>
            </div>

          </div>

          <!-- form footer -->
          <div class="px-6 py-4 border-t border-[#EBEBE5] flex items-center gap-3 flex-shrink-0">
            <button @click="formOpen = false"
                    class="flex-1 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] py-2.5 rounded-lg transition">
              Annuler
            </button>
            <button @click="submitForm" :disabled="formLoading"
                    class="flex-1 text-[13px] font-medium bg-ink hover:bg-[#1A1A18] text-white py-2.5 rounded-lg transition disabled:opacity-50">
              {{ formLoading ? 'Enregistrement...' : (editingOffer ? 'Enregistrer' : 'Publier') }}
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- MODAL CLÔTURER -->
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="closeTarget" class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4" @click.self="closeTarget = null">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl p-6 max-w-sm w-full" @click.stop>
          <p class="text-[15px] font-semibold text-ink">Clôturer cette offre ?</p>
          <p class="text-[13px] text-[#73726C] mt-2">
            L'offre « {{ closeTarget?.title }} » ne recevra plus de candidatures. Cette action est irréversible.
          </p>
          <div class="flex gap-3 mt-5">
            <button @click="closeTarget = null"
                    class="flex-1 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] py-2 rounded-lg transition">
              Annuler
            </button>
            <button @click="doClose" :disabled="actionLoading"
                    class="flex-1 text-[13px] font-medium bg-ink text-white py-2 rounded-lg hover:bg-[#1A1A18] transition disabled:opacity-50">
              {{ actionLoading ? '...' : 'Clôturer' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- MODAL SUPPRIMER -->
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="deleteTarget" class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4" @click.self="deleteTarget = null">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl p-6 max-w-sm w-full" @click.stop>
          <p class="text-[15px] font-semibold text-ink">Supprimer cette offre ?</p>
          <p class="text-[13px] text-[#73726C] mt-2">
            Cette action est définitive. Les offres ayant des candidatures ne peuvent pas être supprimées.
          </p>
          <div class="flex gap-3 mt-5">
            <button @click="deleteTarget = null"
                    class="flex-1 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] py-2 rounded-lg transition">
              Annuler
            </button>
            <button @click="doDelete" :disabled="actionLoading"
                    class="flex-1 text-[13px] font-medium bg-red-600 text-white py-2 rounded-lg hover:bg-red-700 transition disabled:opacity-50">
              {{ actionLoading ? '...' : 'Supprimer' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- TOAST -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast"
           class="fixed bottom-5 left-1/2 -translate-x-1/2 bg-ink text-white text-[13px] font-medium px-4 py-2.5 rounded-xl shadow-lg z-50">
        {{ toast }}
      </div>
    </Transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const offers      = ref([])
const loading     = ref(true)
const formOpen    = ref(false)
const formLoading = ref(false)
const actionLoading = ref(false)
const editingOffer  = ref(null)
const closeTarget   = ref(null)
const deleteTarget  = ref(null)
const toast         = ref('')
const activeTab     = ref('all')

const form = ref({ title: '', description: '', category: '', budgetType: 'FIXED', budgetMin: null, budgetMax: null, deadline: '', requiredSkills: '' })

const tabs = computed(() => [
  { value: 'all',    label: 'Toutes',    count: offers.value.length },
  { value: 'open',   label: 'Ouvertes',  count: offers.value.filter(o => o.status === 'OPEN').length },
  { value: 'closed', label: 'Clôturées', count: offers.value.filter(o => o.status === 'CLOSED').length },
])

const filteredOffers = computed(() => {
  if (activeTab.value === 'open')   return offers.value.filter(o => o.status === 'OPEN')
  if (activeTab.value === 'closed') return offers.value.filter(o => o.status === 'CLOSED')
  return offers.value
})

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

const token   = () => localStorage.getItem('token')
const headers = () => ({ Authorization: `Bearer ${token()}` })

const load = async () => {
  loading.value = true
  try {
    const { data } = await axios.get('http://localhost:8080/api/offers/my', { headers: headers() })
    offers.value = data
  } finally { loading.value = false }
}

const openForm = (offer) => {
  editingOffer.value = offer
  if (offer) {
    form.value = {
      title: offer.title, description: offer.description,
      category: offer.category || '', budgetType: offer.budgetType || 'FIXED',
      budgetMin: offer.budgetMin, budgetMax: offer.budgetMax,
      deadline: offer.deadline || '', requiredSkills: offer.requiredSkills || ''
    }
  } else {
    form.value = { title: '', description: '', category: '', budgetType: 'FIXED', budgetMin: null, budgetMax: null, deadline: '', requiredSkills: '' }
  }
  formOpen.value = true
}

const submitForm = async () => {
  if (!form.value.title.trim() || !form.value.description.trim()) {
    showToast('Titre et description sont obligatoires')
    return
  }
  formLoading.value = true
  try {
    const payload = { ...form.value }
    if (!payload.budgetMin) delete payload.budgetMin
    if (!payload.budgetMax) delete payload.budgetMax
    if (!payload.deadline)  delete payload.deadline
    if (!payload.category)  delete payload.category

    if (editingOffer.value) {
      const { data } = await axios.put(`http://localhost:8080/api/offers/${editingOffer.value.id}`, payload, { headers: headers() })
      const idx = offers.value.findIndex(o => o.id === data.id)
      if (idx !== -1) offers.value[idx] = data
      showToast('Offre mise à jour')
    } else {
      const { data } = await axios.post('http://localhost:8080/api/offers', payload, { headers: headers() })
      offers.value.unshift(data)
      showToast('Offre publiée avec succès')
    }
    formOpen.value = false
  } catch (e) {
    showToast(e.response?.data?.message || 'Erreur lors de l\'enregistrement')
  } finally { formLoading.value = false }
}

const confirmClose  = (offer) => { closeTarget.value = offer }
const confirmDelete = (offer) => { deleteTarget.value = offer }

const doClose = async () => {
  actionLoading.value = true
  try {
    const { data } = await axios.patch(`http://localhost:8080/api/offers/${closeTarget.value.id}/close`, {}, { headers: headers() })
    const idx = offers.value.findIndex(o => o.id === data.id)
    if (idx !== -1) offers.value[idx] = data
    showToast('Offre clôturée')
    closeTarget.value = null
  } catch (e) { showToast(e.response?.data?.message || 'Erreur')
  } finally { actionLoading.value = false }
}

const doDelete = async () => {
  actionLoading.value = true
  try {
    await axios.delete(`http://localhost:8080/api/offers/${deleteTarget.value.id}`, { headers: headers() })
    offers.value = offers.value.filter(o => o.id !== deleteTarget.value.id)
    showToast('Offre supprimée')
    deleteTarget.value = null
  } catch (e) { showToast(e.response?.data?.message || 'Impossible de supprimer cette offre')
  } finally { actionLoading.value = false }
}

const categoryLabel = (v) => categories.find(c => c.value === v)?.label || v
const budgetLabel   = (o) => {
  if (o.budgetMin && o.budgetMax) return `${o.budgetMin} – ${o.budgetMax} DH`
  if (o.budgetMin) return `Dès ${o.budgetMin} DH`
  if (o.budgetMax) return `Jusqu'à ${o.budgetMax} DH`
  return ''
}
const formatDate = (d) => d ? new Date(d).toLocaleDateString('fr-FR') : ''
const timeAgo = (iso) => {
  const diff = Date.now() - new Date(iso).getTime()
  const d = Math.floor(diff / 86400000)
  if (d === 0) return "aujourd'hui"
  if (d === 1) return "hier"
  return `il y a ${d}j`
}

let toastTimer = null
const showToast = (msg) => {
  toast.value = msg
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => { toast.value = '' }, 3000)
}

onMounted(async () => {
  await load()
  if (route.query.new === 'true') openForm(null)
})

watch(() => route.query.new, (val) => {
  if (val === 'true') openForm(null)
})
</script>
