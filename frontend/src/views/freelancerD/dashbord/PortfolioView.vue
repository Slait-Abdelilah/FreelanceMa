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

      <!-- LOADING SKELETON -->
      <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5 animate-pulse">
        <div v-for="i in 3" :key="i" class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
          <div class="h-44 bg-[#F4F4ED]"></div>
          <div class="p-4 space-y-2">
            <div class="h-4 w-3/4 bg-[#EBEBE5] rounded"></div>
            <div class="h-3 w-full bg-[#F4F4ED] rounded"></div>
            <div class="h-3 w-2/3 bg-[#F4F4ED] rounded"></div>
          </div>
        </div>
      </div>

      <!-- ÉTAT VIDE -->
      <div v-else-if="projects.length === 0"
           class="bg-white rounded-xl border border-dashed border-[#EBEBE5] py-20 text-center">
        <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
          <svg class="w-6 h-6 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
          </svg>
        </div>
        <h3 class="text-[14px] font-semibold text-ink mb-1">Portfolio vide</h3>
        <p class="text-[13px] text-[#73726C] mb-6 max-w-xs mx-auto">
          Ajoutez vos projets pour les montrer aux clients.
        </p>
        <button @click="startNew"
                class="inline-flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-5 py-2.5 rounded-lg transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
          </svg>
          Ajouter un projet
        </button>
      </div>

      <!-- GRILLE PROJETS -->
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">

        <div v-for="project in projects" :key="project.id"
             class="group bg-white rounded-xl border border-[#EBEBE5] overflow-hidden hover:border-[#D3D1C7] transition cursor-pointer"
             @click="startEdit(project)">

          <!-- IMAGE -->
          <div class="relative h-44 bg-[#F4F4ED] overflow-hidden">
            <img v-if="project.imageUrl"
                 :src="project.imageUrl" :alt="project.title"
                 class="w-full h-full object-cover"
                 @error="e => e.target.style.display='none'"/>
            <div v-else class="absolute inset-0 flex flex-col items-center justify-center gap-1.5 text-[#C4C2B9]">
              <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
              </svg>
              <span class="text-[11px]">Aucune image</span>
            </div>
            <!-- actions -->
            <div class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition">
              <button @click.stop="askDelete(project)"
                      class="w-7 h-7 bg-white border border-[#EBEBE5] text-red-500 rounded-lg flex items-center justify-center hover:bg-red-50 transition">
                <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- INFOS -->
          <div class="p-4">
            <h3 class="text-[13px] font-semibold text-ink mb-1 line-clamp-1">{{ project.title }}</h3>
            <p v-if="project.description" class="text-[12px] text-[#73726C] line-clamp-2 mb-3">{{ project.description }}</p>
            <div class="flex items-center gap-3">
              <div v-if="project.technologies" class="flex flex-wrap gap-1 flex-1 min-w-0">
                <span v-for="tech in getTechTags(project.technologies).slice(0, 3)" :key="tech"
                      class="text-[10px] border border-[#EBEBE5] text-[#5F5E5A] px-1.5 py-0.5 rounded">{{ tech }}</span>
                <span v-if="getTechTags(project.technologies).length > 3" class="text-[10px] text-[#9C9A92]">+{{ getTechTags(project.technologies).length - 3 }}</span>
              </div>
              <div class="flex gap-2 flex-shrink-0">
                <a v-if="project.projectUrl" :href="project.projectUrl" target="_blank" @click.stop
                   class="text-[10px] text-[#73726C] hover:text-ink transition">Live</a>
                <a v-if="project.githubUrl" :href="project.githubUrl" target="_blank" @click.stop
                   class="text-[10px] text-[#73726C] hover:text-ink transition">GitHub</a>
              </div>
            </div>
          </div>
        </div>

        <!-- CARD AJOUTER -->
        <div @click="startNew"
             class="flex flex-col items-center justify-center min-h-[220px] bg-white rounded-xl border border-dashed border-[#EBEBE5] hover:border-[#D3D1C7] transition cursor-pointer">
          <div class="w-10 h-10 bg-[#F4F4ED] rounded-lg flex items-center justify-center mb-2">
            <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
            </svg>
          </div>
          <span class="text-[13px] text-[#73726C]">Ajouter un projet</span>
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
          <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
            <div class="px-6 py-4 border-b border-[#EBEBE5]">
              <p class="text-[14px] font-semibold text-ink">Informations générales</p>
            </div>
            <div class="p-6 space-y-5">

              <!-- TITRE -->
              <div>
                <label class="block text-xs text-[#9C9A92] mb-1.5">
                  Titre <span class="text-red-500">*</span>
                </label>
                <input v-model="form.title" type="text" maxlength="80"
                       placeholder="Ex : Plateforme e-commerce — Vue.js + Spring Boot"
                       class="w-full border rounded-lg px-3 py-2.5 text-[13px] outline-none transition"
                       :class="formErrors.title
                         ? 'border-red-400 bg-red-50/50 text-red-900'
                         : 'border-[#EBEBE5] focus:border-[#5F5E5A] text-ink'"/>
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
                <label class="block text-xs text-[#9C9A92] mb-1.5">Description</label>
                <textarea v-model="form.description" rows="5" maxlength="600"
                          placeholder="Contexte, rôle, défis techniques, résultats..."
                          class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition resize-none leading-relaxed text-ink"></textarea>
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
                <label class="block text-xs text-[#9C9A92] mb-1.5">Technologies</label>
                <input v-model="form.technologies" type="text"
                       placeholder="Vue.js, Spring Boot, Docker..."
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition text-ink"/>
                <p class="text-[11px] text-[#9C9A92] mt-1.5">Séparées par des virgules</p>
                <div v-if="techTags.length > 0" class="flex flex-wrap gap-1.5 mt-2">
                  <span v-for="tech in techTags" :key="tech"
                        class="text-[11px] border border-[#EBEBE5] text-[#5F5E5A] px-2 py-0.5 rounded">
                    {{ tech }}
                  </span>
                </div>
              </div>

            </div>
          </div>

          <!-- BLOC 2 : MÉDIAS & LIENS -->
          <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
            <div class="px-6 py-4 border-b border-[#EBEBE5]">
              <p class="text-[14px] font-semibold text-ink">Médias & Liens</p>
            </div>
            <div class="p-6 space-y-5">

              <!-- IMAGE URL + APERÇU -->
              <div>
                <label class="block text-xs text-[#9C9A92] mb-1.5">Image de couverture (URL)</label>
                <input v-model="form.imageUrl" type="url"
                       placeholder="https://exemple.com/screenshot.png"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition text-ink"/>

                <!-- APERÇU IMAGE -->
                <div class="mt-3 rounded-lg overflow-hidden border border-[#EBEBE5] bg-[#F4F4ED]"
                     :class="form.imageUrl ? 'h-44' : 'h-20'">
                  <img v-if="form.imageUrl" :src="form.imageUrl" alt="aperçu"
                       class="w-full h-full object-cover"
                       @error="e => e.target.style.display='none'"/>
                  <div v-else class="h-full flex items-center justify-center gap-2 text-[#C4C2B9]">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
                    </svg>
                    <span class="text-[11px]">Aperçu</span>
                  </div>
                </div>
              </div>

              <!-- LIENS -->
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label class="block text-xs text-[#9C9A92] mb-1.5">Lien live</label>
                  <input v-model="form.projectUrl" type="url"
                         placeholder="https://monprojet.ma"
                         class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition text-ink"/>
                </div>
                <div>
                  <label class="block text-xs text-[#9C9A92] mb-1.5">GitHub</label>
                  <input v-model="form.githubUrl" type="url"
                         placeholder="https://github.com/user/repo"
                         class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition text-ink"/>
                </div>
              </div>

            </div>
          </div>

          <!-- ACTIONS BAS DE PAGE -->
          <div class="flex items-center justify-between bg-white rounded-xl border border-[#EBEBE5] px-6 py-4">
            <button @click="cancelForm" class="text-[13px] text-[#73726C] hover:text-ink transition">
              Annuler
            </button>
            <div class="flex items-center gap-3">
              <span class="hidden sm:block text-[11px] text-[#9C9A92]">Ctrl + S</span>
              <button @click="saveProject" :disabled="saving"
                      class="flex items-center gap-2 px-5 py-2 text-[13px] font-semibold text-white rounded-lg transition disabled:opacity-50 bg-ink hover:bg-[#1A1A18]">
                <svg v-if="saving" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
                </svg>
                {{ saving ? 'Enregistrement...' : editingProject ? 'Enregistrer' : 'Publier' }}
              </button>
            </div>
          </div>

        </div>

        <!-- ===== COLONNE DROITE : APERÇU + CONSEILS (2/5) ===== -->
        <div class="xl:col-span-2 space-y-5">

          <!-- APERÇU EN DIRECT -->
          <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden sticky top-6">
            <div class="px-5 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
              <span class="text-[13px] font-semibold text-ink">Aperçu</span>
              <span class="text-[11px] text-[#9C9A92]">Visible par les clients</span>
            </div>

            <!-- CARTE PREVIEW -->
            <div class="p-4">
              <div class="rounded-xl border border-[#EBEBE5] overflow-hidden shadow-sm">

                <!-- IMAGE PREVIEW -->
                <div class="h-36 bg-[#F4F4ED] relative overflow-hidden">
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
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast.show"
           class="fixed bottom-6 right-6 z-50 px-4 py-3 rounded-lg border text-[13px] font-medium flex items-center gap-2"
           :class="toast.type === 'success' ? 'bg-ink text-white border-transparent' : 'bg-white text-red-600 border-red-200'">
        <svg v-if="toast.type === 'success'" class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 13l4 4L19 7"/>
        </svg>
        <svg v-else class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
        </svg>
        {{ toast.message }}
      </div>
    </Transition>

    <!-- ===== MODAL SUPPRESSION ===== -->
    <div v-if="showDeleteModal"
         class="fixed inset-0 bg-ink/50 z-50 flex items-center justify-center p-4"
         @click.self="showDeleteModal = false">
      <div class="bg-white rounded-xl w-full max-w-sm p-6 border border-[#EBEBE5]">
        <h3 class="text-[15px] font-semibold text-ink mb-2">Supprimer ce projet ?</h3>
        <p class="text-[13px] text-[#73726C] mb-5">
          <span class="font-medium text-ink">"{{ projectToDelete?.title }}"</span> sera définitivement retiré. Action irréversible.
        </p>
        <div class="flex gap-3">
          <button @click="showDeleteModal = false"
                  class="flex-1 py-2.5 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-lg transition">
            Annuler
          </button>
          <button @click="deleteProject" :disabled="deleting"
                  class="flex-1 py-2.5 bg-red-600 hover:bg-red-700 text-white text-[13px] font-semibold rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
            <svg v-if="deleting" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ deleting ? 'Suppression...' : 'Supprimer' }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

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
const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token') || sessionStorage.getItem('token')}` } })

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

