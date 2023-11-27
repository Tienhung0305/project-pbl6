package com.example.sportstore06.repository;

import com.example.sportstore06.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT i FROM Comment i")
    Page<Comment> findByPage(Pageable pageable);
    @Query("SELECT i FROM Comment i WHERE i.productInfo.id = :id_product_information")
    Page<Comment> findByProductInfo(Pageable pageable, Integer id_product_information);
    List<Comment> findByReply(Integer reply);
}
