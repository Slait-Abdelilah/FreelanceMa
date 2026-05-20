<template>
  <div class="max-w-5xl mx-auto space-y-6">

    <!-- EN-TÊTE -->
    <div>
      <h1 class="text-[22px] font-bold text-ink tracking-tight">Mon Wallet</h1>
      <p class="text-[13px] text-[#9C9A92] mt-1">Gérez votre solde et financez vos missions</p>
    </div>

    <!-- SKELETON -->
    <div v-if="loading" class="space-y-5 animate-pulse">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div v-for="i in 3" :key="i" class="bg-white rounded-2xl border border-[#EBEBE5] p-5">
          <div class="h-3 w-20 bg-[#F4F4ED] rounded mb-4"></div>
          <div class="h-8 w-32 bg-[#F4F4ED] rounded mb-2"></div>
          <div class="h-3 w-40 bg-[#F4F4ED] rounded"></div>
        </div>
      </div>
      <div class="bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5]"><div class="h-4 w-40 bg-[#F4F4ED] rounded"></div></div>
        <div class="divide-y divide-[#EBEBE5]">
          <div v-for="i in 4" :key="i" class="flex items-center gap-4 px-6 py-4">
            <div class="w-9 h-9 bg-[#F4F4ED] rounded-xl flex-shrink-0"></div>
            <div class="flex-1 space-y-1.5">
              <div class="h-3 w-36 bg-[#F4F4ED] rounded"></div>
              <div class="h-3 w-24 bg-[#F4F4ED] rounded"></div>
            </div>
            <div class="h-4 w-20 bg-[#F4F4ED] rounded"></div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="space-y-5">

      <!-- ═══ SOLDES ═══ -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">

        <!-- Solde disponible -->
        <div class="bg-white rounded-2xl border border-[#EBEBE5] p-5 relative overflow-hidden">
          <div class="absolute top-0 right-0 w-24 h-24 bg-ink/[0.02] rounded-full -translate-y-6 translate-x-6"></div>
          <div class="text-[11px] font-bold text-[#9C9A92] uppercase tracking-[0.07em] mb-3">Solde disponible</div>
          <div class="text-[32px] font-bold text-ink leading-none tabular-nums mb-1">
            {{ formatAmount(wallet.balance) }}
            <span class="text-[16px] font-normal text-[#9C9A92]">DH</span>
          </div>
          <p class="text-[12px] text-[#73726C] mb-4">Prêt à financer vos missions</p>
          <button @click="showDepositModal = true"
                  class="w-full bg-ink hover:bg-[#1A1A18] text-white text-[12px] font-semibold py-2.5 rounded-xl transition-colors flex items-center justify-center gap-1.5">
            <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
            </svg>
            Recharger le solde
          </button>
        </div>

        <!-- En escrow -->
        <div class="bg-white rounded-2xl border border-[#EBEBE5] p-5">
          <div class="text-[11px] font-bold text-[#9C9A92] uppercase tracking-[0.07em] mb-3">En cours (Escrow)</div>
          <div class="text-[32px] font-bold text-amber-600 leading-none tabular-nums mb-1">
            {{ formatAmount(wallet.pendingBalance) }}
            <span class="text-[16px] font-normal text-amber-400">DH</span>
          </div>
          <p class="text-[12px] text-[#73726C]">Bloqué pour missions en cours</p>
          <div class="mt-4 flex items-center gap-2 text-[11px] text-[#9C9A92] bg-amber-50 border border-amber-100 rounded-xl px-3 py-2">
            <svg class="w-3.5 h-3.5 text-amber-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"/>
            </svg>
            Libéré à la fin de la mission
          </div>
        </div>

        <!-- Total dépensé -->
        <div class="bg-white rounded-2xl border border-[#EBEBE5] p-5">
          <div class="text-[11px] font-bold text-[#9C9A92] uppercase tracking-[0.07em] mb-3">Total dépensé</div>
          <div class="text-[32px] font-bold text-ink leading-none tabular-nums mb-1">
            {{ formatAmount(totalSpent) }}
            <span class="text-[16px] font-normal text-[#9C9A92]">DH</span>
          </div>
          <p class="text-[12px] text-[#73726C]">Depuis votre inscription</p>
          <div class="mt-4 text-[11px] text-[#9C9A92]">
            <span class="font-semibold text-ink">{{ completedMissionsCount }}</span>
            mission{{ completedMissionsCount !== 1 ? 's' : '' }} financée{{ completedMissionsCount !== 1 ? 's' : '' }}
          </div>
        </div>

      </div>

      <!-- ═══ HISTORIQUE TRANSACTIONS ═══ -->
      <div class="bg-white rounded-2xl border border-[#EBEBE5] overflow-hidden">

        <div class="px-6 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
          <h2 class="text-[14px] font-bold text-ink">Historique</h2>
          <select v-model="selectedFilter"
                  class="text-[12px] bg-[#F4F4ED] border-0 rounded-lg px-3 py-1.5 outline-none text-[#5F5E5A] cursor-pointer">
            <option value="ALL">Toutes</option>
            <option value="DEPOSIT">Recharges</option>
            <option value="ESCROW_HOLD">Missions bloquées</option>
            <option value="ESCROW_RELEASE">Paiements effectués</option>
          </select>
        </div>

        <!-- vide -->
        <div v-if="filteredTransactions.length === 0" class="py-14 text-center">
          <div class="w-12 h-12 bg-[#F4F4ED] rounded-2xl flex items-center justify-center mx-auto mb-3">
            <svg class="w-5 h-5 text-[#B4B2A9]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
              <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 18.75a60.07 60.07 0 0115.797 2.101c.727.198 1.453-.342 1.453-1.096V18.75M3.75 4.5v.75A.75.75 0 013 6h-.75m0 0v-.375c0-.621.504-1.125 1.125-1.125H20.25M2.25 6v9m18-10.5v.75c0 .414.336.75.75.75h.75m-1.5-1.5h.375c.621 0 1.125.504 1.125 1.125v9.75c0 .621-.504 1.125-1.125 1.125h-.375m1.5-1.5H21a.75.75 0 00-.75.75v.75m0 0H3.75m0 0h-.375a1.125 1.125 0 01-1.125-1.125V15m1.5 1.5v-.75A.75.75 0 003 15h-.75"/>
            </svg>
          </div>
          <p class="text-[13px] font-semibold text-ink">Aucune transaction</p>
          <p class="text-[12px] text-[#9C9A92] mt-1">Rechargez votre solde pour commencer</p>
        </div>

        <!-- liste -->
        <div v-else class="divide-y divide-[#F4F4ED]">
          <div v-for="tx in filteredTransactions" :key="tx.id"
               class="flex items-center gap-4 px-6 py-4 hover:bg-[#FAFAF7] transition-colors">

            <!-- icône type -->
            <div class="w-9 h-9 rounded-xl flex items-center justify-center flex-shrink-0"
                 :class="txIconBg(tx.type)">
              <svg class="w-4 h-4" :class="txIconColor(tx.type)" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path v-if="tx.type === 'DEPOSIT'" stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
                <path v-else-if="tx.type === 'ESCROW_HOLD'" stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z"/>
                <path v-else-if="tx.type === 'ESCROW_RELEASE'" stroke-linecap="round" stroke-linejoin="round" d="M13.5 10.5V6.75a4.5 4.5 0 119 0v3.75M3.75 21.75h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H3.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z"/>
                <path v-else stroke-linecap="round" stroke-linejoin="round" d="M2.25 18.75a60.07 60.07 0 0115.797 2.101c.727.198 1.453-.342 1.453-1.096V18.75"/>
              </svg>
            </div>

            <div class="flex-1 min-w-0">
              <div class="text-[13px] font-semibold text-ink">{{ txLabel(tx.type) }}</div>
              <div class="text-[12px] text-[#9C9A92] mt-0.5 truncate">
                {{ tx.description || formatDate(tx.createdAt) }}
              </div>
            </div>

            <div class="text-right flex-shrink-0">
              <div class="text-[14px] font-bold tabular-nums"
                   :class="parseFloat(tx.amount) >= 0 ? 'text-green-600' : 'text-ink'">
                {{ parseFloat(tx.amount) >= 0 ? '+' : '−' }}{{ formatAmount(Math.abs(parseFloat(tx.amount))) }}
                <span class="text-[11px] font-normal text-[#9C9A92]">DH</span>
              </div>
              <div class="text-[11px] mt-0.5" :class="statusClass(tx.status)">
                {{ statusLabel(tx.status) }}
              </div>
            </div>

          </div>
        </div>

        <!-- voir plus -->
        <div v-if="!allLoaded && transactions.length >= 10"
             class="px-6 py-3.5 border-t border-[#F4F4ED] text-center">
          <button @click="loadAll" :disabled="loadingMore"
                  class="text-[12px] font-semibold text-[#73726C] hover:text-ink transition-colors disabled:opacity-50">
            {{ loadingMore ? 'Chargement...' : 'Voir toutes les transactions' }}
          </button>
        </div>

      </div>

    </div>

    <!-- ═══ TOAST ═══ -->
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast.show"
           class="fixed bottom-6 right-6 z-50 px-4 py-3 rounded-xl border text-[13px] font-semibold flex items-center gap-2.5 shadow-lg"
           :class="toast.type === 'success'
             ? 'bg-ink text-white border-transparent'
             : 'bg-white text-red-600 border-red-200'">
        <svg v-if="toast.type === 'success'" class="w-4 h-4 text-green-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 13l4 4L19 7"/>
        </svg>
        <svg v-else class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
        </svg>
        {{ toast.message }}
      </div>
    </Transition>

    <!-- ═══ MODAL RECHARGE ═══ -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0"
                leave-active-class="transition duration-150" leave-to-class="opacity-0">
      <div v-if="showDepositModal"
           class="fixed inset-0 bg-ink/40 backdrop-blur-[2px] z-50 flex items-end md:items-center justify-center p-0 md:p-4"
           @click.self="closeDepositModal">

        <div class="bg-white rounded-t-3xl md:rounded-2xl w-full md:max-w-md shadow-2xl">

          <!-- header -->
          <div class="px-6 pt-6 pb-5 border-b border-[#EBEBE5]">
            <div class="flex items-center justify-between">
              <div>
                <h2 class="text-[16px] font-bold text-ink">Recharger le solde</h2>
                <p class="text-[12px] text-[#9C9A92] mt-0.5">
                  Solde actuel :
                  <span class="font-bold text-ink">{{ formatAmount(wallet.balance) }} DH</span>
                </p>
              </div>
              <button @click="closeDepositModal"
                      class="w-8 h-8 hover:bg-[#F4F4ED] rounded-full flex items-center justify-center transition-colors">
                <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- body -->
          <div class="px-6 py-5 space-y-5">

            <!-- erreur -->
            <div v-if="depositError"
                 class="bg-red-50 border border-red-200 text-red-700 rounded-xl p-3 text-[12px] flex items-center gap-2">
              <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z"/>
              </svg>
              {{ depositError }}
            </div>

            <!-- montant -->
            <div>
              <label class="block text-[11px] font-bold text-[#9C9A92] uppercase tracking-[0.07em] mb-2">
                Montant (DH)
              </label>
              <div class="relative">
                <input v-model="depositForm.amount" type="number" min="50" placeholder="Ex: 500"
                       class="w-full border border-[#EBEBE5] focus:border-[#B4B2A9] bg-[#FAFAF7] focus:bg-white rounded-xl px-4 py-3 text-[15px] font-semibold text-ink outline-none transition-all pr-14"/>
                <span class="absolute right-4 top-1/2 -translate-y-1/2 text-[13px] font-semibold text-[#9C9A92]">DH</span>
              </div>
              <p class="text-[11px] text-[#9C9A92] mt-1.5 flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z"/>
                </svg>
                Minimum 50 DH par recharge
              </p>
            </div>

            <!-- montants rapides -->
            <div>
              <label class="block text-[11px] font-bold text-[#9C9A92] uppercase tracking-[0.07em] mb-2">
                Montants rapides
              </label>
              <div class="grid grid-cols-4 gap-2">
                <button v-for="amt in quickAmounts" :key="amt"
                        @click="depositForm.amount = amt"
                        class="py-2.5 rounded-xl border text-[12px] font-bold transition-all"
                        :class="depositForm.amount == amt
                          ? 'bg-ink text-white border-ink'
                          : 'bg-white text-[#5F5E5A] border-[#EBEBE5] hover:border-[#B4B2A9] hover:text-ink'">
                  {{ amt }}
                </button>
              </div>
            </div>

            <!-- aperçu -->
            <div v-if="depositForm.amount && depositForm.amount >= 50"
                 class="bg-[#F4F4ED] rounded-xl p-4 flex items-center justify-between">
              <div>
                <p class="text-[11px] text-[#73726C]">Nouveau solde après recharge</p>
                <p class="text-[18px] font-bold text-ink mt-0.5 tabular-nums">
                  {{ formatAmount(parseFloat(wallet.balance) + parseFloat(depositForm.amount)) }}
                  <span class="text-[12px] font-normal text-[#9C9A92]">DH</span>
                </p>
              </div>
              <svg class="w-8 h-8 text-green-500 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
            </div>

          </div>

          <!-- footer -->
          <div class="px-6 pb-6 flex gap-3">
            <button @click="closeDepositModal"
                    class="flex-1 py-3 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-semibold text-[#5F5E5A] rounded-xl transition-colors">
              Annuler
            </button>
            <button @click="confirmDeposit" :disabled="depositing || !depositForm.amount || depositForm.amount < 50"
                    class="flex-1 py-3 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-bold rounded-xl transition-colors disabled:opacity-40 flex items-center justify-center gap-2">
              <svg v-if="depositing" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ depositing ? 'Traitement...' : 'Confirmer la recharge' }}
            </button>
          </div>

        </div>
      </div>
    </Transition>
    </Teleport>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const loading         = ref(true)
