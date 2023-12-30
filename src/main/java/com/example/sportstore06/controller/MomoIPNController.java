package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.MomoIPNRequest;
import com.example.sportstore06.entity.Bill;
import com.example.sportstore06.entity.Transaction;
import com.example.sportstore06.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/momo_ipn")
@RequiredArgsConstructor
public class MomoIPNController {
    //private static final String PARTNER_SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final BillService billService;
    private static final Logger logger = LoggerFactory.getLogger(MomoIPNController.class);

    @PostMapping
    public ResponseEntity<?> handleMomoIPN(@RequestBody MomoIPNRequest momoIPNRequest) {
        logger.info("Received MoMo IPN: {}", momoIPNRequest.toString());
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
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
