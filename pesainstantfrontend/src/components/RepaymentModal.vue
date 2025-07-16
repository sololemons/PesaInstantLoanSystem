<script setup>
import { ref } from 'vue';

const props = defineProps({
    phoneNumber: String,
});

const emit = defineEmits(['close', 'repay']);

const amount = ref('');

const submitRepayment = () => {
    const value = Number(amount.value);
    if (isNaN(value) || value <= 0) {
        alert('Enter a valid amount');
        return;
    }

    emit('repay', { amount: value });
};
</script>

<template>
    <div class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg p-6 w-[90%] max-w-md shadow-xl">
            <h2 class="text-lg font-bold mb-4">Repay Loan</h2>

            <div class="mb-4">
                <label class="block text-gray-700 font-semibold">Phone Number:</label>
                <input type="text" :value="phoneNumber" readonly class="w-full px-4 py-2 border rounded bg-gray-100" />
            </div>

            <div class="mb-4">
                <label class="block text-gray-700 font-semibold">Amount to Repay:</label>
                <input type="number" v-model="amount" placeholder="Enter amount"
                    class="w-full px-4 py-2 border rounded" />
            </div>

            <div class="flex justify-end gap-4 mt-6">
                <button @click="$emit('close')" class="bg-gray-500 text-white px-4 py-2 rounded">
                    Cancel
                </button>
                <button @click="submitRepayment" class="bg-blue-600 text-white px-4 py-2 rounded">
                    Repay
                </button>
            </div>
        </div>
    </div>
</template>
