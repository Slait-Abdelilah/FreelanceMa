import { createRouter, createWebHistory } from 'vue-router'

// ===== HELPERS =====
const getUser = () => {
  try {
    const raw = localStorage.getItem('user') || sessionStorage.getItem('user') || '{}'
    return JSON.parse(raw)
  } catch {
    return {}
  }
}

const getToken = () =>
  localStorage.getItem('token') || sessionStorage.getItem('token') || null

const isTokenExpired = (token) => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.exp * 1000 < Date.now()
  } catch {
    return true
  }
}

const routes = [
  // ===== PAGES PUBLIQUES (avec NavBar + PublicFooter) =====
  {
    path: '/',
    component: () => import('@/layouts/PublicLayout.vue'),
    children: [
      { path: '', component: () => import('@/views/HomeView.vue'), meta: { showPricing: true } },
      { path: 'how-it-works', component: () => import('@/views/HowItWorksView.vue') },
      { path: 'categories', component: () => import('@/views/CategoriesView.vue') },
      { path: 'faq', component: () => import('@/views/FAQView.vue') },
    ],
  },

  // ===== AUTH =====
  { path: '/forgot-password', component: () => import('@/views/ForgotPasswordView.vue') },
  { path: '/reset-password', component: () => import('@/views/ResetPasswordView.vue') },
  { path: '/verify-account', component: () => import('@/views/VerifyAccountView.vue') },

  // ===== CLIENT AUTH =====
  {
    path: '/register/client',
    component: () => import('@/views/ClientLog/RegisterClientView.vue'),
    meta: { guestOnly: true },
  },
  {
    path: '/login/client',
    component: () => import('@/views/ClientLog/LoginClientView.vue'),
    meta: { guestOnly: true },
  },

  // ===== FREELANCER AUTH =====
  {
    path: '/register/freelancer',
    component: () => import('@/views/freelancerLog/RegisterFreelancerView.vue'),
    meta: { guestOnly: true },
  },
  {
    path: '/login/freelancer',
    component: () => import('@/views/freelancerLog/LoginFreelancerView.vue'),
    meta: { guestOnly: true },
  },
  {
    path: '/onboarding/freelancer',
    component: () => import('@/views/freelancerLog/OnboardingFreelancerView.vue'),
    meta: { requiresAuth: true, requiredRole: 'FREELANCER' },
  },

  // ===== CLIENT DASHBOARD =====
  {
    path: '/client',
    component: () => import('@/views/clientsD/dashbord/ClientDashboardLayout.vue'),
    meta: { requiresAuth: true, requiredRole: 'CLIENT' },
    children: [
      { path: '', redirect: '/client/dashboard' },
      { path: 'dashboard', component: () => import('@/views/clientsD/dashbord/ClientHomeView.vue') },
      { path: 'offers', component: () => import('@/views/clientsD/dashbord/ClientOffersView.vue') },
      { path: 'applications', component: () => import('@/views/clientsD/dashbord/ClientApplicationsView.vue') },
      { path: 'missions', component: () => import('@/views/clientsD/dashbord/ClientMissionsView.vue') },
      { path: 'freelancers', component: () => import('@/views/clientsD/dashbord/ClientFreelancersView.vue') },
      { path: 'wallet', component: () => import('@/views/clientsD/dashbord/ClientWalletView.vue') },
      { path: 'messages', component: () => import('@/views/MessagesView.vue') },
      { path: 'settings', component: () => import('@/views/clientsD/dashbord/ClientSettingsView.vue') },
    ],
  },

  // ===== FREELANCER DASHBOARD =====
  {
    path: '/freelancer',
    component: () => import('@/views/freelancerD/dashbord/FreelancerDashboardLayout.vue'),
    meta: { requiresAuth: true, requiredRole: 'FREELANCER' },
    children: [
      { path: '', redirect: '/freelancer/dashboard' },
      { path: 'dashboard', component: () => import('@/views/freelancerD/dashbord/HomeView.vue') },
      { path: 'settings', component: () => import('@/views/freelancerD/dashbord/SettingsView.vue') },
      { path: 'profile', component: () => import('@/views/freelancerD/dashbord/ProfileView.vue') },
      { path: 'portfolio', component: () => import('@/views/freelancerD/dashbord/PortfolioView.vue') },
      { path: 'explore', component: () => import('@/views/freelancerD/dashbord/ExploreMissionsView.vue') },
      { path: 'applications', component: () => import('@/views/freelancerD/dashbord/ApplicationsView.vue') },
      { path: 'active-missions', component: () => import('@/views/freelancerD/dashbord/ActiveMissionsView.vue') },
      { path: 'favorites', component: () => import('@/views/freelancerD/dashbord/FavoritesView.vue') },
      { path: 'messages', component: () => import('@/views/MessagesView.vue') },
      { path: 'notifications', component: () => import('@/views/freelancerD/dashbord/NotificationsView.vue') },
      { path: 'wallet', component: () => import('@/views/freelancerD/dashbord/WalletView.vue') },
      { path: 'help', component: () => import('@/views/freelancerD/dashbord/ComingSoonView.vue') },
    ],
  },

  // ===== 404 =====
  { path: '/:pathMatch(.*)*', component: () => import('@/views/NotFoundView.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// ===== NAVIGATION GUARD =====
router.beforeEach((to) => {
  const token = getToken()
  const user = getUser()

  if (token && isTokenExpired(token)) {
    const refreshToken = localStorage.getItem('refreshToken') || sessionStorage.getItem('refreshToken')
    if (refreshToken) {
      return true
    }
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    sessionStorage.removeItem('token')
    sessionStorage.removeItem('user')
    if (to.meta.requiresAuth) {
      return user.role === 'CLIENT' ? '/login/client' : '/login/freelancer'
    }
    return true
  }

  const isAuthenticated = !!token && !!user.role
  const userRole = user.role

  if (to.meta.requiresAuth) {
    if (!isAuthenticated) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('refreshToken')
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('user')
      sessionStorage.removeItem('refreshToken')
      return userRole === 'CLIENT' ? '/login/client' : '/login/freelancer'
    }
    if (to.meta.requiredRole && userRole !== to.meta.requiredRole) {
      if (userRole === 'CLIENT') return '/client/dashboard'
      if (userRole === 'FREELANCER') return '/freelancer/dashboard'
      return '/'
    }
  }

  if (to.meta.guestOnly && isAuthenticated) {
    if (userRole === 'CLIENT') return '/client/dashboard'
    if (userRole === 'FREELANCER') return '/freelancer/dashboard'
  }

  return true
})

export default router
