<template>
  <div class="space-y-5">

    <!-- HEADER -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-[20px] font-semibold text-ink tracking-tight">Explorer les freelancers</h1>
        <p class="text-[13px] text-[#9C9A92] mt-0.5">
          <span class="font-medium text-ink">{{ total }}</span>
          profil{{ total !== 1 ? 's' : '' }} trouvé{{ total !== 1 ? 's' : '' }}
        </p>
      </div>
      <div class="flex items-center gap-1 bg-[#F4F4ED] p-1 rounded-lg">
        <button @click="viewMode = 'grid'" class="p-1.5 rounded-md transition-all"
                :class="viewMode === 'grid' ? 'bg-white text-ink shadow-sm' : 'text-[#9C9A92] hover:text-ink'">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6A2.25 2.25 0 016 3.75h2.25A2.25 2.25 0 0110.5 6v2.25a2.25 2.25 0 01-2.25 2.25H6a2.25 2.25 0 01-2.25-2.25V6zM3.75 15.75A2.25 2.25 0 016 13.5h2.25a2.25 2.25 0 012.25 2.25V18a2.25 2.25 0 01-2.25 2.25H6A2.25 2.25 0 013.75 18v-2.25zM13.5 6a2.25 2.25 0 012.25-2.25H18A2.25 2.25 0 0120.25 6v2.25A2.25 2.25 0 0118 10.5h-2.25a2.25 2.25 0 01-2.25-2.25V6zM13.5 15.75a2.25 2.25 0 012.25-2.25H18a2.25 2.25 0 012.25 2.25V18A2.25 2.25 0 0118 20.25h-2.25A2.25 2.25 0 0113.5 18v-2.25z"/>
          </svg>
        </button>
        <button @click="viewMode = 'list'" class="p-1.5 rounded-md transition-all"
                :class="viewMode === 'list' ? 'bg-white text-ink shadow-sm' : 'text-[#9C9A92] hover:text-ink'">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zM3.75 12h.007v.008H3.75V12zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm-.375 5.25h.007v.008H3.75v-.008zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- SEARCH + FILTERS -->
    <div class="bg-white border border-[#EBEBE5] rounded-xl p-4 space-y-3">
      <div class="relative">
        <svg class="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-[#9C9A92]"
             fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
        </svg>
        <input v-model="searchQuery" @input="onSearch" type="text"
               placeholder="Rechercher par nom, compétences, titre, ville..."
               class="w-full bg-[#FAFAF7] border border-[#EBEBE5] focus:border-[#B4B2A9] focus:bg-white rounded-lg pl-10 pr-9 py-2.5 text-[13px] outline-none transition placeholder:text-[#B4B2A9]"/>
        <button v-if="searchQuery" @click="clearSearch"
                class="absolute right-3 top-1/2 -translate-y-1/2 text-[#B4B2A9] hover:text-[#73726C] transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>
      <div class="flex items-center gap-2 flex-wrap">
        <button @click="filterAvailable = filterAvailable === 'true' ? '' : 'true'"
                class="flex items-center gap-1.5 text-[12px] font-medium px-3 py-1.5 rounded-lg border transition"
                :class="filterAvailable === 'true'
                  ? 'bg-green-50 text-green-700 border-green-200'
                  : 'bg-white text-[#5F5E5A] border-[#EBEBE5] hover:border-[#D3D1C7] hover:text-ink'">
          <span class="w-1.5 h-1.5 rounded-full flex-shrink-0"
                :class="filterAvailable === 'true' ? 'bg-green-500' : 'bg-[#D3D1C7]'"></span>
          Disponible
        </button>
        <button v-for="lvl in levels" :key="lvl.value"
                @click="filterLevel = filterLevel === lvl.value ? '' : lvl.value"
                class="text-[12px] font-medium px-3 py-1.5 rounded-lg border transition"
                :class="filterLevel === lvl.value ? lvl.activeClass : 'bg-white text-[#5F5E5A] border-[#EBEBE5] hover:border-[#D3D1C7] hover:text-ink'">
          {{ lvl.label }}
        </button>
        <div class="w-px h-4 bg-[#EBEBE5] mx-0.5"></div>
        <select v-model="sortBy"
                class="text-[12px] bg-white text-[#5F5E5A] border border-[#EBEBE5] rounded-lg px-2.5 py-1.5 outline-none focus:border-[#B4B2A9] transition cursor-pointer">
          <option value="name">Nom A–Z</option>
          <option value="rate_asc">Tarif croissant</option>
          <option value="rate_desc">Tarif décroissant</option>
          <option value="missions">Missions</option>
        </select>
        <button v-if="filterAvailable || filterLevel || sortBy !== 'name' || searchQuery"
                @click="clearSearch"
                class="ml-auto text-[12px] text-[#9C9A92] hover:text-red-500 transition flex items-center gap-1">
          <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
          </svg>
          Réinitialiser
        </button>
      </div>
    </div>

    <!-- SKELETON -->
    <template v-if="loading">
      <div v-if="viewMode === 'grid'" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
        <div v-for="i in 6" :key="i" class="bg-white border border-[#EBEBE5] rounded-xl p-5 animate-pulse">
          <div class="flex items-start gap-3 mb-4">
            <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex-shrink-0"></div>
            <div class="flex-1 space-y-2">
              <div class="h-3.5 bg-[#F4F4ED] rounded w-2/3"></div>
              <div class="h-3 bg-[#F4F4ED] rounded w-1/2"></div>
            </div>
            <div class="w-14 h-7 bg-[#F4F4ED] rounded"></div>
          </div>
          <div class="flex gap-1.5 mb-4">
            <div class="h-5 w-14 bg-[#F4F4ED] rounded"></div>
            <div class="h-5 w-18 bg-[#F4F4ED] rounded"></div>
            <div class="h-5 w-12 bg-[#F4F4ED] rounded"></div>
          </div>
          <div class="h-px bg-[#F4F4ED] mb-3"></div>
          <div class="flex justify-between">
            <div class="h-4 w-16 bg-[#F4F4ED] rounded"></div>
            <div class="h-4 w-20 bg-[#F4F4ED] rounded"></div>
          </div>
        </div>
      </div>
      <div v-else class="bg-white border border-[#EBEBE5] rounded-xl divide-y divide-[#F4F4ED]">
        <div v-for="i in 5" :key="i" class="p-4 animate-pulse flex items-center gap-4">
          <div class="w-11 h-11 bg-[#F4F4ED] rounded-xl flex-shrink-0"></div>
          <div class="flex-1 space-y-2">
            <div class="h-3.5 bg-[#F4F4ED] rounded w-1/3"></div>
            <div class="h-3 bg-[#F4F4ED] rounded w-1/2"></div>
          </div>
          <div class="h-4 w-20 bg-[#F4F4ED] rounded"></div>
        </div>
      </div>
    </template>

    <!-- EMPTY -->
    <div v-else-if="filtered.length === 0"
         class="bg-white border border-[#EBEBE5] rounded-xl py-16 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-6 h-6 text-[#B4B2A9]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">Aucun freelancer trouvé</p>
      <p class="text-[12px] text-[#9C9A92] mt-1 mb-5">Essayez d'autres mots-clés ou modifiez vos filtres</p>
      <button @click="clearSearch"
              class="inline-flex items-center gap-2 text-[12px] font-medium text-ink bg-[#F4F4ED] hover:bg-[#EBEBE5] px-4 py-2 rounded-lg transition">
        Réinitialiser les filtres
      </button>
    </div>

    <!-- GRID -->
    <div v-else-if="viewMode === 'grid'" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
      <div v-for="f in filtered" :key="f.id"
           @click="openProfile(f)"
           class="group bg-white border border-[#EBEBE5] rounded-xl p-5 cursor-pointer hover:border-[#C8C6BC] hover:shadow-sm transition-all">
        <div class="flex items-start gap-3 mb-4">
          <div class="relative flex-shrink-0">
            <div class="w-12 h-12 rounded-xl flex items-center justify-center text-[14px] font-bold text-ink"
                 :style="{ background: avatarBg(f.id) }">
              {{ initials(f) }}
            </div>
            <span v-if="f.isAvailable"
                  class="absolute -bottom-0.5 -right-0.5 w-3 h-3 bg-green-400 border-2 border-white rounded-full"></span>
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-[13px] font-semibold text-ink truncate">{{ fullName(f) }}</p>
            <p class="text-[12px] text-[#73726C] truncate mt-0.5">{{ f.title || 'Freelancer indépendant' }}</p>
            <p v-if="f.location" class="text-[11px] text-[#9C9A92] mt-0.5 truncate">{{ f.location }}</p>
          </div>
          <div v-if="f.hourlyRate" class="flex-shrink-0 text-right">
            <p class="text-[14px] font-semibold text-ink">{{ f.hourlyRate }} <span class="text-[11px] font-normal text-[#9C9A92]">DH</span></p>
            <p class="text-[10px] text-[#9C9A92]">/heure</p>
          </div>
        </div>
        <div v-if="f.skills" class="flex flex-wrap gap-1.5 mb-4">
          <span v-for="skill in skillTags(f.skills).slice(0, 3)" :key="skill"
                class="text-[11px] bg-[#F4F4ED] text-[#5F5E5A] px-2 py-0.5 rounded-md">{{ skill }}</span>
          <span v-if="skillTags(f.skills).length > 3" class="text-[11px] text-[#9C9A92] self-center">
            +{{ skillTags(f.skills).length - 3 }}
          </span>
        </div>
        <div v-else class="mb-4"></div>
        <div class="pt-3 border-t border-[#F4F4ED] flex items-center justify-between">
          <div class="flex items-center gap-2">
            <span v-if="f.experienceLevel"
                  class="text-[10px] font-semibold px-2 py-0.5 rounded-full border"
                  :class="levelClass(f.experienceLevel)">
              {{ levelLabel(f.experienceLevel) }}
            </span>
            <span v-if="f.averageRating" class="flex items-center gap-0.5 text-[11px] text-[#73726C]">
              <svg class="w-3 h-3 fill-amber-400" viewBox="0 0 24 24">
                <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
              </svg>
              {{ f.averageRating.toFixed(1) }}
            </span>
          </div>
          <div class="flex items-center gap-2">
            <span class="text-[11px] font-medium text-[#5F5E5A] border border-[#EBEBE5] px-2 py-0.5 rounded-md flex items-center gap-1">
              <svg class="w-3 h-3 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
              </svg>
              Portfolio
            </span>
            <span class="text-[11px] text-[#9C9A92] group-hover:text-ink transition flex items-center gap-0.5">
              Voir
              <svg class="w-3.5 h-3.5 group-hover:translate-x-0.5 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
              </svg>
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- LIST -->
    <div v-else class="bg-white border border-[#EBEBE5] rounded-xl overflow-hidden divide-y divide-[#F4F4ED]">
      <div v-for="f in filtered" :key="f.id"
           @click="openProfile(f)"
           class="group flex items-center gap-4 px-5 py-4 cursor-pointer hover:bg-[#FAFAF7] transition-colors">
        <div class="relative flex-shrink-0">
          <div class="w-11 h-11 rounded-xl flex items-center justify-center text-[13px] font-bold text-ink"
               :style="{ background: avatarBg(f.id) }">
            {{ initials(f) }}
          </div>
          <span v-if="f.isAvailable"
                class="absolute -bottom-0.5 -right-0.5 w-2.5 h-2.5 bg-green-400 border-2 border-white rounded-full"></span>
        </div>
        <div class="flex-1 min-w-0">
          <div class="flex items-center gap-2">
            <p class="text-[13px] font-semibold text-ink truncate">{{ fullName(f) }}</p>
            <span v-if="f.experienceLevel"
                  class="text-[10px] font-semibold px-2 py-0.5 rounded-full border flex-shrink-0"
                  :class="levelClass(f.experienceLevel)">
              {{ levelLabel(f.experienceLevel) }}
            </span>
          </div>
          <p class="text-[12px] text-[#73726C] truncate mt-0.5">
            {{ f.title || 'Freelancer indépendant' }}
            <span v-if="f.location" class="text-[#B4B2A9]"> · {{ f.location }}</span>
          </p>
          <div v-if="f.skills" class="flex flex-wrap gap-1 mt-1.5">
            <span v-for="skill in skillTags(f.skills).slice(0, 4)" :key="skill"
                  class="text-[11px] bg-[#F4F4ED] text-[#5F5E5A] px-2 py-0.5 rounded-md">{{ skill }}</span>
          </div>
        </div>
        <div class="flex-shrink-0 text-right">
          <p v-if="f.hourlyRate" class="text-[13px] font-semibold text-ink">{{ f.hourlyRate }} <span class="text-[11px] font-normal text-[#9C9A92]">DH/h</span></p>
          <div class="flex items-center justify-end gap-2 mt-0.5 text-[11px] text-[#9C9A92]">
            <span v-if="f.averageRating" class="flex items-center gap-0.5">
              <svg class="w-3 h-3 fill-amber-400" viewBox="0 0 24 24">
                <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
              </svg>
              {{ f.averageRating.toFixed(1) }}
            </span>
            <span v-if="f.completedMissions">{{ f.completedMissions }} miss.</span>
          </div>
        </div>
        <span class="flex-shrink-0 text-[11px] font-medium text-[#5F5E5A] border border-[#EBEBE5] px-2.5 py-1.5 rounded-md flex items-center gap-1">
          <svg class="w-3 h-3 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5z"/>
          </svg>
          Portfolio
        </span>
        <svg class="w-4 h-4 text-[#D3D1C7] group-hover:text-ink transition-colors flex-shrink-0"
             fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
        </svg>
      </div>
    </div>

    <!-- ═══════════════════ MODAL PROFIL ═══════════════════ -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0"
                leave-active-class="transition duration-150" leave-to-class="opacity-0">
      <div v-if="selected"
           class="fixed inset-0 bg-black/40 z-50 flex items-end md:items-center justify-center p-0 md:p-4"
           @click.self="selected = null">

        <Transition enter-active-class="transition duration-200"
                    enter-from-class="opacity-0 translate-y-4 md:translate-y-0 md:scale-95"
                    leave-active-class="transition duration-150"
                    leave-to-class="opacity-0 translate-y-4 md:scale-95">
          <div v-if="selected"
               class="bg-white rounded-t-2xl md:rounded-xl w-full md:max-w-[480px] shadow-xl flex flex-col max-h-[90vh]">

            <!-- EN-TÊTE -->
            <div class="px-6 pt-6 pb-5">
              <div class="flex items-start gap-4">
                <div class="w-14 h-14 rounded-xl flex items-center justify-center text-[18px] font-bold text-ink flex-shrink-0"
                     :style="{ background: avatarBg(selected.id) }">
                  {{ initials(selected) }}
                </div>
                <div class="flex-1 min-w-0">
                  <div class="flex items-start justify-between gap-2">
                    <div class="min-w-0">
                      <h2 class="text-[16px] font-semibold text-ink leading-snug">{{ fullName(selected) }}</h2>
                      <p class="text-[13px] text-[#73726C] mt-0.5">{{ selected.title || 'Freelancer indépendant' }}</p>
                    </div>
                    <button @click="selected = null"
                            class="w-7 h-7 flex-shrink-0 flex items-center justify-center rounded-lg hover:bg-[#F4F4ED] transition-colors -mt-0.5">
                      <svg class="w-4 h-4 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
                      </svg>
                    </button>
                  </div>
                  <div class="flex items-center gap-3 mt-2 flex-wrap">
                    <span v-if="selected.location" class="text-[12px] text-[#9C9A92]">{{ selected.location }}</span>
                    <span v-if="selected.isAvailable" class="flex items-center gap-1 text-[12px] text-green-600">
                      <span class="w-1.5 h-1.5 rounded-full bg-green-500 flex-shrink-0"></span>
                      Disponible
                    </span>
                    <span v-if="selected.experienceLevel"
                          class="text-[10px] font-semibold px-2 py-0.5 rounded-full border"
                          :class="levelClass(selected.experienceLevel)">
                      {{ levelLabel(selected.experienceLevel) }}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            <!-- STATS -->
            <div class="border-t border-b border-[#EBEBE5] grid grid-cols-3 divide-x divide-[#EBEBE5] flex-shrink-0">
              <div class="py-3 text-center">
                <p class="text-[15px] font-semibold text-ink">
                  {{ selected.hourlyRate ? selected.hourlyRate + ' DH' : '—' }}
                </p>
                <p class="text-[11px] text-[#9C9A92] mt-0.5">/ heure</p>
              </div>
              <div class="py-3 text-center">
                <p class="text-[15px] font-semibold text-ink">{{ selected.completedMissions || '0' }}</p>
                <p class="text-[11px] text-[#9C9A92] mt-0.5">missions</p>
              </div>
              <div class="py-3 text-center">
                <p class="text-[15px] font-semibold text-ink flex items-center justify-center gap-1">
                  <svg v-if="selected.averageRating" class="w-3.5 h-3.5 fill-amber-400" viewBox="0 0 24 24">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                  </svg>
                  {{ selected.averageRating ? selected.averageRating.toFixed(1) : '—' }}
                </p>
                <p class="text-[11px] text-[#9C9A92] mt-0.5">note</p>
              </div>
            </div>

            <!-- BODY -->
            <div class="flex-1 overflow-y-auto">
              <div class="px-6 py-5 space-y-6">

                <!-- À propos -->
                <div v-if="selected.bio">
                  <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">À propos</p>
                  <p class="text-[13px] text-[#5F5E5A] leading-relaxed whitespace-pre-line">{{ selected.bio }}</p>
                </div>

                <!-- Compétences -->
                <div v-if="selected.skills">
                  <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2">Compétences</p>
                  <div class="flex flex-wrap gap-1.5">
                    <span v-for="skill in skillTags(selected.skills)" :key="skill"
                          class="text-[12px] bg-[#F4F4ED] text-[#5F5E5A] px-2.5 py-1 rounded-lg">{{ skill }}</span>
                  </div>
                </div>

                <!-- Projets portfolio -->
                <div v-if="portfolioProjects.length > 0">
                  <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2.5">
                    Portfolio <span class="normal-case font-normal">({{ portfolioProjects.length }} projet{{ portfolioProjects.length > 1 ? 's' : '' }})</span>
                  </p>
                  <div class="space-y-2.5">
                    <div v-for="p in portfolioProjects" :key="p.id"
                         class="border border-[#EBEBE5] rounded-xl overflow-hidden">
                      <div v-if="p.imageUrl" class="h-28 bg-[#F4F4ED] overflow-hidden">
                        <img :src="p.imageUrl" :alt="p.title" class="w-full h-full object-cover"
                             @error="e => e.target.style.display='none'"/>
                      </div>
                      <div class="px-3 py-2.5">
                        <p class="text-[12px] font-semibold text-ink">{{ p.title }}</p>
                        <p v-if="p.description" class="text-[11px] text-[#73726C] mt-0.5 line-clamp-2">{{ p.description }}</p>
                        <div v-if="p.technologies" class="flex flex-wrap gap-1 mt-1.5">
                          <span v-for="tech in p.technologies.split(',').map(t => t.trim()).filter(t => t)" :key="tech"
                                class="text-[10px] bg-[#F4F4ED] text-[#5F5E5A] px-1.5 py-0.5 rounded">{{ tech }}</span>
                        </div>
                        <div class="flex gap-3 mt-2">
                          <a v-if="p.projectUrl" :href="p.projectUrl" target="_blank" @click.stop
                             class="text-[11px] text-ink font-medium hover:underline flex items-center gap-1">
                            <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                              <path stroke-linecap="round" stroke-linejoin="round" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"/>
                            </svg>
                            Voir live
                          </a>
                          <a v-if="p.githubUrl" :href="p.githubUrl" target="_blank" @click.stop
                             class="text-[11px] text-[#73726C] hover:text-ink flex items-center gap-1">
                            <svg class="w-3 h-3" fill="currentColor" viewBox="0 0 24 24">
                              <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
                            </svg>
                            GitHub
                          </a>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Liens -->
                <div v-if="selected.githubUrl || selected.linkedinUrl || selected.portfolioUrl">
                  <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2.5">Liens</p>
                  <div class="space-y-1.5">
                    <a v-if="selected.githubUrl" :href="selected.githubUrl" target="_blank" @click.stop
                       class="flex items-center gap-2.5 text-[13px] text-[#5F5E5A] hover:text-ink py-1.5 transition-colors group">
                      <svg class="w-4 h-4 flex-shrink-0 text-[#9C9A92] group-hover:text-ink transition-colors" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
                      </svg>
                      <span class="truncate">{{ selected.githubUrl }}</span>
                    </a>
                    <a v-if="selected.linkedinUrl" :href="selected.linkedinUrl" target="_blank" @click.stop
                       class="flex items-center gap-2.5 text-[13px] text-[#5F5E5A] hover:text-ink py-1.5 transition-colors group">
                      <svg class="w-4 h-4 flex-shrink-0 text-blue-500 group-hover:text-blue-600 transition-colors" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
                      </svg>
                      <span class="truncate">{{ selected.linkedinUrl }}</span>
                    </a>
                    <a v-if="selected.portfolioUrl" :href="selected.portfolioUrl" target="_blank" @click.stop
                       class="flex items-center gap-2.5 text-[13px] text-[#5F5E5A] hover:text-ink py-1.5 transition-colors group">
                      <svg class="w-4 h-4 flex-shrink-0 text-[#9C9A92] group-hover:text-ink transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M13.19 8.688a4.5 4.5 0 011.242 7.244l-4.5 4.5a4.5 4.5 0 01-6.364-6.364l1.757-1.757m13.35-.622l1.757-1.757a4.5 4.5 0 00-6.364-6.364l-4.5 4.5a4.5 4.5 0 001.242 7.244"/>
                      </svg>
                      <span class="truncate">{{ selected.portfolioUrl }}</span>
                    </a>
                  </div>
                </div>

                <!-- Avis -->
                <div>
                  <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-2.5">
                    Avis<span v-if="!loadingReviews && reviews.length"> ({{ reviews.length }})</span>
                  </p>

                  <div v-if="loadingReviews" class="space-y-2.5">
                    <div v-for="i in 2" :key="i" class="animate-pulse flex gap-3">
                      <div class="w-8 h-8 bg-[#F4F4ED] rounded-full flex-shrink-0"></div>
                      <div class="flex-1 space-y-2 pt-1">
                        <div class="h-3 bg-[#F4F4ED] rounded w-1/4"></div>
                        <div class="h-3 bg-[#F4F4ED] rounded w-3/4"></div>
                      </div>
                    </div>
                  </div>

                  <div v-else-if="reviews.length === 0"
                       class="py-5 text-center border border-dashed border-[#EBEBE5] rounded-xl">
                    <p class="text-[12px] text-[#9C9A92]">Aucun avis pour le moment</p>
                  </div>

                  <div v-else class="space-y-3">
                    <div v-for="review in reviews" :key="review.id"
                         class="flex gap-3">
                      <div class="w-8 h-8 rounded-full flex items-center justify-center text-[11px] font-bold text-ink flex-shrink-0"
                           :style="{ background: avatarBg((review.id || 1) * 3) }">
                        C
                      </div>
                      <div class="flex-1 min-w-0">
                        <div class="flex items-center gap-2 mb-1">
                          <div class="flex gap-0.5">
                            <svg v-for="s in 5" :key="s" class="w-3 h-3"
                                 :class="s <= review.rating ? 'fill-amber-400' : 'fill-[#E8E6DE]'"
                                 viewBox="0 0 24 24">
                              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                            </svg>
                          </div>
                          <span class="text-[10px] text-[#9C9A92]">{{ formatReviewDate(review.createdAt) }}</span>
                        </div>
                        <p v-if="review.comment" class="text-[13px] text-[#5F5E5A] leading-relaxed">
                          {{ review.comment }}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

              </div>
            </div>

            <!-- FOOTER -->
            <div class="px-6 py-4 border-t border-[#EBEBE5] flex-shrink-0 space-y-2">

              <!-- formulaire d'avis -->
              <div v-if="showReviewForm" class="space-y-3">
                <div class="flex items-center justify-between">
                  <p class="text-[12px] font-semibold text-ink">Laisser un avis</p>
                  <button @click="showReviewForm = false" class="text-[11px] text-[#9C9A92] hover:text-ink transition">Annuler</button>
                </div>
                <div class="flex gap-1">
                  <button v-for="s in 5" :key="s" @click="reviewRating = s" type="button"
                          class="transition-transform hover:scale-110">
                    <svg class="w-6 h-6" :class="s <= reviewRating ? 'fill-amber-400' : 'fill-[#E8E6DE]'" viewBox="0 0 24 24">
                      <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                    </svg>
                  </button>
                </div>
                <textarea v-model="reviewComment" rows="3"
                          placeholder="Décrivez votre expérience (optionnel)..."
                          class="w-full text-[12px] bg-[#FAFAF7] border border-[#EBEBE5] focus:border-[#B4B2A9] rounded-xl px-3 py-2.5 outline-none resize-none placeholder:text-[#B4B2A9] transition-colors"></textarea>
                <div v-if="reviewError" class="text-[11px] text-red-500">{{ reviewError }}</div>
                <button @click="submitReview" :disabled="reviewRating === 0 || submittingReview"
                        class="w-full text-[12px] font-semibold text-white bg-ink hover:bg-[#2C2B27] rounded-xl py-2.5 transition disabled:opacity-50">
                  {{ submittingReview ? 'Envoi…' : 'Publier l\'avis' }}
                </button>
              </div>

              <template v-else>
                <button @click.stop="contactFreelancer(selected)"
                        class="w-full text-[13px] font-semibold text-white bg-ink hover:bg-[#2C2B27] rounded-xl py-3 transition-colors">
                  Envoyer un message
                </button>
                <button v-if="pendingReviewApp" @click="openReviewForm"
                        class="w-full text-[12px] font-medium text-[#5F5E5A] hover:text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] rounded-xl py-2.5 transition-colors">
                  Laisser un avis
                </button>
              </template>

            </div>

          </div>
        </Transition>
      </div>
    </Transition>
    </Teleport>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const router          = useRouter()
