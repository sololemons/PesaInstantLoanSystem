<script setup>
import { ref, computed } from "vue";
import Login from "./Login.vue";
import Signup from "./Signup.vue";
import ForgotPassword from "./ForgotPassword.vue";
import VerifyOtp from "./VerifyOtp.vue";
import ResetPassword from "./ResetPassword.vue";
const activeTab = ref("login");

const components = {
    login: Login,
    signup: Signup,
    forgotpassword: ForgotPassword,
    verifyotp: VerifyOtp,
    resetpassword: ResetPassword
};

const activeComponent = computed(() => components[activeTab.value]);
function handleSignUp() {
    activeTab.value = "login"
}
function handleForgotPassword() {
    activeTab.value = "verifyotp"
}
function handleVerifyOtp() {
    activeTab.value = "resetpassword"
}
function handleResetPassword() {
    activeTab.value = "login"
}
</script>


<template>
    <div class="flex flex-row min-h-screen  bg-[#0b393f] text-sm p-4 space-x-64">
        <div class=" bg-[#103b45] rounded-4xl  w-1/3">
            <img src="/pesainstant.webp" alt="pesainstantimages" class="h-full rounded-4xl" />
        </div>
        <div class="mt-12 w-1/2 mr-24 flex flex-col space-y-4">
            <div class="flex space-x-6 text-2xl font-bold text-white mb-6">
                <span :class="{ 'text-emerald-400 underline': activeTab === 'login' }" class="cursor-pointer"
                    @click="activeTab = 'login'">Login</span>
                <span :class="{ 'text-emerald-400 underline': activeTab === 'signup' }" class="cursor-pointer"
                    @click="activeTab = 'signup'">Signup</span>
                <span :class="{ 'text-emerald-400 underline': activeTab === 'forgotpassword' }" class="cursor-pointer"
                    @click="activeTab = 'forgotpassword'">ForgotPassword</span>
            </div>

            <transition name="fade" mode="out-in">
                <component :is="activeComponent" @signedup="handleSignUp" @forgotpassword="handleForgotPassword"
                    @otpverified="handleVerifyOtp" @passwordreset="handleResetPassword" :key="activeTab" />
            </transition>

        </div>


    </div>

</template>
<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.4s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>