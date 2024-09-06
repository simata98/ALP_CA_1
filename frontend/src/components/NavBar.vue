<script setup>
import {
  CCollapse,
  CDropdown,
  CDropdownDivider,
  CDropdownItem,
  CDropdownMenu,
  CDropdownToggle,
  CNavbar,
  CNavbarBrand,
  CNavbarNav,
  CNavItem,
  CNavLink,
  CNavbarToggler,
  CContainer,
} from '@coreui/vue';

import { ref } from 'vue';
import { useAuthStore } from '../stores/auth';

// 반응형 변수 정의
const visible = ref(false);

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
</script>

<template>
  <CNavbar
    expand="lg"
    color-scheme="light"
    class="bg-info"
    placement="sticky-top"
  >
    <CContainer fluid>
      <CNavbarBrand href="#">KT DB Searcher</CNavbarBrand>
      <CNavbarToggler
        aria-label="Toggle navigation"
        aria-expanded="{visible}"
        @click="visible = !visible"
      />
      <CCollapse class="navbar-collapse" :visible="visible">
        <CNavbarNav>
          <CNavItem>
            <CNavLink href="#">Main</CNavLink>
          </CNavItem>
          <CDropdown variant="nav-item" :popper="false">
            <CDropdownToggle color="primary">드롭다운 버튼</CDropdownToggle>
            <CDropdownMenu>
              <CDropdownItem href="#">누가 만들었나</CDropdownItem>
              <CDropdownItem href="#">왜 만들었나</CDropdownItem>
              <CDropdownDivider />
              <CDropdownItem href="#">Something else here</CDropdownItem>
            </CDropdownMenu>
          </CDropdown>
        </CNavbarNav>
      </CCollapse>
    </CContainer>
    <CForm class="container-fluid justify-content-end">
      <!-- pinia의 token 상태에 따라 버튼 변경 -->
      <CButton
        v-if="authStore.token"
        type="button"
        color="danger"
        variant="ghost"
        @click="logout"
        class="btn"
      >
        로그아웃
      </CButton>
      <CButton
        v-else
        type="button"
        color="success"
        variant="ghost"
        @click="login"
        class="btn"
      >
        로그인
      </CButton>
    </CForm>
  </CNavbar>
</template>
