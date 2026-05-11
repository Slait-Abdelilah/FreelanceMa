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
import RegisterClientView from '@/views/ClientLog/RegisterClientView.vue'
import LoginClientView from '@/views/ClientLog/LoginClientView.vue'

// ===== FREELANCER AUTH =====
import RegisterFreelancerView from '@/views/freelancerLog/RegisterFreelancerView.vue'
import LoginFreelancerView from '@/views/freelancerLog/LoginFreelancerView.vue'
import OnboardingFreelancerView from '@/views/freelancerLog/OnboardingFreelancerView.vue'
// ===== CLIENT DASHBOARD =====
import ClientDashboardLayout from '@/views/clientsD/dashbord/ClientDashboardLayout.vue'
import ClientHomeView from '@/views/clientsD/dashbord/ClientHomeView.vue'
import ClientOffersView from '@/views/clientsD/dashbord/ClientOffersView.vue'
import ClientApplicationsView from '@/views/clientsD/dashbord/ClientApplicationsView.vue'
import ClientMissionsView from '@/views/clientsD/dashbord/ClientMissionsView.vue'
import ClientSettingsView from '@/views/clientsD/dashbord/ClientSettingsView.vue'
// ===== FREELANCER DASHBOARD =====
import FreelancerDashboardLayout from '@/views/freelancerD/dashbord/FreelancerDashboardLayout.vue'
import FreelancerHomeView from '@/views/freelancerD/dashbord/HomeView.vue'
import SettingsView from '@/views/freelancerD/dashbord/SettingsView.vue'
import ProfileView from '@/views/freelancerD/dashbord/ProfileView.vue'
import PortfolioView from '@/views/freelancerD/dashbord/PortfolioView.vue'
import ComingSoonView from '@/views/freelancerD/dashbord/ComingSoonView.vue'
import WalletView from '@/views/freelancerD/dashbord/WalletView.vue'
import ExploreMissionsView from '@/views/freelancerD/dashbord/ExploreMissionsView.vue'
import ApplicationsView from '@/views/freelancerD/dashbord/ApplicationsView.vue'
import ActiveMissionsView from '@/views/freelancerD/dashbord/ActiveMissionsView.vue'
import FavoritesView from '@/views/freelancerD/dashbord/FavoritesView.vue'
import NotificationsView from '@/views/freelancerD/dashbord/NotificationsView.vue'



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
    {
        path: '/onboarding/freelancer',
        component: OnboardingFreelancerView,
        meta: { requiresAuth: true, requiredRole: 'FREELANCER' }
    },
    // ===== CLIENT DASHBOARD =====
    {
        path: '/client',
        component: ClientDashboardLayout,
        meta: { requiresAuth: true, requiredRole: 'CLIENT' },
        children: [
            { path: '', redirect: '/client/dashboard' },
            { path: 'dashboard', component: ClientHomeView },
            { path: 'offers', component: ClientOffersView },
            { path: 'applications', component: ClientApplicationsView },
            { path: 'missions', component: ClientMissionsView },
            { path: 'settings', component: ClientSettingsView },
        ]
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
            { path: 'active-missions', component: ActiveMissionsView },
            { path: 'favorites', component: FavoritesView },
            { path: 'messages', component: ComingSoonView },
            { path: 'notifications', component: NotificationsView },
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
router.beforeEach((to, from) => {
    const token = localStorage.getItem('token')
    const user = getUser()

    // Si le token est expiré → déconnexion silencieuse immédiate
    if (token && isTokenExpired(token)) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        localStorage.removeItem('refreshToken')
        if (to.meta.requiresAuth) {
            return user.role === 'CLIENT' ? '/login/client' : '/login/freelancer'
        }
        return true
    }

    const isAuthenticated = !!token && !!user.role
    const userRole = user.role

    // ===== CAS 1 : route nécessite d'être connecté =====
    if (to.meta.requiresAuth) {
        if (!isAuthenticated) {
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            localStorage.removeItem('refreshToken')
            return userRole === 'CLIENT' ? '/login/client' : '/login/freelancer'
        }
        // mauvais rôle → rediriger vers son propre dashboard
        if (to.meta.requiredRole && userRole !== to.meta.requiredRole) {
            if (userRole === 'CLIENT') return '/client/dashboard'
            if (userRole === 'FREELANCER') return '/freelancer/dashboard'
            return '/'
        }
    }

    // ===== CAS 2 : page réservée aux invités =====
    if (to.meta.guestOnly && isAuthenticated) {
        if (userRole === 'CLIENT') return '/client/dashboard'
        if (userRole === 'FREELANCER') return '/freelancer/dashboard'
    }

    return true
})

export default router