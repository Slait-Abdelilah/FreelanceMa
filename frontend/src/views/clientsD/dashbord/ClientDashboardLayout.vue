
<template>
  <div class="min-h-screen bg-[#FAFAF7] flex">

    <!-- ============ SIDEBAR DESKTOP ============ -->
    <aside class="hidden lg:flex flex-col w-[260px] bg-white border-r border-[#EBEBE5] sticky top-0 h-screen">

      <!-- LOGO -->
      <div class="px-6 pt-7 pb-5">
        <RouterLink to="/" class="flex items-center gap-2.5 group">
          <div class="w-8 h-8 bg-ink rounded-lg flex items-center justify-center group-hover:rotate-6 transition-transform duration-300">
            <svg class="w-4 h-4 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 2L4 7v10l8 5 8-5V7l-8-5zm0 2.236L18 8l-6 3.764L6 8l6-3.764zM5 9.236l6 3.764v8.236l-6-3.764V9.236zm14 0v8.236l-6 3.764v-8.236l6-3.764z"/>
            </svg>
          </div>
          <div>
            <span class="text-[15px] font-semibold text-ink tracking-tight">FreelanceMa</span>
            <div class="text-[10px] text-[#9C9A92] font-medium -mt-0.5">Espace client</div>
          </div>
        </RouterLink>
      </div>

      <!-- RECHERCHE -->
      <div class="px-3 pb-3">
        <button class="w-full flex items-center gap-2.5 px-3 py-2 rounded-lg bg-[#F4F4ED] hover:bg-[#EBEBE5] transition text-left">
          <svg class="w-3.5 h-3.5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
          </svg>
          <span class="flex-1 text-[12px] text-[#73726C]">Rechercher...</span>
          <kbd class="text-[10px] font-medium text-[#9C9A92] bg-white border border-[#EBEBE5] rounded px-1.5 py-0.5">⌘K</kbd>
        </button>
      </div>

      <!-- NAVIGATION -->
      <nav class="flex-1 overflow-y-auto px-3 pb-3 custom-scroll">

        <div class="mb-1">
          <RouterLink v-for="item in mainItems" :key="item.path" :to="item.path"
            class="flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium transition-all duration-100 group relative"
            :class="isActive(item.path) ? 'bg-[#F0EFE6] text-ink' : 'text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink'">
            <span v-html="item.icon" class="w-[15px] h-[15px] flex-shrink-0"
                  :class="isActive(item.path) ? 'text-ink' : 'text-[#73726C] group-hover:text-ink'"></span>
            <span class="flex-1 truncate">{{ item.label }}</span>
          </RouterLink>
        </div>

        <!-- WORKSPACE -->
        <div class="mt-5 mb-1">
          <div class="px-3 mb-1.5">
            <span class="text-[10px] font-semibold text-[#9C9A92] uppercase tracking-[0.06em]">Workspace</span>
          </div>
          <RouterLink v-for="item in workspaceItems" :key="item.path" :to="item.path"
            class="flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium transition-all duration-100 group"
            :class="isActive(item.path) ? 'bg-[#F0EFE6] text-ink' : 'text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink'">
            <span v-html="item.icon" class="w-[15px] h-[15px] flex-shrink-0"
                  :class="isActive(item.path) ? 'text-ink' : 'text-[#73726C] group-hover:text-ink'"></span>
            <span class="flex-1 truncate">{{ item.label }}</span>
            <span v-if="item.badge"
                  class="text-[10px] font-semibold tabular-nums"
                  :class="item.urgent ? 'text-brand-600' : (isActive(item.path) ? 'text-[#73726C]' : 'text-[#9C9A92]')">
              {{ item.badge }}
            </span>
          </RouterLink>
        </div>

        <!-- COMPTE -->
        <div class="mt-5 mb-1">
          <div class="px-3 mb-1.5">
            <span class="text-[10px] font-semibold text-[#9C9A92] uppercase tracking-[0.06em]">Compte</span>
          </div>
          <RouterLink v-for="item in accountItems" :key="item.path" :to="item.path"
            class="flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium transition-all duration-100 group"
            :class="isActive(item.path) ? 'bg-[#F0EFE6] text-ink' : 'text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink'">
            <span v-html="item.icon" class="w-[15px] h-[15px] flex-shrink-0"
                  :class="isActive(item.path) ? 'text-ink' : 'text-[#73726C] group-hover:text-ink'"></span>
            <span class="flex-1 truncate">{{ item.label }}</span>
          </RouterLink>
        </div>

      </nav>

      <!-- FOOTER -->
      <div class="px-3 py-3 border-t border-[#EBEBE5] space-y-0.5">
        <button @click="logout"
                class="w-full flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium text-[#5F5E5A] hover:bg-red-50 hover:text-red-600 transition group">
          <svg class="w-[15px] h-[15px] text-[#73726C] group-hover:text-red-600 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
          </svg>
          <span>Se déconnecter</span>
        </button>
      </div>

    </aside>

    <!-- ============ MOBILE OVERLAY ============ -->
    <div v-if="mobileMenuOpen" @click="mobileMenuOpen = false"
         class="lg:hidden fixed inset-0 bg-ink/40 z-40"></div>

    <aside v-if="mobileMenuOpen"
           class="lg:hidden fixed left-0 top-0 h-full w-[280px] bg-white z-50 flex flex-col">
      <div class="px-6 pt-7 pb-5 flex items-center justify-between">
        <div class="flex items-center gap-2.5">
          <div class="w-8 h-8 bg-ink rounded-lg flex items-center justify-center">
            <svg class="w-4 h-4 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 2L4 7v10l8 5 8-5V7l-8-5zm0 2.236L18 8l-6 3.764L6 8l6-3.764zM5 9.236l6 3.764v8.236l-6-3.764V9.236zm14 0v8.236l-6 3.764v-8.236l6-3.764z"/>
            </svg>
          </div>
          <span class="text-[15px] font-semibold text-ink">FreelanceMa</span>
        </div>
        <button @click="mobileMenuOpen = false" class="p-1.5 hover:bg-[#F4F4ED] rounded-md">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>
      <nav class="flex-1 overflow-y-auto px-3 pb-4 space-y-0.5">
        <RouterLink v-for="item in allItems" :key="item.path" :to="item.path"
          @click="mobileMenuOpen = false"
          class="flex items-center gap-2.5 px-3 py-2 rounded-md text-[13px] font-medium transition"
          :class="isActive(item.path) ? 'bg-[#F0EFE6] text-ink' : 'text-[#5F5E5A] hover:bg-[#F4F4ED]'">
          <span v-html="item.icon" class="w-[15px] h-[15px]"></span>
          <span class="flex-1">{{ item.label }}</span>
          <span v-if="item.badge" class="text-[10px] text-[#9C9A92]">{{ item.badge }}</span>
        </RouterLink>
      </nav>
      <div class="px-3 py-3 border-t border-[#EBEBE5]">
        <button @click="logout" class="w-full flex items-center gap-2.5 px-3 py-2 rounded-md text-[13px] font-medium text-[#5F5E5A] hover:bg-red-50 hover:text-red-600 transition">
          <svg class="w-[15px] h-[15px]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
          </svg>
          Se déconnecter
        </button>
      </div>
    </aside>

    <!-- ============ CONTENU PRINCIPAL ============ -->
    <main class="flex-1 flex flex-col min-w-0">

      <!-- TOPBAR -->
      <header class="h-12 bg-white border-b border-[#EBEBE5] sticky top-0 z-30 flex items-center px-4 lg:px-6 gap-3">

        <button @click="mobileMenuOpen = true" class="lg:hidden p-1.5 hover:bg-[#F4F4ED] rounded-md transition">
          <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
        </button>

        <!-- BREADCRUMB -->
        <div class="flex items-center gap-1.5 text-[13px] min-w-0">
          <RouterLink to="/client/dashboard" class="text-[#9C9A92] hover:text-ink transition hidden sm:block">Client</RouterLink>
          <svg class="w-3 h-3 text-[#D3D1C7] flex-shrink-0 hidden sm:block" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
          <span class="font-medium text-ink truncate">{{ pageTitle }}</span>
        </div>

        <div class="flex-1"></div>

        <!-- ACTIONS -->
        <div class="flex items-center gap-1">

          <!-- Publier une offre -->
          <RouterLink to="/client/offers?new=true"
                      class="hidden md:flex items-center gap-1.5 bg-ink hover:bg-[#1A1A18] text-white text-[12px] font-medium px-3 py-1.5 rounded-md transition">
            <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
            </svg>
            Publier une offre
          </RouterLink>

          <div class="w-px h-4 bg-[#EBEBE5] mx-0.5"></div>

          <!-- profil -->
          <div ref="profileContainer" class="relative">
            <button @click.stop="profileOpen = !profileOpen"
                    class="flex items-center gap-1.5 px-1 py-0.5 hover:bg-[#F4F4ED] rounded-md transition">
              <div class="w-6 h-6 bg-[#F0EFE6] border border-[#EBEBE5] rounded-md flex items-center justify-center text-[10px] font-semibold text-ink">
                {{ userInitials }}
              </div>
              <span class="hidden lg:block text-[13px] font-medium text-ink max-w-[100px] truncate">{{ userFirstName }}</span>
              <svg class="w-3 h-3 text-[#9C9A92] transition-transform" :class="profileOpen ? 'rotate-180' : ''"
                   fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M19 9l-7 7-7-7"/>
              </svg>
            </button>

            <Transition enter-active-class="transition duration-100" enter-from-class="opacity-0 scale-95 -translate-y-1"
                        leave-active-class="transition duration-75" leave-to-class="opacity-0 scale-95 -translate-y-1">
              <div v-if="profileOpen" @click.stop
                   class="absolute right-0 top-full mt-1.5 w-[220px] bg-white rounded-xl border border-[#EBEBE5] shadow-lg overflow-hidden z-40">
                <div class="px-4 py-3 border-b border-[#EBEBE5]">
                  <div class="flex items-center gap-2.5">
                    <div class="w-9 h-9 bg-[#F0EFE6] border border-[#EBEBE5] rounded-lg flex items-center justify-center text-[12px] font-semibold text-ink flex-shrink-0">
                      {{ userInitials }}
                    </div>
                    <div class="min-w-0">
                      <div class="text-[13px] font-semibold text-ink truncate">{{ userName }}</div>
                      <div class="text-[11px] text-[#9C9A92] truncate">{{ userEmail }}</div>
                    </div>
                  </div>
                </div>
                <div class="py-1.5">
                  <RouterLink to="/client/settings" @click="profileOpen = false"
                              class="flex items-center gap-2.5 px-3 py-1.5 mx-1.5 rounded-md text-[13px] text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink transition">
                    <svg class="w-[14px] h-[14px] text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.324.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 011.37.49l1.296 2.247a1.125 1.125 0 01-.26 1.431l-1.003.827c-.293.241-.438.613-.43.992a6.759 6.759 0 010 .255c-.008.378.137.75.43.991l1.004.827c.424.35.534.955.26 1.43l-1.298 2.247a1.125 1.125 0 01-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.57 6.57 0 01-.22.128c-.331.183-.581.495-.644.869l-.213 1.28c-.09.543-.56.941-1.11.941h-2.594c-.55 0-1.02-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 01-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 01-1.369-.49l-1.297-2.247a1.125 1.125 0 01.26-1.431l1.004-.827c.292-.24.437-.613.43-.991a6.932 6.932 0 010-.255c.007-.38-.138-.751-.43-.992l-1.004-.827a1.125 1.125 0 01-.26-1.43l1.297-2.247a1.125 1.125 0 011.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.087.22-.128.332-.183.582-.495.644-.869l.214-1.28z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                    </svg>
                    Paramètres
                  </RouterLink>
                </div>
                <div class="border-t border-[#EBEBE5] py-1.5">
                  <button @click="logout; profileOpen = false"
                          class="w-full flex items-center gap-2.5 px-3 py-1.5 text-left rounded-md text-[13px] text-red-600 hover:bg-red-50 transition mx-1.5" style="width: calc(100% - 12px);">
                    <svg class="w-[14px] h-[14px]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
                    </svg>
                    Se déconnecter
                  </button>
                </div>
              </div>
            </Transition>
          </div>

        </div>
      </header>

      <!-- CONTENU -->
      <div class="flex-1 px-5 lg:px-8 py-6 lg:py-8">
        <RouterView />
      </div>

    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import axios from 'axios'

