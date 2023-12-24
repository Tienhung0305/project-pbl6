package com.example.sportstore06.repository;

import com.example.sportstore06.entity.Bill;
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

    // thong ke
    //admin
    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b")
    double getAllTotal();

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b " +
            "WHERE b.updated_at >= :startDate AND b.updated_at <= :endDate")
    double getAllTotal(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(b) FROM Bill b")
    double getAllCount();

    @Query("SELECT COUNT(b) FROM Bill b " +
            "WHERE b.updated_at >= :startDate AND b.updated_at <= :endDate")
    double getAllCount(@Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate);

    // add state

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.state =:state")
    double getAllTotal(@Param("state") Integer state);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b " +
            "WHERE b.updated_at >= :startDate AND b.updated_at <= :endDate AND b.state =:state")
    double getAllTotal(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("state") Integer state);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.state =:state")
    double getAllCount(@Param("state") Integer state);

    @Query("SELECT COUNT(b) FROM Bill b " +
            "WHERE b.updated_at >= :startDate AND b.updated_at <= :endDate AND b.state =:state")
    double getAllCount(@Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate,
                       @Param("state") Integer state);

    //business

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE  b.id_business = :idBusiness")
    double getAllTotalBusiness(@Param("idBusiness") Integer idBusiness);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b " +
            "WHERE b.updated_at >= :startDate AND b.updated_at <= :endDate AND b.id_business = :idBusiness")
    double getAllTotalBusiness(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("idBusiness") Integer idBusiness);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.id_business = :idBusiness")
    double getAllCountBusiness(@Param("idBusiness") Integer idBusiness);

    @Query("SELECT COUNT(b) FROM Bill b " +
            "WHERE b.updated_at >= :startDate AND b.updated_at <= :endDate AND b.id_business = :idBusiness")
    double getAllCountBusiness(@Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate,
                       @Param("idBusiness") Integer idBusiness);

    // add state

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.state =:state AND b.id_business = :idBusiness")
    double getAllTotalBusiness(@Param("state") Integer state,
                       @Param("idBusiness") Integer idBusiness);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b " +
            "WHERE b.updated_at >= :startDate AND b.updated_at <= :endDate AND b.state =:state AND b.id_business = :idBusiness")
    double getAllTotalBusiness(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("state") Integer state,
            @Param("idBusiness") Integer idBusiness);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.state =:state AND b.id_business = :idBusiness")
    double getAllCountBusiness(@Param("state") Integer state,
                       @Param("idBusiness") Integer idBusiness);

    @Query("SELECT COUNT(b) FROM Bill b " +
            "WHERE b.updated_at >= :startDate AND b.updated_at <= :endDate AND b.state =:state and b.id_business = :idBusiness")
    double getAllCountBusiness(@Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate,
                       @Param("state") Integer state,
                       @Param("idBusiness") Integer idBusiness);

    @Query("SELECT MIN(b.updated_at) FROM Bill b")
    Timestamp getEarliestUpdateAt();

    @Query("SELECT MAX(b.updated_at) FROM Bill b")
    Timestamp getLatestUpdateAt();

}
