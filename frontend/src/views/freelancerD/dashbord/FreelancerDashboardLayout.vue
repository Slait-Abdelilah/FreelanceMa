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
          <span class="text-[15px] font-semibold text-ink tracking-tight">FreelanceMa</span>
        </RouterLink>
      </div>

      <!-- BARRE DE RECHERCHE -->
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

        <!-- SECTION : OVERVIEW -->
        <div class="mb-1">
          <RouterLink
              v-for="item in mainItems"
              :key="item.path"
              :to="item.path"
              class="flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium transition-all duration-100 group relative"
              :class="isActive(item.path)
              ? 'bg-[#F0EFE6] text-ink'
              : 'text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink'"
          >
            <span v-html="item.icon" class="w-[15px] h-[15px] flex-shrink-0 transition"
                  :class="isActive(item.path) ? 'text-ink' : 'text-[#73726C] group-hover:text-ink'"></span>
            <span class="flex-1 truncate">{{ item.label }}</span>
            <span v-if="item.badge"
                  class="text-[10px] font-semibold tabular-nums"
                  :class="isActive(item.path) ? 'text-[#73726C]' : 'text-[#9C9A92]'">
              {{ item.badge }}
            </span>
          </RouterLink>
        </div>

        <!-- SECTION : WORKSPACE -->
        <div class="mt-5 mb-1">
          <div class="px-3 mb-1.5 flex items-center justify-between group">
            <span class="text-[10px] font-semibold text-[#9C9A92] uppercase tracking-[0.06em]">Workspace</span>
            <button class="opacity-0 group-hover:opacity-100 transition w-4 h-4 flex items-center justify-center text-[#9C9A92] hover:text-ink rounded">
              <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
              </svg>
            </button>
          </div>
          <RouterLink
              v-for="item in workspaceItems"
              :key="item.path"
              :to="item.path"
              class="flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium transition-all duration-100 group"
              :class="isActive(item.path)
              ? 'bg-[#F0EFE6] text-ink'
              : 'text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink'"
          >
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

        <!-- SECTION : COMMUNICATION -->
        <div class="mt-5 mb-1">
          <div class="px-3 mb-1.5">
            <span class="text-[10px] font-semibold text-[#9C9A92] uppercase tracking-[0.06em]">Communication</span>
          </div>
          <RouterLink
              v-for="item in communicationItems"
              :key="item.path"
              :to="item.path"
              class="flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium transition-all duration-100 group"
              :class="isActive(item.path)
              ? 'bg-[#F0EFE6] text-ink'
              : 'text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink'"
          >
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

        <!-- SECTION : ACCOUNT -->
        <div class="mt-5 mb-1">
          <div class="px-3 mb-1.5">
            <span class="text-[10px] font-semibold text-[#9C9A92] uppercase tracking-[0.06em]">Compte</span>
          </div>
          <RouterLink
              v-for="item in accountItems"
              :key="item.path"
              :to="item.path"
              class="flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium transition-all duration-100 group"
              :class="isActive(item.path)
              ? 'bg-[#F0EFE6] text-ink'
              : 'text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink'"
          >
            <span v-html="item.icon" class="w-[15px] h-[15px] flex-shrink-0"
                  :class="isActive(item.path) ? 'text-ink' : 'text-[#73726C] group-hover:text-ink'"></span>
            <span class="flex-1 truncate">{{ item.label }}</span>
          </RouterLink>
        </div>

      </nav>

      <!-- FOOTER : AIDE + DÉCONNEXION -->
      <div class="px-3 py-3 border-t border-[#EBEBE5] space-y-0.5">

        <RouterLink to="/freelancer/help"
                    class="flex items-center gap-2.5 px-3 py-1.5 rounded-md text-[13px] font-medium text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink transition group">
          <svg class="w-[15px] h-[15px] text-[#73726C] group-hover:text-ink transition" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9 5.25h.008v.008H12v-.008z"/>
          </svg>
          <span>Centre d'aide</span>
        </RouterLink>

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
    <div v-if="mobileMenuOpen"
         @click="mobileMenuOpen = false"
         class="lg:hidden fixed inset-0 bg-ink/40 z-40 backdrop-blur-sm"></div>

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

      <nav class="flex-1 overflow-y-auto px-3 pb-4">
        <div v-for="(section, sIdx) in allSections" :key="sIdx" class="mb-5">
          <div v-if="section.title" class="px-3 mb-1.5">
            <span class="text-[10px] font-semibold text-[#9C9A92] uppercase tracking-[0.06em]">{{ section.title }}</span>
          </div>
          <RouterLink
              v-for="item in section.items"
              :key="item.path"
              :to="item.path"
              @click="mobileMenuOpen = false"
              class="flex items-center gap-2.5 px-3 py-2 rounded-md text-[13px] font-medium transition"
              :class="isActive(item.path) ? 'bg-[#F0EFE6] text-ink' : 'text-[#5F5E5A] hover:bg-[#F4F4ED]'"
          >
            <span v-html="item.icon" class="w-[15px] h-[15px]"></span>
            <span class="flex-1">{{ item.label }}</span>
            <span v-if="item.badge" class="text-[10px] font-semibold text-[#9C9A92]">{{ item.badge }}</span>
          </RouterLink>
        </div>
      </nav>

      <!-- footer mobile -->
      <div class="px-3 py-3 border-t border-[#EBEBE5] space-y-0.5">
        <RouterLink to="/freelancer/help"
                    @click="mobileMenuOpen = false"
                    class="flex items-center gap-2.5 px-3 py-2 rounded-md text-[13px] font-medium text-[#5F5E5A] hover:bg-[#F4F4ED] transition">
          <svg class="w-[15px] h-[15px] text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9 5.25h.008v.008H12v-.008z"/>
          </svg>
          <span>Centre d'aide</span>
        </RouterLink>
        <button @click="logout"
                class="w-full flex items-center gap-2.5 px-3 py-2 rounded-md text-[13px] font-medium text-[#5F5E5A] hover:bg-red-50 hover:text-red-600 transition">
          <svg class="w-[15px] h-[15px] text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
          </svg>
          <span>Se déconnecter</span>
        </button>
      </div>
    </aside>

    <!-- ============ CONTENU PRINCIPAL ============ -->
    <main class="flex-1 flex flex-col min-w-0">

      <!-- TOPBAR -->
      <header class="h-12 bg-white border-b border-[#EBEBE5] sticky top-0 z-30 flex items-center px-4 lg:px-6 gap-3">

        <!-- burger mobile -->
        <button @click="mobileMenuOpen = true"
                class="lg:hidden p-1.5 hover:bg-[#F4F4ED] rounded-md transition">
          <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
        </button>

        <!-- BREADCRUMB -->
        <div class="flex items-center gap-1.5 text-[13px] min-w-0">
          <RouterLink to="/freelancer/dashboard"
                      class="text-[#9C9A92] hover:text-ink transition hidden sm:block">
            Freelancer
          </RouterLink>
          <svg class="w-3 h-3 text-[#D3D1C7] flex-shrink-0 hidden sm:block" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
          <span class="font-medium text-ink truncate">{{ pageTitle }}</span>
        </div>

        <div class="flex-1"></div>

        <!-- ACTIONS DROITE -->
        <div class="flex items-center gap-1">

          <!-- disponibilité -->
          <button @click="available = !available"
                  class="hidden md:flex items-center gap-1.5 px-2.5 py-1 rounded-md border transition text-[12px] font-medium"
                  :class="available
                    ? 'border-[#EBEBE5] text-[#5F5E5A] hover:border-[#D3D1C7]'
                    : 'border-[#EBEBE5] text-[#9C9A92] hover:border-[#D3D1C7]'">
            <span class="w-1.5 h-1.5 rounded-full flex-shrink-0 transition-colors"
                  :class="available ? 'bg-green-500' : 'bg-[#D3D1C7]'"></span>
            {{ available ? 'Disponible' : 'Indisponible' }}
          </button>

          <div class="hidden md:block w-px h-4 bg-[#EBEBE5] mx-0.5"></div>

          <!-- explorer missions -->
          <RouterLink to="/freelancer/explore"
                      class="hidden md:flex items-center gap-1.5 bg-ink hover:bg-[#1A1A18] text-white text-[12px] font-medium px-3 py-1.5 rounded-md transition">
            <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
            </svg>
            Explorer
          </RouterLink>

          <div class="w-px h-4 bg-[#EBEBE5] mx-0.5"></div>

          <!-- messages -->
          <RouterLink to="/freelancer/messages"
                      class="relative p-1.5 hover:bg-[#F4F4ED] rounded-md transition group">
            <svg class="w-4 h-4 text-[#73726C] group-hover:text-ink transition" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
          </RouterLink>

          <!-- notifications -->
          <RouterLink to="/freelancer/notifications"
                      class="relative p-1.5 hover:bg-[#F4F4ED] rounded-md transition group">
            <svg class="w-4 h-4 text-[#73726C] group-hover:text-ink transition" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/>
            </svg>
            <span v-if="unreadCount > 0"
                  class="absolute top-0.5 right-0.5 w-[14px] h-[14px] bg-ink text-white text-[8px] font-bold rounded-full flex items-center justify-center ring-2 ring-white">
              {{ unreadCount > 9 ? '9+' : unreadCount }}
            </span>
          </RouterLink>

          <div class="w-px h-4 bg-[#EBEBE5] mx-0.5"></div>

          <!-- profil -->
          <div ref="profileContainer" class="relative">
            <button @click.stop="profileOpen = !profileOpen"
                    class="flex items-center gap-1.5 px-1 py-0.5 hover:bg-[#F4F4ED] rounded-md transition">
              <!-- avatar initiales -->
              <div class="w-6 h-6 bg-[#F0EFE6] border border-[#EBEBE5] rounded-md flex items-center justify-center text-[10px] font-semibold text-ink">
                {{ userInitials }}
              </div>
              <span class="hidden lg:block text-[13px] font-medium text-ink max-w-[100px] truncate">{{ userFirstName }}</span>
              <svg class="w-3 h-3 text-[#9C9A92] transition-transform"
                   :class="profileOpen ? 'rotate-180' : ''"
                   fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M19 9l-7 7-7-7"/>
              </svg>
            </button>

            <!-- dropdown profil -->
            <Transition enter-active-class="transition duration-100" enter-from-class="opacity-0 scale-95 -translate-y-1"
                        leave-active-class="transition duration-75" leave-to-class="opacity-0 scale-95 -translate-y-1">
              <div v-if="profileOpen"
                   @click.stop
                   class="absolute right-0 top-full mt-1.5 w-[240px] bg-white rounded-xl border border-[#EBEBE5] shadow-lg overflow-hidden z-40">

                <!-- user info -->
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

                <!-- navigation links -->
                <div class="py-1.5">
                  <RouterLink v-for="item in profileMenu"
                              :key="item.path"
                              :to="item.path"
                              @click="profileOpen = false"
                              class="flex items-center gap-2.5 px-3 py-1.5 mx-1.5 rounded-md text-[13px] text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink transition">
                    <span v-html="item.icon" class="w-[14px] h-[14px] text-[#9C9A92] flex-shrink-0"></span>
                    <span class="flex-1">{{ item.label }}</span>
                    <kbd v-if="item.shortcut" class="text-[10px] text-[#9C9A92] bg-[#F4F4ED] rounded px-1 py-0.5">{{ item.shortcut }}</kbd>
                  </RouterLink>
                </div>

                <!-- déconnexion -->
                <div class="border-t border-[#EBEBE5] py-1.5">
                  <button @click="logout; profileOpen = false"
                          class="w-full flex items-center gap-2.5 px-3 py-1.5 mx-auto text-left rounded-md text-[13px] text-red-600 hover:bg-red-50 transition" style="width: calc(100% - 12px); margin: 0 6px;">
                    <svg class="w-[14px] h-[14px] flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
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
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const mobileMenuOpen = ref(false)
const profileOpen = ref(false)
const profileContainer = ref(null)
const available = ref(true)

