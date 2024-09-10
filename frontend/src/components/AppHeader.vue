<script setup>
import { onMounted, ref } from 'vue';
import { useColorModes } from '@coreui/vue';
import { useAuthStore } from '../stores/auth';

const headerClassNames = ref('mb-0 p-0');
const { colorMode, setColorMode } = useColorModes(
  'coreui-free-vue-admin-template-theme',
);

// Pinia 스토어 사용
const authStore = useAuthStore();

// 로그인/로그아웃 메소드
const logout = () => {
  authStore.clearToken(); // 로그아웃
  window.location.href = 'login'; // 로그인 페이지로 이동
};

const login = () => {
  window.location.href = 'login'; // 로그인 페이지로 이동
};

onMounted(() => {
  document.addEventListener('scroll', () => {
    if (document.documentElement.scrollTop > 0) {
      headerClassNames.value = 'mb-0 p-0 shadow-sm';
    } else {
      headerClassNames.value = 'mb-0 p-0';
    }
  });
});
</script>

<template>
  <CHeader position="sticky" :class="headerClassNames">
    <CContainer class="border-bottom px-4" fluid style="height: 60px">
      <CHeaderNav class="d-none d-md-flex">
        <CNavItem>
          <CNavLink href="/dashboard"> Dashboard </CNavLink>
        </CNavItem>
        <CNavItem>
          <CNavLink href="#">Users</CNavLink>
        </CNavItem>
      </CHeaderNav>
      <CHeaderNav>
        <li class="nav-item py-1">
          <div class="vr h-100 mx-2 text-body text-opacity-75"></div>
        </li>
        <CDropdown variant="nav-item" placement="bottom-end">
          <CDropdownToggle :caret="false">
            <CIcon v-if="colorMode === 'dark'" icon="cil-moon" size="lg" />
            <CIcon v-else-if="colorMode === 'light'" icon="cil-sun" size="lg" />
            <CIcon v-else icon="cil-contrast" size="lg" />
          </CDropdownToggle>
          <CDropdownMenu>
            <CDropdownItem
              :active="colorMode === 'light'"
              class="d-flex align-items-center"
              component="button"
              type="button"
              @click="setColorMode('light')"
            >
              <CIcon class="me-2" icon="cil-sun" size="lg" /> Light
            </CDropdownItem>
            <CDropdownItem
              :active="colorMode === 'dark'"
              class="d-flex align-items-center"
              component="button"
              type="button"
              @click="setColorMode('dark')"
            >
              <CIcon class="me-2" icon="cil-moon" size="lg" /> Dark
            </CDropdownItem>
            <CDropdownItem
              :active="colorMode === 'auto'"
              class="d-flex align-items-center"
              component="button"
              type="button"
              @click="setColorMode('auto')"
            >
              <CIcon class="me-2" icon="cil-contrast" size="lg" /> Auto
            </CDropdownItem>
          </CDropdownMenu>
        </CDropdown>
        <CForm class="container-fluid justify-content-end">
          <!-- pinia의 token 상태에 따라 버튼 변경 -->
          <CButton
            v-if="authStore.token"
            type="button"
            color="primary"
            @click="logout"
            class="btn"
          >
            로그아웃
          </CButton>
          <CButton
            v-else
            type="button"
            color="primary"
            @click="login"
            class="btn"
          >
            로그인
          </CButton>
        </CForm>
        <li class="nav-item py-1">
          <div class="vr h-100 mx-2 text-body text-opacity-75"></div>
        </li>
      </CHeaderNav>
    </CContainer>
  </CHeader>
</template>
