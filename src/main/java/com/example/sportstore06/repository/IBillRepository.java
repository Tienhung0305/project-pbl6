package com.example.sportstore06.repository;

import com.example.sportstore06.entity.Bill;
import com.example.sportstore06.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT i FROM Bill i")
    Page<Bill> findByPage(Pageable pageable);
    @Query("SELECT i FROM Bill i WHERE i.state = :state")
    Page<Bill> findByPage(Pageable pageable, Integer state);
    @Query("SELECT i FROM Bill i WHERE i.name LIKE %:Name%")
    Page<Bill> SearchByName(Pageable pageable, String Name);
    @Query("SELECT i FROM Bill i WHERE i.name LIKE %:Name% AND i.state = :state")
    Page<Bill> SearchByName(Pageable pageable, String Name, Integer state);
    @Query("SELECT i FROM Bill i WHERE i.user.id = :id_user")
    List<Bill> findByIdUser(Integer id_user);

    @Query("SELECT i FROM Bill i WHERE i.user.id = :id_user AND i.state = :state")
    List<Bill> findByIdUser(Integer id_user, Integer state);

    @Query("SELECT i FROM Bill i WHERE i.id_business = :id_business")
    Page<Bill> findByIdBusiness(Pageable pageable, Integer id_business);

    @Query("SELECT i FROM Bill i WHERE i.transaction.id = :transaction_id")
    List<Bill> findByTransactionId(Integer transaction_id);

    @Query("SELECT i FROM Bill i WHERE i.id_business = :id_business AND i.state = :state")
    Page<Bill> findByIdBusiness(Pageable pageable, Integer id_business, Integer state);

    // thong ke
    //admin
    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b")
    Long getAllTotal();

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b " +
            "WHERE DATE(b.updated_at) >= :startDate AND DATE(b.updated_at) <= :endDate")
    Long getAllTotal(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(b) FROM Bill b")
    Long getAllCount();

    @Query("SELECT COUNT(b) FROM Bill b " +
            "WHERE DATE(b.updated_at) >= :startDate AND DATE(b.updated_at) <= :endDate")
    Long getAllCount(@Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate);

    // add state

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.state =:state")
    Long getAllTotal(@Param("state") Integer state);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b " +
            "WHERE DATE(b.updated_at) >= :startDate AND DATE(b.updated_at) <= :endDate AND b.state =:state")
    Long getAllTotal(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("state") Integer state);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.state =:state")
    Long getAllCount(@Param("state") Integer state);

    @Query("SELECT COUNT(b) FROM Bill b " +
            "WHERE DATE(b.updated_at) >= :startDate AND DATE(b.updated_at) <= :endDate AND b.state =:state")
    Long getAllCount(@Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate,
                       @Param("state") Integer state);

    //business

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE  b.id_business = :idBusiness")
    Long getAllTotalBusiness(@Param("idBusiness") Integer idBusiness);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b " +
            "WHERE DATE(b.updated_at) >= :startDate AND DATE(b.updated_at) <= :endDate AND b.id_business = :idBusiness")
    Long getAllTotalBusiness(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("idBusiness") Integer idBusiness);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.id_business = :idBusiness")
    Long getAllCountBusiness(@Param("idBusiness") Integer idBusiness);

    @Query("SELECT COUNT(b) FROM Bill b " +
            "WHERE DATE(b.updated_at) >= :startDate AND DATE(b.updated_at) <= :endDate AND b.id_business = :idBusiness")
    Long getAllCountBusiness(@Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate,
                       @Param("idBusiness") Integer idBusiness);

    // add state

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.state =:state AND b.id_business = :idBusiness")
    Long getAllTotalBusiness(@Param("state") Integer state,
                       @Param("idBusiness") Integer idBusiness);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b " +
            "WHERE DATE(b.updated_at) >= :startDate AND DATE(b.updated_at) <= :endDate AND b.state =:state AND b.id_business = :idBusiness")
    Long getAllTotalBusiness(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("state") Integer state,
            @Param("idBusiness") Integer idBusiness);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.state =:state AND b.id_business = :idBusiness")
    Long getAllCountBusiness(@Param("state") Integer state,
                       @Param("idBusiness") Integer idBusiness);

    @Query("SELECT COUNT(b) FROM Bill b " +
            "WHERE DATE(b.updated_at) >= :startDate AND DATE(b.updated_at) <= :endDate AND b.state =:state and b.id_business = :idBusiness")
    Long getAllCountBusiness(@Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate,
                       @Param("state") Integer state,
                       @Param("idBusiness") Integer idBusiness);

    @Query("SELECT MIN(b.updated_at) FROM Bill b")
    Timestamp getEarliestUpdateAt();

    @Query("SELECT MAX(b.updated_at) FROM Bill b")
    Timestamp getLatestUpdateAt();

    // most product
    @Query("SELECT bd.product.productInfo FROM BillDetail bd " +
            "GROUP BY bd.product " +
            "ORDER BY SUM(bd.quantity) DESC")
    Page<ProductInfo> findMostSoldProducts(Pageable pageable);

    @Query("SELECT bd.product.productInfo FROM BillDetail bd " +
            "WHERE bd.product.productInfo.state = :state " +
            "AND bd.product.productInfo.business.user.state = 0" +
            "GROUP BY bd.product " +
            "ORDER BY SUM(bd.quantity) DESC")
    Page<ProductInfo> findMostSoldProducts(Pageable pageable, Integer state);

    @Query("SELECT bd.product.productInfo FROM BillDetail bd " +
            "WHERE bd.product.productInfo.business.id = :id_business " +
            "AND bd.product.productInfo.business.user.state = 0" +
            "GROUP BY bd.product " +
            "ORDER BY SUM(bd.quantity) DESC")
    Page<ProductInfo> findMostSoldProductsBusiness(Pageable pageable, Integer id_business);

    @Query("SELECT bd.product.productInfo FROM BillDetail bd " +
            "WHERE bd.product.productInfo.business.id = :id_business " +
            "AND bd.product.productInfo.state = :state " +
            "AND bd.product.productInfo.business.user.state = 0" +
            "GROUP BY bd.product " +
            "ORDER BY SUM(bd.quantity) DESC")
    Page<ProductInfo> findMostSoldProductsBusiness(Pageable pageable, Integer id_business, Integer state);
}
