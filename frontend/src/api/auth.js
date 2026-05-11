import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// ── Inscription ────────────────────────────────────────────────────────────
export const registerFreelancer = (data) =>
  axios.post(`${BASE}/api/auth/register`, { ...data, role: 'FREELANCER' })

export const registerClient = (data) =>
  axios.post(`${BASE}/api/auth/register`, { ...data, role: 'CLIENT' })

// ── Connexion ──────────────────────────────────────────────────────────────
export const loginUser = (email, password) =>
  axios.post(`${BASE}/api/auth/login`, { email, password })

export const refreshTokenRequest = (refreshToken) =>
  axios.post(`${BASE}/api/auth/refresh`, { refreshToken })

export const logoutUser = (refreshToken) =>
  axios.post(`${BASE}/api/auth/logout`, { refreshToken })

// ── Vérification email ─────────────────────────────────────────────────────
export const verifyAccount = (email, code) =>
  axios.post(`${BASE}/api/auth/verify-account`, { email, code })

export const resendCode = (email) =>
  axios.post(`${BASE}/api/auth/resend-code`, { email })

// ── Mot de passe oublié ────────────────────────────────────────────────────
export const forgotPassword = (email) =>
  axios.post(`${BASE}/api/auth/forgot-password`, { email })

export const verifyResetCode = (email, code) =>
  axios.post(`${BASE}/api/auth/verify-reset-code`, { email, code })

export const resetPassword = (token, newPassword) =>
  axios.post(`${BASE}/api/auth/reset-password`, { token, newPassword })

// ── Compte (authentifié) ───────────────────────────────────────────────────
export const changePassword = (currentPassword, newPassword) =>
  axios.put(`${BASE}/api/auth/change-password`, { currentPassword, newPassword })

export const deleteAccount = () =>
  axios.delete(`${BASE}/api/auth/account`)
