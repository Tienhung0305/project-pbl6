package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.CartRequest;
import com.example.sportstore06.model.Cart;
import com.example.sportstore06.model.Product;
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
    private final ISizeProductRepository sizRepository;

    public Long getCount() {
        return cartRepository.count();
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Optional<Cart> findById(int id) {
        return cartRepository.findById(id);
    }

    Optional<Cart> FindByIdUserAndIdSize(Integer id_user, Integer id_size) {
        return cartRepository.FindByIdUserAndIdSize(id_user, id_size);
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

        Optional<Cart> ObCart = cartRepository.FindByIdUserAndIdSize(request.getId_user(), request.getId_size());
        Integer q = request.getQuantity();
        int id_cart = id;
        if (ObCart.isPresent() && id == 0) {
            q = ObCart.get().getQuantity() + request.getQuantity();
            id_cart = ObCart.get().getId();
        }
        if (q >= sizRepository.findById(request.getId_size()).get().getQuantity()) {
            q = sizRepository.findById(request.getId_size()).get().getQuantity();
        }

        var c = Cart.builder().
                id(id_cart).
                user(userRepository.findById(request.getId_user()).get()).
                size(sizRepository.findById(request.getId_size()).get()).
                quantity(q).
                created_at(created_at).
                updated_at(updated_at).
                build();
        cartRepository.save(c);
    }

    public void changeQuantity(int id, int quantity) {
        Cart cart = cartRepository.findById(id).get();
        Integer q = quantity;
        if (q >= cart.getSize().getQuantity()) {
            q = cart.getSize().getQuantity();
        }
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }
}
