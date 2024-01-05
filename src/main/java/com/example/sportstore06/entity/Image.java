package com.example.sportstore06.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Type;

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
    private Integer id;
    private String name;
<<<<<<< HEAD
=======
    @Column(columnDefinition = "boolean default false")
>>>>>>> cc691a0a6f175c61790dc0263343e61bee503bca
    private Boolean is_main;
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotBlank
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product_information", referencedColumnName = "id")
    @JsonBackReference
    private ProductInfo productInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comment", referencedColumnName = "id")
    @JsonBackReference
    private Comment comment;
}
