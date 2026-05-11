<template>
  <div class="max-w-5xl mx-auto space-y-5">

    <!-- EN-TÊTE -->
    <div>
      <h1 class="text-xl font-bold text-ink">Wallet</h1>
      <p class="text-[13px] text-[#73726C] mt-0.5">Vos gains et retraits</p>
    </div>

    <!-- LOADING SKELETON -->
    <div v-if="loading" class="space-y-5 animate-pulse">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div v-for="i in 3" :key="i" class="bg-white rounded-xl border border-[#EBEBE5] p-5">
          <div class="h-3 w-24 bg-[#EBEBE5] rounded mb-4"></div>
          <div class="h-8 w-32 bg-[#F4F4ED] rounded mb-2"></div>
          <div class="h-3 w-40 bg-[#F4F4ED] rounded"></div>
        </div>
      </div>
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">
        <div class="px-6 py-4 border-b border-[#EBEBE5]"><div class="h-4 w-48 bg-[#EBEBE5] rounded"></div></div>
        <div class="divide-y divide-[#EBEBE5]">
          <div v-for="i in 5" :key="i" class="flex items-center gap-4 px-6 py-4">
            <div class="w-8 h-8 bg-[#F4F4ED] rounded-lg flex-shrink-0"></div>
            <div class="flex-1 space-y-1.5">
              <div class="h-3 w-36 bg-[#EBEBE5] rounded"></div>
              <div class="h-3 w-24 bg-[#F4F4ED] rounded"></div>
            </div>
            <div class="h-4 w-20 bg-[#F4F4ED] rounded"></div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="space-y-5">

      <!-- ===== SOLDES ===== -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">

        <!-- solde disponible -->
        <div class="bg-white rounded-xl border border-[#EBEBE5] p-5">
          <div class="text-xs text-[#9C9A92] mb-3">Solde disponible</div>
          <div class="text-3xl font-bold text-ink tabular-nums mb-1">
            {{ formatAmount(wallet.balance) }}
            <span class="text-[14px] font-normal text-[#73726C]">DH</span>
          </div>
          <div class="text-[12px] text-[#73726C] mb-4">Peut être retiré maintenant</div>
          <button @click="showWithdrawModal = true"
                  class="w-full bg-ink hover:bg-[#1A1A18] text-white text-[12px] font-semibold py-2 rounded-lg transition">
            Demander un retrait
          </button>
        </div>

        <!-- solde en attente -->
        <div class="bg-white rounded-xl border border-[#EBEBE5] p-5">
          <div class="text-xs text-[#9C9A92] mb-3">En attente (Escrow)</div>
          <div class="text-3xl font-bold text-ink tabular-nums mb-1">
            {{ formatAmount(wallet.pendingBalance) }}
            <span class="text-[14px] font-normal text-[#73726C]">DH</span>
          </div>
          <div class="text-[12px] text-[#73726C]">Libéré après validation du client</div>
        </div>

        <!-- total gagné -->
        <div class="bg-white rounded-xl border border-[#EBEBE5] p-5">
          <div class="text-xs text-[#9C9A92] mb-3">Total gagné</div>
          <div class="text-3xl font-bold text-ink tabular-nums mb-1">
            {{ formatAmount(wallet.totalEarned) }}
            <span class="text-[14px] font-normal text-[#73726C]">DH</span>
          </div>
          <div class="text-[12px] text-[#73726C]">Depuis votre inscription</div>
        </div>

      </div>

      <!-- ===== TRANSACTIONS ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">

        <div class="px-6 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
          <h2 class="text-[14px] font-semibold text-ink">Historique</h2>
          <select v-model="selectedFilter"
                  class="text-[12px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-1.5 outline-none text-[#5F5E5A]">
            <option value="ALL">Toutes</option>
            <option value="DEPOSIT">Dépôts</option>
            <option value="WITHDRAWAL">Retraits</option>
            <option value="ESCROW_HOLD">Escrow bloqué</option>
            <option value="ESCROW_RELEASE">Escrow libéré</option>
          </select>
        </div>

        <div v-if="filteredTransactions.length === 0" class="py-12 text-center">
          <div class="text-[13px] text-[#73726C]">Aucune transaction</div>
        </div>

        <div v-else class="divide-y divide-[#EBEBE5]">
          <div v-for="transaction in filteredTransactions" :key="transaction.id"
               class="flex items-center gap-4 px-6 py-4">

            <!-- direction icon -->
            <div class="w-8 h-8 rounded-lg border border-[#EBEBE5] flex items-center justify-center flex-shrink-0">
              <svg v-if="isPositive(transaction.type)" class="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7"/>
              </svg>
              <svg v-else class="w-4 h-4 text-[#5F5E5A]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M5 15l7-7 7 7"/>
              </svg>
            </div>

            <div class="flex-1 min-w-0">
              <div class="text-[13px] font-medium text-ink">{{ getTransactionLabel(transaction.type) }}</div>
              <div class="text-[12px] text-[#9C9A92] mt-0.5 truncate">{{ transaction.description || formatDate(transaction.createdAt) }}</div>
            </div>

            <div class="text-right flex-shrink-0">
              <div class="text-[13px] font-semibold tabular-nums"
                   :class="isPositive(transaction.type) ? 'text-green-700' : 'text-ink'">
                {{ isPositive(transaction.type) ? '+' : '−' }}{{ formatAmount(Math.abs(transaction.amount)) }} DH
              </div>
              <div class="text-[11px] mt-0.5" :class="getStatusClass(transaction.status)">
                {{ getStatusLabel(transaction.status) }}
              </div>
            </div>

          </div>
        </div>

        <div v-if="!allLoaded && transactions.length >= 10" class="px-6 py-3 border-t border-[#EBEBE5]">
          <button @click="loadAllTransactions" :disabled="loadingMore"
                  class="w-full text-[12px] font-medium text-[#73726C] hover:text-ink py-2 transition disabled:opacity-50">
            {{ loadingMore ? 'Chargement...' : 'Voir toutes les transactions' }}
          </button>
        </div>

      </div>

    </div>

    <!-- TOAST -->
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

    <!-- ===== MODAL RETRAIT ===== -->
    <div v-if="showWithdrawModal"
         class="fixed inset-0 bg-ink/50 z-50 flex items-center justify-center p-4"
         @click.self="closeWithdrawModal">

      <div class="bg-white rounded-xl w-full max-w-md p-6 border border-[#EBEBE5]">

        <div class="flex items-center justify-between mb-5">
          <div>
            <h2 class="text-[15px] font-semibold text-ink">Demande de retrait</h2>
            <p class="text-xs text-[#9C9A92] mt-0.5">
              Disponible : <span class="font-semibold text-ink tabular-nums">{{ formatAmount(wallet.balance) }} DH</span>
            </p>
          </div>
          <button @click="closeWithdrawModal" class="p-1.5 hover:bg-[#F4F4ED] rounded-md transition">
            <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div v-if="withdrawError" class="bg-red-50 border border-red-200 text-red-700 rounded-lg p-3 mb-4 text-[12px]">
          {{ withdrawError }}
        </div>

        <div class="space-y-4">

          <div>
            <label class="block text-xs text-[#9C9A92] mb-1.5">Montant (DH)</label>
            <div class="relative">
              <input v-model="withdrawForm.amount" type="number" min="100" :max="wallet.balance" placeholder="Ex: 500"
                     class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition pr-10"/>
              <span class="absolute right-3 top-1/2 -translate-y-1/2 text-[12px] text-[#9C9A92]">DH</span>
            </div>
            <p class="text-[11px] text-[#9C9A92] mt-1">Minimum : 100 DH</p>
            <div class="flex gap-2 mt-2">
              <button v-for="amount in quickAmounts" :key="amount"
                      @click="withdrawForm.amount = amount" :disabled="wallet.balance < amount"
                      class="text-[11px] font-medium px-2.5 py-1 rounded-md border transition disabled:opacity-30"
                      :class="withdrawForm.amount == amount
                        ? 'bg-ink text-white border-ink'
                        : 'border-[#EBEBE5] text-[#5F5E5A] hover:border-[#5F5E5A]'">
                {{ amount }}
              </button>
              <button @click="withdrawForm.amount = wallet.balance" :disabled="!wallet.balance"
                      class="text-[11px] font-medium px-2.5 py-1 rounded-md border border-[#EBEBE5] text-[#5F5E5A] hover:border-[#5F5E5A] transition disabled:opacity-30">
                Tout
              </button>
            </div>
          </div>

          <div>
            <label class="block text-xs text-[#9C9A92] mb-1.5">IBAN / RIB</label>
            <input v-model="withdrawForm.iban" type="text" placeholder="MA64011519000001234567890"
                   class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-lg px-3 py-2.5 text-[13px] outline-none transition font-mono"/>
            <p class="text-[11px] text-[#9C9A92] mt-1">Virement sous 2–3 jours ouvrables</p>
          </div>

        </div>

        <div class="flex gap-3 mt-6">
          <button @click="closeWithdrawModal"
                  class="flex-1 py-2.5 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-lg transition">
            Annuler
          </button>
          <button @click="requestWithdrawal" :disabled="withdrawing"
                  class="flex-1 py-2.5 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
            <svg v-if="withdrawing" class="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ withdrawing ? 'Traitement...' : 'Confirmer' }}
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

