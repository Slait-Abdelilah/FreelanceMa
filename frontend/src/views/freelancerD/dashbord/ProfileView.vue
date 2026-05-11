<template>
  <div class="max-w-4xl mx-auto space-y-5">

    <!-- EN-TÊTE -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-xl font-bold text-ink">Mon Profil</h1>
        <p class="text-[13px] text-[#73726C] mt-0.5">Votre vitrine publique visible par les clients</p>
      </div>
      <!-- lien vers le profil public -->
      <a v-if="profile.id"
         :href="`${API_URL}/api/profile/${profile.id}/public`"
         target="_blank"
         class="flex items-center gap-1.5 text-[12px] font-semibold text-brand-600 hover:bg-brand-50 px-3 py-1.5 rounded-lg transition">
        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"/>
        </svg>
        Voir mon profil public
      </a>
    </div>

    <!-- LOADING -->
    <div v-if="loading" class="flex justify-center py-16">
      <div class="w-6 h-6 border-2 border-brand-500 border-t-transparent rounded-full animate-spin"></div>
    </div>

    <div v-else class="space-y-5">

      <!-- ===== BLOC 1 : IDENTITÉ ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
          <h2 class="text-[14px] font-bold text-ink">Identité</h2>
          <span class="text-[11px] font-bold px-2.5 py-1 rounded-full"
                :class="profile.role === 'FREELANCER' ? 'bg-brand-50 text-brand-700' : 'bg-blue-50 text-blue-700'">
            {{ profile.role === 'FREELANCER' ? '💼 Freelancer' : '🏢 Client' }}
          </span>
        </div>
        <div class="p-6">

          <!-- avatar + infos de base -->
          <div class="flex items-start gap-5 mb-6">
            <!-- avatar -->
            <div class="w-16 h-16 bg-gradient-to-br from-brand-500 to-brand-700 rounded-xl flex items-center justify-center text-white text-xl font-bold flex-shrink-0">
              {{ userInitials }}
            </div>
            <div class="flex-1">
              <div class="text-lg font-bold text-ink">{{ profile.firstName }} {{ profile.lastName }}</div>
              <div class="text-[13px] text-[#73726C] mt-0.5">{{ profile.email }}</div>
              <div class="flex items-center gap-3 mt-2 text-[12px] text-[#73726C]">
                <span v-if="profile.averageRating" class="flex items-center gap-1">
                  <span class="text-amber-500">★</span>
                  <span class="font-semibold text-ink">{{ profile.averageRating?.toFixed(1) }}</span>
                  ({{ profile.totalReviews }} avis)
                </span>
                <span v-if="profile.completedMissions">
                  · {{ profile.completedMissions }} missions terminées
                </span>
                <span class="flex items-center gap-1">
                  <span class="w-2 h-2 rounded-full"
                        :class="form.isAvailable ? 'bg-brand-500' : 'bg-gray-400'"></span>
                  {{ form.isAvailable ? 'Disponible' : 'Non disponible' }}
                </span>
              </div>
            </div>
          </div>

          <!-- titre pro -->
          <div class="mb-4">
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              Titre professionnel
            </label>
            <input
                v-model="form.title"
                type="text"
                placeholder="Ex: Développeur Full Stack Vue.js + Spring Boot"
                class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition"
            />
          </div>

          <!-- localisation + disponibilité -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
                Localisation
              </label>
              <input
                  v-model="form.location"
                  type="text"
                  placeholder="Ex: Fès, Maroc"
                  class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition"
              />
            </div>
            <div>
              <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
                Disponibilité
              </label>
              <div class="flex items-center gap-3 pt-1">
                <button @click="form.isAvailable = !form.isAvailable"
                        class="relative w-10 h-5 rounded-full transition-colors"
                        :class="form.isAvailable ? 'bg-brand-500' : 'bg-[#D3D1C7]'">
                  <span class="absolute top-0.5 left-0.5 w-4 h-4 bg-white rounded-full shadow-sm transition-transform"
                        :class="form.isAvailable ? 'translate-x-5' : 'translate-x-0'"></span>
                </button>
                <span class="text-[13px] font-medium"
                      :class="form.isAvailable ? 'text-brand-600' : 'text-[#73726C]'">
                  {{ form.isAvailable ? 'Disponible pour de nouvelles missions' : 'Non disponible' }}
                </span>
              </div>
            </div>
          </div>

        </div>
      </div>

      <!-- ===== BLOC 2 : BIO ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5]">
          <h2 class="text-[14px] font-bold text-ink">À propos</h2>
        </div>
        <div class="p-6">
          <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
            Bio / Description
          </label>
          <textarea
              v-model="form.bio"
              rows="5"
              placeholder="Décrivez votre expérience, vos spécialités, ce que vous apportez à vos clients..."
              class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition resize-none"
          ></textarea>
          <div class="flex justify-end mt-1">
            <span class="text-[11px]"
                  :class="(form.bio?.length || 0) > 500 ? 'text-red-500' : 'text-[#9C9A92]'">
              {{ form.bio?.length || 0 }} / 500 caractères
            </span>
          </div>
        </div>
      </div>

      <!-- ===== BLOC 3 : COMPÉTENCES & TARIF ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5]">
          <h2 class="text-[14px] font-bold text-ink">Compétences & Tarif</h2>
        </div>
        <div class="p-6 space-y-5">

          <!-- compétences -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              Compétences
            </label>
            <input
                v-model="form.skills"
                type="text"
                placeholder="Ex: Vue.js, Spring Boot, MariaDB, Docker, Tailwind CSS"
                class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition"
            />
            <p class="text-[11px] text-[#9C9A92] mt-1.5">Séparez les compétences par des virgules</p>

            <!-- aperçu des tags -->
            <div v-if="skillTags.length > 0" class="flex flex-wrap gap-2 mt-3">
              <span v-for="skill in skillTags" :key="skill"
                    class="text-[12px] bg-[#F4F4ED] text-ink px-2.5 py-1 rounded-md font-medium">
                {{ skill }}
              </span>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

            <!-- tarif horaire -->
            <div>
              <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
                Tarif horaire (DH)
              </label>
              <div class="relative">
                <input
                    v-model="form.hourlyRate"
                    type="number"
                    min="50"
                    max="2000"
                    placeholder="150"
                    class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition pr-14"
                />
                <span class="absolute right-3 top-1/2 -translate-y-1/2 text-[12px] font-medium text-[#73726C]">
                  DH/h
                </span>
              </div>
              <p class="text-[11px] text-[#9C9A92] mt-1.5">Tarif moyen au Maroc : 100-250 DH/h</p>
            </div>

            <!-- niveau d'expérience -->
            <div>
              <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
                Niveau d'expérience
              </label>
              <div class="grid grid-cols-3 gap-2">
                <button
                    v-for="level in experienceLevels"
                    :key="level.value"
                    @click="form.experienceLevel = level.value"
                    class="py-2 rounded-lg text-[12px] font-semibold border transition text-center"
                    :class="form.experienceLevel === level.value
                    ? 'bg-ink text-white border-ink'
                    : 'border-[#EBEBE5] text-[#5F5E5A] hover:border-brand-500 hover:text-brand-600'"
                >
                  {{ level.label }}
                </button>
              </div>
              <p class="text-[11px] text-[#9C9A92] mt-1.5">
                {{ experienceLevels.find(l => l.value === form.experienceLevel)?.desc }}
              </p>
            </div>

          </div>

        </div>
      </div>

      <!-- ===== BLOC 4 : LIENS ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5]">
          <h2 class="text-[14px] font-bold text-ink">Liens & Portfolio</h2>
        </div>
        <div class="p-6 space-y-4">

          <!-- portfolio -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              Portfolio
            </label>
            <div class="relative">
              <span class="absolute left-3 top-1/2 -translate-y-1/2 text-[#9C9A92]">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"/>
                </svg>
              </span>
              <input
                  v-model="form.portfolioUrl"
                  type="url"
                  placeholder="https://monportfolio.ma"
                  class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg pl-9 pr-3 py-2.5 text-[13px] outline-none transition"
              />
            </div>
          </div>

          <!-- github -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              GitHub
            </label>
            <div class="relative">
              <span class="absolute left-3 top-1/2 -translate-y-1/2 text-[#9C9A92] text-[12px] font-medium">
                github.com/
              </span>
              <input
                  v-model="form.githubUrl"
                  type="text"
                  placeholder="votre-username"
                  class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg pl-[88px] pr-3 py-2.5 text-[13px] outline-none transition"
              />
            </div>
          </div>

          <!-- linkedin -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              LinkedIn
            </label>
            <div class="relative">
              <span class="absolute left-3 top-1/2 -translate-y-1/2 text-[#9C9A92] text-[12px] font-medium">
                linkedin.com/in/
              </span>
              <input
                  v-model="form.linkedinUrl"
                  type="text"
                  placeholder="votre-username"
                  class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg pl-[118px] pr-3 py-2.5 text-[13px] outline-none transition"
              />
            </div>
          </div>

        </div>
      </div>

      <!-- ===== BOUTON SAUVEGARDER ===== -->
      <div class="flex items-center justify-between bg-white rounded-xl border border-[#EBEBE5] px-6 py-4">
        <p v-if="lastSaved" class="text-[12px] text-[#73726C] flex items-center gap-1.5">
          <svg class="w-4 h-4 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 13l4 4L19 7"/>
          </svg>
          Dernière sauvegarde : {{ lastSaved }}
        </p>
        <div v-else></div>
        <button @click="saveProfile" :disabled="saving"
                class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-5 py-2.5 rounded-lg transition disabled:opacity-50">
          <svg v-if="saving" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
          </svg>
          {{ saving ? 'Enregistrement...' : 'Enregistrer le profil' }}
        </button>
      </div>

    </div>

    <!-- TOAST -->
    <div v-if="toast.show"
         class="fixed bottom-6 right-6 z-50 px-4 py-3 rounded-xl shadow-xl text-[13px] font-medium flex items-center gap-2"
         :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
      <svg v-if="toast.type === 'success'" class="w-4 h-4 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
      </svg>
      <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
      </svg>
      {{ toast.message }}
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// ===== ÉTAT =====
const loading = ref(true)
const saving = ref(false)
const lastSaved = ref('')
const toast = ref({ show: false, message: '', type: 'success' })

