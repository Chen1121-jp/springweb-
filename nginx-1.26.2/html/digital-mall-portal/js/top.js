// Digital Mall — 顶部导航栏组件 (Vue 3)
const TopNav = {
  template: `
  <header class="header">
    <div class="header-inner">
      <div class="logo-area">
        <a href="/" style="display:flex;align-items:center;gap:12px;">
          <div class="logo-icon">D</div>
          <div class="logo-text"><span>数字</span>商城</div>
        </a>
      </div>
      <ul class="nav-links">
        <li><a href="/" :class="{active: page==='home'}">🏠 首页</a></li>
        <li><a href="/search.html" :class="{active: page==='search'}">🔍 搜索</a></li>
        <li><a href="/seckill.html" :class="{active: page==='seckill'}">⚡ 秒杀</a></li>
        <li>
          <a href="/cart.html" class="cart-icon" :class="{active: page==='cart'}">
            🛒 购物车
            <span class="badge" v-if="cartCount > 0">{{ cartCount }}</span>
          </a>
        </li>
        <li v-if="!user"><a href="/login.html" :class="{active: page==='login'}">👤 登录</a></li>
        <li v-else>
          <span class="username" style="display:inline-flex;align-items:center;gap:6px;">
            👤 {{ user.username }}
          </span>
          <a href="#" @click.prevent="util.logout()" class="btn btn-sm btn-ghost">退出</a>
        </li>
      </ul>
    </div>
  </header>`,
  props: {
    page: { type: String, default: 'home' }
  },
  data() {
    return {
      user: null,
      cartCount: 0,
      util
    };
  },
  mounted() {
    this.user = util.store.get("user-info");
    if (this.user) {
      axios.get("/carts")
        .then(r => this.cartCount = Array.isArray(r) ? r.length : 0)
        .catch(() => {});
    }
  },
  methods: {}
};
