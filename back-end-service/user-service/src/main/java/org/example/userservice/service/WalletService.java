package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.DepositRequest;
import org.example.userservice.dto.EscrowRequest;
import org.example.userservice.dto.TransactionDTO;
import org.example.userservice.dto.WalletDTO;
import org.example.userservice.dto.WalletSummaryDTO;
import org.example.userservice.dto.WithdrawalRequest;
import org.example.userservice.entity.Transaction;
import org.example.userservice.entity.Wallet;
import org.example.userservice.enums.TransactionStatus;
import org.example.userservice.enums.TransactionType;
import org.example.userservice.exception.AppException;
import org.example.userservice.repository.TransactionRepository;
import org.example.userservice.repository.WalletRepository;
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

    public WalletSummaryDTO getMyWallet(Long userId) {
        Wallet wallet = getOrCreateWallet(userId);
        List<TransactionDTO> recent = transactionRepository
                .findTop10ByWalletIdOrderByCreatedAtDesc(wallet.getId())
                .stream().map(this::toTransactionDTO).collect(Collectors.toList());
        return WalletSummaryDTO.builder()
                .wallet(toWalletDTO(wallet))
                .recentTransactions(recent)
                .build();
    }

    public List<TransactionDTO> getAllTransactions(Long userId) {
        Wallet wallet = getOrCreateWallet(userId);
        return transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId())
                .stream().map(this::toTransactionDTO).collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO deposit(Long userId, DepositRequest request) {
        if (request.getAmount().compareTo(new BigDecimal("50")) < 0) {
            throw new AppException("Montant minimum de recharge : 50 DH", HttpStatus.BAD_REQUEST);
        }

        Wallet wallet = getOrCreateWallet(userId);
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        walletRepository.save(wallet);

        Transaction tx = Transaction.builder()
                .walletId(wallet.getId())
                .amount(request.getAmount())
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .description("Recharge du solde : +" + request.getAmount() + " DH")
                .build();

        return toTransactionDTO(transactionRepository.save(tx));
    }

    @Transactional
    public TransactionDTO escrowRelease(EscrowRequest request) {
        Wallet wallet = getOrCreateWallet(request.getUserId());

        if (request.getAmount().compareTo(wallet.getPendingBalance()) > 0) {
            throw new AppException(
                    "Montant à libérer (" + request.getAmount() + ") supérieur au solde en attente (" + wallet.getPendingBalance() + ")",
                    HttpStatus.BAD_REQUEST);
        }

        wallet.setPendingBalance(wallet.getPendingBalance().subtract(request.getAmount()));
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        wallet.setTotalEarned(wallet.getTotalEarned().add(request.getAmount()));
        walletRepository.save(wallet);

        Transaction tx = Transaction.builder()
                .walletId(wallet.getId())
                .amount(request.getAmount())
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription())
                .missionId(request.getMissionId())
                .build();

        return toTransactionDTO(transactionRepository.save(tx));
    }

    // Direct credit to freelancer balance (no escrow hold needed)
    @Transactional
    public TransactionDTO directCredit(EscrowRequest request) {
        Wallet wallet = getOrCreateWallet(request.getUserId());
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        wallet.setTotalEarned(wallet.getTotalEarned().add(request.getAmount()));
        walletRepository.save(wallet);

        Transaction tx = Transaction.builder()
                .walletId(wallet.getId())
                .amount(request.getAmount())
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription())
                .missionId(request.getMissionId())
                .build();

        return toTransactionDTO(transactionRepository.save(tx));
    }

    // Client side: block funds when accepting an application
    @Transactional
    public TransactionDTO clientEscrowHold(EscrowRequest request) {
        Wallet wallet = getOrCreateWallet(request.getUserId());

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new AppException("Solde client insuffisant pour bloquer les fonds", HttpStatus.BAD_REQUEST);
        }

        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        wallet.setPendingBalance(wallet.getPendingBalance().add(request.getAmount()));
        walletRepository.save(wallet);

        Transaction tx = Transaction.builder()
                .walletId(wallet.getId())
                .amount(request.getAmount().negate())
                .type(TransactionType.ESCROW_HOLD)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription())
                .missionId(request.getMissionId())
                .build();

        return toTransactionDTO(transactionRepository.save(tx));
    }

    // Client side: release funds from pending when mission is validated
    @Transactional
    public TransactionDTO clientEscrowRelease(EscrowRequest request) {
        Wallet wallet = getOrCreateWallet(request.getUserId());

        if (request.getAmount().compareTo(wallet.getPendingBalance()) > 0) {
            throw new AppException(
                    "Montant à libérer (" + request.getAmount() + ") supérieur au solde bloqué (" + wallet.getPendingBalance() + ")",
                    HttpStatus.BAD_REQUEST);
        }

        wallet.setPendingBalance(wallet.getPendingBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

        Transaction tx = Transaction.builder()
                .walletId(wallet.getId())
                .amount(request.getAmount().negate())
                .type(TransactionType.ESCROW_RELEASE)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription())
                .missionId(request.getMissionId())
                .build();

        return toTransactionDTO(transactionRepository.save(tx));
    }

    @Transactional
    public TransactionDTO requestWithdrawal(Long userId, WithdrawalRequest request) {
        Wallet wallet = getOrCreateWallet(userId);

        validateIban(request.getIban());

        if (request.getAmount().compareTo(new BigDecimal("100")) < 0) {
            throw new AppException("Montant minimum de retrait : 100 DH", HttpStatus.BAD_REQUEST);
        }
        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new AppException("Solde insuffisant", HttpStatus.BAD_REQUEST);
        }

        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);

        Transaction tx = Transaction.builder()
                .walletId(wallet.getId())
                .amount(request.getAmount().negate())
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.PENDING)
                .description("Retrait vers IBAN : " + maskIban(request.getIban()))
                .build();

        return toTransactionDTO(transactionRepository.save(tx));
    }

    private Wallet getOrCreateWallet(Long userId) {
        return walletRepository.findByUserId(userId).orElseGet(() -> {
            Wallet w = Wallet.builder().userId(userId)
                    .balance(BigDecimal.ZERO).pendingBalance(BigDecimal.ZERO).totalEarned(BigDecimal.ZERO)
                    .build();
            return walletRepository.save(w);
        });
    }

    private void validateIban(String iban) {
        if (iban == null || iban.isBlank()) throw new AppException("IBAN requis", HttpStatus.BAD_REQUEST);
        String n = iban.replaceAll("\\s+", "").toUpperCase();
        if (!n.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{11,30}$"))
            throw new AppException("Format IBAN invalide", HttpStatus.BAD_REQUEST);
    }

    private String maskIban(String iban) {
        if (iban == null || iban.length() < 8) return "****";
        String n = iban.replaceAll("\\s+", "").toUpperCase();
        return n.substring(0, 4) + "*".repeat(Math.max(0, n.length() - 8)) + n.substring(n.length() - 4);
    }

    private WalletDTO toWalletDTO(Wallet w) {
        return WalletDTO.builder().id(w.getId()).balance(w.getBalance())
                .pendingBalance(w.getPendingBalance()).totalEarned(w.getTotalEarned())
                .createdAt(w.getCreatedAt()).build();
    }

    private TransactionDTO toTransactionDTO(Transaction t) {
        return TransactionDTO.builder().id(t.getId()).amount(t.getAmount())
                .type(t.getType().name()).status(t.getStatus().name())
                .description(t.getDescription()).missionId(t.getMissionId())
                .createdAt(t.getCreatedAt()).build();
    }
}
