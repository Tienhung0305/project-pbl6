package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.BillDetailRequest;
import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.dao.request.CartRequest;
import com.example.sportstore06.dao.response.*;
import com.example.sportstore06.model.*;
import com.example.sportstore06.service.*;
import com.example.sportstore06.service.MomoService.MomoPaymentService;
import com.example.sportstore06.service.MomoService.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final BusinessService businessService;
    private final ProductService productService;
    private final BillService billService;
    private final MomoPaymentService momoPaymentService;

    @PostMapping("/momo")
    public ResponseEntity<?> momo(@RequestParam(value = "id_user", required = true) Integer id_user,
                                  HttpServletRequest request) {
        if (userService.findById(id_user).isPresent()) {
            String baseUrl = UrlUtil.getBaseUrl(request);
            List<Cart> carts = cartService.GetAllByIdUser(id_user);
            double total = 0.0;
            if (!carts.isEmpty()) {
                Set<BillDetailRequest> set = new HashSet<>();
                BillRequest bill = new BillRequest();
                for (Cart cart : carts) {
                    BillDetailRequest bill_detail = new BillDetailRequest();

                    bill_detail.setId_product(cart.getProduct().getId());
                    bill_detail.setQuantity(cart.getQuantity());

                    Sale sale = cart.getProduct().getProductInfo().getSale();
                    if (sale != null) {
                        total = total + cart.getProduct().getPrice() * cart.getQuantity() * (100 - sale.getDiscount());
                    } else {
                        total = total + cart.getProduct().getPrice() * cart.getQuantity();
                    }

                    bill_detail.setPrice(total);
                    set.add(bill_detail);

                    //remove cart
                    cartService.deleteById(cart.getId());
                }
                // 0 : đang giao
                // 1 : đã giao thành công
                // 2 : chưa thanh toán

                LocalDateTime currentDateTime = LocalDateTime.now();
                String username = userService.findById(id_user).get().getUsername();
                String orderId = username + "_" + currentDateTime.toString();
                BigDecimal amount = new BigDecimal(total);
                String payUrl = momoPaymentService.initiatePayment(amount, orderId, baseUrl);

                bill.setTotal(total);
                bill.setId_user(id_user);
                bill.setState(2);
                bill.setBill_detailSet(set);
                int id = billService.save(0, bill);
                billService.findById(id).get().setName(orderId + "_" + String.valueOf(id));
                billService.findById(id).get().setInformation(orderId + "_" + String.valueOf(id));

                return ResponseEntity.status(HttpStatus.OK).body(payUrl);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @PostMapping("/momo-payment_save")
    public ResponseEntity<?> momoSave(@RequestParam(value = "orderId") String orderId,
                                      @RequestParam(value = "amount") Integer amount) {
        String[] split = orderId.split("_");
        String username = split[0];
        String time = split[1];
        Timestamp time_convert = Timestamp.valueOf(time);
        int id_bill = Integer.parseInt(split[2]);
        if (billService.findById(id_bill).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
        } else if (userService.findByUsername(username).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        } else {
            Bill bill = billService.findById(id_bill).get();
            bill.setState(0);
            bill.setUpdated_at(time_convert);
            return ResponseEntity.status(HttpStatus.OK).body(username);
        }
    }

    @GetMapping("/get-count")
    public ResponseEntity<?> getCount() {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (cartService.findById(id).isPresent()) {
                CartResponse b = new CartResponse(cartService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(b);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id cart not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<Cart> list = cartService.findAll();
            List<CartResponse> response = list.stream().map(cart -> cart != null ? new CartResponse(cart) : null)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-by-id-user/{id_user}")
    public ResponseEntity<?> GetAllByIdUser(@PathVariable("id_user") Integer id_user) {
        try {
            if (userService.findById(id_user).isPresent()) {
                List<Cart> carts = cartService.GetAllByIdUser(id_user);
                List<CartResponse> responses = new ArrayList<>();
                for (Cart c : carts) {
                    responses.add(new CartResponse(c));
                }
                return ResponseEntity.status(HttpStatus.OK).body(responses);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeCart(@Valid @RequestBody CartRequest request,
                                         @PathVariable("id") Integer id) {
        try {
            if (cartService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id cart not found ");
            }
            if (userService.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found ");
            }
            if (productService.findById(request.getId_product()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
            cartService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/change-quantity/{id}")
    private ResponseEntity<?> changeQuantity(@PathVariable("id") Integer id,
                                             @RequestParam(value = "quantity", required = true) Integer quantity) {
        try {
            if (cartService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id cart not found");
            } else if (quantity < 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quantity must be greater than 0");
            } else if (quantity == 0) {
                boolean checkDelete = cartService.deleteById(id);
                if (!checkDelete) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can't delete");
                }
                return ResponseEntity.accepted().build();
            } else {
                cartService.changeQuantity(id, quantity);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addCart(@RequestBody CartRequest request) {
        try {
            if (userService.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found ");
            }
            if (productService.findById(request.getId_product()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
            cartService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (cartService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id cart not found");
            } else {
                boolean checkDelete = cartService.deleteById(id);
                if (!checkDelete) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can't delete");
                }
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}