<script setup>
import { ref } from 'vue';

const props = defineProps({
    phoneNumber: String,
});
const emit = defineEmits(['close', 'withdraw']);

const amount = ref('');

const handleWithdraw = () => {
    if (!amount.value || isNaN(amount.value) || Number(amount.value) <= 0) {
        alert('Please enter a valid amount.');
        return;
    }

    emit('withdraw', {
        amount: Number(amount.value),
    });
};
</script>

<template>
    <div class="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg p-6 w-[90%] max-w-md shadow-xl">
            <h2 class="text-xl font-bold mb-4 text-gray-800">Withdraw Funds</h2>

            <div class="mb-4">
                <label class="block text-sm text-gray-700 font-medium mb-1">Phone Number</label>
                <input type="text" :value="phoneNumber" readonly class="w-full px-4 py-2 border rounded bg-gray-100" />
            </div>

            <div class="mb-4">
                <label class="block text-sm text-gray-700 font-medium mb-1">Amount</label>
                <input type="number" v-model="amount" class="w-full px-4 py-2 border rounded"
                    placeholder="Enter amount to withdraw" />
            </div>

            <div class="flex justify-end gap-3 mt-6">
                <button @click="$emit('close')" class="bg-gray-500 text-white px-4 py-2 rounded">
                    Cancel
                </button>
                <button @click="handleWithdraw" class="bg-green-600 text-white px-4 py-2 rounded">
                    Withdraw
                </button>
            </div>
        </div>
    </div>
</template>
