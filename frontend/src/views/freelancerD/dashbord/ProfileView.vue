<template>
  <div class="max-w-3xl mx-auto pb-8">

    <!-- ─── HEADER ──────────────────────────────────────────── -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-xl font-bold text-ink">Profil</h1>
        <p class="text-sm text-[#9C9A92] mt-0.5">Votre vitrine visible par les clients</p>
      </div>
    </div>

    <!-- ─── SQUELETTE ────────────────────────────────────────── -->
    <div v-if="loading" class="space-y-4">
      <div v-for="i in 4" :key="i"
           class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden animate-pulse">
        <div class="px-5 py-4 border-b border-[#EBEBE5]">
          <div class="h-3 bg-[#F4F4ED] rounded w-32"></div>
        </div>
        <div class="p-5 space-y-3">
          <div class="h-3 bg-[#F4F4ED] rounded w-full"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-3/4"></div>
          <div class="h-8 bg-[#F4F4ED] rounded w-full"></div>
        </div>
      </div>
    </div>

    <div v-else class="space-y-4">

      <!-- ─── IDENTITÉ ─────────────────────────────────────── -->
      <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">

        <div class="flex items-center justify-between px-5 py-4 border-b border-[#EBEBE5]">
          <p class="text-sm font-semibold text-ink">Identité</p>
          <span class="text-xs text-[#73726C] border border-[#EBEBE5] px-2.5 py-1 rounded-full">
            {{ profile.role === 'FREELANCER' ? 'Freelancer' : 'Client' }}
          </span>
        </div>

        <div class="p-5">

          <!-- avatar + nom + stats -->
          <div class="flex items-center gap-4 mb-5 pb-5 border-b border-[#EBEBE5]">
            <div class="w-12 h-12 rounded-xl bg-[#F4F4ED] border border-[#EBEBE5] flex items-center justify-center text-base font-bold text-ink flex-shrink-0">
              {{ userInitials }}
            </div>
            <div>
              <p class="text-sm font-bold text-ink">
                {{ profile.firstName || '—' }} {{ profile.lastName }}
              </p>
              <p class="text-xs text-[#9C9A92] mt-0.5">{{ profile.email }}</p>
              <div class="flex items-center gap-3 mt-1.5 text-xs text-[#9C9A92]">
                <span v-if="profile.averageRating">
                  {{ profile.averageRating.toFixed(1) }} ★ · {{ profile.totalReviews }} avis
                </span>
                <span v-if="profile.completedMissions">
                  {{ profile.completedMissions }} missions complétées
                </span>
              </div>
            </div>
          </div>

          <!-- champs -->
          <div class="space-y-4">

            <div>
              <label class="block text-xs text-[#9C9A92] mb-1.5">Titre professionnel</label>
              <input v-model="form.title" type="text"
                     placeholder="Ex : Développeur Full Stack Vue.js + Spring Boot"
                     class="w-full border border-[#EBEBE5] focus:border-[#C4C3BC] rounded-lg px-3 py-2.5 text-sm outline-none transition placeholder:text-[#C4C3BC]"/>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

              <div>
                <label class="block text-xs text-[#9C9A92] mb-1.5">Localisation</label>
                <input v-model="form.location" type="text"
                       placeholder="Ex : Fès, Maroc"
                       class="w-full border border-[#EBEBE5] focus:border-[#C4C3BC] rounded-lg px-3 py-2.5 text-sm outline-none transition placeholder:text-[#C4C3BC]"/>
              </div>

              <div>
                <label class="block text-xs text-[#9C9A92] mb-1.5">Disponibilité</label>
                <div class="flex items-center gap-3 h-10">
                  <button @click="form.isAvailable = !form.isAvailable"
                          class="relative w-9 h-5 rounded-full transition-colors flex-shrink-0"
                          :class="form.isAvailable ? 'bg-brand-500' : 'bg-[#D3D1C7]'">
                    <span class="absolute top-0.5 w-4 h-4 bg-white rounded-full shadow-sm transition-all"
                          :class="form.isAvailable ? 'left-4' : 'left-0.5'"></span>
                  </button>
                  <span class="text-sm" :class="form.isAvailable ? 'text-ink' : 'text-[#9C9A92]'">
                    {{ form.isAvailable ? 'Disponible' : 'Indisponible' }}
                  </span>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>

      <!-- ─── BIO ──────────────────────────────────────────── -->
      <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
        <div class="px-5 py-4 border-b border-[#EBEBE5]">
          <p class="text-sm font-semibold text-ink">À propos</p>
        </div>
        <div class="p-5">
          <label class="block text-xs text-[#9C9A92] mb-1.5">Bio</label>
          <textarea v-model="form.bio" rows="4"
                    placeholder="Décrivez votre expérience, vos spécialités et ce que vous apportez à vos clients..."
                    class="w-full border border-[#EBEBE5] focus:border-[#C4C3BC] rounded-lg px-3 py-2.5 text-sm outline-none transition resize-none placeholder:text-[#C4C3BC]">
          </textarea>
          <p class="text-xs mt-1 text-right"
             :class="(form.bio?.length || 0) > 500 ? 'text-red-500' : 'text-[#C4C3BC]'">
            {{ form.bio?.length || 0 }} / 500
          </p>
        </div>
      </div>

      <!-- ─── COMPÉTENCES & TARIF ──────────────────────────── -->
      <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
        <div class="px-5 py-4 border-b border-[#EBEBE5]">
          <p class="text-sm font-semibold text-ink">Compétences & tarif</p>
        </div>
        <div class="p-5 space-y-5">

          <div>
            <label class="block text-xs text-[#9C9A92] mb-1.5">Compétences</label>
            <input v-model="form.skills" type="text"
                   placeholder="Vue.js, Spring Boot, MariaDB, Docker..."
                   class="w-full border border-[#EBEBE5] focus:border-[#C4C3BC] rounded-lg px-3 py-2.5 text-sm outline-none transition placeholder:text-[#C4C3BC]"/>
            <p class="text-xs text-[#C4C3BC] mt-1">Séparées par des virgules</p>
            <div v-if="skillTags.length" class="flex flex-wrap gap-1.5 mt-3">
              <span v-for="skill in skillTags" :key="skill"
                    class="text-xs border border-[#EBEBE5] text-[#5F5E5A] px-2.5 py-1 rounded-md">
                {{ skill }}
              </span>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

            <div>
              <label class="block text-xs text-[#9C9A92] mb-1.5">Tarif horaire</label>
              <div class="relative">
                <input v-model="form.hourlyRate" type="number" min="50" max="2000"
                       placeholder="150"
                       class="w-full border border-[#EBEBE5] focus:border-[#C4C3BC] rounded-lg px-3 py-2.5 text-sm outline-none transition pr-12 placeholder:text-[#C4C3BC]"/>
                <span class="absolute right-3 top-1/2 -translate-y-1/2 text-xs text-[#9C9A92]">DH/h</span>
              </div>
            </div>

            <div>
              <label class="block text-xs text-[#9C9A92] mb-1.5">Niveau</label>
              <div class="grid grid-cols-3 gap-1.5">
                <button v-for="level in experienceLevels" :key="level.value"
                        @click="form.experienceLevel = level.value"
                        class="py-2 rounded-lg text-xs font-medium border transition"
                        :class="form.experienceLevel === level.value
                          ? 'bg-ink text-white border-ink'
                          : 'border-[#EBEBE5] text-[#73726C] hover:border-[#C4C3BC]'">
                  {{ level.label }}
                </button>
              </div>
              <p class="text-xs text-[#9C9A92] mt-1.5">
                {{ experienceLevels.find(l => l.value === form.experienceLevel)?.desc }}
              </p>
            </div>

          </div>
        </div>
      </div>

      <!-- ─── LIENS ─────────────────────────────────────────── -->
      <div class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">
        <div class="px-5 py-4 border-b border-[#EBEBE5]">
          <p class="text-sm font-semibold text-ink">Liens</p>
        </div>
        <div class="divide-y divide-[#EBEBE5]">

          <div class="flex items-center gap-0 px-5 py-3">
            <span class="text-xs text-[#9C9A92] w-28 flex-shrink-0">Portfolio</span>
            <input v-model="form.portfolioUrl" type="url"
                   placeholder="https://monportfolio.ma"
                   class="flex-1 text-sm outline-none placeholder:text-[#C4C3BC] bg-transparent"/>
          </div>

          <div class="flex items-center gap-0 px-5 py-3">
            <span class="text-xs text-[#9C9A92] w-28 flex-shrink-0">GitHub</span>
            <span class="text-xs text-[#C4C3BC] mr-1">github.com/</span>
            <input v-model="form.githubUrl" type="text"
                   placeholder="votre-username"
                   class="flex-1 text-sm outline-none placeholder:text-[#C4C3BC] bg-transparent"/>
          </div>

          <div class="flex items-center gap-0 px-5 py-3">
            <span class="text-xs text-[#9C9A92] w-28 flex-shrink-0">LinkedIn</span>
            <span class="text-xs text-[#C4C3BC] mr-1">linkedin.com/in/</span>
            <input v-model="form.linkedinUrl" type="text"
                   placeholder="votre-username"
                   class="flex-1 text-sm outline-none placeholder:text-[#C4C3BC] bg-transparent"/>
          </div>

        </div>
      </div>

      <!-- ─── BARRE SAUVEGARDE ──────────────────────────────── -->
      <div class="flex items-center justify-between bg-white border border-[#EBEBE5] rounded-xl px-5 py-4">
        <p v-if="lastSaved" class="text-xs text-[#9C9A92]">
          Enregistré à {{ lastSaved }}
        </p>
        <div v-else></div>
        <button @click="saveProfile" :disabled="saving"
                class="text-sm font-semibold bg-ink hover:bg-[#1A1A18] text-white px-5 py-2 rounded-lg transition disabled:opacity-40">
          {{ saving ? 'Enregistrement...' : 'Enregistrer' }}
        </button>
      </div>

    </div>

    <!-- ─── TOAST ────────────────────────────────────────────── -->
    <Transition enter-from-class="opacity-0 translate-y-2"
                enter-active-class="transition duration-200"
                leave-to-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150">
      <div v-if="toast.show"
           class="fixed bottom-6 right-6 z-50 flex items-center gap-2.5 px-4 py-3 rounded-xl text-sm font-medium shadow-lg"
           :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
        <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5"
                :d="toast.type === 'success' ? 'M5 13l4 4L19 7' : 'M6 18L18 6M6 6l12 12'"/>
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

