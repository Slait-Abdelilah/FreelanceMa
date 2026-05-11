<template>
  <div class="max-w-6xl mx-auto space-y-5">

    <!-- EN-TÊTE -->
    <div>
      <h1 class="text-xl font-bold text-ink">Explorer les missions</h1>
      <p class="text-[13px] text-[#73726C] mt-0.5">
        {{ totalOffers }} missions disponibles
      </p>
    </div>

    <!-- BARRE DE RECHERCHE + FILTRES -->
    <div class="bg-white rounded-xl border border-[#EBEBE5] p-4 space-y-3">

      <!-- recherche -->
      <div class="relative">
        <svg class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[#9C9A92]"
             fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
        </svg>
        <input
            v-model="searchQuery"
            @input="onSearch"
            type="text"
            placeholder="Rechercher par titre, compétences, description..."
            class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg pl-10 pr-4 py-2.5 text-[13px] outline-none transition"
        />
        <button v-if="searchQuery"
                @click="clearSearch"
                class="absolute right-3.5 top-1/2 -translate-y-1/2 text-[#9C9A92] hover:text-ink">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>

      <!-- filtres -->
      <div class="flex items-center gap-3 flex-wrap">

        <!-- catégorie -->
        <select v-model="selectedCategory" @change="applyFilters"
                class="text-[12px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-2 outline-none focus:border-brand-500 transition">
          <option value="">Toutes catégories</option>
          <option v-for="cat in categories" :key="cat.value" :value="cat.value">
            {{ cat.label }}
          </option>
        </select>

        <!-- budget type -->
        <select v-model="selectedBudgetType" @change="applyFilters"
                class="text-[12px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-2 outline-none focus:border-brand-500 transition">
          <option value="">Tous les budgets</option>
          <option value="FIXED">Prix fixe</option>
          <option value="HOURLY">Tarif horaire</option>
        </select>

        <!-- tri -->
        <select v-model="sortBy" @change="sortOffers"
                class="text-[12px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-2 outline-none focus:border-brand-500 transition">
          <option value="recent">Plus récentes</option>
          <option value="budget_desc">Budget décroissant</option>
          <option value="budget_asc">Budget croissant</option>
          <option value="applications">Moins de candidatures</option>
        </select>

        <!-- reset filtres -->
        <button v-if="hasFilters" @click="resetFilters"
                class="text-[12px] font-medium text-[#73726C] hover:text-ink flex items-center gap-1 transition">
          <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
          </svg>
          Réinitialiser
        </button>

        <div class="ml-auto flex items-center gap-2">
          <!-- vue grille / liste -->
          <button @click="viewMode = 'grid'"
                  class="p-1.5 rounded-md transition"
                  :class="viewMode === 'grid' ? 'bg-ink text-white' : 'hover:bg-[#F4F4ED] text-[#73726C]'">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6A2.25 2.25 0 016 3.75h2.25A2.25 2.25 0 0110.5 6v2.25a2.25 2.25 0 01-2.25 2.25H6a2.25 2.25 0 01-2.25-2.25V6zM3.75 15.75A2.25 2.25 0 016 13.5h2.25a2.25 2.25 0 012.25 2.25V18a2.25 2.25 0 01-2.25 2.25H6A2.25 2.25 0 013.75 18v-2.25zM13.5 6a2.25 2.25 0 012.25-2.25H18A2.25 2.25 0 0120.25 6v2.25A2.25 2.25 0 0118 10.5h-2.25a2.25 2.25 0 01-2.25-2.25V6zM13.5 15.75a2.25 2.25 0 012.25-2.25H18a2.25 2.25 0 012.25 2.25V18A2.25 2.25 0 0118 20.25h-2.25A2.25 2.25 0 0113.5 18v-2.25z"/>
            </svg>
          </button>
          <button @click="viewMode = 'list'"
                  class="p-1.5 rounded-md transition"
                  :class="viewMode === 'list' ? 'bg-ink text-white' : 'hover:bg-[#F4F4ED] text-[#73726C]'">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zM3.75 12h.007v.008H3.75V12zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm-.375 5.25h.007v.008H3.75v-.008zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z"/>
            </svg>
          </button>
        </div>

      </div>

    </div>

    <!-- LOADING -->
    <div v-if="loading" class="flex justify-center py-16">
      <div class="w-6 h-6 border-2 border-brand-500 border-t-transparent rounded-full animate-spin"></div>
    </div>

    <!-- AUCUN RÉSULTAT -->
    <div v-else-if="filteredOffers.length === 0"
         class="bg-white rounded-xl border border-[#EBEBE5] py-16 text-center">
      <div class="w-14 h-14 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-7 h-7 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
        </svg>
      </div>
      <h3 class="text-[15px] font-bold text-ink mb-1">Aucune mission trouvée</h3>
      <p class="text-[13px] text-[#73726C] mb-4">Essayez d'autres mots-clés ou réinitialisez les filtres</p>
      <button @click="resetFilters"
              class="text-[13px] font-semibold text-brand-600 hover:underline">
        Voir toutes les missions
      </button>
    </div>

    <!-- LISTE OFFRES — MODE GRILLE -->
    <div v-else-if="viewMode === 'grid'"
         class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
      <div v-for="offer in filteredOffers" :key="offer.id"
           @click="openOffer(offer)"
           class="bg-white rounded-xl border border-[#EBEBE5] p-5 cursor-pointer hover:border-brand-300 hover:shadow-sm transition group">

        <!-- header carte -->
        <div class="flex items-start justify-between gap-3 mb-3">
          <div class="flex-1 min-w-0">
            <span class="inline-block text-[10px] font-bold px-2 py-0.5 rounded-full mb-2"
                  :class="getCategoryClass(offer.category)">
              {{ getCategoryLabel(offer.category) }}
            </span>
            <h3 class="text-[14px] font-bold text-ink leading-snug line-clamp-2 group-hover:text-brand-600 transition">
              {{ offer.title }}
            </h3>
          </div>
          <div class="text-right flex-shrink-0">
            <div class="text-[13px] font-bold text-ink">
              {{ formatBudget(offer.budgetMin, offer.budgetMax) }}
            </div>
            <div class="text-[10px] text-[#9C9A92]">
              {{ offer.budgetType === 'FIXED' ? 'Prix fixe' : 'Horaire' }}
            </div>
          </div>
        </div>

        <!-- description -->
        <p class="text-[12px] text-[#73726C] leading-relaxed line-clamp-2 mb-3">
          {{ offer.description }}
        </p>

        <!-- compétences -->
        <div v-if="offer.requiredSkills" class="flex flex-wrap gap-1.5 mb-3">
          <span v-for="skill in getSkillTags(offer.requiredSkills).slice(0, 3)"
                :key="skill"
                class="text-[11px] bg-[#F4F4ED] text-ink px-2 py-0.5 rounded-md">
            {{ skill }}
          </span>
          <span v-if="getSkillTags(offer.requiredSkills).length > 3"
                class="text-[11px] text-[#9C9A92]">
            +{{ getSkillTags(offer.requiredSkills).length - 3 }}
          </span>
        </div>

        <!-- footer -->
        <div class="flex items-center justify-between pt-3 border-t border-[#EBEBE5]">
          <div class="flex items-center gap-3 text-[11px] text-[#9C9A92]">
            <span class="flex items-center gap-1">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z"/>
              </svg>
              {{ offer.applicationsCount }} candidats
            </span>
            <span v-if="offer.deadline" class="flex items-center gap-1">
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 9v7.5"/>
              </svg>
              {{ formatDate(offer.deadline) }}
            </span>
          </div>
          <span class="text-[11px] text-[#9C9A92]">
            {{ timeAgo(offer.createdAt) }}
          </span>
        </div>

      </div>
    </div>

    <!-- LISTE OFFRES — MODE LISTE -->
    <div v-else class="space-y-3">
      <div v-for="offer in filteredOffers" :key="offer.id"
           @click="openOffer(offer)"
           class="bg-white rounded-xl border border-[#EBEBE5] p-5 cursor-pointer hover:border-brand-300 hover:shadow-sm transition group">

        <div class="flex items-start gap-4">

          <!-- catégorie icon -->
          <div class="w-10 h-10 rounded-lg flex items-center justify-center flex-shrink-0 text-lg"
               :class="getCategoryBg(offer.category)">
            {{ getCategoryIcon(offer.category) }}
          </div>

          <!-- infos -->
          <div class="flex-1 min-w-0">
            <div class="flex items-start justify-between gap-4">
              <div class="flex-1">
                <div class="flex items-center gap-2 flex-wrap mb-1">
                  <h3 class="text-[14px] font-bold text-ink group-hover:text-brand-600 transition">
                    {{ offer.title }}
                  </h3>
                  <span class="text-[10px] font-bold px-2 py-0.5 rounded-full"
                        :class="getCategoryClass(offer.category)">
                    {{ getCategoryLabel(offer.category) }}
                  </span>
                </div>
                <p class="text-[12px] text-[#73726C] line-clamp-1 mb-2">
                  {{ offer.description }}
                </p>
                <div v-if="offer.requiredSkills" class="flex flex-wrap gap-1.5">
                  <span v-for="skill in getSkillTags(offer.requiredSkills).slice(0, 4)"
                        :key="skill"
                        class="text-[11px] bg-[#F4F4ED] text-ink px-2 py-0.5 rounded-md">
                    {{ skill }}
                  </span>
                </div>
              </div>

              <div class="text-right flex-shrink-0">
                <div class="text-[15px] font-bold text-ink">
                  {{ formatBudget(offer.budgetMin, offer.budgetMax) }}
                </div>
                <div class="text-[11px] text-[#9C9A92]">
                  {{ offer.budgetType === 'FIXED' ? 'Prix fixe' : 'Tarif horaire' }}
                </div>
              </div>
            </div>

            <div class="flex items-center gap-4 mt-2 text-[11px] text-[#9C9A92]">
              <span>{{ offer.applicationsCount }} candidats</span>
              <span v-if="offer.deadline">Deadline : {{ formatDate(offer.deadline) }}</span>
              <span>{{ timeAgo(offer.createdAt) }}</span>
            </div>
          </div>

        </div>
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

    <!-- ===== MODAL DÉTAIL OFFRE + POSTULER ===== -->
    <div v-if="selectedOffer"
         class="fixed inset-0 bg-ink/50 z-50 flex items-end md:items-center justify-center p-0 md:p-4"
         @click.self="selectedOffer = null">

      <div class="bg-white rounded-t-2xl md:rounded-2xl w-full md:max-w-2xl shadow-2xl max-h-[90vh] overflow-y-auto">

        <!-- header modal -->
        <div class="sticky top-0 bg-white px-6 py-4 border-b border-[#EBEBE5] flex items-start justify-between">
          <div class="flex-1 pr-4">
            <span class="inline-block text-[10px] font-bold px-2 py-0.5 rounded-full mb-2"
                  :class="getCategoryClass(selectedOffer.category)">
              {{ getCategoryLabel(selectedOffer.category) }}
            </span>
            <h2 class="text-[16px] font-bold text-ink leading-snug">
              {{ selectedOffer.title }}
            </h2>
          </div>
          <button @click="selectedOffer = null" class="p-1.5 hover:bg-[#F4F4ED] rounded-md flex-shrink-0">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <!-- body modal -->
        <div class="p-6 space-y-5">

          <!-- stats rapides -->
          <div class="grid grid-cols-3 gap-3">
            <div class="bg-[#FAFAF7] rounded-xl p-3 text-center">
              <div class="text-[16px] font-bold text-ink">
                {{ formatBudget(selectedOffer.budgetMin, selectedOffer.budgetMax) }}
              </div>
              <div class="text-[11px] text-[#73726C]">
                {{ selectedOffer.budgetType === 'FIXED' ? 'Prix fixe' : 'Horaire' }}
              </div>
            </div>
            <div class="bg-[#FAFAF7] rounded-xl p-3 text-center">
              <div class="text-[16px] font-bold text-ink">
                {{ selectedOffer.applicationsCount }}
              </div>
              <div class="text-[11px] text-[#73726C]">Candidats</div>
            </div>
            <div class="bg-[#FAFAF7] rounded-xl p-3 text-center">
              <div class="text-[14px] font-bold text-ink">
                {{ selectedOffer.deadline ? formatDate(selectedOffer.deadline) : '—' }}
              </div>
              <div class="text-[11px] text-[#73726C]">Deadline</div>
            </div>
          </div>

          <!-- description -->
          <div>
            <h3 class="text-[13px] font-bold text-ink mb-2 uppercase tracking-wider">Description</h3>
            <p class="text-[13px] text-[#5F5E5A] leading-relaxed whitespace-pre-line">
              {{ selectedOffer.description }}
            </p>
          </div>

          <!-- compétences requises -->
          <div v-if="selectedOffer.requiredSkills">
            <h3 class="text-[13px] font-bold text-ink mb-2 uppercase tracking-wider">
              Compétences requises
            </h3>
            <div class="flex flex-wrap gap-2">
              <span v-for="skill in getSkillTags(selectedOffer.requiredSkills)"
                    :key="skill"
                    class="text-[12px] bg-brand-50 text-brand-700 px-2.5 py-1 rounded-md font-medium">
                {{ skill }}
              </span>
            </div>
          </div>

          <!-- formulaire candidature -->
          <div v-if="!alreadyApplied" class="border-t border-[#EBEBE5] pt-5">
            <h3 class="text-[14px] font-bold text-ink mb-4">Votre candidature</h3>

            <!-- erreur -->
            <div v-if="applyError"
                 class="bg-red-50 border border-red-200 text-red-700 rounded-lg p-3 mb-4 text-[12px]">
              {{ applyError }}
            </div>

            <div class="space-y-4">

              <!-- lettre de motivation -->
              <div>
                <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
                  Lettre de motivation
                </label>
                <textarea v-model="applyForm.coverLetter"
                          rows="4"
                          placeholder="Présentez-vous et expliquez pourquoi vous êtes le meilleur candidat pour cette mission..."
                          class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition resize-none"></textarea>
              </div>

              <div class="grid grid-cols-2 gap-4">

                <!-- budget proposé -->
                <div>
                  <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
                    Votre devis (DH)
                  </label>
                  <div class="relative">
                    <input v-model="applyForm.proposedBudget"
                           type="number"
                           :placeholder="selectedOffer.budgetMin"
                           class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition pr-10"/>
                    <span class="absolute right-3 top-1/2 -translate-y-1/2 text-[11px] text-[#9C9A92]">DH</span>
                  </div>
                  <p class="text-[11px] text-[#9C9A92] mt-1">
                    Budget client : {{ formatBudget(selectedOffer.budgetMin, selectedOffer.budgetMax) }} DH
                  </p>
                </div>

                <!-- délai proposé -->
                <div>
                  <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
                    Délai estimé (jours)
                  </label>
                  <div class="relative">
                    <input v-model="applyForm.proposedDays"
                           type="number"
                           min="1"
                           placeholder="Ex: 14"
                           class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition pr-16"/>
                    <span class="absolute right-3 top-1/2 -translate-y-1/2 text-[11px] text-[#9C9A92]">jours</span>
                  </div>
                </div>

              </div>

            </div>
          </div>

          <!-- déjà postulé -->
          <div v-else
               class="border-t border-[#EBEBE5] pt-5 flex items-center gap-3 p-4 bg-brand-50 rounded-xl">
            <svg class="w-5 h-5 text-brand-600 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
            <div>
              <div class="text-[13px] font-bold text-brand-700">Candidature envoyée</div>
              <div class="text-[12px] text-brand-600">Vous avez déjà postulé à cette offre</div>
            </div>
          </div>

        </div>

        <!-- footer modal -->
        <div v-if="!alreadyApplied" class="sticky bottom-0 bg-white px-6 py-4 border-t border-[#EBEBE5] flex gap-3">
          <button @click="selectedOffer = null"
                  class="flex-1 py-2.5 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-lg transition">
            Fermer
          </button>
          <button @click="submitApplication" :disabled="applying"
                  class="flex-1 py-2.5 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
            <svg v-if="applying" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ applying ? 'Envoi en cours...' : '🚀 Postuler maintenant' }}
          </button>
        </div>

        <div v-else class="px-6 py-4 border-t border-[#EBEBE5]">
          <button @click="selectedOffer = null"
                  class="w-full py-2.5 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-lg transition">
            Fermer
          </button>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// ===== ÉTAT =====
