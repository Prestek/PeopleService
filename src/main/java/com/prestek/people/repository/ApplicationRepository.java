package com.prestek.people.repository;

import com.prestek.people.model.Application;
import com.prestek.people.model.Application.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    List<Application> findByUserId(String userId);
    
    List<Application> findByCreditOfferId(Long creditOfferId);
    
    List<Application> findByStatus(ApplicationStatus status);
    
    @Query("SELECT a FROM Application a WHERE a.user.id = :userId AND a.status = :status")
    List<Application> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ApplicationStatus status);
    
    @Query("SELECT a FROM Application a WHERE a.creditOffer.id = :creditOfferId AND a.status = :status")
    List<Application> findByCreditOfferIdAndStatus(@Param("creditOfferId") Long creditOfferId, @Param("status") ApplicationStatus status);
    
    @Query("SELECT COUNT(a) FROM Application a WHERE a.user.id = :userId")
    Long countByUserId(@Param("userId") String userId);
}