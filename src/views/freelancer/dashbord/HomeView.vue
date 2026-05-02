<template>
  <div class="space-y-6">

    <!-- ============ EN-TÊTE BIENVENUE ============ -->
    <div class="bg-gradient-to-br from-ink to-gray-900 rounded-3xl p-8 text-white relative overflow-hidden">

      <div class="absolute top-0 right-0 w-96 h-96 bg-brand-500/20 rounded-full blur-3xl"></div>
      <div class="absolute -bottom-20 -left-20 w-72 h-72 bg-brand-500/10 rounded-full blur-3xl"></div>

      <div class="relative">
        <div class="flex items-start justify-between flex-wrap gap-6">

          <div class="flex-1 min-w-0">
            <div class="text-sm text-brand-400 font-semibold mb-2">{{ greeting }} 👋</div>
            <h1 class="text-3xl lg:text-4xl font-bold mb-3">
              Bonjour {{ userName }}
            </h1>
            <p class="text-gray-400 text-base lg:text-lg max-w-xl">
              Vous avez <span class="text-brand-400 font-semibold">{{ todayTasks }} tâches</span>
              à traiter aujourd'hui et <span class="text-brand-400 font-semibold">{{ newOffers }} nouvelles offres</span>
              correspondent à votre profil.
            </p>
          </div>

          <div class="bg-white/5 border border-white/10 rounded-2xl px-5 py-4 backdrop-blur">
            <div class="text-xs text-gray-400 uppercase tracking-wider mb-1">{{ dayName }}</div>
            <div class="text-2xl font-bold">{{ dayNumber }} {{ monthName }}</div>
            <div class="text-xs text-gray-400 mt-1">{{ year }}</div>
          </div>

        </div>
      </div>
    </div>

    <!-- ============ STATISTIQUES ============ -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">

      <div v-for="(stat, i) in stats" :key="i"
           class="bg-white rounded-2xl p-5 border border-gray-100 hover:border-brand-200 hover:shadow-md transition-all duration-300 cursor-pointer">

        <div class="flex items-start justify-between mb-3">
          <div class="w-11 h-11 rounded-xl flex items-center justify-center text-2xl"
               :class="stat.iconBg">
            {{ stat.icon }}
          </div>
          <div class="text-xs font-bold px-2 py-1 rounded-full flex items-center gap-1"
               :class="stat.trend > 0 ? 'bg-brand-50 text-brand-700' : 'bg-red-50 text-red-600'">
            <svg v-if="stat.trend > 0" class="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M5.293 9.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 7.414V15a1 1 0 11-2 0V7.414L6.707 9.707a1 1 0 01-1.414 0z" clip-rule="evenodd"/>
            </svg>
            <svg v-else class="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M14.707 10.293a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 111.414-1.414L9 12.586V5a1 1 0 112 0v7.586l2.293-2.293a1 1 0 011.414 0z" clip-rule="evenodd"/>
            </svg>
            {{ Math.abs(stat.trend) }}%
          </div>
        </div>

        <div class="text-2xl lg:text-3xl font-bold text-ink mb-1 flex items-baseline gap-1">
          {{ stat.value }}
          <span class="text-sm text-ink-soft font-medium">{{ stat.unit }}</span>
        </div>
        <div class="text-sm text-ink-soft">{{ stat.label }}</div>

      </div>

    </div>

    <!-- ============ GRAPHIQUE + À FAIRE ============ -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <!-- graphique revenus -->
      <div class="lg:col-span-2 bg-white rounded-2xl p-6 border border-gray-100">

        <div class="flex items-center justify-between mb-6">
          <div>
            <h2 class="text-lg font-bold text-ink">Évolution des revenus</h2>
            <p class="text-sm text-ink-soft">6 derniers mois</p>
          </div>
          <div class="flex items-center gap-2">
            <button v-for="period in ['6M', '1A', 'Tout']" :key="period"
                    @click="selectedPeriod = period"
                    class="text-xs font-semibold px-3 py-1.5 rounded-lg transition"
                    :class="selectedPeriod === period
                      ? 'bg-ink text-white'
                      : 'text-ink-soft hover:bg-gray-100'">
              {{ period }}
            </button>
          </div>
        </div>

        <!-- graphique en barres SVG -->
        <div class="relative h-64">
          <svg class="w-full h-full" viewBox="0 0 600 240" preserveAspectRatio="none">

            <line v-for="i in 5" :key="`line-${i}`"
                  :x1="0" :y1="i * 48" :x2="600" :y2="i * 48"
                  stroke="#e5e7eb" stroke-width="1" stroke-dasharray="4"/>

            <g v-for="(month, i) in revenueData" :key="i">
              <rect
                  :x="i * 100 + 30"
                  :y="240 - (month.value / maxRevenue * 200)"
                  :width="50"
                  :height="month.value / maxRevenue * 200"
                  fill="#14A800"
                  rx="6"
                  class="hover:fill-brand-600 transition cursor-pointer"
              />
              <text
                  :x="i * 100 + 55"
                  y="235"
                  text-anchor="middle"
                  class="text-[10px] fill-current text-ink-soft">
                {{ month.label }}
              </text>
            </g>
          </svg>
        </div>

        <div class="grid grid-cols-3 gap-4 mt-4 pt-4 border-t border-gray-100">
          <div>
            <div class="text-xs text-ink-soft">Ce mois</div>
            <div class="text-lg font-bold text-ink">12,450 DH</div>
          </div>
          <div>
            <div class="text-xs text-ink-soft">Mois dernier</div>
            <div class="text-lg font-bold text-ink">9,800 DH</div>
          </div>
          <div>
            <div class="text-xs text-ink-soft">Croissance</div>
            <div class="text-lg font-bold text-brand-600">+27%</div>
          </div>
        </div>

      </div>

      <!-- à faire aujourd'hui -->
      <div class="bg-white rounded-2xl p-6 border border-gray-100">

        <div class="flex items-center justify-between mb-5">
          <div>
            <h2 class="text-lg font-bold text-ink">À faire aujourd'hui</h2>
            <p class="text-sm text-ink-soft">{{ todoTasks.filter(t => !t.done).length }} tâches restantes</p>
          </div>
        </div>

        <div class="space-y-3">
          <div v-for="(task, i) in todoTasks" :key="i"
               class="flex items-start gap-3 p-3 rounded-xl hover:bg-gray-50 transition cursor-pointer group"
               :class="task.done ? 'opacity-50' : ''"
               @click="task.done = !task.done">

            <button class="w-5 h-5 rounded-md border-2 flex-shrink-0 mt-0.5 transition"
                    :class="task.done
                      ? 'bg-brand-500 border-brand-500'
                      : 'border-gray-300 group-hover:border-brand-500'">
              <svg v-if="task.done" class="w-3 h-3 text-white mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"/>
              </svg>
            </button>

            <div class="flex-1 min-w-0">
              <div class="text-sm font-semibold text-ink"
                   :class="task.done ? 'line-through' : ''">
                {{ task.title }}
              </div>
              <div class="flex items-center gap-2 mt-1">
                <span class="text-xs px-2 py-0.5 rounded-full font-medium"
                      :class="task.priorityClass">
                  {{ task.priority }}
                </span>
                <span class="text-xs text-ink-soft">{{ task.time }}</span>
              </div>
            </div>
          </div>
        </div>

        <button class="w-full mt-4 py-2.5 text-sm font-semibold text-brand-600 hover:bg-brand-50 rounded-xl transition">
          + Ajouter une tâche
        </button>

      </div>

    </div>

    <!-- ============ OFFRES RECOMMANDÉES ============ -->
    <div class="bg-white rounded-2xl p-6 border border-gray-100">

      <div class="flex items-center justify-between mb-5">
        <div>
          <div class="flex items-center gap-2">
            <h2 class="text-lg font-bold text-ink">Offres recommandées pour vous</h2>
            <span class="text-xs font-bold bg-brand-50 text-brand-700 px-2 py-0.5 rounded-full">
              {{ recommendedOffers.length }} matchs
            </span>
          </div>
          <p class="text-sm text-ink-soft mt-0.5">Basé sur vos compétences et votre historique</p>
        </div>
        <RouterLink to="/freelancer/explore"
                    class="text-sm font-semibold text-brand-600 hover:underline">
          Voir tout →
        </RouterLink>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">

        <div v-for="offer in recommendedOffers" :key="offer.id"
             class="border border-gray-200 rounded-2xl p-5 hover:border-brand-500 hover:shadow-md transition-all duration-300 cursor-pointer group">

          <div class="flex items-center justify-between mb-3">
            <span class="text-xs font-bold bg-brand-50 text-brand-700 px-2 py-0.5 rounded-full">
              {{ offer.match }}% match
            </span>
            <button class="text-gray-400 hover:text-red-500 transition">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
              </svg>
            </button>
          </div>

          <h3 class="text-base font-bold text-ink mb-2 line-clamp-2 group-hover:text-brand-600 transition">
            {{ offer.title }}
          </h3>

          <div class="flex items-center gap-2 mb-3">
            <div class="w-8 h-8 bg-gradient-to-br from-brand-500 to-brand-700 rounded-full flex items-center justify-center text-xs font-bold text-white">
              {{ offer.clientInitials }}
            </div>
            <div class="flex-1 min-w-0">
              <div class="text-sm font-semibold text-ink truncate">{{ offer.clientName }}</div>
              <div class="text-xs text-ink-soft flex items-center gap-1">
                <span class="text-amber-500">★</span>
                {{ offer.clientRating }}
              </div>
            </div>
          </div>

          <div class="flex flex-wrap gap-1.5 mb-4">
            <span v-for="skill in offer.skills" :key="skill"
                  class="text-xs bg-gray-100 text-ink px-2 py-1 rounded-md font-medium">
              {{ skill }}
            </span>
          </div>

          <div class="flex items-center justify-between pt-3 border-t border-gray-100">
            <div>
              <div class="text-xs text-ink-soft">Budget</div>
              <div class="text-base font-bold text-ink">{{ offer.budget }}</div>
            </div>
            <div class="text-right">
              <div class="text-xs text-ink-soft">Délai</div>
              <div class="text-sm font-semibold text-ink">{{ offer.deadline }}</div>
            </div>
          </div>

        </div>

      </div>

    </div>

    <!-- ============ MISSIONS ACTIVES ============ -->
    <div class="bg-white rounded-2xl p-6 border border-gray-100">

      <div class="flex items-center justify-between mb-5">
        <div>
          <h2 class="text-lg font-bold text-ink">Missions en cours</h2>
          <p class="text-sm text-ink-soft mt-0.5">{{ activeMissions.length }} missions actives</p>
        </div>
        <RouterLink to="/freelancer/active-missions"
                    class="text-sm font-semibold text-brand-600 hover:underline">
          Voir tout →
        </RouterLink>
      </div>

      <div class="space-y-3">

        <div v-for="mission in activeMissions" :key="mission.id"
             class="flex items-center gap-4 p-4 border border-gray-100 rounded-2xl hover:border-brand-300 transition cursor-pointer">

          <div class="w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0"
               :class="mission.iconBg">
            <span class="text-xl">{{ mission.icon }}</span>
          </div>

          <div class="flex-1 min-w-0">
            <div class="flex items-center gap-2 mb-1">
              <h3 class="text-sm font-bold text-ink truncate">{{ mission.title }}</h3>
              <span class="text-xs font-medium text-ink-soft">·</span>
              <span class="text-xs text-ink-soft">{{ mission.client }}</span>
            </div>

            <div class="flex items-center gap-3">
              <div class="flex-1 h-2 bg-gray-100 rounded-full overflow-hidden">
                <div class="h-full bg-brand-500 rounded-full transition-all duration-500"
                     :style="{ width: mission.progress + '%' }"></div>
              </div>
              <span class="text-xs font-bold text-ink whitespace-nowrap">{{ mission.progress }}%</span>
            </div>

            <div class="flex items-center gap-3 mt-2 text-xs text-ink-soft">
              <span>📅 {{ mission.deadline }}</span>
              <span>·</span>
              <span class="font-semibold text-ink">{{ mission.budget }}</span>
            </div>
          </div>

          <span class="text-xs font-bold px-3 py-1.5 rounded-full whitespace-nowrap"
                :class="mission.statusClass">
            {{ mission.status }}
          </span>

        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// ============ INFOS UTILISATEUR ============