// ===== DONNÉES BACKEND =====
// GET /api/profile → ProfileDTO
const profile = ref({
  id: null,
  email: '',
  firstName: '',
  lastName: '',
  role: '',
  averageRating: 0,
  totalReviews: 0,
  completedMissions: 0,
})

// ===== FORMULAIRE =====
// PUT /api/profile → UpdateProfileRequest
const form = ref({
  title: '',
  bio: '',
  location: '',
  skills: '',
  hourlyRate: null,
  experienceLevel: 'JUNIOR',
  isAvailable: true,
  portfolioUrl: '',
  githubUrl: '',
  linkedinUrl: '',
})

// ===== NIVEAUX D'EXPÉRIENCE =====
const experienceLevels = [
  { value: 'JUNIOR', label: 'Junior', desc: '0 - 2 ans d\'expérience' },
  { value: 'MID', label: 'Confirmé', desc: '3 - 5 ans d\'expérience' },
  { value: 'SENIOR', label: 'Senior', desc: '5+ ans d\'expérience' },
]

// ===== COMPUTED =====
const userInitials = computed(() => {
  const f = profile.value.firstName?.[0] || ''
  const l = profile.value.lastName?.[0] || ''
  return (f + l).toUpperCase() || 'FL'
})

// aperçu des compétences en tags
const skillTags = computed(() => {
  if (!form.value.skills) return []
  return form.value.skills
      .split(',')
      .map(s => s.trim())
      .filter(s => s.length > 0)
})

