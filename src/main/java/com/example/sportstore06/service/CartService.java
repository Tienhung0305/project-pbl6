package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.CartRequest;
import com.example.sportstore06.model.Cart;
import com.example.sportstore06.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    public Long getCount() {
        return cartRepository.count();
    }
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }
    public Optional<Cart> findById(int id) {
        return cartRepository.findById(id);
    }
    public List<Cart> GetAllByIdUser(Integer id) {
        return cartRepository.GetAllByIdUser(id);
    }
    public boolean deleteById(int id) {
        try {
            cartRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void save(int id, CartRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (cartRepository.findById(id).isPresent()) {
            created_at = cartRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }

        var c = Cart.builder().
                id(id).
                user(userRepository.findById(request.getId_user()).get()).
                product(productRepository.findById(request.getId_product()).get()).
                quantity(request.getQuantity()).
                created_at(created_at).
                updated_at(updated_at).
                build();
        cartRepository.save(c);
    }

    public void changeQuantity(int id, int quantity) {
        Cart cart = cartRepository.findById(id).get();
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }
}
