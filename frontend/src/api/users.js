import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// ── Profil ─────────────────────────────────────────────────────────────────
export const getProfile = () =>
  axios.get(`${BASE}/api/profile`)

export const updateProfile = (data) =>
  axios.put(`${BASE}/api/profile`, data)

export const getPublicProfile = (userId) =>
  axios.get(`${BASE}/api/profile/${userId}/public`)

// ── Paramètres ─────────────────────────────────────────────────────────────
export const getSettings = () =>
  axios.get(`${BASE}/api/settings`)

export const updateAccount = (data) =>
  axios.put(`${BASE}/api/settings/account`, data)

export const updatePrivacy = (data) =>
  axios.put(`${BASE}/api/settings/privacy`, data)

// ── Portfolio ──────────────────────────────────────────────────────────────
export const getMyPortfolio = () =>
  axios.get(`${BASE}/api/portfolio`)

export const addPortfolioProject = (data) =>
  axios.post(`${BASE}/api/portfolio`, data)

export const updatePortfolioProject = (id, data) =>
  axios.put(`${BASE}/api/portfolio/${id}`, data)

export const deletePortfolioProject = (id) =>
  axios.delete(`${BASE}/api/portfolio/${id}`)

export const getPublicPortfolio = (freelancerId) =>
  axios.get(`${BASE}/api/portfolio/public/${freelancerId}`)

// ── Wallet ─────────────────────────────────────────────────────────────────
export const getWallet = () =>
  axios.get(`${BASE}/api/wallet`)

export const getTransactions = () =>
  axios.get(`${BASE}/api/wallet/transactions`)

export const requestWithdrawal = (data) =>
  axios.post(`${BASE}/api/wallet/withdraw`, data)
