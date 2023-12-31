package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.MomoIPNRequest;
import com.example.sportstore06.entity.Bill;
import com.example.sportstore06.entity.Transaction;
import com.example.sportstore06.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/momo")
@RequiredArgsConstructor
public class MomoIPNController {
    private final BillService billService;
    @GetMapping
    public ResponseEntity<String> SayHi() {
        return ResponseEntity.ok("Momo");
    }
    @PostMapping("/momo_ipn")
    public ResponseEntity<?> handleMomoIPN(@RequestBody MomoIPNRequest momoIPNRequest) {
        if (momoIPNRequest.getResultCode() == 0) {
            String[] split_extraData = momoIPNRequest.getExtraData().split(",");
            Set<Integer> set_id_bill = new HashSet<>();
            for (String i : split_extraData) {
                set_id_bill.add(Integer.valueOf(i));
            }
            for (Integer id : set_id_bill) {
                Bill bill = billService.findById(id).get();
                bill.setState(3);
                bill.setUpdated_at(new Timestamp(new Date().getTime()));
                Transaction transaction = bill.getTransaction();
                transaction.setTransId(String.valueOf(momoIPNRequest.getTransId()));
                transaction.setPayType(momoIPNRequest.getPayType());
                transaction.setOrderType(momoIPNRequest.getOrderId());
                bill.setTransaction(transaction);
                billService.save(bill);
            }
        }
        if (momoIPNRequest.getResultCode() != 0) {
            String[] split_extraData = momoIPNRequest.getExtraData().split(",");
            Set<Integer> set_id_bill = new HashSet<>();
            for (String i : split_extraData) {
                set_id_bill.add(Integer.valueOf(i));
            }
            for (Integer id : set_id_bill) {
                Bill bill = billService.findById(id).get();
                bill.setState(4);
                bill.setUpdated_at(new Timestamp(new Date().getTime()));
                Transaction transaction = bill.getTransaction();
                transaction.setTransId(String.valueOf(momoIPNRequest.getTransId()));
                transaction.setPayType(momoIPNRequest.getPayType());
                transaction.setOrderType(momoIPNRequest.getOrderId());
                bill.setTransaction(transaction);
                billService.save(bill);
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
