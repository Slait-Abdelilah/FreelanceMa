<template>
  <div class="space-y-6">

    <!-- ═══════════════════════════════════════════════════════════
         HEADER — BIENVENUE
    ═══════════════════════════════════════════════════════════ -->
    <div class="bg-gradient-to-br from-[#0D1B0D] via-[#132613] to-[#0D1B0D] rounded-3xl p-8 text-white relative overflow-hidden">

      <div class="absolute top-0 right-0 w-[500px] h-[500px] bg-brand-500/10 rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -bottom-32 -left-16 w-80 h-80 bg-brand-500/5 rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative flex items-start justify-between flex-wrap gap-6">

        <!-- gauche : salutation + profil -->
        <div class="flex items-center gap-5">
          <!-- avatar -->
          <div class="w-16 h-16 rounded-2xl bg-brand-500/20 border-2 border-brand-500/40 flex items-center justify-center text-2xl font-black text-brand-400 flex-shrink-0">
            {{ userInitials }}
          </div>
          <div>
            <div class="flex items-center gap-2 mb-1">
              <span class="text-xs font-bold text-brand-400 uppercase tracking-widest">{{ greeting }}</span>
              <span class="text-base">👋</span>
            </div>
            <h1 class="text-2xl lg:text-3xl font-black">
              {{ profile.firstName || profile.email?.split('@')[0] || 'Freelancer' }}
              <span v-if="profile.lastName"> {{ profile.lastName }}</span>
            </h1>
            <p class="text-gray-400 text-sm mt-1">
              <span v-if="profile.title" class="text-brand-400 font-medium">{{ profile.title }}</span>
              <span v-else class="italic">Complétez votre profil →</span>
            </p>
          </div>
        </div>

        <!-- droite : date + indicateurs rapides -->
        <div class="flex items-start gap-3">

          <!-- disponibilité -->
          <div class="bg-white/5 border rounded-2xl px-4 py-3 text-center"
               :class="profile.isAvailable ? 'border-brand-500/30' : 'border-white/10'">
            <div class="flex items-center gap-1.5 mb-0.5">
              <div class="w-2 h-2 rounded-full" :class="profile.isAvailable ? 'bg-brand-400 animate-pulse' : 'bg-gray-500'"></div>
              <span class="text-xs font-semibold" :class="profile.isAvailable ? 'text-brand-400' : 'text-gray-400'">
                {{ profile.isAvailable ? 'Disponible' : 'Indisponible' }}
              </span>
            </div>
            <div class="text-xs text-gray-500">pour missions</div>
          </div>

          <!-- date -->
          <div class="bg-white/5 border border-white/10 rounded-2xl px-5 py-3 text-center">
            <div class="text-xs text-gray-400 uppercase tracking-wider mb-0.5">{{ dayName }}</div>
            <div class="text-xl font-black">{{ dayNumber }} {{ monthName }}</div>
            <div class="text-xs text-gray-500 mt-0.5">{{ year }}</div>
          </div>

        </div>

      </div>

      <!-- barre résumé rapide -->
      <div class="relative mt-6 flex flex-wrap items-center gap-4 pt-5 border-t border-white/10 text-sm">
        <div class="flex items-center gap-2 text-gray-400">
          <svg class="w-4 h-4 text-brand-400" fill="currentColor" viewBox="0 0 20 20">
            <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
          </svg>
          <span class="font-bold text-white">{{ profile.averageRating?.toFixed(1) || '—' }}</span>
          <span>({{ profile.totalReviews || 0 }} avis)</span>
        </div>
        <span class="text-white/20">|</span>
        <div class="flex items-center gap-2 text-gray-400">
          <svg class="w-4 h-4 text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
          <span class="font-bold text-white">{{ profile.completedMissions || 0 }}</span>
          <span>missions complétées</span>
        </div>
        <span class="text-white/20">|</span>
        <div class="flex items-center gap-2 text-gray-400">
          <svg class="w-4 h-4 text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
          </svg>
          <span class="font-bold text-white">{{ pendingApplications }}</span>
          <span>candidatures en attente</span>
        </div>
      </div>

    </div>

    <!-- ═══════════════════════════════════════════════════════════
         STATISTIQUES — 4 CARTES
    ═══════════════════════════════════════════════════════════ -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">

      <!-- solde actuel -->
      <div class="bg-white rounded-2xl p-5 border border-gray-100 hover:border-brand-200 hover:shadow-lg transition-all duration-300">
        <div class="flex items-center justify-between mb-4">
          <div class="w-11 h-11 rounded-xl bg-brand-50 flex items-center justify-center">
            <svg class="w-5 h-5 text-brand-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"/>
            </svg>
          </div>
          <span class="text-xs font-bold text-brand-600 bg-brand-50 px-2 py-1 rounded-full">Wallet</span>
        </div>
        <div v-if="loadingWallet" class="h-8 bg-gray-100 rounded animate-pulse mb-1"></div>
        <div v-else class="text-2xl lg:text-3xl font-black text-ink mb-1">
          {{ formatAmount(wallet.balance) }}
          <span class="text-sm font-semibold text-ink-soft">DH</span>
        </div>
        <div class="text-sm text-ink-soft">Solde disponible</div>
      </div>

      <!-- total gagné -->
      <div class="bg-white rounded-2xl p-5 border border-gray-100 hover:border-emerald-200 hover:shadow-lg transition-all duration-300">
        <div class="flex items-center justify-between mb-4">
          <div class="w-11 h-11 rounded-xl bg-emerald-50 flex items-center justify-center">
            <svg class="w-5 h-5 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"/>
            </svg>
          </div>
          <span class="text-xs font-bold text-emerald-700 bg-emerald-50 px-2 py-1 rounded-full">Total</span>
        </div>
        <div v-if="loadingWallet" class="h-8 bg-gray-100 rounded animate-pulse mb-1"></div>
        <div v-else class="text-2xl lg:text-3xl font-black text-ink mb-1">
          {{ formatAmount(wallet.totalEarned) }}
          <span class="text-sm font-semibold text-ink-soft">DH</span>
        </div>
        <div class="text-sm text-ink-soft">Total gagné</div>
      </div>

      <!-- candidatures -->
      <div class="bg-white rounded-2xl p-5 border border-gray-100 hover:border-violet-200 hover:shadow-lg transition-all duration-300">
        <div class="flex items-center justify-between mb-4">
          <div class="w-11 h-11 rounded-xl bg-violet-50 flex items-center justify-center">
            <svg class="w-5 h-5 text-violet-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
            </svg>
          </div>
          <span class="text-xs font-bold text-violet-700 bg-violet-50 px-2 py-1 rounded-full">En attente</span>
        </div>
        <div v-if="loadingApplications" class="h-8 bg-gray-100 rounded animate-pulse mb-1"></div>
        <div v-else class="text-2xl lg:text-3xl font-black text-ink mb-1">{{ pendingApplications }}</div>
        <div class="text-sm text-ink-soft">
          sur {{ applications.length }} candidature{{ applications.length !== 1 ? 's' : '' }}
        </div>
      </div>

      <!-- note -->
      <div class="bg-white rounded-2xl p-5 border border-gray-100 hover:border-amber-200 hover:shadow-lg transition-all duration-300">
        <div class="flex items-center justify-between mb-4">
          <div class="w-11 h-11 rounded-xl bg-amber-50 flex items-center justify-center">
            <svg class="w-5 h-5 text-amber-500" fill="currentColor" viewBox="0 0 20 20">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
            </svg>
          </div>
          <span class="text-xs font-bold text-amber-700 bg-amber-50 px-2 py-1 rounded-full">Note</span>
        </div>
        <div v-if="loadingProfile" class="h-8 bg-gray-100 rounded animate-pulse mb-1"></div>
        <div v-else class="text-2xl lg:text-3xl font-black text-ink mb-1">
          {{ profile.averageRating ? profile.averageRating.toFixed(1) : '—' }}
          <span class="text-sm font-semibold text-amber-500">★</span>
        </div>
        <div class="text-sm text-ink-soft">{{ profile.totalReviews || 0 }} évaluation{{ (profile.totalReviews || 0) !== 1 ? 's' : '' }}</div>
      </div>

    </div>

    <!-- ═══════════════════════════════════════════════════════════
         MILIEU : GRAPHIQUE + COMPLÉTUDE PROFIL
    ═══════════════════════════════════════════════════════════ -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <!-- graphique transactions par mois -->
      <div class="lg:col-span-2 bg-white rounded-2xl p-6 border border-gray-100">

        <div class="flex items-center justify-between mb-6">
          <div>
            <h2 class="text-lg font-bold text-ink">Activité financière</h2>
            <p class="text-sm text-ink-soft">Transactions des 6 derniers mois</p>
          </div>
          <div class="flex items-center gap-3">
            <div class="flex items-center gap-1.5 text-xs text-ink-soft">
              <div class="w-3 h-3 rounded-sm bg-brand-500"></div> Crédits
            </div>
            <div class="flex items-center gap-1.5 text-xs text-ink-soft">
              <div class="w-3 h-3 rounded-sm bg-red-400"></div> Débits
            </div>
          </div>
        </div>

        <!-- état vide -->
        <div v-if="!loadingWallet && chartData.length === 0"
             class="h-56 flex flex-col items-center justify-center text-center">
          <div class="w-16 h-16 rounded-2xl bg-brand-50 flex items-center justify-center mb-4">
            <svg class="w-8 h-8 text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/>
            </svg>
          </div>
          <p class="text-sm font-semibold text-ink mb-1">Aucune transaction pour l'instant</p>
          <p class="text-xs text-ink-soft">Vos revenus apparaîtront ici dès votre première mission</p>
        </div>

        <!-- squelette -->
        <div v-else-if="loadingWallet" class="h-56 flex items-end gap-3 px-2">
          <div v-for="i in 6" :key="i" class="flex-1 bg-gray-100 rounded-t-lg animate-pulse"
               :style="{ height: (30 + Math.random() * 60) + '%' }"></div>
        </div>

        <!-- graphique SVG -->
        <div v-else class="relative h-56">
          <svg class="w-full h-full overflow-visible" :viewBox="`0 0 ${chartWidth} 200`" preserveAspectRatio="none">

            <!-- lignes horizontales -->
            <line v-for="i in 4" :key="`h-${i}`"
                  x1="0" :y1="i * 50" :x2="chartWidth" :y2="i * 50"
                  stroke="#f3f4f6" stroke-width="1"/>

            <!-- barres par mois -->
            <g v-for="(month, i) in chartData" :key="month.label">
              <!-- barre crédit -->
              <rect
                v-if="month.credit > 0"
                :x="i * colWidth + colWidth * 0.15"
                :y="200 - (month.credit / chartMax * 180)"
                :width="colWidth * 0.3"
                :height="month.credit / chartMax * 180"
                fill="#14A800" rx="3"
                class="transition-all duration-500"
              />
              <!-- barre débit -->
              <rect
                v-if="month.debit > 0"
                :x="i * colWidth + colWidth * 0.5"
                :y="200 - (month.debit / chartMax * 180)"
                :width="colWidth * 0.3"
                :height="month.debit / chartMax * 180"
                fill="#f87171" rx="3"
                class="transition-all duration-500"
              />
              <!-- label mois -->
              <text
                :x="i * colWidth + colWidth / 2"
                y="215"
                text-anchor="middle"
                font-size="10"
                fill="#9ca3af">
                {{ month.label }}
              </text>
            </g>

          </svg>
        </div>

        <!-- totaux -->
        <div class="grid grid-cols-3 gap-4 mt-4 pt-4 border-t border-gray-100">
          <div>
            <div class="text-xs text-ink-soft mb-1">Total crédits</div>
            <div class="text-base font-bold text-brand-600">+ {{ formatAmount(totalCredit) }} DH</div>
          </div>
          <div>
            <div class="text-xs text-ink-soft mb-1">Total débits</div>
            <div class="text-base font-bold text-red-500">- {{ formatAmount(totalDebit) }} DH</div>
          </div>
          <div>
            <div class="text-xs text-ink-soft mb-1">En attente</div>
            <div class="text-base font-bold text-amber-600">{{ formatAmount(wallet.pendingBalance) }} DH</div>
          </div>
        </div>

      </div>

      <!-- complétude du profil + actions rapides -->
      <div class="space-y-4">

        <!-- complétude profil -->
        <div class="bg-white rounded-2xl p-6 border border-gray-100">
          <h2 class="text-base font-bold text-ink mb-1">Complétude du profil</h2>
          <p class="text-xs text-ink-soft mb-4">Un profil complet reçoit 3× plus d'offres</p>

          <div class="flex items-center gap-3 mb-4">
            <div class="flex-1 h-3 bg-gray-100 rounded-full overflow-hidden">
              <div class="h-full rounded-full transition-all duration-700"
                   :class="profileCompletion >= 80 ? 'bg-brand-500' : profileCompletion >= 50 ? 'bg-amber-400' : 'bg-red-400'"
                   :style="{ width: profileCompletion + '%' }"></div>
            </div>
            <span class="text-sm font-black" :class="profileCompletion >= 80 ? 'text-brand-600' : profileCompletion >= 50 ? 'text-amber-600' : 'text-red-500'">
              {{ profileCompletion }}%
            </span>
          </div>

          <div class="space-y-2">
            <div v-for="item in profileChecklist" :key="item.label"
                 class="flex items-center gap-2.5 text-xs">
              <div class="w-4 h-4 rounded-full flex items-center justify-center flex-shrink-0"
                   :class="item.done ? 'bg-brand-500' : 'bg-gray-200'">
                <svg v-if="item.done" class="w-2.5 h-2.5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
                </svg>
              </div>
              <span :class="item.done ? 'text-ink line-through opacity-50' : 'text-ink font-medium'">{{ item.label }}</span>
            </div>
          </div>

          <RouterLink v-if="profileCompletion < 100" to="/freelancer/profile"
                      class="mt-4 w-full block text-center text-xs font-bold text-brand-600 bg-brand-50 hover:bg-brand-100 py-2.5 rounded-xl transition">
            Compléter mon profil →
          </RouterLink>
        </div>

        <!-- actions rapides -->
        <div class="bg-white rounded-2xl p-5 border border-gray-100">
          <h2 class="text-base font-bold text-ink mb-3">Actions rapides</h2>
          <div class="space-y-2">
            <RouterLink to="/freelancer/explore"
                        class="flex items-center gap-3 p-3 rounded-xl hover:bg-brand-50 transition group">
              <div class="w-8 h-8 rounded-lg bg-brand-50 group-hover:bg-brand-100 flex items-center justify-center transition">
                <svg class="w-4 h-4 text-brand-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                </svg>
              </div>
              <span class="text-sm font-semibold text-ink">Explorer les missions</span>
              <svg class="w-4 h-4 text-gray-400 ml-auto group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
              </svg>
            </RouterLink>
            <RouterLink to="/freelancer/portfolio"
                        class="flex items-center gap-3 p-3 rounded-xl hover:bg-violet-50 transition group">
              <div class="w-8 h-8 rounded-lg bg-violet-50 group-hover:bg-violet-100 flex items-center justify-center transition">
                <svg class="w-4 h-4 text-violet-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"/>
                </svg>
              </div>
              <span class="text-sm font-semibold text-ink">Mon portfolio</span>
              <svg class="w-4 h-4 text-gray-400 ml-auto group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
              </svg>
            </RouterLink>
            <RouterLink to="/freelancer/wallet"
                        class="flex items-center gap-3 p-3 rounded-xl hover:bg-emerald-50 transition group">
              <div class="w-8 h-8 rounded-lg bg-emerald-50 group-hover:bg-emerald-100 flex items-center justify-center transition">
                <svg class="w-4 h-4 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"/>
                </svg>
              </div>
              <span class="text-sm font-semibold text-ink">Mon wallet</span>
              <svg class="w-4 h-4 text-gray-400 ml-auto group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
              </svg>
            </RouterLink>
          </div>
        </div>

      </div>
    </div>

    <!-- ═══════════════════════════════════════════════════════════
         BAS : CANDIDATURES RÉCENTES + OFFRES RÉCENTES
    ═══════════════════════════════════════════════════════════ -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">

      <!-- candidatures récentes -->
      <div class="bg-white rounded-2xl p-6 border border-gray-100">

        <div class="flex items-center justify-between mb-5">
          <div>
            <h2 class="text-lg font-bold text-ink">Mes candidatures récentes</h2>
            <p class="text-sm text-ink-soft">{{ applications.length }} au total</p>
          </div>
          <RouterLink to="/freelancer/applications"
                      class="text-sm font-semibold text-brand-600 hover:underline flex items-center gap-1">
            Voir tout
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
            </svg>
          </RouterLink>
        </div>

        <!-- squelette -->
        <div v-if="loadingApplications" class="space-y-3">
          <div v-for="i in 4" :key="i" class="flex items-center gap-3 p-3 rounded-xl bg-gray-50 animate-pulse">
            <div class="w-10 h-10 rounded-xl bg-gray-200 flex-shrink-0"></div>
            <div class="flex-1">
              <div class="h-3 bg-gray-200 rounded w-3/4 mb-2"></div>
              <div class="h-3 bg-gray-200 rounded w-1/2"></div>
            </div>
            <div class="w-16 h-6 bg-gray-200 rounded-full"></div>
          </div>
        </div>

        <!-- état vide -->
        <div v-else-if="recentApplications.length === 0"
             class="flex flex-col items-center justify-center py-12 text-center">
          <div class="w-14 h-14 rounded-2xl bg-violet-50 flex items-center justify-center mb-3">
            <svg class="w-7 h-7 text-violet-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
            </svg>
          </div>
          <p class="text-sm font-semibold text-ink mb-1">Aucune candidature</p>
          <p class="text-xs text-ink-soft mb-3">Explorez les missions disponibles et postulez</p>
          <RouterLink to="/freelancer/explore"
                      class="text-xs font-bold text-brand-600 bg-brand-50 px-4 py-2 rounded-xl hover:bg-brand-100 transition">
            Explorer les offres
          </RouterLink>
        </div>

        <!-- liste -->
        <div v-else class="space-y-2">
          <div v-for="app in recentApplications" :key="app.id"
               class="flex items-center gap-3 p-3 rounded-xl border border-transparent hover:border-gray-200 hover:bg-gray-50 transition cursor-pointer">

            <div class="w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0 text-sm font-black"
                 :class="statusStyle(app.status).iconBg">
              {{ statusStyle(app.status).icon }}
            </div>

            <div class="flex-1 min-w-0">
              <p class="text-sm font-semibold text-ink truncate">{{ app.offerTitle || 'Mission #' + app.offerId }}</p>
              <p class="text-xs text-ink-soft">{{ formatDate(app.createdAt) }}</p>
            </div>

            <span class="text-xs font-bold px-2.5 py-1 rounded-full whitespace-nowrap"
                  :class="statusStyle(app.status).badge">
              {{ statusLabel(app.status) }}
            </span>

          </div>
        </div>

      </div>

      <!-- offres récentes de la plateforme -->
      <div class="bg-white rounded-2xl p-6 border border-gray-100">

        <div class="flex items-center justify-between mb-5">
          <div>
            <h2 class="text-lg font-bold text-ink">Offres récentes</h2>
            <p class="text-sm text-ink-soft">Publiées sur la plateforme</p>
          </div>
          <RouterLink to="/freelancer/explore"
                      class="text-sm font-semibold text-brand-600 hover:underline flex items-center gap-1">
            Voir tout
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
            </svg>
          </RouterLink>
        </div>

        <!-- squelette -->
        <div v-if="loadingOffers" class="space-y-3">
          <div v-for="i in 4" :key="i" class="p-4 rounded-xl bg-gray-50 animate-pulse">
            <div class="h-3 bg-gray-200 rounded w-3/4 mb-2"></div>
            <div class="h-3 bg-gray-200 rounded w-1/3"></div>
          </div>
        </div>

        <!-- état vide -->
        <div v-else-if="recentOffers.length === 0"
             class="flex flex-col items-center justify-center py-12 text-center">
          <div class="w-14 h-14 rounded-2xl bg-brand-50 flex items-center justify-center mb-3">
            <svg class="w-7 h-7 text-brand-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
            </svg>
          </div>
          <p class="text-sm font-semibold text-ink mb-1">Aucune offre disponible</p>
          <p class="text-xs text-ink-soft">Revenez bientôt pour de nouvelles missions</p>
        </div>

        <!-- liste -->
        <div v-else class="space-y-2">
          <RouterLink v-for="offer in recentOffers" :key="offer.id"
                      to="/freelancer/explore"
                      class="block p-4 rounded-xl border border-gray-100 hover:border-brand-300 hover:shadow-sm transition-all duration-200 cursor-pointer group">

            <div class="flex items-start justify-between gap-2 mb-2">
              <h3 class="text-sm font-bold text-ink line-clamp-1 group-hover:text-brand-600 transition">
                {{ offer.title }}
              </h3>
              <span class="text-xs text-ink-soft whitespace-nowrap flex-shrink-0">{{ formatDate(offer.createdAt) }}</span>
            </div>

            <div class="flex items-center justify-between">
              <div class="flex flex-wrap gap-1">
                <span v-for="(skill, idx) in (offer.requiredSkills || '').split(',').slice(0, 3)" :key="idx"
                      v-if="skill.trim()"
                      class="text-xs bg-gray-100 text-ink-soft px-2 py-0.5 rounded-md">
                  {{ skill.trim() }}
                </span>
              </div>
              <div class="text-sm font-bold text-brand-600 whitespace-nowrap">
                {{ offer.budgetMin && offer.budgetMax ? `${offer.budgetMin}–${offer.budgetMax} DH` : offer.budgetMin ? `${offer.budgetMin} DH` : '—' }}
              </div>
            </div>

          </RouterLink>
        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const authHeaders = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

