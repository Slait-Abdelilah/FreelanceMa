import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// ── Offres ─────────────────────────────────────────────────────────────────
export const getOffers = (params) =>
  axios.get(`${BASE}/api/offers`, { params })

export const getOffer = (id) =>
  axios.get(`${BASE}/api/offers/${id}`)

export const createOffer = (data) =>
  axios.post(`${BASE}/api/offers`, data)

// ── Candidatures ───────────────────────────────────────────────────────────
export const applyToOffer = (data) =>
  axios.post(`${BASE}/api/applications`, data)

export const getMyApplications = () =>
  axios.get(`${BASE}/api/applications/my`)

export const getApplication = (id) =>
  axios.get(`${BASE}/api/applications/${id}`)

export const withdrawApplication = (id) =>
  axios.delete(`${BASE}/api/applications/${id}`)
