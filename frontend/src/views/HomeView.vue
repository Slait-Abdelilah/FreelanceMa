<template>
  <div class="bg-white text-ink">

    <!-- ===================== HERO SPECTACULAIRE ===================== -->
    <section class="relative overflow-hidden bg-cream">
      <!-- formes décoratives -->
      <div class="absolute top-20 left-10 w-72 h-72 bg-brand-500/10 rounded-full blur-3xl"></div>
      <div class="absolute bottom-20 right-10 w-96 h-96 bg-brand-500/5 rounded-full blur-3xl"></div>

      <div class="max-w-7xl mx-auto px-6 py-20 md:py-28 relative">

        <!-- badge animé -->
        <div class="flex justify-center mb-8 animate-fade-up">
          <div class="inline-flex items-center gap-2 bg-white border border-gray-200 px-4 py-2 rounded-full text-xs font-semibold shadow-sm">
            <span class="flex w-2 h-2">
              <span class="animate-ping absolute inline-flex h-2 w-2 rounded-full bg-brand-500 opacity-75"></span>
              <span class="relative inline-flex rounded-full h-2 w-2 bg-brand-500"></span>
            </span>
            <span>+2,340 projets actifs en ce moment</span>
          </div>
        </div>

        <!-- titre géant -->
        <div class="text-center max-w-5xl mx-auto animate-fade-up" style="animation-delay:0.1s">
          <h1 class="text-6xl md:text-8xl font-bold leading-[0.95] tracking-tight mb-8">
            Travaillez avec les
            <span class="inline-block relative">
              <span class="italic text-brand-500">meilleurs</span>
              <svg class="absolute -bottom-4 left-0 w-full" viewBox="0 0 300 20" fill="none">
                <path d="M5 15 Q 150 0, 295 12" stroke="#14A800" stroke-width="3" stroke-linecap="round"/>
              </svg>
            </span>
            <br>
            talents du Maroc
          </h1>

          <p class="text-xl text-ink-soft max-w-2xl mx-auto mb-10 leading-relaxed">
            La première plateforme marocaine qui connecte entreprises et freelancers vérifiés. Publiez votre projet en 2 minutes, recevez des propositions en quelques heures.
          </p>

          <div class="flex gap-4 justify-center flex-wrap mb-12">
            <RouterLink to="/register/client" class="group bg-ink text-white px-8 py-4 rounded-full font-semibold hover:bg-brand-500 transition shadow-xl shadow-ink/20">
              <span class="flex items-center gap-2">
                Publier un projet
                <svg class="w-4 h-4 group-hover:translate-x-1 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
                </svg>
              </span>
            </RouterLink>
            <RouterLink to="/register/freelancer" class="bg-white border-2 border-ink px-8 py-4 rounded-full font-semibold hover:bg-ink hover:text-white transition">
              Je suis freelancer
            </RouterLink>
          </div>

          <!-- avatars + trust -->
          <div class="flex items-center justify-center gap-4 flex-wrap">
            <div class="flex -space-x-3">
              <div class="w-10 h-10 rounded-full bg-gradient-to-br from-brand-500 to-brand-700 border-2 border-white"></div>
              <div class="w-10 h-10 rounded-full bg-gradient-to-br from-amber-400 to-amber-600 border-2 border-white"></div>
              <div class="w-10 h-10 rounded-full bg-gradient-to-br from-rose-400 to-rose-600 border-2 border-white"></div>
              <div class="w-10 h-10 rounded-full bg-gradient-to-br from-purple-400 to-purple-600 border-2 border-white"></div>
              <div class="w-10 h-10 rounded-full bg-ink border-2 border-white flex items-center justify-center text-white text-xs font-bold">+6K</div>
            </div>
            <div class="text-sm">
              <div class="flex items-center gap-1 text-amber-500">
                <span>★★★★★</span>
                <span class="text-ink font-bold ml-1">4.9/5</span>
              </div>
              <div class="text-ink-soft text-xs">6,000+ clients nous font confiance</div>
            </div>
          </div>
        </div>

      </div>
    </section>

    <!-- ===================== LOGOS CLIENTS ===================== -->
    <section class="border-y border-gray-100 py-12 bg-white">
      <div class="max-w-7xl mx-auto px-6">
        <p class="text-center text-xs font-bold text-ink-soft uppercase tracking-widest mb-8">Ils nous font confiance</p>
        <div class="flex items-center justify-around flex-wrap gap-10 opacity-50">
          <div class="font-black text-2xl tracking-tight">INWI</div>
          <div class="font-serif italic text-2xl">Maroc Telecom</div>
          <div class="font-bold text-2xl tracking-wider">OCP</div>
          <div class="font-black text-2xl">BMCE</div>
          <div class="font-serif text-2xl">Attijariwafa</div>
          <div class="font-bold text-2xl tracking-tight">CIH</div>
        </div>
      </div>
    </section>

    <!-- ===================== STATISTIQUES IMPACTANTES ===================== -->
    <section class="py-24 bg-ink text-white overflow-hidden relative">
      <div class="absolute inset-0 bg-gradient-to-br from-ink via-ink to-brand-900/40"></div>
      <div class="max-w-7xl mx-auto px-6 relative">
        <div class="grid md:grid-cols-4 gap-8 text-center">
          <div class="animate-fade-up">
            <div class="text-6xl font-bold mb-2 text-brand-500">10K+</div>
            <div class="text-sm text-gray-400 uppercase tracking-wider">Freelancers vérifiés</div>
          </div>
          <div class="animate-fade-up" style="animation-delay:0.1s">
            <div class="text-6xl font-bold mb-2 text-brand-500">5K+</div>
            <div class="text-sm text-gray-400 uppercase tracking-wider">Projets livrés</div>
          </div>
          <div class="animate-fade-up" style="animation-delay:0.2s">
            <div class="text-6xl font-bold mb-2 text-brand-500">98%</div>
            <div class="text-sm text-gray-400 uppercase tracking-wider">Satisfaction</div>
          </div>
          <div class="animate-fade-up" style="animation-delay:0.3s">
            <div class="text-6xl font-bold mb-2 text-brand-500">24h</div>
            <div class="text-sm text-gray-400 uppercase tracking-wider">Temps réponse moyen</div>
          </div>
        </div>
      </div>
    </section>

    <!-- ===================== COMMENT ÇA MARCHE (détaillé) ===================== -->
    <section class="py-28">
      <div class="max-w-7xl mx-auto px-6">

        <div class="text-center mb-20 animate-fade-up">
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-4">Processus simple</div>
          <h2 class="text-5xl md:text-6xl font-bold leading-tight max-w-3xl mx-auto mb-6">Du projet à la livraison en 4 étapes</h2>
          <p class="text-ink-soft text-lg max-w-xl mx-auto">Un processus clair et transparent conçu pour votre succès</p>
        </div>

        <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6">

          <div v-for="(step, i) in steps" :key="i"
               class="relative animate-fade-up"
               :style="{ animationDelay: (i * 0.1) + 's' }">
            <div class="bg-cream rounded-3xl p-8 h-full hover:bg-ink hover:text-white transition group">
              <div class="flex items-center justify-between mb-6">
                <div class="text-5xl font-bold text-brand-500">0{{ i+1 }}</div>
                <svg class="w-6 h-6 text-ink-soft group-hover:text-white transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
                </svg>
              </div>
              <h3 class="text-xl font-bold mb-3">{{ step.title }}</h3>
              <p class="text-ink-soft text-sm leading-relaxed group-hover:text-gray-300">{{ step.desc }}</p>
            </div>
          </div>

        </div>
      </div>
    </section>

    <!-- ===================== CATÉGORIES RICHE ===================== -->
    <section class="py-28 bg-cream">
      <div class="max-w-7xl mx-auto px-6">

        <div class="flex items-end justify-between mb-16 flex-wrap gap-6 animate-fade-up">
          <div>
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-4">Explorez les expertises</div>
            <h2 class="text-5xl md:text-6xl font-bold leading-tight max-w-xl">Trouvez l'expert qu'il vous faut</h2>
          </div>
          <RouterLink to="/categories" class="group flex items-center gap-2 font-bold hover:text-brand-500 transition">
            Voir toutes les catégories
            <svg class="w-4 h-4 group-hover:translate-x-1 transition" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </RouterLink>
        </div>

        <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          <div v-for="(cat, i) in categories" :key="cat.name"
               class="bg-white p-6 rounded-2xl cursor-pointer group transition hover:shadow-xl animate-fade-up"
               :style="{ animationDelay: (i * 0.05) + 's' }">
            <div class="flex items-start justify-between mb-4">
              <div class="text-4xl">{{ cat.icon }}</div>
              <span class="text-xs font-bold text-brand-500 bg-brand-100 px-2 py-1 rounded-full">{{ cat.trend }}</span>
            </div>
            <div class="font-bold text-lg mb-1">{{ cat.name }}</div>
            <div class="text-sm text-ink-soft mb-3">{{ cat.count }} freelancers</div>
            <div class="text-xs text-ink-soft border-t border-gray-100 pt-3">À partir de <span class="font-bold text-ink">{{ cat.price }} DH/h</span></div>
          </div>
        </div>

      </div>
    </section>

    <!-- ===================== POURQUOI NOUS ===================== -->
    <section class="py-28">
      <div class="max-w-7xl mx-auto px-6">

        <div class="grid md:grid-cols-2 gap-16 items-center">

          <div class="animate-fade-up">
            <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-4">Pourquoi FreelanceMa</div>
            <h2 class="text-5xl md:text-6xl font-bold leading-tight mb-6">La plateforme pensée pour les <span class="italic text-brand-500">résultats</span></h2>
            <p class="text-ink-soft text-lg mb-10 leading-relaxed">Nous avons créé FreelanceMa pour résoudre les vrais problèmes du freelancing au Maroc : paiements sécurisés, talents vérifiés, et support local.</p>

            <div class="space-y-6">
              <div v-for="feat in features" :key="feat.title" class="flex gap-4">
                <div class="w-12 h-12 bg-brand-100 text-brand-700 rounded-2xl flex items-center justify-center flex-shrink-0">
                  <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                  </svg>
                </div>
                <div>
                  <h3 class="font-bold mb-1">{{ feat.title }}</h3>
                  <p class="text-sm text-ink-soft leading-relaxed">{{ feat.desc }}</p>
                </div>
              </div>
            </div>
          </div>

          <div class="relative animate-fade-up" style="animation-delay:0.2s">
            <div class="bg-cream rounded-[2.5rem] p-10 aspect-square relative overflow-hidden">
              <div class="absolute top-8 left-8 bg-white rounded-2xl p-5 shadow-xl w-60">
                <div class="flex items-center justify-between mb-3">
                  <div class="text-xs font-bold text-brand-500 uppercase">Escrow</div>
                  <div class="w-3 h-3 bg-brand-500 rounded-full"></div>
                </div>
                <div class="text-3xl font-bold mb-1">15,000 DH</div>
                <div class="text-xs text-ink-soft">Bloqués en sécurité</div>
              </div>
              <div class="absolute bottom-8 right-8 bg-ink text-white rounded-2xl p-5 shadow-xl w-60">
                <div class="text-xs text-gray-400 mb-2">Livrable validé</div>
                <div class="font-bold mb-3">Site e-commerce refondu</div>
                <div class="flex gap-2">
                  <div class="bg-brand-500 text-white text-xs font-bold px-2 py-1 rounded-full">Payé</div>
                  <div class="text-xs text-gray-400">il y a 2h</div>
                </div>
              </div>
              <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-32 h-32 bg-brand-500 rounded-full flex items-center justify-center shadow-2xl">
                <svg class="w-16 h-16 text-white" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm-2 16l-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z"/>
                </svg>
              </div>
            </div>
          </div>

        </div>
      </div>
    </section>

    <!-- ===================== TESTIMONIALS ===================== -->
    <section class="py-28 bg-ink text-white">
      <div class="max-w-7xl mx-auto px-6">

        <div class="text-center mb-16 animate-fade-up">
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-4">Témoignages</div>
          <h2 class="text-5xl md:text-6xl font-bold leading-tight max-w-3xl mx-auto">Ils ont transformé leur façon de travailler</h2>
        </div>

        <div class="grid md:grid-cols-3 gap-6">
          <div v-for="(t, i) in testimonials" :key="i"
               class="bg-white/5 border border-white/10 rounded-3xl p-8 hover:bg-white/10 transition animate-fade-up"
               :style="{ animationDelay: (i * 0.1) + 's' }">
            <div class="flex gap-1 text-brand-500 mb-4">★★★★★</div>
            <p class="text-gray-300 leading-relaxed mb-6">"{{ t.quote }}"</p>
            <div class="flex items-center gap-3">
              <div class="w-12 h-12 rounded-full bg-gradient-to-br from-brand-500 to-brand-700"></div>
              <div>
                <div class="font-bold">{{ t.name }}</div>
                <div class="text-xs text-gray-400">{{ t.role }}</div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </section>

    <!-- ===================== PRICING ===================== -->
    <section id="pricing" class="py-28">
      <div class="max-w-7xl mx-auto px-6">

        <div class="text-center mb-16 animate-fade-up">
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-4">Tarifs transparents</div>
          <h2 class="text-5xl md:text-6xl font-bold leading-tight max-w-2xl mx-auto">Sans frais cachés ni surprises</h2>
        </div>

        <div class="grid md:grid-cols-3 gap-6 max-w-5xl mx-auto">

          <div class="border-2 border-gray-200 rounded-3xl p-8 animate-fade-up">
            <div class="text-sm font-bold text-ink-soft uppercase mb-2">Gratuit</div>
            <div class="flex items-baseline gap-1 mb-6">
              <span class="text-5xl font-bold">0</span>
              <span class="text-ink-soft">DH/mois</span>
            </div>
            <p class="text-ink-soft text-sm mb-8">Parfait pour commencer et tester la plateforme</p>
            <ul class="space-y-3 text-sm mb-8">
              <li class="flex gap-2">✓ 3 candidatures/mois</li>
              <li class="flex gap-2">✓ Profil de base</li>
              <li class="flex gap-2">✓ Support email</li>
            </ul>
            <RouterLink to="/register/freelancer" class="block w-full text-center border-2 border-ink py-3 rounded-full font-semibold hover:bg-ink hover:text-white transition">S'inscrire</RouterLink>
          </div>

          <div class="bg-ink text-white rounded-3xl p-8 transform md:-translate-y-4 shadow-2xl animate-fade-up" style="animation-delay:0.1s">
            <div class="flex justify-between items-center mb-2">
              <div class="text-sm font-bold text-brand-500 uppercase">Pro</div>
              <span class="text-xs bg-brand-500 text-white px-3 py-1 rounded-full font-bold">POPULAIRE</span>
            </div>
            <div class="flex items-baseline gap-1 mb-6">
              <span class="text-5xl font-bold">199</span>
              <span class="text-gray-400">DH/mois</span>
            </div>
            <p class="text-gray-400 text-sm mb-8">Pour les freelancers sérieux qui veulent plus d'opportunités</p>
            <ul class="space-y-3 text-sm mb-8">
              <li class="flex gap-2">✓ Candidatures illimitées</li>
              <li class="flex gap-2">✓ Badge Pro vérifié</li>
              <li class="flex gap-2">✓ Profil mis en avant</li>
              <li class="flex gap-2">✓ Support prioritaire 24/7</li>
            </ul>
            <RouterLink to="/register/freelancer" class="block w-full text-center bg-brand-500 py-3 rounded-full font-semibold hover:bg-brand-600 transition">Commencer</RouterLink>
          </div>

          <div class="border-2 border-gray-200 rounded-3xl p-8 animate-fade-up" style="animation-delay:0.2s">
            <div class="text-sm font-bold text-ink-soft uppercase mb-2">Business</div>
            <div class="flex items-baseline gap-1 mb-6">
              <span class="text-5xl font-bold">499</span>
              <span class="text-ink-soft">DH/mois</span>
            </div>
            <p class="text-ink-soft text-sm mb-8">Pour les agences et équipes qui recrutent régulièrement</p>
            <ul class="space-y-3 text-sm mb-8">
              <li class="flex gap-2">✓ Tout du plan Pro</li>
              <li class="flex gap-2">✓ Jusqu'à 10 membres</li>
              <li class="flex gap-2">✓ API access</li>
              <li class="flex gap-2">✓ Account manager dédié</li>
            </ul>
            <RouterLink to="/register/client" class="block w-full text-center border-2 border-ink py-3 rounded-full font-semibold hover:bg-ink hover:text-white transition">Nous contacter</RouterLink>
          </div>

        </div>
      </div>
    </section>

    <!-- ===================== FAQ ===================== -->
    <section class="py-28 bg-cream">
      <div class="max-w-4xl mx-auto px-6">

        <div class="text-center mb-16 animate-fade-up">
          <div class="text-xs font-bold text-brand-500 uppercase tracking-widest mb-4">Questions fréquentes</div>
          <h2 class="text-5xl md:text-6xl font-bold leading-tight">Tout ce que vous devez savoir</h2>
        </div>

        <div class="space-y-3">
          <div v-for="(faq, i) in faqs" :key="i"
               class="bg-white rounded-2xl overflow-hidden animate-fade-up"
               :style="{ animationDelay: (i * 0.05) + 's' }">
            <button @click="faqOpen = faqOpen === i ? null : i"
                    class="w-full p-6 flex items-center justify-between text-left font-bold hover:bg-gray-50 transition">
              <span>{{ faq.q }}</span>
              <svg class="w-5 h-5 transition-transform flex-shrink-0 ml-4"
                   :class="{ 'rotate-180': faqOpen === i }"
                   fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M19 9l-7 7-7-7"/>
              </svg>
            </button>
            <div v-if="faqOpen === i" class="px-6 pb-6 text-ink-soft leading-relaxed">{{ faq.a }}</div>
          </div>
        </div>

      </div>
    </section>

    <!-- ===================== CTA FINAL ===================== -->
    <section class="py-28">
      <div class="max-w-6xl mx-auto px-6 animate-fade-up">
        <div class="bg-ink rounded-[2.5rem] p-16 text-center relative overflow-hidden">

          <div class="absolute top-0 right-0 w-96 h-96 bg-brand-500 rounded-full blur-[120px] opacity-40"></div>
          <div class="absolute bottom-0 left-0 w-96 h-96 bg-brand-500 rounded-full blur-[150px] opacity-20"></div>

          <div class="relative">
            <h2 class="text-5xl md:text-7xl font-bold text-white mb-6 leading-[0.95]">
              Prêt à <span class="italic text-brand-500">révolutionner</span><br>votre façon de travailler ?
            </h2>
            <p class="text-gray-300 text-xl mb-10 max-w-2xl mx-auto">Rejoignez plus de 10,000 freelancers et 6,000 clients sur la première plateforme freelance du Maroc</p>
            <div class="flex gap-3 justify-center flex-wrap">
              <RouterLink to="/register/client" class="bg-white text-ink px-10 py-4 rounded-full font-semibold hover:bg-gray-100 transition">Publier un projet</RouterLink>
              <RouterLink to="/register/freelancer" class="bg-brand-500 text-white px-10 py-4 rounded-full font-semibold hover:bg-brand-600 transition">Devenir freelancer</RouterLink>
            </div>
          </div>

        </div>
      </div>
    </section>

    <!-- ===================== FOOTER RICHE ===================== -->
    <footer class="bg-ink text-white pt-20 pb-8">
      <div class="max-w-7xl mx-auto px-6">

        <div class="grid md:grid-cols-5 gap-12 mb-16">

          <div class="md:col-span-2">
            <div class="flex items-center gap-2.5 mb-6">
              <svg class="w-9 h-9 text-brand-500" fill="currentColor" viewBox="0 0 24 24">
                <circle cx="12" cy="12" r="10"/>
                <circle cx="12" cy="12" r="4" fill="#001E00"/>
              </svg>
              <span class="text-xl font-bold">FreelanceMa</span>
            </div>
            <p class="text-gray-400 leading-relaxed max-w-sm mb-6">La première plateforme freelance du Maroc. Connectez-vous aux meilleurs talents et projets du pays.</p>
            <div class="flex gap-3">
              <a href="#" class="w-10 h-10 bg-white/10 rounded-full flex items-center justify-center hover:bg-brand-500 transition">in</a>
              <a href="#" class="w-10 h-10 bg-white/10 rounded-full flex items-center justify-center hover:bg-brand-500 transition">X</a>
              <a href="#" class="w-10 h-10 bg-white/10 rounded-full flex items-center justify-center hover:bg-brand-500 transition">f</a>
            </div>
          </div>

          <div>
            <div class="font-bold mb-4 text-sm uppercase tracking-wider">Plateforme</div>
            <div class="space-y-3 text-sm text-gray-400">
              <RouterLink to="/how-it-works" class="block hover:text-white">Comment ça marche</RouterLink>
              <RouterLink to="/categories" class="block hover:text-white">Catégories</RouterLink>
              <a href="#pricing" class="block hover:text-white">Tarifs</a>
              <RouterLink to="/faq" class="block hover:text-white">FAQ</RouterLink>
            </div>
          </div>

          <div>
            <div class="font-bold mb-4 text-sm uppercase tracking-wider">Pour vous</div>
            <div class="space-y-3 text-sm text-gray-400">
              <RouterLink to="/register/client" class="block hover:text-white">Je suis client</RouterLink>
              <RouterLink to="/register/freelancer" class="block hover:text-white">Je suis freelancer</RouterLink>
              <a href="#" class="block hover:text-white">Entreprises</a>
              <a href="#" class="block hover:text-white">Agences</a>
            </div>
          </div>

          <div>
            <div class="font-bold mb-4 text-sm uppercase tracking-wider">Entreprise</div>
            <div class="space-y-3 text-sm text-gray-400">
              <a href="#" class="block hover:text-white">À propos</a>
              <a href="#" class="block hover:text-white">Blog</a>
              <a href="#" class="block hover:text-white">Carrières</a>
              <a href="#" class="block hover:text-white">Contact</a>
            </div>
          </div>

        </div>

        <div class="border-t border-white/10 pt-8 flex items-center justify-between flex-wrap gap-4 text-sm text-gray-400">
          <div>© 2025 FreelanceMa. Tous droits réservés.</div>
          <div class="flex gap-6">
            <a href="#" class="hover:text-white">Conditions</a>
            <a href="#" class="hover:text-white">Confidentialité</a>
            <a href="#" class="hover:text-white">Cookies</a>
          </div>
        </div>

      </div>
    </footer>

  </div>
