import { defineStore } from 'pinia'
import axios from 'axios'
axios.defaults.baseURL = 'http://localhost:8080'
export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: !!localStorage.getItem('token'),
    token: localStorage.getItem('token') || '',
    user: {},
    status: '',
    errorMessage: localStorage.getItem('phoneNumber') || '',
    phoneNumber: '',
  }),

  actions: {
    setErrorMessage(message) {
      this.errorMessage = message
      setTimeout(() => {
        this.errorMessage = ''
      }, 3000)
    },

    async login(credentials) {
      this.status = 'loading'
      try {
        const response = await axios.post('/users/auth/authenticate', credentials)
        const token = response.data.token
        localStorage.setItem('token', token)
        localStorage.setItem('phoneNumber', phoneNumber)
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        this.token = token
        this.phoneNumber = this.phoneNumber
        this.user = response.data.user
        this.isAuthenticated = true
        this.status = 'success'
      } catch (error) {
        this.status = 'error'
        this.isAuthenticated = false
        localStorage.removeItem('token')
        localStorage.removeItem('phoneNumber')

        if (error.response && error.response.status === 401) {
          this.setErrorMessage('Invalid username or password')
        } else {
          this.setErrorMessage('Login failed')
        }
        throw error
      }
    },

    async register(user) {
      this.status = 'loading'
      try {
        const response = await axios.post('/users/auth/register', user)
        const token = response.data.token
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        this.token = token
        this.user = response.data.user
        this.isAuthenticated = false
        this.status = 'success'
      } catch (error) {
        this.status = 'error'
        this.setErrorMessage('Registering failed')

        console.error('Registration failed:', error)
        throw error
      }
    },
    async forgotPassword(payload) {
      try {
        const response = await axios.post('/users/forgot/password', payload)
        return response.data
      } catch (error) {
        this.setErrorMessage('reseting password failed failed')
        console.error('Error', error)
        throw error
      }
    },
    async verifyOtp(payload) {
      try {
        const response = await axios.post('/users/forgot/verify-otp', payload)
        return response.data
      } catch (error) {
        this.setErrorMessage('Error Verifying the Otp')
        console.error('Error', error)
        throw error
      }
    },
    async resetPassword(payload) {
      try {
        const response = await axios.post('/users/forgot/reset-password', payload)
        return response.data
      } catch (err) {
        this.errorMessage = err.response?.data?.message || 'Password reset failed.'
        throw err
      }
    },

    logout() {
      this.isAuthenticated = false
      this.token = ''
      this.user = {}
      this.status = ''
      this.errorMessage = ''
      localStorage.removeItem('token')
      delete axios.defaults.headers.common['Authorization']
    },
  },
})

export default useAuthStore
