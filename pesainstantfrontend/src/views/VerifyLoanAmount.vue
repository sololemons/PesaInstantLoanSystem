\
<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import LoadingSpinner from '@/components/LoadingSpinner.vue';

const route = useRoute();
const applicationId = route.query.applicationId;

const application = ref(null);
const router = useRouter()
const amount = ref(null);
const error = ref('');
const success = ref('');
const loading = ref(false);

const fetchApplication = async () => {
    try {
        loading.value = true;
        const res = await axios.get(`/application/get/loan/applicationId/${applicationId}`);
        application.value = res.data;

    } catch (err) {
        console.error('Failed to fetch application:', err);
        error.value = 'Failed to fetch application data';
    } finally {
        loading.value = false;
    }
};

const initiateDepositToWallet = async () => {
    error.value = '';
    success.value = '';

    if (!amount.value || amount.value <= 0 || amount.value > application.value?.loanLimit) {
        error.value = 'Please enter a valid amount';
        return;
    }

    const rawPhone = application.value?.phoneNumber;
    if (!rawPhone) {
        error.value = 'Phone number is missing from application data';
        return;
    }

    const cleanedPhone = rawPhone.startsWith('0')
        ? '254' + rawPhone.slice(1)
        : rawPhone;

    try {
        loading.value = true;

        const payload = {
            amount: amount.value,
            phoneNumber: cleanedPhone,
        };

        const res = await axios.post('/wallet/deposit', payload);
        console.log('Disbursement response:', res.data);
        router.push({
            name: 'management',
            query: {
                phoneNumber: cleanedPhone
            }
        });
        success.value = 'Payment initiated successfully!';

    } catch (err) {
        console.error(' Disbursement failed:', err);
        error.value = 'Failed to initiate payment. Please try again.';
    } finally {
        loading.value = false;
    }
};

onMounted(fetchApplication);
</script>

<template>
    <div class="bg-gray-600 min-h-screen flex items-start justify-center text-white">
        <LoadingSpinner :show="loading" />

        <div class="bg-gray-600 text-black rounded-3xl p-6 shadow-sm mt-10 w-full max-w-xl">
            <h1 class="text-2xl font-bold mb-4">Funds Disbursement</h1>

            <div v-if="loading">Loading application...</div>
            <div v-else>
                <div v-if="error" class="text-red-500 font-medium mb-4">{{ error }}</div>

                <p v-if="application" class="mb-4">
                    <strong>Loan Limit:</strong> KES {{ application.loanLimit?.toLocaleString() || 0 }}
                </p>

                <label class="block font-medium mb-1">Enter Amount to Disburse</label>
                <input type="number" v-model="amount"
                    class="w-full border border-gray-300 rounded px-4 py-2 mb-4 focus:outline-none focus:ring-2 focus:ring-green-500"
                    :max="application?.loanLimit || 0" placeholder="e.g. 1500" />
                <button @click="initiateDepositToWallet"
                    class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg shadow">
                    Disburse to Wallet
                </button>



                <p v-if="success" class="text-green-600 font-medium mt-4">{{ success }}</p>
            </div>
        </div>
    </div>
</template>
