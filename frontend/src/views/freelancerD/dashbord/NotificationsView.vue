<template>
  <div class="space-y-5" style="max-width: calc(100% - 40px); margin: 0 auto;">

    <!-- HEADER -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-[20px] font-semibold text-ink tracking-tight">Notifications</h1>
        <p class="text-[13px] text-[#9C9A92] mt-0.5">
          <span v-if="unreadCount > 0">{{ unreadCount }} non lue{{ unreadCount > 1 ? 's' : '' }}</span>
          <span v-else>Tout est à jour</span>
        </p>
      </div>
      <div class="flex items-center gap-2">
        <button v-if="unreadCount > 0"
                @click="markAllRead"
                class="text-[12px] font-medium text-[#5F5E5A] hover:text-ink border border-[#EBEBE5] hover:border-[#D3D1C7] px-3 py-1.5 rounded-md transition">
          Tout marquer comme lu
        </button>
      </div>
    </div>

    <!-- FILTER TABS -->
    <div class="flex gap-1 bg-[#F4F4ED] p-1 rounded-lg w-fit">
      <button v-for="tab in tabs" :key="tab.value"
              @click="activeTab = tab.value"
              class="text-[12px] font-medium px-3 py-1 rounded-md transition"
              :class="activeTab === tab.value
                ? 'bg-white text-ink shadow-sm'
                : 'text-[#73726C] hover:text-ink'">
        {{ tab.label }}
        <span v-if="tab.value === 'unread' && unreadCount > 0"
              class="ml-1 text-[10px] bg-ink text-white rounded-full px-1.5 py-0.5">
          {{ unreadCount }}
        </span>
      </button>
    </div>

    <!-- SKELETON -->
    <div v-if="loading" class="space-y-2">
      <div v-for="i in 5" :key="i" class="bg-white border border-[#EBEBE5] rounded-xl p-4 flex gap-3 animate-pulse">
        <div class="w-9 h-9 bg-[#F4F4ED] rounded-lg flex-shrink-0"></div>
        <div class="flex-1 space-y-2">
          <div class="h-3.5 bg-[#F4F4ED] rounded w-2/5"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-4/5"></div>
          <div class="h-3 bg-[#F4F4ED] rounded w-1/4"></div>
        </div>
      </div>
    </div>

    <!-- LISTE -->
    <div v-else-if="filteredNotifs.length > 0" class="space-y-1">

      <template v-for="(group, gIdx) in groupedNotifs" :key="gIdx">
        <!-- séparateur de date -->
        <div class="flex items-center gap-3 py-2" :class="gIdx > 0 ? 'mt-3' : ''">
          <span class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider">{{ group.label }}</span>
          <div class="flex-1 h-px bg-[#EBEBE5]"></div>
        </div>

        <!-- notification item -->
        <div v-for="notif in group.items" :key="notif.id"
             class="group bg-white border rounded-xl p-4 flex gap-3 transition cursor-pointer"
             :class="notif.isRead
               ? 'border-[#EBEBE5] hover:border-[#D3D1C7]'
               : 'border-[#EBEBE5] bg-[#FAFAF7] hover:border-[#D3D1C7]'"
             @click="handleClick(notif)">

          <!-- icône type -->
          <div class="w-9 h-9 rounded-lg flex-shrink-0 flex items-center justify-center border"
               :class="typeStyle(notif.type).bg + ' ' + typeStyle(notif.type).border">
            <span v-html="typeStyle(notif.type).icon" class="w-4 h-4" :class="typeStyle(notif.type).color"></span>
          </div>

          <!-- contenu -->
          <div class="flex-1 min-w-0">
            <div class="flex items-start justify-between gap-2">
              <p class="text-[13px] font-semibold text-ink leading-tight">{{ notif.title }}</p>
              <div class="flex items-center gap-1.5 flex-shrink-0">
                <!-- point non lu -->
                <span v-if="!notif.isRead" class="w-2 h-2 bg-ink rounded-full flex-shrink-0"></span>
                <!-- supprimer -->
                <button @click.stop="deleteNotif(notif.id)"
                        class="opacity-0 group-hover:opacity-100 w-5 h-5 flex items-center justify-center text-[#9C9A92] hover:text-red-500 transition rounded">
                  <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
                  </svg>
                </button>
              </div>
            </div>
            <p class="text-[12px] text-[#73726C] mt-0.5 leading-relaxed">{{ notif.message }}</p>
            <p class="text-[11px] text-[#9C9A92] mt-1.5">{{ timeAgo(notif.createdAt) }}</p>
          </div>

        </div>
      </template>

    </div>

    <!-- EMPTY STATE -->
    <div v-else class="bg-white border border-[#EBEBE5] rounded-xl p-12 text-center">
      <div class="w-12 h-12 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-4">
        <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
          <path stroke-linecap="round" stroke-linejoin="round" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/>
        </svg>
      </div>
      <p class="text-[14px] font-semibold text-ink">
        {{ activeTab === 'unread' ? 'Aucune notification non lue' : 'Aucune notification' }}
      </p>
      <p class="text-[12px] text-[#9C9A92] mt-1">
        {{ activeTab === 'unread' ? 'Vous êtes à jour.' : 'Les notifications apparaîtront ici.' }}
      </p>
    </div>

    <!-- TOAST -->
    <Transition enter-active-class="transition duration-200"
                enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150"
                leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast"
           class="fixed bottom-5 left-1/2 -translate-x-1/2 bg-ink text-white text-[13px] font-medium px-4 py-2.5 rounded-xl shadow-lg z-50">
        {{ toast }}
      </div>
    </Transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const notifications = ref([])
