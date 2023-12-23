package com.example.sportstore06.repository;

import com.example.sportstore06.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT i FROM Cart i WHERE i.user.id = :id_user")
    List<Cart> GetAllByIdUser(Integer id_user);
    @Query("SELECT i FROM Cart i WHERE i.user.id = :id_user AND i.product.id = :id_product")
    Optional<Cart> FindByIdUserAndIdProduct(Integer id_user, Integer id_product);
}
