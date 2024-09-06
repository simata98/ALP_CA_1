import './assets/main.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
import router from './router';
import vuetify from './plugins/vuetify';
import axios from 'axios';

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
app.use(vuetify);
app.use(router);

app.mount('#app');
