import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8081'

export const loginUser = (email, password) =>
  axios.post(`${BASE}/api/auth/login`, { email, password })

export const registerFreelancer = (data) =>
  axios.post(`${BASE}/api/auth/register/freelancer`, data)

export const registerClient = (data) =>
  axios.post(`${BASE}/api/auth/register/client`, data)

export const forgotPassword = (email) =>
  axios.post(`${BASE}/api/auth/forgot-password`, { email })

export const resetPassword = (token, newPassword) =>
  axios.post(`${BASE}/api/auth/reset-password`, { token, newPassword })

export const verifyAccount = (token) =>
  axios.post(`${BASE}/api/auth/verify`, { token })

export const refreshTokenRequest = (refreshToken) =>
  axios.post(`${BASE}/api/auth/refresh`, { refreshToken })

export const logoutUser = (refreshToken) =>
  axios.post(`${BASE}/api/auth/logout`, { refreshToken })