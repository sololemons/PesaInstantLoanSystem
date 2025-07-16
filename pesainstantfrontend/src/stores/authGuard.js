import { useAuthStore } from '@/stores/auth'

export const requireAuth = (to, from, next) => {
  const authStore = useAuthStore()

  if (!authStore.isAuthenticated) {
    return next({ name: 'homepage' })
  }

  next()
}
