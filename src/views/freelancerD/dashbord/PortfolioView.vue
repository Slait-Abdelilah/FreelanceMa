<template>
  <div>

    <!-- ==================== VUE LISTE ==================== -->
    <div v-if="!formOpen" class="space-y-6">

      <!-- HEADER -->
      <div class="flex items-start justify-between gap-4 flex-wrap">
        <div>
          <h1 class="text-xl font-bold text-ink">Portfolio</h1>
          <p class="text-[13px] text-[#73726C] mt-0.5">Vos projets réalisés — vitrine publique visible par les clients</p>
        </div>
        <div class="flex items-center gap-3">
          <div v-if="projects.length > 0"
               class="hidden sm:flex items-center gap-3 text-[12px] text-[#73726C] bg-white border border-[#EBEBE5] rounded-xl px-4 py-2">
            <span class="flex items-center gap-1.5">
              <span class="w-2 h-2 rounded-full bg-brand-500"></span>
              <strong class="text-ink font-bold">{{ projects.length }}</strong>
              projet{{ projects.length > 1 ? 's' : '' }}
            </span>
          </div>
          <button @click="startNew"
                  class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-4 py-2.5 rounded-xl transition">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
            </svg>
            Nouveau projet
          </button>
        </div>
      </div>

      <!-- LOADING -->
      <div v-if="loading" class="flex flex-col items-center justify-center py-24 gap-4">
        <div class="w-8 h-8 border-2 border-brand-500 border-t-transparent rounded-full animate-spin"></div>
        <p class="text-[12px] text-[#9C9A92]">Chargement du portfolio...</p>
      </div>

      <!-- ÉTAT VIDE -->
      <div v-else-if="projects.length === 0"
           class="bg-white rounded-2xl border-2 border-dashed border-[#EBEBE5] py-28 text-center">
        <div class="w-20 h-20 bg-gradient-to-br from-[#F4F4ED] to-[#EBEBE5] rounded-3xl flex items-center justify-center mx-auto mb-6">
          <svg class="w-10 h-10 text-[#B4B2A9]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.3">
            <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5zm10.5-11.25h.008v.008h-.008V8.25zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z"/>
          </svg>
        </div>
        <h3 class="text-[18px] font-bold text-ink mb-2">Votre portfolio est vide</h3>
        <p class="text-[13px] text-[#73726C] mb-8 max-w-sm mx-auto leading-relaxed">
          Ajoutez vos meilleurs projets pour impressionner les clients et décrocher plus de missions.
        </p>
        <button @click="startNew"
                class="inline-flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-6 py-3 rounded-xl transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
          </svg>
          Ajouter mon premier projet
        </button>
      </div>

      <!-- GRILLE PROJETS -->
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">

        <div v-for="project in projects" :key="project.id"
             class="group bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden hover:border-brand-300 hover:shadow-lg transition-all duration-200 cursor-pointer"
             @click="startEdit(project)">

          <!-- IMAGE -->
          <div class="relative h-48 bg-gradient-to-br from-[#F4F4ED] to-[#EBEBE5] overflow-hidden">
            <img v-if="project.imageUrl"
                 :src="project.imageUrl" :alt="project.title"
                 class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                 @error="e => e.target.style.display='none'"/>
            <div v-else class="absolute inset-0 flex flex-col items-center justify-center gap-2 text-[#C4C2B9]">
              <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
              </svg>
              <span class="text-[11px]">Aucune image</span>
            </div>
            <!-- OVERLAY HOVER -->
            <div class="absolute inset-0 bg-gradient-to-t from-ink/80 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-200 flex items-end justify-between p-4">
              <div class="flex gap-2">
                <a v-if="project.projectUrl" :href="project.projectUrl" target="_blank" @click.stop
                   class="text-[11px] font-semibold text-white bg-white/20 backdrop-blur-sm border border-white/30 px-3 py-1.5 rounded-lg hover:bg-white/30 transition">
                  Voir le live →
                </a>
                <a v-if="project.githubUrl" :href="project.githubUrl" target="_blank" @click.stop
                   class="text-[11px] font-semibold text-white bg-white/20 backdrop-blur-sm border border-white/30 px-3 py-1.5 rounded-lg hover:bg-white/30 transition">
                  GitHub
                </a>
              </div>
              <button @click.stop="askDelete(project)"
                      class="w-8 h-8 bg-red-600/90 hover:bg-red-600 text-white rounded-lg flex items-center justify-center transition">
                <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- INFOS -->
          <div class="p-4">
            <h3 class="text-[14px] font-bold text-ink mb-1.5 line-clamp-1 group-hover:text-brand-600 transition">{{ project.title }}</h3>
            <p v-if="project.description" class="text-[12px] text-[#73726C] line-clamp-2 mb-3 leading-relaxed">{{ project.description }}</p>
            <div v-if="project.technologies" class="flex flex-wrap gap-1.5">
              <span v-for="tech in getTechTags(project.technologies).slice(0, 4)" :key="tech"
                    class="text-[10px] bg-[#F4F4ED] text-[#5F5E5A] px-2 py-0.5 rounded-md font-semibold">{{ tech }}</span>
              <span v-if="getTechTags(project.technologies).length > 4"
                    class="text-[10px] text-[#9C9A92]">+{{ getTechTags(project.technologies).length - 4 }}</span>
            </div>
          </div>
        </div>

        <!-- CARD AJOUTER -->
        <div @click="startNew"
             class="group flex flex-col items-center justify-center min-h-[240px] bg-white rounded-2xl border-2 border-dashed border-[#EBEBE5] hover:border-brand-400 hover:bg-brand-50/40 transition-all duration-200 cursor-pointer">
          <div class="w-14 h-14 bg-[#F4F4ED] group-hover:bg-brand-100 rounded-2xl flex items-center justify-center mb-3 transition">
            <svg class="w-7 h-7 text-[#9C9A92] group-hover:text-brand-600 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
            </svg>
          </div>
          <span class="text-[13px] font-semibold text-[#73726C] group-hover:text-brand-600 transition">Ajouter un projet</span>
          <span class="text-[11px] text-[#B4B2A9] mt-1">Cliquez pour commencer</span>
        </div>

      </div>
    </div>

    <!-- ==================== VUE FORMULAIRE PLEINE PAGE ==================== -->
    <div v-else>

      <!-- BARRE DE NAVIGATION -->
      <div class="flex items-center justify-between mb-8 pb-6 border-b border-[#EBEBE5]">
        <div class="flex items-center gap-3">
          <button @click="cancelForm"
                  class="flex items-center gap-2 text-[#73726C] hover:text-ink text-[13px] font-medium transition">
            <span class="w-8 h-8 bg-white border border-[#EBEBE5] rounded-lg flex items-center justify-center hover:border-brand-400 transition">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
              </svg>
            </span>
            <span>Portfolio</span>
          </button>
          <svg class="w-4 h-4 text-[#D3D1C7]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
          <span class="text-[13px] font-bold text-ink">
            {{ editingProject ? 'Modifier le projet' : 'Nouveau projet' }}
          </span>
        </div>

        <div class="flex items-center gap-3">
          <button @click="cancelForm"
                  class="px-4 py-2 text-[13px] font-medium text-[#5F5E5A] bg-white border border-[#EBEBE5] rounded-xl hover:bg-[#F4F4ED] transition">
            Annuler
          </button>
          <button @click="saveProject" :disabled="saving"
                  class="flex items-center gap-2 px-5 py-2 text-[13px] font-semibold text-white rounded-xl transition disabled:opacity-50"
                  :class="editingProject ? 'bg-amber-500 hover:bg-amber-600' : 'bg-ink hover:bg-[#1A1A18]'">
            <svg v-if="saving" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <svg v-else class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
            </svg>
            {{ saving ? 'Enregistrement...' : editingProject ? 'Enregistrer' : 'Publier le projet' }}
          </button>
        </div>
      </div>

      <!-- LAYOUT 2 COLONNES -->
      <div class="grid grid-cols-1 xl:grid-cols-5 gap-8">

        <!-- ===== COLONNE GAUCHE : FORMULAIRE (3/5) ===== -->
        <div class="xl:col-span-3 space-y-5">

          <!-- BLOC 1 : INFORMATIONS GÉNÉRALES -->
          <div class="bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden">
            <div class="px-7 py-5 border-b border-[#EBEBE5] flex items-center gap-3">
              <span class="w-8 h-8 bg-ink rounded-lg flex items-center justify-center flex-shrink-0">
                <svg class="w-4 h-4 text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                </svg>
              </span>
              <div>
                <p class="text-[14px] font-bold text-ink">Informations générales</p>
                <p class="text-[11px] text-[#9C9A92]">Titre, description, technologies</p>
              </div>
            </div>
            <div class="p-7 space-y-6">

              <!-- TITRE -->
              <div>
                <label class="block text-[11px] font-bold text-[#5F5E5A] uppercase tracking-widest mb-2">
                  Titre du projet <span class="text-red-500 normal-case tracking-normal font-semibold">* requis</span>
                </label>
                <input v-model="form.title" type="text" maxlength="80"
                       placeholder="Ex : Plateforme e-commerce Maroc — Vue.js + Spring Boot"
                       class="w-full border-2 rounded-xl px-4 py-3 text-[14px] font-medium outline-none transition"
                       :class="formErrors.title
                         ? 'border-red-400 bg-red-50/50 text-red-900 placeholder:text-red-300'
                         : 'border-[#EBEBE5] focus:border-brand-500 text-ink'"/>
                <div class="flex items-center justify-between mt-2">
                  <p v-if="formErrors.title" class="text-red-500 text-[12px] flex items-center gap-1.5">
                    <svg class="w-3.5 h-3.5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
                    </svg>
                    {{ formErrors.title }}
                  </p>
                  <span v-else class="text-[11px] text-[#B4B2A9]">Soyez précis et accrocheur</span>
                  <span class="text-[11px] flex-shrink-0 ml-4"
                        :class="form.title.length > 70 ? 'text-amber-500 font-semibold' : 'text-[#B4B2A9]'">
                    {{ form.title.length }}/80
                  </span>
                </div>
              </div>

              <!-- DESCRIPTION -->
              <div>
                <label class="block text-[11px] font-bold text-[#5F5E5A] uppercase tracking-widest mb-2">Description</label>
                <textarea v-model="form.description" rows="5" maxlength="600"
                          placeholder="Décrivez le contexte du projet, votre rôle, les défis techniques relevés et les résultats obtenus..."
                          class="w-full border-2 border-[#EBEBE5] focus:border-brand-500 rounded-xl px-4 py-3 text-[13px] outline-none transition resize-none leading-relaxed text-ink"></textarea>
                <div class="flex items-center justify-between mt-2">
                  <span class="text-[11px]">
                    <span v-if="(form.description?.length || 0) < 50" class="text-amber-500 font-semibold">
                      {{ 50 - (form.description?.length || 0) }} caractères min. restants
                    </span>
                    <span v-else class="text-brand-600 font-semibold flex items-center gap-1">
                      <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="3">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                      </svg>
                      Longueur idéale
                    </span>
                  </span>
                  <span class="text-[11px] flex-shrink-0 ml-4"
                        :class="(form.description?.length || 0) > 550 ? 'text-red-500 font-semibold' : 'text-[#B4B2A9]'">
                    {{ form.description?.length || 0 }}/600
                  </span>
                </div>
              </div>

              <!-- TECHNOLOGIES -->
              <div>
                <label class="block text-[11px] font-bold text-[#5F5E5A] uppercase tracking-widest mb-2">Technologies utilisées</label>
                <input v-model="form.technologies" type="text"
                       placeholder="Vue.js, Spring Boot, MariaDB, Docker, Tailwind CSS..."
                       class="w-full border-2 border-[#EBEBE5] focus:border-brand-500 rounded-xl px-4 py-3 text-[13px] outline-none transition text-ink"/>
                <p class="text-[11px] text-[#B4B2A9] mt-2">Séparez chaque technologie par une virgule</p>
                <div v-if="techTags.length > 0" class="flex flex-wrap gap-2 mt-3">
                  <span v-for="tech in techTags" :key="tech"
                        class="inline-flex items-center gap-1.5 text-[12px] bg-brand-50 text-brand-700 border border-brand-200/70 px-3 py-1 rounded-lg font-semibold">
                    <span class="w-1.5 h-1.5 rounded-full bg-brand-400 flex-shrink-0"></span>
                    {{ tech }}
                  </span>
                </div>
              </div>

            </div>
          </div>

          <!-- BLOC 2 : MÉDIAS & LIENS -->
          <div class="bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden">
            <div class="px-7 py-5 border-b border-[#EBEBE5] flex items-center gap-3">
              <span class="w-8 h-8 bg-ink rounded-lg flex items-center justify-center flex-shrink-0">
                <svg class="w-4 h-4 text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"/>
                </svg>
              </span>
              <div>
                <p class="text-[14px] font-bold text-ink">Médias & Liens</p>
                <p class="text-[11px] text-[#9C9A92]">Image de couverture, lien live, code source</p>
              </div>
            </div>
            <div class="p-7 space-y-6">

              <!-- IMAGE URL + APERÇU -->
              <div>
                <label class="block text-[11px] font-bold text-[#5F5E5A] uppercase tracking-widest mb-2">
                  Image de couverture
                  <span class="normal-case tracking-normal text-[#9C9A92] font-normal ml-1">— URL publique</span>
                </label>
                <input v-model="form.imageUrl" type="url"
                       placeholder="https://exemple.com/capture-ecran.png"
                       class="w-full border-2 border-[#EBEBE5] focus:border-brand-500 rounded-xl px-4 py-3 text-[13px] outline-none transition text-ink"/>
                <p class="text-[11px] text-[#B4B2A9] mt-2">Une image de qualité augmente les vues de +40%</p>

                <!-- APERÇU IMAGE -->
                <div class="mt-4 rounded-2xl overflow-hidden border-2 border-[#EBEBE5] bg-gradient-to-br from-[#F4F4ED] to-[#EBEBE5] transition-all duration-300"
                     :class="form.imageUrl ? 'h-56' : 'h-28'">
                  <img v-if="form.imageUrl" :src="form.imageUrl" alt="aperçu"
                       class="w-full h-full object-cover"
                       @error="e => e.target.style.display='none'"/>
                  <div v-else class="h-full flex flex-col items-center justify-center gap-2 text-[#C4C2B9]">
                    <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.2">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
                    </svg>
                    <span class="text-[11px]">L'aperçu apparaîtra ici</span>
                  </div>
                </div>
              </div>

              <!-- LIENS CÔTE À CÔTE -->
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">

                <!-- LIEN LIVE -->
                <div>
                  <label class="block text-[11px] font-bold text-[#5F5E5A] uppercase tracking-widest mb-2">Lien live</label>
                  <input v-model="form.projectUrl" type="url"
                         placeholder="https://monprojet.ma"
                         class="w-full border-2 border-[#EBEBE5] focus:border-brand-500 rounded-xl px-4 py-3 text-[13px] outline-none transition text-ink"/>
                </div>

                <!-- LIEN GITHUB -->
                <div>
                  <label class="block text-[11px] font-bold text-[#5F5E5A] uppercase tracking-widest mb-2">GitHub</label>
                  <div class="relative">
                    <span class="absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none">
                      <svg class="w-4 h-4 text-[#9C9A92]" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
                      </svg>
                    </span>
                    <input v-model="form.githubUrl" type="url"
                           placeholder="https://github.com/user/repo"
                           class="w-full border-2 border-[#EBEBE5] focus:border-brand-500 rounded-xl pl-10 pr-4 py-3 text-[13px] outline-none transition text-ink"/>
                  </div>
                </div>
              </div>

            </div>
          </div>

          <!-- ACTIONS BAS DE PAGE -->
          <div class="flex items-center justify-between bg-white rounded-2xl border border-[#EBEBE5] px-7 py-4">
            <button @click="cancelForm"
                    class="text-[13px] font-medium text-[#73726C] hover:text-red-500 transition flex items-center gap-1.5">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
              </svg>
              Annuler et revenir
            </button>
            <div class="flex items-center gap-3">
              <span class="hidden sm:block text-[11px] text-[#B4B2A9]">Ctrl + S pour sauvegarder</span>
              <button @click="saveProject" :disabled="saving"
                      class="flex items-center gap-2 px-6 py-3 text-[13px] font-bold text-white rounded-xl transition disabled:opacity-50"
                      :class="editingProject ? 'bg-amber-500 hover:bg-amber-600' : 'bg-ink hover:bg-[#1A1A18]'">
                <svg v-if="saving" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
                </svg>
                <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                </svg>
                {{ saving ? 'Enregistrement...' : editingProject ? 'Enregistrer les modifications' : 'Publier le projet' }}
              </button>
            </div>
          </div>

        </div>

        <!-- ===== COLONNE DROITE : APERÇU + CONSEILS (2/5) ===== -->
        <div class="xl:col-span-2 space-y-5">

          <!-- APERÇU EN DIRECT -->
          <div class="bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden sticky top-6">
            <div class="px-5 py-4 border-b border-[#EBEBE5] flex items-center gap-2">
              <span class="w-2 h-2 rounded-full bg-brand-500 animate-pulse"></span>
              <span class="text-[13px] font-bold text-ink">Aperçu en direct</span>
              <span class="text-[11px] text-[#9C9A92] ml-auto">Visible par les clients</span>
            </div>

            <!-- CARTE PREVIEW -->
            <div class="p-4">
              <div class="rounded-xl border border-[#EBEBE5] overflow-hidden shadow-sm">

                <!-- IMAGE PREVIEW -->
                <div class="h-36 bg-gradient-to-br from-[#F4F4ED] to-[#EBEBE5] relative overflow-hidden">
                  <img v-if="form.imageUrl" :src="form.imageUrl" alt="preview"
                       class="w-full h-full object-cover"
                       @error="e => e.target.style.display='none'"/>
                  <div v-else class="absolute inset-0 flex flex-col items-center justify-center gap-1.5 text-[#C4C2B9]">
                    <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.2">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
                    </svg>
                    <span class="text-[10px]">Image de couverture</span>
                  </div>
                </div>

                <!-- CONTENU PREVIEW -->
                <div class="p-3.5">
                  <h4 class="text-[13px] font-bold text-ink mb-1 line-clamp-1">
                    {{ form.title || 'Titre du projet' }}
                  </h4>
                  <p v-if="form.description" class="text-[11px] text-[#73726C] line-clamp-2 mb-2.5 leading-relaxed">{{ form.description }}</p>
                  <p v-else class="text-[11px] text-[#C4C2B9] mb-2.5 italic">Description du projet...</p>
                  <div v-if="techTags.length > 0" class="flex flex-wrap gap-1 mb-2.5">
                    <span v-for="tech in techTags.slice(0, 3)" :key="tech"
                          class="text-[10px] bg-[#F4F4ED] text-[#5F5E5A] px-1.5 py-0.5 rounded font-medium">{{ tech }}</span>
                    <span v-if="techTags.length > 3" class="text-[10px] text-[#9C9A92]">+{{ techTags.length - 3 }}</span>
                  </div>
                  <div class="flex items-center gap-2 pt-2.5 border-t border-[#F4F4ED]">
                    <span v-if="form.projectUrl" class="text-[10px] text-brand-600 font-semibold flex items-center gap-1">
                      <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"/>
                      </svg>
                      Voir le live
                    </span>
                    <span v-if="form.githubUrl" class="text-[10px] text-[#5F5E5A] font-semibold flex items-center gap-1">
                      <svg class="w-3 h-3" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
                      </svg>
                      GitHub
                    </span>
                    <span v-if="!form.projectUrl && !form.githubUrl" class="text-[10px] text-[#C4C2B9] italic">Aucun lien ajouté</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- SCORE DE COMPLÉTUDE -->
            <div class="px-4 pb-5">
              <div class="flex items-center justify-between mb-2">
                <span class="text-[11px] font-semibold text-[#5F5E5A]">Complétude</span>
                <span class="text-[12px] font-bold tabular-nums"
                      :class="completionScore >= 80 ? 'text-brand-600' : completionScore >= 50 ? 'text-amber-500' : 'text-[#9C9A92]'">
                  {{ completionScore }}%
                </span>
              </div>
              <div class="h-2 bg-[#F4F4ED] rounded-full overflow-hidden">
                <div class="h-full rounded-full transition-all duration-500"
                     :class="completionScore >= 80 ? 'bg-brand-500' : completionScore >= 50 ? 'bg-amber-400' : 'bg-[#D3D1C7]'"
                     :style="{ width: completionScore + '%' }"></div>
              </div>
              <div class="grid grid-cols-2 gap-x-4 gap-y-1.5 mt-3">
                <div v-for="item in completionItems" :key="item.label"
                     class="flex items-center gap-1.5 text-[10px]"
                     :class="item.done ? 'text-[#5F5E5A]' : 'text-[#C4C2B9]'">
                  <svg v-if="item.done" class="w-3 h-3 text-brand-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="3">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                  </svg>
                  <svg v-else class="w-3 h-3 text-[#D3D1C7] flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                    <circle cx="12" cy="12" r="9"/>
                  </svg>
                  {{ item.label }}
                </div>
              </div>
            </div>
          </div>


        </div>
      </div>
    </div>

    <!-- ===== TOAST ===== -->
    <transition name="toast">
      <div v-if="toast.show"
           class="fixed bottom-6 right-6 z-50 px-4 py-3 rounded-xl shadow-2xl text-[13px] font-semibold flex items-center gap-2.5"
           :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
        <svg v-if="toast.type === 'success'" class="w-4 h-4 text-brand-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="3">
          <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
        </svg>
        <svg v-else class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
        </svg>
        {{ toast.message }}
      </div>
    </transition>

    <!-- ===== MODAL SUPPRESSION ===== -->
    <transition name="fade">
      <div v-if="showDeleteModal"
           class="fixed inset-0 bg-ink/60 backdrop-blur-sm z-50 flex items-center justify-center p-4"
           @click.self="showDeleteModal = false">
        <div class="bg-white rounded-2xl w-full max-w-sm p-7 shadow-2xl">
          <div class="w-14 h-14 bg-red-100 rounded-2xl flex items-center justify-center mx-auto mb-5">
            <svg class="w-7 h-7 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916"/>
            </svg>
          </div>
          <h3 class="text-[17px] font-bold text-ink text-center mb-2">Supprimer ce projet ?</h3>
          <p class="text-[13px] text-[#73726C] text-center mb-6 leading-relaxed">
            <strong class="text-ink">"{{ projectToDelete?.title }}"</strong> sera définitivement retiré de votre portfolio. Cette action est irréversible.
          </p>
          <div class="flex gap-3">
            <button @click="showDeleteModal = false"
                    class="flex-1 py-3 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-xl transition text-[#5F5E5A]">
              Annuler
            </button>
            <button @click="deleteProject" :disabled="deleting"
                    class="flex-1 py-3 bg-red-600 hover:bg-red-700 text-white text-[13px] font-semibold rounded-xl transition disabled:opacity-50 flex items-center justify-center gap-2">
              <svg v-if="deleting" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ deleting ? 'Suppression...' : 'Oui, supprimer' }}
            </button>
          </div>
        </div>
      </div>
    </transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081'

