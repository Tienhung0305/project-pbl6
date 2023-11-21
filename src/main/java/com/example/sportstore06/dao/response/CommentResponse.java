package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Comment;
import com.example.sportstore06.model.Image;
import com.example.sportstore06.repository.ICommentRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private int id;
    private Integer id_product;
    private String content;
    private Boolean report;
    private Set<Integer> SetReply;
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Set<Image> imageSet = new HashSet<>();

    public CommentResponse(Comment comment, List<Comment> commentList) {
        this.id = comment.getId();
        this.id_product = comment.getProduct().getId();
        this.content = comment.getContent();
        this.report = comment.getReport();
        this.id_user = comment.getUser().getId();
        this.created_at = comment.getCreated_at();
        this.updated_at = comment.getUpdated_at();
        this.SetReply = commentList.stream().map(c -> c.getId()).collect(Collectors.toSet());
        this.imageSet = comment.getImageSet();
    }
}