const loading = ref(true)
const loadingMore = ref(false)
const withdrawing = ref(false)
const showWithdrawModal = ref(false)
const allLoaded = ref(false)
const selectedFilter = ref('ALL')
const withdrawError = ref('')
const toast = ref({ show: false, message: '', type: 'success' })

const wallet = ref({ id: null, balance: 0, pendingBalance: 0, totalEarned: 0, createdAt: null })
const transactions = ref([])
const withdrawForm = ref({ amount: null, iban: '' })
const quickAmounts = [100, 500, 1000, 2000]

const filteredTransactions = computed(() => {
  if (selectedFilter.value === 'ALL') return transactions.value
  return transactions.value.filter(t => t.type === selectedFilter.value)
})

const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } })

const loadWallet = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/wallet`, headers())
    wallet.value = {
      id: data.wallet.id,
      balance: data.wallet.balance || 0,
      pendingBalance: data.wallet.pendingBalance || 0,
      totalEarned: data.wallet.totalEarned || 0,
      createdAt: data.wallet.createdAt,
    }
    transactions.value = data.recentTransactions || []
  } catch (err) {
    console.error('loadWallet error:', err)
    showToast('Impossible de charger le wallet', 'error')
  } finally {
    loading.value = false
  }
}

const loadAllTransactions = async () => {
  loadingMore.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/wallet/transactions`, headers())
    transactions.value = data
    allLoaded.value = true
  } catch {
    showToast('Erreur lors du chargement', 'error')
  } finally {
    loadingMore.value = false
  }
}

