import axios from 'axios';

const API_BASE_URL = 'http://localhost:8083';

const api = {
  getWeeks: () => axios.get(`${API_BASE_URL}/api/v1/stock-data/weeks`),
  getStockData: (symbol, date) => axios.get(`${API_BASE_URL}/api/v1/stock-data/${symbol}/${date}`),
  getSymbols: () => axios.get(`${API_BASE_URL}/api/v1/stock-data/symbols`),
  createDatabase: () => axios.post(`${API_BASE_URL}/api/v1/database/db_create`),
  collectData: (data) => axios.post(`${API_BASE_URL}/api/v1/data-collection/collect_data`, data)
};

export default api;