// ===== STATE =====
const loading         = ref(true)
const saving          = ref(false)
const deleting        = ref(false)
const formOpen        = ref(false)
const showDeleteModal = ref(false)
const editingProject  = ref(null)
const projectToDelete = ref(null)
const toast   = ref({ show: false, message: '', type: 'success' })
const projects = ref([])

const form = ref({
  title: '', description: '', technologies: '',
  imageUrl: '', projectUrl: '', githubUrl: '',
})
const formErrors = ref({ title: '' })


// ===== COMPUTED =====
const techTags = computed(() =>
  form.value.technologies
    ? form.value.technologies.split(',').map(t => t.trim()).filter(t => t.length > 0)
    : []
)

const completionItems = computed(() => [
  { label: 'Titre',        done: !!form.value.title.trim() },
  { label: 'Description',  done: (form.value.description?.length || 0) >= 50 },
  { label: 'Technologies', done: techTags.value.length >= 1 },
  { label: 'Image',        done: !!form.value.imageUrl },
  { label: 'Lien live',    done: !!form.value.projectUrl },
  { label: 'GitHub',       done: !!form.value.githubUrl },
])

const completionScore = computed(() => {
  const done = completionItems.value.filter(i => i.done).length
  return Math.round((done / completionItems.value.length) * 100)
})