const loading         = ref(true)
const freelancers     = ref([])
const selected        = ref(null)
const reviews         = ref([])
const loadingReviews  = ref(false)
const searchQuery     = ref('')
const filterAvailable = ref('')
const filterLevel     = ref('')
const sortBy          = ref('name')
const viewMode        = ref('grid')
let   searchTimer     = null

const portfolioProjects = ref([])
const myReviewedIds    = ref(new Set())
const completedApps    = ref([])
const showReviewForm   = ref(false)
const reviewRating     = ref(0)
const reviewComment    = ref('')
const reviewError      = ref('')
const submittingReview = ref(false)

const pendingReviewApp = computed(() =>
  completedApps.value.find(a => !myReviewedIds.value.has(a.id)) || null
)

const levels = [
  { value: 'JUNIOR',  label: 'Junior',        activeClass: 'bg-blue-50 text-blue-700 border-blue-200'      },
  { value: 'MID',     label: 'Intermédiaire', activeClass: 'bg-amber-50 text-amber-700 border-amber-200'   },
  { value: 'SENIOR',  label: 'Senior',        activeClass: 'bg-green-50 text-green-700 border-green-200'   },
  { value: 'EXPERT',  label: 'Expert',        activeClass: 'bg-purple-50 text-purple-700 border-purple-200' },
]

