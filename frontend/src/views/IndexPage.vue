<script setup lang="ts">
import { CButton, CCol, CForm, CFormInput } from '@coreui/vue';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { onMounted, ref } from 'vue';
axios.defaults.withCredentials = true;

// State 관리
const dbName = ref('');
const urls = ref([]); // URL 목록을 저장할 ref
const chatInput = ref('');
const messages = ref([]); // 채팅 메시지 저장
const isLoading = ref(false);

// Toast states
const toastVisible = ref(false);
const toastMessage = ref('');
const toastColor = ref('');

// 응답 파싱 함수
const parseResponse = data => {
  const parsedMessages = [];

  // 개행 문자를 <br>로 치환하는 함수
  const replaceNewlinesWithBr = text => {
    // 먼저 이스케이프된 \n을 실제 개행 문자로 변환
    const unescapedText = text.replace(/\\n/g, '\n');
    // 그 후 개행 문자를 <br>로 변환
    return unescapedText.replace(/\n/g, '<br>');
  };

  // 백틱으로 감싸진 부분을 <pre><code>로 치환하는 함수
  const replaceBackticksWithCodeBlock = text => {
    return text
      .replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>') // 여러 줄 코드 처리
      .replace(/`(.*?)`/g, '<code>$1</code>'); // 한 줄 코드 처리
  };

  // 최종 텍스트 처리 함수: 개행 문자와 백틱 처리를 결합
  const formatText = text => {
    let formattedText = replaceBackticksWithCodeBlock(text); // 백틱 먼저 처리
    formattedText = replaceNewlinesWithBr(formattedText); // 그 후 개행 처리
    return formattedText;
  };

  // Human Message 추출
  const humanMessageRegex = /Human Message\s*={32}\s*([\s\S]*?)\s*={32}/;
  const humanMessage = humanMessageRegex.exec(data);
  if (humanMessage) {
    parsedMessages.push({
      type: 'human',
      text: formatText(humanMessage[1].trim()),
    });
  }

  // Ai Message 추출 (최종 응답 포함 여부 판단)
  const aiMessageRegex = /Ai Message\s*={32}\s*([\s\S]*?)(?=\s*={32}|$)/g;
  const aiMessages = [...data.matchAll(aiMessageRegex)];
  aiMessages.forEach(aiMatch => {
    parsedMessages.push({ type: 'ai', text: formatText(aiMatch[1].trim()) });
  });

  // Tool Message 추출
  const toolMessageRegex =
    /Tool Message\s*={32}\s*Name:.*?\n([\s\S]*?)\s*={32}/g;
  const toolMessages = [...data.matchAll(toolMessageRegex)];
  toolMessages.forEach(toolMatch => {
    parsedMessages.push({
      type: 'tool',
      text: formatText(toolMatch[1].trim()),
    });
  });

  // 최종 결과 추출 (Ai Message 중 마지막 부분에 존재)
  const finalResultRegex = /Ai Message\s*={32}[\s\S]*?\n([\s\S]+)$/;
  const finalResult = finalResultRegex.exec(data);
  if (finalResult) {
    parsedMessages.push({
      type: 'finalresult',
      text: formatText(finalResult[1].trim()),
    });
  }

  return parsedMessages;
};

// DB 저장 API 호출
const saveDbInfo = async () => {
  try {
    const token = localStorage.getItem('token');
    const decoded = jwtDecode(token);
    const data = {
      dbName: dbName.value,
      userName: decoded.sub,
    };
    await axios.post('/api/save-db', data); // Springboot에 해당 API 구현
    toastMessage.value = 'DB 정보 저장 완료!';
    toastColor.value = 'success';
    toastVisible.value = true;
  } catch (error) {
    toastMessage.value = 'DB 정보 저장 실패: ' + error.message;
    toastColor.value = 'danger';
    toastVisible.value = true;
  }
};

