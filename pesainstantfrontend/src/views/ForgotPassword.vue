<script setup>
import { useToast } from "vue-toast-notification";
import { useAuthStore } from '@/stores/auth'
import { ref } from "vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
const authStore = useAuthStore()
const loading = ref(false)
const phoneNumber = ref("")
const $toast = useToast()
const emit = defineEmits(["forgotpassword"]);

const handleSubmit = async () => {
    try {
        loading.value = true;

        await authStore.forgotPassword({
            phoneNumber: phoneNumber.value,
        });
        $toast.success("sucessfull!", {
            duration: 3000,
            position: "top",
        });
        emit("forgotpassword")
    }
    finally {
        loading.value = false;
    }
};

</script>
<template>
    <div>
        <LoadingSpinner :show="loading" />
        <form @submit.prevent="handleSubmit" class="space-y-5">
            <div>
                <label for="phoneNumber" class="block text-emerald-200 font-medium mb-1">Phone Number</label>
                <input id="phoneNumber" type="tel" v-model="phoneNumber" placeholder="e.g. +254712345678"
                    class="w-full px-4 py-2.5 bg-zinc-900 border border-zinc-700 rounded-xl text-white placeholder-gray-400 focus:ring-blue-600"
                    required />
            </div>
            <button type="submit"
                class="w-full py-3 mt-2 bg-gradient-to-r from-emerald-400 to-green-500 hover:from-emerald-500 hover:to-green-600 text-white font-semibold rounded-xl transition duration-300 shadow-md">
                submit
            </button>
        </form>
    </div>

</template>