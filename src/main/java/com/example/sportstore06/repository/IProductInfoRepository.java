package com.example.sportstore06.repository;

import com.example.sportstore06.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductInfoRepository extends JpaRepository<ProductInfo,Integer> {
    @Query("SELECT i FROM ProductInfo i")
    Page<ProductInfo> findByPage(Pageable pageable);
    @Query("SELECT i FROM ProductInfo i WHERE i.state = :state")
    Page<ProductInfo> findByPage(Pageable pageable, Integer state);
    @Query("SELECT i FROM ProductInfo i WHERE i.state = :state AND i.business.user.state = :state_business")
    Page<ProductInfo> findByPage(Pageable pageable, Integer state, Integer state_business);
    @Query("SELECT i FROM ProductInfo i JOIN i.categorySet c WHERE c.id IN (:categoryIds)")
    Page<ProductInfo> findByCategory(Pageable pageable, List<Integer> categoryIds);
    @Query("SELECT i FROM ProductInfo i JOIN i.categorySet c WHERE i.state = :state AND c.id IN (:categoryIds)")
    Page<ProductInfo> findByCategory(Pageable pageable, Integer state, List<Integer> categoryIds);
    @Query("SELECT i FROM ProductInfo i JOIN i.categorySet c WHERE i.state = :state AND c.id IN (:categoryIds) AND i.business.user.state = :state_business")
    Page<ProductInfo> findByCategory(Pageable pageable, Integer state, List<Integer> categoryIds, Integer state_business);
    @Query("SELECT i FROM ProductInfo i WHERE i.state = :state AND i.business.id = :id_business")
    Page<ProductInfo> findByBusiness(Pageable pageable, Integer state, Integer id_business);
    @Query("SELECT i FROM ProductInfo i WHERE i.business.id = :id_business")
    Page<ProductInfo> findByBusiness(Pageable pageable, Integer id_business);
    @Query("SELECT i FROM ProductInfo i WHERE i.state = :state AND i.sale.id = :id_sale")
    Page<ProductInfo> findBySale(Pageable pageable, Integer state, Integer id_sale);
    @Query("SELECT i FROM ProductInfo i WHERE i.sale.id = :id_sale")
    Page<ProductInfo> findBySale(Pageable pageable, Integer id_sale);
    @Query("SELECT i FROM ProductInfo i WHERE i.sale.id = :id_sale AND i.business.user.state = :state_business")
    Page<ProductInfo> findBySale(Pageable pageable, Integer id_sale, Integer state, Integer state_business);
    @Query("SELECT i FROM ProductInfo i WHERE i.sale.discount = :discount")
    Page<ProductInfo> findBySaleDiscount(Pageable pageable, Long discount);
    @Query("SELECT i FROM ProductInfo i WHERE i.state = :state AND i.sale.discount = :discount")
    Page<ProductInfo> findBySaleDiscount(Pageable pageable, Integer state, Long discount);
    @Query("SELECT i FROM ProductInfo i WHERE i.sale.discount = :discount AND i.business.user.state = :state_business")
    Page<ProductInfo> findBySaleDiscount(Pageable pageable, Integer state, Long discount, Integer state_business);
    @Query("SELECT i FROM ProductInfo i WHERE LOWER(i.name) LIKE %:Name%")
    Page<ProductInfo>SearchByName(Pageable pageable, String Name);
    @Query("SELECT i FROM ProductInfo i WHERE LOWER(i.name) LIKE %:Name% AND i.state = :state")
    Page<ProductInfo>SearchByName(Pageable pageable, String Name, Integer state);
    @Query("SELECT i FROM ProductInfo i WHERE LOWER(i.name) LIKE %:Name% AND i.state = :state AND i.business.user.state = :state_business")
    Page<ProductInfo>SearchByName(Pageable pageable, String Name, Integer state , Integer state_business);
    @Query("SELECT i FROM ProductInfo i WHERE LOWER(i.name) LIKE %:Name% AND i.business.id = :id_business")
    Page<ProductInfo>SearchByNameAndBusiness(Pageable pageable, String Name, Integer id_business);
    @Query("SELECT i FROM ProductInfo i WHERE LOWER(i.name) LIKE %:Name% AND i.state = :state AND i.business.id = :id_business")
    Page<ProductInfo>SearchByNameAndBusiness(Pageable pageable, String Name, Integer state, Integer id_business);
    @Query("SELECT i FROM ProductInfo i WHERE LOWER(i.name) LIKE %:Name% AND i.sale.id = :id_sale")
    Page<ProductInfo>SearchByNameAndSale(Pageable pageable, String Name, Integer id_sale);
    @Query("SELECT i FROM ProductInfo i WHERE LOWER(i.name) LIKE %:Name% AND i.state = :state AND i.sale.id = :id_sale")
    Page<ProductInfo>SearchByNameAndSale(Pageable pageable, String Name, Integer state, Integer id_sale);
    @Query("SELECT i FROM ProductInfo i WHERE LOWER(i.name) LIKE %:Name% AND i.state = :state AND i.sale.id = :id_sale AND i.business.user.state = :state_business")
    Page<ProductInfo>SearchByNameAndSale(Pageable pageable, String Name, Integer state, Integer id_sale, Integer state_business);

}
