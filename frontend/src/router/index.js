import { h, resolveComponent } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import IndexPage from '../views/IndexPage.vue';
import DefaultLayout from '@/layout/DefaultLayout.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: DefaultLayout,
      redirect: '/dashboard',
      children: [
        {
          path: '/dashboard',
          name: 'Dashboard',
          component: () => import('@/views/dashboard/Dashboard.vue'),
          meta: { requiresAuth: true },
        },
        {
          path: '/index',
          name: 'IndexPage',
          component: IndexPage,
          meta: { requiresAuth: true },
        },
        {
          path: '/theme',
          name: 'Theme',
          redirect: '/theme/typography',
        },
        {
          path: '/theme/colors',
          name: 'Colors',
          component: () => import('@/views/theme/Colors.vue'),
        },
        {
          path: '/theme/typography',
          name: 'Typography',
          component: () => import('@/views/theme/Typography.vue'),
        },
        {
          path: '/base',
          name: 'Base',
          component: {
            render() {
              return h(resolveComponent('router-view'));
            },
          },
          redirect: '/base/breadcrumbs',
          children: [
            {
              path: '/base/accordion',
              name: 'Accordion',
              component: () => import('@/views/base/Accordion.vue'),
            },
            {
              path: '/base/breadcrumbs',
              name: 'Breadcrumbs',
              component: () => import('@/views/base/Breadcrumbs.vue'),
            },
            {
              path: '/base/cards',
              name: 'Cards',
              component: () => import('@/views/base/Cards.vue'),
            },
            {
              path: '/base/carousels',
              name: 'Carousels',
              component: () => import('@/views/base/Carousels.vue'),
            },
            {
              path: '/base/collapses',
              name: 'Collapses',
              component: () => import('@/views/base/Collapses.vue'),
            },
            {
              path: '/base/list-groups',
              name: 'List Groups',
              component: () => import('@/views/base/ListGroups.vue'),
            },
            {
              path: '/base/navs',
              name: 'Navs',
              component: () => import('@/views/base/Navs.vue'),
            },
            {
              path: '/base/paginations',
              name: 'Paginations',
              component: () => import('@/views/base/Paginations.vue'),
            },
            {
              path: '/base/placeholders',
              name: 'Placeholders',
              component: () => import('@/views/base/Placeholders.vue'),
            },
            {
              path: '/base/popovers',
              name: 'Popovers',
              component: () => import('@/views/base/Popovers.vue'),
            },
            {
              path: '/base/progress',
              name: 'Progress',
              component: () => import('@/views/base/Progress.vue'),
            },
            {
              path: '/base/spinners',
              name: 'Spinners',
              component: () => import('@/views/base/Spinners.vue'),
            },
            {
              path: '/base/tables',
              name: 'Tables',
              component: () => import('@/views/base/Tables.vue'),
            },
            {
              path: '/base/tabs',
              name: 'Tabs',
              component: () => import('@/views/base/Tabs.vue'),
            },
            {
              path: '/base/tooltips',
              name: 'Tooltips',
              component: () => import('@/views/base/Tooltips.vue'),
            },
          ],
        },
      ],
    },
    {
      path: '/login',
      name: 'LoginRegisterPage',
      component: () => import('../views/LoginRegisterPage.vue'),
    },
  ],
  scrollBehavior() {
    return { top: 0 };
  },
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