const user = JSON.parse(localStorage.getItem('user') || '{}')
const userName = computed(() => {
  const email = user.email || 'freelancer@freelancema.ma'
  const name = email.split('@')[0].replace(/\./g, ' ')
  return name.charAt(0).toUpperCase() + name.slice(1)
})

// ============ DATE & SALUTATION ============
const today = new Date()
const greeting = computed(() => {
  const hour = today.getHours()
  if (hour < 12) return 'Bonne matinée'
  if (hour < 18) return 'Bon après-midi'
  return 'Bonne soirée'
})

const days = ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi']
const months = ['Jan', 'Fév', 'Mars', 'Avril', 'Mai', 'Juin', 'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc']

const dayName = days[today.getDay()]
const dayNumber = today.getDate()
const monthName = months[today.getMonth()]
const year = today.getFullYear()

const todayTasks = 4
const newOffers = 12

// ============ STATISTIQUES ============
const stats = ref([
  {
    value: '12,450',
    unit: 'DH',
    label: 'Gains ce mois',
    icon: '💰',
    iconBg: 'bg-brand-100',
    trend: 27
  },
  {
    value: '3',
    unit: '',
    label: 'Missions actives',
    icon: '💼',
    iconBg: 'bg-blue-100',
    trend: 50
  },
  {
    value: '18',
    unit: '',
    label: 'Candidatures',
    icon: '📨',
    iconBg: 'bg-purple-100',
    trend: 12
  },
  {
    value: '4.9',
    unit: '★',
    label: 'Note moyenne',
    icon: '⭐',
    iconBg: 'bg-amber-100',
    trend: 2
  },
])