// ═══════════════════════════════════════════
// ÉTAT
// ═══════════════════════════════════════════
const profile       = ref({})
const wallet        = ref({ balance: 0, totalEarned: 0, pendingBalance: 0 })
const transactions  = ref([])
const applications  = ref([])
const recentOffers  = ref([])

const loadingProfile      = ref(true)
const loadingWallet       = ref(true)
const loadingApplications = ref(true)
const loadingOffers       = ref(true)

// ═══════════════════════════════════════════
// DATE & SALUTATION
// ═══════════════════════════════════════════
const today = new Date()
const days   = ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi']
const months = ['Jan', 'Fév', 'Mars', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc']

const dayName   = days[today.getDay()]
const dayNumber = today.getDate()
const monthName = months[today.getMonth()]
const year      = today.getFullYear()

const greeting = computed(() => {
  const h = today.getHours()
  if (h < 6)  return 'Bonne nuit'
  if (h < 12) return 'Bonne matinée'
  if (h < 18) return 'Bon après-midi'
  return 'Bonne soirée'
})

// ═══════════════════════════════════════════
// INITIALES
// ═══════════════════════════════════════════
const userInitials = computed(() => {
  const f = profile.value.firstName?.[0] || ''
  const l = profile.value.lastName?.[0] || ''
  if (f || l) return (f + l).toUpperCase()
  return (profile.value.email?.[0] || 'F').toUpperCase()
})

// ═══════════════════════════════════════════
// CANDIDATURES
// ═══════════════════════════════════════════
const recentApplications = computed(() =>
  [...applications.value]
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 5)
)

