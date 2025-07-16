<script setup>
import Logo from '@/components/Logo.vue';
import router from '@/router';
import { ArrowUpTrayIcon } from '@heroicons/vue/24/solid';
import axios from 'axios';
import { ref } from 'vue';
import { useToast } from 'vue-toast-notification';
const dateOfBirth = ref("")
const $toast = useToast()
const formatDate = () => {
    let value = dateOfBirth.value.replace(/\D/g, '');
    if (value.length > 2) value = value.slice(0, 2) + '/' + value.slice(2);
    if (value.length > 5) value = value.slice(0, 5) + '/' + value.slice(5);
    dateOfBirth.value = value.slice(0, 10);
};

const occupations = ref([
    { name: 'EMPLOYED', color: 'bg-blue-600' },
    { name: 'SELF_EMPLOYED', color: 'bg-green-600' },
    { name: 'UNEMPLOYED', color: 'bg-red-600' }
]);
const fullNames = ref("")
const idNumber = ref("")
const selectedOccupation = ref(occupations.value[2]);

const status = ref('default');

const changeWindow = (newStatus) => {
    status.value = newStatus;
}
const selectedFile = ref(null)

const handleFileUpload = (event) => {
    const file = event.target.files[0];
    selectedFile.value = file;


}
const submitApplication = async () => {
    const token = localStorage.getItem("token");
    const formData = new FormData();

    formData.append('fullName', fullNames.value);
    formData.append('idNumber', idNumber.value);
    formData.append('occupation', selectedOccupation.value.name);
    formData.append('dateOfBirth', dateOfBirth.value);
    formData.append('image', selectedFile.value);

    try {
        const response = await axios.post('/application/loan', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${token}`,
            },
        });
        $toast.success("Loan Application Sucesssfull", {
            position: 'top',
            duration: 3000
        })
        router.push({ name: 'profile' })
    } catch (error) {
        console.error('Failed to submit application', error);
        alert('Failed to submit application.');
    }
};

</script>

<template>
    <div class="min-h-screen flex items-start p-10 justify-center bg-[#0e4b65] relative">
        <div class="absolute top-4 left-4">
            <Logo class="w-20 h-8" />
        </div>

        <Transition name="fade" mode="out-in">
            <div class="justify-center-safe" :key="status">
                <div v-if="status === 'default'" class=" flex items-start justify-center bg-[#0e4b65] p-8">
                    <div class="mt-6 flex flex-col space-y-6">
                        <div>
                            <h1 class="text-green-200 text-lg font-extrabold">PERSONAL DETAILS</h1>
                        </div>
                        <div class="flex flex-row space-x-5">
                            <div class="w-full">
                                <label for="fullNames" class="block text-emerald-200 font-medium mb-2">
                                    Full Legal Names:
                                </label>
                                <input id="fullNames" type="text" v-model="fullNames" placeholder="e.g.John Doe...."
                                    class="w-full px-4 py-2 bg-zinc-900 border border-zinc-700 rounded-xl text-white 
                    placeholder-gray-400 focus:ring-green-500 focus:outline-none" required />
                            </div>
                            <div class="w-full">
                                <label for="idNumber" class="block text-emerald-200 font-medium mb-2">
                                    ID NUMBER:
                                </label>
                                <input id="idNumber" type="text" v-model="idNumber" placeholder="e.g.12345...." class="w-full px-4 py-2 bg-zinc-900 border border-zinc-700 rounded-xl text-white 
                    placeholder-gray-400 focus:ring-green-500" required />
                            </div>
                        </div>

                        <div class="w-full">
                            <p class="text-lg font-extrabold  text-emerald-200">Occupation</p>
                            <div class="mt-4 flex justify-start space-x-4">
                                <div v-for="occupation in occupations" :key="occupation.name"
                                    @click="selectedOccupation = occupation"
                                    :class="['w-fit h-16 p-4 rounded-full cursor-pointer', selectedOccupation.name === occupation.name ? occupation.color : 'bg-black']">
                                    <span class="text-white text-sm">{{ occupation.name }}</span>
                                </div>
                            </div>
                        </div>
                        <div class="w-full">
                            <label for="dob" class="block text-emerald-200 font-medium mb-2">
                                Date of Birth:
                            </label>
                            <input id="dob" type="text" v-model="dateOfBirth" @input="formatDate"
                                placeholder="31/01/2000...." class="w-full px-4 py-2 bg-zinc-900 border border-zinc-700 rounded-xl text-white 
                    placeholder-gray-400 focus:ring-green-500" required />
                        </div>
                        <div class="w-full flex justify-center">
                            <button
                                class="rounded-3xl text-lg text-center bg-blue-500 transform hover:scale-90 transition hover:bg-blue-800 w-24 cursor-pointer p-4"
                                @click="changeWindow('upload')">Next</button>
                        </div>
                    </div>
                </div>
                <div v-if="status === 'upload'" class=" bg-[#0e4b65] w-full p-8 mt-5">
                    <h1 class="text-3xl font-extrabold text-center text-white">Upload Scanned ID</h1>

                    <div class="mt-6 flex justify-center space-x-3">
                        <label for="fileUpload"
                            class="flex items-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded-lg cursor-pointer">
                            <ArrowUpTrayIcon class="h-6 w-6 text-white" />
                            <span>Upload File</span>
                        </label>

                        <input id="fileUpload" type="file" @change="handleFileUpload" accept=".jpg, .jpeg, .png, .pdf"
                            class="hidden" />
                    </div>
                    <div class="mt-6 flex justify-center">
                        <div v-if="selectedFile">
                            <p class="text-white font-medium rounded-3xl w-full p-6 bg-blue-700">Uploaded File: {{
                                selectedFile.name }}</p>
                        </div>
                        <div v-else class="mt-4 text-yellow-400">
                            <p>No file uploaded yet.</p>
                        </div>
                    </div>
                    <div @click="submitApplication" class="flex justify-center mt-3">
                        <button
                            class="bg-green-400 rounded-4xl p-2 shadow-2xs cursor-pointer border-4 border-green-400 transform hover:scale-75 transition duration-500">
                            Submit
                        </button>

                    </div>
                </div>
            </div>

        </Transition>
    </div>


</template>
<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.6s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>
