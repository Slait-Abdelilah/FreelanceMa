<template>
  <div class="bg-white text-ink">

    <!-- ============ HERO ============ -->
    <section class="bg-cream py-24 relative overflow-hidden">
      <div class="absolute top-0 right-0 w-96 h-96 bg-brand-500/10 rounded-full blur-3xl"></div>
      <div class="max-w-5xl mx-auto px-6 text-center relative animate-fade-up">
        <div class="inline-flex items-center gap-2 bg-white border border-gray-200 px-4 py-2 rounded-full text-xs font-semibold mb-8">
          <span class="w-2 h-2 bg-brand-500 rounded-full animate-pulse"></span>
          {{ totalFreelancers }} freelancers disponibles
        </div>
        <h1 class="text-6xl md:text-7xl font-bold leading-[0.95] tracking-tight mb-6">
          Toutes les <span class="italic text-brand-500">expertises</span><br>au Maroc
        </h1>
        <p class="text-xl text-ink-soft max-w-2xl mx-auto leading-relaxed mb-10">
          Trouvez l'expert qu'il vous faut parmi nos freelancers vérifiés dans tous les domaines.
        </p>

        <!-- barre de recherche -->
        <div class="max-w-xl mx-auto relative">
          <input
              v-model="search"
              type="text"
              placeholder="Rechercher une catégorie..."
              class="w-full bg-white border-2 border-gray-200 focus:border-brand-500 rounded-full px-6 py-4 text-sm outline-none transition pl-12"
          />
          <svg class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-ink-soft" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0"/>
          </svg>
        </div>
      </div>
    </section>

    <!-- ============ FILTRES ============ -->
    <section class="border-b border-gray-100 py-6 sticky top-16 bg-white z-40">
      <div class="max-w-7xl mx-auto px-6 flex items-center gap-3 overflow-x-auto pb-1">
        <button
            @click="activeFilter = 'all'"
            :class="activeFilter === 'all' ? 'bg-ink text-white' : 'bg-gray-100 text-ink hover:bg-gray-200'"
            class="px-5 py-2 rounded-full text-sm font-semibold whitespace-nowrap transition">
          Toutes
        </button>
        <button v-for="filter in filters" :key="filter"
                @click="activeFilter = filter"
                :class="activeFilter === filter ? 'bg-ink text-white' : 'bg-gray-100 text-ink hover:bg-gray-200'"
                class="px-5 py-2 rounded-full text-sm font-semibold whitespace-nowrap transition">
          {{ filter }}
        </button>
      </div>
    </section>

    <!-- ============ CATÉGORIES HERO (TOP 3) ============ -->
    <section class="py-16 max-w-7xl mx-auto px-6" v-if="!search && activeFilter === 'all'">
      <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-8 animate-fade-up">Top catégories</div>
      <div class="grid md:grid-cols-3 gap-6 mb-20">
        <div v-for="(cat, i) in topCategories" :key="cat.name"
             class="relative overflow-hidden rounded-3xl cursor-pointer group animate-fade-up"
             :style="{ animationDelay: (i * 0.1) + 's', background: cat.bg }">
          <div class="p-8 h-48 flex flex-col justify-between relative z-10">
            <div class="flex items-start justify-between">
              <div class="text-5xl">{{ cat.icon }}</div>
              <span class="text-xs font-bold bg-black/20 text-white px-3 py-1 rounded-full">{{ cat.badge }}</span>
            </div>
            <div>
              <div class="text-2xl font-bold text-white mb-1">{{ cat.name }}</div>
              <div class="text-white/70 text-sm">{{ cat.count }} experts disponibles</div>
            </div>
          </div>
          <div class="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition"></div>
        </div>
      </div>
    </section>

    <!-- ============ TOUTES LES CATÉGORIES ============ -->
    <section class="pb-28 max-w-7xl mx-auto px-6">

      <div v-if="filteredCategories.length === 0" class="text-center py-20 text-ink-soft">
        <div class="text-6xl mb-4">🔍</div>
        <div class="text-xl font-bold">Aucune catégorie trouvée</div>
        <div class="text-sm mt-2">Essayez un autre terme de recherche</div>
      </div>

      <div v-else>
        <div v-for="group in groupedCategories" :key="group.filter" class="mb-16 animate-fade-up">

          <div class="flex items-center justify-between mb-6">
            <div>
              <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-1">{{ group.filter }}</div>
              <h2 class="text-2xl font-bold">{{ group.categories.length }} catégories</h2>
            </div>
          </div>

          <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            <div v-for="(cat, i) in group.categories" :key="cat.name"
                 class="bg-white border-2 border-gray-100 p-6 rounded-2xl cursor-pointer group hover:border-brand-500 hover:shadow-xl transition animate-fade-up"
                 :style="{ animationDelay: (i * 0.04) + 's' }">

              <div class="flex items-start justify-between mb-4">
                <div class="text-4xl">{{ cat.icon }}</div>
                <span class="text-xs font-bold px-2 py-1 rounded-full"
                      :class="cat.trend === '🔥 Top' ? 'bg-red-100 text-red-600' : cat.trend.includes('+') ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-600'">
                  {{ cat.trend }}
                </span>
              </div>

              <div class="font-bold text-lg mb-1 group-hover:text-brand-500 transition">{{ cat.name }}</div>
              <div class="text-sm text-ink-soft mb-4">{{ cat.count }} freelancers</div>

              <div class="flex flex-wrap gap-1.5 mb-4">
                <span v-for="skill in cat.skills.slice(0,3)" :key="skill"
                      class="text-xs bg-gray-100 text-ink-soft px-2 py-0.5 rounded-full">
                  {{ skill }}
                </span>
              </div>

              <div class="border-t border-gray-100 pt-3 flex items-center justify-between">
                <div class="text-xs text-ink-soft">À partir de</div>
                <div class="font-bold text-brand-500">{{ cat.price }} DH/h</div>
              </div>
            </div>
          </div>

        </div>
      </div>

    </section>

    <!-- ============ CTA ============ -->
    <section class="py-28 bg-ink text-white">
      <div class="max-w-5xl mx-auto px-6 text-center animate-fade-up">
        <h2 class="text-5xl md:text-6xl font-bold leading-tight mb-6">
          Vous ne trouvez pas<br>ce qu'il vous faut ?
        </h2>
        <p class="text-gray-400 text-lg mb-10 max-w-xl mx-auto">Publiez votre projet et recevez des propositions d'experts qualifiés en moins de 24h</p>
        <div class="flex gap-3 justify-center flex-wrap">
          <RouterLink to="/register/client" class="bg-brand-500 text-white px-8 py-4 rounded-full font-semibold hover:bg-brand-600 transition">Publier un projet</RouterLink>
          <RouterLink to="/register/freelancer" class="bg-white/10 border border-white/20 text-white px-8 py-4 rounded-full font-semibold hover:bg-white/20 transition">Je suis freelancer</RouterLink>
        </div>
      </div>
    </section>

    <PublicFooter />

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import PublicFooter from '@/components/layout/PublicFooter.vue'