const loading = ref(true)
const applying = ref(false)
const viewMode = ref('grid')
const selectedOffer = ref(null)
const alreadyApplied = ref(false)
const applyError = ref('')
const toast = ref({ show: false, message: '', type: 'success' })

// ===== FILTRES =====
const searchQuery = ref('')
const selectedCategory = ref('')
const selectedBudgetType = ref('')
const sortBy = ref('recent')
let searchTimeout = null

// ===== DONNÉES =====
// GET http://localhost:8080/api/offers → List<OfferDTO>
const offers = ref([])

// ===== FORMULAIRE CANDIDATURE =====
// POST http://localhost:8080/api/applications → CreateApplicationRequest
const applyForm = ref({
  coverLetter: '',
  proposedBudget: null,
  proposedDays: null,
})

// ===== CATÉGORIES =====
const categories = [
  { value: 'WEB_DEVELOPMENT', label: 'Développement Web' },
  { value: 'MOBILE_DEVELOPMENT', label: 'Mobile' },
  { value: 'DESIGN', label: 'Design' },
  { value: 'MARKETING', label: 'Marketing' },
  { value: 'WRITING', label: 'Rédaction' },
  { value: 'VIDEO', label: 'Vidéo' },
  { value: 'TRANSLATION', label: 'Traduction' },
  { value: 'DATA_SCIENCE', label: 'Data Science' },
  { value: 'OTHER', label: 'Autre' },
]

