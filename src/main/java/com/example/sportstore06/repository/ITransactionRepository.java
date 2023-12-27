package com.example.sportstore06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sportstore06.entity.Transaction;
@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Integer> {
}