const search = ref('')
const activeFilter = ref('all')

const filters = ['Tech & Dev', 'Design & Créa', 'Marketing', 'Business', 'Contenu', 'Autres']

const allCategories = [
  // Tech & Dev
  { name: 'Développement Web', icon: '💻', count: '2,340', price: '150', trend: '🔥 Top', filter: 'Tech & Dev', skills: ['React', 'Vue.js', 'Laravel'] },
  { name: 'Applications Mobile', icon: '📱', count: '980', price: '180', trend: '↑ +22%', filter: 'Tech & Dev', skills: ['Flutter', 'React Native', 'Swift'] },
  { name: 'Backend & API', icon: '⚙️', count: '760', price: '160', trend: '↑ +15%', filter: 'Tech & Dev', skills: ['Spring Boot', 'Node.js', 'Django'] },
  { name: 'DevOps & Cloud', icon: '☁️', count: '430', price: '200', trend: 'En hausse', filter: 'Tech & Dev', skills: ['Docker', 'AWS', 'CI/CD'] },
  { name: 'Intelligence Artificielle', icon: '🤖', count: '320', price: '250', trend: '🔥 Top', filter: 'Tech & Dev', skills: ['Python', 'TensorFlow', 'NLP'] },
  { name: 'Cybersécurité', icon: '🛡️', count: '210', price: '220', trend: '↑ +30%', filter: 'Tech & Dev', skills: ['Pentesting', 'OWASP', 'SSL'] },

  // Design & Créa
  { name: 'UI/UX Design', icon: '🎨', count: '1,890', price: '120', trend: '🔥 Top', filter: 'Design & Créa', skills: ['Figma', 'Adobe XD', 'Sketch'] },
  { name: 'Identité Visuelle', icon: '✏️', count: '1,200', price: '100', trend: '↑ +15%', filter: 'Design & Créa', skills: ['Illustrator', 'Photoshop', 'InDesign'] },
  { name: 'Motion Design', icon: '🎬', count: '650', price: '180', trend: '🔥 Top', filter: 'Design & Créa', skills: ['After Effects', 'Cinema 4D', 'Lottie'] },
  { name: 'Illustration', icon: '🖌️', count: '430', price: '90', trend: 'Populaire', filter: 'Design & Créa', skills: ['Procreate', 'Illustrator', 'Clip Studio'] },
  { name: 'Design Print', icon: '🖨️', count: '340', price: '80', trend: 'Stable', filter: 'Design & Créa', skills: ['InDesign', 'Canva Pro', 'Affinity'] },

  // Marketing
  { name: 'Marketing Digital', icon: '📊', count: '980', price: '100', trend: '↑ +22%', filter: 'Marketing', skills: ['SEO', 'Google Ads', 'Analytics'] },
  { name: 'Social Media', icon: '📲', count: '860', price: '80', trend: '🔥 Top', filter: 'Marketing', skills: ['Instagram', 'TikTok', 'LinkedIn'] },
  { name: 'Email Marketing', icon: '📧', count: '420', price: '90', trend: '↑ +10%', filter: 'Marketing', skills: ['Mailchimp', 'HubSpot', 'Klaviyo'] },
  { name: 'Growth Hacking', icon: '🚀', count: '280', price: '150', trend: '↑ +35%', filter: 'Marketing', skills: ['A/B Testing', 'Funnels', 'CRO'] },

  // Business
  { name: 'Comptabilité & Finance', icon: '💼', count: '720', price: '180', trend: '↑ +10%', filter: 'Business', skills: ['Excel', 'Sage', 'Odoo'] },
  { name: 'Consulting', icon: '🤝', count: '560', price: '200', trend: 'Populaire', filter: 'Business', skills: ['Stratégie', 'Business Plan', 'Pivot'] },
  { name: 'Juridique', icon: '⚖️', count: '230', price: '250', trend: 'Stable', filter: 'Business', skills: ['Droit des affaires', 'RGPD', 'Contrats'] },
  { name: 'RH & Recrutement', icon: '👥', count: '310', price: '120', trend: '↑ +18%', filter: 'Business', skills: ['Sourcing', 'LinkedIn', 'ATS'] },

  // Contenu
  { name: 'Rédaction Web', icon: '✍️', count: '1,420', price: '80', trend: 'Populaire', filter: 'Contenu', skills: ['SEO', 'Copywriting', 'Blog'] },
  { name: 'Traduction', icon: '🌐', count: '540', price: '70', trend: 'Stable', filter: 'Contenu', skills: ['FR/AR/EN', 'Assermenté', 'Technique'] },
  { name: 'Podcast & Audio', icon: '🎙️', count: '180', price: '90', trend: 'Nouveau', filter: 'Contenu', skills: ['Montage', 'Mixage', 'Voix off'] },
  { name: 'Photographie', icon: '📷', count: '340', price: '100', trend: '↑ +12%', filter: 'Contenu', skills: ['Studio', 'Retouche', 'Produit'] },

  // Autres
  { name: 'Formation & Cours', icon: '🎓', count: '290', price: '120', trend: '↑ +25%', filter: 'Autres', skills: ['E-learning', 'LMS', 'MOOC'] },
  { name: 'Support Client', icon: '🎧', count: '410', price: '60', trend: 'Stable', filter: 'Autres', skills: ['Zendesk', 'Chat', 'Email'] },
  { name: 'Saisie de données', icon: '⌨️', count: '580', price: '40', trend: 'Populaire', filter: 'Autres', skills: ['Excel', 'Google Sheets', 'CRM'] },
]

