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
    //private int id_bill;
    @NotBlank
    @Length(min = 1, max = 200)
    private String content;
    @Column(columnDefinition = "boolean")
    @NotNull
    private Boolean report;
    @Column(columnDefinition = "boolean")
    @NotNull
    private Boolean is_like;
    private Integer reply;
    //private int id_user;
    private Timestamp created_at;
    private Timestamp updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product_information", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private ProductInfo productInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bill", nullable = false, unique = false ,referencedColumnName = "id")
    @JsonBackReference
    private Bill bill;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Image> imageSet = new HashSet<>();

}
