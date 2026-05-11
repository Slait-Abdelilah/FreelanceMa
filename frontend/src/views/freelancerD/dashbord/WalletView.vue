<template>
  <div class="max-w-5xl mx-auto space-y-5">

    <!-- EN-TÊTE -->
    <div>
      <h1 class="text-xl font-bold text-ink">Mon Wallet</h1>
      <p class="text-[13px] text-[#73726C] mt-0.5">Gérez vos gains et vos retraits</p>
    </div>

    <!-- LOADING -->
    <div v-if="loading" class="flex justify-center py-16">
      <div class="w-6 h-6 border-2 border-brand-500 border-t-transparent rounded-full animate-spin"></div>
    </div>

    <div v-else class="space-y-5">

      <!-- ===== BLOC 1 : SOLDES ===== -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">

        <!-- solde disponible -->
        <div class="bg-ink rounded-xl p-5 text-white relative overflow-hidden">
          <div class="absolute top-0 right-0 w-32 h-32 bg-brand-500/20 rounded-full blur-2xl"></div>
          <div class="relative">
            <div class="text-[11px] font-semibold text-gray-400 uppercase tracking-wider mb-3">
              Solde disponible
            </div>
            <div class="text-3xl font-bold mb-1">
              {{ formatAmount(wallet.balance) }}
              <span class="text-[14px] font-normal text-gray-400">DH</span>
            </div>
            <div class="text-[12px] text-gray-400 mt-2">Peut être retiré maintenant</div>
            <button @click="showWithdrawModal = true"
                    class="mt-4 w-full bg-brand-500 hover:bg-brand-600 text-white text-[12px] font-semibold py-2 rounded-lg transition">
              Retirer
            </button>
          </div>
        </div>

        <!-- solde en attente -->
        <div class="bg-white rounded-xl border border-[#EBEBE5] p-5">
          <div class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-3">
            En attente (Escrow)
          </div>
          <div class="text-3xl font-bold text-ink mb-1">
            {{ formatAmount(wallet.pendingBalance) }}
            <span class="text-[14px] font-normal text-[#73726C]">DH</span>
          </div>
          <div class="text-[12px] text-[#73726C] mt-2">
            Libéré après validation du client
          </div>
          <div class="mt-4 flex items-center gap-1.5">
            <div class="w-2 h-2 bg-amber-500 rounded-full animate-pulse"></div>
            <span class="text-[11px] text-amber-600 font-medium">En cours de traitement</span>
          </div>
        </div>

        <!-- total gagné -->
        <div class="bg-white rounded-xl border border-[#EBEBE5] p-5">
          <div class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-3">
            Total gagné
          </div>
          <div class="text-3xl font-bold text-ink mb-1">
            {{ formatAmount(wallet.totalEarned) }}
            <span class="text-[14px] font-normal text-[#73726C]">DH</span>
          </div>
          <div class="text-[12px] text-[#73726C] mt-2">Depuis votre inscription</div>
          <div class="mt-4 flex items-center gap-1.5">
            <svg class="w-4 h-4 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"/>
            </svg>
            <span class="text-[11px] text-brand-600 font-medium">Progression continue</span>
          </div>
        </div>

      </div>

      <!-- ===== BLOC 2 : TRANSACTIONS ===== -->
      <div class="bg-white rounded-xl border border-[#EBEBE5] overflow-hidden">

        <!-- header -->
        <div class="px-6 py-4 border-b border-[#EBEBE5] flex items-center justify-between">
          <h2 class="text-[14px] font-bold text-ink">Historique des transactions</h2>

          <!-- filtre -->
          <div class="flex items-center gap-2">
            <select v-model="selectedFilter"
                    class="text-[12px] bg-white border border-[#EBEBE5] rounded-lg px-3 py-1.5 outline-none focus:border-brand-500 transition">
              <option value="ALL">Toutes</option>
              <option value="DEPOSIT">Dépôts</option>
              <option value="WITHDRAWAL">Retraits</option>
              <option value="ESCROW_HOLD">Escrow bloqué</option>
              <option value="ESCROW_RELEASE">Escrow libéré</option>
            </select>
          </div>
        </div>

        <!-- liste transactions -->
        <div v-if="filteredTransactions.length === 0"
             class="py-12 text-center">
          <div class="w-12 h-12 bg-[#F4F4ED] rounded-full flex items-center justify-center mx-auto mb-3">
            <svg class="w-6 h-6 text-[#9C9A92]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 8.25h19.5M2.25 9h19.5m-16.5 5.25h6m-6 2.25h3m-3.75 3h15a2.25 2.25 0 002.25-2.25V6.75A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25v10.5A2.25 2.25 0 004.5 19.5z"/>
            </svg>
          </div>
          <h3 class="text-[14px] font-semibold text-ink mb-1">Aucune transaction</h3>
          <p class="text-[12px] text-[#73726C]">Vos transactions apparaîtront ici</p>
        </div>

        <div v-else>
          <div v-for="transaction in filteredTransactions"
               :key="transaction.id"
               class="flex items-center gap-4 px-6 py-4 border-b border-[#EBEBE5] last:border-0 hover:bg-[#FAFAF7] transition">

            <!-- icône transaction -->
            <div class="w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0"
                 :class="getTransactionIconBg(transaction.type)">
              <span class="text-lg">{{ getTransactionIcon(transaction.type) }}</span>
            </div>

            <!-- infos -->
            <div class="flex-1 min-w-0">
              <div class="text-[13px] font-semibold text-ink">
                {{ getTransactionLabel(transaction.type) }}
              </div>
              <div class="text-[12px] text-[#73726C] mt-0.5 truncate">
                {{ transaction.description || '—' }}
              </div>
              <div class="text-[11px] text-[#9C9A92] mt-0.5">
                {{ formatDate(transaction.createdAt) }}
              </div>
            </div>

            <!-- statut -->
            <div class="text-right flex-shrink-0">
              <div class="text-[14px] font-bold"
                   :class="isPositive(transaction.type) ? 'text-brand-600' : 'text-ink'">
                {{ isPositive(transaction.type) ? '+' : '' }}{{ formatAmount(Math.abs(transaction.amount)) }} DH
              </div>
              <span class="text-[10px] font-semibold px-2 py-0.5 rounded-full"
                    :class="getStatusClass(transaction.status)">
                {{ getStatusLabel(transaction.status) }}
              </span>
            </div>

          </div>
        </div>

        <!-- bouton voir plus -->
        <div v-if="!allLoaded && transactions.length >= 10"
             class="px-6 py-3 border-t border-[#EBEBE5]">
          <button @click="loadAllTransactions"
                  :disabled="loadingMore"
                  class="w-full text-[12px] font-semibold text-ink hover:bg-[#F4F4ED] py-2 rounded-lg transition disabled:opacity-50">
            {{ loadingMore ? 'Chargement...' : 'Voir toutes les transactions' }}
          </button>
        </div>

      </div>

    </div>

    <!-- TOAST -->
    <div v-if="toast.show"
         class="fixed bottom-6 right-6 z-50 px-4 py-3 rounded-xl shadow-xl text-[13px] font-medium flex items-center gap-2"
         :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
      <svg v-if="toast.type === 'success'" class="w-4 h-4 text-brand-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
      </svg>
      <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
      </svg>
      {{ toast.message }}
    </div>

    <!-- ===== MODAL RETRAIT ===== -->
    <div v-if="showWithdrawModal"
         class="fixed inset-0 bg-ink/50 z-50 flex items-center justify-center p-4"
         @click.self="closeWithdrawModal">

      <div class="bg-white rounded-2xl w-full max-w-md p-6 shadow-2xl">

        <div class="flex items-center justify-between mb-5">
          <div>
            <h2 class="text-[16px] font-bold text-ink">Demande de retrait</h2>
            <p class="text-[12px] text-[#73726C] mt-0.5">
              Solde disponible : <span class="font-bold text-ink">{{ formatAmount(wallet.balance) }} DH</span>
            </p>
          </div>
          <button @click="closeWithdrawModal" class="p-1.5 hover:bg-[#F4F4ED] rounded-md">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <!-- erreur -->
        <div v-if="withdrawError"
             class="bg-red-50 border border-red-200 text-red-700 rounded-lg p-3 mb-4 text-[12px]">
          {{ withdrawError }}
        </div>

        <div class="space-y-4">

          <!-- montant -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              Montant à retirer (DH)
            </label>
            <div class="relative">
              <input v-model="withdrawForm.amount"
                     type="number"
                     min="100"
                     :max="wallet.balance"
                     placeholder="Ex: 500"
                     class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition pr-12"/>
              <span class="absolute right-3 top-1/2 -translate-y-1/2 text-[12px] font-medium text-[#73726C]">
                DH
              </span>
            </div>
            <p class="text-[11px] text-[#9C9A92] mt-1">Minimum : 100 DH</p>

            <!-- boutons rapides -->
            <div class="flex gap-2 mt-2">
              <button v-for="amount in quickAmounts"
                      :key="amount"
                      @click="withdrawForm.amount = amount"
                      :disabled="wallet.balance < amount"
                      class="text-[11px] font-semibold px-2.5 py-1 rounded-md border transition disabled:opacity-30 disabled:cursor-not-allowed"
                      :class="withdrawForm.amount == amount
                        ? 'bg-ink text-white border-ink'
                        : 'border-[#EBEBE5] text-[#5F5E5A] hover:border-brand-500'">
                {{ amount }} DH
              </button>
              <button @click="withdrawForm.amount = wallet.balance"
                      :disabled="!wallet.balance || wallet.balance <= 0"
                      class="text-[11px] font-semibold px-2.5 py-1 rounded-md border border-[#EBEBE5] text-[#5F5E5A] hover:border-brand-500 transition disabled:opacity-30">
                Tout
              </button>
            </div>
          </div>

          <!-- IBAN -->
          <div>
            <label class="block text-[11px] font-semibold text-[#9C9A92] uppercase tracking-wider mb-1.5">
              IBAN / RIB
            </label>
            <input v-model="withdrawForm.iban"
                   type="text"
                   placeholder="MA64011519000001234567890"
                   class="w-full border border-[#EBEBE5] focus:border-brand-500 rounded-lg px-3 py-2.5 text-[13px] outline-none transition font-mono"/>
            <p class="text-[11px] text-[#9C9A92] mt-1">
              Virement sous 2-3 jours ouvrables
            </p>
          </div>

        </div>

        <div class="flex gap-3 mt-6">
          <button @click="closeWithdrawModal"
                  class="flex-1 py-2.5 border border-[#EBEBE5] hover:bg-[#F4F4ED] text-[13px] font-medium rounded-lg transition">
            Annuler
          </button>
          <button @click="requestWithdrawal" :disabled="withdrawing"
                  class="flex-1 py-2.5 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
            <svg v-if="withdrawing" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
            </svg>
            {{ withdrawing ? 'Traitement...' : 'Confirmer le retrait' }}
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

