<template>
  <div class="min-h-screen bg-cream flex items-center justify-center p-6">
    <div class="w-full max-w-lg">

      <!-- Logo -->
      <div class="text-center mb-8">
        <RouterLink to="/" class="inline-flex items-center gap-2">
          <svg class="w-8 h-8 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
            <circle cx="12" cy="12" r="10"/>
            <circle cx="12" cy="12" r="4" fill="#001E00"/>
          </svg>
          <span class="text-xl font-bold text-ink">FreelanceMa</span>
        </RouterLink>
      </div>

      <!-- Indicateur d'étapes (1, 2, 3) -->
      <div class="flex items-center justify-center gap-2 mb-8">
        <div v-for="step in 3" :key="step" class="flex items-center gap-2">

          <!-- Cercle de l'étape -->
          <div class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-bold transition-all"
               :class="
                 currentStep > step  ? 'bg-brand-500 text-white' :
                 currentStep === step ? 'bg-ink text-white' :
                                        'bg-gray-200 text-gray-400'
               ">
            <!-- Coche si étape passée -->
            <svg v-if="currentStep > step" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
            </svg>
            <span v-else>{{ step }}</span>
          </div>

          <!-- Ligne entre les étapes -->
          <div v-if="step < 3" class="w-14 h-0.5 transition-all"
               :class="currentStep > step ? 'bg-brand-500' : 'bg-gray-200'"></div>
        </div>
      </div>

      <!-- Carte blanche principale -->
      <div class="bg-white rounded-2xl border border-[#EBEBE5] p-8 shadow-sm">

        <!-- ==================== ÉTAPE 1 : Spécialité ==================== -->
        <div v-if="currentStep === 1">

          <div class="mb-6">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-1">Étape 1 sur 3</div>
            <h1 class="text-2xl font-bold text-ink">Votre spécialité</h1>
            <p class="text-sm text-[#73726C] mt-1">Dites aux clients ce que vous faites</p>
          </div>

          <!-- Titre professionnel -->
          <div class="mb-5">
            <label class="block text-xs font-bold text-ink uppercase tracking-wider mb-2">
              Titre professionnel
            </label>
            <input
                v-model="form.title"
                type="text"
                placeholder="Ex: Développeur Full Stack Vue.js + Spring Boot"
                class="w-full border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3 text-sm outline-none transition"
            />
            <p class="text-xs text-[#9C9A92] mt-1.5">Ce titre apparaît en haut de votre profil public</p>
          </div>

          <!-- Compétences -->
          <div>
            <label class="block text-xs font-bold text-ink uppercase tracking-wider mb-2">
              Compétences principales
            </label>
            <input
                v-model="form.skills"
                type="text"
                placeholder="Vue.js, Spring Boot, MySQL, Docker, Tailwind..."
                class="w-full border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3 text-sm outline-none transition"
            />
            <p class="text-xs text-[#9C9A92] mt-1.5">Séparez chaque compétence par une virgule</p>

            <!-- Aperçu des tags de compétences -->
            <div v-if="skillTags.length > 0" class="flex flex-wrap gap-2 mt-3">
              <span v-for="skill in skillTags" :key="skill"
                    class="text-xs bg-[#F4F4ED] text-ink px-2.5 py-1 rounded-md font-medium">
                {{ skill }}
              </span>
            </div>
          </div>

        </div>

        <!-- ==================== ÉTAPE 2 : Expérience & Tarif ==================== -->
        <div v-if="currentStep === 2">

          <div class="mb-6">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-1">Étape 2 sur 3</div>
            <h1 class="text-2xl font-bold text-ink">Votre expérience</h1>
            <p class="text-sm text-[#73726C] mt-1">Ces infos aident les clients à vous choisir</p>
          </div>

          <!-- Niveau d'expérience -->
          <div class="mb-6">
            <label class="block text-xs font-bold text-ink uppercase tracking-wider mb-3">
              Niveau d'expérience
            </label>
            <div class="grid grid-cols-3 gap-3">
              <button
                  v-for="niveau in niveaux"
                  :key="niveau.value"
                  @click="form.experienceLevel = niveau.value"
                  class="border-2 rounded-xl p-4 text-center transition"
                  :class="form.experienceLevel === niveau.value
                    ? 'border-brand-500 bg-brand-50 text-brand-700'
                    : 'border-gray-200 text-ink hover:border-gray-300'"
              >
                <div class="text-sm font-bold">{{ niveau.label }}</div>
                <div class="text-xs text-[#73726C] mt-1">{{ niveau.desc }}</div>
              </button>
            </div>
          </div>

          <!-- Tarif horaire -->
          <div>
            <label class="block text-xs font-bold text-ink uppercase tracking-wider mb-2">
              Tarif horaire souhaité (DH)
            </label>
            <div class="relative">
              <input
                  v-model="form.hourlyRate"
                  type="number"
                  min="50"
                  placeholder="150"
                  class="w-full border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3 text-sm outline-none transition pr-14"
              />
              <span class="absolute right-4 top-1/2 -translate-y-1/2 text-sm text-[#9C9A92] font-medium">DH/h</span>
            </div>
            <p class="text-xs text-[#9C9A92] mt-1.5">Tarif moyen au Maroc : 100 - 250 DH/h</p>
          </div>

        </div>

        <!-- ==================== ÉTAPE 3 : Bio & Localisation ==================== -->
        <div v-if="currentStep === 3">

          <div class="mb-6">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-1">Étape 3 sur 3</div>
            <h1 class="text-2xl font-bold text-ink">À propos de vous</h1>
            <p class="text-sm text-[#73726C] mt-1">Présentez-vous aux clients en quelques lignes</p>
          </div>

          <!-- Bio -->
          <div class="mb-5">
            <label class="block text-xs font-bold text-ink uppercase tracking-wider mb-2">
              Bio / Description
            </label>
            <textarea
                v-model="form.bio"
                rows="5"
                placeholder="Décrivez votre expérience, vos spécialités, et ce que vous apportez à vos clients..."
                class="w-full border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3 text-sm outline-none transition resize-none"
            ></textarea>
            <div class="flex justify-end mt-1">
              <span class="text-xs" :class="(form.bio?.length || 0) > 500 ? 'text-red-500' : 'text-[#9C9A92]'">
                {{ form.bio?.length || 0 }} / 500
              </span>
            </div>
          </div>

          <!-- Localisation -->
          <div>
            <label class="block text-xs font-bold text-ink uppercase tracking-wider mb-2">
              Ville / Pays
            </label>
            <input
                v-model="form.location"
                type="text"
                placeholder="Ex: Casablanca, Maroc"
                class="w-full border-2 border-gray-200 focus:border-brand-500 rounded-xl px-4 py-3 text-sm outline-none transition"
            />
          </div>

        </div>

        <!-- ==================== BOUTONS DE NAVIGATION ==================== -->
        <div class="flex items-center justify-between mt-8 pt-6 border-t border-gray-100">

          <!-- Bouton "Passer" (ignorer cette étape) -->
          <button
              @click="passer"
              class="text-sm text-[#73726C] hover:text-ink transition flex items-center gap-1"
          >
            Passer
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </button>

          <!-- Bouton "Continuer" ou "Terminer" -->
          <button
              @click="continuer"
              :disabled="loading"
              class="bg-ink text-white px-7 py-3 rounded-xl text-sm font-semibold hover:bg-brand-500 transition disabled:opacity-50 flex items-center gap-2"
          >
            <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ loading ? 'Enregistrement...' : currentStep === 3 ? 'Terminer' : 'Continuer' }}
          </button>

        </div>

      </div>

      <!-- Message en bas -->
      <p class="text-center text-xs text-[#9C9A92] mt-5">
        Vous pouvez modifier ces informations à tout moment dans votre tableau de bord
      </p>

    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// ===== ÉTAT =====
