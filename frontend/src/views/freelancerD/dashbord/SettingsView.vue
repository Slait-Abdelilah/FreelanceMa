<template>
  <div class="space-y-6 pb-8">

    <!-- HEADER -->
    <div>
      <h1 class="text-[20px] font-semibold text-ink tracking-tight">Paramètres</h1>
      <p class="text-[13px] text-[#9C9A92] mt-0.5">Gérez votre compte et vos préférences</p>
    </div>

    <!-- SKELETON -->
    <div v-if="loading" class="grid grid-cols-1 lg:grid-cols-3 gap-5">
      <div class="lg:col-span-1 bg-white border border-[#EBEBE5] rounded-2xl p-6 animate-pulse space-y-4">
        <div class="flex flex-col items-center gap-3">
          <div class="w-16 h-16 rounded-2xl bg-[#F4F4ED]"></div>
          <div class="h-4 w-32 bg-[#F4F4ED] rounded"></div>
          <div class="h-3 w-24 bg-[#F4F4ED] rounded"></div>
        </div>
        <div class="space-y-2 pt-4 border-t border-[#EBEBE5]">
          <div v-for="i in 4" :key="i" class="h-10 bg-[#F4F4ED] rounded-xl"></div>
        </div>
      </div>
      <div class="lg:col-span-2 space-y-4">
        <div v-for="i in 3" :key="i" class="bg-white border border-[#EBEBE5] rounded-2xl p-6 animate-pulse space-y-4">
          <div class="h-3 w-36 bg-[#F4F4ED] rounded"></div>
          <div class="grid grid-cols-2 gap-3">
            <div class="h-10 bg-[#F4F4ED] rounded-xl"></div>
            <div class="h-10 bg-[#F4F4ED] rounded-xl"></div>
          </div>
          <div class="h-10 bg-[#F4F4ED] rounded-xl"></div>
        </div>
      </div>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-5">

      <!-- ══ COLONNE GAUCHE : IDENTITÉ + NAV ══ -->
      <div class="lg:col-span-1 space-y-4">

        <!-- Carte identité -->
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 flex flex-col items-center text-center">
          <div class="w-16 h-16 rounded-2xl bg-ink flex items-center justify-center mb-3">
            <span class="text-[22px] font-bold text-white">{{ userInitials }}</span>
          </div>
          <p class="text-[16px] font-bold text-ink">{{ form.firstName }} {{ form.lastName }}</p>
          <p class="text-[13px] text-[#9C9A92] mt-0.5">{{ form.email }}</p>
          <span class="mt-3 text-[11px] font-semibold px-2.5 py-1 rounded-full border"
                :class="form.role === 'FREELANCER'
                  ? 'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]'
                  : 'bg-[#F4F4ED] text-[#5F5E5A] border-[#EBEBE5]'">
            {{ form.role === 'FREELANCER' ? 'Freelancer' : 'Client' }}
          </span>
        </div>

        <!-- Nav sections -->
        <div class="bg-white border border-[#EBEBE5] rounded-2xl p-2 space-y-0.5">
          <button v-for="nav in navItems" :key="nav.id" @click="activeSection = nav.id"
                  class="w-full flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-left transition"
                  :class="activeSection === nav.id
                    ? 'bg-ink text-white'
                    : 'text-[#5F5E5A] hover:bg-[#F4F4ED] hover:text-ink'">
            <svg class="w-4 h-4 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" :d="nav.icon"/>
            </svg>
            <span class="text-[13px] font-medium">{{ nav.label }}</span>
          </button>
        </div>

      </div>

      <!-- ══ COLONNE DROITE : CONTENU ══ -->
      <div class="lg:col-span-2 space-y-4">

        <!-- ── COMPTE ── -->
        <div v-show="activeSection === 'account'" class="space-y-4">
          <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 space-y-5">
            <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Informations personnelles</p>

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Prénom</label>
                <div class="px-4 py-3 bg-[#FAFAF7] border border-[#EBEBE5] rounded-xl text-[14px] text-[#5F5E5A]">
                  {{ form.firstName || '—' }}
                </div>
              </div>
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Nom</label>
                <div class="px-4 py-3 bg-[#FAFAF7] border border-[#EBEBE5] rounded-xl text-[14px] text-[#5F5E5A]">
                  {{ form.lastName || '—' }}
                </div>
              </div>
            </div>

            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Email</label>
              <div class="flex items-center gap-3">
                <div class="flex-1 px-4 py-3 bg-[#FAFAF7] border border-[#EBEBE5] rounded-xl text-[14px] text-[#5F5E5A]">
                  {{ form.email }}
                </div>
                <span class="text-[12px] text-[#9C9A92] bg-[#F4F4ED] border border-[#EBEBE5] px-3 py-2 rounded-xl whitespace-nowrap">
                  Non modifiable
                </span>
              </div>
            </div>

            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Téléphone</label>
              <input v-model="form.phone" type="tel" placeholder="+212 6 12 34 56 78"
                     class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC] max-w-sm"/>
            </div>

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Langue</label>
                <select v-model="form.language"
                        class="w-full text-[14px] bg-white border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 outline-none transition">
                  <option value="fr">Français</option>
                  <option value="en">English</option>
                  <option value="ar">العربية</option>
                </select>
              </div>
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Devise</label>
                <select v-model="form.currency"
                        class="w-full text-[14px] bg-white border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 outline-none transition">
                  <option value="MAD">DH (MAD)</option>
                  <option value="EUR">€ (EUR)</option>
                  <option value="USD">$ (USD)</option>
                </select>
              </div>
            </div>

            <div class="flex justify-end pt-1">
              <button @click="saveAccount" :disabled="savingAccount"
                      class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-5 py-2.5 rounded-xl transition disabled:opacity-50">
                <svg v-if="savingAccount" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
                </svg>
                {{ savingAccount ? 'Enregistrement…' : 'Enregistrer' }}
              </button>
            </div>
          </div>
        </div>

        <!-- ── PROFIL PRO ── -->
        <div v-show="activeSection === 'profile' && form.role === 'FREELANCER'" class="space-y-4">
          <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 space-y-5">
            <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Profil professionnel</p>

            <div class="flex items-end gap-4">
              <div class="flex-1">
                <label class="block text-[13px] font-medium text-ink mb-2">Titre</label>
                <input v-model="profileForm.title" type="text" placeholder="Ex : Développeur Full Stack React / Node"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
              </div>
              <div class="pb-0.5 flex items-center gap-2.5 flex-shrink-0">
                <button @click="profileForm.isAvailable = !profileForm.isAvailable"
                        class="relative w-11 h-6 rounded-full transition-colors"
                        :class="profileForm.isAvailable ? 'bg-green-500' : 'bg-[#D3D1C7]'">
                  <span class="absolute top-1 w-4 h-4 bg-white rounded-full shadow transition-all"
                        :class="profileForm.isAvailable ? 'left-6' : 'left-1'"></span>
                </button>
                <span class="text-[13px]" :class="profileForm.isAvailable ? 'text-green-600 font-medium' : 'text-[#9C9A92]'">
                  {{ profileForm.isAvailable ? 'Disponible' : 'Indisponible' }}
                </span>
              </div>
            </div>

            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Bio</label>
              <textarea v-model="profileForm.bio" rows="3" placeholder="Décrivez votre expérience et expertise..."
                        class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition resize-none placeholder:text-[#C4C3BC]">
              </textarea>
            </div>

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Localisation</label>
                <input v-model="profileForm.location" type="text" placeholder="Casablanca, Maroc"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
              </div>
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Tarif horaire (DH)</label>
                <input v-model.number="profileForm.hourlyRate" type="number" min="0" placeholder="150"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Compétences</label>
                <input v-model="profileForm.skills" type="text" placeholder="Vue.js, Node.js, MySQL…"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"/>
                <p class="text-[12px] text-[#C4C3BC] mt-1.5">Séparées par des virgules</p>
              </div>
              <div>
                <label class="block text-[13px] font-medium text-ink mb-2">Niveau</label>
                <div class="flex gap-2 h-[50px]">
                  <button v-for="lvl in experienceLevels" :key="lvl.value"
                          @click="profileForm.experienceLevel = lvl.value"
                          class="flex-1 rounded-xl text-[12px] font-semibold border transition"
                          :class="profileForm.experienceLevel === lvl.value
                            ? 'bg-ink text-white border-ink'
                            : 'border-[#EBEBE5] text-[#73726C] hover:border-[#D3D1C7]'">
                    {{ lvl.label }}
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 space-y-4">
            <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Liens</p>

            <div class="flex items-center gap-3 border border-[#EBEBE5] focus-within:border-[#5F5E5A] rounded-xl px-4 py-3 transition">
              <svg class="w-4 h-4 text-[#9C9A92] flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9"/>
              </svg>
              <span class="text-[12px] text-[#9C9A92] w-20 flex-shrink-0">Portfolio</span>
              <input v-model="profileForm.portfolioUrl" type="url" placeholder="https://monportfolio.com"
                     class="flex-1 text-[14px] text-ink outline-none bg-transparent placeholder:text-[#C4C3BC] min-w-0"/>
            </div>

            <div class="flex items-center gap-3 border border-[#EBEBE5] focus-within:border-[#5F5E5A] rounded-xl px-4 py-3 transition">
              <svg class="w-4 h-4 text-[#9C9A92] flex-shrink-0" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
              </svg>
              <span class="text-[12px] text-[#C4C3BC]">github.com/</span>
              <input v-model="profileForm.githubUrl" type="text" placeholder="username"
                     class="flex-1 text-[14px] text-ink outline-none bg-transparent placeholder:text-[#C4C3BC] min-w-0"/>
            </div>

            <div class="flex items-center gap-3 border border-[#EBEBE5] focus-within:border-[#5F5E5A] rounded-xl px-4 py-3 transition">
              <svg class="w-4 h-4 text-[#9C9A92] flex-shrink-0" fill="currentColor" viewBox="0 0 24 24">
                <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
              </svg>
              <span class="text-[12px] text-[#C4C3BC]">linkedin.com/in/</span>
              <input v-model="profileForm.linkedinUrl" type="text" placeholder="username"
                     class="flex-1 text-[14px] text-ink outline-none bg-transparent placeholder:text-[#C4C3BC] min-w-0"/>
            </div>

            <div class="flex justify-end pt-1">
              <button @click="saveProfile" :disabled="savingProfile"
                      class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-5 py-2.5 rounded-xl transition disabled:opacity-50">
                <svg v-if="savingProfile" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
                </svg>
                {{ savingProfile ? 'Enregistrement…' : 'Enregistrer le profil' }}
              </button>
            </div>
          </div>
        </div>

        <!-- ── SÉCURITÉ ── -->
        <div v-show="activeSection === 'security'">
          <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6">
            <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest mb-5">Sécurité</p>
            <div class="flex items-center justify-between py-4 px-5 bg-[#FAFAF7] border border-[#EBEBE5] rounded-xl">
              <div class="flex items-center gap-4">
                <div class="w-10 h-10 bg-white border border-[#EBEBE5] rounded-xl flex items-center justify-center">
                  <svg class="w-5 h-5 text-[#5F5E5A]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z"/>
                  </svg>
                </div>
                <div>
                  <p class="text-[14px] font-semibold text-ink">Mot de passe</p>
                  <p class="text-[12px] text-[#9C9A92] mt-0.5">Dernière modification inconnue</p>
                </div>
              </div>
              <button @click="showPasswordModal = true"
                      class="text-[13px] font-semibold text-ink bg-white border border-[#EBEBE5] hover:border-[#D3D1C7] px-4 py-2 rounded-lg transition">
                Modifier
              </button>
            </div>
          </div>
        </div>

        <!-- ── CONFIDENTIALITÉ ── -->
        <div v-show="activeSection === 'privacy'">
          <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6 space-y-5">
            <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest">Confidentialité</p>

            <div v-for="item in privacyItems" :key="item.key"
                 class="flex items-center justify-between py-4 border-b border-[#F4F4ED] last:border-0">
              <div>
                <p class="text-[14px] font-semibold text-ink">{{ item.label }}</p>
                <p class="text-[13px] text-[#9C9A92] mt-0.5">{{ item.desc }}</p>
              </div>
              <button @click="privacy[item.key] = !privacy[item.key]"
                      class="relative w-11 h-6 rounded-full transition-colors flex-shrink-0 ml-6"
                      :class="privacy[item.key] ? 'bg-ink' : 'bg-[#D3D1C7]'">
                <span class="absolute top-1 w-4 h-4 bg-white rounded-full shadow transition-all"
                      :class="privacy[item.key] ? 'left-6' : 'left-1'"></span>
              </button>
            </div>

            <div class="flex justify-end">
              <button @click="savePrivacy" :disabled="savingPrivacy"
                      class="flex items-center gap-2 bg-ink hover:bg-[#1A1A18] text-white text-[13px] font-semibold px-5 py-2.5 rounded-xl transition disabled:opacity-50">
                <svg v-if="savingPrivacy" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
                </svg>
                {{ savingPrivacy ? 'Enregistrement…' : 'Enregistrer' }}
              </button>
            </div>
          </div>
        </div>

        <!-- ── ZONE DANGEREUSE ── -->
        <div v-show="activeSection === 'danger'">
          <div class="bg-white border border-[#EBEBE5] rounded-2xl p-6">
            <p class="text-[11px] font-semibold text-[#9C9A92] uppercase tracking-widest mb-5">Zone dangereuse</p>
            <div class="flex items-start justify-between gap-6 py-4 px-5 bg-[#FAFAF7] border border-[#EBEBE5] rounded-xl">
              <div>
                <p class="text-[14px] font-semibold text-ink">Supprimer mon compte</p>
                <p class="text-[13px] text-[#73726C] mt-0.5">Toutes vos données seront supprimées définitivement.</p>
                <p v-if="confirmDelete" class="text-[12px] text-[#5F5E5A] font-semibold mt-2">
                  Cliquez encore pour confirmer. Cette action est irréversible.
                </p>
              </div>
              <button @click="handleDelete" :disabled="deletingAccount"
                      class="text-[13px] font-semibold px-4 py-2 rounded-xl transition flex-shrink-0 disabled:opacity-50"
                      :class="confirmDelete
                        ? 'bg-ink hover:bg-[#1A1A18] text-white'
                        : 'border border-[#EBEBE5] text-[#5F5E5A] hover:border-[#D3D1C7] hover:text-ink'">
                <svg v-if="deletingAccount" class="w-4 h-4 animate-spin inline mr-1.5" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
                </svg>
                {{ deletingAccount ? 'Suppression…' : confirmDelete ? 'Confirmer' : 'Supprimer' }}
              </button>
            </div>
          </div>
        </div>

      </div>
    </div>

    <!-- MODAL MOT DE PASSE -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-150" enter-from-class="opacity-0"
                leave-active-class="transition duration-100" leave-to-class="opacity-0">
      <div v-if="showPasswordModal" class="fixed inset-0 bg-ink/30 z-50 flex items-center justify-center p-4"
           @click.self="closeModal">
        <div class="bg-white rounded-2xl border border-[#EBEBE5] shadow-xl w-full max-w-md p-6" @click.stop>

          <div class="flex items-center justify-between mb-5">
            <p class="text-[15px] font-semibold text-ink">Changer le mot de passe</p>
            <button @click="closeModal" class="p-1.5 hover:bg-[#F4F4ED] rounded-lg transition">
              <svg class="w-4 h-4 text-[#73726C]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
          </div>

          <div v-if="modalError" class="bg-red-50 border border-red-100 text-red-600 rounded-xl p-3.5 mb-4 text-[13px]">
            {{ modalError }}
          </div>

          <div class="space-y-4">
            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Mot de passe actuel</label>
              <div class="relative">
                <input v-model="pwdForm.current" :type="showPwd.current ? 'text' : 'password'"
                       placeholder="Votre mot de passe actuel"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition pr-12 placeholder:text-[#C4C3BC]"/>
                <button @click="showPwd.current = !showPwd.current" type="button"
                        class="absolute right-4 top-1/2 -translate-y-1/2 text-[#9C9A92] hover:text-ink">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                  </svg>
                </button>
              </div>
            </div>
            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Nouveau mot de passe</label>
              <div class="relative">
                <input v-model="pwdForm.new" :type="showPwd.new ? 'text' : 'password'"
                       placeholder="Minimum 8 caractères"
                       class="w-full border border-[#EBEBE5] focus:border-[#5F5E5A] rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition pr-12 placeholder:text-[#C4C3BC]"/>
                <button @click="showPwd.new = !showPwd.new" type="button"
                        class="absolute right-4 top-1/2 -translate-y-1/2 text-[#9C9A92] hover:text-ink">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zM2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                  </svg>
                </button>
              </div>
            </div>
            <div>
              <label class="block text-[13px] font-medium text-ink mb-2">Confirmer</label>
              <input v-model="pwdForm.confirm" :type="showPwd.new ? 'text' : 'password'"
                     placeholder="Répétez le nouveau mot de passe"
                     @keyup.enter="changePassword"
                     class="w-full border rounded-xl px-4 py-3 text-[14px] text-ink outline-none transition placeholder:text-[#C4C3BC]"
                     :class="pwdForm.confirm && pwdForm.new !== pwdForm.confirm
                       ? 'border-red-300' : 'border-[#EBEBE5] focus:border-[#5F5E5A]'"/>
              <p v-if="pwdForm.confirm && pwdForm.new !== pwdForm.confirm" class="text-[12px] text-red-500 mt-1.5">
                Les mots de passe ne correspondent pas
              </p>
            </div>
          </div>

          <div class="flex gap-3 mt-6">
            <button @click="closeModal"
                    class="flex-1 py-2.5 text-[13px] font-medium text-[#5F5E5A] border border-[#EBEBE5] hover:border-[#D3D1C7] rounded-lg transition">
              Annuler
            </button>
            <button @click="changePassword" :disabled="savingPassword"
                    class="flex-1 py-2.5 text-[13px] font-semibold bg-ink hover:bg-[#1A1A18] text-white rounded-lg transition disabled:opacity-50 flex items-center justify-center gap-2">
              <svg v-if="savingPassword" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              {{ savingPassword ? 'Modification…' : 'Confirmer' }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
    </Teleport>

    <!-- TOAST -->
    <Teleport to="body">
    <Transition enter-active-class="transition duration-200" enter-from-class="opacity-0 translate-y-2"
                leave-active-class="transition duration-150" leave-to-class="opacity-0 translate-y-2">
      <div v-if="toast.show"
           class="fixed bottom-5 left-1/2 -translate-x-1/2 text-[13px] font-medium px-4 py-2.5 rounded-xl shadow-lg z-50"
           :class="toast.type === 'success' ? 'bg-ink text-white' : 'bg-red-600 text-white'">
        {{ toast.message }}
      </div>
    </Transition>
    </Teleport>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore.js'
import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const router    = useRouter()
const authStore = useAuthStore()

const loading         = ref(true)
const savingAccount   = ref(false)
const savingProfile   = ref(false)
const savingPrivacy   = ref(false)
const savingPassword  = ref(false)
const deletingAccount = ref(false)
const confirmDelete   = ref(false)
const showPasswordModal = ref(false)
const modalError      = ref('')
const activeSection   = ref('account')
const toast           = ref({ show: false, message: '', type: 'success' })

const form = ref({ email: '', firstName: '', lastName: '', phone: '', language: 'fr', currency: 'MAD', role: '' })

const profileForm = ref({
  title: '', bio: '', location: '', skills: '', hourlyRate: null,
  experienceLevel: 'JUNIOR', isAvailable: true,
  portfolioUrl: '', githubUrl: '', linkedinUrl: '',
})

const privacy = ref({ publicProfile: true, showEarnings: false, showOnlineStatus: true })
const pwdForm = ref({ current: '', new: '', confirm: '' })
const showPwd = ref({ current: false, new: false })

const experienceLevels = [
  { value: 'JUNIOR', label: 'Junior' },
  { value: 'MID',    label: 'Confirmé' },
  { value: 'SENIOR', label: 'Senior' },
]

const navItems = computed(() => {
  const items = [
    { id: 'account',  label: 'Compte',         icon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z' },
    { id: 'security', label: 'Sécurité',        icon: 'M16.5 10.5V6.75a4.5 4.5 0 10-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 002.25-2.25v-6.75a2.25 2.25 0 00-2.25-2.25H6.75a2.25 2.25 0 00-2.25 2.25v6.75a2.25 2.25 0 002.25 2.25z' },
    { id: 'privacy',  label: 'Confidentialité', icon: 'M2.036 12.322a1.012 1.012 0 010-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178z M15 12a3 3 0 11-6 0 3 3 0 016 0z' },
    { id: 'danger',   label: 'Zone dangereuse', icon: 'M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z' },
  ]
  if (form.value.role === 'FREELANCER') {
    items.splice(1, 0, { id: 'profile', label: 'Profil pro', icon: 'M20.25 14.15v4.25c0 1.094-.787 2.036-1.872 2.18-2.087.277-4.216.42-6.378.42s-4.291-.143-6.378-.42c-1.085-.144-1.872-1.086-1.872-2.18v-4.25m16.5 0a2.18 2.18 0 00.75-1.661V8.706c0-1.081-.768-2.015-1.837-2.175a48.114 48.114 0 00-3.413-.387m4.5 8.006c-.194.165-.42.295-.673.38A23.978 23.978 0 0112 15.75c-2.648 0-5.195-.429-7.577-1.22a2.016 2.016 0 01-.673-.38m0 0A2.18 2.18 0 013 12.489V8.706c0-1.081.768-2.015 1.837-2.175a48.111 48.111 0 013.413-.387m7.5 0V5.25A2.25 2.25 0 0013.5 3h-3a2.25 2.25 0 00-2.25 2.25v.894m7.5 0a48.667 48.667 0 00-7.5 0M12 12.75h.008v.008H12v-.008z' },
  )
  }
  return items
})

const privacyItems = computed(() => {
  const items = [
    { key: 'publicProfile',    label: 'Profil public',     desc: 'Visible par tous les clients' },
    { key: 'showOnlineStatus', label: 'Statut en ligne',   desc: 'Les clients voient si vous êtes connecté' },
  ]
  if (form.value.role === 'FREELANCER') {
    items.push({ key: 'showEarnings', label: 'Afficher mes gains', desc: 'Visible sur votre profil public' })
  }
  return items
})

const userInitials = computed(() => {
  const f = form.value.firstName?.[0] || ''
  const l = form.value.lastName?.[0] || ''
  return (f + l).toUpperCase() || '?'
})

const headers = () => ({ headers: { Authorization: `Bearer ${localStorage.getItem('token') || sessionStorage.getItem('token')}` } })

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => toast.value.show = false, 4000)
}

const loadSettings = async () => {
  loading.value = true
  try {
    const { data } = await axios.get(`${API_URL}/api/settings`, headers())
    form.value = { email: data.email || '', firstName: data.firstName || '', lastName: data.lastName || '', phone: data.phone || '', language: data.language || 'fr', currency: data.currency || 'MAD', role: data.role || '' }
    privacy.value = { publicProfile: data.publicProfile ?? true, showEarnings: data.showEarnings ?? false, showOnlineStatus: data.showOnlineStatus ?? true }
    if (data.role === 'FREELANCER') {
      try {
        const { data: p } = await axios.get(`${API_URL}/api/profile`, headers())
        profileForm.value = { title: p.title || '', bio: p.bio || '', location: p.location || '', skills: p.skills || '', hourlyRate: p.hourlyRate || null, experienceLevel: p.experienceLevel || 'JUNIOR', isAvailable: p.isAvailable ?? true, portfolioUrl: p.portfolioUrl || '', githubUrl: p.githubUrl || '', linkedinUrl: p.linkedinUrl || '' }
      } catch { /* silencieux */ }
    }
  } catch {
    const user = JSON.parse(localStorage.getItem('user') || sessionStorage.getItem('user') || '{}')
    form.value.email = user.email || ''
    form.value.role  = user.role  || ''
    showToast('Impossible de charger les paramètres', 'error')
  } finally {
    loading.value = false
  }
}

const saveAccount = async () => {
  savingAccount.value = true
  try {
    await axios.put(`${API_URL}/api/settings/account`, { phone: form.value.phone, language: form.value.language, currency: form.value.currency }, headers())
    showToast('Informations enregistrées !')
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la sauvegarde', 'error')
  } finally { savingAccount.value = false }
}

const saveProfile = async () => {
  savingProfile.value = true
  try {
    await axios.put(`${API_URL}/api/profile`, { ...profileForm.value }, headers())
    showToast('Profil mis à jour !')
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la sauvegarde', 'error')
  } finally { savingProfile.value = false }
}

const savePrivacy = async () => {
  savingPrivacy.value = true
  try {
    await axios.put(`${API_URL}/api/settings/privacy`, { ...privacy.value }, headers())
    showToast('Préférences enregistrées !')
  } catch { showToast('Erreur lors de la sauvegarde', 'error')
  } finally { savingPrivacy.value = false }
}

const changePassword = async () => {
  modalError.value = ''
  if (!pwdForm.value.current)           { modalError.value = 'Entrez votre mot de passe actuel'; return }
  if (pwdForm.value.new.length < 8)     { modalError.value = 'Minimum 8 caractères'; return }
  if (pwdForm.value.new !== pwdForm.value.confirm) { modalError.value = 'Les mots de passe ne correspondent pas'; return }
  savingPassword.value = true
  try {
    await axios.put(`${API_URL}/api/auth/change-password`, { currentPassword: pwdForm.value.current, newPassword: pwdForm.value.new }, headers())
    showToast('Mot de passe modifié !')
    closeModal()
  } catch (err) {
    modalError.value = err.response?.data?.message || 'Mot de passe actuel incorrect'
  } finally { savingPassword.value = false }
}

const handleDelete = async () => {
  if (!confirmDelete.value) { confirmDelete.value = true; setTimeout(() => { confirmDelete.value = false }, 5000); return }
  deletingAccount.value = true
  try {
    await axios.delete(`${API_URL}/api/auth/account`, headers())
    authStore.clearAuth()
    window.location.href = '/'
  } catch (err) {
    showToast(err.response?.data?.message || 'Erreur lors de la suppression', 'error')
    confirmDelete.value = false
    deletingAccount.value = false
  }
}

const closeModal = () => {
  showPasswordModal.value = false
  modalError.value = ''
  pwdForm.value = { current: '', new: '', confirm: '' }
  showPwd.value = { current: false, new: false }
}

onMounted(loadSettings)
</script>