</template>

<script setup>
import { ref } from 'vue'

const faqOpen = ref(0)

const steps = [
  { title: 'Créez votre compte', desc: 'Inscription gratuite en 2 minutes. Complétez votre profil et ajoutez vos compétences ou vos besoins.' },
  { title: 'Publiez ou candidatez', desc: 'Clients publient leur projet avec budget et deadline. Freelancers envoient leurs meilleures propositions.' },
  { title: 'Collaborez en confiance', desc: 'Messagerie intégrée, partage de fichiers, suivi des jalons. Tout se passe sur la plateforme.' },
  { title: 'Paiement sécurisé', desc: 'Système escrow : les fonds sont bloqués et libérés uniquement après validation du livrable.' },
]

const categories = [
  { icon: '💻', name: 'Développement', count: '2,340', price: '150', trend: '🔥 Top' },
  { icon: '🎨', name: 'Design & UI/UX', count: '1,890', price: '120', trend: '↑ +15%' },
  { icon: '✍️', name: 'Rédaction', count: '1,420', price: '80', trend: 'Populaire' },
  { icon: '📱', name: 'Marketing', count: '980', price: '100', trend: '↑ +22%' },
  { icon: '🎬', name: 'Vidéo & Animation', count: '650', price: '200', trend: '🔥 Top' },
  { icon: '🎵', name: 'Audio & Musique', count: '430', price: '90', trend: 'Nouveau' },
  { icon: '📊', name: 'Business', count: '720', price: '180', trend: '↑ +10%' },
  { icon: '🌐', name: 'Traduction', count: '540', price: '70', trend: 'Populaire' },
]