// infos utilisateur
const authStore = useAuthStore()
const userEmail = computed(() => authStore.user?.email || '')
const userName = computed(() => {
  const name = userEmail.value.split('@')[0].replace(/[._]/g, ' ')
  return name.split(' ').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')
})
const userFirstName = computed(() => userName.value.split(' ')[0])
const userInitials = computed(() => {
  return userName.value.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2) || '—'
})

// route active
const isActive = (path) => {
  if (path === '/freelancer/dashboard') {
    return route.path === '/freelancer/dashboard' || route.path === '/freelancer'
  }
  return route.path.startsWith(path)
}

// titre dynamique
const pageTitle = computed(() => {
  const titles = {
    '/freelancer/dashboard': 'Vue d\'ensemble',
    '/freelancer/explore': 'Explorer',
    '/freelancer/applications': 'Candidatures',
    '/freelancer/active-missions': 'Missions actives',
    '/freelancer/favorites': 'Favoris',
    '/freelancer/messages': 'Messages',
    '/freelancer/notifications': 'Notifications',
    '/freelancer/profile': 'Profil',
    '/freelancer/portfolio': 'Portfolio',
    '/freelancer/wallet': 'Wallet',
    '/freelancer/settings': 'Paramètres',
    '/freelancer/help': 'Aide',
  }
  return titles[route.path] || 'Dashboard'
})

