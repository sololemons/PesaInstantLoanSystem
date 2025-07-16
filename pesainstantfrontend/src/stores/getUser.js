import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

export const useUserStore = defineStore('user', () => {
  const user = ref({
    phoneNumber: '',
    userSignUpDate: '',
    userStatus: '',
    profilePathImage: '',
    activeLoans: 0,
  })

  const getUser = async () => {
    try {
      const token = localStorage.getItem('token')

      const response = await axios.get('/users/get/user', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      })

      user.value = response.data
    } catch (err) {
      console.error('Failed to get user', err)
    }
  }

  const profileImageUrl = computed(() => {
    return user.value.profilePathImage
      ? encodeURI(`http://localhost:8080/${user.value.profilePathImage}`)
      : '/default-avatar.png'
  })

  return { user, getUser, profileImageUrl }
})
