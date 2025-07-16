<script setup>
import { useToast } from "vue-toast-notification";
import { useAuthStore } from '@/stores/auth'
import { ref } from "vue";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
const authStore = useAuthStore()
const loading = ref(false)
const phoneNumber = ref("")
const otp = ref("")
const newPassword = ref("")
const confirmNewPassword = ref("")
const $toast = useToast()
const emit = defineEmits(["passwordreset"]);

const handleSubmit = async () => {
    try {
        loading.value = true;

        await authStore.resetPassword({
            phoneNumber: phoneNumber.value,
            otp: otp.value,
            newPassword: newPassword.value,
            confirmNewPassword: confirmNewPassword.value
        });
        $toast.success(" reset password sucess!", {
            duration: 3000,
            position: "top",
        });
        emit("passwordreset")
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
            <div>
                <label for="otp" class="block text-emerald-200 font-medium mb-1">Otp</label>
                <input id="otp" v-model="otp" placeholder="e.g. 12345"
                    class="w-full px-4 py-2.5 bg-zinc-900 border border-zinc-700 rounded-xl text-white placeholder-gray-400 focus:ring-blue-600"
                    required />
            </div>
            <div>
                <label for="newPassword" class="block text-emerald-200 font-medium mb-1">NewPassword </label>
                <input id="newPassword" v-model="newPassword" placeholder="e.g. Password"
                    class="w-full px-4 py-2.5 bg-zinc-900 border border-zinc-700 rounded-xl text-white placeholder-gray-400 focus:ring-blue-600"
                    required />
            </div>
            <div>
                <label for="confirmNewPassword"
                    class="block text-emerald-200 font-medium mb-1">ConfirmNewPassword</label>
                <input id="confirmNewPassword" v-model="confirmNewPassword" placeholder="e.g. confirmpassword"
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