const pendingApplications = computed(() =>
  applications.value.filter(a => a.status === 'PENDING').length
)

// ═══════════════════════════════════════════
// GRAPHIQUE — regrouper transactions par mois
// ═══════════════════════════════════════════
const chartData = computed(() => {
  const monthsMap = {}

  // initialiser les 6 derniers mois
  for (let i = 5; i >= 0; i--) {
    const d = new Date(today.getFullYear(), today.getMonth() - i, 1)
    const key = `${d.getFullYear()}-${d.getMonth()}`
    monthsMap[key] = { label: months[d.getMonth()], credit: 0, debit: 0 }
  }

  transactions.value.forEach(tx => {
    if (!tx.createdAt) return
    const d = new Date(tx.createdAt)
    const key = `${d.getFullYear()}-${d.getMonth()}`
    if (!monthsMap[key]) return
    const amount = Math.abs(parseFloat(tx.amount) || 0)
    if (parseFloat(tx.amount) >= 0) {
      monthsMap[key].credit += amount
    } else {
      monthsMap[key].debit += amount
    }
  })

  return Object.values(monthsMap)
})

const chartMax    = computed(() => Math.max(...chartData.value.flatMap(m => [m.credit, m.debit]), 1))
const chartWidth  = computed(() => chartData.value.length * 90)
const colWidth    = computed(() => chartData.value.length > 0 ? chartWidth.value / chartData.value.length : 90)