const route  = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const mobileMenuOpen = ref(false)
const profileOpen    = ref(false)
const profileContainer = ref(null)

const userEmail = computed(() => authStore.user?.email || '')
const userName  = computed(() => {
  const name = userEmail.value.split('@')[0].replace(/[._]/g, ' ')
  return name.split(' ').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')
})
const userFirstName = computed(() => userName.value.split(' ')[0])
const userInitials  = computed(() =>
  userName.value.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2) || '—'
)

const isActive = (path) => {
  if (path === '/client/dashboard') return route.path === '/client/dashboard' || route.path === '/client'
  return route.path.startsWith(path)
}

const pageTitle = computed(() => ({
  '/client/dashboard':   "Vue d'ensemble",
  '/client/offers':      'Mes offres',
  '/client/applications':'Candidatures',
  '/client/missions':     'Missions actives',
  '/client/freelancers':  'Explorer les freelancers',
  '/client/settings':     'Paramètres',
}[route.path] || 'Dashboard'))

// ── Badges réels ────────────────────────────────────────────────────────
const openOffersCount   = ref(null)
const pendingAppsCount  = ref(null)

const loadCounts = async () => {
  const token = localStorage.getItem('token')
  const h = { Authorization: `Bearer ${token}` }
  try {
    const { data: offers } = await axios.get('http://localhost:8080/api/offers/my', { headers: h })
    const open = offers.filter(o => o.status === 'OPEN')
    openOffersCount.value = open.length

    // total candidatures en attente sur toutes les offres ouvertes
    const appsResults = await Promise.allSettled(
      open.map(o => axios.get(`http://localhost:8080/api/offers/${o.id}/applications`, { headers: h }))
    )
    let pending = 0
    appsResults.forEach(r => {
      if (r.status === 'fulfilled')
        pending += r.value.data.filter(a => a.status === 'PENDING').length
    })
    pendingAppsCount.value = pending
  } catch { /* silencieux */ }
}