const features = [
  { title: 'Paiement escrow sécurisé', desc: 'Les fonds sont bloqués et libérés uniquement après validation. Zéro risque pour les deux parties.' },
  { title: 'Freelancers vérifiés', desc: 'Chaque profil est vérifié manuellement. Badges de confiance pour les top freelancers.' },
  { title: 'Support local 24/7', desc: 'Équipe marocaine disponible en français, arabe et anglais pour vous accompagner.' },
  { title: 'Paiement local simplifié', desc: 'Virement bancaire, wafacash, cartes — tous les moyens de paiement marocains acceptés.' },
]

const testimonials = [
  { quote: 'FreelanceMa m\'a permis de trouver des clients réguliers au Maroc. Le paiement escrow m\'a donné confiance dès le début.', name: 'Yasmine Benali', role: 'Designer UI/UX • Casablanca' },
  { quote: 'En 48h, j\'avais 12 candidatures de qualité pour mon projet e-commerce. Le meilleur freelancer était juste à côté de chez moi !', name: 'Hamza Alaoui', role: 'CEO • Rabat' },
  { quote: 'La meilleure plateforme que j\'ai utilisée. Interface claire, support réactif, et des projets intéressants chaque semaine.', name: 'Sarah Tazi', role: 'Développeuse Full-Stack • Marrakech' },
]

