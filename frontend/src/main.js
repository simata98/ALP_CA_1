import '@coreui/coreui/dist/css/coreui.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import CoreuiVue from '@coreui/vue';
import { iconsSet as icons } from './components/icons';
import CIcon from '@coreui/icons-vue';

import App from './App.vue';
import router from './router';
import axios from 'axios';
import DocsExample from './components/DocsExample.vue';

// Axios 인터셉터 설정
axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      // JWT 토큰이 존재할 경우 Authorization 헤더에 추가
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  },
);

const app = createApp(App);

// Axios를 vue 인스턴스에 추가
app.config.globalProperties.$axios = axios;

app.use(createPinia());
app.use(router);
app.use(CoreuiVue);
app.provide('icons', icons);
app.component('CIcon', CIcon);
app.component('DocsExample', DocsExample);
app.mount('#app');