// ===== ÉTAT =====
const loading = ref(true)
const loadingMore = ref(false)
const withdrawing = ref(false)
const showWithdrawModal = ref(false)
const allLoaded = ref(false)
const selectedFilter = ref('ALL')
const withdrawError = ref('')
const toast = ref({ show: false, message: '', type: 'success' })

// ===== DONNÉES =====
// GET /api/wallet → WalletSummaryDTO
const wallet = ref({
  id: null,
  balance: 0,
  pendingBalance: 0,
  totalEarned: 0,
  createdAt: null,
})

// GET /api/wallet/transactions → List<TransactionDTO>
const transactions = ref([])

// ===== FORMULAIRE RETRAIT =====
// POST /api/wallet/withdraw → WithdrawalRequest { amount, iban }
const withdrawForm = ref({
  amount: null,
  iban: '',
})

const quickAmounts = [100, 500, 1000, 2000]

// ===== COMPUTED =====
const filteredTransactions = computed(() => {
  if (selectedFilter.value === 'ALL') return transactions.value
  return transactions.value.filter(t => t.type === selectedFilter.value)
})

// ===== AXIOS =====
const headers = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
})

// ===== CHARGER WALLET → GET /api/wallet =====
const loadWallet = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(
        `${API_URL}/api/wallet`,
        headers()
    )

    // WalletSummaryDTO.wallet → WalletDTO
    wallet.value = {
      id: data.wallet.id,
      balance: data.wallet.balance || 0,
      pendingBalance: data.wallet.pendingBalance || 0,
      totalEarned: data.wallet.totalEarned || 0,
      createdAt: data.wallet.createdAt,
    }

    // WalletSummaryDTO.recentTransactions → List<TransactionDTO>
    transactions.value = data.recentTransactions || []

  } catch (err) {
    console.error('loadWallet error:', err)
    showToast('Impossible de charger le wallet', 'error')
  } finally {
    loading.value = false
  }
}

