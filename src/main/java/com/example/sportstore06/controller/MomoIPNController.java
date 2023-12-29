package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.MomoIPNRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/momo_ipn")
@RequiredArgsConstructor
public class MomoIPNController {
    private static final String PARTNER_SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @PostMapping
    public ResponseEntity<String> handleMomoIPN(@RequestBody MomoIPNRequest momoIPNRequest) {
        try {
            System.out.println("Received MOMO IPN: " + momoIPNRequest.toString());
            // Xử lý IPN ở đây
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    private boolean isValidSignature(MomoIPNRequest momoIPNRequest) {
        String signatureToVerify = momoIPNRequest.getSignature();
        momoIPNRequest.setSignature(null);

        String data = momoIPNRequest.toString();
        momoIPNRequest.setSignature(signatureToVerify);

        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(PARTNER_SECRET_KEY.getBytes(), "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] signatureBytes = sha256Hmac.doFinal(data.getBytes());
            String calculatedSignature = Base64.getEncoder().encodeToString(signatureBytes);

            return calculatedSignature.equals(signatureToVerify);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }

}