const totalCredit = computed(() => chartData.value.reduce((s, m) => s + m.credit, 0))
const totalDebit  = computed(() => chartData.value.reduce((s, m) => s + m.debit, 0))

// ═══════════════════════════════════════════
// COMPLÉTUDE PROFIL
// ═══════════════════════════════════════════
const profileChecklist = computed(() => [
  { label: 'Email vérifié',          done: !!profile.value.email },
  { label: 'Titre professionnel',    done: !!profile.value.title },
  { label: 'Bio / description',      done: !!profile.value.bio },
  { label: 'Compétences renseignées', done: !!profile.value.skills },
  { label: 'Tarif horaire défini',   done: !!profile.value.hourlyRate },
  { label: 'Localisation',          done: !!profile.value.location },
])

const profileCompletion = computed(() => {
  const done = profileChecklist.value.filter(i => i.done).length
  return Math.round((done / profileChecklist.value.length) * 100)
})

// ═══════════════════════════════════════════
// HELPERS
// ═══════════════════════════════════════════
const formatAmount = (val) =>
  Number(val || 0).toLocaleString('fr-MA', { maximumFractionDigits: 2 })

const formatDate = (iso) => {
  if (!iso) return ''
  const d = new Date(iso)
  return `${d.getDate()} ${months[d.getMonth()]} ${d.getFullYear()}`
}