const loading   = ref(true)
const saving    = ref(false)
const lastSaved = ref('')
const toast     = ref({ show: false, message: '', type: 'success' })

const profile = ref({
  id: null, email: '', firstName: '', lastName: '', role: '',
  averageRating: 0, totalReviews: 0, completedMissions: 0,
})

const form = ref({
  title: '', bio: '', location: '', skills: '',
  hourlyRate: null, experienceLevel: 'JUNIOR', isAvailable: true,
  portfolioUrl: '', githubUrl: '', linkedinUrl: '',
})

const experienceLevels = [
  { value: 'JUNIOR', label: 'Junior',    desc: '0 – 2 ans' },
  { value: 'MID',    label: 'Confirmé',  desc: '3 – 5 ans' },
  { value: 'SENIOR', label: 'Senior',    desc: '5 + ans' },
]

const userInitials = computed(() => {
  const f = profile.value.firstName?.[0] || ''
  const l = profile.value.lastName?.[0] || ''
  return (f + l).toUpperCase() || 'FL'
})

const skillTags = computed(() =>
  (form.value.skills || '').split(',').map(s => s.trim()).filter(Boolean)
)

const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token') || sessionStorage.getItem('token')}` } })

const loadProfile = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/profile`, headers())
    profile.value = {
      id: data.id, email: data.email || '', firstName: data.firstName || '',
      lastName: data.lastName || '', role: data.role || '',
      averageRating: data.averageRating || 0, totalReviews: data.totalReviews || 0,
      completedMissions: data.completedMissions || 0,
    }
    form.value = {
      title: data.title || '', bio: data.bio || '', location: data.location || '',
      skills: data.skills || '', hourlyRate: data.hourlyRate || null,
      experienceLevel: data.experienceLevel || 'JUNIOR',
      isAvailable: data.isAvailable ?? true,
      portfolioUrl: data.portfolioUrl || '', githubUrl: data.githubUrl || '',
      linkedinUrl: data.linkedinUrl || '',
    }
  } catch {
    showToast('Impossible de charger le profil', 'error')
  } finally {
    loading.value = false
  }
}

const saveProfile = async () => {
  saving.value = true
  try {
    const { data } = await axios.put(
      `${API_URL}/api/profile`,
      {
        title:           form.value.title || null,
        bio:             form.value.bio || null,
        location:        form.value.location || null,
        skills:          form.value.skills || null,
        hourlyRate:      form.value.hourlyRate || null,
        experienceLevel: form.value.experienceLevel,
        isAvailable:     form.value.isAvailable,
        portfolioUrl:    form.value.portfolioUrl || null,
        githubUrl:       form.value.githubUrl || null,
        linkedinUrl:     form.value.linkedinUrl || null,
      },
      headers()
    )
    profile.value.averageRating    = data.averageRating
    profile.value.totalReviews     = data.totalReviews
    profile.value.completedMissions = data.completedMissions
    lastSaved.value = new Date().toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
    showToast('Profil enregistré')
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la sauvegarde', 'error')
  } finally {
    saving.value = false
  }
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 3500)
}

onMounted(loadProfile)
</script>