const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token') || sessionStorage.getItem('token')}` } })

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
  searchQuery.value     = ''
  filterAvailable.value = ''
  filterLevel.value     = ''
  sortBy.value          = 'name'
  load()
}

const filtered = computed(() => {
  let list = [...freelancers.value]
  if (filterAvailable.value === 'true') list = list.filter(f => f.isAvailable)
  if (filterLevel.value)               list = list.filter(f => f.experienceLevel === filterLevel.value)
  switch (sortBy.value) {
    case 'rate_asc':  list.sort((a, b) => (a.hourlyRate || 0) - (b.hourlyRate || 0)); break
    case 'rate_desc': list.sort((a, b) => (b.hourlyRate || 0) - (a.hourlyRate || 0)); break
    case 'missions':  list.sort((a, b) => (b.completedMissions || 0) - (a.completedMissions || 0)); break
    default:          list.sort((a, b) => fullName(a).localeCompare(fullName(b), 'fr'))
  }
  return list
})

const total = computed(() => filtered.value.length)

const openProfile = async (f) => {
  selected.value          = f
  reviews.value           = []
  portfolioProjects.value = []
  completedApps.value     = []
  showReviewForm.value    = false
  reviewRating.value      = 0
  reviewComment.value     = ''
  reviewError.value       = ''
  loadingReviews.value    = true
  try {
    const [{ data: revData }, { data: appData }, { data: portData }] = await Promise.all([
      axios.get(`${API_URL}/api/reviews/freelancer/${f.id}`),
      axios.get(`${API_URL}/api/applications/completed-with/${f.id}`, headers()),
      axios.get(`${API_URL}/api/portfolio/public/${f.id}`),
    ])
    reviews.value           = revData || []
    completedApps.value     = appData || []
    portfolioProjects.value = portData || []
  } catch {
    reviews.value = []
  } finally {
    loadingReviews.value = false
  }
}

