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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final BusinessService businessService;
    private final ProductService productService;
    private final BillService billService;
    private final RoleService roleService;
    private final MomoPaymentService momoPaymentService;

    @PostMapping("/momo")
    public ResponseEntity<?> momo(@RequestParam(value = "id_user", required = true) Integer id_user,
                                  HttpServletRequest request) {
        try {
            if (userService.findById(id_user).isPresent()) {
                String baseUrl = UrlUtil.getBaseUrl(request);
                List<Cart> carts = cartService.GetAllByIdUser(id_user);
                double total = 0;
                double price = 0;
                if (!carts.isEmpty()) {
                    Set<BillDetailRequest> set = new HashSet<>();
                    BillRequest bill = new BillRequest();
                    for (Cart cart : carts) {
                        if (cart.getProduct().getProductInfo().getState() != 0 || cart.getProduct().getQuantity() <= 0) {
                            cartService.deleteById(cart.getId());
                            //409
                            return ResponseEntity.status(HttpStatus.GONE).body("product does not exist or has been deleted");
                        }
                        BillDetailRequest bill_detail = new BillDetailRequest();

                        bill_detail.setId_product(cart.getProduct().getId());
                        bill_detail.setQuantity(cart.getQuantity());

                        Sale sale = cart.getProduct().getProductInfo().getSale();
                        if (sale != null) {
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
                    bill.setBill_detailSet(set);
                    int id = billService.save(0, bill, 2);
                    billService.findById(id).get().setName(orderId + "_" + String.valueOf(id));
                    billService.findById(id).get().setInformation(orderId + "_" + String.valueOf(id));

                    return ResponseEntity.status(HttpStatus.OK).body(payUrl);
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cart is empty");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id user not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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