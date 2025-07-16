<script setup>
import { useRoute } from 'vue-router';
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import InterestCalculator from '@/components/InterestCalculator.vue';
import WithdrawalModal from '@/components/WithdrawalModal.vue';
import RepaymentModal from '@/components/RepaymentModal.vue';

const route = useRoute();
const phoneNumber = route.query.phoneNumber;
const cleanedPhone = phoneNumber?.replace('+', '');
const showInterestModal = ref(false);
const showRepaymentModal = ref(false);
const showWithdrawalModal = ref(false);
const interestRate = '12%';
const principal = computed(() => wallet.value.walletBalance || 0);

const wallet = ref({});
const interestPlan = ref({})
const transactions = ref([]);

const handleCalculate = async ({ months }) => {
    if (!months || Number(months) <= 0) {
        alert("Please enter a valid number of months (greater than 0).");
        return;
    }

    try {
        const payload = {
            principal: principal.value,
            months: Number(months),
            phoneNumber: phoneNumber,
            interestRate: interestRate
        };
        const res = await axios.post('/application/calculate/interest', payload);
        interestPlan.value = res.data;
        showInterestModal.value = false;
    } catch (err) {
        console.error('Interest calculation failed:', err);
    }
};

const handleRepayment = async ({ amount }) => {
    try {

        const repaymentRequest = {
            phoneNumber: cleanedPhone,
            amount: Number(amount)
        };
        const response = await axios.post('/mpesa/stkpush', repaymentRequest);

    }
    catch (err) {
        console.error('Error pushing the stk push', err)
    }
    finally {
        showRepaymentModal.value = false;
    }
}
const handleWithdrawal = async ({ amount }) => {
    if (amount > principal) {
        alert("You cannot withdraw more than your wallet balance.");
        return;
    }
    try {

        const repaymentRequest = {
            phoneNumber: phoneNumber,
            amount: Number(amount)
        };
        const response = await axios.post('/mpesa/b2c/withdraw', repaymentRequest);
        alert("Withdrwal sucessfull")

    }
    catch (err) {
        console.log('Error oushing the stk push', err)
    }
    finally {
        showWithdrawalModal.value = false
    }
}


const fetchWalletData = async () => {
    try {
        const res = await axios.get(`/wallet/get/phoneNumber`, {
            params: { phoneNumber: cleanedPhone }
        });
        wallet.value = res.data;
    } catch (err) {
        console.error('Failed to fetch wallet info:', err);
    }
};
const fetchInterestPlan = async () => {

    try {
        const res = await axios.get('application/get/interest/plan', {

            params: { phoneNumber: cleanedPhone }
        });

        interestPlan.value = res.data;

    }
    catch (err) {
        console.error('Failed to get interesplan', err)

    }
}

const fetchTransactions = async () => {
    try {
        const res = await axios.get(`/wallet/transactions`, {
            params: { phoneNumber: cleanedPhone }
        });
        transactions.value = res.data;
    } catch (err) {
        console.error('Failed to fetch transactions:', err);
    }
};

onMounted(() => {
    fetchWalletData();
    fetchTransactions();
    fetchInterestPlan();
});
</script>

<template>
    <div class="bg-gray-900 min-h-screen w-full flex justify-center items-start p-4 md:p-8">
        <div class="bg-transparent rounded-3xl shadow-sm p-4 md:p-6 w-full max-w-6xl flex flex-col gap-y-6">
            <div class="flex  justify-center items-center gap-4 font-bold">
                <div @click="showWithdrawalModal = true" class="bg-green-500 p-3 rounded-lg w-full text-center">
                    <p>Withdraw</p>
                </div>
                <div @click="showRepaymentModal = true" class="bg-red-600 p-3 rounded-lg w-full  text-center">
                    <p>Repay</p>
                </div>
            </div>

            <div class="flex flex-col md:flex-row bg-transparent px-4 gap-4 font-extrabold text-amber-50">
                <div class="bg-gray-800 p-4 rounded-lg flex-1">
                    <p>Wallet Balance</p>
                    <p class="text-blue-200 text-lg font-extrabold">KES {{ wallet.walletBalance || '0.00' }}</p>
                </div>
                <div class="bg-gray-800 p-4 rounded-lg flex-1">
                    <p>Funds Due</p>
                    <p class="text-blue-200 text-lg font-extrabold">KES {{ wallet.fundsDue || '0.00' }}</p>
                </div>
                <div @click="showInterestModal = true" class="bg-gray-800 p-4 rounded-lg flex-1">
                    <p>Interest Calculator</p>
                    <p class="text-white text-xl font-semibold">
                        <span class="text-blue-400 text-lg">+</span>
                    </p>
                </div>
            </div>

            <div class="flex flex-col bg-gray-800 px-4 py-4 text-amber-50 font-bold rounded-lg shadow-sm space-y-2">
                <div>
                    <p>Interest Plan</p>
                    <p>{{ wallet.interestPlan || 'Flexible' }}</p>
                </div>
                <div>
                    <p>Interest Amount</p>
                    <p>KES {{ wallet.interestAccrued || '0.00' }}</p>
                </div>
                <div>
                    <p>Repayment Date</p>
                    <p>{{ wallet.repaymentDate || 'N/A' }}</p>
                </div>
            </div>

            <div class="bg-gray-800 text-white p-4 md:p-6 rounded-lg shadow-lg mt-4 overflow-x-auto">
                <h2 class="text-xl font-bold mb-4">Transaction History</h2>
                <table class="min-w-full text-left text-sm">
                    <thead class="border-b border-gray-600">
                        <tr>
                            <th class="pb-2">Date</th>
                            <th class="pb-2">Type</th>
                            <th class="pb-2">Amount (KES)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(tx, index) in transactions" :key="index" class="border-b border-gray-700">
                            <td class="py-2">{{ new Date(tx.transactionDate).toLocaleDateString() }}</td>
                            <td class="py-2">
                                <span :class="{
                                    'text-green-400': tx.transactionType === 'CREDIT',
                                    'text-red-400': tx.transactionType === 'DEBIT',
                                    'text-green-200': tx.transactionType !== 'DEPOSIT'
                                }">
                                    {{ tx.transactionType }}
                                </span>
                            </td>
                            <td class="py-2">{{ tx.transactionAmount }}</td>
                        </tr>
                        <tr v-if="transactions.length === 0">
                            <td colspan="3" class="text-gray-400 py-4 text-center">No transactions found.</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <InterestCalculator v-if="showInterestModal" :principal="principal" :interestRate="interestRate"
            @close="showInterestModal = false" @calculate="handleCalculate" />
        <WithdrawalModal v-if="showWithdrawalModal" :phoneNumber="phoneNumber" @close="showWithdrawalModal = false"
            @withdraw="handleWithdrawal" />
        <RepaymentModal v-if="showRepaymentModal" :phoneNumber="phoneNumber" @close="showRepaymentModal = false"
            @repay="handleRepayment" />



    </div>


</template>
