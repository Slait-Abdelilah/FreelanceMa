<template>
  <div class="bg-white text-ink">

    <!-- ============ HERO ============ -->
    <section class="bg-cream py-24 relative overflow-hidden">
      <div class="absolute top-0 right-0 w-96 h-96 bg-brand-500/10 rounded-full blur-3xl"></div>
      <div class="max-w-4xl mx-auto px-6 text-center relative animate-fade-up">
        <div class="inline-flex items-center gap-2 bg-white border border-gray-200 px-4 py-2 rounded-full text-xs font-semibold mb-8">
          <span class="w-2 h-2 bg-brand-500 rounded-full"></span>
          Centre d'aide FreelanceMa
        </div>
        <h1 class="text-6xl md:text-7xl font-bold leading-[0.95] tracking-tight mb-6">
          Questions<br><span class="italic text-brand-500">fréquentes</span>
        </h1>
        <p class="text-xl text-ink-soft max-w-xl mx-auto leading-relaxed mb-10">
          Tout ce que vous devez savoir sur FreelanceMa. Vous ne trouvez pas votre réponse ? Contactez notre support.
        </p>

        <!-- recherche -->
        <div class="max-w-lg mx-auto relative">
          <input
              v-model="search"
              type="text"
              placeholder="Rechercher une question..."
              class="w-full bg-white border-2 border-gray-200 focus:border-brand-500 rounded-full px-6 py-4 text-sm outline-none transition pl-12"
          />
          <svg class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-ink-soft" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0"/>
          </svg>
        </div>
      </div>
    </section>

    <!-- ============ FILTRES ============ -->
    <section class="border-b border-gray-100 py-5 sticky top-16 bg-white z-40">
      <div class="max-w-5xl mx-auto px-6 flex items-center gap-3 overflow-x-auto pb-1">
        <button
            @click="activeCategory = 'all'"
            :class="activeCategory === 'all' ? 'bg-ink text-white' : 'bg-gray-100 text-ink hover:bg-gray-200'"
            class="px-5 py-2 rounded-full text-sm font-semibold whitespace-nowrap transition">
          Toutes
        </button>
        <button v-for="cat in faqCategories" :key="cat.id"
                @click="activeCategory = cat.id"
                :class="activeCategory === cat.id ? 'bg-ink text-white' : 'bg-gray-100 text-ink hover:bg-gray-200'"
                class="px-5 py-2 rounded-full text-sm font-semibold whitespace-nowrap transition">
          {{ cat.icon }} {{ cat.label }}
        </button>
      </div>
    </section>

    <!-- ============ STATS ============ -->
    <section class="py-12 border-b border-gray-100">
      <div class="max-w-5xl mx-auto px-6 grid grid-cols-3 gap-6 text-center">
        <div>
          <div class="text-3xl font-bold text-ink mb-1">{{ filteredFaqs.length }}</div>
          <div class="text-sm text-ink-soft">Questions disponibles</div>
        </div>
        <div>
          <div class="text-3xl font-bold text-brand-500 mb-1">{{ faqCategories.length }}</div>
          <div class="text-sm text-ink-soft">Catégories</div>
        </div>
        <div>
          <div class="text-3xl font-bold text-ink mb-1">24/7</div>
          <div class="text-sm text-ink-soft">Support disponible</div>
        </div>
      </div>
    </section>

    <!-- ============ FAQ LISTE ============ -->
    <section class="py-20 max-w-4xl mx-auto px-6">

      <div v-if="filteredFaqs.length === 0" class="text-center py-20">
        <div class="text-6xl mb-4">🤔</div>
        <div class="text-2xl font-bold mb-2">Aucun résultat</div>
        <div class="text-ink-soft">Essayez un autre terme ou contactez notre support</div>
        <a href="mailto:support@freelancema.ma" class="inline-flex items-center gap-2 mt-6 bg-ink text-white px-6 py-3 rounded-full font-semibold hover:bg-brand-500 transition">
          Contacter le support
        </a>
      </div>

      <div v-else>
        <div v-for="group in groupedFaqs" :key="group.id" class="mb-16 animate-fade-up">

          <div class="flex items-center gap-3 mb-6">
            <div class="w-10 h-10 bg-brand-100 text-brand-700 rounded-xl flex items-center justify-center text-xl">
              {{ group.icon }}
            </div>
            <h2 class="text-xl font-bold">{{ group.label }}</h2>
            <span class="text-xs font-bold bg-gray-100 text-ink-soft px-2 py-1 rounded-full ml-auto">{{ group.faqs.length }} questions</span>
          </div>

          <div class="space-y-3">
            <div v-for="(faq, i) in group.faqs" :key="faq.q"
                 class="border-2 rounded-2xl overflow-hidden transition"
                 :class="openFaq === faq.q ? 'border-brand-500' : 'border-gray-100 hover:border-gray-200'">

              <button @click="openFaq = openFaq === faq.q ? null : faq.q"
                      class="w-full p-6 flex items-center justify-between text-left gap-4">
                <div class="flex items-center gap-3">
                  <span class="text-xs font-bold text-ink-soft bg-gray-100 w-7 h-7 rounded-full flex items-center justify-center flex-shrink-0">
                    {{ i+1 }}
                  </span>
                  <span class="font-semibold">{{ faq.q }}</span>
                </div>
                <svg class="w-5 h-5 flex-shrink-0 transition-transform"
                     :class="openFaq === faq.q ? 'rotate-180 text-brand-500' : 'text-ink-soft'"
                     fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M19 9l-7 7-7-7"/>
                </svg>
              </button>

              <div v-if="openFaq === faq.q" class="px-6 pb-6">
                <div class="border-t border-gray-100 pt-4">
                  <p class="text-ink-soft leading-relaxed">{{ faq.a }}</p>
                  <div v-if="faq.link" class="mt-4">
                    <RouterLink :to="faq.link.url" class="inline-flex items-center gap-2 text-brand-500 font-semibold text-sm hover:underline">
                      {{ faq.link.label }}
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
                      </svg>
                    </RouterLink>
                  </div>
                </div>
              </div>

            </div>
          </div>

        </div>
      </div>

    </section>

    <!-- ============ CONTACT SUPPORT ============ -->
    <section class="py-28 bg-ink text-white">
      <div class="max-w-5xl mx-auto px-6">
        <div class="text-center mb-16 animate-fade-up">
          <h2 class="text-5xl md:text-6xl font-bold leading-tight mb-4">Vous n'avez pas trouvé<br>votre <span class="italic text-brand-500">réponse</span> ?</h2>
          <p class="text-gray-400 text-lg">Notre équipe est disponible 7j/7 pour vous aider</p>
        </div>

        <div class="grid md:grid-cols-3 gap-6">

          <div v-for="(contact, i) in contactOptions" :key="i"
               class="bg-white/5 border border-white/10 rounded-3xl p-8 hover:bg-white/10 transition cursor-pointer animate-fade-up"
               :style="{ animationDelay: (i * 0.1) + 's' }">
            <div class="text-4xl mb-4">{{ contact.icon }}</div>
            <h3 class="text-xl font-bold mb-2">{{ contact.title }}</h3>
            <p class="text-gray-400 text-sm leading-relaxed mb-6">{{ contact.desc }}</p>
            <a :href="contact.href" class="inline-flex items-center gap-2 text-brand-500 font-semibold text-sm hover:underline">
              {{ contact.action }}
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
              </svg>
            </a>
          </div>

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
const activeCategory = ref('all')
const openFaq = ref(null)

