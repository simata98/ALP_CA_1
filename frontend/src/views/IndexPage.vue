<script setup lang="ts">
import { ref } from 'vue';
import { CCol, CContainer, CForm, CFormInput, CButton, CRow } from '@coreui/vue';
import axios from 'axios';
axios.defaults.withCredentials = true;

// State 관리
const dbName = ref('');
const chatInput = ref('');
const messages = ref([]); // 채팅 메시지 저장
const isLoading = ref(false);

// DB 저장 API 호출
const saveDbInfo = async () => {
  try {
    const userInfo = { userId: 1 }; // 사용자 정보 (임의로 설정)
    const data = {
      dbName: dbName.value,
      userInfo,
    };
    await axios.post('/api/save-db', data); // Springboot에 해당 API 구현
    alert('DB 정보 저장 완료!');
  } catch (error) {
    alert('DB 정보 저장 실패: ' + error.message);
  }
};

// 채팅 API 호출
const sendMessage = async () => {
  if (chatInput.value.trim() === '') return;

  // 사용자가 입력한 메시지 추가
  messages.value.push({ type: 'user', text: chatInput.value });
  isLoading.value = true;

  try {
    const response = await axios.post('/api/executeQuery', {
      dbName: dbName.value,
      inputQuery: chatInput.value,
    }); // Springboot API 호출
    messages.value.push({ type: 'bot', text: response.data });
  } catch (error) {
    messages.value.push({
      type: 'bot',
      text: '응답을 가져오는 중 오류가 발생했습니다.',
    });
  } finally {
    isLoading.value = false;
  }

  // 입력창 비우기
  chatInput.value = '';
};
</script>

<template>
  <CContainer class="m-0 p-0">
    <CRow>
      <!-- 왼쪽 영역: DB 설정 및 저장 -->
      <CCol xs="4" class="border-right db-setting-area">
        <CRow> 채팅 이력 및 세팅 </CRow>
        <br />
        <CRow> DB 테이블 선택 </CRow>
        <CRow>
          <CForm>
            <CFormInput
              v-model="dbName"
              type="text"
              id="DBSelectInput"
              label="DB 이름 입력"
              placeholder="DB 이름"
              text="PostgresSQL 내 DB만 연결 가능"
              aria-describedby="DBInputFormControlHelpInline"
            />
            <CButton @click="saveDbInfo" class="mt-2">저장</CButton>
          </CForm>
        </CRow>
      </CCol>

      <!-- 오른쪽 영역: 채팅 -->
      <CCol class="chatting-area">
        <div class="chat-window">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="message.type"
          >
            <div class="chat-bubble">{{ message.text }}</div>
          </div>
          <div v-if="isLoading" class="bot">
            <div class="chat-bubble">응답을 기다리는 중...</div>
          </div>
        </div>

        <!-- 입력 영역 -->
        <div class="input-area">
          <CFormInput v-model="chatInput" placeholder="질문을 입력하세요..." />
          <CButton @click="sendMessage" class="mt-2">전송</CButton>
        </div>
      </CCol>
    </CRow>
  </CContainer>
</template>

<style scoped>
.border-right {
  border-right: 1px solid;
  padding-right: 15px;
}

.db-setting-area {
  background-color: #638091;
  padding-left: 15px;
}

.chatting-area {
  background-color: #b4c6d1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.chat-window {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  max-height: 500px;
}

.user {
  text-align: right;
}

.bot {
  text-align: left;
}

.chat-bubble {
  display: inline-block;
  padding: 10px;
  margin: 5px;
  border-radius: 10px;
  background-color: #fff;
  max-width: 70%;
}

.user .chat-bubble {
  background-color: #daf8e3;
}

.bot .chat-bubble {
  background-color: #f1f0f0;
}

.input-area {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background-color: #e0e0e0;
}
</style>