// ============ GRAPHIQUE REVENUS ============
const selectedPeriod = ref('6M')
const revenueData = ref([
  { label: 'Nov', value: 6500 },
  { label: 'Déc', value: 8200 },
  { label: 'Jan', value: 7100 },
  { label: 'Fév', value: 9800 },
  { label: 'Mars', value: 11200 },
  { label: 'Avr', value: 12450 },
])

const maxRevenue = computed(() => Math.max(...revenueData.value.map(m => m.value)))

// ============ TÂCHES À FAIRE ============
const todoTasks = ref([
  {
    title: 'Livrer projet Refonte Site',
    priority: 'Urgent',
    priorityClass: 'bg-red-50 text-red-600',
    time: 'Aujourd\'hui',
    done: false
  },
  {
    title: 'Répondre au message d\'Ahmed',
    priority: 'Important',
    priorityClass: 'bg-amber-50 text-amber-700',
    time: 'Avant 18h',
    done: false
  },
  {
    title: 'Postuler à 3 nouvelles offres',
    priority: 'Normal',
    priorityClass: 'bg-blue-50 text-blue-600',
    time: 'Cette semaine',
    done: false
  },
  {
    title: 'Mettre à jour mon portfolio',
    priority: 'Low',
    priorityClass: 'bg-gray-100 text-gray-600',
    time: 'Quand possible',
    done: true
  },
])

