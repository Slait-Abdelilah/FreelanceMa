import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import axios from 'axios'

// ===== INTERCEPTEUR AXIOS =====
// gère automatiquement les tokens expirés (401)
// et les accès interdits (403)
axios.interceptors.response.use(

    // si la requête réussit → laisser passer normalement
    response => response,

    // si la requête échoue → analyser l'erreur
    error => {
        const status = error.response?.status

        // 401 = token expiré ou invalide
        if (status === 401) {
            const savedUser = JSON.parse(localStorage.getItem('user') || '{}')
            const role = savedUser?.role
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            delete axios.defaults.headers.common['Authorization']
            if (role === 'CLIENT') {
                router.push('/login/client')
            } else {
                router.push('/login/freelancer')
            }
        }

        return Promise.reject(error)
    }
)

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.mount('#app')