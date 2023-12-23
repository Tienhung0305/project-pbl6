package com.example.sportstore06.service.MomoService;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class MomoPaymentService {
    private static final String ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";
    private final RestTemplate restTemplate = new RestTemplate();

    public String initiatePayment(BigDecimal amount, String username, Set<Integer> set_id_cart, String baseUrl, String requestType) {
        String REDIRECT_URL = baseUrl + "/api/v1/cart/momo-payment_save";
        String IPN_URL = baseUrl + "/";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String PARTNER_CODE = "MOMOBKUN20180529";
        String ACCESS_KEY = "klm05TvNBzhg7h7j";
        String SECRET_KEY = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa";
        String orderInfo = "Thanh toán qua MoMo";
        String orderId = timestamp.toInstant().toString();
        String requestId = timestamp.toInstant().toString();
        String extraData = "";
        for (Integer id : set_id_cart) {
            extraData = extraData + id + ",";
        }


        Map<String, Object> requestData = new HashMap<>();
        requestData.put("partnerCode", PARTNER_CODE);
        requestData.put("partnerName", "Test");
        requestData.put("storeId", "MomoTestStore");
        requestData.put("requestId", requestId);
        requestData.put("amount", amount.toString());
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

        ResponseEntity<String> response = restTemplate.exchange(ENDPOINT, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> jsonResponse = JsonUtil.jsonToMap(response.getBody());
            if (jsonResponse.containsKey("payUrl")) {
                return jsonResponse.get("payUrl");
            }
        }

        // Handle error
        return null;
    }
}