const topCategories = [
  { name: 'Développement Web', icon: '💻', count: '2,340', badge: '🔥 #1', bg: 'linear-gradient(135deg, #14A800, #108A00)' },
  { name: 'UI/UX Design', icon: '🎨', count: '1,890', badge: '⭐ #2', bg: 'linear-gradient(135deg, #001E00, #1a1a1a)' },
  { name: 'Rédaction Web', icon: '✍️', count: '1,420', badge: '💡 #3', bg: 'linear-gradient(135deg, #2d2d2d, #001E00)' },
]

const totalFreelancers = computed(() => {
  return allCategories.reduce((sum, cat) => {
    return sum + parseInt(cat.count.replace(',', ''))
  }, 0).toLocaleString()
})

const filteredCategories = computed(() => {
  return allCategories.filter(cat => {
    const matchSearch = cat.name.toLowerCase().includes(search.value.toLowerCase()) ||
        cat.skills.some(s => s.toLowerCase().includes(search.value.toLowerCase()))
    const matchFilter = activeFilter.value === 'all' || cat.filter === activeFilter.value
    return matchSearch && matchFilter
  })
})

const groupedCategories = computed(() => {
  if (search.value || activeFilter.value !== 'all') {
    return [{ filter: 'Résultats', categories: filteredCategories.value }]
  }
  return filters.map(filter => ({
    filter,
    categories: allCategories.filter(cat => cat.filter === filter)
  }))
})
</script>