<template>
  <div class="flex h-[calc(100vh-112px)] bg-white rounded-2xl overflow-hidden shadow-sm border border-[#E8E8E2]">

    <!-- ═══ TOASTS ═══════════════════════════════════════════════════ -->
    <Teleport to="body">
      <div class="fixed top-5 right-5 z-[9999] flex flex-col gap-2.5 pointer-events-none w-80">
        <TransitionGroup name="toast">
          <div v-for="toast in toasts" :key="toast.id"
               class="relative pointer-events-auto overflow-hidden
                      flex items-start gap-3 bg-white border border-[#E8E8E2]
                      rounded-2xl shadow-xl px-4 py-3.5 cursor-pointer select-none
                      hover:shadow-2xl transition-shadow"
               @click="openConvFromToast(toast)">
            <div class="w-9 h-9 rounded-full flex items-center justify-center text-xs font-bold flex-shrink-0 mt-0.5"
                 :style="avatarStyle(toast.conv)">{{ toast.conv.initials }}</div>
            <div class="flex-1 min-w-0">
              <p class="text-xs font-bold text-ink truncate">{{ toast.conv.offerTitle }}</p>
              <p class="text-[11px] text-[#9C9A92]">{{ toast.conv.otherLabel }}</p>
              <p class="text-xs text-ink mt-1.5 line-clamp-2 leading-relaxed">{{ toast.msg.content }}</p>
            </div>
            <button @click.stop="dismissToast(toast.id)"
                    class="w-5 h-5 rounded-full hover:bg-[#F0EFE6] flex items-center justify-center
                           text-[#B0AFA7] hover:text-ink transition flex-shrink-0 mt-0.5">
              <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
            <!-- barre de progression -->
            <div class="absolute bottom-0 left-0 h-0.5 bg-brand-500 rounded-b-2xl toast-progress"
                 :style="{ animationDuration: TOAST_MS + 'ms' }"></div>
          </div>
        </TransitionGroup>
      </div>
    </Teleport>

    <!-- ═══ SIDEBAR ══════════════════════════════════════════════════ -->
    <div class="w-80 flex-shrink-0 flex flex-col border-r border-[#F0EFE6] bg-[#FAFAF7]">

      <!-- Header -->
      <div class="px-5 pt-5 pb-4 border-b border-[#EEEEE8]">
        <div class="flex items-center justify-between mb-3">
          <h2 class="text-base font-bold text-ink">Messages</h2>
          <span v-if="totalUnread > 0"
                class="text-[11px] font-bold bg-brand-500 text-white px-2 py-0.5 rounded-full min-w-[22px] text-center animate-pulse">
            {{ totalUnread > 99 ? '99+' : totalUnread }}
          </span>
          <span v-else-if="conversations.length > 0"
                class="text-[11px] font-semibold bg-brand-100 text-brand-700 px-2 py-0.5 rounded-full">
            {{ conversations.length }}
          </span>
        </div>
        <div class="relative">
          <svg class="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-[#B0AFA7]"
               fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-4.35-4.35M17 11A6 6 0 111 11a6 6 0 0116 0z"/>
          </svg>
          <input v-model="searchQuery" type="text" placeholder="Rechercher…"
                 class="w-full bg-white border border-[#E8E8E2] rounded-lg pl-8 pr-3 py-2
                        text-xs text-ink placeholder:text-[#B0AFA7] outline-none
                        focus:border-brand-500 focus:ring-2 focus:ring-brand-500/10 transition"/>
        </div>
      </div>

      <!-- Skeleton -->
      <div v-if="loadingConvs" class="p-3 space-y-1 flex-1">
        <div v-for="i in 4" :key="i" class="flex items-center gap-3 px-3 py-3 rounded-xl animate-pulse">
          <div class="w-10 h-10 bg-[#EEEEE8] rounded-full flex-shrink-0"></div>
          <div class="flex-1 space-y-2">
            <div class="h-3 bg-[#EEEEE8] rounded-md w-3/4"></div>
            <div class="h-2.5 bg-[#EEEEE8] rounded-md w-1/2"></div>
          </div>
        </div>
      </div>

      <!-- Liste -->
      <div v-else-if="filteredConversations.length > 0" class="flex-1 overflow-y-auto py-2">
        <button v-for="conv in filteredConversations" :key="conv.id"
                @click="selectConversation(conv)"
                class="w-full text-left relative flex items-center gap-3 px-4 py-3 mx-1 rounded-xl transition-all duration-150"
                :class="selectedConv?.id === conv.id
                  ? 'bg-white shadow-sm border border-[#E8E8E2]'
                  : 'hover:bg-white/70'">
          <div class="relative flex-shrink-0">
            <div class="w-10 h-10 rounded-full flex items-center justify-center text-xs font-bold"
                 :style="avatarStyle(conv)">{{ conv.initials }}</div>
            <span class="absolute -bottom-0.5 -right-0.5 w-3 h-3 rounded-full border-2 border-[#FAFAF7]"
                  :class="conv.status === 'ACCEPTED'            ? 'bg-brand-500'
                         : conv.status === 'AWAITING_VALIDATION' ? 'bg-amber-400'
                         : 'bg-[#C4C3BC]'"></span>
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-xs truncate leading-tight"
               :class="unreadCounts[conv.id] ? 'font-bold text-ink' : 'font-semibold text-ink'">
              {{ conv.offerTitle }}
            </p>
            <p class="text-[11px] text-[#9C9A92] truncate leading-tight mt-0.5">{{ conv.otherLabel }}</p>
          </div>
          <!-- badge non-lu -->
          <span v-if="unreadCounts[conv.id]"
                class="text-[10px] font-bold bg-brand-500 text-white rounded-full
                       min-w-[18px] h-[18px] flex items-center justify-center px-1 flex-shrink-0">
            {{ unreadCounts[conv.id] > 9 ? '9+' : unreadCounts[conv.id] }}
          </span>
          <span v-else
                class="text-[9px] font-semibold px-1.5 py-0.5 rounded-md flex-shrink-0"
                :class="conv.status === 'ACCEPTED'            ? 'bg-brand-50 text-brand-700'
                       : conv.status === 'AWAITING_VALIDATION' ? 'bg-amber-50 text-amber-700'
                       : 'bg-[#F0EFE6] text-[#73726C]'">
            {{ statusLabel(conv.status) }}
          </span>
        </button>
      </div>

      <!-- Erreur -->
      <div v-else-if="conversationError"
           class="flex-1 flex items-center justify-center p-6 text-center">
        <div>
          <div class="w-12 h-12 bg-red-50 rounded-2xl flex items-center justify-center mx-auto mb-3">
            <svg class="w-5 h-5 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.8"
                    d="M12 9v4m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/>
            </svg>
          </div>
          <p class="text-xs font-semibold text-ink">Messagerie indisponible</p>
          <p class="text-[11px] text-[#9C9A92] mt-1 leading-relaxed">{{ conversationError }}</p>
        </div>
      </div>

      <!-- Vide -->
      <div v-else class="flex-1 flex items-center justify-center p-6 text-center">
        <div>
          <div class="w-14 h-14 bg-[#F0EFE6] rounded-2xl flex items-center justify-center mx-auto mb-4">
            <svg class="w-6 h-6 text-[#B0AFA7]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.6"
                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
          </div>
          <p class="text-xs font-semibold text-ink">Aucune mission active</p>
          <p class="text-[11px] text-[#9C9A92] mt-1.5 leading-relaxed max-w-[160px] mx-auto">
            Les conversations apparaissent dès qu'une candidature est acceptée
          </p>
        </div>
      </div>
    </div>

    <!-- ═══ ZONE CHAT ════════════════════════════════════════════════ -->
    <div class="flex-1 flex flex-col min-w-0 bg-white">

      <!-- Aucune sélection -->
      <div v-if="!selectedConv" class="flex-1 flex items-center justify-center">
        <div class="text-center">
          <div class="w-20 h-20 bg-[#F4F4ED] rounded-3xl flex items-center justify-center mx-auto mb-5 shadow-inner">
            <svg class="w-9 h-9 text-[#C4C3BC]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.4"
                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
          </div>
          <p class="text-sm font-bold text-ink">Vos conversations</p>
          <p class="text-xs text-[#9C9A92] mt-1.5 max-w-[200px] mx-auto leading-relaxed">
            Sélectionnez une mission dans la liste pour démarrer
          </p>
        </div>
      </div>

      <template v-else>

        <!-- Header conversation -->
        <div class="flex items-center gap-4 px-6 py-3.5 border-b border-[#F0EFE6] flex-shrink-0">
          <div class="relative flex-shrink-0">
            <div class="w-10 h-10 rounded-full flex items-center justify-center text-xs font-bold"
                 :style="avatarStyle(selectedConv)">{{ selectedConv.initials }}</div>
            <span class="absolute -bottom-0.5 -right-0.5 w-3 h-3 rounded-full border-2 border-white"
                  :class="selectedConv.status === 'ACCEPTED'            ? 'bg-brand-500'
                         : selectedConv.status === 'AWAITING_VALIDATION' ? 'bg-amber-400'
                         : 'bg-[#C4C3BC]'"></span>
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-sm font-bold text-ink truncate leading-snug">{{ selectedConv.offerTitle }}</p>
            <div class="flex items-center gap-1.5 mt-0.5">
              <p class="text-xs text-[#9C9A92] leading-tight">{{ selectedConv.otherLabel }}</p>
              <!-- statut connexion WS -->
              <span class="flex items-center gap-1 text-[10px] font-medium"
                    :class="wsStatus === 'connected' ? 'text-brand-600'
                           : wsStatus === 'reconnecting' ? 'text-amber-600'
                           : 'text-[#9C9A92]'">
                <span class="w-1.5 h-1.5 rounded-full"
                      :class="wsStatus === 'connected'    ? 'bg-brand-500'
                             : wsStatus === 'reconnecting' ? 'bg-amber-400 animate-pulse'
                             : 'bg-[#C4C3BC]'"></span>
                {{ wsStatus === 'connected' ? 'En ligne' : wsStatus === 'reconnecting' ? 'Reconnexion…' : 'Hors ligne' }}
              </span>
            </div>
          </div>
          <span class="text-[10px] font-semibold px-2.5 py-1 rounded-lg border flex-shrink-0"
                :class="selectedConv.status === 'ACCEPTED'
                  ? 'bg-brand-50 text-brand-700 border-brand-100'
                  : selectedConv.status === 'AWAITING_VALIDATION'
                  ? 'bg-amber-50 text-amber-700 border-amber-100'
                  : 'bg-[#F4F4ED] text-[#73726C] border-[#EBEBE5]'">
            {{ statusLabel(selectedConv.status) }}
          </span>
        </div>

        <!-- Zone messages -->
        <div class="flex-1 relative overflow-hidden">
          <div ref="messagesContainer"
               class="h-full overflow-y-auto px-6 py-5 bg-[#FAFAF7]"
               @scroll="onScroll">

            <!-- Skeleton -->
            <div v-if="loadingMessages" class="space-y-4">
              <div v-for="i in 5" :key="i" class="flex animate-pulse"
                   :class="i % 2 === 0 ? 'justify-end' : 'justify-start'">
                <div class="rounded-2xl"
                     :class="[i % 2 === 0 ? 'bg-[#D8EDD4]' : 'bg-[#EEEEE8]',
                               i % 3 === 0 ? 'h-10 w-52' : 'h-8 w-36']"></div>
              </div>
            </div>

            <!-- Erreur -->
            <div v-else-if="messageError"
                 class="h-full flex items-center justify-center text-center">
              <div>
                <div class="w-12 h-12 bg-red-50 rounded-2xl flex items-center justify-center mx-auto mb-3">
                  <svg class="w-5 h-5 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.8"
                          d="M12 9v4m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/>
                  </svg>
                </div>
                <p class="text-sm font-semibold text-ink">Impossible d'ouvrir la conversation</p>
                <p class="text-xs text-[#9C9A92] mt-1">{{ messageError }}</p>
              </div>
            </div>

            <!-- Vide -->
            <div v-else-if="messages.length === 0"
                 class="h-full flex items-center justify-center text-center">
              <div>
                <div class="w-14 h-14 bg-white rounded-2xl border border-[#EEEEE8] shadow-sm
                            flex items-center justify-center mx-auto mb-4">
                  <svg class="w-6 h-6 text-[#B0AFA7]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.6"
                          d="M7 8h10M7 12h6m-9 8l4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"/>
                  </svg>
                </div>
                <p class="text-sm font-semibold text-ink">Commencez la discussion</p>
                <p class="text-xs text-[#9C9A92] mt-1.5 leading-relaxed max-w-[180px] mx-auto">
                  Envoyez votre premier message pour démarrer la collaboration
                </p>
              </div>
            </div>

            <!-- Messages -->
            <template v-else>
              <template v-for="(msg, index) in messages" :key="msg.id">

                <!-- Séparateur date -->
                <div v-if="shouldShowDate(index)" class="flex items-center gap-3 my-5">
                  <div class="flex-1 h-px bg-[#E8E8E2]"></div>
                  <span class="text-[10px] text-[#9C9A92] font-medium bg-[#F0EFE6]
                               px-3 py-1 rounded-full select-none tracking-wide">
                    {{ formatDateLabel(msg.createdAt) }}
                  </span>
                  <div class="flex-1 h-px bg-[#E8E8E2]"></div>
                </div>

                <!-- Bulle -->
                <div class="group flex items-end gap-2 msg-in"
                     :class="[
                       msg.senderId === myId ? 'justify-end' : 'justify-start',
                       index > 0 && messages[index-1].senderId === msg.senderId && !shouldShowDate(index)
                         ? 'mt-1' : 'mt-4'
                     ]"
                     @mouseenter="hoveredId = msg.id"
                     @mouseleave="hoveredId = null">

                  <!-- Avatar interlocuteur -->
                  <div v-if="msg.senderId !== myId"
                       class="w-7 h-7 rounded-full flex items-center justify-center text-[9px] font-bold
                              flex-shrink-0 mb-0.5 shadow-sm"
                       :style="avatarStyle(selectedConv)">
                    {{ selectedConv.initials }}
                  </div>

                  <div class="max-w-[62%] relative">
                    <!-- Bouton copier (hover) -->
                    <Transition name="fade-quick">
                      <button v-if="hoveredId === msg.id"
                              @click="copyMsg(msg)"
                              class="absolute -top-7 z-10 flex items-center gap-1
                                     bg-white border border-[#E8E8E2] shadow-md
                                     text-[10px] font-semibold text-[#73726C] hover:text-ink
                                     px-2 py-1 rounded-lg transition-colors"
                              :class="msg.senderId === myId ? 'right-0' : 'left-0'">
                        <svg v-if="copiedId !== msg.id" class="w-3 h-3" fill="none" stroke="currentColor"
                             viewBox="0 0 24 24" stroke-width="2">
                          <path stroke-linecap="round" stroke-linejoin="round"
                                d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"/>
                        </svg>
                        <svg v-else class="w-3 h-3 text-brand-500" fill="none" stroke="currentColor"
                             viewBox="0 0 24 24" stroke-width="2.5">
                          <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                        </svg>
                        {{ copiedId === msg.id ? 'Copié !' : 'Copier' }}
                      </button>
                    </Transition>

                    <!-- Contenu -->
                    <div class="px-4 py-2.5 text-sm leading-relaxed shadow-sm"
                         :class="msg.senderId === myId
                           ? 'bg-ink text-white rounded-2xl rounded-br-md'
                           : 'bg-white text-ink rounded-2xl rounded-bl-md border border-[#EEEEE8]'"
                         v-html="renderContent(msg.content, msg.senderId === myId)">
                    </div>
                    <!-- Heure -->
                    <p class="text-[10px] text-[#B0AFA7] mt-1 leading-none"
                       :class="msg.senderId === myId ? 'text-right pr-1' : 'text-left pl-1'">
                      {{ relativeTime(msg.createdAt) }}
                    </p>
                  </div>

                  <div v-if="msg.senderId === myId" class="w-7 flex-shrink-0"></div>
                </div>

              </template>

              <!-- Indicateur de frappe -->
              <Transition name="typing-fade">
                <div v-if="peerTyping" class="flex items-end gap-2 mt-4">
                  <div class="w-7 h-7 rounded-full flex items-center justify-center text-[9px] font-bold
                              flex-shrink-0 mb-0.5 shadow-sm"
                       :style="avatarStyle(selectedConv)">
                    {{ selectedConv.initials }}
                  </div>
                  <div class="bg-white border border-[#EEEEE8] rounded-2xl rounded-bl-md
                              px-4 py-3 shadow-sm flex items-center gap-1">
                    <span class="w-1.5 h-1.5 bg-[#9C9A92] rounded-full typing-dot-1"></span>
                    <span class="w-1.5 h-1.5 bg-[#9C9A92] rounded-full typing-dot-2"></span>
                    <span class="w-1.5 h-1.5 bg-[#9C9A92] rounded-full typing-dot-3"></span>
                  </div>
                </div>
              </Transition>
            </template>
          </div>

          <!-- Bouton "Nouveau message" flottant -->
          <Transition name="slide-up">
            <button v-if="newMsgBelow"
                    @click="scrollToBottom"
                    class="absolute bottom-4 left-1/2 -translate-x-1/2 z-10
                           flex items-center gap-2 bg-ink text-white text-xs font-semibold
                           px-4 py-2 rounded-full shadow-lg hover:bg-[#1A1A18] transition-all border border-white/10">
              <span class="w-2 h-2 rounded-full bg-brand-500 animate-pulse"></span>
              Nouveau message
              <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7"/>
              </svg>
            </button>
          </Transition>
        </div>

        <!-- Emoji picker -->
        <Transition name="picker-pop">
          <div v-if="showEmoji" ref="emojiPickerRef"
               class="mx-5 mb-2 bg-white border border-[#E8E8E2] rounded-2xl shadow-xl
                      p-3 grid grid-cols-10 gap-1">
            <button v-for="e in EMOJIS" :key="e"
                    @click="insertEmoji(e)"
                    class="text-lg h-8 w-full rounded-lg hover:bg-[#F4F4ED] transition flex items-center justify-center">
              {{ e }}
            </button>
          </div>
        </Transition>

        <!-- Zone de saisie -->
        <div class="px-5 pb-5 pt-2 border-t border-[#F0EFE6] flex-shrink-0 bg-white">
          <div class="flex items-end gap-2 bg-[#F4F4ED] rounded-2xl px-3 py-2.5
                      focus-within:bg-white focus-within:ring-2 focus-within:ring-brand-500/20
                      border border-transparent focus-within:border-brand-500/20 transition-all duration-200">

            <!-- Bouton emoji -->
            <button @click.stop="toggleEmoji"
                    class="w-8 h-8 rounded-xl flex items-center justify-center flex-shrink-0
                           text-[#9C9A92] hover:text-ink hover:bg-[#EEEEE8] transition mb-0.5"
                    :class="showEmoji ? 'bg-[#EEEEE8] text-ink' : ''">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="1.8">
                <path stroke-linecap="round" stroke-linejoin="round"
                      d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01
                         M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
            </button>

            <!-- Textarea -->
            <textarea v-model="newMessage"
                      ref="textareaRef"
                      @keydown.enter.exact.prevent="sendMessage"
                      @input="onInput"
                      rows="1"
                      placeholder="Écrivez un message…"
                      class="flex-1 resize-none text-sm text-ink bg-transparent
                             outline-none placeholder:text-[#B0AFA7] max-h-28
                             overflow-y-auto leading-relaxed pt-1.5">
            </textarea>

            <!-- Son toggle -->
            <button @click="soundOn = !soundOn"
                    :title="soundOn ? 'Couper le son' : 'Activer le son'"
                    class="w-8 h-8 rounded-xl flex items-center justify-center flex-shrink-0
                           text-[#9C9A92] hover:text-ink hover:bg-[#EEEEE8] transition mb-0.5">
              <svg v-if="soundOn" class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round"
                      d="M15.536 8.464a5 5 0 010 7.072M12 6v12m-3.536-9.536A5 5 0 008.464 12a5 5 0 00-.464 2 5 5 0 00.464 2M6 9.344A8 8 0 0012 21a8 8 0 008-14M6 9.344A8 8 0 0112 3"/>
              </svg>
              <svg v-else class="w-4 h-4 text-[#C4C3BC]" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round"
                      d="M5.586 15H4a1 1 0 01-1-1v-4a1 1 0 011-1h1.586l4.707-4.707C10.923 3.663 12 4.109 12 5v14c0 .891-1.077 1.337-1.707.707L5.586 15z"/>
                <path stroke-linecap="round" stroke-linejoin="round" d="M17 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2"/>
              </svg>
            </button>

            <!-- Envoyer -->
            <button @click="sendMessage"
                    :disabled="!newMessage.trim() || sending"
                    class="w-8 h-8 rounded-xl flex items-center justify-center flex-shrink-0
                           transition-all duration-150 mb-0.5"
                    :class="newMessage.trim() && !sending
                      ? 'bg-ink hover:bg-[#1A1A18] shadow-sm hover:shadow-md'
                      : 'bg-[#D8D7CE] cursor-not-allowed'">
              <svg v-if="sending" class="w-3.5 h-3.5 text-white animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/>
              </svg>
              <svg v-else class="w-3.5 h-3.5 text-white" fill="currentColor" viewBox="0 0 24 24">
                <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
              </svg>
            </button>
          </div>
          <p class="text-[10px] text-[#C4C3BC] mt-2 pl-1 select-none">
            Entrée pour envoyer &nbsp;·&nbsp; Maj+Entrée pour nouvelle ligne
          </p>
        </div>

      </template>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

const route    = useRoute()
const API_URL  = import.meta.env.VITE_API_URL         || 'http://localhost:8080'
const WS_BASE  = import.meta.env.VITE_JOB_SERVICE_URL || 'http://localhost:8083'
const getToken = () => localStorage.getItem('token') || sessionStorage.getItem('token')
const authHeaders = () => ({
  headers: { Authorization: `Bearer ${getToken()}` },
  _noRedirectOn403: true,
})

const TOAST_MS = 5000
const EMOJIS = [
  '😀','😂','😊','😍','😎','😢','😤','😭','🤔','🥰',
  '😅','🤣','😇','😘','😏','😒','😩','🤯','😱','🥳',
  '👍','👎','👋','🤝','👏','🙌','💪','🤞','✌️','🫶',
  '❤️','💔','🔥','✅','❌','⚠️','💡','🎉','🚀','💯',
  '⭐','🌟','💰','🏆','📌','🔗','📝','✍️','💼','🎯',
]

const AVATAR_COLORS = [
  { bg: '#E8F5E2', text: '#2D6A1E', border: '#C5E0BB' },
  { bg: '#E3F0FF', text: '#1A5BAB', border: '#B8D4F5' },
  { bg: '#FFF3E0', text: '#B45309', border: '#F5D9A8' },
  { bg: '#F3E8FF', text: '#7C3AED', border: '#DDD0F7' },
  { bg: '#FEE2E2', text: '#B91C1C', border: '#FBBFBF' },
  { bg: '#ECFDF5', text: '#065F46', border: '#A7F3D0' },
]
const avatarStyle = (conv) => {
  const c = AVATAR_COLORS[conv.id % AVATAR_COLORS.length]
  return { backgroundColor: c.bg, color: c.text, border: `1.5px solid ${c.border}` }
}

const myId = computed(() => {
  try {
    const token = getToken()
    if (!token) return null
    const b64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
    const p   = JSON.parse(atob(b64.padEnd(b64.length + (4 - b64.length % 4) % 4, '=')))
    return String(p.userId)
  } catch { return null }
})
const userRole = JSON.parse(localStorage.getItem('user') || sessionStorage.getItem('user') || '{}').role || ''

// ── STATE ───────────────────────────────────────────────────────────
const conversations     = ref([])
const loadingConvs      = ref(true)
const conversationError = ref('')
const selectedConv      = ref(null)
const selectedConvId    = ref(null)
const messages          = ref([])
const loadingMessages   = ref(false)
const messageError      = ref('')
const newMessage        = ref('')
const sending           = ref(false)
const messagesContainer = ref(null)
const textareaRef       = ref(null)
const emojiPickerRef    = ref(null)
const searchQuery       = ref('')
const unreadCounts      = reactive({})
const toasts            = ref([])
const isAtBottom        = ref(true)
const newMsgBelow       = ref(false)
const wsStatus          = ref('connecting')   // 'connecting' | 'connected' | 'reconnecting' | 'disconnected'
const peerTyping        = ref(false)
const soundOn           = ref(true)
const showEmoji         = ref(false)
const hoveredId         = ref(null)
const copiedId          = ref(null)
const nowTick           = ref(Date.now())

const totalUnread = computed(() => Object.values(unreadCounts).reduce((s, n) => s + n, 0))
const filteredConversations = computed(() => {
  const q = searchQuery.value.trim().toLowerCase()
  if (!q) return conversations.value
  return conversations.value.filter(c =>
    c.offerTitle.toLowerCase().includes(q) || c.otherLabel.toLowerCase().includes(q)
  )
})

let stompClient   = null
let typingTimer   = null
let iAmTyping     = false
let peerTypingTimer = null
const toastTimers = {}
let nowInterval   = null

// ── SOUND ────────────────────────────────────────────────────────────
const playPing = () => {
  if (!soundOn.value) return
  try {
    const Ctx  = window.AudioContext || window.webkitAudioContext
    if (!Ctx) return
    const ctx  = new Ctx()
    const play = (freq, start, dur) => {
      const osc  = ctx.createOscillator()
      const gain = ctx.createGain()
      osc.connect(gain); gain.connect(ctx.destination)
      osc.type = 'sine'
      osc.frequency.setValueAtTime(freq, ctx.currentTime + start)
      gain.gain.setValueAtTime(0, ctx.currentTime + start)
      gain.gain.linearRampToValueAtTime(0.25, ctx.currentTime + start + 0.02)
      gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + start + dur)
      osc.start(ctx.currentTime + start)
      osc.stop(ctx.currentTime + start + dur)
    }
    play(880,  0,    0.15)
    play(1108, 0.12, 0.20)
    setTimeout(() => ctx.close(), 600)
  } catch {}
}

// ── TOAST ─────────────────────────────────────────────────────────────
const showToast = (conv, msg) => {
  const id = Date.now() + Math.random()
  toasts.value.push({ id, conv, msg })
  toastTimers[id] = setTimeout(() => dismissToast(id), TOAST_MS)
  playPing()
}
const dismissToast = (id) => {
  clearTimeout(toastTimers[id]); delete toastTimers[id]
  toasts.value = toasts.value.filter(t => t.id !== id)
}
const openConvFromToast = (toast) => {
  dismissToast(toast.id); selectConversation(toast.conv)
}

// ── EMOJI PICKER ──────────────────────────────────────────────────────
const toggleEmoji = () => { showEmoji.value = !showEmoji.value }
const insertEmoji = (e) => {
  newMessage.value += e
  showEmoji.value   = false
  nextTick(() => textareaRef.value?.focus())
}
const onDocumentClick = (ev) => {
  if (showEmoji.value && emojiPickerRef.value && !emojiPickerRef.value.contains(ev.target))
    showEmoji.value = false
}

// ── COPY MESSAGE ───────────────────────────────────────────────────────
const copyMsg = async (msg) => {
  try {
    await navigator.clipboard.writeText(msg.content)
    copiedId.value = msg.id
    setTimeout(() => { copiedId.value = null }, 1500)
  } catch {}
}

// ── LINK DETECTION ─────────────────────────────────────────────────────
const renderContent = (text, isOwn) => {
  const escaped = text
    .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;').replace(/'/g, '&#39;')
  const linkClass = isOwn
    ? 'underline underline-offset-2 opacity-80 hover:opacity-100'
    : 'text-brand-600 underline underline-offset-2 hover:text-brand-700'
  return escaped.replace(
    /(https?:\/\/[^\s<]+)/g,
    `<a href="$1" target="_blank" rel="noopener noreferrer" class="${linkClass}">$1</a>`
  )
}

// ── SCROLL ─────────────────────────────────────────────────────────────
const onScroll = () => {
  const el = messagesContainer.value; if (!el) return
  isAtBottom.value = el.scrollHeight - el.scrollTop - el.clientHeight < 60
  if (isAtBottom.value) newMsgBelow.value = false
}
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value)
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    newMsgBelow.value = false
  })
}

// ── TYPING ─────────────────────────────────────────────────────────────
const sendTypingEvent = (typing) => {
  if (!stompClient?.active || !selectedConvId.value) return
  stompClient.publish({
    destination: `/app/chat/${selectedConvId.value}/typing`,
    body: JSON.stringify({ typing }),
  })
}
const stopTyping = () => {
  clearTimeout(typingTimer)
  if (iAmTyping) { iAmTyping = false; sendTypingEvent(false) }
}
const onInput = (e) => {
  autoResize(e)
  if (!selectedConvId.value) return
  if (!iAmTyping) { iAmTyping = true; sendTypingEvent(true) }
  clearTimeout(typingTimer)
  typingTimer = setTimeout(() => { iAmTyping = false; sendTypingEvent(false) }, 2500)
}

// ── WEBSOCKET ──────────────────────────────────────────────────────────
const connectAll = () => {
  if (stompClient) { stompClient.deactivate(); stompClient = null }
  wsStatus.value = 'connecting'

  stompClient = new Client({
    webSocketFactory: () => new SockJS(`${WS_BASE}/ws`),
    connectHeaders:   { Authorization: `Bearer ${getToken()}` },
    reconnectDelay:   3000,
    onConnect: () => {
      wsStatus.value = 'connected'
      conversations.value.forEach(conv => {
        const cid = `conv_${conv.id}`

        // Messages
        stompClient.subscribe(`/topic/conversation/${cid}`, (frame) => {
          const msg = JSON.parse(frame.body)
          if (selectedConvId.value === cid) {
            if (!messages.value.some(m => m.id === msg.id)) {
              messages.value.push(msg)
              peerTyping.value = false
              if (isAtBottom.value) scrollToBottom()
              else if (msg.senderId !== myId.value) newMsgBelow.value = true
            }
          } else {
            if (msg.senderId !== myId.value) {
              unreadCounts[conv.id] = (unreadCounts[conv.id] || 0) + 1
              showToast(conv, msg)
            }
          }
        })

        // Typing
        stompClient.subscribe(`/topic/conversation/${cid}/typing`, (frame) => {
          const ev = JSON.parse(frame.body)
          if (String(ev.userId) !== myId.value && selectedConvId.value === cid) {
            peerTyping.value = ev.typing
            clearTimeout(peerTypingTimer)
            if (ev.typing) {
              peerTypingTimer = setTimeout(() => { peerTyping.value = false }, 4000)
              if (isAtBottom.value) scrollToBottom()
            }
          }
        })
      })
    },
    onWebSocketClose: () => { wsStatus.value = 'reconnecting' },
    onWebSocketError: () => { wsStatus.value = 'disconnected' },
    onStompError:     () => { wsStatus.value = 'disconnected' },
  })
  stompClient.activate()
}

// ── LOAD CONVERSATIONS ─────────────────────────────────────────────────
const loadConversations = async () => {
  loadingConvs.value      = true
  conversationError.value = ''
  try {
    const ACTIVE = ['ACCEPTED', 'AWAITING_VALIDATION', 'COMPLETED']
    if (userRole === 'FREELANCER') {
      const { data } = await axios.get(`${API_URL}/api/applications/my`, authHeaders())
      conversations.value = data
        .filter(a => ACTIVE.includes(a.status))
        .map(a => ({ id: a.id, offerTitle: a.offerTitle || 'Mission',
                     status: a.status, otherLabel: 'Client', initials: 'CL' }))
    } else {
      const { data: offers } = await axios.get(`${API_URL}/api/offers/my`, authHeaders())
      const results = await Promise.allSettled(
        offers.map(o =>
          axios.get(`${API_URL}/api/offers/${o.id}/applications`, authHeaders())
            .then(r => r.data
              .filter(a => ACTIVE.includes(a.status))
              .map(a => ({
                id: a.id, offerTitle: o.title, status: a.status,
                freelancerId: a.freelancerId,
                otherLabel: `Freelancer #${String(a.freelancerId).slice(-3)}`,
                initials:   `F${String(a.freelancerId).slice(-2)}`,
              }))
            )
        )
      )
      const all = []
      results.forEach(r => { if (r.status === 'fulfilled') all.push(...r.value) })
      conversations.value = all
    }
    if (conversations.value.length > 0) connectAll()
    // auto-select if navigated from freelancer profile
    const targetFreelancer = route.query.freelancer ? Number(route.query.freelancer) : null
    if (targetFreelancer) {
      const conv = conversations.value.find(c => c.freelancerId === targetFreelancer)
      if (conv) selectConversation(conv)
    }
  } catch (e) {
    conversationError.value = e?.response?.data?.message || 'Impossible de charger vos conversations.'
  } finally {
    loadingConvs.value = false
  }
}

// ── SELECT CONVERSATION ────────────────────────────────────────────────
const selectConversation = async (conv) => {
  stopTyping()
  peerTyping.value      = false
  selectedConv.value    = conv
  selectedConvId.value  = `conv_${conv.id}`
  loadingMessages.value = true
  messageError.value    = ''
  messages.value        = []
  newMsgBelow.value     = false
  isAtBottom.value      = true
  delete unreadCounts[conv.id]

  try {
    const { data } = await axios.get(
      `${API_URL}/api/messages/history/${selectedConvId.value}`, authHeaders()
    )
    messages.value        = data
    loadingMessages.value = false
    scrollToBottom()
  } catch (e) {
    messageError.value    = e?.response?.data?.message || 'Impossible de charger les messages.'
    loadingMessages.value = false
  }
}

// ── SEND MESSAGE ───────────────────────────────────────────────────────
const sendMessage = () => {
  const content = newMessage.value.trim()
  if (!content || !selectedConvId.value || sending.value || !stompClient?.active) return
  stopTyping()
  sending.value    = true
  newMessage.value = ''
  if (textareaRef.value) textareaRef.value.style.height = 'auto'
  try {
    stompClient.publish({
      destination: `/app/chat/${selectedConvId.value}`,
      body: JSON.stringify({ content }),
    })
  } catch (e) {
    messageError.value = 'Envoi échoué. Vérifiez votre connexion.'
    newMessage.value   = content
  } finally {
    sending.value = false
  }
}

// ── HELPERS ────────────────────────────────────────────────────────────
const autoResize = (e) => {
  const el = e.target; el.style.height = 'auto'
  el.style.height = Math.min(el.scrollHeight, 112) + 'px'
}

const shouldShowDate = (i) => {
  if (i === 0) return true
  const c = new Date(messages.value[i].createdAt)
  const p = new Date(messages.value[i - 1].createdAt)
  return c.toDateString() !== p.toDateString()
}

const formatDateLabel = (ts) => {
  const d   = new Date(ts)
  const now = new Date()
  const y   = new Date(now); y.setDate(y.getDate() - 1)
  if (d.toDateString() === now.toDateString()) return "Aujourd'hui"
  if (d.toDateString() === y.toDateString())   return 'Hier'
  return d.toLocaleDateString('fr-FR', { weekday: 'long', day: 'numeric', month: 'long' })
}

const relativeTime = (ts) => {
  if (!ts) return ''
  const d    = new Date(ts)
  if (isNaN(d.getTime())) return ''
  const diff = Math.floor((nowTick.value - d.getTime()) / 1000)
  if (diff < 60)   return "À l'instant"
  if (diff < 3600) return `il y a ${Math.floor(diff / 60)} min`
  const now = new Date()
  if (d.toDateString() === now.toDateString())
    return d.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
  return d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short' }) +
         ' ' + d.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
}

const statusLabel = (s) => ({ ACCEPTED: 'En cours', AWAITING_VALIDATION: 'À valider', COMPLETED: 'Terminée' }[s] || s)

// ── LIFECYCLE ──────────────────────────────────────────────────────────
onMounted(() => {
  loadConversations()
  document.addEventListener('click', onDocumentClick)
  nowInterval = setInterval(() => { nowTick.value = Date.now() }, 30_000)
})
onUnmounted(() => {
  stopTyping()
  stompClient?.deactivate()
  document.removeEventListener('click', onDocumentClick)
  Object.keys(toastTimers).forEach(id => clearTimeout(toastTimers[id]))
  clearInterval(nowInterval)
  clearTimeout(peerTypingTimer)
})
</script>

<style scoped>
/* ── Toasts ─────────────────────────────────────────────── */
.toast-enter-active { transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1); }
.toast-leave-active { transition: all 0.22s ease-in; }
.toast-enter-from,
.toast-leave-to     { opacity: 0; transform: translateX(110%) scale(0.9); }