const faqCategories = [
  { id: 'general', icon: '💡', label: 'Général' },
  { id: 'client', icon: '🏢', label: 'Clients' },
  { id: 'freelancer', icon: '👤', label: 'Freelancers' },
  { id: 'payment', icon: '💳', label: 'Paiements' },
  { id: 'security', icon: '🔒', label: 'Sécurité' },
  { id: 'account', icon: '⚙️', label: 'Compte' },
]

const allFaqs = [
  // Général
  {
    category: 'general',
    q: "C'est quoi FreelanceMa ?",
    a: "FreelanceMa est la première plateforme marocaine qui connecte les clients (entreprises et particuliers) avec des freelancers vérifiés. Notre mission est de simplifier et sécuriser la collaboration freelance au Maroc.",
    link: { label: 'Découvrir comment ça marche', url: '/how-it-works' }
  },
  {
    category: 'general',
    q: "FreelanceMa est-il gratuit ?",
    a: "L'inscription est 100% gratuite pour les clients et les freelancers. Nous prélevons une commission de 10% uniquement sur les transactions réussies. Les plans Pro et Business offrent des fonctionnalités avancées à partir de 199 DH/mois.",
  },
  {
    category: 'general',
    q: "Dans quelles villes FreelanceMa est-il disponible ?",
    a: "FreelanceMa est disponible dans tout le Maroc — Casablanca, Rabat, Marrakech, Fès, Tanger, Agadir et toutes les autres villes. Les missions peuvent être réalisées à distance ou en présentiel selon les préférences.",
  },
  {
    category: 'general',
    q: "En quelles langues puis-je utiliser la plateforme ?",
    a: "La plateforme est disponible en français, arabe et anglais. Notre équipe support peut vous assister dans ces trois langues.",
  },

  // Client
  {
    category: 'client',
    q: "Comment publier mon premier projet ?",
    a: "Après inscription, cliquez sur 'Publier un projet'. Remplissez le formulaire avec le titre, la description, le budget et la deadline. Votre projet sera en ligne immédiatement et les freelancers pourront postuler.",
    link: { label: 'Créer un compte client', url: '/register/client' }
  },
  {
    category: 'client',
    q: "Combien de temps pour recevoir des propositions ?",
    a: "En moyenne, les clients reçoivent leurs premières propositions en moins de 4 heures. Pour les projets urgents, vous pouvez inviter directement des freelancers spécifiques.",
  },
  {
    category: 'client',
    q: "Comment choisir le bon freelancer ?",
    a: "Consultez le profil complet, le portfolio, les avis clients vérifiés et le taux de réussite. Vous pouvez aussi échanger avec plusieurs freelancers via la messagerie avant de faire votre choix.",
  },
  {
    category: 'client',
    q: "Puis-je travailler avec le même freelancer sur plusieurs projets ?",
    a: "Absolument ! Vous pouvez retrouver vos freelancers précédents dans votre liste de favoris et les inviter directement sur vos nouveaux projets.",
  },

  // Freelancer
  {
    category: 'freelancer',
    q: "Comment créer un profil qui attire les clients ?",
    a: "Complétez votre profil à 100% : photo professionnelle, bio percutante, compétences vérifiées, portfolio de qualité et tarifs compétitifs. Les profils complets reçoivent 3x plus de projets.",
    link: { label: 'Créer mon profil freelancer', url: '/register/freelancer' }
  },
  {
    category: 'freelancer',
    q: "Combien puis-je facturer sur FreelanceMa ?",
    a: "Vous fixez vos propres tarifs. Les développeurs facturent en moyenne 150-250 DH/h, les designers 120-180 DH/h. Nous recommandons de consulter les tarifs moyens par catégorie avant de fixer vos prix.",
  },
  {
    category: 'freelancer',
    q: "Comment obtenir le badge 'Vérifié' ?",
    a: "Soumettez une copie de votre CIN ou passeport, complétez votre profil à 100% et réalisez au moins 3 projets avec une note supérieure à 4.5/5. La vérification est effectuée sous 48h.",
  },
  {
    category: 'freelancer',
    q: "Y a-t-il un minimum de projets à réaliser ?",
    a: "Non, il n'y a aucun minimum. Vous êtes libre de travailler autant ou aussi peu que vous le souhaitez. La plateforme est adaptée aux freelancers occasionnels comme aux professionnels à temps plein.",
  },

  // Paiements
  {
    category: 'payment',
    q: "Comment fonctionne le paiement escrow ?",
    a: "Quand un client accepte votre proposition, il dépose le montant total sur un compte escrow sécurisé. L'argent est bloqué et libéré uniquement après validation du livrable. Zéro risque de non-paiement.",
  },
  {
    category: 'payment',
    q: "Quels moyens de paiement sont acceptés ?",
    a: "Nous acceptons les virements bancaires marocains (CIH, BMCE, Attijariwafa...), les cartes bancaires Visa/Mastercard, et les paiements mobiles populaires au Maroc.",
  },
  {
    category: 'payment',
    q: "Quand reçois-je mon argent après validation ?",
    a: "Le paiement est disponible dans votre wallet FreelanceMa immédiatement après validation du client. Le virement vers votre compte bancaire prend 1-3 jours ouvrés.",
  },
  {
    category: 'payment',
    q: "Que se passe-t-il si le client refuse le livrable ?",
    a: "Si le client n'est pas satisfait, il peut demander des révisions. En cas de désaccord persistant, notre équipe intervient pour analyser les échanges et trancher équitablement. Le freelancer est protégé si le travail respecte les spécifications initiales.",
  },
  {
    category: 'payment',
    q: "Les freelancers paient-ils des taxes sur leurs revenus ?",
    a: "FreelanceMa génère automatiquement des récapitulatifs annuels de revenus pour faciliter vos déclarations fiscales. Nous vous recommandons de consulter un comptable pour votre situation spécifique.",
  },

  // Sécurité
  {
    category: 'security',
    q: "Mes données personnelles sont-elles sécurisées ?",
    a: "Oui. FreelanceMa utilise un chiffrement SSL 256 bits, des serveurs hébergés au Maroc conformes à la loi 09-08, et ne vend jamais vos données à des tiers.",
  },
  {
    category: 'security',
    q: "Comment signaler un utilisateur frauduleux ?",
    a: "Cliquez sur les 3 points (...) sur le profil ou la conversation, puis 'Signaler'. Notre équipe traite les signalements sous 24h et peut suspendre immédiatement les comptes frauduleux.",
  },
  {
    category: 'security',
    q: "Que faire en cas de litige avec un client ou freelancer ?",
    a: "Ouvrez un litige depuis votre tableau de bord. Soumettez vos preuves (messages, fichiers, accords). Notre équipe de médiation analyse la situation et rend une décision sous 72h.",
  },

  // Compte
  {
    category: 'account',
    q: "Comment modifier mon profil ?",
    a: "Allez dans Paramètres > Mon profil. Vous pouvez modifier toutes vos informations, ajouter des compétences, mettre à jour votre portfolio et changer votre photo à tout moment.",
  },
  {
    category: 'account',
    q: "Puis-je avoir un compte client et freelancer en même temps ?",
    a: "Oui ! Vous pouvez basculer entre les deux modes depuis votre tableau de bord. Beaucoup de nos utilisateurs sont à la fois clients sur certains projets et freelancers sur d'autres.",
  },
  {
    category: 'account',
    q: "Comment supprimer mon compte ?",
    a: "Allez dans Paramètres > Compte > Supprimer mon compte. Attention : cette action est irréversible. Vos projets en cours doivent être clôturés avant la suppression.",
  },
]

