package com.example.sportstore06.controller;

import com.example.sportstore06.dao.CartBusiness;
import com.example.sportstore06.dao.request.BillDetailRequest;
import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.dao.request.CartRequest;
import com.example.sportstore06.dao.response.CartResponse;
import com.example.sportstore06.entity.*;
import com.example.sportstore06.service.*;
import com.example.sportstore06.service.MomoService.MomoPaymentService;
import com.example.sportstore06.service.MomoService.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
    private final BillService billService;
    private final RoleService roleService;
    private final MomoPaymentService momoPaymentService;
    private final TransactionService transactionService;

    @PostMapping("/buy-with-momo")
    public ResponseEntity<?> PayWithMomo(
            @RequestBody(required = true) Set<Integer> set_id_cart,
            @RequestParam(required = true) String requestType,
            @RequestParam(required = false) Optional<String> redirectUrl,
            @AuthenticationPrincipal User user,
            HttpServletRequest request) {
        String baseUrl = UrlUtil.getBaseUrl(request);
        String payUrl = "";
        if (!requestType.equals("payWithATM") && !requestType.equals("captureWallet")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("request type not found");
        }
        Set<Cart> set_cart = new HashSet<>();
        Set<Business> set_business = new HashSet<>();
        Set<Integer> set_id_bill = new HashSet<>();
        Set<CartBusiness> set_cart_business = new HashSet<>();
        for (Integer id_cart : set_id_cart) {
            Optional<Cart> Op = cartService.findById(id_cart);
            if (Op.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id cart not found");
            }
            if (Op.get().getUser().getId() != user.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you don't have the authority to buy");
            }
            set_cart.add(Op.get());
            set_business.add(Op.get().getProduct().getProductInfo().getBusiness());
        }

        set_cart_business = set_business.stream().map(business -> {
            Set<Cart> cartSet = set_cart.stream()
                    .filter(cart -> cart.getProduct().getProductInfo().getBusiness().getId() == business.getId())
                    .collect(Collectors.toSet());
            return new CartBusiness(business, cartSet);
        }).collect(Collectors.toSet());

        long total_all = 0;
        for (CartBusiness cartBusiness : set_cart_business) {
            long total = 0;
            long price = 0;
            BillRequest bill = new BillRequest();
            Set<BillDetailRequest> set = new HashSet<>();
            for (Cart cart : cartBusiness.getCartSet()) {
                //create bill detail
                BillDetailRequest bill_detail = new BillDetailRequest();
                bill_detail.setId_product(cart.getProduct().getId());
                bill_detail.setQuantity(cart.getQuantity());
                Sale sale = cart.getProduct().getProductInfo().getSale();
                Timestamp time_now = new Timestamp(new Date().getTime());
                if (sale != null && time_now.after(sale.getStarted_at()) && time_now.before(sale.getEnded_at())) {
                    price = cart.getProduct().getPrice() * cart.getQuantity() * (100 - sale.getDiscount()) / 100;
                    total = total + price;
                } else {
                    price = cart.getProduct().getPrice() * cart.getQuantity();
                    total = total + price;
                }
                bill_detail.setPrice(price);
                set.add(bill_detail);

                //remove cart
                cartService.deleteById(cart.getId());
            }

            bill.setTotal(total);

            bill.setId_business(cartBusiness.getBusiness().getId());
            bill.setId_user(user.getId());
            bill.setBill_detailSet(set);
            bill.setName("Business :" + cartBusiness.getBusiness().getName());
            bill.setInformation(
                    "Address : " + user.getAddress() + " | " +
                            "Total : " + String.valueOf(total) + " | "
            );
            int id = billService.save(0, bill, 2);
            set_id_bill.add(id);
            total_all += total;
        }

        Transaction transaction_temp = momoPaymentService.initiatePayment(total_all, set_id_bill, redirectUrl, requestType);
        Transaction transaction = Transaction
                .builder()
                .id(Integer.parseInt(transaction_temp.getOrderId()))
                .orderId(transaction_temp.getOrderId())
                .requestId(transaction_temp.getRequestId())
                .payUrl(transaction_temp.getPayUrl())
                .build();
        transactionService.save(transaction);
        for (Integer id : set_id_bill) {
            Bill bill = billService.findById(id).get();
            Optional<Transaction> Ob = transactionService.findById(Integer.parseInt(transaction.getOrderId()));
            if (Ob.isPresent()) {
                bill.setTransaction(Ob.get());
                billService.save(bill);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id transaction not found");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(transaction.getPayUrl());
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
                                         @PathVariable("id") Integer id,
                                         @AuthenticationPrincipal User user) {
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

            //check role
            Role role_admin = roleService.findByName("ROLE_ADMIN").get();
            if (!user.getRoleSet().contains(role_admin)) {
                if (user.getId() != id) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you don't have the authority to edit");
                }
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
                if (quantity > cartService.findById(id).get().getProduct().getQuantity()) {
                    quantity = cartService.findById(id).get().getProduct().getQuantity();
                }
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