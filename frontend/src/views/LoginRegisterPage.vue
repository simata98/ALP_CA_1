<template>
  <div id="app">
    <div class="loginBox">
      <div class="inner">
        <div class="signIn" v-if="signIn">
          <div class="top">
            <img
              class="logo"
              src="data:image/png;base64,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"
            />
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
            <img
              class="logo"
              src="https://res.cloudinary.com/dc3c8nrut/image/upload/v1685298768/logo-placeholder_l3yodl.png"
            />
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

<script>
import axios from 'axios';

axios.defaults.withCredentials = true;

export default {
  data() {
    return {
      username: '',
      password: '',
      signIn: true, // 로그인/회원가입 폼을 전환하기 위한 플래그
    };
  },
  methods: {
    // 로그인 요청
    async login() {
      try {
        const response = await axios.post('/auth/login', {
          username: this.username,
          password: this.password,
        });
        const token = response.data.token;
        localStorage.setItem('token', token); // 로컬 스토리지에 토큰 저장
        this.$router.push('/index'); // 로그인 후 페이지 이동
      } catch (error) {
        alert(error.response.data);
      }
    },

    // 회원가입 요청
    async register() {
      try {
        await axios.post('/auth/register', {
          username: this.username,
          password: this.password,
        });
        alert('Registration successful');
        this.toggleForm(); // 회원가입 후 로그인 폼으로 전환
      } catch (error) {
        alert(error.response.data);
      }
    },

    // 로그인/회원가입 폼을 전환
    toggleForm() {
      this.signIn = !this.signIn;
    },
  },

  computed: {
    // 유효성 검사: 로그인
    loginValid() {
      return this.username.length > 0 && this.password.length > 0;
    },

    // 유효성 검사: 회원가입
    registerValid() {
      return this.username.length > 0 && this.password.length > 0;
    },
  },
};
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
