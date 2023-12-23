package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.Comment;
import com.example.sportstore06.entity.Image;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private int id;
    private Integer id_product_info;
    private String content;
    private Boolean report;
    private Set<Integer> SetReply;
    private Integer id_user;
    private Boolean is_like;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Set<Image> imageSet = new HashSet<>();

    public CommentResponse(Comment comment, List<Comment> commentList) {
        this.id = comment.getId();
        this.id_product_info = comment.getProductInfo().getId();
        this.content = comment.getContent();
        this.report = comment.getReport();
        this.is_like = comment.getIs_like();
        this.id_user = comment.getUser().getId();
        this.created_at = comment.getCreated_at();
        this.updated_at = comment.getUpdated_at();
        this.SetReply = commentList.stream()
                .map(c -> c != null ? c.getId() : null)
                .collect(Collectors.toSet());
        this.imageSet = comment.getImageSet();
    }
}