// ===== CHARGER TOUTES LES TRANSACTIONS → GET /api/wallet/transactions =====
const loadAllTransactions = async () => {
  loadingMore.value = true
  try {
    const { data } = await axios.get(
        `${API_URL}/api/wallet/transactions`,
        headers()
    )
    transactions.value = data
    allLoaded.value = true
  } catch (err) {
    showToast('Erreur lors du chargement', 'error')
  } finally {
    loadingMore.value = false
  }
}

// ===== DEMANDE DE RETRAIT → POST /api/wallet/withdraw =====
// body : WithdrawalRequest { amount, iban }
const requestWithdrawal = async () => {
  withdrawError.value = ''

  // validations
  if (!withdrawForm.value.amount || withdrawForm.value.amount <= 0) {
    withdrawError.value = 'Entrez un montant valide'
    return
  }

  if (withdrawForm.value.amount < 100) {
    withdrawError.value = 'Montant minimum : 100 DH'
    return
  }

  if (withdrawForm.value.amount > wallet.value.balance) {
    withdrawError.value = 'Solde insuffisant'
    return
  }

  if (!withdrawForm.value.iban.trim()) {
    withdrawError.value = 'Entrez votre IBAN/RIB'
    return
  }

  withdrawing.value = true
  try {
    const { data } = await axios.post(
        `${API_URL}/api/wallet/withdraw`,
        {
          amount: parseFloat(withdrawForm.value.amount),
          iban: withdrawForm.value.iban.trim(),
        },
        headers()
    )

    // mettre à jour le solde localement
    wallet.value.balance -= parseFloat(withdrawForm.value.amount)

    // ajouter la transaction dans la liste
    transactions.value.unshift(data)

    showToast('Demande de retrait envoyée !')
    closeWithdrawModal()

  } catch (err) {
    withdrawError.value = err.response?.data?.message || 'Erreur lors du retrait'
  } finally {
    withdrawing.value = false
  }
}