// ===== COMPUTED =====
const totalOffers = computed(() => filteredOffers.value.length)

const hasFilters = computed(() =>
    searchQuery.value || selectedCategory.value || selectedBudgetType.value
)

const filteredOffers = computed(() => {
  let result = [...offers.value]

  // filtre catégorie
  if (selectedCategory.value) {
    result = result.filter(o => o.category === selectedCategory.value)
  }

  // filtre budget type
  if (selectedBudgetType.value) {
    result = result.filter(o => o.budgetType === selectedBudgetType.value)
  }

  // tri
  switch (sortBy.value) {
    case 'budget_desc':
      result.sort((a, b) => (b.budgetMax || 0) - (a.budgetMax || 0))
      break
    case 'budget_asc':
      result.sort((a, b) => (a.budgetMin || 0) - (b.budgetMin || 0))
      break
    case 'applications':
      result.sort((a, b) => (a.applicationsCount || 0) - (b.applicationsCount || 0))
      break
    default:
      result.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  }

  return result
})

// ===== AXIOS =====
const authHeaders = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
})

// ===== CHARGER LES OFFRES → GET /api/offers =====
const loadOffers = async (search = '') => {
  loading.value = true
  try {
    const params = {}
    if (search) params.search = search

    const { data } = await axios.get(
        `${API_URL}/api/offers`,
        { params }
    )
    offers.value = data.content ?? data
  } catch (err) {
    console.error('loadOffers error:', err)
    showToast('Impossible de charger les missions', 'error')
  } finally {
    loading.value = false
  }
}

