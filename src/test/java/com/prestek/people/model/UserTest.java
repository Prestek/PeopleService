package com.prestek.people.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for User entity model
 */
@DisplayName("User Entity Tests")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .documentNumber("12345678")
                .monthlyIncome(5000.0)
                .monthlyExpenses(3000.0)
                .creditScore(750)
                .employmentStatus("EMPLOYED")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .applications(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Should create user with builder pattern")
    void shouldCreateUserWithBuilderPattern() {
        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(user.getPhone()).isEqualTo("+1234567890");
        assertThat(user.getDocumentNumber()).isEqualTo("12345678");
        assertThat(user.getMonthlyIncome()).isEqualTo(5000.0);
        assertThat(user.getMonthlyExpenses()).isEqualTo(3000.0);
        assertThat(user.getCreditScore()).isEqualTo(750);
        assertThat(user.getEmploymentStatus()).isEqualTo("EMPLOYED");
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should create user with no args constructor")
    void shouldCreateUserWithNoArgsConstructor() {
        // When
        User emptyUser = new User();

        // Then
        assertThat(emptyUser).isNotNull();
        assertThat(emptyUser.getId()).isNull();
        assertThat(emptyUser.getFirstName()).isNull();
        assertThat(emptyUser.getLastName()).isNull();
        assertThat(emptyUser.getEmail()).isNull();
    }

    @Test
    @DisplayName("Should create user with all args constructor")
    void shouldCreateUserWithAllArgsConstructor() {
        // When
        LocalDateTime now = LocalDateTime.now();
        User newUser = new User(
                2L,
                "Jane",
                "Smith",
                "jane.smith@example.com",
                "+9876543210",
                "87654321",
                6000.0,
                3500.0,
                800,
                "SELF_EMPLOYED",
                now,
                now,
                new ArrayList<>());

        // Then
        assertThat(newUser).isNotNull();
        assertThat(newUser.getId()).isEqualTo(2L);
        assertThat(newUser.getFirstName()).isEqualTo("Jane");
        assertThat(newUser.getLastName()).isEqualTo("Smith");
        assertThat(newUser.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(newUser.getPhone()).isEqualTo("+9876543210");
        assertThat(newUser.getDocumentNumber()).isEqualTo("87654321");
        assertThat(newUser.getMonthlyIncome()).isEqualTo(6000.0);
        assertThat(newUser.getMonthlyExpenses()).isEqualTo(3500.0);
        assertThat(newUser.getCreditScore()).isEqualTo(800);
        assertThat(newUser.getEmploymentStatus()).isEqualTo("SELF_EMPLOYED");
    }

    @Test
    @DisplayName("Should allow setting and getting all properties")
    void shouldAllowSettingAndGettingAllProperties() {
        // Given
        User newUser = new User();
        LocalDateTime now = LocalDateTime.now();

        // When
        newUser.setId(3L);
        newUser.setFirstName("Alice");
        newUser.setLastName("Johnson");
        newUser.setEmail("alice.johnson@example.com");
        newUser.setPhone("+5555555555");
        newUser.setDocumentNumber("11111111");
        newUser.setMonthlyIncome(7000.0);
        newUser.setMonthlyExpenses(4000.0);
        newUser.setCreditScore(850);
        newUser.setEmploymentStatus("EMPLOYED");
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);
        newUser.setApplications(new ArrayList<>());

        // Then
        assertThat(newUser.getId()).isEqualTo(3L);
        assertThat(newUser.getFirstName()).isEqualTo("Alice");
        assertThat(newUser.getLastName()).isEqualTo("Johnson");
        assertThat(newUser.getEmail()).isEqualTo("alice.johnson@example.com");
        assertThat(newUser.getPhone()).isEqualTo("+5555555555");
        assertThat(newUser.getDocumentNumber()).isEqualTo("11111111");
        assertThat(newUser.getMonthlyIncome()).isEqualTo(7000.0);
        assertThat(newUser.getMonthlyExpenses()).isEqualTo(4000.0);
        assertThat(newUser.getCreditScore()).isEqualTo(850);
        assertThat(newUser.getEmploymentStatus()).isEqualTo("EMPLOYED");
        assertThat(newUser.getCreatedAt()).isEqualTo(now);
        assertThat(newUser.getUpdatedAt()).isEqualTo(now);
        assertThat(newUser.getApplications()).isNotNull();
    }

    @Test
    @DisplayName("Should support equals and hashCode for same user")
    void shouldSupportEqualsAndHashCodeForSameUser() {
        // Given
        User user1 = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .documentNumber("12345678")
                .build();

        User user2 = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .documentNumber("12345678")
                .build();

        // Then
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("Should support equals and hashCode for different users")
    void shouldSupportEqualsAndHashCodeForDifferentUsers() {
        // Given
        User user1 = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        User user2 = User.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        // Then
        assertThat(user1).isNotEqualTo(user2);
        assertThat(user1.hashCode()).isNotEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("Should generate toString with all fields")
    void shouldGenerateToStringWithAllFields() {
        // When
        String userString = user.toString();

        // Then
        assertThat(userString).contains("John");
        assertThat(userString).contains("Doe");
        assertThat(userString).contains("john.doe@example.com");
        assertThat(userString).contains("+1234567890");
        assertThat(userString).contains("12345678");
    }

    @Test
    @DisplayName("Should allow null optional fields")
    void shouldAllowNullOptionalFields() {
        // When
        User minimalUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .documentNumber("12345678")
                .build();

        // Then
        assertThat(minimalUser.getMonthlyIncome()).isNull();
        assertThat(minimalUser.getMonthlyExpenses()).isNull();
        assertThat(minimalUser.getCreditScore()).isNull();
        assertThat(minimalUser.getEmploymentStatus()).isNull();
    }

    @Test
    @DisplayName("Should handle applications relationship")
    void shouldHandleApplicationsRelationship() {
        // Given
        Application application1 = Application.builder()
                .id(1L)
                .user(user)
                .build();

        Application application2 = Application.builder()
                .id(2L)
                .user(user)
                .build();

        // When
        user.getApplications().add(application1);
        user.getApplications().add(application2);

        // Then
        assertThat(user.getApplications()).hasSize(2);
        assertThat(user.getApplications()).contains(application1, application2);
    }

    @Test
    @DisplayName("Should update financial information")
    void shouldUpdateFinancialInformation() {
        // When
        user.setMonthlyIncome(8000.0);
        user.setMonthlyExpenses(4500.0);
        user.setCreditScore(820);

        // Then
        assertThat(user.getMonthlyIncome()).isEqualTo(8000.0);
        assertThat(user.getMonthlyExpenses()).isEqualTo(4500.0);
        assertThat(user.getCreditScore()).isEqualTo(820);
    }

    @Test
    @DisplayName("Should update employment status")
    void shouldUpdateEmploymentStatus() {
        // When
        user.setEmploymentStatus("SELF_EMPLOYED");

        // Then
        assertThat(user.getEmploymentStatus()).isEqualTo("SELF_EMPLOYED");
    }
}
