import { createRouter, createWebHistory } from 'vue-router'

// ===== PAGES PUBLIQUES =====
import HomeView from '../views/HomeView.vue'
import HowItWorksView from '../views/HowItWorksView.vue'
import CategoriesView from '../views/CategoriesView.vue'
import FAQView from '../views/FAQView.vue'

// ===== AUTH =====
import ForgotPasswordView from '@/views/ForgotPasswordView.vue'
import ResetPasswordView from '@/views/ResetPasswordView.vue'
import VerifyAccountView from '@/views/VerifyAccountView.vue'

// ===== CLIENT =====
import RegisterClientView from '../views/ClientSite/RegisterClientView.vue'
import LoginClientView from '../views/ClientSite/LoginClientView.vue'

// ===== FREELANCER AUTH =====
import RegisterFreelancerView from '../views/freelancerSite/RegisterFreelancerView.vue'
import LoginFreelancerView from '../views/freelancerSite/LoginFreelancerView.vue'
// ===== CLIENT DASHBOARD =====
import ClientDashboard from '../views/clientts/dashbord/ClientDashboard.vue'
// ===== FREELANCER DASHBOARD =====
import FreelancerDashboardLayout from '../views/freelancer/dashbord/FreelancerDashboardLayout.vue'
import FreelancerHomeView from '../views/freelancer/dashbord/HomeView.vue'
import SettingsView from '../views/freelancer/dashbord/SettingsView.vue'
import ProfileView from '../views/freelancer/dashbord/ProfileView.vue'
import PortfolioView from '../views/freelancer/dashbord/PortfolioView.vue'
import ComingSoonView from '../views/freelancer/dashbord/ComingSoonView.vue'
import WalletView from '../views/freelancer/dashbord/WalletView.vue'
import ExploreMissionsView from '../views/freelancer/dashbord/ExploreMissionsView.vue'
import ApplicationsView from '../views/freelancer/dashbord/ApplicationsView.vue'



// ===== HELPERS =====
const getUser = () => {
    try {
        return JSON.parse(localStorage.getItem('user') || '{}')
    } catch {
        return {}
    }
}

// Vérifie si le JWT est expiré en décodant son payload (aucune lib nécessaire)
const isTokenExpired = (token) => {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]))
        return payload.exp * 1000 < Date.now()
    } catch {
        return true // token malformé → traité comme expiré
    }
}
const routes = [
    // ===== PAGES PUBLIQUES =====
    { path: '/', component: HomeView },
    { path: '/how-it-works', component: HowItWorksView },
    { path: '/categories', component: CategoriesView },
    { path: '/faq', component: FAQView },
    // ===== AUTH =====
    { path: '/forgot-password', component: ForgotPasswordView },
    { path: '/reset-password', component: ResetPasswordView },
    { path: '/verify-account', component: VerifyAccountView },

    // ===== CLIENT =====
    {
        path: '/register/client',
        component: RegisterClientView,
        meta: { guestOnly: true }
    },
    {
        path: '/login/client',
        component: LoginClientView,
        meta: { guestOnly: true }
    },

    // ===== FREELANCER AUTH =====
    {
        path: '/register/freelancer',
        component: RegisterFreelancerView,
        meta: { guestOnly: true }
    },
    {
        path: '/login/freelancer',
        component: LoginFreelancerView,
        meta: { guestOnly: true }
    },
    // ===== CLIENT DASHBOARD =====
    {
        path: '/client/dashboard',
        component: ClientDashboard,
        meta: { requiresAuth: true, requiredRole: 'CLIENT' }
    },
    // ===== FREELANCER DASHBOARD =====
    {
        path: '/freelancer',
        component: FreelancerDashboardLayout,
        meta: { requiresAuth: true, requiredRole: 'FREELANCER' },
        children: [
            { path: '', redirect: '/freelancer/dashboard' },
            { path: 'dashboard', component: FreelancerHomeView },
            { path: 'settings', component: SettingsView },
            { path: 'profile', component: ProfileView },
            { path: 'portfolio', component: PortfolioView },
            { path: 'explore', component: ExploreMissionsView },
            { path: 'applications', component: ApplicationsView },
            { path: 'active-missions', component: ComingSoonView },
            { path: 'favorites', component: ComingSoonView },
            { path: 'messages', component: ComingSoonView },
            { path: 'notifications', component: ComingSoonView },
            { path: 'wallet', component: WalletView },
            { path: 'help', component: ComingSoonView },

        ]
    },
    // ===== 404 =====
    { path: '/:pathMatch(.*)*', redirect: '/' },
]
const router = createRouter({
    history: createWebHistory(),
    routes
})
// ===== NAVIGATION GUARD =====
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    const user = getUser()

    // Si le token est expiré → déconnexion silencieuse immédiate
    if (token && isTokenExpired(token)) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        if (to.meta.requiresAuth) {
            return next(user.role === 'CLIENT' ? '/login/client' : '/login/freelancer')
        }
        return next()
    }

    const isAuthenticated = !!token && !!user.role
    const userRole = user.role

    // ===== CAS 1 : route nécessite d'être connecté =====
    if (to.meta.requiresAuth) {
        if (!isAuthenticated) {
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            return next(userRole === 'CLIENT' ? '/login/client' : '/login/freelancer')
        }
        // mauvais rôle → rediriger vers son propre dashboard
        if (to.meta.requiredRole && userRole !== to.meta.requiredRole) {
            if (userRole === 'CLIENT') return next('/client/dashboard')
            if (userRole === 'FREELANCER') return next('/freelancer/dashboard')
            return next('/')
        }
    }

    // ===== CAS 2 : page réservée aux invités =====
    if (to.meta.guestOnly && isAuthenticated) {
        if (userRole === 'CLIENT') return next('/client/dashboard')
        if (userRole === 'FREELANCER') return next('/freelancer/dashboard')
    }

    next()
})

export default router