<template>
  <div class="bg-white text-ink">

    <!-- ============ HERO ============ -->
    <section class="bg-cream py-24 relative overflow-hidden">
      <div class="absolute top-10 right-10 w-96 h-96 bg-brand-500/10 rounded-full blur-3xl"></div>

      <div class="max-w-5xl mx-auto px-6 text-center relative animate-fade-up">
        <div class="inline-flex items-center gap-2 bg-white border border-gray-200 px-4 py-2 rounded-full text-xs font-semibold mb-8">
          <span class="w-2 h-2 bg-brand-500 rounded-full"></span>
          Processus simple et transparent
        </div>

        <h1 class="text-6xl md:text-7xl font-bold leading-[0.95] tracking-tight mb-6">
          Comment <span class="italic text-brand-500">FreelanceMa</span><br>
          fonctionne
        </h1>

        <p class="text-xl text-ink-soft max-w-2xl mx-auto leading-relaxed">
          Découvrez les étapes simples pour trouver le talent parfait ou décrocher votre prochaine mission en toute sécurité.
        </p>
      </div>
    </section>

    <!-- ============ TOGGLE CLIENT / FREELANCER ============ -->
    <section class="py-16">
      <div class="max-w-5xl mx-auto px-6">
        <div class="flex justify-center mb-16 animate-fade-up">
          <div class="bg-gray-100 p-1.5 rounded-full inline-flex">
            <button @click="activeView = 'client'"
                    :class="activeView === 'client' ? 'bg-ink text-white shadow-lg' : 'text-ink hover:text-brand-500'"
                    class="px-8 py-3 rounded-full font-semibold text-sm transition">
              Pour les clients
            </button>
            <button @click="activeView = 'freelancer'"
                    :class="activeView === 'freelancer' ? 'bg-ink text-white shadow-lg' : 'text-ink hover:text-brand-500'"
                    class="px-8 py-3 rounded-full font-semibold text-sm transition">
              Pour les freelancers
            </button>
          </div>
        </div>

        <!-- ÉTAPES CLIENT -->
        <div v-if="activeView === 'client'" class="space-y-16">

          <div v-for="(step, i) in clientSteps" :key="'c'+i"
               class="grid md:grid-cols-2 gap-12 items-center animate-fade-up"
               :class="{ 'md:flex-row-reverse': i % 2 === 1 }">

            <div :class="i % 2 === 1 ? 'md:order-2' : ''">
              <div class="text-8xl font-bold text-brand-500/20 mb-2">0{{ i+1 }}</div>
              <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-3">Étape {{ i+1 }}</div>
              <h2 class="text-4xl md:text-5xl font-bold leading-tight mb-4">{{ step.title }}</h2>
              <p class="text-lg text-ink-soft leading-relaxed mb-6">{{ step.desc }}</p>
              <ul class="space-y-3">
                <li v-for="point in step.points" :key="point" class="flex gap-3 text-ink-soft">
                  <svg class="w-5 h-5 text-brand-500 flex-shrink-0 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                  </svg>
                  <span class="text-sm">{{ point }}</span>
                </li>
              </ul>
            </div>

            <div :class="i % 2 === 1 ? 'md:order-1' : ''">
              <div class="bg-cream rounded-3xl p-8 aspect-square flex items-center justify-center relative overflow-hidden">
                <div class="absolute top-6 left-6 right-6 bg-white rounded-2xl p-5 shadow-xl">
                  <div class="flex items-center justify-between mb-3">
                    <div class="text-xs font-bold text-brand-500 uppercase">{{ step.badge }}</div>
                    <span class="text-xs text-ink-soft">Étape {{ i+1 }}/4</span>
                  </div>
                  <div class="text-lg font-bold mb-1">{{ step.cardTitle }}</div>
                  <div class="text-sm text-ink-soft">{{ step.cardDesc }}</div>
                </div>
                <div class="text-9xl">{{ step.emoji }}</div>
              </div>
            </div>

          </div>

        </div>

        <!-- ÉTAPES FREELANCER -->
        <div v-else class="space-y-16">

          <div v-for="(step, i) in freelancerSteps" :key="'f'+i"
               class="grid md:grid-cols-2 gap-12 items-center animate-fade-up"
               :class="{ 'md:flex-row-reverse': i % 2 === 1 }">

            <div :class="i % 2 === 1 ? 'md:order-2' : ''">
              <div class="text-8xl font-bold text-brand-500/20 mb-2">0{{ i+1 }}</div>
              <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-3">Étape {{ i+1 }}</div>
              <h2 class="text-4xl md:text-5xl font-bold leading-tight mb-4">{{ step.title }}</h2>
              <p class="text-lg text-ink-soft leading-relaxed mb-6">{{ step.desc }}</p>
              <ul class="space-y-3">
                <li v-for="point in step.points" :key="point" class="flex gap-3 text-ink-soft">
                  <svg class="w-5 h-5 text-brand-500 flex-shrink-0 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                  </svg>
                  <span class="text-sm">{{ point }}</span>
                </li>
              </ul>
            </div>

            <div :class="i % 2 === 1 ? 'md:order-1' : ''">
              <div class="bg-cream rounded-3xl p-8 aspect-square flex items-center justify-center relative overflow-hidden">
                <div class="absolute top-6 left-6 right-6 bg-white rounded-2xl p-5 shadow-xl">
                  <div class="flex items-center justify-between mb-3">
                    <div class="text-xs font-bold text-brand-500 uppercase">{{ step.badge }}</div>
                    <span class="text-xs text-ink-soft">Étape {{ i+1 }}/4</span>
                  </div>
                  <div class="text-lg font-bold mb-1">{{ step.cardTitle }}</div>
                  <div class="text-sm text-ink-soft">{{ step.cardDesc }}</div>
                </div>
                <div class="text-9xl">{{ step.emoji }}</div>
              </div>
            </div>

          </div>

        </div>

      </div>
    </section>

    <!-- ============ SÉCURITÉ & CONFIANCE ============ -->
    <section class="py-28 bg-ink text-white">
      <div class="max-w-7xl mx-auto px-6">

        <div class="text-center mb-16 animate-fade-up">
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-4">Sécurité garantie</div>
          <h2 class="text-5xl md:text-6xl font-bold leading-tight max-w-3xl mx-auto">Votre argent et votre travail <span class="italic text-brand-500">protégés</span></h2>
        </div>

        <div class="grid md:grid-cols-3 gap-6">
          <div v-for="(sec, i) in security" :key="i"
               class="bg-white/5 border border-white/10 rounded-3xl p-8 hover:bg-white/10 transition animate-fade-up"
               :style="{ animationDelay: (i * 0.1) + 's' }">
            <div class="w-14 h-14 bg-brand-500 rounded-2xl flex items-center justify-center text-3xl mb-6">{{ sec.icon }}</div>
            <h3 class="text-xl font-bold mb-3">{{ sec.title }}</h3>
            <p class="text-gray-400 leading-relaxed text-sm">{{ sec.desc }}</p>
          </div>
        </div>

      </div>
    </section>

    <!-- ============ ESCROW EXPLIQUÉ ============ -->
    <section class="py-28">
      <div class="max-w-6xl mx-auto px-6">

        <div class="text-center mb-16 animate-fade-up">
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-4">Paiement escrow</div>
          <h2 class="text-5xl md:text-6xl font-bold leading-tight">Comment fonctionne le paiement sécurisé</h2>
        </div>

        <div class="relative">
          <!-- ligne verticale -->
          <div class="absolute left-1/2 top-0 bottom-0 w-0.5 bg-gray-200 hidden md:block"></div>

          <div class="space-y-16">
            <div v-for="(flow, i) in escrowFlow" :key="i"
                 class="grid md:grid-cols-2 gap-8 items-center relative animate-fade-up">

              <div v-if="i % 2 === 0" class="md:text-right md:pr-12">
                <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Étape {{ i+1 }}</div>
                <h3 class="text-2xl font-bold mb-3">{{ flow.title }}</h3>
                <p class="text-ink-soft leading-relaxed">{{ flow.desc }}</p>
              </div>
              <div v-else class="hidden md:block md:pr-12"></div>

              <!-- point central -->
              <div class="absolute left-1/2 -translate-x-1/2 w-12 h-12 bg-brand-500 text-white rounded-full flex items-center justify-center font-bold shadow-xl hidden md:flex z-10">
                {{ i+1 }}
              </div>

              <div v-if="i % 2 === 1" class="md:pl-12">
                <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-2">Étape {{ i+1 }}</div>
                <h3 class="text-2xl font-bold mb-3">{{ flow.title }}</h3>
                <p class="text-ink-soft leading-relaxed">{{ flow.desc }}</p>
              </div>
              <div v-else class="hidden md:block md:pl-12"></div>

            </div>
          </div>

        </div>
      </div>
    </section>

    <!-- ============ CTA ============ -->
    <section class="py-28 bg-cream">
      <div class="max-w-5xl mx-auto px-6 text-center animate-fade-up">
        <h2 class="text-5xl md:text-6xl font-bold leading-tight mb-6">Prêt à commencer ?</h2>
        <p class="text-ink-soft text-lg mb-10 max-w-xl mx-auto">Rejoignez FreelanceMa aujourd'hui et découvrez une nouvelle façon de travailler</p>
        <div class="flex gap-3 justify-center flex-wrap">
          <RouterLink to="/register/client" class="bg-ink text-white px-8 py-4 rounded-full font-semibold hover:bg-brand-500 transition">
            Publier un projet
          </RouterLink>
          <RouterLink to="/register/freelancer" class="bg-white border-2 border-ink px-8 py-4 rounded-full font-semibold hover:bg-ink hover:text-white transition">
            Devenir freelancer
          </RouterLink>
        </div>
      </div>
    </section>

    <PublicFooter />

  </div>
