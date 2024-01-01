package com.example.sportstore06.repository;

import com.example.sportstore06.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Integer> {
    @Query("SELECT i FROM Sale i WHERE i.business.user.state = 0")
    Page<Sale> findByPage(Pageable pageable);

    @Query("SELECT i FROM Sale i WHERE i.name LIKE %:Name% AND i.business.user.state = 0")
    Page<Sale> SearchByName(Pageable pageable, String Name);

    @Query("SELECT i FROM Sale i WHERE i.business.id = :id_business")
    Page<Sale> findByIdBusiness(Pageable pageable, Integer id_business);

    @Query("SELECT i FROM Sale i WHERE i.discount >= :discount_min AND i.discount <= :discount_max AND  i.business.user.state = 0")
    Page<Sale> getByDiscount(Pageable pageable, @Param("discount_min") Long discount_min, @Param("discount_max") Long discount_max);

    @Query("SELECT i FROM Sale i WHERE i.discount >= :discount_min AND i.discount <= :discount_max AND i.business.id = :id_business AND  i.business.user.state = 0")
    Page<Sale> getByDiscount(Pageable pageable, @Param("discount_min") Long discount_min, @Param("discount_max") Long discount_max, @Param("id_business") Integer id_business);

    @Query("SELECT MAX(i.discount) FROM Sale i WHERE i.business.user.state = 0")
    Long getMaxDiscount();

    @Query("SELECT MIN(i.discount) FROM Sale i WHERE i.business.user.state = 0")
    Long getMinDiscount();
}
