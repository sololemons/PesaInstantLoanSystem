<script setup>
import { ref } from 'vue';

defineProps({
    principal: String,
    interestRate: String,
});
const emit = defineEmits(['close', 'calculate']);

const months = ref('');
const submit = () => {
    emit('calculate', { months: months.value });
};
</script>

<template>
    <div class="fixed inset-0 bg-black/35 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg p-6 w-[90%] max-w-md shadow-lg">
            <h2 class="text-lg font-bold mb-4">Interest Calculator</h2>

            <div class="mb-4">
                <label class="block text-gray-700 font-semibold">Principal:</label>
                <input type="text" :value="principal" readonly class="w-full px-4 py-2 border rounded bg-gray-100" />
            </div>

            <div class="mb-4">
                <label class="block text-gray-700 font-semibold">Interest Rate:</label>
                <input type="text" :value="interestRate" readonly class="w-full px-4 py-2 border rounded bg-gray-100" />
            </div>

            <div class="mb-4">
                <label class="block text-gray-700 font-semibold">Repayment Months:</label>
                <input type="number" v-model="months" class="w-full px-4 py-2 border rounded" />
            </div>

            <div class="flex justify-end gap-4">
                <button @click="$emit('close')" class="bg-gray-500 text-white px-4 py-2 rounded">Cancel</button>
                <button @click="submit" class="bg-blue-600 text-white px-4 py-2 rounded">Calculate</button>
            </div>
        </div>
    </div>
</template>