// MENU PRINCIPAL
const mainItems = [
  {
    path: '/freelancer/dashboard',
    label: 'Vue d\'ensemble',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M3.75 12h16.5m-16.5 3.75h16.5M3.75 19.5h16.5M5.625 4.5h12.75a1.875 1.875 0 010 3.75H5.625a1.875 1.875 0 010-3.75z"/></svg>'
  },
]

const workspaceItems = [
  {
    path: '/freelancer/explore',
    label: 'Explorer',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>',
    badge: '42'
  },
  {
    path: '/freelancer/applications',
    label: 'Mes candidatures',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>',
    badge: '5'
  },
  {
    path: '/freelancer/active-missions',
    label: 'Missions actives',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/></svg>',
    badge: '3',
    urgent: true
  },
  {
    path: '/freelancer/favorites',
    label: 'Favoris',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 011.04 0l2.125 5.111a.563.563 0 00.475.345l5.518.442c.499.04.701.663.32.988l-4.204 3.602a.563.563 0 00-.182.557l1.285 5.385a.562.562 0 01-.84.61l-4.725-2.885a.563.563 0 00-.586 0L6.982 20.54a.562.562 0 01-.84-.61l1.285-5.386a.562.562 0 00-.182-.557l-4.204-3.602a.562.562 0 01.321-.988l5.518-.442a.563.563 0 00.475-.345L11.48 3.5z"/></svg>'
  },
]

