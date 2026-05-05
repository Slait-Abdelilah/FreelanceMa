import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'
import { logoutUser } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)
  const refreshToken = ref(localStorage.getItem('refreshToken') || null)
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

  const isAuthenticated = computed(() => !!token.value && !!user.value?.role)
  const userRole = computed(() => user.value?.role || null)

  function setAuth(newToken, userData, newRefreshToken) {
    token.value = newToken
    user.value = userData
    localStorage.setItem('token', newToken)
    localStorage.setItem('user', JSON.stringify(userData))
    axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`
    if (newRefreshToken) {
      refreshToken.value = newRefreshToken
      localStorage.setItem('refreshToken', newRefreshToken)
    }
  }

  function clearAuth() {
    token.value = null
    refreshToken.value = null
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
    delete axios.defaults.headers.common['Authorization']
  }

  async function logout() {
    const rt = refreshToken.value
    clearAuth()
    if (rt) {
      try {
        await logoutUser(rt)
      } catch {
        // ignore — session locale déjà détruite
      }
    }
  }

  if (token.value) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`
  }

  return { token, refreshToken, user, isAuthenticated, userRole, setAuth, clearAuth, logout }
})