</template>

<script setup>
import { ref } from 'vue'
import PublicFooter from '@/components/layout/PublicFooter.vue'

const activeView = ref('client')

const clientSteps = [
  {
    title: 'Créez votre compte client',
    desc: 'Inscription gratuite en moins de 2 minutes. Complétez le profil de votre entreprise pour attirer les meilleurs talents.',
    points: [
      'Inscription 100% gratuite',
      'Profil entreprise personnalisable',
      'Vérification rapide pour plus de crédibilité',
    ],
    badge: 'Inscription',
    cardTitle: 'Nouveau compte créé',
    cardDesc: 'Profil 80% complet',
    emoji: '🏢',
  },
  {
    title: 'Publiez votre projet',
    desc: 'Décrivez votre projet en détail, définissez votre budget et votre deadline. Notre système intelligent trouvera les freelancers parfaits.',
    points: [
      'Formulaire simple et guidé',
      'Suggestions automatiques de compétences',
      'Budget flexible : fixe ou horaire',
      'Publication immédiate',
    ],
    badge: 'Nouveau projet',
    cardTitle: 'Refonte site web',
    cardDesc: 'Budget : 15,000 DH',
    emoji: '📝',
  },
  {
    title: 'Recevez des propositions',
    desc: 'Les freelancers qualifiés postulent avec leurs meilleures propositions. Comparez les profils, portfolios et avis clients avant de décider.',
    points: [
      'Propositions en moins de 24h',
      'Comparaison côte à côte',
      'Messagerie intégrée pour échanger',
      'Avis vérifiés des anciens clients',
    ],
    badge: 'Candidatures',
    cardTitle: '12 nouvelles propositions',
    cardDesc: 'Reçues en 4 heures',
    emoji: '📨',
  },
  {
    title: 'Payez en toute sécurité',
    desc: 'Le système escrow bloque votre paiement et ne le libère qu\'après validation du travail. Vous gardez le contrôle total.',
    points: [
      'Paiement bloqué jusqu\'à validation',
      'Libération des fonds uniquement après approbation',
      'Système de litiges en cas de problème',
      'Remboursement garanti',
    ],
    badge: 'Paiement',
    cardTitle: 'Fonds escrow sécurisés',
    cardDesc: '15,000 DH bloqués',
    emoji: '🔒',
  },
]