const currentStep = ref(1)  // étape actuelle : 1, 2 ou 3
const loading = ref(false)  // true pendant l'envoi vers le backend

// ===== FORMULAIRE (correspond à UpdateProfileRequest côté backend) =====
const form = ref({
  title: '',            // Titre professionnel (étape 1)
  skills: '',           // Compétences séparées par virgules (étape 1)
  experienceLevel: 'JUNIOR',  // Niveau d'expérience (étape 2)
  hourlyRate: null,     // Tarif horaire en DH (étape 2)
  bio: '',              // Biographie (étape 3)
  location: '',         // Ville / Pays (étape 3)
})

// ===== NIVEAUX D'EXPÉRIENCE =====
const niveaux = [
  { value: 'JUNIOR',  label: 'Junior',   desc: '0 - 2 ans' },
  { value: 'MID',     label: 'Confirmé', desc: '3 - 5 ans' },
  { value: 'SENIOR',  label: 'Senior',   desc: '5+ ans'    },
]

// ===== Aperçu des tags de compétences =====
// Transforme "Vue.js, Docker, MySQL" en tableau ["Vue.js", "Docker", "MySQL"]
const skillTags = computed(() => {
  if (!form.value.skills) return []
  return form.value.skills
    .split(',')
    .map(s => s.trim())
    .filter(s => s.length > 0)
})

// ===== Construire le header JWT pour axios =====
const getHeaders = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
})

// ===== Envoyer le profil au backend → PUT /api/profile =====
const sauvegarderProfil = async () => {
  loading.value = true
  try {
    await axios.put(
      `${API_URL}/api/profile`,
      {
        title:           form.value.title       || null,
        skills:          form.value.skills      || null,
        experienceLevel: form.value.experienceLevel,
        hourlyRate:      form.value.hourlyRate  || null,
        bio:             form.value.bio         || null,
        location:        form.value.location    || null,
        isAvailable:     true,
      },
      getHeaders()
    )
  } catch (err) {
    // On ne bloque pas l'utilisateur si ça échoue — il pourra compléter son profil plus tard
    console.error('Erreur lors de la sauvegarde du profil:', err)
  } finally {
    loading.value = false
  }
}

// ===== Bouton "Continuer" =====
const continuer = async () => {
  if (currentStep.value < 3) {
    // Passer à l'étape suivante
    currentStep.value++
  } else {
    // Dernière étape → sauvegarder tout et aller au dashboard
    await sauvegarderProfil()
    router.push('/freelancer/dashboard')
  }
}

// ===== Bouton "Passer" (ignorer cette étape sans sauvegarder) =====
const passer = () => {
  if (currentStep.value < 3) {
    currentStep.value++
  } else {
    // Dernière étape "Passer" → aller directement au dashboard sans sauvegarder
    router.push('/freelancer/dashboard')
  }
}
</script>
