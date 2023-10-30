package com.example.sportstore06.service;


import com.example.sportstore06.dao.request.CommentRequest;
import com.example.sportstore06.model.Comment;
import com.example.sportstore06.repository.ICommentRepository;
import com.example.sportstore06.repository.IProductRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ICommentRepository commentRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }

    public Page<Comment> findByPage(Pageable pageable) {
        return commentRepository.findByPage(pageable);
    }

    public boolean deleteById(int id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Optional<Comment> findByReply(Integer reply) {
        return commentRepository.findByReply(reply);
    }
    public void save(int id, CommentRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (commentRepository.findById(id).isPresent()) {
            created_at = commentRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }
        var c = Comment.builder().
                id(id).
                product(productRepository.findById(request.getId_product()).get()).
                content(request.getContent()).
                report(request.getReport()).
                reply(request.getReply() == null ? null : request.getReply()).
                user(userRepository.findById(request.getId_user()).get()).
                created_at(created_at).
                updated_at(updated_at).
                build();
        commentRepository.save(c);
    }
}