// 채팅 API 호출
const sendMessage = async () => {
  console.log(chatInput.value.trim());
  if (chatInput.value.trim() === '') {
    toastMessage.value = 'DB명을 입력하세요.';
    toastColor.value = 'danger';
    toastVisible.value = true;
    return;
  }

  // 사용자가 입력한 메시지 추가
  messages.value.push({ type: 'human', text: chatInput.value });
  isLoading.value = true;

  try {
    const response = await axios.post('/api/executeQuery', {
      dbName: dbName.value,
      inputQuery: chatInput.value,
    }); // Springboot API 호출
    console.log(response.data.result);
    const parsedMessages = parseResponse(response.data.result);

    parsedMessages.forEach(msg => {
      if (msg.type !== 'human') {
        messages.value.push(msg);
      }
    });
  } catch (error) {
    messages.value.push({
      type: 'error',
      text: '응답을 가져오는 중 오류가 발생했습니다.',
    });
  } finally {
    isLoading.value = false;
  }

  // 입력창 비우기
  chatInput.value = '';
};

// API를 통해 사용자와 연관된 URL 목록 불러오기
const fetchUrls = async () => {
  try {
    const token = localStorage.getItem('token');
    const decoded = jwtDecode(token); // 토큰에서 사용자 정보 가져오기
    const response = await axios.get(`/api/get-urls/${decoded.sub}`);
    urls.value = response.data; // URL 리스트를 urls에 저장
  } catch (error) {
    toastMessage.value = 'URL 정보를 불러오는 중 오류가 발생했습니다.';
    toastColor.value = 'danger';
    toastVisible.value = true;
  }
};

// 컴포넌트가 마운트되면 URL 목록을 불러옴
onMounted(() => {
  fetchUrls();
});
</script>

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
  <CRow class="h-svh">
    <!-- 왼쪽 영역: DB 설정 및 저장 -->
    <CCol xs="3" class="border-right db-setting-area">
      <CRow class="m-2">
        <label for="DBSelect">DB 테이블 선택</label>
        <select class="form-select" id="DBSelect" v-model="dbName">
          <option value="" disabled selected>테이블을 선택하세요</option>
          <option v-for="(url, index) in urls" :key="index" :value="url">
            {{ url }}
          </option>
        </select>
      </CRow>
      <CRow>
        <CForm>
          <CFormInput
            v-model="dbName"
            type="text"
            id="DBSelectInput"
            label="DB 정보 저장"
            placeholder="DB 이름"
            text="PostgresSQL 내 DB만 연결 가능"
            aria-describedby="DBInputFormControlHelpInline"
          />
          <CButton @click="saveDbInfo" class="btn btn-warning mt-2"
            >저장</CButton
          >
        </CForm>
      </CRow>
    </CCol>

    <!-- 오른쪽 영역: 채팅 -->
    <CCol class="chatting-area m-0 p-0">
      <div class="chat-window">
        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="message.type"
        >
          <div class="chat-bubble" v-html="message.text"></div>
        </div>
        <div v-if="isLoading" class="ai">
          <div class="chat-bubble">응답을 기다리는 중...</div>
        </div>
      </div>
      <!-- 개인사업자 업황 데이터에서 20, 30대 여자가 가장 많이 방문한 상권은 어디인지 알려줘 -->
      <!-- 입력 영역 -->
      <div class="input-area">
        <CFormInput v-model="chatInput" placeholder="질문을 입력하세요..." />
        <CButton @click="sendMessage" class="mt-2">
          <img
            src="@/assets/send.png"
            alt="Send"
            class="mr-3"
            style="width: 40px; height: 40px"
          />
        </CButton>
      </div>
    </CCol>
  </CRow>
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
  height: 100vh; /* 화면 전체 높이에 맞추기 */
}

.chat-window {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  max-height: calc(100vh - 120px); /* 화면에서 입력 영역을 제외한 공간에 맞춤 */
}

.human {
  text-align: right;
}

.ai {
  text-align: left;
}

.tool {
  text-align: left;
  font-style: italic;
}

.final {
  text-align: left;
  font-weight: bold;
  background-color: #daf8e3;
}

.chat-bubble {
  display: inline-block;
  padding: 10px;
  margin: 5px;
  border-radius: 10px;
  background-color: #fff;
  max-width: 70%;
  white-space: break-spaces; /* 개행 문자 처리 */
}

.human .chat-bubble {
  background-color: #daf8e3;
}

.ai .chat-bubble {
  background-color: #f1f0f0;
}

.tool .chat-bubble {
  background-color: #e8e8e8;
}

.final .chat-bubble {
  background-color: #ffeb3b;
}

.input-area {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background-color: #e0e0e0;
  position: sticky;
  bottom: 0;
}
</style>
