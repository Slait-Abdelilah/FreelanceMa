<template>
  <div class="flex h-[calc(100vh-120px)] bg-white border border-[#EBEBE5] rounded-xl overflow-hidden">

    <!-- ═══ SIDEBAR CONVERSATIONS ═══ -->
    <div class="w-72 flex-shrink-0 border-r border-[#EBEBE5] flex flex-col">
      <div class="p-4 border-b border-[#EBEBE5]">
        <h2 class="text-[14px] font-semibold text-ink">Messages</h2>
        <p class="text-[11px] text-[#9C9A92] mt-0.5">{{ conversations.length }} conversation{{ conversations.length !== 1 ? 's' : '' }}</p>
      </div>

      <!-- Skeleton -->
      <div v-if="loadingConvs" class="p-3 space-y-2">
        <div v-for="i in 3" :key="i" class="animate-pulse flex items-center gap-3 p-2">
          <div class="w-9 h-9 bg-[#F4F4ED] rounded-full flex-shrink-0"></div>
          <div class="flex-1 space-y-1.5">
            <div class="h-3 bg-[#F4F4ED] rounded w-3/4"></div>
            <div class="h-2.5 bg-[#F4F4ED] rounded w-1/2"></div>
          </div>
        </div>
      </div>

      <!-- Liste -->
      <div v-else-if="conversations.length > 0" class="flex-1 overflow-y-auto">
        <button v-for="conv in conversations" :key="conv.id"
                @click="selectConversation(conv)"
                class="w-full text-left flex items-center gap-3 px-4 py-3 border-b border-[#EBEBE5] transition hover:bg-[#F4F4ED]"
                :class="selectedConv?.id === conv.id ? 'bg-[#F4F4ED]' : ''">
          <div class="w-9 h-9 bg-[#F0EFE6] border border-[#EBEBE5] rounded-full flex items-center justify-center text-[11px] font-bold text-ink flex-shrink-0">
            {{ conv.initials }}
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-[12px] font-semibold text-ink truncate">{{ conv.offerTitle }}</p>
            <p class="text-[11px] text-[#9C9A92] truncate">{{ conv.otherLabel }}</p>
          </div>
          <span class="w-2 h-2 rounded-full flex-shrink-0"
                :class="conv.status === 'ACCEPTED' ? 'bg-green-400' : conv.status === 'AWAITING_VALIDATION' ? 'bg-amber-400' : 'bg-[#D3D1C7]'">
          </span>
        </button>
      </div>

      <!-- Vide -->
      <div v-else class="flex-1 flex items-center justify-center p-6 text-center">
        <div>
          <div class="w-10 h-10 bg-[#F4F4ED] rounded-xl flex items-center justify-center mx-auto mb-3">
            <svg class="w-5 h-5 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
          </div>
          <p class="text-[12px] font-semibold text-ink">Aucune mission active</p>
          <p class="text-[11px] text-[#9C9A92] mt-1">Les conversations apparaissent dès qu'une candidature est acceptée</p>
        </div>
      </div>
    </div>

    <!-- ═══ ZONE CHAT ═══ -->
    <div class="flex-1 flex flex-col min-w-0">

      <!-- Aucune conv sélectionnée -->
      <div v-if="!selectedConv" class="flex-1 flex items-center justify-center">
        <div class="text-center">
          <div class="w-14 h-14 bg-[#F4F4ED] rounded-2xl flex items-center justify-center mx-auto mb-4">
            <svg class="w-7 h-7 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
          </div>
          <p class="text-[14px] font-semibold text-ink">Sélectionnez une conversation</p>
          <p class="text-[12px] text-[#9C9A92] mt-1">Choisissez une mission pour démarrer</p>
        </div>
      </div>

      <template v-else>
        <!-- Header chat -->
        <div class="flex items-center gap-3 px-5 py-3.5 border-b border-[#EBEBE5] flex-shrink-0">
          <div class="w-8 h-8 bg-[#F0EFE6] border border-[#EBEBE5] rounded-full flex items-center justify-center text-[11px] font-bold text-ink">
            {{ selectedConv.initials }}
          </div>
          <div>
            <p class="text-[13px] font-semibold text-ink">{{ selectedConv.offerTitle }}</p>
            <p class="text-[11px] text-[#9C9A92]">{{ selectedConv.otherLabel }}</p>
          </div>
          <span class="ml-auto text-[10px] font-medium px-2 py-0.5 rounded-full border"
                :class="selectedConv.status === 'ACCEPTED' ? 'bg-green-50 text-green-600 border-green-100'
                       : selectedConv.status === 'AWAITING_VALIDATION' ? 'bg-amber-50 text-amber-600 border-amber-200'
                       : 'bg-[#F4F4ED] text-[#9C9A92] border-[#EBEBE5]'">
            {{ statusLabel(selectedConv.status) }}
          </span>
        </div>

        <!-- Messages -->
        <div ref="messagesContainer" class="flex-1 overflow-y-auto p-4 space-y-3">

          <!-- Skeleton chargement -->
          <div v-if="loadingMessages" class="space-y-3">
            <div v-for="i in 4" :key="i" class="flex" :class="i % 2 === 0 ? 'justify-end' : ''">
              <div class="animate-pulse max-w-xs">
                <div class="h-9 bg-[#F4F4ED] rounded-2xl w-48"></div>
              </div>
            </div>
          </div>

          <!-- Pas de messages -->
          <div v-else-if="messages.length === 0" class="flex items-center justify-center h-full">
            <div class="text-center">
              <p class="text-[13px] font-medium text-ink">Démarrez la conversation</p>
              <p class="text-[12px] text-[#9C9A92] mt-1">Envoyez votre premier message ci-dessous</p>
            </div>
          </div>

          <!-- Liste messages -->
          <template v-else>
            <div v-for="msg in messages" :key="msg.id"
                 class="flex" :class="msg.senderId === myId ? 'justify-end' : 'justify-start'">
              <div class="max-w-[70%]">
                <div class="px-3.5 py-2 rounded-2xl text-[13px] leading-relaxed"
                     :class="msg.senderId === myId
                       ? 'bg-ink text-white rounded-br-sm'
                       : 'bg-[#F4F4ED] text-ink rounded-bl-sm'">
                  {{ msg.content }}
                </div>
                <p class="text-[10px] text-[#9C9A92] mt-1"
                   :class="msg.senderId === myId ? 'text-right' : 'text-left'">
                  {{ formatTime(msg.createdAt) }}
                </p>
              </div>
            </div>
          </template>
        </div>

        <!-- Input message -->
        <div class="px-4 py-3 border-t border-[#EBEBE5] flex-shrink-0">
          <div class="flex items-end gap-2">
            <textarea v-model="newMessage"
                      @keydown.enter.exact.prevent="sendMessage"
                      rows="1"
                      placeholder="Écrivez votre message... (Entrée pour envoyer)"
                      class="flex-1 resize-none text-[13px] text-ink bg-[#F4F4ED] rounded-xl px-3.5 py-2.5 outline-none placeholder:text-[#9C9A92] focus:bg-white focus:ring-1 focus:ring-[#EBEBE5] transition max-h-28 overflow-y-auto"
                      @input="autoResize">
            </textarea>
            <button @click="sendMessage"
                    :disabled="!newMessage.trim() || sending"
                    class="w-9 h-9 rounded-xl bg-ink flex items-center justify-center hover:bg-[#1A1A18] disabled:opacity-40 transition flex-shrink-0">
              <svg v-if="sending" class="w-4 h-4 text-white animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              <svg v-else class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 12L3.269 3.126A59.768 59.768 0 0121.485 12 59.77 59.77 0 013.27 20.876L5.999 12zm0 0h7.5"/>
              </svg>
            </button>
          </div>
        </div>
      </template>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import axios from 'axios'
