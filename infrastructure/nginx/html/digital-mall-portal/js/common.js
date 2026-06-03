// Digital Mall — 公共 JS
// Vue 3 + axios 全局配置

// =============================================
// Axios 配置
// =============================================
axios.defaults.baseURL = "/api";
axios.defaults.timeout = 10000;

// 请求拦截器：自动携带 JWT token
axios.interceptors.request.use(config => {
  const token = sessionStorage.getItem("token");
  if (token) config.headers['authorization'] = token;
  return config;
});

// 响应拦截器：401 跳转登录
axios.interceptors.response.use(
  resp => resp.data,
  err => {
    if (err.response && err.response.status === 401) {
      sessionStorage.removeItem("user-info");
      sessionStorage.removeItem("token");
      location.href = "/login.html";
    }
    return Promise.reject(err);
  }
);

// =============================================
// 工具函数
// =============================================
const util = {
  isLogin() {
    return !!sessionStorage.getItem("user-info");
  },
  getUser() {
    try { return JSON.parse(sessionStorage.getItem("user-info")); } catch(e) { return null; }
  },
  logout() {
    sessionStorage.removeItem("user-info");
    sessionStorage.removeItem("token");
    location.href = "/";
  },
  getUrlParam(name) {
    const params = new URLSearchParams(window.location.search);
    return params.get(name);
  },
  // 价格格式化：分 → 元
  formatPrice(price) {
    if (price == null) return '0.00';
    return (Number(price) / 100).toFixed(2);
  },
  // 数字缩略
  formatCount(n) {
    if (n >= 10000) return Math.floor(n / 1000) / 10 + '万';
    return String(n);
  },
  // sessionStorage 快捷操作
  store: {
    set(key, obj) { sessionStorage.setItem(key, JSON.stringify(obj)); },
    get(key) { try { return JSON.parse(sessionStorage.getItem(key)); } catch(e) { return null; } },
    del(key) { sessionStorage.removeItem(key); }
  }
};
