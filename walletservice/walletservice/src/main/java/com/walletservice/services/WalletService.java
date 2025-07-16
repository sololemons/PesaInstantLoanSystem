package com.walletservice.services;

import com.walletservice.configuration.RabbitMQConfiguration;
import com.walletservice.dtos.WalletDto;
import com.walletservice.entity.DisbursementRequest;
import com.walletservice.entity.TransactionType;
import com.walletservice.entity.Wallet;
import com.walletservice.entity.WalletTransactionHistory;
import com.walletservice.exceptions.UserNotFoundException;
import com.walletservice.repository.TransactionRepository;
import com.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import com.shared.dtos.ProgressDto;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;
    public void disburseFundsToWallet(DisbursementRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String amount = request.getAmount();
        Optional<Wallet> wallet = walletRepository.findByPhoneNumber(phoneNumber);
        Wallet newWallet;
        if (wallet.isEmpty()) {
             newWallet = new Wallet();
            newWallet.setPhoneNumber(phoneNumber);
            newWallet.setWalletBalance(amount);
            newWallet.setDateCreated(LocalDate.now());
            newWallet.setFundsDue(amount);
            walletRepository.save(newWallet);
        }
        else {
            newWallet = wallet.get();
            newWallet.setWalletBalance(amount+wallet.get().getWalletBalance());
            walletRepository.save(newWallet);
        }
        WalletTransactionHistory walletTransactionHistory = new WalletTransactionHistory();
        walletTransactionHistory.setWallet(newWallet);
        walletTransactionHistory.setTransactionAmount(Double.parseDouble(amount));
        walletTransactionHistory.setPhoneNumber(phoneNumber);
        walletTransactionHistory.setTransactionType(TransactionType.DEPOSIT);
        walletTransactionHistory.setTransactionDateTime(LocalDateTime.now());
        walletTransactionHistory.setTransactionCode(generateWalletCode());
        transactionRepository.save(walletTransactionHistory);
        ProgressDto progressDto = new ProgressDto();
        progressDto.setPhoneNumber(phoneNumber);
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.PROGRESS_QUEUE ,progressDto);

    }
        public static String generateWalletCode() {
            String prefix = "PS";
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            SecureRandom random = new SecureRandom();
            StringBuilder sb = new StringBuilder(prefix);
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            return sb.toString();
        }


    public WalletDto getWalletByPhoneNumber(String phoneNumber) {
        Optional<Wallet> wallet = walletRepository.findByPhoneNumber(phoneNumber);
        if (wallet.isPresent()) {
            WalletDto walletDto = new WalletDto();
            walletDto.setWalletBalance(wallet.get().getWalletBalance());
            walletDto.setFundsDue(wallet.get().getFundsDue());
            walletDto.setPhoneNumber(wallet.get().getPhoneNumber());
            walletDto.setCreatedAt(wallet.get().getDateCreated().toString());
            return walletDto;

        }
        else {
        throw new UserNotFoundException("Wallet not found with phone number " + phoneNumber);
        }
    }




}
