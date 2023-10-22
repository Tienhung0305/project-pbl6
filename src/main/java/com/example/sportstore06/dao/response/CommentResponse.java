package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Comment;
import com.example.sportstore06.repository.ICommentRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private int id;
    private Integer id_product;
    private String content;
    private Boolean report;
    private Integer id_reply;
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;

    public CommentResponse(Comment comment, Comment reply) {
        this.id = comment.getId();
        this.id_product = comment.getProduct().getId();
        this.content = comment.getContent();
        this.report = comment.getReport();
        this.id_user = comment.getUser().getId();
        this.created_at = comment.getCreated_at();
        this.updated_at = comment.getUpdated_at();
        this.id_reply = reply != null ? reply.getId() : null;
    }
}