const loadingMore     = ref(false)
const depositing      = ref(false)
const allLoaded       = ref(false)
const showDepositModal = ref(false)
const selectedFilter  = ref('ALL')
const depositError    = ref('')
const toast           = ref({ show: false, message: '', type: 'success' })

const wallet       = ref({ id: null, balance: 0, pendingBalance: 0, totalEarned: 0 })
const transactions = ref([])
const depositForm  = ref({ amount: null })
const quickAmounts = [100, 200, 500, 1000]

const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token') || sessionStorage.getItem('token')}` } })

// ── CHARGEMENT ──────────────────────────────────────────────────────────────

const loadWallet = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/wallet`, headers())
    wallet.value = {
      id:             data.wallet.id,
      balance:        data.wallet.balance        || 0,
      pendingBalance: data.wallet.pendingBalance || 0,
      totalEarned:    data.wallet.totalEarned    || 0,
    }
    transactions.value = data.recentTransactions || []
  } catch (err) {
    showToast('Impossible de charger le wallet', 'error')
  } finally {
    loading.value = false
  }
}

const loadAll = async () => {
  loadingMore.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/wallet/transactions`, headers())
    transactions.value = data
    allLoaded.value = true
  } catch {
    showToast('Erreur de chargement', 'error')
  } finally {
    loadingMore.value = false
  }
}