// ===== HELPERS =====

const formatAmount = (amount) => {
  if (!amount && amount !== 0) return '0.00'
  return parseFloat(amount).toLocaleString('fr-MA', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

const formatDate = (date) => {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const isPositive = (type) => {
  return ['DEPOSIT', 'ESCROW_RELEASE', 'ESCROW_REFUND'].includes(type)
}

const getTransactionIcon = (type) => {
  const icons = {
    DEPOSIT: '💰',
    WITHDRAWAL: '🏦',
    ESCROW_HOLD: '🔒',
    ESCROW_RELEASE: '✅',
    ESCROW_REFUND: '↩️',
    COMMISSION: '📊',
  }
  return icons[type] || '💳'
}

const getTransactionIconBg = (type) => {
  const bgs = {
    DEPOSIT: 'bg-brand-50',
    WITHDRAWAL: 'bg-blue-50',
    ESCROW_HOLD: 'bg-amber-50',
    ESCROW_RELEASE: 'bg-green-50',
    ESCROW_REFUND: 'bg-purple-50',
    COMMISSION: 'bg-gray-100',
  }
  return bgs[type] || 'bg-gray-100'
}

const getTransactionLabel = (type) => {
  const labels = {
    DEPOSIT: 'Paiement reçu',
    WITHDRAWAL: 'Retrait bancaire',
    ESCROW_HOLD: 'Fonds bloqués (escrow)',
    ESCROW_RELEASE: 'Fonds libérés',
    ESCROW_REFUND: 'Remboursement',
    COMMISSION: 'Commission plateforme',
  }
  return labels[type] || type
}

const getStatusLabel = (status) => {
  const labels = {
    PENDING: 'En attente',
    COMPLETED: 'Complété',
    FAILED: 'Échoué',
    CANCELLED: 'Annulé',
  }
  return labels[status] || status
}

const getStatusClass = (status) => {
  const classes = {
    PENDING: 'bg-amber-50 text-amber-700',
    COMPLETED: 'bg-brand-50 text-brand-700',
    FAILED: 'bg-red-50 text-red-700',
    CANCELLED: 'bg-gray-100 text-gray-600',
  }
  return classes[status] || 'bg-gray-100 text-gray-600'
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

onMounted(() => {
  loadWallet()
})
</script>