package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.Transaction;
import com.example.sportstore06.entity.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String pay_url;
    private String pay_type;
    private String order_type;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.pay_url = transaction.getPayUrl();
        this.pay_type = transaction.getPayType();
        this.order_type = transaction.getOrderType();
    }
}
