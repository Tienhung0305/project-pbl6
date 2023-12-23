package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoResponse {
    private int id;
    private String name;
    private String detail;
    private String brand;
    private String attribute;
    private BusinessResponse business;
    private SaleResponse sale;
    private Set<CategoryResponse> categorySet;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Set<ImageResponse> imageSet;
    private Integer state;
    private Double price_min;
    private Integer number_like;
    private Integer number_dislike;
    private Integer number_comment;
    private Set<ProductResponse> productSet;

    public ProductInfoResponse(ProductInfo productInfo) {

        this.id = productInfo.getId();
        this.name = productInfo.getName();
        this.detail = productInfo.getDetail();
        Set<Category> categorySet = productInfo.getCategorySet();
        for (Category category : categorySet) {
            if (category.getCategoryGroup().getId() == 5) {
                this.brand = category.getName();
            }
        }
        this.attribute = productInfo.getAttribute();
        this.business = new BusinessResponse(productInfo.getBusiness());
        this.sale = productInfo.getSale() != null ? new SaleResponse(productInfo.getSale()) : null;
        this.categorySet = categorySet
                .stream()
                .map(category -> category != null ? new CategoryResponse(category) : null)
                .collect(Collectors.toSet());
        this.created_at = productInfo.getCreated_at();
        this.updated_at = productInfo.getUpdated_at();
        this.state = productInfo.getState();
        this.imageSet = productInfo
                .getImageSet()
                .stream()
                .map(image -> image != null ? new ImageResponse(image) : null)
                .collect(Collectors.toSet());
        this.productSet = productInfo.getProductSet()
                .stream()
                .map(product -> product != null ? new ProductResponse(product) : null)
                .collect(Collectors.toSet());

        List<Double> l = new ArrayList<>();
        productInfo.getProductSet().forEach(product -> l.add(product.getPrice()));
        this.price_min = l.isEmpty() ? null : Collections.min(l);

        Integer number_like = 0;
        Integer number_dislike = 0;
        List<Comment> commentList = productInfo.getCommentSet().stream().toList();

        for (Comment c : commentList) {
            if (c.getIs_like()) {
                number_like += 1;
            } else {
                number_dislike += 1;
            }
        }
        this.number_comment = commentList.size();
        this.number_like = number_like;
        this.number_dislike = number_dislike;
    }
}