const communicationItems = [
  {
    path: '/freelancer/messages',
    label: 'Messages',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/></svg>',
    badge: '3',
    urgent: true
  },
  {
    path: '/freelancer/notifications',
    label: 'Notifications',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/></svg>'
  },
]

const accountItems = [
  {
    path: '/freelancer/profile',
    label: 'Profil',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/></svg>'
  },
  {
    path: '/freelancer/portfolio',
    label: 'Portfolio',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 001.5-1.5V6a1.5 1.5 0 00-1.5-1.5H3.75A1.5 1.5 0 002.25 6v12a1.5 1.5 0 001.5 1.5zm10.5-11.25h.008v.008h-.008V8.25zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z"/></svg>'
  },
  {
    path: '/freelancer/wallet',
    label: 'Wallet',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M21 12a2.25 2.25 0 00-2.25-2.25H15a3 3 0 11-6 0H5.25A2.25 2.25 0 003 12m18 0v6a2.25 2.25 0 01-2.25 2.25H5.25A2.25 2.25 0 013 18v-6m18 0V9M3 12V9m18 0a2.25 2.25 0 00-2.25-2.25H5.25A2.25 2.25 0 003 9m18 0V6a2.25 2.25 0 00-2.25-2.25H5.25A2.25 2.25 0 003 6v3"/></svg>'
  },
  {
    path: '/freelancer/settings',
    label: 'Paramètres',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.324.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 011.37.49l1.296 2.247a1.125 1.125 0 01-.26 1.431l-1.003.827c-.293.241-.438.613-.43.992a6.759 6.759 0 010 .255c-.008.378.137.75.43.991l1.004.827c.424.35.534.955.26 1.43l-1.298 2.247a1.125 1.125 0 01-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.57 6.57 0 01-.22.128c-.331.183-.581.495-.644.869l-.213 1.28c-.09.543-.56.941-1.11.941h-2.594c-.55 0-1.02-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 01-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 01-1.369-.49l-1.297-2.247a1.125 1.125 0 01.26-1.431l1.004-.827c.292-.24.437-.613.43-.991a6.932 6.932 0 010-.255c.007-.38-.138-.751-.43-.992l-1.004-.827a1.125 1.125 0 01-.26-1.43l1.297-2.247a1.125 1.125 0 011.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.087.22-.128.332-.183.582-.495.644-.869l.214-1.28z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>'
  },
]