// ── DÉPÔT ───────────────────────────────────────────────────────────────────

const confirmDeposit = async () => {
  depositError.value = ''
  const amount = parseFloat(depositForm.value.amount)
  if (!amount || amount < 50) {
    depositError.value = 'Montant minimum : 50 DH'
    return
  }

  depositing.value = true
  try {
    const { data } = await axios.post(`${API_URL}/api/wallet/deposit`,
      { amount },
      headers()
    )
    wallet.value.balance = parseFloat(wallet.value.balance) + amount
    transactions.value.unshift(data)
    showToast(`+${formatAmount(amount)} DH ajoutés à votre solde !`)
    closeDepositModal()
  } catch (err) {
    depositError.value = err.response?.data?.message || 'Erreur lors de la recharge'
  } finally {
    depositing.value = false
  }
}

const closeDepositModal = () => {
  showDepositModal.value = false
  depositForm.value = { amount: null }
  depositError.value = ''
}

// ── COMPUTED ────────────────────────────────────────────────────────────────

const filteredTransactions = computed(() => {
  if (selectedFilter.value === 'ALL') return transactions.value
  return transactions.value.filter(t => t.type === selectedFilter.value)
})

// Total réellement payé aux freelancers (ESCROW_RELEASE côté client = paiement sorti)
const totalSpent = computed(() => {
  return transactions.value
    .filter(t => t.type === 'ESCROW_RELEASE')
    .reduce((sum, t) => sum + Math.abs(parseFloat(t.amount) || 0), 0)
})

