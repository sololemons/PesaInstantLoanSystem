import { requireAuth } from '@/stores/authGuard'
import ApplyPage from '@/views/ApplyPage.vue'
import HomePage from '@/views/HomePage.vue'
import LoanManagement from '@/views/LoanManagement.vue'
import Profile from '@/views/Profile.vue'
import VerifyLoanAmount from '@/views/VerifyLoanAmount.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'homepage',
      component: HomePage,
    },
    {
      path: '/apply',
      name: 'apply',
      component: ApplyPage,
      beforeEnter: requireAuth,
    },

    {
      path: '/profile',
      name: 'profile',
      component: Profile,
      beforeEnter: requireAuth,
    },
    {
      path: '/disbursment',
      name: 'disbursment',
      component: VerifyLoanAmount,
    },
    {
      path: '/management',
      name: 'management',
      component: LoanManagement,
      beforeEnter: requireAuth,
    },
  ],
})

export default router