import { db, auth } from '@/firebase'
import { signInWithCustomToken } from 'firebase/auth'
import {
  collection, addDoc, onSnapshot,
  query, orderBy, serverTimestamp
} from 'firebase/firestore'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

// Décoder le userId depuis le JWT
const myId = computed(() => {
  try {
    const token = localStorage.getItem('token')
    if (!token) return null
    const payload = JSON.parse(atob(token.split('.')[1]))
    return String(payload.userId)
  } catch { return null }
})

const userRole = JSON.parse(localStorage.getItem('user') || '{}').role || ''

// ── STATE ────────────────────────────────────────────────────────────────────
const conversations    = ref([])
const loadingConvs     = ref(true)
const selectedConv     = ref(null)
const selectedConvId   = ref(null)
const messages         = ref([])
const loadingMessages  = ref(false)
const newMessage       = ref('')
const sending          = ref(false)
const messagesContainer = ref(null)
let unsubscribeMessages = null

// ── LOAD CONVERSATIONS ───────────────────────────────────────────────────────
const loadConversations = async () => {
  loadingConvs.value = true
  try {
    const ACTIVE = ['ACCEPTED', 'AWAITING_VALIDATION', 'COMPLETED']

    if (userRole === 'FREELANCER') {
      const { data } = await axios.get(`${API_URL}/api/applications/my`, headers())
      conversations.value = data
        .filter(a => ACTIVE.includes(a.status))
        .map(a => ({
          id:         a.id,
          offerTitle: a.offerTitle || 'Mission',
          status:     a.status,
          otherLabel: 'Client',
          initials:   'CL',
        }))
    } else {
      // CLIENT
      const { data: offers } = await axios.get(`${API_URL}/api/offers/my`, headers())
      const results = await Promise.allSettled(
        offers.map(o =>
          axios.get(`${API_URL}/api/offers/${o.id}/applications`, headers())
            .then(r => r.data
              .filter(a => ACTIVE.includes(a.status))
              .map(a => ({
                id:         a.id,
                offerTitle: o.title,
                status:     a.status,
                otherLabel: `Freelancer #${String(a.freelancerId).slice(-3)}`,
                initials:   `F${String(a.freelancerId).slice(-2)}`,
              }))
            )
        )
      )
      const all = []
      results.forEach(r => { if (r.status === 'fulfilled') all.push(...r.value) })
      conversations.value = all
    }
  } catch (e) {
    console.error('Failed to load conversations', e)
  } finally {
    loadingConvs.value = false
  }
}

