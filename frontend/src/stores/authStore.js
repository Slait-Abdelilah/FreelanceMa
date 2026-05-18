import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'
import { logoutUser } from '@/api/auth'

const getStorage = () =>
  sessionStorage.getItem('token') ? sessionStorage : localStorage

export const useAuthStore = defineStore('auth', () => {
  const storage = getStorage()

  const token = ref(storage.getItem('token') || null)
  const refreshToken = ref(storage.getItem('refreshToken') || null)
  const user = ref(JSON.parse(storage.getItem('user') || 'null'))

  const isAuthenticated = computed(() => !!token.value && !!user.value?.role)
  const userRole = computed(() => user.value?.role || null)

  function setAuth(newToken, userData, newRefreshToken, rememberMe = true) {
    token.value = newToken
    user.value = userData

    const store = rememberMe ? localStorage : sessionStorage

    store.setItem('token', newToken)
    store.setItem('user', JSON.stringify(userData))
    axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`

    if (newRefreshToken) {
      refreshToken.value = newRefreshToken
      store.setItem('refreshToken', newRefreshToken)
    }
  }

  function clearAuth() {
    token.value = null
    refreshToken.value = null
    user.value = null
    ;['token', 'refreshToken', 'user'].forEach(k => {
      localStorage.removeItem(k)
      sessionStorage.removeItem(k)
    })
    delete axios.defaults.headers.common['Authorization']
  }

  async function logout() {
    const rt = refreshToken.value
    clearAuth()
    if (rt) {
      try {
        await logoutUser(rt)
      } catch {
        // session locale déjà détruite
      }
    }
  }

  if (token.value) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`
  }

  return { token, refreshToken, user, isAuthenticated, userRole, setAuth, clearAuth, logout }
})
