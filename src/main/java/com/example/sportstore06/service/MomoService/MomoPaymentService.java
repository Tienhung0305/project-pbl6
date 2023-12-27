package com.example.sportstore06.service.MomoService;

import com.example.sportstore06.dao.response.MomoResponse;
import com.example.sportstore06.entity.Bill;
import com.example.sportstore06.entity.Transaction;
import com.example.sportstore06.repository.IPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MomoPaymentService {

    //  PARTNER_CODE = "MOMOBKUN20180529";
    //  ACCESS_KEY = "klm05TvNBzhg7h7j";
    //  SECRET_KEY = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa";

    private final IPaymentRepository paymentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public Transaction initiatePayment(double total_all, String username, Set<Integer> set_id_cart, String baseUrl, String requestType) {
        String PARTNER_CODE = paymentRepository.findById("momo").get().getPartner_code();
        String ACCESS_KEY = paymentRepository.findById("momo").get().getAccess_key();
        String SECRET_KEY = paymentRepository.findById("momo").get().getSecret_key();

        String API = "https://test-payment.momo.vn/v2/gateway/api/create";
        String REDIRECT_URL = baseUrl + "/api/v1/cart/momo-payment_save";
        String IPN_URL = baseUrl + "/";

        Transaction transaction = new Transaction();
        String orderInfo = "Thanh toán qua MoMo";
        long unixTime = System.currentTimeMillis() / 1000L;
        String orderId = "" + unixTime;
        String requestId = "" + unixTime;
        Long amount = (long) total_all;
        String extraData = "";
        for (Integer id : set_id_cart) {
            extraData = extraData + id + ",";
        }

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("partnerCode", PARTNER_CODE);
        requestData.put("partnerName", "Test");
        requestData.put("storeId", "MomoTestStore");
        requestData.put("requestId", requestId);
        requestData.put("amount", amount);
        requestData.put("orderId", orderId);
        requestData.put("orderInfo", orderInfo);
        requestData.put("redirectUrl", REDIRECT_URL);
        requestData.put("ipnUrl", IPN_URL);
        requestData.put("lang", "vi");
        requestData.put("extraData", extraData);
        requestData.put("requestType", requestType);

        String rawHash = "accessKey=" + ACCESS_KEY + "&amount=" + amount + "&extraData=" + extraData +
                "&ipnUrl=" + IPN_URL + "&orderId=" + orderId + "&orderInfo=" + orderInfo +
                "&partnerCode=" + PARTNER_CODE + "&redirectUrl=" + REDIRECT_URL +
                "&requestId=" + orderId + "&requestType=" + requestType;
        String signature = HMACSHA256Util.calculateSignature(rawHash, SECRET_KEY);
        requestData.put("signature", signature);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);
        ResponseEntity<String> response = restTemplate.exchange(API, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> jsonResponse = JsonUtil.jsonToMap(response.getBody());
            if (jsonResponse.containsKey("payUrl")) {
                String url = jsonResponse.get("payUrl");
                transaction.setUrl(url);
                transaction.setOrderId(orderId);
                transaction.setRequestId(requestId);
                return transaction;
            }
        }
        // Handle error
        return null;
    }


    public MomoResponse checkTransactionStatus(Bill bill) {
        String PARTNER_CODE = paymentRepository.findById("momo").get().getPartner_code();
        String ACCESS_KEY = paymentRepository.findById("momo").get().getAccess_key();
        String SECRET_KEY = paymentRepository.findById("momo").get().getSecret_key();
        String API = "https://test-payment.momo.vn/v2/gateway/api/query";

        String orderId = bill.getTransaction().getOrderId();
        String requestId = bill.getTransaction().getRequestId();

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("partnerCode", PARTNER_CODE);
        requestData.put("requestId", requestId);
        requestData.put("orderId", orderId);
        requestData.put("lang", "vi");

        String rawHash =
                "accessKey=" + ACCESS_KEY +
                        "&orderId=" + orderId +
                        "&partnerCode=" + PARTNER_CODE +
                        "&requestId=" + requestId;
        String signature = HMACSHA256Util.calculateSignature(rawHash, SECRET_KEY);
        requestData.put("signature", signature);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);
        ResponseEntity<String> response = restTemplate.exchange(API, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> jsonResponse = JsonUtil.jsonToMap(response.getBody());
            if (jsonResponse.containsKey("resultCode")) {
                MomoResponse momoResponse = new MomoResponse();
                momoResponse.setMessage(jsonResponse.get("message"));
                momoResponse.setResultCode((jsonResponse.get("resultCode")));
                return momoResponse;
            }
        }

        // Handle error
        return null;
    }

    public MomoResponse refundTransactionStatus(Bill bill) {
        String PARTNER_CODE = paymentRepository.findById("momo").get().getPartner_code();
        String ACCESS_KEY = paymentRepository.findById("momo").get().getAccess_key();
        String SECRET_KEY = paymentRepository.findById("momo").get().getSecret_key();

        String API = "https://test-payment.momo.vn/v2/gateway/api/refund";

        String orderId = bill.getTransaction().getTransId();
        String requestId = bill.getTransaction().getRequestId();
        long transId = Long.decode(bill.getTransaction().getTransId());
        long amount = (long) bill.getTotal();

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("partnerCode", PARTNER_CODE);
        requestData.put("orderId", orderId);
        requestData.put("requestId", requestId);
        requestData.put("amount", amount);
        requestData.put("transId", transId);
        requestData.put("lang", "vi");
        requestData.put("description", "");

        String rawHash =
                "accessKey=" + ACCESS_KEY +
                        "&amount=" + String.valueOf(amount) +
                        "&description=" + "" +
                        "&orderId=" + orderId +
                        "&partnerCode=" + PARTNER_CODE +
                        "&requestId=" + requestId +
                        "&transId=" + String.valueOf(transId);
        String signature = HMACSHA256Util.calculateSignature(rawHash, SECRET_KEY);
        requestData.put("signature", signature);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);
        ResponseEntity<String> response = restTemplate.exchange(API, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> jsonResponse = JsonUtil.jsonToMap(response.getBody());
            if (jsonResponse.containsKey("resultCode")) {
                MomoResponse momoResponse = new MomoResponse();
                momoResponse.setMessage(jsonResponse.get("message"));
                momoResponse.setResultCode(jsonResponse.get("resultCode"));
                return momoResponse;
            }
        }

        // Handle error
        return null;
    }


}
