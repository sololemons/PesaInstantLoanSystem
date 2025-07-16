import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

export const useApplicationStore = defineStore('application', () => {
  const applications = ref([])

  const getApplicationsForm = async () => {
    try {
      const token = localStorage.getItem('token')

      const response = await axios.get('/application/get/loan', {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      })
      console.log('fetching application', response.data)

      applications.value = response.data
    } catch (err) {
      console.error('Failed to get the application forms', err)
    }
  }

  return { applications, getApplicationsForm }
})
