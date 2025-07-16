package com.walletservice.services;
import com.walletservice.dtos.TransactionDto;
import com.walletservice.entity.WalletTransactionHistory;
import com.walletservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<TransactionDto> getTransactionsByPhoneNumber(String phoneNumber) {
        List<WalletTransactionHistory> historyList = transactionRepository.findByPhoneNumber(phoneNumber);
        return historyList.stream()
                .map(this::mapToTransactionDto)
                .collect(Collectors.toList());
    }

    private TransactionDto mapToTransactionDto(WalletTransactionHistory history) {
        TransactionDto dto = new TransactionDto();
        dto.setPhoneNumber(history.getPhoneNumber());
        dto.setTransactionDate(String.valueOf(history.getTransactionDateTime()));
        dto.setTransactionType(String.valueOf(history.getTransactionType()));
        dto.setTransactionAmount(String.valueOf(history.getTransactionAmount()));
        dto.setTransactionCode(String.valueOf(history.getTransactionCode()));
        return dto;
    }


}

