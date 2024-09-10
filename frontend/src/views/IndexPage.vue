<script setup lang="ts">
import { CButton, CCol, CForm, CFormInput } from '@coreui/vue';
import axios from 'axios';
import { ref } from 'vue';
axios.defaults.withCredentials = true;

// State 관리
const dbName = ref('');
const chatInput = ref('');
const messages = ref([]); // 채팅 메시지 저장
const isLoading = ref(false);

// 응답 파싱 함수
const parseResponse = data => {
  const parsedMessages = [];

  // 개행 문자를 <br>로 치환하는 함수
  const replaceNewlinesWithBr = text => {
    return text.replace(/(\\r)*\\n/g, '<br>'); // 백슬래시를 추가한 개행 문자 처리
  };

  // 백틱으로 감싸진 부분을 <pre><code>로 치환하는 함수
  const replaceBackticksWithCodeBlock = text => {
    return text
      .replace(/```(.*?)```/g, '<pre><code>$1</code></pre>')
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
  messages.value.push({ type: 'human', text: chatInput.value });
  isLoading.value = true;

  try {
    const response = await axios.post('/api/executeQuery', {
      dbName: dbName.value,
      inputQuery: chatInput.value,
    }); // Springboot API 호출
    const parsedMessages = parseResponse(response.data.result);

    parsedMessages.forEach(msg => {
      messages.value.push(msg);
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
</script>

<template>
  <CRow class="h-svh">
    <!-- 왼쪽 영역: DB 설정 및 저장 -->
    <CCol xs="3" class="border-right db-setting-area">
      <CRow class="m-2"> 채팅 이력 및 세팅 </CRow>
      <br />
      <CRow class="m-2"> DB 테이블 선택 </CRow>
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

      <!-- 입력 영역 -->
      <div class="input-area">
        <CFormInput v-model="chatInput" placeholder="질문을 입력하세요..." />
        <CButton @click="sendMessage" class="mt-2">전송</CButton>
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
}

.chat-window {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  max-height: 1000px;
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
}
</style>
