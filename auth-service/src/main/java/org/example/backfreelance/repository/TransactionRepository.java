package org.example.backfreelance.repository;

import org.example.backfreelance.entity.Transaction;
import org.example.backfreelance.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);
    List<Transaction> findByWalletIdAndTypeOrderByCreatedAtDesc(Long walletId, TransactionType type);
    List<Transaction> findTop10ByWalletIdOrderByCreatedAtDesc(Long walletId);

}