// ===== AXIOS HEADER =====
const headers = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
})

// ===== CHARGER LE PROFIL → GET /api/profile =====
const loadProfile = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(
        `${API_URL}/api/profile`,
        headers()
    )

    // infos lecture seule
    profile.value = {
      id: data.id,
      email: data.email || '',
      firstName: data.firstName || '',
      lastName: data.lastName || '',
      role: data.role || '',
      averageRating: data.averageRating || 0,
      totalReviews: data.totalReviews || 0,
      completedMissions: data.completedMissions || 0,
    }

    // formulaire modifiable
    // correspond exactement à UpdateProfileRequest
    form.value = {
      title: data.title || '',
      bio: data.bio || '',
      location: data.location || '',
      skills: data.skills || '',
      hourlyRate: data.hourlyRate || null,
      experienceLevel: data.experienceLevel || 'JUNIOR',
      isAvailable: data.isAvailable ?? true,
      portfolioUrl: data.portfolioUrl || '',
      githubUrl: data.githubUrl || '',
      linkedinUrl: data.linkedinUrl || '',
    }

  } catch (err) {
    console.error('loadProfile error:', err)
    showToast('Impossible de charger le profil', 'error')
  } finally {
    loading.value = false
  }
}

// ===== SAUVEGARDER → PUT /api/profile =====
// body : UpdateProfileRequest {
//   title, bio, location, skills,
//   hourlyRate, experienceLevel, isAvailable,
//   portfolioUrl, githubUrl, linkedinUrl
// }
const saveProfile = async () => {
  saving.value = true
  try {
    const { data } = await axios.put(
        `${API_URL}/api/profile`,
        {
          title: form.value.title || null,
          bio: form.value.bio || null,
          location: form.value.location || null,
          skills: form.value.skills || null,
          hourlyRate: form.value.hourlyRate || null,
          experienceLevel: form.value.experienceLevel,
          isAvailable: form.value.isAvailable,
          portfolioUrl: form.value.portfolioUrl || null,
          githubUrl: form.value.githubUrl || null,
          linkedinUrl: form.value.linkedinUrl || null,
        },
        headers()
    )

    // mettre à jour les stats depuis la réponse backend
    profile.value.averageRating = data.averageRating
    profile.value.totalReviews = data.totalReviews
    profile.value.completedMissions = data.completedMissions

    // heure de sauvegarde
    const now = new Date()
    lastSaved.value = now.toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit'
    })

    showToast('Profil enregistré avec succès !')

  } catch (err) {
    console.error('saveProfile error:', err)
    showToast(
        err.response?.data?.message || 'Erreur lors de la sauvegarde',
        'error'
    )
  } finally {
    saving.value = false
  }
}

// ===== TOAST =====
const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4000)
}

onMounted(() => {
  loadProfile()
})
</script>