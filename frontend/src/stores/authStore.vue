import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const refreshToken = ref(localStorage.getItem('refreshToken') || null)

  const isAuthenticated = computed(() => !!token.value && !!user.value)
  const userRole = computed(() => user.value?.role || null)

  function setAuth(newToken, newUser, newRefreshToken) {
    token.value = newToken
    user.value = newUser
    refreshToken.value = newRefreshToken
    localStorage.setItem('token', newToken)
    localStorage.setItem('user', JSON.stringify(newUser))
    localStorage.setItem('refreshToken', newRefreshToken)
  }

  function clearAuth() {
    token.value = null
    user.value = null
    refreshToken.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    localStorage.removeItem('refreshToken')
  }

  async function logout() {
    try {
      const rt = refreshToken.value
      if (rt) {
        await axios.post(`${BASE}/api/auth/logout`, { refreshToken: rt })
      }
    } catch {
      // silencieux — on déconnecte quand même
    } finally {
      clearAuth()
    }
  }

  return { token, user, refreshToken, isAuthenticated, userRole, setAuth, clearAuth, logout }
})
