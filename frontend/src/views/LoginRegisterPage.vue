<template>
  <!-- CToast for notifications -->
  <CToaster class="p-3" placement="top-center">
    <CToast
      v-if="toastVisible"
      :autohide="false"
      :color="toastColor"
      class="text-white align-items-center"
      visible
    >
      <div class="d-flex">
        <CToastBody>{{ toastMessage }}</CToastBody>
        <CToastClose class="me-2 m-auto" white @click="toastVisible = false" />
      </div>
    </CToast>
  </CToaster>
  <div id="app">
    <div class="loginBox">
      <div class="inner">
        <div class="signIn" v-if="signIn">
          <div class="top">
            <img class="logo" src="../assets/kt-ci.png" alt="Logo" />
            <div class="title">로그인</div>
            <div class="subtitle">
              계정이 없으신가요?
              <span class="subtitle-action" @click="toggleForm">
                계정 만들기
              </span>
            </div>
          </div>
          <form @submit.prevent="login">
            <div class="form">
              <input
                required
                aria-required="true"
                aria-invalid="false"
                aria-label="username"
                type="text"
                class="w100"
                placeholder="Username"
                v-model="username"
              />

              <input
                required
                aria-required="true"
                type="password"
                class="w100"
                placeholder="Password"
                v-model="password"
              />
            </div>

            <input
              type="submit"
              value="Submit"
              class="action"
              :class="{ 'action-disabled': !loginValid }"
            />
          </form>
        </div>

        <div class="register" v-else>
          <div class="top">
            <img class="logo" src="../assets/kt-ci.png" />
            <div class="title">계정 만들기</div>
            <div class="subtitle">
              이미 계정을 만들었나요?
              <span class="subtitle-action" @click="toggleForm"> 로그인 </span>
            </div>
          </div>

          <form @submit.prevent="register">
            <div class="form">
              <input
                required
                type="text"
                class="w100"
                placeholder="Username"
                v-model="username"
              />
              <input
                required
                type="password"
                class="w100"
                placeholder="Password"
                v-model="password"
              />
            </div>

            <button
              type="submit"
              class="action"
              :class="{ 'action-disabled': !registerValid }"
            >
              Create Account
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth'; // Pinia store import
import { CToaster } from '@coreui/vue';

// Refs and Pinia store setup
const username = ref('');
const password = ref('');
const signIn = ref(true); // To toggle between login/register
const authStore = useAuthStore();
const router = useRouter();

// Toast states
const toastVisible = ref(false);
const toastMessage = ref('');
const toastColor = ref('');

// Methods for login and register
const login = async () => {
  try {
    const response = await axios.post('/auth/login', {
      username: username.value,
      password: password.value,
    });
    const token = response.data.token;

    // Save token to Pinia store and localStorage
    authStore.setToken(token);
    toastMessage.value = '로그인 성공!';
    toastColor.value = 'primary';
    toastVisible.value = true;

    setTimeout(() => {
      router.push('/index'); // Navigate to home page after login
      toastVisible.value = false;
    }, 2000);
  } catch (error) {
    toastMessage.value = error.response?.data?.message || '로그인 실패';
    toastColor.value = 'danger';
    toastVisible.value = true;
  }
};

const register = async () => {
  try {
    await axios.post('/auth/register', {
      username: username.value,
      password: password.value,
    });
    toastMessage.value = '회원가입 성공!';
    toastColor.value = 'primary';
    toastVisible.value = true;

    setTimeout(() => {
      toggleForm(); // Switch back to login form
      toastVisible.value = false;
    }, 2000);
  } catch (error) {
    toastMessage.value = error.response?.data?.message || '회원가입 실패';
    toastColor.value = 'danger';
    toastVisible.value = true;
  }
};

// Toggle between login and register forms
const toggleForm = () => {
  signIn.value = !signIn.value;
};

// Computed validations for login and register
const loginValid = computed(
  () => username.value.length > 0 && password.value.length > 0,
);
const registerValid = computed(
  () => username.value.length > 0 && password.value.length > 0,
);
</script>

<!-- Use preprocessors via the lang attribute! e.g. <style lang="scss"> -->
<style lang="scss" scoped>
#app {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5; /* 배경색은 필요에 따라 변경 */
}

@mixin box {
  box-shadow: 1px 1px 2px 1px #ccc;
}

@mixin field {
  border: 1px solid #aaa;
  height: 40px;
  padding: 10px;
  margin-top: 20px;
  border-radius: 5px;
  box-sizing: border-box;
}

input[type='text'] {
  @include field;
}

input[type='email'] {
  @include field;
}

input[type='password'] {
  @include field;
}

.invalid {
  border: 2px solid red !important;
  &::placeholder {
    color: red;
  }
}

.errorMessage {
  color: red;
  margin: 10px;
  top: 5px;
}

.w100 {
  width: 100%;
}

.logo {
  width: 300px;
  margin-bottom: 10px;
}

.action {
  height: 40px;
  text-transform: uppercase;
  border-radius: 25px;
  width: 100%;
  border: none;
  cursor: pointer;
  background: green;
  margin-top: 20px;
  color: #fff;
  font-size: 1.2rem;
  @include box;
}

.action-disabled {
  color: #eee;
  background: #aaa;
  cursor: auto;
}

.top {
  display: flex;
  align-items: center;
  flex-direction: column;
  margin-bottom: 10px;
}

.title {
  width: 100%;
  font-size: 1.8rem;
  margin-bottom: 10px;
  text-align: center;
}

.subtitle {
  .subtitle-action {
    color: green;
    font-weight: bold;
    cursor: pointer;
  }
}

html {
  background-repeat: no-repeat;
  background: linear-gradient(
    to bottom,
    rgba(96, 108, 136, 1) 0%,
    rgba(63, 76, 107, 1) 100%
  );
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  font-family: sans-serif;
}

.loginBox {
  background: #fff;
  border-radius: 15px;
  max-width: 400px;
  padding: 25px 55px;
  animation: slideInTop 1s;
}

@keyframes slideInTop {
  from {
    opacity: 0;
    transform: translateY(-30%);
  }

  to {
    opacity: 100;
    transform: translateY(0%);
  }
}

@media screen and (min-width: 440px) {
  .loginBox {
    @include box;
  }
}

@media screen and (max-width: 440px) {
  html {
    background: #fff;
    align-items: start;
    justify-content: start;
  }

  .loginBox {
    padding: 25px 25px;
    max-width: 100vw;
  }
}
</style>
