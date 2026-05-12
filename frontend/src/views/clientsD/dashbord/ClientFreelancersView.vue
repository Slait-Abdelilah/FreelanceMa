<template>
  <div class="max-w-6xl mx-auto space-y-5">

    <!-- EN-TÊTE -->
    <div>
      <h1 class="text-[20px] font-semibold text-ink tracking-tight">Explorer les freelancers</h1>
      <p class="text-[13px] text-[#9C9A92] mt-0.5">{{ total }} freelancer{{ total !== 1 ? 's' : '' }} disponible{{ total !== 1 ? 's' : '' }}</p>
    </div>

    <!-- BARRE RECHERCHE + FILTRES -->
    <div class="bg-white rounded-xl border border-[#EBEBE5] p-4 space-y-3">

      <div class="relative">
        <svg class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[#9C9A92]"
             fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
        </svg>
        <input v-model="searchQuery" @input="onSearch" type="text"
               placeholder="Rechercher par nom, compétences, titre, ville..."
               class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg pl-10 pr-10 py-2.5 text-[13px] outline-none transition"/>
        <button v-if="searchQuery" @click="clearSearch"
                class="absolute right-3.5 top-1/2 -translate-y-1/2 text-[#9C9A92] hover:text-ink">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>

      <div class="flex items-center gap-3 flex-wrap">

        <select v-model="filterAvailable"
                class="text-[12px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-2 outline-none focus:border-[#5F5E5A] transition">
          <option value="">Tous</option>
          <option value="true">Disponibles uniquement</option>
        </select>

        <select v-model="filterLevel"
                class="text-[12px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-2 outline-none focus:border-[#5F5E5A] transition">
          <option value="">Tous niveaux</option>
          <option value="JUNIOR">Junior</option>
          <option value="MID">Intermédiaire</option>
          <option value="SENIOR">Senior</option>
          <option value="EXPERT">Expert</option>
        </select>

        <select v-model="sortBy"
                class="text-[12px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-2 outline-none focus:border-[#5F5E5A] transition">
          <option value="name">Nom A–Z</option>
          <option value="rate_asc">Tarif croissant</option>
          <option value="rate_desc">Tarif décroissant</option>
          <option value="missions">Missions complétées</option>
        </select>

        <button v-if="filterAvailable || filterLevel || sortBy !== 'name'"
                @click="resetFilters"
                class="text-[12px] font-medium text-[#73726C] hover:text-ink flex items-center gap-1 transition">
          <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
          </svg>
          Réinitialiser
        </button>

        <!-- toggle grille / liste -->
        <div class="ml-auto flex items-center gap-1">
          <button @click="viewMode = 'grid'" class="p-1.5 rounded-md transition"
                  :class="viewMode === 'grid' ? 'bg-ink text-white' : 'hover:bg-[#F4F4ED] text-[#73726C]'">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6A2.25 2.25 0 016 3.75h2.25A2.25 2.25 0 0110.5 6v2.25a2.25 2.25 0 01-2.25 2.25H6a2.25 2.25 0 01-2.25-2.25V6zM3.75 15.75A2.25 2.25 0 016 13.5h2.25a2.25 2.25 0 012.25 2.25V18a2.25 2.25 0 01-2.25 2.25H6A2.25 2.25 0 013.75 18v-2.25zM13.5 6a2.25 2.25 0 012.25-2.25H18A2.25 2.25 0 0120.25 6v2.25A2.25 2.25 0 0118 10.5h-2.25a2.25 2.25 0 01-2.25-2.25V6zM13.5 15.75a2.25 2.25 0 012.25-2.25H18a2.25 2.25 0 012.25 2.25V18A2.25 2.25 0 0118 20.25h-2.25A2.25 2.25 0 0113.5 18v-2.25z"/>
            </svg>
          </button>
          <button @click="viewMode = 'list'" class="p-1.5 rounded-md transition"
                  :class="viewMode === 'list' ? 'bg-ink text-white' : 'hover:bg-[#F4F4ED] text-[#73726C]'">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zM3.75 12h.007v.008H3.75V12zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm-.375 5.25h.007v.008H3.75v-.008zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z"/>
            </svg>
          </button>
        </div>

      </div>
    </div>

    <!-- SKELETON -->
    <div v-if="loading">
      <div v-if="viewMode === 'grid'" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
        <div v-for="i in 6" :key="i" class="bg-white rounded-xl border border-[#EBEBE5] p-5 animate-pulse">
          <div class="flex items-center gap-3 mb-4">
            <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex-shrink-0"></div>
            <div class="flex-1 space-y-2">
              <div class="h-3.5 bg-[#F4F4ED] rounded w-2/3"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-1/2"></div>
            </div>
          </div>
          <div class="h-3 bg-[#F4F4ED] rounded mb-2"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-4/5 mb-4"></div>
          <div class="flex gap-1.5">
            <div class="h-5 w-14 bg-[#F4F4ED] rounded-md"></div>
            <div class="h-5 w-16 bg-[#F4F4ED] rounded-md"></div>
          </div>
        </div>
      </div>
      <div v-else class="space-y-3">
        <div v-for="i in 5" :key="i" class="bg-white rounded-xl border border-[#EBEBE5] p-5 animate-pulse flex items-center gap-4">
          <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex-shrink-0"></div>
          <div class="flex-1 space-y-2">
            <div class="h-3.5 bg-[#F4F4ED] rounded w-1/4"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-1/2"></div>
          </div>
          <div class="h-3 bg-[#F4F4ED] rounded w-20"></div>
        </div>
      </div>
    </div>

    <!-- AUCUN RÉSULTAT -->
    <div v-else-if="filtered.length === 0"
         class="bg-white rounded-xl border border-[#EBEBE5] py-16 text-center">
      <div class="w-14 h-14 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-7 h-7 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/>
        </svg>
      </div>
      <p class="text-[15px] font-semibold text-ink">Aucun freelancer trouvé</p>
      <p class="text-[13px] text-[#9C9A92] mt-1">Essayez d'autres mots-clés ou réinitialisez les filtres</p>
      <button @click="clearSearch" class="mt-4 text-[13px] font-medium text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
        Voir tous les freelancers
      </button>
    </div>

    <!-- VUE GRILLE -->
    <div v-else-if="viewMode === 'grid'" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
      <div v-for="f in filtered" :key="f.id"
           @click="openProfile(f)"
           class="bg-white rounded-xl border border-[#EBEBE5] p-5 cursor-pointer hover:border-[#D3D1C7] hover:shadow-sm transition group">

        <!-- Avatar + nom -->
        <div class="flex items-start gap-3 mb-3">
          <div class="w-12 h-12 rounded-xl flex items-center justify-center text-[16px] font-bold text-ink flex-shrink-0"
               :style="{ background: avatarColor(f.id) }">
            {{ initials(f) }}
          </div>
          <div class="flex-1 min-w-0">
            <div class="flex items-center gap-2">
              <p class="text-[14px] font-semibold text-ink truncate group-hover:text-[#1A1A18]">
                {{ fullName(f) }}
              </p>
              <span v-if="f.isAvailable"
                    class="w-1.5 h-1.5 rounded-full bg-green-400 flex-shrink-0" title="Disponible"></span>
            </div>
            <p class="text-[12px] text-[#73726C] truncate">{{ f.title || 'Freelancer' }}</p>
            <p v-if="f.location" class="text-[11px] text-[#9C9A92] flex items-center gap-1 mt-0.5">
              <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z"/>
                <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z"/>
              </svg>
              {{ f.location }}
            </p>
          </div>
          <div v-if="f.hourlyRate" class="text-right flex-shrink-0">
            <div class="text-[13px] font-bold text-ink">{{ f.hourlyRate }} DH</div>
            <div class="text-[10px] text-[#9C9A92]">/heure</div>
          </div>
        </div>

        <!-- Bio -->
        <p v-if="f.bio" class="text-[12px] text-[#73726C] leading-relaxed line-clamp-2 mb-3">
          {{ f.bio }}
        </p>

        <!-- Skills -->
        <div v-if="f.skills" class="flex flex-wrap gap-1.5 mb-3">
          <span v-for="skill in skillTags(f.skills).slice(0, 4)" :key="skill"
                class="text-[11px] bg-[#F4F4ED] text-ink px-2 py-0.5 rounded-md">
            {{ skill }}
          </span>
          <span v-if="skillTags(f.skills).length > 4" class="text-[11px] text-[#9C9A92]">
            +{{ skillTags(f.skills).length - 4 }}
          </span>
        </div>

        <!-- Footer -->
        <div class="flex items-center justify-between pt-3 border-t border-[#EBEBE5]">
          <div class="flex items-center gap-3 text-[11px] text-[#9C9A92]">
            <span v-if="f.completedMissions" class="flex items-center gap-1">
              <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              {{ f.completedMissions }} mission{{ f.completedMissions !== 1 ? 's' : '' }}
            </span>
            <span v-if="f.experienceLevel" :class="levelClass(f.experienceLevel)"
                  class="text-[10px] font-semibold px-2 py-0.5 rounded-full border">
              {{ levelLabel(f.experienceLevel) }}
            </span>
          </div>
          <div v-if="f.averageRating" class="flex items-center gap-1 text-[11px] text-amber-600 font-semibold">
            <svg class="w-3 h-3 fill-amber-400" viewBox="0 0 24 24">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
            </svg>
            {{ f.averageRating.toFixed(1) }}
          </div>
        </div>

      </div>
    </div>

    <!-- VUE LISTE -->
    <div v-else class="space-y-3">
      <div v-for="f in filtered" :key="f.id"
           @click="openProfile(f)"
           class="bg-white rounded-xl border border-[#EBEBE5] p-5 cursor-pointer hover:border-[#D3D1C7] hover:shadow-sm transition group flex items-center gap-4">

        <div class="w-12 h-12 rounded-xl flex items-center justify-center text-[16px] font-bold text-ink flex-shrink-0"
             :style="{ background: avatarColor(f.id) }">
          {{ initials(f) }}
        </div>

        <div class="flex-1 min-w-0">
          <div class="flex items-center gap-2">
            <p class="text-[14px] font-semibold text-ink">{{ fullName(f) }}</p>
            <span v-if="f.isAvailable" class="w-1.5 h-1.5 rounded-full bg-green-400" title="Disponible"></span>
            <span v-if="f.experienceLevel" :class="levelClass(f.experienceLevel)"
                  class="text-[10px] font-semibold px-2 py-0.5 rounded-full border">
              {{ levelLabel(f.experienceLevel) }}
            </span>
          </div>
          <p class="text-[12px] text-[#73726C]">{{ f.title || 'Freelancer' }}
            <span v-if="f.location" class="text-[#9C9A92]"> · {{ f.location }}</span>
          </p>
          <div v-if="f.skills" class="flex flex-wrap gap-1.5 mt-1.5">
            <span v-for="skill in skillTags(f.skills).slice(0, 5)" :key="skill"
                  class="text-[11px] bg-[#F4F4ED] text-ink px-2 py-0.5 rounded-md">
              {{ skill }}
            </span>
          </div>
        </div>

        <div class="text-right flex-shrink-0">
          <div v-if="f.hourlyRate" class="text-[14px] font-bold text-ink">{{ f.hourlyRate }} DH<span class="text-[11px] font-normal text-[#9C9A92]">/h</span></div>
          <div v-if="f.completedMissions" class="text-[11px] text-[#9C9A92]">{{ f.completedMissions }} mission{{ f.completedMissions !== 1 ? 's' : '' }}</div>
          <div v-if="f.averageRating" class="flex items-center justify-end gap-1 text-[11px] text-amber-600 font-semibold mt-0.5">
            <svg class="w-3 h-3 fill-amber-400" viewBox="0 0 24 24">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
            </svg>
            {{ f.averageRating.toFixed(1) }}
          </div>
        </div>

      </div>
    </div>

    <!-- MODAL PROFIL -->
    <div v-if="selected"
         class="fixed inset-0 bg-ink/50 z-50 flex items-end md:items-center justify-center p-0 md:p-4"
         @click.self="selected = null">

      <div class="bg-white rounded-t-2xl md:rounded-2xl w-full md:max-w-lg shadow-2xl max-h-[90vh] overflow-y-auto">

        <!-- header -->
        <div class="sticky top-0 bg-white px-6 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
          <div class="flex items-center gap-3">
            <div class="w-11 h-11 rounded-xl flex items-center justify-center text-[15px] font-bold text-ink flex-shrink-0"
                 :style="{ background: avatarColor(selected.id) }">
              {{ initials(selected) }}
            </div>
            <div>
              <div class="flex items-center gap-2">
                <p class="text-[15px] font-semibold text-ink">{{ fullName(selected) }}</p>
                <span v-if="selected.isAvailable"
                      class="text-[10px] font-semibold text-green-700 bg-green-50 border border-green-100 px-2 py-0.5 rounded-full">
                  Disponible
                </span>
              </div>
              <p class="text-[12px] text-[#73726C]">{{ selected.title || 'Freelancer' }}</p>
            </div>
          </div>
          <button @click="selected = null" class="p-1.5 hover:bg-[#F4F4ED] rounded-md transition">
            <svg class="w-5 h-5 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <!-- body -->
        <div class="p-6 space-y-5">

          <!-- stats -->
          <div class="grid grid-cols-3 gap-3">
            <div class="bg-[#FAFAF7] rounded-xl p-3 text-center">
              <div class="text-[15px] font-bold text-ink">
                {{ selected.hourlyRate ? selected.hourlyRate + ' DH' : '—' }}
              </div>
              <div class="text-[11px] text-[#73726C]">/ heure</div>
            </div>
            <div class="bg-[#FAFAF7] rounded-xl p-3 text-center">
              <div class="text-[15px] font-bold text-ink">{{ selected.completedMissions || 0 }}</div>
              <div class="text-[11px] text-[#73726C]">Missions</div>
            </div>
            <div class="bg-[#FAFAF7] rounded-xl p-3 text-center">
              <div class="text-[15px] font-bold text-ink">
                {{ selected.averageRating ? selected.averageRating.toFixed(1) + ' ★' : '—' }}
              </div>
              <div class="text-[11px] text-[#73726C]">Note</div>
            </div>
          </div>

          <!-- infos -->
          <div class="flex flex-wrap gap-3 text-[12px] text-[#5F5E5A]">
            <span v-if="selected.location" class="flex items-center gap-1.5">
              <svg class="w-3.5 h-3.5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z"/>
                <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z"/>
              </svg>
              {{ selected.location }}
            </span>
            <span v-if="selected.experienceLevel" :class="levelClass(selected.experienceLevel)"
                  class="text-[10px] font-semibold px-2 py-0.5 rounded-full border">
              {{ levelLabel(selected.experienceLevel) }}
            </span>
          </div>

          <!-- bio -->
          <div v-if="selected.bio">
            <h3 class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">À propos</h3>
            <p class="text-[13px] text-[#5F5E5A] leading-relaxed whitespace-pre-line">{{ selected.bio }}</p>
          </div>

          <!-- skills -->
          <div v-if="selected.skills">
            <h3 class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">Compétences</h3>
            <div class="flex flex-wrap gap-2">
              <span v-for="skill in skillTags(selected.skills)" :key="skill"
                    class="text-[12px] bg-[#F4F4ED] text-ink px-2.5 py-1 rounded-md font-medium">
                {{ skill }}
              </span>
            </div>
          </div>

          <!-- liens -->
          <div v-if="selected.githubUrl || selected.linkedinUrl || selected.portfolioUrl">
            <h3 class="text-[12px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">Liens</h3>
            <div class="flex flex-wrap gap-2">
              <a v-if="selected.githubUrl" :href="selected.githubUrl" target="_blank"
                 class="text-[12px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] hover:text-ink px-3 py-1.5 rounded-lg transition flex items-center gap-1.5"
                 @click.stop>
                <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
                </svg>
                GitHub
              </a>
              <a v-if="selected.linkedinUrl" :href="selected.linkedinUrl" target="_blank"
                 class="text-[12px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] hover:text-ink px-3 py-1.5 rounded-lg transition flex items-center gap-1.5"
                 @click.stop>
                <svg class="w-3.5 h-3.5 text-blue-600" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
                </svg>
                LinkedIn
              </a>
              <a v-if="selected.portfolioUrl" :href="selected.portfolioUrl" target="_blank"
                 class="text-[12px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] hover:text-ink px-3 py-1.5 rounded-lg transition flex items-center gap-1.5"
                 @click.stop>
                <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M13.19 8.688a4.5 4.5 0 011.242 7.244l-4.5 4.5a4.5 4.5 0 01-6.364-6.364l1.757-1.757m13.35-.622l1.757-1.757a4.5 4.5 0 00-6.364-6.364l-4.5 4.5a4.5 4.5 0 001.242 7.244"/>
                </svg>
                Portfolio
              </a>
            </div>
          </div>

        </div>

        <!-- footer : messagerie à venir -->
        <div class="sticky bottom-0 bg-white px-6 py-4 border-t border-[#EBEBE5]">
          <button disabled
                  class="w-full py-2.5 bg-[#F4F4ED] text-[#9C9A92] text-[13px] font-medium rounded-lg cursor-not-allowed flex items-center justify-center gap-2">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8.625 12a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H8.25m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H12m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h-.375M21 12c0 4.556-4.03 8.25-9 8.25a9.764 9.764 0 01-2.555-.337A5.972 5.972 0 015.41 20.97a5.969 5.969 0 01-.474-.065 4.48 4.48 0 00.978-2.025c.09-.457-.133-.901-.467-1.226C3.93 16.178 3 14.189 3 12c0-4.556 4.03-8.25 9-8.25s9 3.694 9 8.25z"/>
            </svg>
            Envoyer un message — Bientôt disponible
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

const loading      = ref(true)
const freelancers  = ref([])
const selected     = ref(null)
const searchQuery  = ref('')
const filterAvailable = ref('')
const filterLevel  = ref('')
const sortBy       = ref('name')
const viewMode     = ref('grid')
let   searchTimer  = null

const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

const load = async (q = '') => {
  loading.value = true
  try {
    const params = q ? { search: q } : {}
    const { data } = await axios.get(`${API_URL}/api/profile/freelancers`, { ...headers(), params })
    freelancers.value = data
  } catch (err) {
    console.error('load freelancers error:', err)
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => load(searchQuery.value), 400)
}

const clearSearch = () => {
  searchQuery.value = ''
  filterAvailable.value = ''
  filterLevel.value = ''
  sortBy.value = 'name'
  load()
}

const resetFilters = () => {
  filterAvailable.value = ''
  filterLevel.value = ''
  sortBy.value = 'name'
}

const filtered = computed(() => {
  let list = [...freelancers.value]

  if (filterAvailable.value === 'true') list = list.filter(f => f.isAvailable)
  if (filterLevel.value)               list = list.filter(f => f.experienceLevel === filterLevel.value)

  switch (sortBy.value) {
    case 'rate_asc':   list.sort((a, b) => (a.hourlyRate || 0) - (b.hourlyRate || 0)); break
    case 'rate_desc':  list.sort((a, b) => (b.hourlyRate || 0) - (a.hourlyRate || 0)); break
    case 'missions':   list.sort((a, b) => (b.completedMissions || 0) - (a.completedMissions || 0)); break
    default:           list.sort((a, b) => fullName(a).localeCompare(fullName(b), 'fr'))
  }

  return list
})

const total = computed(() => filtered.value.length)

const openProfile = (f) => { selected.value = f }

const fullName = (f) =>
  [f.firstName, f.lastName].filter(Boolean).join(' ') || f.email?.split('@')[0] || `Freelancer #${f.id}`

const initials = (f) =>
  [f.firstName, f.lastName].filter(Boolean).map(n => n[0].toUpperCase()).join('') ||
  (f.email ? f.email[0].toUpperCase() : '#')

const skillTags = (s) => s ? s.split(',').map(t => t.trim()).filter(Boolean) : []

const avatarColors = ['#E8E4D9', '#D9E4E8', '#E4D9E8', '#D9E8D9', '#E8DDD9', '#D9D9E8']
const avatarColor  = (id) => avatarColors[(id || 0) % avatarColors.length]

const levelLabel = (l) => ({ JUNIOR: 'Junior', MID: 'Intermédiaire', SENIOR: 'Senior', EXPERT: 'Expert' }[l] || l)
const levelClass  = (l) => ({
  JUNIOR:  'bg-blue-50 text-blue-600 border-blue-100',
  MID:     'bg-amber-50 text-amber-600 border-amber-100',
  SENIOR:  'bg-green-50 text-green-600 border-green-100',
  EXPERT:  'bg-purple-50 text-purple-600 border-purple-100',
}[l] || 'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]')

onMounted(() => load())
</script>
