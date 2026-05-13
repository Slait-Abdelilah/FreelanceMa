import { initializeApp } from 'firebase/app'
import { getFirestore } from 'firebase/firestore'
import { getAuth } from 'firebase/auth'

const firebaseConfig = {
  apiKey: "AIzaSyAfmaDaBPlwYceoQWlVAeIor8LPJMDC3U0",
  authDomain: "freelancema-d0bed.firebaseapp.com",
  projectId: "freelancema-d0bed",
  storageBucket: "freelancema-d0bed.firebasestorage.app",
  messagingSenderId: "809873763318",
  appId: "1:809873763318:web:7ea5026e8a34626365b5d7"
}

const app = initializeApp(firebaseConfig)

export const db   = getFirestore(app)
export const auth = getAuth(app)
