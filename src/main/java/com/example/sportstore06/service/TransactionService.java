package com.example.sportstore06.service;

import com.example.sportstore06.entity.Sale;
import com.example.sportstore06.entity.Transaction;
import com.example.sportstore06.repository.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final ITransactionRepository transactionRepository;

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Optional<Transaction> findById(int id) {
        return transactionRepository.findById(id);
    }

}
