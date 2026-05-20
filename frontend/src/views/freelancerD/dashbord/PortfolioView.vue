<template>
  <div class="space-y-6 pb-8">

    <!-- ══════════════════ VUE LISTE ══════════════════ -->
    <template v-if="!formOpen">

      <!-- HEADER -->
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-[20px] font-semibold text-ink tracking-tight">Portfolio</h1>
          <p class="text-[13px] text-[#9C9A92] mt-0.5">
            {{ loading ? '…' : `${projects.length} projet${projects.length !== 1 ? 's' : ''} publiés` }}
          </p>
        </div>
        <button @click="startNew"
                class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-4 py-2.5 rounded-xl transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
          </svg>
          Nouveau projet
        </button>
      </div>

      <!-- SKELETON -->
      <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
        <div v-for="i in 3" :key="i" class="bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden animate-pulse">
          <div class="h-52 bg-[#F4F4ED]"></div>
          <div class="p-5 space-y-3">
            <div class="h-4 w-3/4 bg-[#EBEBE5] rounded-lg"></div>
            <div class="h-3 w-full bg-[#F4F4ED] rounded"></div>
            <div class="h-3 w-2/3 bg-[#F4F4ED] rounded"></div>
            <div class="flex gap-1.5 pt-1">
              <div class="h-6 w-16 bg-[#F4F4ED] rounded-lg"></div>
              <div class="h-6 w-20 bg-[#F4F4ED] rounded-lg"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- EMPTY -->
      <div v-else-if="projects.length === 0"
           class="bg-white rounded-2xl border border-dashed border-[#EBEBE5] py-20 text-center">
        <div class="w-14 h-14 bg-[#F4F4ED] rounded-2xl flex items-center justify-center mx-auto mb-5">
          <svg class="w-7 h-7 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
          </svg>
        </div>
        <h3 class="text-[15px] font-semibold text-ink mb-1.5">Portfolio vide</h3>
        <p class="text-[13px] text-[#9C9A92] mb-7 max-w-xs mx-auto">
          Ajoutez vos meilleurs projets pour impressionner les clients.
        </p>
        <button @click="startNew"
                class="inline-flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-5 py-2.5 rounded-xl transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
          </svg>
          Ajouter un projet
        </button>
      </div>

      <!-- GRILLE -->
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">

        <div v-for="project in projects" :key="project.id"
             class="group bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden hover:border-[#D3D1C7] hover:shadow-md transition cursor-pointer"
             @click="startEdit(project)">

          <!-- IMAGE -->
          <div class="relative h-52 bg-[#F4F4ED] overflow-hidden">
            <img v-if="project.imageUrl" :src="project.imageUrl" :alt="project.title"
                 class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                 @error="e => e.target.style.display='none'"/>
            <div v-else class="absolute inset-0 flex flex-col items-center justify-center gap-2 text-[#C4C2B9]">
              <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
              </svg>
              <span class="text-[12px]">Aucune image</span>
            </div>
            <!-- Bouton supprimer au hover -->
            <div class="absolute top-3 right-3 opacity-0 group-hover:opacity-100 transition">
              <button @click.stop="askDelete(project)"
                      class="w-8 h-8 bg-white/90 backdrop-blur-sm border border-[#EBEBE5] text-red-500 rounded-xl flex items-center justify-center hover:bg-red-50 hover:border-red-200 transition shadow-sm">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- INFOS -->
          <div class="p-5">
            <h3 class="text-[15px] font-semibold text-ink mb-1.5 line-clamp-1">{{ project.title }}</h3>
            <p v-if="project.description" class="text-[13px] text-[#73726C] line-clamp-2 mb-4 leading-relaxed">{{ project.description }}</p>

            <div class="flex items-center justify-between">
              <div v-if="project.technologies" class="flex flex-wrap gap-1.5">
                <span v-for="tech in getTechTags(project.technologies).slice(0, 3)" :key="tech"
                      class="text-[11px] font-medium bg-[#F4F4ED] text-[#5F5E5A] border border-[#EBEBE5] px-2.5 py-1 rounded-lg">{{ tech }}</span>
                <span v-if="getTechTags(project.technologies).length > 3"
                      class="text-[11px] text-[#9C9A92] self-center">+{{ getTechTags(project.technologies).length - 3 }}</span>
              </div>
              <div class="flex gap-2 flex-shrink-0 ml-auto">
                <a v-if="project.projectUrl" :href="project.projectUrl" target="_blank" @click.stop
                   class="text-[12px] font-medium text-[#5F5E5A] hover:text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-2.5 py-1 rounded-lg transition">
                  Live ↗
                </a>
                <a v-if="project.githubUrl" :href="project.githubUrl" target="_blank" @click.stop
                   class="text-[12px] font-medium text-[#5F5E5A] hover:text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-2.5 py-1 rounded-lg transition">
                  GitHub
                </a>
              </div>
            </div>
          </div>
        </div>

        <!-- CARD AJOUTER -->
        <div @click="startNew"
             class="flex flex-col items-center justify-center min-h-[280px] bg-white rounded-2xl border border-dashed border-[#EBEBE5] hover:border-[#D3D1C7] hover:bg-[#FAFAF7] transition cursor-pointer group">
          <div class="w-12 h-12 bg-[#F4F4ED] group-hover:bg-white rounded-xl flex items-center justify-center mb-3 transition">
            <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
            </svg>
          </div>
          <span class="text-[13px] font-medium text-[#9C9A92] group-hover:text-ink transition">Ajouter un projet</span>
        </div>

      </div>
    </template>

    <!-- ══════════════════ VUE FORMULAIRE ══════════════════ -->
    <template v-else>

      <!-- NAV BREADCRUMB -->
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-2">
          <button @click="cancelForm"
                  class="flex items-center gap-2 text-[13px] font-medium text-[#73726C] hover:text-ink transition">
            <span class="w-8 h-8 bg-white border border-[#EBEBE5] hover:border-[#D3D1C7] rounded-xl flex items-center justify-center transition">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
              </svg>
            </span>
            Portfolio
          </button>
          <svg class="w-4 h-4 text-[#D3D1C7]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
          </svg>
          <span class="text-[14px] font-semibold text-ink">
            {{ editingProject ? 'Modifier le projet' : 'Nouveau projet' }}
          </span>
        </div>
        <div class="flex items-center gap-3">
          <button @click="cancelForm"
                  class="text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2.5 rounded-xl transition">
            Annuler
          </button>
          <button @click="saveProject" :disabled="saving"
                  class="flex items-center gap-2 text-[13px] font-semibold text-white px-5 py-2.5 rounded-xl transition disabled:opacity-50 bg-ink hover:bg-[#1A1A18]">
            <svg v-if="saving" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
            </svg>
            {{ saving ? 'Enregistrement…' : editingProject ? 'Enregistrer' : 'Publier' }}
          </button>
        </div>
      </div>

      <!-- LAYOUT 2 COLONNES -->
      <div class="grid grid-cols-1 xl:grid-cols-5 gap-5">

        <!-- COLONNE FORMULAIRE (3/5) -->
        <div class="xl:col-span-3 space-y-4">

          <!-- Infos générales -->
          <div class="bg-white rounded-2xl border border-[#EBEBE5] p-6 space-y-5">
            <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Informations générales</p>

            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">
                Titre <span class="text-red-400">*</span>
              </label>
              <input v-model="form.title" type="text" maxlength="80"
                     placeholder="Ex : Plateforme e-commerce — Vue.js + Spring Boot"
                     class="w-full border rounded-xl px-4 py-3 text-[14px] outline-none transition"
                     :class="formErrors.title
                       ? 'border-red-300 bg-red-50 text-red-900'
                       : 'border-[#EBEBE5] focus:border-[#5F5E5A] text-ink'"/>
              <div class="flex items-center justify-between mt-1.5">
                <p v-if="formErrors.title" class="text-[12px] text-red-500">{{ formErrors.title }}</p>
                <span v-else class="text-[12px] text-[#C4C3BC]">Soyez précis et accrocheur</span>
                <span class="text-[12px] flex-shrink-0 ml-4"
                      :class="form.title.length > 70 ? 'text-amber-500 font-semibold' : 'text-[#C4C3BC]'">
                  {{ form.title.length }}/80
                </span>
              </div>
            </div>

            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Description</label>
              <textarea v-model="form.description" rows="5" maxlength="600"
                        placeholder="Contexte du projet, votre rôle, défis techniques, résultats obtenus..."
                        class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition resize-none leading-relaxed placeholder:text-[#C4C3BC]">
              </textarea>
              <div class="flex items-center justify-between mt-1.5">
                <span class="text-[12px]"
                      :class="(form.description?.length || 0) < 50 ? 'text-amber-500' : 'text-green-600 font-medium'">
                  {{ (form.description?.length || 0) < 50
                    ? `Encore ${50 - (form.description?.length || 0)} caractères min.`
                    : '✓ Longueur idéale' }}
                </span>
                <span class="text-[12px] flex-shrink-0 ml-4"
                      :class="(form.description?.length || 0) > 550 ? 'text-red-500 font-semibold' : 'text-[#C4C3BC]'">
                  {{ form.description?.length || 0 }}/600
                </span>
              </div>
            </div>

            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Technologies</label>
              <input v-model="form.technologies" type="text"
                     placeholder="Vue.js, Spring Boot, Docker, MySQL…"
                     class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
              <p class="text-[12px] text-[#C4C3BC] mt-1.5">Séparées par des virgules</p>
              <div v-if="techTags.length" class="flex flex-wrap gap-2 mt-3">
                <span v-for="tech in techTags" :key="tech"
                      class="text-[13px] font-medium bg-[#F4F4ED] text-[#5F5E5A] border border-[#EBEBE5] px-3 py-1.5 rounded-lg">
                  {{ tech }}
                </span>
              </div>
            </div>
          </div>

          <!-- Médias & Liens -->
          <div class="bg-white rounded-2xl border border-[#EBEBE5] p-6 space-y-5">
            <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Médias & Liens</p>

            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Image de couverture (URL)</label>
              <input v-model="form.imageUrl" type="url"
                     placeholder="https://exemple.com/screenshot.png"
                     class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
              <div class="mt-3 rounded-xl overflow-hidden border border-[#EBEBE5] bg-[#F4F4ED]"
                   :class="form.imageUrl ? 'h-52' : 'h-16'">
                <img v-if="form.imageUrl" :src="form.imageUrl" alt="aperçu"
                     class="w-full h-full object-cover"
                     @error="e => e.target.style.display='none'"/>
                <div v-else class="h-full flex items-center justify-center gap-2 text-[#C4C2B9]">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
                  </svg>
                  <span class="text-[12px]">Aperçu de l'image</span>
                </div>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Lien live</label>
                <input v-model="form.projectUrl" type="url" placeholder="https://monprojet.ma"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
              </div>
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">GitHub</label>
                <input v-model="form.githubUrl" type="url" placeholder="https://github.com/user/repo"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
              </div>
            </div>
          </div>

        </div>

        <!-- COLONNE APERÇU (2/5) -->
        <div class="xl:col-span-2">
          <div class="bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden sticky top-6">

            <div class="px-5 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
              <span class="text-[13px] font-semibold text-ink">Aperçu client</span>
              <span class="text-[11px] text-[#9C9A92]">Mise à jour en direct</span>
            </div>

            <!-- Preview carte -->
            <div class="p-4">
              <div class="rounded-xl border border-[#EBEBE5] overflow-hidden">
                <div class="h-44 bg-[#F4F4ED] relative overflow-hidden">
                  <img v-if="form.imageUrl" :src="form.imageUrl" alt="preview"
                       class="w-full h-full object-cover"
                       @error="e => e.target.style.display='none'"/>
                  <div v-else class="absolute inset-0 flex flex-col items-center justify-center gap-1.5 text-[#C4C2B9]">
                    <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.2">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
                    </svg>
                    <span class="text-[11px]">Image de couverture</span>
                  </div>
                </div>
                <div class="p-4">
                  <h4 class="text-[14px] font-semibold text-ink mb-1 line-clamp-1">
                    {{ form.title || 'Titre du projet' }}
                  </h4>
                  <p class="text-[12px] line-clamp-2 mb-3 leading-relaxed"
                     :class="form.description ? 'text-[#73726C]' : 'text-[#C4C2B9] italic'">
                    {{ form.description || 'Description du projet…' }}
                  </p>
                  <div v-if="techTags.length" class="flex flex-wrap gap-1 mb-3">
                    <span v-for="tech in techTags.slice(0, 3)" :key="tech"
                          class="text-[10px] font-medium bg-[#F4F4ED] text-[#5F5E5A] px-2 py-0.5 rounded-md">{{ tech }}</span>
                    <span v-if="techTags.length > 3" class="text-[10px] text-[#9C9A92]">+{{ techTags.length - 3 }}</span>
                  </div>
                  <div class="flex items-center gap-2 pt-3 border-t border-[#F4F4ED]">
                    <span v-if="form.projectUrl" class="text-[11px] font-semibold text-ink border border-[#EBEBE5] px-2 py-1 rounded-md">Live ↗</span>
                    <span v-if="form.githubUrl" class="text-[11px] font-semibold text-[#5F5E5A] border border-[#EBEBE5] px-2 py-1 rounded-md">GitHub</span>
                    <span v-if="!form.projectUrl && !form.githubUrl" class="text-[11px] text-[#C4C2B9] italic">Aucun lien</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- Score complétude -->
            <div class="px-5 pb-5 space-y-3">
              <div class="flex items-center justify-between">
                <span class="text-[12px] font-semibold text-ink">Complétude</span>
                <span class="text-[13px] font-bold tabular-nums"
                      :class="completionScore >= 80 ? 'text-green-600' : completionScore >= 50 ? 'text-amber-500' : 'text-[#9C9A92]'">
                  {{ completionScore }}%
                </span>
              </div>
              <div class="h-2 bg-[#F4F4ED] rounded-full overflow-hidden">
                <div class="h-full rounded-full transition-all duration-500"
                     :class="completionScore >= 80 ? 'bg-green-500' : completionScore >= 50 ? 'bg-amber-400' : 'bg-[#D3D1C7]'"
                     :style="{ width: completionScore + '%' }">
                </div>
              </div>
              <div class="grid grid-cols-2 gap-x-4 gap-y-2">
                <div v-for="item in completionItems" :key="item.label"
                     class="flex items-center gap-2 text-[12px]"
                     :class="item.done ? 'text-[#5F5E5A]' : 'text-[#C4C3BC]'">
                  <svg v-if="item.done" class="w-3.5 h-3.5 text-green-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="3">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                  </svg>
                  <span v-else class="w-3.5 h-3.5 rounded-full border-2 border-[#D3D1C7] flex-shrink-0"></span>
                  {{ item.label }}
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </template>

    <!-- MODAL SUPPRESSION -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="showDeleteModal" class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4"
           @click.self="showDeleteModal = false">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl w-full max-w-sm p-6" @click.stop>
          <p class="text-[15px] font-semibold text-ink">Supprimer ce projet ?</p>
          <p class="text-[13px] text-[#73726C] mt-2">
            <span class="font-medium text-ink">"{{ projectToDelete?.title }}"</span> sera définitivement retiré.
          </p>
          <div class="flex gap-3 mt-5">
            <button @click="showDeleteModal = false"
                    class="flex-1 py-2.5 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] rounded-lg transition">
              Annuler
            </button>
            <button @click="deleteProject" :disabled="deleting"
                    class="flex-1 py-2.5 text-[13px] font-semibold bg-red-600 hover:bg-red-700 text-white rounded-lg transition disabled:opacity-50">
              {{ deleting ? '…' : 'Supprimer' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
    </Teleport>

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
import { ref, computed, onMounted, onUnmounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const loading         = ref(true)
const saving          = ref(false)
const deleting        = ref(false)
const formOpen        = ref(false)
const showDeleteModal = ref(false)
const editingProject  = ref(null)
const projectToDelete = ref(null)
const toast           = ref({ show: false, message: '', type: 'success' })
const projects        = ref([])

const form = ref({ title: '', description: '', technologies: '', imageUrl: '', projectUrl: '', githubUrl: '' })
const formErrors = ref({ title: '' })

const techTags = computed(() =>
  form.value.technologies
    ? form.value.technologies.split(',').map(t => t.trim()).filter(Boolean)
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

const getTechTags = (t) => t ? t.split(',').map(s => s.trim()).filter(Boolean) : []
const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token') || sessionStorage.getItem('token')}` } })

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 4000)
}

const resetForm = () => {
  form.value = { title: '', description: '', technologies: '', imageUrl: '', projectUrl: '', githubUrl: '' }
  formErrors.value = { title: '' }
}

const onKeydown = (e) => {
  if (formOpen.value && (e.ctrlKey || e.metaKey) && e.key === 's') {
    e.preventDefault()
    saveProject()
  }
}

const startNew = () => {
  editingProject.value = null
  resetForm()
  formOpen.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const startEdit = (p) => {
  editingProject.value = p
  form.value = { title: p.title || '', description: p.description || '', technologies: p.technologies || '', imageUrl: p.imageUrl || '', projectUrl: p.projectUrl || '', githubUrl: p.githubUrl || '' }
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
  if (!form.value.title.trim()) { formErrors.value.title = 'Le titre est obligatoire'; return }
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
      showToast('Projet modifié !')
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

onMounted(() => { loadPortfolio(); window.addEventListener('keydown', onKeydown) })
onUnmounted(() => { window.removeEventListener('keydown', onKeydown) })
</script>
