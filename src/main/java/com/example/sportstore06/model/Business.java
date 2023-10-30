package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "business")
public class Business {
    @Id
    private int id;
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 50, message = "name must be between 1 and 50 characters")
    private String name;
    private String about;
    private Integer tax;
    @NotNull(message = "state must not be null")
    @Min(value = 0, message = "state must is (0,1,2)")
    @Max(value = 3, message = "state must is (0,1,2)")
    private Integer state;
    private Timestamp created_at;
    private Timestamp updated_at;

    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<Product> productSet = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @OneToOne(mappedBy = "business", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Image image;
}