const loading = ref(true)
const activeTab = ref('all')
const toast = ref('')

const tabs = [
  { value: 'all', label: 'Toutes' },
  { value: 'unread', label: 'Non lues' },
]

const unreadCount = computed(() => notifications.value.filter(n => !n.isRead).length)

const filteredNotifs = computed(() => {
  if (activeTab.value === 'unread') return notifications.value.filter(n => !n.isRead)
  return notifications.value
})

const groupedNotifs = computed(() => {
  const now = new Date()
  const todayStr = now.toDateString()
  const yesterdayStr = new Date(now - 86400000).toDateString()

  const groups = {}
  for (const n of filteredNotifs.value) {
    const d = new Date(n.createdAt)
    const ds = d.toDateString()
    const label = ds === todayStr ? "Aujourd'hui" : ds === yesterdayStr ? 'Hier' : formatDate(d)
    if (!groups[label]) groups[label] = []
    groups[label].push(n)
  }
  return Object.entries(groups).map(([label, items]) => ({ label, items }))
})

const typeStyle = (type) => {
  const styles = {
    APPLICATION_SUBMITTED: {
      bg: 'bg-blue-50',
      border: 'border-blue-100',
      color: 'text-blue-500',
      icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M6 12L3.269 3.126A59.768 59.768 0 0121.485 12 59.77 59.77 0 013.27 20.876L5.999 12zm0 0h7.5"/></svg>'
    },
    APPLICATION_ACCEPTED: {
      bg: 'bg-green-50',
      border: 'border-green-100',
      color: 'text-green-500',
      icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>'
    },
    APPLICATION_REJECTED: {
      bg: 'bg-red-50',
      border: 'border-red-100',
      color: 'text-red-400',
      icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9.75 9.75l4.5 4.5m0-4.5l-4.5 4.5M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>'
    },
    APPLICATION_WITHDRAWN: {
      bg: 'bg-[#F4F4ED]',
      border: 'border-[#EBEBE5]',
      color: 'text-[#73726C]',
      icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9 15L3 9m0 0l6-6M3 9h12a6 6 0 010 12h-3"/></svg>'
    },
    MISSION_COMPLETED: {
      bg: 'bg-amber-50',
      border: 'border-amber-100',
      color: 'text-amber-500',
      icon: '<svg fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M11.48 3.499a.562.562 0 011.04 0l2.125 5.111a.563.563 0 00.475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 00-.182.557l1.285 5.385a.562.562 0 01-.84.61l-4.725-2.885a.563.563 0 00-.586 0L6.982 20.54a.562.562 0 01-.84-.61l1.285-5.386a.562.562 0 00-.182-.557l-4.204-3.602a.562.562 0 01.321-.988l5.518-.442a.563.563 0 00.475-.345L11.48 3.5z"/></svg>'
    },
  }
  return styles[type] || styles['APPLICATION_SUBMITTED']
}

const token = () => localStorage.getItem('token')
const headers = () => ({ Authorization: `Bearer ${token()}` })

const load = async () => {
  loading.value = true
  try {
    const { data } = await axios.get('http://localhost:8080/api/notifications', { headers: headers() })
    notifications.value = data
  } catch {
    showToast('Erreur de chargement')
  } finally {
    loading.value = false
  }
}

const handleClick = async (notif) => {
  if (!notif.isRead) {
    try {
      await axios.put(`http://localhost:8080/api/notifications/${notif.id}/read`, {}, { headers: headers() })
      notif.isRead = true
    } catch { /* silencieux */ }
  }
}

const markAllRead = async () => {
  try {
    await axios.put('http://localhost:8080/api/notifications/read-all', {}, { headers: headers() })
    notifications.value.forEach(n => { n.isRead = true })
    showToast('Toutes les notifications marquées comme lues')
  } catch {
    showToast('Erreur')
  }
}

const deleteNotif = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/api/notifications/${id}`, { headers: headers() })
    notifications.value = notifications.value.filter(n => n.id !== id)
  } catch {
    showToast('Erreur lors de la suppression')
  }
}

const timeAgo = (iso) => {
  const diff = Date.now() - new Date(iso).getTime()
  const m = Math.floor(diff / 60000)
  if (m < 1) return "À l'instant"
  if (m < 60) return `Il y a ${m} min`
  const h = Math.floor(m / 60)
  if (h < 24) return `Il y a ${h}h`
  const d = Math.floor(h / 24)
  return `Il y a ${d} jour${d > 1 ? 's' : ''}`
}

const formatDate = (d) => {
  return d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'long' })
}

let toastTimer = null
const showToast = (msg) => {
  toast.value = msg
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => { toast.value = '' }, 3000)
}

onMounted(load)
</script>
