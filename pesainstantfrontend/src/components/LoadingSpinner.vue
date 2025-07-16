<template>
    <div v-if="show" class="fixed inset-0 flex items-center justify-center bg-black/50 z-50">
        <div class="flex space-x-2">
            <span class="w-3 h-3 bg-blue-500 rounded-full animate-bounce [animation-delay:-0.3s]"></span>
            <span class="w-3 h-3 bg-blue-500 rounded-full animate-bounce [animation-delay:-0.15s]"></span>
            <span class="w-3 h-3 bg-blue-500 rounded-full animate-bounce"></span>
        </div>
    </div>
</template>

<script setup>
defineProps({
    show: Boolean,
});
</script>