const contactFreelancer = (f) => {
  selected.value = null
  router.push(`/client/messages?freelancer=${f.id}`)
}

const openReviewForm = () => {
  showReviewForm.value = true
  reviewRating.value   = 0
  reviewComment.value  = ''
  reviewError.value    = ''
}

const submitReview = async () => {
  if (reviewRating.value === 0) return
  submittingReview.value = true
  reviewError.value      = ''
  try {
    await axios.post(`${API_URL}/api/reviews`, {
      applicationId: pendingReviewApp.value.id,
      rating:        reviewRating.value,
      comment:       reviewComment.value || null,
    }, headers())
    myReviewedIds.value.add(pendingReviewApp.value.id)
    showReviewForm.value = false
    const { data } = await axios.get(`${API_URL}/api/reviews/freelancer/${selected.value.id}`)
    reviews.value = data || []
  } catch (err) {
    reviewError.value = err?.response?.data?.message || "Erreur lors de l'envoi de l'avis"
  } finally {
    submittingReview.value = false
  }
}

const fullName = (f) =>
  [f.firstName, f.lastName].filter(Boolean).join(' ') || f.email?.split('@')[0] || `Freelancer #${f.id}`

const initials = (f) =>
  [f.firstName, f.lastName].filter(Boolean).map(n => n[0].toUpperCase()).join('') ||
  (f.email ? f.email[0].toUpperCase() : '#')

