import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import axios from 'axios'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// ===== INTERCEPTEUR AXIOS — REFRESH TOKEN =====
const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8081'
let isRefreshing = false
let failedQueue = []

const processQueue = (error, token = null) => {
  failedQueue.forEach(p => error ? p.reject(error) : p.resolve(token))
  failedQueue = []
}

axios.interceptors.response.use(
  response => response,
  async error => {
    const { useAuthStore } = await import('./stores/authStore')
    const authStore = useAuthStore()

    const originalRequest = error.config
    const status = error.response?.status

    // 401 sur un endpoint autre que /refresh lui-même
    if (status === 401 && !originalRequest._retry && !originalRequest.url?.includes('/api/auth/refresh')) {
      if (isRefreshing) {
        // mettre en file d'attente jusqu'à ce que le refresh soit terminé
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject })
        }).then(token => {
          originalRequest.headers['Authorization'] = `Bearer ${token}`
          return axios(originalRequest)
        }).catch(err => Promise.reject(err))
      }

      originalRequest._retry = true
      isRefreshing = true

      const storedRefreshToken = authStore.refreshToken

      if (!storedRefreshToken) {
        isRefreshing = false
        const role = authStore.userRole
        authStore.clearAuth()
        router.push(role === 'CLIENT' ? '/login/client' : '/login/freelancer')
        return Promise.reject(error)
      }

      try {
        const resp = await axios.post(`${BASE}/api/auth/refresh`, { refreshToken: storedRefreshToken })
        const { token: newToken, refreshToken: newRefreshToken, email, role } = resp.data

        authStore.setAuth(newToken, { email, role }, newRefreshToken)
        processQueue(null, newToken)

        originalRequest.headers['Authorization'] = `Bearer ${newToken}`
        return axios(originalRequest)

      } catch (refreshErr) {
        const role = authStore.userRole
        authStore.clearAuth()
        processQueue(refreshErr, null)
        router.push(role === 'CLIENT' ? '/login/client' : '/login/freelancer')
        return Promise.reject(refreshErr)
      } finally {
        isRefreshing = false
      }
    }

    // 403 = accès interdit (rôle insuffisant)
    if (status === 403) {
      const { useAuthStore: useStore } = await import('./stores/authStore')
      const store = useStore()
      const role = store.userRole
      router.push(role === 'CLIENT' ? '/client/dashboard' : '/freelancer/dashboard')
    }

    return Promise.reject(error)
  }
)

app.mount('#app')