// menu profil dropdown (sans déconnexion car déjà dans sidebar)
const profileMenu = [
  {
    path: '/freelancer/profile',
    label: 'Mon profil',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/></svg>',
    shortcut: 'P'
  },
  {
    path: '/freelancer/wallet',
    label: 'Mon wallet',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M21 12a2.25 2.25 0 00-2.25-2.25H15a3 3 0 11-6 0H5.25A2.25 2.25 0 003 12m18 0v6a2.25 2.25 0 01-2.25 2.25H5.25A2.25 2.25 0 013 18v-6m18 0V9M3 12V9"/></svg>',
    shortcut: 'W'
  },
  {
    path: '/freelancer/settings',
    label: 'Paramètres',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M10.343 3.94c.09-.542.56-.94 1.11-.94h1.093c.55 0 1.02.398 1.11.94l.149.894c.07.424.384.764.78.93.398.164.855.142 1.205-.108l.737-.527a1.125 1.125 0 011.45.12l.773.774c.39.389.44 1.002.12 1.45l-.527.737c-.25.35-.272.806-.107 1.204.165.397.505.71.93.78l.893.15c.543.09.94.56.94 1.109v1.094c0 .55-.397 1.02-.94 1.11l-.893.149c-.425.07-.765.383-.93.78-.165.398-.143.854.107 1.204l.527.738c.32.447.269 1.06-.12 1.45l-.774.773a1.125 1.125 0 01-1.449.12l-.738-.527c-.35-.25-.806-.272-1.203-.107-.397.165-.71.505-.78.93l-.15.893c-.09.543-.56.94-1.11.94h-1.094c-.55 0-1.019-.398-1.11-.94l-.148-.894c-.071-.424-.384-.764-.781-.93-.398-.164-.854-.142-1.204.108l-.738.527c-.447.32-1.06.269-1.45-.12l-.773-.774a1.125 1.125 0 01-.12-1.45l.527-.737c.25-.35.273-.806.108-1.204-.165-.397-.505-.71-.93-.78l-.894-.15c-.542-.09-.94-.56-.94-1.109v-1.094c0-.55.398-1.02.94-1.11l.894-.149c.424-.07.765-.383.93-.78.165-.398.143-.854-.107-1.204l-.527-.738a1.125 1.125 0 01.12-1.45l.773-.773a1.125 1.125 0 011.45-.12l.737.527c.35.25.807.272 1.204.107.397-.165.71-.505.78-.93l.15-.893z"/><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>',
    shortcut: ','
  },
  {
    path: '/freelancer/help',
    label: 'Aide & support',
    icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9 5.25h.008v.008H12v-.008z"/></svg>'
  },
]

const unreadCount = ref(0)

const loadUnreadCount = async () => {
  try {
    const token = localStorage.getItem('token')
    const { data } = await axios.get('http://localhost:8080/api/notifications/unread-count', {
      headers: { Authorization: `Bearer ${token}` }
    })
    unreadCount.value = data.count || 0
  } catch {
    // silencieux — badge non critique
  }
}

let pollInterval = null

// pour mobile
const allSections = computed(() => [
  { title: '', items: mainItems },
  { title: 'Workspace', items: workspaceItems },
  { title: 'Communication', items: communicationItems },
  { title: 'Compte', items: accountItems },
])

const logout = async () => {
  await authStore.logout()
  router.push('/login/freelancer')
}

const handleClickOutside = (e) => {
  if (profileContainer.value && !profileContainer.value.contains(e.target)) {
    profileOpen.value = false
  }
}

// reset badge when navigating to notifications page
watch(() => route.path, (path) => {
  if (path === '/freelancer/notifications') {
    setTimeout(() => { unreadCount.value = 0 }, 800)
  }
})

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  loadUnreadCount()
  pollInterval = setInterval(loadUnreadCount, 30000)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  clearInterval(pollInterval)
})


</script>

<style scoped>
.custom-scroll::-webkit-scrollbar {
  width: 4px;
}
.custom-scroll::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scroll::-webkit-scrollbar-thumb {
  background: #D3D1C7;
  border-radius: 4px;
}
.custom-scroll::-webkit-scrollbar-thumb:hover {
  background: #B4B2A9;
}
</style>