const statusLabel = (s) => ({
  PENDING:   'En attente',
  ACCEPTED:  'Acceptée',
  REJECTED:  'Refusée',
  WITHDRAWN: 'Retirée',
}[s] || s)

const statusStyle = (s) => ({
  PENDING:   { iconBg: 'bg-amber-100',  icon: '⏳', badge: 'bg-amber-50 text-amber-700' },
  ACCEPTED:  { iconBg: 'bg-brand-100',  icon: '✅', badge: 'bg-brand-50 text-brand-700' },
  REJECTED:  { iconBg: 'bg-red-100',    icon: '❌', badge: 'bg-red-50 text-red-600' },
  WITHDRAWN: { iconBg: 'bg-gray-100',   icon: '↩️', badge: 'bg-gray-100 text-gray-600' },
}[s] || { iconBg: 'bg-gray-100', icon: '•', badge: 'bg-gray-100 text-gray-600' })

// ═══════════════════════════════════════════
// CHARGEMENT DES DONNÉES
// ═══════════════════════════════════════════
const loadAll = async () => {
  await Promise.allSettled([
    loadProfile(),
    loadWallet(),
    loadApplications(),
    loadOffers(),
  ])
}

const loadProfile = async () => {
  loadingProfile.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/profile`, authHeaders())
    profile.value = data
  } catch {
    profile.value = {}
  } finally {
    loadingProfile.value = false
  }
}

const loadWallet = async () => {
  loadingWallet.value = true
  try {
    const [walletRes, txRes] = await Promise.all([
      axios.get(`${API_URL}/api/wallet`, authHeaders()),
      axios.get(`${API_URL}/api/wallet/transactions`, authHeaders()),
    ])
    wallet.value = walletRes.data.wallet || {}
    transactions.value = txRes.data || []
  } catch {
    wallet.value = { balance: 0, totalEarned: 0, pendingBalance: 0 }
    transactions.value = []
  } finally {
    loadingWallet.value = false
  }
}

const loadApplications = async () => {
  loadingApplications.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/applications/my`, authHeaders())
    applications.value = data || []
  } catch {
    applications.value = []
  } finally {
    loadingApplications.value = false
  }
}

const loadOffers = async () => {
  loadingOffers.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/offers`, { params: { page: 0, size: 5 } })
    recentOffers.value = (data.content || data || []).slice(0, 5)
  } catch {
    recentOffers.value = []
  } finally {
    loadingOffers.value = false
  }
}

onMounted(loadAll)
</script>
