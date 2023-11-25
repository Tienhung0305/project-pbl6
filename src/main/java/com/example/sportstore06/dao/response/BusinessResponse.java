package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.ProductInfo;
import com.example.sportstore06.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessResponse {
    private Integer id;
    private String name;
    private String about;
    private Integer tax;
    private String payment;
    private Set<Integer> id_productInforeSet;
    private Integer count_comment;
    private Integer count_comment_like;
    private Integer count_comment_dislike;
    private Timestamp time_start;
    private Timestamp count_product;

    public BusinessResponse(Business business) {
        this.id = business.getId();
        this.name = business.getName();
        this.about = business.getAbout();
        this.tax = business.getTax();
        this.payment = business.getPayment();
        this.id_productInforeSet = business.getProductInfoSet()
                .stream()
                .map(productInfor -> productInfor != null ? productInfor.getId() : null)
                .collect(Collectors.toSet());
        Integer count_comment = 0;
        Integer count_comment_like = 0;
        Integer count_comment_dislike = 0;
        Integer count_product = 0;
        Set<ProductInfo> productInfoSet = business.getProductInfoSet();
        for (ProductInfo p : productInfoSet) {
            count_product = count_product + 1;
            for (Comment c : p.getCommentSet()) {
                if (c.getReply()!=null) {
                    if (c.getIs_like()) {
                        count_comment_like = count_comment_like + 1;
                    }
                    if (!c.getIs_like()) {
                        count_comment_dislike = count_comment_dislike + 1;
                    }
                    count_comment = count_comment + 1;
                }
            }
        }
        this.count_comment_like = count_comment_like;
        this.count_comment_dislike = count_comment_dislike;
        this.count_comment = count_comment;

        this.time_start = business.getUser().getCreated_at();
    }
}