// ===== HELPERS =====
const getTechTags = (t) => t ? t.split(',').map(s => s.trim()).filter(s => s.length > 0) : []
const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 4000)
}

const resetForm = () => {
  form.value = { title: '', description: '', technologies: '', imageUrl: '', projectUrl: '', githubUrl: '' }
  formErrors.value = { title: '' }
}

// ===== KEYBOARD SHORTCUT Ctrl+S =====
const onKeydown = (e) => {
  if (formOpen.value && (e.ctrlKey || e.metaKey) && e.key === 's') {
    e.preventDefault()
    saveProject()
  }
}

// ===== ACTIONS =====
const startNew = () => {
  editingProject.value = null
  resetForm()
  formOpen.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const startEdit = (p) => {
  editingProject.value = p
  form.value = {
    title: p.title || '',
    description: p.description || '',
    technologies: p.technologies || '',
    imageUrl: p.imageUrl || '',
    projectUrl: p.projectUrl || '',
    githubUrl: p.githubUrl || '',
  }
  formErrors.value = { title: '' }
  formOpen.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const cancelForm = () => {
  formOpen.value = false
  editingProject.value = null
  resetForm()
}

const loadPortfolio = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/portfolio`, headers())
    projects.value = data
  } catch {
    showToast('Impossible de charger le portfolio', 'error')
  } finally {
    loading.value = false
  }
}

const saveProject = async () => {
  formErrors.value = { title: '' }
  if (!form.value.title.trim()) {
    formErrors.value.title = 'Le titre est obligatoire'
    return
  }
  saving.value = true
  try {
    const body = {
      title: form.value.title.trim(),
      description: form.value.description || null,
      technologies: form.value.technologies || null,
      imageUrl: form.value.imageUrl || null,
      projectUrl: form.value.projectUrl || null,
      githubUrl: form.value.githubUrl || null,
    }
    if (editingProject.value) {
      const { data } = await axios.put(`${API_URL}/api/portfolio/${editingProject.value.id}`, body, headers())
      const i = projects.value.findIndex(p => p.id === editingProject.value.id)
      if (i !== -1) projects.value[i] = data
      showToast('Projet modifié avec succès !')
    } else {
      const { data } = await axios.post(`${API_URL}/api/portfolio`, body, headers())
      projects.value.push(data)
      showToast('Projet publié dans votre portfolio !')
    }
    cancelForm()
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la sauvegarde', 'error')
  } finally {
    saving.value = false
  }
}

const askDelete = (p) => {
  projectToDelete.value = p
  showDeleteModal.value = true
}

const deleteProject = async () => {
  deleting.value = true
  try {
    await axios.delete(`${API_URL}/api/portfolio/${projectToDelete.value.id}`, headers())
    projects.value = projects.value.filter(p => p.id !== projectToDelete.value.id)
    showDeleteModal.value = false
    showToast('Projet supprimé.')
  } catch {
    showToast('Erreur lors de la suppression', 'error')
  } finally {
    deleting.value = false
  }
}

onMounted(() => {
  loadPortfolio()
  window.addEventListener('keydown', onKeydown)
})

onUnmounted(() => {
  window.removeEventListener('keydown', onKeydown)
})
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active { transition: all 0.3s cubic-bezier(.4, 0, .2, 1); }
.toast-enter-from,
.toast-leave-to { opacity: 0; transform: translateY(10px) scale(.97); }

.fade-enter-active,
.fade-leave-active { transition: opacity 0.2s ease; }
.fade-enter-from,
.fade-leave-to { opacity: 0; }
</style>