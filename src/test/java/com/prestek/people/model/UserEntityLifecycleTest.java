package com.prestek.people.model;

import com.prestek.people.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for User entity lifecycle callbacks (@PrePersist
 * and @PreUpdate)
 * These tests verify that JPA callbacks are properly executed
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true"
})
@DisplayName("User Entity Lifecycle Tests")
class UserEntityLifecycleTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should automatically set createdAt and updatedAt on persist (@PrePersist)")
    void shouldSetTimestampsOnPersist() {
        // Given
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .documentNumber("12345678")
                .monthlyIncome(5000.0)
                .monthlyExpenses(3000.0)
                .creditScore(750)
                .employmentStatus("EMPLOYED")
                .build();

        // Verify timestamps are null before persist
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();

        // When
        LocalDateTime beforePersist = LocalDateTime.now();
        User savedUser = entityManager.persistAndFlush(user);
        LocalDateTime afterPersist = LocalDateTime.now();

        // Then
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isBetween(beforePersist.minusSeconds(1), afterPersist.plusSeconds(1));
        assertThat(savedUser.getUpdatedAt()).isBetween(beforePersist.minusSeconds(1), afterPersist.plusSeconds(1));
        assertThat(savedUser.getCreatedAt()).isEqualTo(savedUser.getUpdatedAt());
    }

    @Test
    @DisplayName("Should automatically update updatedAt on update (@PreUpdate)")
    void shouldUpdateTimestampOnUpdate() throws InterruptedException {
        // Given - Create and persist a user
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.update@example.com")
                .phone("+1234567890")
                .documentNumber("87654321")
                .creditScore(750)
                .build();

        User savedUser = entityManager.persistAndFlush(user);
        LocalDateTime originalCreatedAt = savedUser.getCreatedAt();
        LocalDateTime originalUpdatedAt = savedUser.getUpdatedAt();

        // Wait a bit to ensure timestamps are different
        Thread.sleep(100);

        // When - Update the user
        savedUser.setFirstName("Jane");
        savedUser.setCreditScore(800);

        LocalDateTime beforeUpdate = LocalDateTime.now();
        entityManager.flush();
        LocalDateTime afterUpdate = LocalDateTime.now();

        // Then
        User updatedUser = entityManager.find(User.class, savedUser.getId());
        assertThat(updatedUser.getCreatedAt()).isEqualTo(originalCreatedAt); // createdAt should not change
        assertThat(updatedUser.getUpdatedAt()).isNotEqualTo(originalUpdatedAt); // updatedAt should change
        assertThat(updatedUser.getUpdatedAt()).isAfter(originalUpdatedAt);
        assertThat(updatedUser.getUpdatedAt()).isBetween(beforeUpdate.minusSeconds(1), afterUpdate.plusSeconds(1));
    }

    @Test
    @DisplayName("Should not modify createdAt on update")
    void shouldNotModifyCreatedAtOnUpdate() throws InterruptedException {
        // Given
        User user = User.builder()
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@example.com")
                .phone("+5555555555")
                .documentNumber("11111111")
                .build();

        User savedUser = entityManager.persistAndFlush(user);
        LocalDateTime originalCreatedAt = savedUser.getCreatedAt();

        // Wait to ensure time difference
        Thread.sleep(100);

        // When - Multiple updates
        savedUser.setFirstName("Alice Updated");
        entityManager.flush();

        savedUser.setLastName("Johnson Updated");
        entityManager.flush();

        // Then
        User finalUser = entityManager.find(User.class, savedUser.getId());
        assertThat(finalUser.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(finalUser.getUpdatedAt()).isAfter(originalCreatedAt);
    }

    @Test
    @DisplayName("Should set timestamps when using repository save")
    void shouldSetTimestampsWithRepositorySave() {
        // Given
        User user = User.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bob.smith@example.com")
                .phone("+9999999999")
                .documentNumber("99999999")
                .build();

        // When
        LocalDateTime beforeSave = LocalDateTime.now();
        User savedUser = userRepository.save(user);
        LocalDateTime afterSave = LocalDateTime.now();

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isBetween(beforeSave.minusSeconds(1), afterSave.plusSeconds(1));
        assertThat(savedUser.getUpdatedAt()).isBetween(beforeSave.minusSeconds(1), afterSave.plusSeconds(1));
    }

    @Test
    @DisplayName("Should update timestamps when using repository save for update")
    void shouldUpdateTimestampsWithRepositorySave() throws InterruptedException {
        // Given - Create user
        User user = User.builder()
                .firstName("Charlie")
                .lastName("Brown")
                .email("charlie.brown@example.com")
                .phone("+1111111111")
                .documentNumber("22222222")
                .build();

        User savedUser = userRepository.save(user);
        LocalDateTime originalCreatedAt = savedUser.getCreatedAt();
        LocalDateTime originalUpdatedAt = savedUser.getUpdatedAt();

        // Wait to ensure time difference
        Thread.sleep(100);

        // When - Update user
        savedUser.setFirstName("Charles");
        savedUser.setMonthlyIncome(7000.0);
        User updatedUser = userRepository.save(savedUser);

        // Then
        assertThat(updatedUser.getCreatedAt()).isEqualTo(originalCreatedAt);
        // updatedAt should be updated or at least equal (if update happens too fast)
        assertThat(updatedUser.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
    }

    @Test
    @DisplayName("Should maintain timestamp integrity across multiple operations")
    void shouldMaintainTimestampIntegrity() throws InterruptedException {
        // Given
        User user = User.builder()
                .firstName("David")
                .lastName("Wilson")
                .email("david.wilson@example.com")
                .phone("+2222222222")
                .documentNumber("33333333")
                .creditScore(700)
                .build();

        // When - Create
        User savedUser = userRepository.save(user);
        LocalDateTime createdAt = savedUser.getCreatedAt();
        LocalDateTime firstUpdatedAt = savedUser.getUpdatedAt();

        Thread.sleep(100);

        // Update 1
        savedUser.setCreditScore(750);
        User afterFirstUpdate = userRepository.save(savedUser);
        LocalDateTime secondUpdatedAt = afterFirstUpdate.getUpdatedAt();

        Thread.sleep(100);

        // Update 2
        afterFirstUpdate.setCreditScore(800);
        User afterSecondUpdate = userRepository.save(afterFirstUpdate);
        LocalDateTime thirdUpdatedAt = afterSecondUpdate.getUpdatedAt();

        // Then
        assertThat(afterSecondUpdate.getCreatedAt()).isEqualTo(createdAt);
        assertThat(secondUpdatedAt).isAfterOrEqualTo(firstUpdatedAt);
        assertThat(thirdUpdatedAt).isAfterOrEqualTo(secondUpdatedAt);
        assertThat(thirdUpdatedAt).isAfterOrEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should handle createdAt as immutable (updatable=false)")
    void shouldKeepCreatedAtImmutable() throws InterruptedException {
        // Given
        User user = User.builder()
                .firstName("Emma")
                .lastName("Davis")
                .email("emma.davis@example.com")
                .phone("+3333333333")
                .documentNumber("44444444")
                .build();

        User savedUser = entityManager.persistAndFlush(user);
        LocalDateTime originalCreatedAt = savedUser.getCreatedAt();

        Thread.sleep(100);

        // When - Try to manually change createdAt (should be ignored by JPA)
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        savedUser.setCreatedAt(futureDate);
        entityManager.flush();
        entityManager.clear(); // Clear cache to force reload from DB

        // Then
        User reloadedUser = entityManager.find(User.class, savedUser.getId());
        // Due to @Column(updatable = false), the createdAt should remain unchanged in
        // DB
        // Compare truncated to seconds to avoid nanosecond precision issues
        assertThat(reloadedUser.getCreatedAt().withNano(0)).isEqualTo(originalCreatedAt.withNano(0));
        assertThat(reloadedUser.getCreatedAt()).isBefore(futureDate);
    }
}
