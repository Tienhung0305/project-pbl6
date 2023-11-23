package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.BusinessRequest;
import com.example.sportstore06.dao.request.CartRequest;
import com.example.sportstore06.dao.response.*;
import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.Cart;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.service.BusinessService;
import com.example.sportstore06.service.CartService;
import com.example.sportstore06.service.ProductService;
import com.example.sportstore06.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
    private final BusinessService businessService;

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
            List<CartResponse> response = list.stream().map(cart -> new CartResponse(cart)).collect(Collectors.toList());
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
                Set<ProductCartResponse> products = new HashSet<>();
                Set<Business> businesses = new HashSet<>();
                Set<BusinessCartResponse> response = new HashSet<>();
                for (Cart cart : carts) {
                    businesses.add(businessService.findById(cart.getProduct().getId()).get());
                    ProductCartResponse productCartResponse = new ProductCartResponse(cart);
                    products.add(productCartResponse);
                }
                for (Business b : businesses) {
                    Set<ProductCartResponse> products_business = products.stream().filter(
                            product -> product.getProduct().getId_business() == b.getId()).collect(Collectors.toSet());
                    BusinessCartResponse businessCartResponse = new BusinessCartResponse(b,products_business);
                    response.add(businessCartResponse);
                }
                return ResponseEntity.status(HttpStatus.OK).body("OK");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addCart(@Valid @RequestBody CartRequest request) {
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
