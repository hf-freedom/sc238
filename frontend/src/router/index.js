import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/application'
  },
  {
    path: '/application',
    name: 'Application',
    component: () => import('../views/Application.vue')
  },
  {
    path: '/supplier',
    name: 'Supplier',
    component: () => import('../views/Supplier.vue')
  },
  {
    path: '/quote/:applicationId',
    name: 'Quote',
    component: () => import('../views/Quote.vue')
  },
  {
    path: '/order',
    name: 'Order',
    component: () => import('../views/Order.vue')
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: () => import('../views/Statistics.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
