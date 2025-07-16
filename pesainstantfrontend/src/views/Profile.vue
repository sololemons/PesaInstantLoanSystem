<script setup>
import Logo from '@/components/Logo.vue';
import useAuthStore from '@/stores/auth';
import { useApplicationStore } from '@/stores/getApplications';
import { UserCircleIcon } from '@heroicons/vue/24/solid';
import axios from 'axios';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ArrowUpRightIcon } from '@heroicons/vue/24/solid';
import { storeToRefs } from 'pinia';

const show = ref(false)
const router = useRouter()
const authStore = useAuthStore()
const user = ref({
    phoneNumber: '',
    userSignUpDate: '',
    userStatus: '',
    profilePathImage: ''
});
const applicationsStore = useApplicationStore()
const { getApplicationsForm } = applicationsStore
const { applications } = storeToRefs(applicationsStore)
function handleApply() {
    router.push({ name: 'apply' })
}
function handleDisbursment(applicationId) {
    router.push({
        name: 'disbursment',
        query: {
            applicationId: applicationId
        }
    });

}
function routeToManagement(phoneNumber) {
    router.push({
        name: 'management',
        query: {
            phoneNumber: phoneNumber
        }
    });
}

const getUser = async () => {
    try {
        const token = localStorage.getItem("token");

        const response = await axios.get('/users/get/user', {
            headers: {
                Authorization: `Bearer ${token}`
            },
            withCredentials: true
        });

        user.value = response.data;
    } catch (err) {
        console.error("Failed to get user", err);
    }
};

function handleLogout() {
    authStore.logout()
    router.push("/")

}
const fileInput = ref(null);

const triggerFileInput = () => {
    fileInput.value.click();
};

const onFileChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const res = await axios.post('/users/upload/profile/picture', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        });
    } catch (err) {
        console.error('Failed to upload profile picture:', err);
    }
};
const profileImageUrl = computed(() => {
    return user.value.profilePathImage
        ? encodeURI(`http://localhost:8080/${user.value.profilePathImage}`)
        : '/default-avatar.png';
});



onMounted(async () => {
    await getUser()
    await getApplicationsForm()
})




</script>


<template>
    <div class=" bg-gray-600  min-h-screen overflow-hidden p-6 relative">
        <div
            class="fixed top-0 left-0 right-0 z-50 bg-gray-600 rounded-sm shadow-sm p-6 flex justify-between items-center">
            <Logo class="w-20 h-8 mb-8" />

            <div class="flex items-center space-x-6">
                <div @click="handleApply"
                    class="p-2 rounded-full bg-green-500 hover:bg-green-500/35 hover:cursor-pointer">
                    <button class="text-white font-semibold">Apply</button>
                </div>

                <div class="relative">
                    <div @click="show = !show" class="cursor-pointer">
                        <UserCircleIcon class="w-12 h-12 text-gray-200" />
                    </div>
                    <div v-if="show" class="absolute mt-2 right-0 bg-gray-300 rounded-md shadow-lg w-40 z-50">
                        <button class="block rounded-md w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100">
                            View Profile
                        </button>
                        <button @click="handleLogout"
                            class="block rounded-md w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100">
                            Logout
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="bg-transparent space-y-4 rounded-4xl shadow-sm mt-24 w-full flex flex-col">

            <div class="flex flex-row space-x-5 p-4">

                <div class="rounded-3xl bg-transparent p-6 w-1/2 text-lg shadow-sm font-semibold">
                    <div class="text-center">
                        <h1 class="text-green-500 font-extrabold text-2xl">Loanee Information</h1>
                    </div>
                    <div class="space-y-8 text-lg text-gray-800">
                        <div class="flex justify-center mb-4">
                            <button @click="triggerFileInput"
                                class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                                Change Profile Picture
                            </button>
                            <input type="file" ref="fileInput" @change="onFileChange" accept="image/*" class="hidden" />
                        </div>


                        <div>
                            <img :src="profileImageUrl"
                                class="w-32 h-32 rounded-full shadow-md border border-gray-300" />
                        </div>


                        <div class="flex gap-x-4">
                            <p class="font-semibold text-black">Username:</p>
                            <span class="bg-blue-50 rounded-full px-4 py-1 shadow text-sm">
                                {{ user.phoneNumber }}
                            </span>
                        </div>

                        <div class="flex gap-x-4">
                            <p class="font-semibold text-black">Joined Date:</p>
                            <span class="bg-blue-50 rounded-full px-4 py-1 shadow text-sm text-gray-800">
                                {{ user.userSignUpDate }}
                            </span>
                        </div>



                        <div class="flex gap-x-4">
                            <p class="font-semibold text-black">No of Active Loans:</p>
                            <span class="bg-blue-100 rounded-full px-4 py-1 shadow text-sm text-gray-600">
                                {{ user.activeLoans || 0 }}
                            </span>
                        </div>

                        <div class="flex gap-x-4">
                            <p class="font-semibold text-black">Status:</p>
                            <span :class="[
                                'rounded-full px-4 py-1 shadow text-sm font-semibold capitalize',
                                user.userStatus === 'active'
                                    ? 'bg-green-100 text-green-700'
                                    : 'bg-gray-200 text-gray-600'
                            ]">
                                {{ user.userStatus }}
                            </span>
                        </div>
                    </div>



                </div>
                <div class="rounded-2xl bg-transparent shadow-md p-6 text-lg font-semibold text-center w-1/2">
                    <h1 class="text-2xl font-extrabold text-gray-800 mb-6">Loan Applications History</h1>

                    <div v-if="applications.length" class="space-y-4">

                        <div v-for="(application, index) in applications" :key="index"
                            class="bg-white/70 border border-gray-300 rounded-2xl p-4 shadow-md text-left">
                            <p class="text-sm text-gray-600 mb-1">
                                <span class="font-medium">Applied On:</span>
                                <span class="text-gray-900">{{ application.applicationDate }}</span>
                            </p>

                            <div class="flex justify-between items-center text-sm text-gray-600">
                                <div>
                                    <span class="font-medium">Progress:</span>
                                    <span class="ml-2 px-3 py-1 rounded-full text-white text-xs font-semibold" :class="{
                                        'bg-blue-500': application.progress === 'PENDING',
                                        'bg-green-500': application.progress === 'APPROVED',
                                        'bg-red-500': application.progress === 'REJECTED',
                                        'bg-green-600': application.progress === 'ACTIVE'
                                    }">
                                        {{ application.progress }}
                                    </span>
                                </div>
                                <button v-if="application.progress === 'APPROVED'"
                                    @click="handleDisbursment(application.id)" title="Disburse Funds"
                                    class="text-green-600 hover:text-green-800 transition">
                                    <ArrowUpRightIcon class="w-5 h-5" />
                                </button>
                                <button v-if="application.progress === 'ACTIVE'"
                                    @click="routeToManagement(application.phoneNumber)" title="To Loan Management"
                                    class="text-green-600 hover:text-green-800 transition bg-amber-50 w-fit">
                                    <ArrowUpRightIcon class="w-5 h-5" />
                                </button>

                            </div>
                        </div>
                    </div>

                    <div v-else class="text-white italic mt-4">No loan applications found.</div>
                </div>


            </div>


        </div>


    </div>

</template>
