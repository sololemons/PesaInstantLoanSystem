import './assets/styles.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import ToastPlugin from 'vue-toast-notification'
import 'vue-toast-notification/dist/theme-bootstrap.css'
const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
app.use(router)
app.use(ToastPlugin)
app.mount('#app')