// ── SELECT CONVERSATION ──────────────────────────────────────────────────────
const selectConversation = async (conv) => {
  if (unsubscribeMessages) {
    unsubscribeMessages()
    unsubscribeMessages = null
  }

  selectedConv.value  = conv
  loadingMessages.value = true
  messages.value = []

  try {
    // Obtenir le token Firebase + conversationId
    const { data } = await axios.get(
      `${API_URL}/api/messages/token?applicationId=${conv.id}`, headers()
    )

    // Se connecter à Firebase
    await signInWithCustomToken(auth, data.firebaseToken)
    selectedConvId.value = data.conversationId

    // Écouter les messages en temps réel
    const q = query(
      collection(db, 'conversations', data.conversationId, 'messages'),
      orderBy('createdAt', 'asc')
    )
    unsubscribeMessages = onSnapshot(q,
      (snapshot) => {
        messages.value = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }))
        loadingMessages.value = false
        scrollToBottom()
      },
      (err) => {
        console.error('Firestore snapshot error:', err)
        loadingMessages.value = false
      }
    )
  } catch (e) {
    console.error('Failed to open conversation', e)
    loadingMessages.value = false
  }
}

// ── SEND MESSAGE ─────────────────────────────────────────────────────────────
const sendMessage = async () => {
  const content = newMessage.value.trim()
  if (!content || !selectedConvId.value || sending.value) return

  sending.value = true
  newMessage.value = ''

  try {
    await addDoc(
      collection(db, 'conversations', selectedConvId.value, 'messages'),
      {
        senderId:   myId.value,
        senderRole: userRole,
        content,
        createdAt:  serverTimestamp(),
        read:       false,
      }
    )
  } catch (e) {
    console.error('Failed to send message', e)
    newMessage.value = content
  } finally {
    sending.value = false
  }
}

// ── HELPERS ──────────────────────────────────────────────────────────────────
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const autoResize = (e) => {
  const el = e.target
  el.style.height = 'auto'
  el.style.height = Math.min(el.scrollHeight, 112) + 'px'
}

const formatTime = (ts) => {
  if (!ts) return ''
  const d = ts.toDate ? ts.toDate() : new Date(ts)
  const now = new Date()
  const isToday = d.toDateString() === now.toDateString()
  if (isToday) return d.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
  return d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short' }) +
         ' ' + d.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
}

const statusLabel = (s) => ({ ACCEPTED: 'En cours', AWAITING_VALIDATION: 'À valider', COMPLETED: 'Terminée' }[s] || s)

// ── LIFECYCLE ────────────────────────────────────────────────────────────────
onMounted(loadConversations)
onUnmounted(() => { if (unsubscribeMessages) unsubscribeMessages() })
</script>