const fmt = (n) => n === null ? null : n > 99 ? '99+' : n > 0 ? String(n) : null

// ── Nav items ────────────────────────────────────────────────────────────
const mainItems = [
  { path: '/client/dashboard', label: "Vue d'ensemble",
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M3.75 12h16.5m-16.5 3.75h16.5M3.75 19.5h16.5M5.625 4.5h12.75a1.875 1.875 0 010 3.75H5.625a1.875 1.875 0 010-3.75z"/></svg>' },
]

const workspaceItems = computed(() => [
  { path: '/client/offers', label: 'Mes offres',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z"/></svg>',
    badge: fmt(openOffersCount.value) },
  { path: '/client/applications', label: 'Candidatures reçues',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z"/></svg>',
    badge: fmt(pendingAppsCount.value), urgent: pendingAppsCount.value > 0 },
  { path: '/client/missions', label: 'Missions actives',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4"/></svg>' },
  { path: '/client/freelancers', label: 'Explorer les freelancers',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M18 18.72a9.094 9.094 0 003.741-.479 3 3 0 00-4.682-2.72m.94 3.198l.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0112 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 016 18.719m12 0a5.971 5.971 0 00-.941-3.197m0 0A5.995 5.995 0 0012 12.75a5.995 5.995 0 00-5.058 2.772m0 0a3 3 0 00-4.681 2.72 8.986 8.986 0 003.74.477m.94-3.197a5.971 5.971 0 00-.94 3.197M15 6.75a3 3 0 11-6 0 3 3 0 016 0zm6 3a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0zm-13.5 0a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0z"/></svg>' },
])

const accountItems = [
  { path: '/client/settings', label: 'Paramètres',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.324.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 011.37.49l1.296 2.247a1.125 1.125 0 01-.26 1.431l-1.003.827c-.293.241-.438.613-.43.992a6.759 6.759 0 010 .255c-.008.378.137.75.43.991l1.004.827c.424.35.534.955.26 1.43l-1.298 2.247a1.125 1.125 0 01-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.57 6.57 0 01-.22.128c-.331.183-.581.495-.644.869l-.213 1.28c-.09.543-.56.941-1.11.941h-2.594c-.55 0-1.02-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 01-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 01-1.369-.49l-1.297-2.247a1.125 1.125 0 01.26-1.431l1.004-.827c.292-.24.437-.613.43-.991a6.932 6.932 0 010-.255c.007-.38-.138-.751-.43-.992l-1.004-.827a1.125 1.125 0 01-.26-1.43l1.297-2.247a1.125 1.125 0 011.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.087.22-.128.332-.183.582-.495.644-.869l.214-1.28z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>' },
]

const allItems = computed(() => [...mainItems, ...workspaceItems.value, ...accountItems])

const logout = async () => {
  await authStore.logout()
  router.push('/login/client')
}

const handleClickOutside = (e) => {
  if (profileContainer.value && !profileContainer.value.contains(e.target))
    profileOpen.value = false
}

let pollInterval = null
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  loadCounts()
  pollInterval = setInterval(loadCounts, 30000)
})
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  clearInterval(pollInterval)
})
</script>

<style scoped>
.custom-scroll::-webkit-scrollbar { width: 4px; }
.custom-scroll::-webkit-scrollbar-track { background: transparent; }
.custom-scroll::-webkit-scrollbar-thumb { background: #D3D1C7; border-radius: 4px; }
.custom-scroll::-webkit-scrollbar-thumb:hover { background: #B4B2A9; }
</style>
