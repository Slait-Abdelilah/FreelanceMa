import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8081'

export const getJobs = (params) =>
  axios.get(`${BASE}/api/jobs`, { params })

export const getJob = (id) =>
  axios.get(`${BASE}/api/jobs/${id}`)

export const createJob = (data) =>
  axios.post(`${BASE}/api/jobs`, data)

export const applyToJob = (jobId) =>
  axios.post(`${BASE}/api/jobs/${jobId}/apply`)

export const getMyApplications = () =>
  axios.get(`${BASE}/api/applications/me`)