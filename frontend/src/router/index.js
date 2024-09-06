import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import IndexPage from '../views/IndexPage.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/index',
      name: 'IndexPage',
      component: IndexPage,
      meta: { requiresAuth: true },
    },
    {
      path: '/login',
      name: 'LoginRegisterPage',
      component: () => import('../views/LoginRegisterPage.vue'),
    },
  ],
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.matched.some(record => record.meta.requiresAuth) && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router;
