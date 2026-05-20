<template>
  <div class="space-y-6 pb-8">

    <!-- HEADER -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-[20px] font-semibold text-ink tracking-tight">Mon profil</h1>
        <p class="text-[13px] text-[#9C9A92] mt-0.5">Votre vitrine visible par les clients</p>
      </div>
      <button @click="saveProfile" :disabled="saving"
              class="flex items-center gap-2 text-[13px] font-semibold bg-ink hover:bg-[#1A1A18] text-white px-5 py-2.5 rounded-xl transition disabled:opacity-40">
        <svg v-if="saving" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
        </svg>
        <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
        </svg>
        {{ saving ? 'Enregistrement…' : 'Enregistrer' }}
      </button>
    </div>

    <!-- SKELETON -->
    <div v-if="loading" class="grid grid-cols-1 lg:grid-cols-3 gap-5">
      <div class="lg:col-span-1 space-y-4">
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 animate-pulse">
          <div class="flex flex-col items-center text-center gap-3">
            <div class="w-24 h-24 rounded-2xl bg-[#F4F4ED]"></div>
            <div class="h-4 bg-[#F4F4ED] rounded w-32"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-24"></div>
          </div>
        </div>
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 animate-pulse space-y-3">
          <div class="h-3 bg-[#F4F4ED] rounded w-16"></div>
          <div class="h-10 bg-[#F4F4ED] rounded-xl"></div>
          <div class="h-10 bg-[#F4F4ED] rounded-xl"></div>
          <div class="h-10 bg-[#F4F4ED] rounded-xl"></div>
        </div>
      </div>
      <div class="lg:col-span-2 space-y-4">
        <div v-for="i in 4" :key="i" class="bg-white border border-[#EBEBE5] rounded-2xl p-6 animate-pulse space-y-3">
          <div class="h-3 bg-[#F4F4ED] rounded w-28"></div>
          <div class="h-10 bg-[#F4F4ED] rounded-xl"></div>
          <div class="h-10 bg-[#F4F4ED] rounded-xl"></div>
        </div>
      </div>
    </div>

    <!-- CONTENU -->
    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-5">

      <!-- ══ COLONNE GAUCHE ══ -->
      <div class="lg:col-span-1 space-y-4">

        <!-- Carte identité -->
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 flex flex-col items-center text-center">

          <!-- Avatar -->
          <div class="w-24 h-24 rounded-2xl bg-ink flex items-center justify-center mb-4">
            <span class="text-[30px] font-bold text-white tracking-tight">{{ userInitials }}</span>
          </div>

          <p class="text-[18px] font-bold text-ink leading-tight">
            {{ profile.firstName || '—' }} {{ profile.lastName }}
          </p>
          <p class="text-[13px] text-[#9C9A92] mt-1">{{ profile.email }}</p>

          <!-- Stats -->
          <div class="flex flex-wrap justify-center gap-2 mt-4">
            <span v-if="profile.averageRating"
                  class="flex items-center gap-1 text-[12px] font-medium text-[#5F5E5A] bg-[#FAFAF7] border border-[#EBEBE5] px-2.5 py-1 rounded-full">
              <svg class="w-3.5 h-3.5 text-amber-400" fill="currentColor" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
              </svg>
              {{ profile.averageRating.toFixed(1) }} · {{ profile.totalReviews }} avis
            </span>
            <span v-if="profile.completedMissions"
                  class="flex items-center gap-1 text-[12px] font-medium text-[#5F5E5A] bg-[#FAFAF7] border border-[#EBEBE5] px-2.5 py-1 rounded-full">
              <svg class="w-3.5 h-3.5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              {{ profile.completedMissions }} missions
            </span>
          </div>

          <!-- Disponibilité -->
          <div class="flex items-center justify-between w-full mt-5 pt-5 border-t border-[#EBEBE5]">
            <span class="text-[13px] font-medium text-ink">Disponibilité</span>
            <div class="flex items-center gap-2">
              <span class="text-[12px]" :class="form.isAvailable ? 'text-green-600' : 'text-[#9C9A92]'">
                {{ form.isAvailable ? 'Disponible' : 'Indisponible' }}
              </span>
              <button @click="form.isAvailable = !form.isAvailable"
                      class="relative w-11 h-6 rounded-full transition-colors"
                      :class="form.isAvailable ? 'bg-green-500' : 'bg-[#D3D1C7]'">
                <span class="absolute top-1 w-4 h-4 bg-white rounded-full shadow transition-all"
                      :class="form.isAvailable ? 'left-6' : 'left-1'"></span>
              </button>
            </div>
          </div>
        </div>

        <!-- Liens -->
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 space-y-4">
          <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Liens</p>

          <div class="flex items-center gap-3 border border-[#EBEBE5] focus-within:border-[#5F5E5A] rounded-xl px-3.5 py-3 transition">
            <svg class="w-4 h-4 text-[#9C9A92] flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9"/>
            </svg>
            <input v-model="form.portfolioUrl" type="url" placeholder="Portfolio URL"
                   class="flex-1 text-[13px] text-ink outline-none bg-transparent placeholder:text-[#C4C3BC] min-w-0"/>
          </div>

          <div class="flex items-center gap-3 border border-[#EBEBE5] focus-within:border-[#5F5E5A] rounded-xl px-3.5 py-3 transition">
            <svg class="w-4 h-4 text-[#9C9A92] flex-shrink-0" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
            </svg>
            <span class="text-[12px] text-[#C4C3BC] flex-shrink-0">github.com/</span>
            <input v-model="form.githubUrl" type="text" placeholder="username"
                   class="flex-1 text-[13px] text-ink outline-none bg-transparent placeholder:text-[#C4C3BC] min-w-0"/>
          </div>

          <div class="flex items-center gap-3 border border-[#EBEBE5] focus-within:border-[#5F5E5A] rounded-xl px-3.5 py-3 transition">
            <svg class="w-4 h-4 text-[#9C9A92] flex-shrink-0" fill="currentColor" viewBox="0 0 24 24">
              <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
            </svg>
            <span class="text-[12px] text-[#C4C3BC] flex-shrink-0">linkedin.com/in/</span>
            <input v-model="form.linkedinUrl" type="text" placeholder="username"
                   class="flex-1 text-[13px] text-ink outline-none bg-transparent placeholder:text-[#C4C3BC] min-w-0"/>
          </div>
        </div>

        <!-- Dernier enregistrement -->
        <p v-if="lastSaved" class="text-center text-[12px] text-[#9C9A92]">
          Enregistré à {{ lastSaved }}
        </p>
      </div>

      <!-- ══ COLONNE DROITE ══ -->
      <div class="lg:col-span-2 space-y-4">

        <!-- Présentation -->
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 space-y-5">
          <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Présentation</p>

          <div>
            <label class="block text-[13px] font-medium text-ink mb-2">Titre professionnel</label>
            <input v-model="form.title" type="text"
                   placeholder="Ex : Développeur Full Stack Vue.js + Spring Boot"
                   class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
          </div>

          <div>
            <label class="block text-[13px] font-medium text-ink mb-2">Localisation</label>
            <div class="relative">
              <svg class="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-[#C4C3BC]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
              </svg>
              <input v-model="form.location" type="text" placeholder="Ex : Fès, Maroc"
                     class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl pl-11 pr-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
            </div>
          </div>
        </div>

        <!-- Bio -->
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6">
          <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest mb-4">À propos</p>
          <textarea v-model="form.bio" rows="5"
                    placeholder="Décrivez votre expérience, vos spécialités et ce que vous apportez à vos clients..."
                    class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition resize-none placeholder:text-[#C4C3BC]">
          </textarea>
          <p class="text-[12px] text-right mt-1.5"
             :class="(form.bio?.length || 0) > 500 ? 'text-red-500' : 'text-[#C4C3BC]'">
            {{ form.bio?.length || 0 }} / 500
          </p>
        </div>

        <!-- Compétences -->
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6">
          <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest mb-4">Compétences</p>
          <input v-model="form.skills" type="text"
                 placeholder="Vue.js, Spring Boot, MariaDB, Docker…"
                 class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
          <p class="text-[12px] text-[#C4C3BC] mt-2">Séparées par des virgules</p>
          <div v-if="skillTags.length" class="flex flex-wrap gap-2 mt-4">
            <span v-for="skill in skillTags" :key="skill"
                  class="text-[13px] font-medium bg-[#F4F4ED] text-[#5F5E5A] border border-[#EBEBE5] px-3 py-1.5 rounded-lg">
              {{ skill }}
            </span>
          </div>
        </div>

        <!-- Tarif & Niveau -->
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 space-y-5">
          <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Tarif & Expérience</p>

          <div class="grid grid-cols-2 gap-5">
            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Tarif horaire</label>
              <div class="relative">
                <input v-model="form.hourlyRate" type="number" min="50" max="2000" placeholder="150"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition pr-16 placeholder:text-[#C4C3BC]"/>
                <span class="absolute right-4 top-1/2 -translate-y-1/2 text-[13px] text-[#9C9A92] font-medium">DH/h</span>
              </div>
            </div>

            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Niveau d'expérience</label>
              <div class="flex gap-2 h-[46px]">
                <button v-for="level in experienceLevels" :key="level.value"
                        @click="form.experienceLevel = level.value"
                        class="flex-1 rounded-xl text-[12px] font-semibold border transition leading-tight"
                        :class="form.experienceLevel === level.value
                          ? 'bg-ink text-white border-ink'
                          : 'border-[#EBEBE5] text-[#73726C] hover:border-[#D3D1C7] hover:text-ink'">
                  {{ level.label }}
                  <span class="block text-[10px] font-normal opacity-70">{{ level.desc }}</span>
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Bouton bas -->
        <div class="flex items-center justify-end">
          <button @click="saveProfile" :disabled="saving"
                  class="flex items-center gap-2 text-[13px] font-semibold bg-ink hover:bg-[#1A1A18] text-white px-6 py-3 rounded-xl transition disabled:opacity-40">
            <svg v-if="saving" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ saving ? 'Enregistrement…' : 'Enregistrer les modifications' }}
          </button>
        </div>

      </div>
    </div>

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
  { value: 'JUNIOR', label: 'Junior',   desc: '0–2 ans' },
  { value: 'MID',    label: 'Confirmé', desc: '3–5 ans' },
  { value: 'SENIOR', label: 'Senior',   desc: '5+ ans' },
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
    profile.value.averageRating     = data.averageRating
    profile.value.totalReviews      = data.totalReviews
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