// ============ OFFRES RECOMMANDÉES ============
const recommendedOffers = ref([
  {
    id: 1,
    match: 95,
    title: 'Développement application Vue.js + Spring Boot',
    clientName: 'Ahmed Bennani',
    clientInitials: 'AB',
    clientRating: 4.8,
    skills: ['Vue.js', 'Spring Boot', 'MariaDB'],
    budget: '5,000 DH',
    deadline: '30 jours'
  },
  {
    id: 2,
    match: 88,
    title: 'Refonte site e-commerce avec design moderne',
    clientName: 'Sarah Karimi',
    clientInitials: 'SK',
    clientRating: 4.9,
    skills: ['Vue.js', 'Tailwind', 'UI/UX'],
    budget: '8,500 DH',
    deadline: '45 jours'
  },
  {
    id: 3,
    match: 82,
    title: 'Création API REST pour application mobile',
    clientName: 'Yassine Alami',
    clientInitials: 'YA',
    clientRating: 4.7,
    skills: ['Spring Boot', 'JWT', 'REST API'],
    budget: '3,200 DH',
    deadline: '20 jours'
  },
])

// ============ MISSIONS ACTIVES ============
const activeMissions = ref([
  {
    id: 1,
    icon: '💻',
    iconBg: 'bg-blue-100',
    title: 'Plateforme freelancing FreelanceMa',
    client: 'TechStart Casa',
    progress: 75,
    deadline: '15 mai 2026',
    budget: '12,000 DH',
    status: 'En cours',
    statusClass: 'bg-brand-50 text-brand-700'
  },
  {
    id: 2,
    icon: '🎨',
    iconBg: 'bg-purple-100',
    title: 'Refonte design site corporate',
    client: 'Sarah Karimi',
    progress: 40,
    deadline: '30 mai 2026',
    budget: '8,500 DH',
    status: 'En cours',
    statusClass: 'bg-brand-50 text-brand-700'
  },
  {
    id: 3,
    icon: '📱',
    iconBg: 'bg-amber-100',
    title: 'Application mobile iOS de gestion',
    client: 'StartupX',
    progress: 90,
    deadline: '25 avril 2026',
    budget: '15,000 DH',
    status: 'Validation',
    statusClass: 'bg-amber-50 text-amber-700'
  },
])
</script>