// ===== RECHERCHE AVEC DEBOUNCE =====
const onSearch = () => {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    loadOffers(searchQuery.value)
  }, 400)
}

const clearSearch = () => {
  searchQuery.value = ''
  loadOffers()
}

// ===== APPLIQUER FILTRES =====
const applyFilters = () => {
  // les filtres locaux sont gérés par computed
  // la recherche appelle le backend
}

const sortOffers = () => {
  // tri géré par computed
}

const resetFilters = () => {
  searchQuery.value = ''
  selectedCategory.value = ''
  selectedBudgetType.value = ''
  sortBy.value = 'recent'
  loadOffers()
}

// ===== OUVRIR UNE OFFRE =====
const openOffer = (offer) => {
  selectedOffer.value = offer
  alreadyApplied.value = false
  applyError.value = ''
  applyForm.value = {
    coverLetter: '',
    proposedBudget: null,
    proposedDays: null,
  }
}

// ===== POSTULER → POST /api/applications =====
// body : CreateApplicationRequest { offerId, coverLetter, proposedBudget, proposedDays }
const submitApplication = async () => {
  applyError.value = ''

  if (!applyForm.value.coverLetter.trim()) {
    applyError.value = 'Écrivez une lettre de motivation'
    return
  }

  if (!applyForm.value.proposedBudget || applyForm.value.proposedBudget <= 0) {
    applyError.value = 'Entrez votre devis'
    return
  }

  if (!applyForm.value.proposedDays || applyForm.value.proposedDays <= 0) {
    applyError.value = 'Entrez votre délai estimé'
    return
  }

  applying.value = true
  try {
    await axios.post(
        `${API_URL}/api/applications`,
        {
          offerId: selectedOffer.value.id,
          coverLetter: applyForm.value.coverLetter,
          proposedBudget: parseFloat(applyForm.value.proposedBudget),
          proposedDays: parseInt(applyForm.value.proposedDays),
        },
        authHeaders()
    )

    // mettre à jour le compteur localement
    const offer = offers.value.find(o => o.id === selectedOffer.value.id)
    if (offer) offer.applicationsCount++

    alreadyApplied.value = true
    showToast('Candidature envoyée avec succès ! 🎉')

  } catch (err) {
    console.error('apply error:', err)
    applyError.value = err.response?.data?.message || 'Erreur lors de l\'envoi'
  } finally {
    applying.value = false
  }
}

