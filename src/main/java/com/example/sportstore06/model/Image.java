package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotBlank
    private String url;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_category", referencedColumnName = "id", unique = true)
    @JsonBackReference
    private Category category;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "id", unique = true)
    @JsonBackReference
    private User user;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_business", referencedColumnName = "id", unique = true)
    @JsonBackReference
    private Business business;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    @JsonBackReference
    private Product product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_comment", referencedColumnName = "id")
    @JsonBackReference
    private Comment comment;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sale", referencedColumnName = "id", unique = true)
    @JsonBackReference
    private Sale sale;
}