const freelancerSteps = [
  {
    title: 'Créez votre profil pro',
    desc: 'Mettez en valeur vos compétences, votre portfolio et vos expériences. Un profil complet attire 3x plus de clients.',
    points: [
      'Portfolio illimité',
      'Compétences certifiées',
      'Vidéo de présentation',
      'Badge vérifié après validation',
    ],
    badge: 'Nouveau profil',
    cardTitle: 'Profil créé',
    cardDesc: 'Badge vérifié obtenu',
    emoji: '👤',
  },
  {
    title: 'Explorez les missions',
    desc: 'Parcourez des centaines de missions adaptées à vos compétences. Utilisez les filtres pour trouver le projet idéal.',
    points: [
      'Filtres par budget et catégorie',
      'Alertes personnalisées',
      'Missions locales et internationales',
      'Invitations directes de clients',
    ],
    badge: 'Missions',
    cardTitle: '120 missions disponibles',
    cardDesc: 'Adaptées à votre profil',
    emoji: '🔍',
  },
  {
    title: 'Postulez avec confiance',
    desc: 'Envoyez des propositions personnalisées. Mettez en avant votre expertise et votre valeur unique pour vous démarquer.',
    points: [
      'Propositions personnalisées',
      'Pièces jointes portfolio',
      'Devis détaillé',
      'Discussion avec le client',
    ],
    badge: 'Proposition',
    cardTitle: 'Candidature envoyée',
    cardDesc: 'En attente de réponse',
    emoji: '✉️',
  },
  {
    title: 'Recevez vos paiements',
    desc: 'Dès que le travail est validé par le client, les fonds sont libérés dans votre wallet. Retirez quand vous voulez.',
    points: [
      'Paiement garanti après validation',
      'Retrait vers votre IBAN',
      'Factures générées automatiquement',
      'Historique complet des revenus',
    ],
    badge: 'Paiement',
    cardTitle: '+15,000 DH reçus',
    cardDesc: 'Disponible pour retrait',
    emoji: '💰',
  },
]