const skillTags  = (s) => s ? s.split(',').map(t => t.trim()).filter(Boolean) : []

const palette  = ['#EDE9DC', '#DCE9ED', '#E9DCED', '#DCE9DC', '#EDE2DC', '#DCDCE9', '#E9EDDC', '#EDD9E0']
const avatarBg = (id) => palette[(id || 0) % palette.length]

const formatReviewDate = (iso) => {
  if (!iso) return ''
  const d = new Date(iso)
  const m = ['jan', 'fév', 'mars', 'avr', 'mai', 'juin', 'juil', 'août', 'sep', 'oct', 'nov', 'déc']
  return `${d.getDate()} ${m[d.getMonth()]} ${d.getFullYear()}`
}

const levelLabel = (l) => ({ JUNIOR: 'Junior', MID: 'Intermédiaire', SENIOR: 'Senior', EXPERT: 'Expert' }[l] || l)
const levelClass = (l) => ({
  JUNIOR: 'bg-blue-50 text-blue-600 border-blue-100',
  MID:    'bg-amber-50 text-amber-600 border-amber-100',
  SENIOR: 'bg-green-50 text-green-600 border-green-100',
  EXPERT: 'bg-purple-50 text-purple-600 border-purple-100',
}[l] || 'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]')

onMounted(async () => {
  load()
  try {
    const { data } = await axios.get(`${API_URL}/api/reviews/my`, headers())
    myReviewedIds.value = new Set(data)
  } catch {
    // not critical
  }
})
</script>