const completedMissionsCount = computed(() =>
  transactions.value.filter(t => t.type === 'ESCROW_RELEASE').length
)

// ── HELPERS ─────────────────────────────────────────────────────────────────

const txLabel = (type) => ({
  DEPOSIT:        'Recharge du solde',
  ESCROW_HOLD:    'Mission bloquée en escrow',
  ESCROW_RELEASE: 'Paiement effectué au freelancer',
  WITHDRAWAL:     'Retrait',
}[type] || type)

const txIconBg = (type) => ({
  DEPOSIT:        'bg-green-50',
  ESCROW_HOLD:    'bg-amber-50',
  ESCROW_RELEASE: 'bg-blue-50',
  ESCROW_REFUND:  'bg-blue-50',
}[type] || 'bg-[#F4F4ED]')

const txIconColor = (type) => ({
  DEPOSIT:        'text-green-600',
  ESCROW_HOLD:    'text-amber-600',
  ESCROW_RELEASE: 'text-blue-600',
  ESCROW_REFUND:  'text-blue-600',
}[type] || 'text-[#9C9A92]')

const statusLabel = (s) => ({
  COMPLETED: 'Complété',
  PENDING:   'En attente',
  FAILED:    'Échoué',
  CANCELLED: 'Annulé',
}[s] || s)

const statusClass = (s) => ({
  COMPLETED: 'text-[#9C9A92]',
  PENDING:   'text-amber-600',
  FAILED:    'text-red-500',
  CANCELLED: 'text-[#9C9A92]',
}[s] || 'text-[#9C9A92]')

const formatAmount = (v) => {
  if (!v && v !== 0) return '0.00'
  return parseFloat(v).toLocaleString('fr-MA', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatDate = (d) => {
  if (!d) return '—'
  return new Date(d).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' })
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4000)
}

onMounted(loadWallet)
</script>
