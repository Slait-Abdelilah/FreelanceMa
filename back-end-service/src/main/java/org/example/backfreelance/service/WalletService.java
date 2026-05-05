package org.example.backfreelance.service;

import lombok.RequiredArgsConstructor;
import org.example.backfreelance.dto.*;
import org.example.backfreelance.entity.Transaction;
import org.example.backfreelance.entity.User;
import org.example.backfreelance.entity.Wallet;
import org.example.backfreelance.enums.TransactionStatus;
import org.example.backfreelance.enums.TransactionType;
import org.example.backfreelance.exceptionn.AppException;
import org.example.backfreelance.repository.TransactionRepository;
import org.example.backfreelance.repository.UserRepository;
import org.example.backfreelance.repository.WalletRepository;
import org.example.backfreelance.seecuriity.AuditLogger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AuditLogger auditLogger;

    public Wallet createWallet(Long userId) {
        if (walletRepository.existsByUserId(userId)) {
            return walletRepository.findByUserId(userId).get();
        }
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .pendingBalance(BigDecimal.ZERO)
                .totalEarned(BigDecimal.ZERO)
                .build();
        return walletRepository.save(wallet);
    }

    public WalletSummaryDTO getMyWallet(String email) {
        User user = getUser(email);
        Wallet wallet = getOrCreateWallet(user.getId());
        List<TransactionDTO> recent = transactionRepository
                .findTop10ByWalletIdOrderByCreatedAtDesc(wallet.getId())
                .stream()
                .map(this::toTransactionDTO)
                .collect(Collectors.toList());
        return WalletSummaryDTO.builder()
                .wallet(toWalletDTO(wallet))
                .recentTransactions(recent)
                .build();
    }

    public List<TransactionDTO> getAllTransactions(String email) {
        User user = getUser(email);
        Wallet wallet = getOrCreateWallet(user.getId());
        return transactionRepository
                .findByWalletIdOrderByCreatedAtDesc(wallet.getId())
                .stream()
                .map(this::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO requestWithdrawal(String email, WithdrawalRequest request) {
        User user = getUser(email);
        Wallet wallet = getOrCreateWallet(user.getId());

        validateIban(request.getIban());

        if (request.getAmount().compareTo(new BigDecimal("100")) < 0) {
            throw new AppException("Montant minimum de retrait : 100 DH", HttpStatus.BAD_REQUEST);
        }
        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new AppException("Solde insuffisant", HttpStatus.BAD_REQUEST);
        }

        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .walletId(wallet.getId())
                .amount(request.getAmount().negate())
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.PENDING)
                .description("Retrait vers IBAN : " + maskIban(request.getIban()))
                .build();

        auditLogger.log("WITHDRAWAL_REQUEST", email);
        return toTransactionDTO(transactionRepository.save(transaction));
    }

    @Transactional
    public void holdEscrow(Long clientUserId, BigDecimal amount, Long missionId, String description) {
        Wallet wallet = getOrCreateWallet(clientUserId);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setPendingBalance(wallet.getPendingBalance().add(amount));
        walletRepository.save(wallet);
        transactionRepository.save(Transaction.builder()
                .walletId(wallet.getId())
                .amount(amount.negate())
                .type(TransactionType.ESCROW_HOLD)
                .status(TransactionStatus.COMPLETED)
                .missionId(missionId)
                .description(description)
                .build());
    }

    @Transactional
    public void releaseEscrow(Long freelancerUserId, BigDecimal amount, Long missionId, String description) {
        Wallet wallet = getOrCreateWallet(freelancerUserId);
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setTotalEarned(wallet.getTotalEarned().add(amount));
        walletRepository.save(wallet);
        transactionRepository.save(Transaction.builder()
                .walletId(wallet.getId())
                .amount(amount)
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.COMPLETED)
                .missionId(missionId)
                .description(description)
                .build());
    }

    @Transactional
    public void refundEscrow(Long clientUserId, BigDecimal amount, Long missionId, String description) {
        Wallet wallet = getOrCreateWallet(clientUserId);
        wallet.setPendingBalance(wallet.getPendingBalance().subtract(amount));
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        transactionRepository.save(Transaction.builder()
                .walletId(wallet.getId())
                .amount(amount)
                .type(TransactionType.ESCROW_REFUND)
                .status(TransactionStatus.COMPLETED)
                .missionId(missionId)
                .description(description)
                .build());
    }

    private void validateIban(String iban) {
        if (iban == null || iban.isBlank()) {
            throw new AppException("IBAN requis", HttpStatus.BAD_REQUEST);
        }
        String normalized = iban.replaceAll("\\s+", "").toUpperCase();
        if (!normalized.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{11,30}$")) {
            throw new AppException("Format IBAN invalide", HttpStatus.BAD_REQUEST);
        }
    }

    private String maskIban(String iban) {
        if (iban == null || iban.length() < 8) return "****";
        String normalized = iban.replaceAll("\\s+", "").toUpperCase();
        int len = normalized.length();
        return normalized.substring(0, 4) + "*".repeat(Math.max(0, len - 8)) + normalized.substring(len - 4);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Utilisateur introuvable", HttpStatus.NOT_FOUND));
    }

    private Wallet getOrCreateWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseGet(() -> createWallet(userId));
    }

    private WalletDTO toWalletDTO(Wallet w) {
        return WalletDTO.builder()
                .id(w.getId())
                .balance(w.getBalance())
                .pendingBalance(w.getPendingBalance())
                .totalEarned(w.getTotalEarned())
                .createdAt(w.getCreatedAt())
                .build();
    }

    private TransactionDTO toTransactionDTO(Transaction t) {
        return TransactionDTO.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType().name())
                .status(t.getStatus().name())
                .description(t.getDescription())
                .missionId(t.getMissionId())
                .createdAt(t.getCreatedAt())
                .build();
    }
}