const security = [
  { icon: '🔒', title: 'Paiement escrow', desc: 'Les fonds sont bloqués sur un compte séquestre et libérés uniquement après validation mutuelle. Zéro risque pour les deux parties.' },
  { icon: '✅', title: 'Profils vérifiés', desc: 'Chaque freelancer passe par une vérification manuelle : identité, compétences, portfolio. Badges de confiance visibles.' },
  { icon: '⚖️', title: 'Résolution de litiges', desc: 'Notre équipe tranche équitablement en cas de désaccord. Décision basée sur les preuves et les échanges documentés.' },
]

const escrowFlow = [
  { title: 'Le client dépose les fonds', desc: 'À l\'acceptation du projet, le client dépose le montant total sur un compte escrow sécurisé. L\'argent est bloqué.' },
  { title: 'Le freelancer travaille sereinement', desc: 'Voyant les fonds bloqués, le freelancer commence à travailler en toute confiance, sans craindre de ne pas être payé.' },
  { title: 'Livraison du travail', desc: 'Le freelancer dépose son livrable sur la plateforme. Le client reçoit une notification pour vérifier.' },
  { title: 'Validation et libération', desc: 'Le client valide le livrable. Les fonds sont immédiatement libérés et transférés vers le wallet du freelancer.' },
]
</script>