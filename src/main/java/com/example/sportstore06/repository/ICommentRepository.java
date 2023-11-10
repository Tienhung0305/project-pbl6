package com.example.sportstore06.repository;

import com.example.sportstore06.model.Comment;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment,Integer> {
    @Query("SELECT i FROM Comment i")
    Page<Comment> findByPage(Pageable pageable);
    Optional<Comment> findByReply(Integer reply);
}
