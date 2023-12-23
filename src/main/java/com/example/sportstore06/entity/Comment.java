package com.example.sportstore06.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //private int id_product_information;
    @NotBlank
    @Length(min = 1, max = 200)
    private String content;
    @NotNull
    private Boolean report;
    private Boolean is_like;
    private Integer reply;
    //private int id_user;
    private Timestamp created_at;
    private Timestamp updated_at;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product_information", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private ProductInfo productInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<Image> imageSet = new HashSet<>();
}