const filteredFaqs = computed(() => {
  return allFaqs.filter(faq => {
    const matchSearch = faq.q.toLowerCase().includes(search.value.toLowerCase()) ||
        faq.a.toLowerCase().includes(search.value.toLowerCase())
    const matchCat = activeCategory.value === 'all' || faq.category === activeCategory.value
    return matchSearch && matchCat
  })
})

const groupedFaqs = computed(() => {
  if (search.value || activeCategory.value !== 'all') {
    return [{ id: 'results', icon: '🔍', label: 'Résultats', faqs: filteredFaqs.value }]
  }
  return faqCategories.map(cat => ({
    ...cat,
    faqs: allFaqs.filter(f => f.category === cat.id)
  }))
})

const contactOptions = [
  {
    icon: '💬',
    title: 'Chat en direct',
    desc: 'Discutez avec notre équipe support en temps réel. Disponible du lundi au samedi de 9h à 22h.',
    action: 'Démarrer un chat',
    href: '#',
  },
  {
    icon: '📧',
    title: 'Email support',
    desc: 'Envoyez-nous un email détaillé. Nous répondons sous 4 heures les jours ouvrés.',
    action: 'Envoyer un email',
    href: 'mailto:support@freelancema.ma',
  },
  {
    icon: '📞',
    title: 'Appel téléphonique',
    desc: 'Parlez directement à un conseiller. Disponible du lundi au vendredi de 9h à 18h.',
    action: 'Appeler maintenant',
    href: 'tel:+212522000000',
  },
]
</script>