// ===== HELPERS =====

const formatBudget = (min, max) => {
  if (!min && !max) return 'À négocier'
  if (min && max) return `${formatNumber(min)} - ${formatNumber(max)}`
  if (min) return `À partir de ${formatNumber(min)}`
  return `Jusqu\'à ${formatNumber(max)}`
}

const formatNumber = (n) => {
  return parseFloat(n).toLocaleString('fr-MA')
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  })
}

const timeAgo = (date) => {
  if (!date) return ''
  const diff = Date.now() - new Date(date).getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 60) return `il y a ${minutes}min`
  if (hours < 24) return `il y a ${hours}h`
  if (days < 7) return `il y a ${days}j`
  return formatDate(date)
}

const getSkillTags = (skills) => {
  if (!skills) return []
  return skills.split(',').map(s => s.trim()).filter(s => s.length > 0)
}

const getCategoryLabel = (category) => {
  return categories.find(c => c.value === category)?.label || category || 'Autre'
}

const getCategoryClass = (category) => {
  const classes = {
    WEB_DEVELOPMENT: 'bg-blue-50 text-blue-700',
    MOBILE_DEVELOPMENT: 'bg-purple-50 text-purple-700',
    DESIGN: 'bg-pink-50 text-pink-700',
    MARKETING: 'bg-orange-50 text-orange-700',
    WRITING: 'bg-amber-50 text-amber-700',
    VIDEO: 'bg-red-50 text-red-700',
    TRANSLATION: 'bg-cyan-50 text-cyan-700',
    DATA_SCIENCE: 'bg-green-50 text-green-700',
    OTHER: 'bg-gray-100 text-gray-600',
  }
  return classes[category] || 'bg-gray-100 text-gray-600'
}

const getCategoryBg = (category) => {
  const bgs = {
    WEB_DEVELOPMENT: 'bg-blue-50',
    MOBILE_DEVELOPMENT: 'bg-purple-50',
    DESIGN: 'bg-pink-50',
    MARKETING: 'bg-orange-50',
    WRITING: 'bg-amber-50',
    VIDEO: 'bg-red-50',
    TRANSLATION: 'bg-cyan-50',
    DATA_SCIENCE: 'bg-green-50',
    OTHER: 'bg-gray-100',
  }
  return bgs[category] || 'bg-gray-100'
}

const getCategoryIcon = (category) => {
  const icons = {
    WEB_DEVELOPMENT: '🌐',
    MOBILE_DEVELOPMENT: '📱',
    DESIGN: '🎨',
    MARKETING: '📈',
    WRITING: '✍️',
    VIDEO: '🎬',
    TRANSLATION: '🌍',
    DATA_SCIENCE: '📊',
    OTHER: '💼',
  }
  return icons[category] || '💼'
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4000)
}

onMounted(() => {
  loadOffers()
})
</script>