const requestWithdrawal = async () => {
  withdrawError.value = ''
  if (!withdrawForm.value.amount || withdrawForm.value.amount < 100) { withdrawError.value = 'Montant minimum : 100 DH'; return }
  if (withdrawForm.value.amount > wallet.value.balance) { withdrawError.value = 'Solde insuffisant'; return }
  if (!withdrawForm.value.iban.trim()) { withdrawError.value = 'Entrez votre IBAN/RIB'; return }

  withdrawing.value = true
  try {
    const { data } = await axios.post(`${API_URL}/api/wallet/withdraw`,
      { amount: parseFloat(withdrawForm.value.amount), iban: withdrawForm.value.iban.trim() },
      headers()
    )
    wallet.value.balance -= parseFloat(withdrawForm.value.amount)
    transactions.value.unshift(data)
    showToast('Demande de retrait envoyée !')
    closeWithdrawModal()
  } catch (err) {
    withdrawError.value = err.response?.data?.message || 'Erreur lors du retrait'
  } finally {
    withdrawing.value = false
  }
}

const formatAmount = (amount) => {
  if (!amount && amount !== 0) return '0.00'
  return parseFloat(amount).toLocaleString('fr-MA', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' })
}

const isPositive = (type) => ['DEPOSIT', 'ESCROW_RELEASE', 'ESCROW_REFUND'].includes(type)

const getTransactionLabel = (type) => {
  const labels = { DEPOSIT: 'Paiement reçu', WITHDRAWAL: 'Retrait bancaire', ESCROW_HOLD: 'Fonds bloqués', ESCROW_RELEASE: 'Fonds libérés', ESCROW_REFUND: 'Remboursement', COMMISSION: 'Commission' }
  return labels[type] || type
}

const getStatusLabel = (status) => {
  const labels = { PENDING: 'En attente', COMPLETED: 'Complété', FAILED: 'Échoué', CANCELLED: 'Annulé' }
  return labels[status] || status
}

const getStatusClass = (status) => {
  const classes = { PENDING: 'text-amber-600', COMPLETED: 'text-[#9C9A92]', FAILED: 'text-red-600', CANCELLED: 'text-[#9C9A92]' }
  return classes[status] || 'text-[#9C9A92]'
}

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4000)
}

const closeWithdrawModal = () => {
  showWithdrawModal.value = false
  withdrawForm.value = { amount: null, iban: '' }
  withdrawError.value = ''
}

onMounted(() => { loadWallet() })
</script>
