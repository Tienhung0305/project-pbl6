package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.CommentRequest;
import com.example.sportstore06.dao.response.CommentResponse;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.model.Comment;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.User;
import com.example.sportstore06.repository.ICommentRepository;
import com.example.sportstore06.repository.IProductRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ICommentRepository commentRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    public ResponseEntity<?> findById(int id) {
        if (commentRepository.findById(id).isPresent()) {
            Comment comment = commentRepository.findById(id).get();
            Comment reply = null;
            return ResponseEntity.ok(new CommentResponse(comment, reply));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id comment not found");
    }

    public ResponseEntity<?> findByPage(Pageable pageable) {
        Page<Comment> byPage = commentRepository.findByPage(pageable);
        Page<CommentResponse> commentResponses = byPage.map(comment -> new CommentResponse(comment, commentRepository.findByReply(comment.getId()).orElse(null)));
        return ResponseEntity.ok().body(commentResponses);
    }

    public ResponseEntity<?> save(CommentRequest request) {
        try {
            if (productRepository.findById(request.getId_product()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found ");
            } else if (userRepository.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            } else if (commentRepository.findById(request.getId()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id reply not found");
            } else {
                Timestamp created_at;
                Timestamp updated_at;
                if (commentRepository.findById(request.getId_user()).isPresent()) {
                    created_at = commentRepository.findById(request.getId_user()).get().getCreated_at();
                    updated_at = new Timestamp(new Date().getTime());
                } else {
                    created_at = new Timestamp(new Date().getTime());
                    updated_at = created_at;
                }
                var c = Comment.builder().
                        id(request.getId()).
                        product(productRepository.findById(request.getId_product()).get()).
                        content(request.getContent()).
                        report(request.getReport()).
                        reply(request.getReply()).
                        user(userRepository.findById(request.getId_user()).get()).
                        created_at(created_at).
                        updated_at(updated_at).
                        build();
                commentRepository.save(c);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> deleteById(int id) {
        try {
            if (commentRepository.findById(id).isPresent()) {
                commentRepository.deleteById(id);
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id comment not found");
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
