package com.prestek.people.repository;

import com.prestek.people.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByDocumentNumber(String documentNumber);
    
    @Query("SELECT u FROM User u WHERE u.email = :email OR u.documentNumber = :documentNumber")
    Optional<User> findByEmailOrDocumentNumber(@Param("email") String email, @Param("documentNumber") String documentNumber);
    
    boolean existsByEmail(String email);
    
    boolean existsByDocumentNumber(String documentNumber);
}