@keyframes shrink { from { width: 100%; } to { width: 0; } }
.toast-progress { animation: shrink linear forwards; }

/* ── Bouton "Nouveau message" ───────────────────────────── */
.slide-up-enter-active { transition: all 0.25s cubic-bezier(0.34, 1.56, 0.64, 1); }
.slide-up-leave-active { transition: all 0.2s ease-in; }
.slide-up-enter-from,
.slide-up-leave-to     { opacity: 0; transform: translateX(-50%) translateY(10px); }

/* ── Emoji picker ───────────────────────────────────────── */
.picker-pop-enter-active { transition: all 0.2s cubic-bezier(0.34, 1.56, 0.64, 1); }
.picker-pop-leave-active { transition: all 0.15s ease-in; }
.picker-pop-enter-from,
.picker-pop-leave-to     { opacity: 0; transform: translateY(8px) scale(0.95); }

/* ── Copier tooltip ─────────────────────────────────────── */
.fade-quick-enter-active { transition: all 0.15s ease-out; }
.fade-quick-leave-active { transition: all 0.1s ease-in; }
.fade-quick-enter-from,
.fade-quick-leave-to     { opacity: 0; transform: translateY(4px); }

/* ── Typing indicator transition ────────────────────────── */
.typing-fade-enter-active { transition: all 0.25s ease-out; }
.typing-fade-leave-active { transition: all 0.2s ease-in; }
.typing-fade-enter-from,
.typing-fade-leave-to     { opacity: 0; transform: translateY(6px); }

/* ── Typing dots bounce ──────────────────────────────────── */
@keyframes typingBounce {
  0%, 100% { transform: translateY(0); opacity: 0.5; }
  50%       { transform: translateY(-5px); opacity: 1; }
}
.typing-dot-1 { animation: typingBounce 1.1s ease-in-out infinite 0s; }
.typing-dot-2 { animation: typingBounce 1.1s ease-in-out infinite 0.18s; }
.typing-dot-3 { animation: typingBounce 1.1s ease-in-out infinite 0.36s; }

/* ── Message enter animation ─────────────────────────────── */
@keyframes msgIn {
  from { opacity: 0; transform: translateY(8px); }
  to   { opacity: 1; transform: translateY(0); }
}
.msg-in { animation: msgIn 0.22s ease-out both; }
</style>