const faqs = [
  { q: 'Comment fonctionne le paiement escrow ?', a: 'Le client dépose les fonds à l\'acceptation du projet. L\'argent est bloqué en toute sécurité et n\'est libéré au freelancer qu\'après validation du livrable final. En cas de litige, notre équipe intervient pour trancher équitablement.' },
  { q: 'Quels sont les frais de la plateforme ?', a: 'L\'inscription est 100% gratuite. Nous prélevons une commission de 10% sur chaque transaction réussie. Les plans Pro et Business offrent des avantages supplémentaires à partir de 199 DH/mois.' },
  { q: 'Comment sont vérifiés les freelancers ?', a: 'Chaque freelancer passe par une vérification manuelle de son identité, ses compétences et son portfolio. Les freelancers Pro disposent d\'un badge de vérification supplémentaire.' },
  { q: 'Puis-je annuler un projet en cours ?', a: 'Oui, les deux parties peuvent annuler un projet avant le début. Si le projet est déjà commencé, une procédure de résolution à l\'amiable est proposée avec l\'aide de notre équipe support.' },
  { q: 'Les paiements sont-ils disponibles au Maroc ?', a: 'Absolument. Nous acceptons les virements bancaires, cartes bancaires marocaines et internationales, ainsi que les paiements mobiles populaires au Maroc.' },
]
</script>