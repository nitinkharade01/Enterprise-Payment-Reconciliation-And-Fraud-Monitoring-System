import axios from 'axios';

const TOKEN_KEY = 'payment_console_token';
const USER_KEY = 'payment_console_user';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || ''
});

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export function unwrap(response) {
  return response?.data?.data ?? response?.data;
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function getStoredUser() {
  const raw = localStorage.getItem(USER_KEY);
  return raw ? JSON.parse(raw) : null;
}

export function storeSession(payload, fallbackEmail) {
  const data = payload?.data ?? payload;
  const token = data?.token || data?.jwt || data?.accessToken;
  const user = {
    email: data?.email || fallbackEmail,
    name: data?.name || data?.fullName || fallbackEmail,
    roles: data?.roles || data?.roleNames || ['ADMIN']
  };

  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  }
  localStorage.setItem(USER_KEY, JSON.stringify(user));
  return user;
}

export function clearSession() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
}
