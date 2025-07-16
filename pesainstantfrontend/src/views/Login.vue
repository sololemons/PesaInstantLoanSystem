<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useToast } from "vue-toast-notification";
import LoadingSpinner from "@/components/LoadingSpinner.vue";

const phoneNumber = ref("");
const password = ref("");

const $toast = useToast();

const authStore = useAuthStore();
const router = useRouter();

const loading = ref(false);

const handleLogin = async () => {
    try {
        loading.value = true;

        await authStore.login({
            phoneNumber: phoneNumber.value,
            password: password.value,
        });
        $toast.success("Login successful, welcome back!", {
            duration: 3000,
            position: "top",
        });
        router.push("/profile");
    }
    finally {
        loading.value = false;
    }
};
</script>
<template>
    <div>
        <LoadingSpinner :show="loading" />


        <form @submit.prevent="handleLogin" class="space-y-5">
            <p v-if="authStore.errorMessage" class="text-red-500 text-sm text-center">
                {{ authStore.errorMessage }}
            </p>

            <div>
                <label for="phoneNumber" class="block text-emerald-200 font-medium mb-1">Phone Number</label>
                <input id="phoneNumber" type="tel" v-model="phoneNumber" placeholder="e.g. +254712345678"
                    class="w-full px-4 py-2.5 bg-zinc-900 border border-zinc-700 rounded-xl text-white placeholder-gray-400 focus:ring-blue-600"
                    required />
            </div>

            <div>
                <label for="password" class="block text-emerald-200 font-medium mb-1">Password</label>
                <input id="password" type="password" v-model="password" placeholder="Enter your password"
                    class="w-full px-4 py-2.5 bg-zinc-900 border border-zinc-700 rounded-xl text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-emerald-400"
                    required />
            </div>

            <button type="submit"
                class="w-full py-3 mt-2 bg-gradient-to-r from-emerald-400 to-green-500 hover:from-emerald-500 hover:to-green-600 text-white font-semibold rounded-xl transition duration-300 shadow-md">
                Login
            </button>
        </form>
    </div>
</template>