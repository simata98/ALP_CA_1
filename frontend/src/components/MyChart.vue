<script>
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js';

import { Bar } from 'vue-chartjs';
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
);

import axios from 'axios';

export default {
  name: 'App',
  components: {
    Bar,
  },
  data() {
    console.log('데이터 세팅');
    return {
      loading: false,
      category: {
        labels: [],
        datasets: [
          {
            label: '차트',
            backgroundColor: '#f83823',
            data: [],
          },
        ],
      },
      options: {
        maintainAspectRatio: false,
        responsive: true,
      },
    };
  },
  mounted() {
    console.log('컴포넌트 mounted call');
    // 스프링부트에 요청해서 대이터 획득\
    try {
      setTimeout(async () => {
        const { data } = await axios.get('/api/sales');
        console.log(data);
        this.category = {
          labels: data.map(sale => sale.category),
          datasets: [
            {
              label: '카테고리별 매출',
              backgroundColor: '#f83823',
              data: data.map(sale => sale.cnt),
            },
          ],
        };
      }, 1000);
    } catch (error) {
      console.log(error);
    }
  },
};
</script>
<template>
  <div style="width: 100%; height: 200px">
    <Bar :data="category" :options="options" />
    <div v-if="loading">Loading...</div>
  </div>
</template>
<style></style>
