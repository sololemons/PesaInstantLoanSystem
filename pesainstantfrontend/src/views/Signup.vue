<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useToast } from "vue-toast-notification";
import LoadingSpinner from "@/components/LoadingSpinner.vue";

const phoneNumber = ref("");
const emit = defineEmits(["signedup"]);
const password = ref("");
const confirmPassword = ref("");
const authStore = useAuthStore();
const $toast = useToast();
const loading = ref(false);

const handleSubmit = async () => {
    try {
        loading.value = true;
        await authStore.register({
            phoneNumber: phoneNumber.value,
            password: password.value,
            confirmPassword: confirmPassword.value,
        });
        $toast.success("Registration successful, please login!", {
            duration: 3000,
            position: "top",
        });
        emit("signedup")
    }
    catch {
        console.error("error")
    }

    finally {
        loading.value = false;
    }
};
</script>
<template>
    <div>

        <LoadingSpinner :show="loading" />

        <h2 class="text-3xl font-bold text-green-400 text-center mb-6">Create Account</h2>

        <form @submit.prevent="handleSubmit" class="space-y-5">
            <p v-if="authStore.errorMessage" class="text-red-500 text-sm text-center">
                {{ authStore.errorMessage }}
            </p>

            <div>
                <label class="block text-green-300 font-medium mb-1"> Phone Number</label>
                <input type="tel" v-model="phoneNumber" placeholder="e.g. +254712345678"
                    class="w-full px-4 py-2.5 bg-zinc-900 border border-zinc-700 rounded-xl text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-400"
                    required />
            </div>

            <div>
                <label class="block text-green-300 font-medium mb-1"> Password</label>
                <input type="password" v-model="password" placeholder="Enter password"
                    class="w-full px-4 py-2.5 bg-zinc-900 border border-zinc-700 rounded-xl text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-400"
                    required />
            </div>

            <div>
                <label class="block text-green-300 font-medium mb-1"> Confirm Password</label>
                <input type="password" v-model="confirmPassword" placeholder="Repeat password"
                    class="w-full px-4 py-2.5 bg-zinc-900 border border-zinc-700 rounded-xl text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-400"
                    required />
            </div>

            <button type="submit"
                class="w-full py-3 mt-2 bg-gradient-to-r from-green-400 to-emerald-500 hover:from-green-500 hover:to-emerald-600 text-white font-semibold rounded-xl transition duration-300 shadow-md">
                Register
            </button>
        </form>
    </div>
</template>
