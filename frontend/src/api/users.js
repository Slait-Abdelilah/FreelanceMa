import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8081'

export const getProfile = () =>
  axios.get(`${BASE}/api/profile`)

export const updateProfile = (data) =>
  axios.put(`${BASE}/api/profile`, data)

export const getSettings = () =>
  axios.get(`${BASE}/api/settings`)

export const updateAccount = (data) =>
  axios.put(`${BASE}/api/settings/account`, data)

export const updatePassword = (data) =>
  axios.put(`${BASE}/api/settings/password`, data)

export const updatePrivacy = (data) =>
  axios.put(`${BASE}/api/settings/privacy`, data)

export const deleteAccount = () =>
  axios.delete(`${BASE}/api/settings/account`)