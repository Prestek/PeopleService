package com.prestek.people.service;

import com.prestek.people.dto.UserDto;
import com.prestek.people.model.User;
import com.prestek.people.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService covering three critical cases:
 * 1. User Creation
 * 2. User Query Operations
 * 3. Error Handling
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDto validUserDto;
    private User validUser;

    @BeforeEach
    void setUp() {
        // Setup valid user data
        validUserDto = UserDto.builder()
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

        validUser = User.builder()
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
                .build();

    }

    // ==========================================
    // 1. USER CREATION TESTS
    // ==========================================

    @Test
    @DisplayName("Should create user successfully with valid data")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.existsByEmail(validUserDto.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(validUserDto.getDocumentNumber())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        // When
        UserDto result = userService.createUser(validUserDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getPhone()).isEqualTo("+1234567890");
        assertThat(result.getDocumentNumber()).isEqualTo("12345678");
        assertThat(result.getMonthlyIncome()).isEqualTo(5000.0);
        assertThat(result.getMonthlyExpenses()).isEqualTo(3000.0);
        assertThat(result.getCreditScore()).isEqualTo(750);
        assertThat(result.getEmploymentStatus()).isEqualTo("EMPLOYED");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(userRepository).existsByEmail(validUserDto.getEmail());
        verify(userRepository).existsByDocumentNumber(validUserDto.getDocumentNumber());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should create user with minimal required data")
    void shouldCreateUserWithMinimalData() {
        // Given
        UserDto minimalUserDto = UserDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("+9876543210")
                .documentNumber("87654321")
                .build();

        User minimalUser = User.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("+9876543210")
                .documentNumber("87654321")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.existsByEmail(minimalUserDto.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(minimalUserDto.getDocumentNumber())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(minimalUser);

        // When
        UserDto result = userService.createUser(minimalUserDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(result.getPhone()).isEqualTo("+9876543210");
        assertThat(result.getDocumentNumber()).isEqualTo("87654321");
        assertThat(result.getMonthlyIncome()).isNull();
        assertThat(result.getMonthlyExpenses()).isNull();
        assertThat(result.getCreditScore()).isNull();
        assertThat(result.getEmploymentStatus()).isNull();

        verify(userRepository).existsByEmail(minimalUserDto.getEmail());
        verify(userRepository).existsByDocumentNumber(minimalUserDto.getDocumentNumber());
        verify(userRepository).save(any(User.class));
    }

    // ==========================================
    // 2. USER QUERY TESTS
    // ==========================================

    @Test
    @DisplayName("Should retrieve all users successfully")
    void shouldRetrieveAllUsers() {
        // Given
        User user1 = createTestUser(1L, "John", "Doe", "john.doe@example.com");
        User user2 = createTestUser(2L, "Jane", "Smith", "jane.smith@example.com");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(0).getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(result.get(1).getEmail()).isEqualTo("jane.smith@example.com");

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should retrieve user by ID successfully")
    void shouldRetrieveUserById() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));

        // When
        Optional<UserDto> result = userService.getUserById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getFirstName()).isEqualTo("John");
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when user not found by ID")
    void shouldReturnEmptyWhenUserNotFoundById() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.getUserById(999L);

        // Then
        assertThat(result).isEmpty();

        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("Should retrieve user by email successfully")
    void shouldRetrieveUserByEmail() {
        // Given
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(validUser));

        // When
        Optional<UserDto> result = userService.getUserByEmail("john.doe@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.get().getFirstName()).isEqualTo("John");

        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.getUserByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();

        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsersExist() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<UserDto> result = userService.getAllUsers();

        // Then
        assertThat(result).isEmpty();

        verify(userRepository).findAll();
    }

    // ==========================================
    // 3. ERROR HANDLING TESTS
    // ==========================================

    @Test
    @DisplayName("Should throw exception when creating user with duplicate email")
    void shouldThrowExceptionWhenCreatingUserWithDuplicateEmail() {
        // Given
        when(userRepository.existsByEmail(validUserDto.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(validUserDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with email already exists: " + validUserDto.getEmail());

        verify(userRepository).existsByEmail(validUserDto.getEmail());
        verify(userRepository, never()).existsByDocumentNumber(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when creating user with duplicate document number")
    void shouldThrowExceptionWhenCreatingUserWithDuplicateDocumentNumber() {
        // Given
        when(userRepository.existsByEmail(validUserDto.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(validUserDto.getDocumentNumber())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(validUserDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with document number already exists: " + validUserDto.getDocumentNumber());

        verify(userRepository).existsByEmail(validUserDto.getEmail());
        verify(userRepository).existsByDocumentNumber(validUserDto.getDocumentNumber());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle null user data gracefully")
    void shouldHandleNullUserDataGracefully() {
        // Given
        UserDto nullUserDto = null;

        // When & Then
        assertThatThrownBy(() -> userService.createUser(nullUserDto))
                .isInstanceOf(NullPointerException.class);

        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).existsByDocumentNumber(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle repository exceptions during user creation")
    void shouldHandleRepositoryExceptionsDuringUserCreation() {
        // Given
        when(userRepository.existsByEmail(validUserDto.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(validUserDto.getDocumentNumber())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        assertThatThrownBy(() -> userService.createUser(validUserDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection failed");

        verify(userRepository).existsByEmail(validUserDto.getEmail());
        verify(userRepository).existsByDocumentNumber(validUserDto.getDocumentNumber());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle repository exceptions during user query")
    void shouldHandleRepositoryExceptionsDuringUserQuery() {
        // Given
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection failed");

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should handle repository exceptions during getAllUsers")
    void shouldHandleRepositoryExceptionsDuringGetAllUsers() {
        // Given
        when(userRepository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        assertThatThrownBy(() -> userService.getAllUsers())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection failed");

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should handle invalid email format gracefully")
    void shouldHandleInvalidEmailFormatGracefully() {
        // Given
        UserDto invalidEmailUserDto = UserDto.builder()
                .firstName("Test")
                .lastName("User")
                .email("invalid-email-format")
                .phone("+1234567890")
                .documentNumber("12345678")
                .build();

        User userWithInvalidEmail = User.builder()
                .id(3L)
                .firstName("Test")
                .lastName("User")
                .email("invalid-email-format")
                .phone("+1234567890")
                .documentNumber("12345678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.existsByEmail(invalidEmailUserDto.getEmail())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(invalidEmailUserDto.getDocumentNumber())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(userWithInvalidEmail);

        // When
        UserDto result = userService.createUser(invalidEmailUserDto);

        // Then
        // The service doesn't validate email format, it just saves what's provided
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("invalid-email-format");

        verify(userRepository).existsByEmail(invalidEmailUserDto.getEmail());
        verify(userRepository).existsByDocumentNumber(invalidEmailUserDto.getDocumentNumber());
        verify(userRepository).save(any(User.class));
    }

    // ==========================================
    // 4. USER UPDATE TESTS
    // ==========================================

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        UserDto updateDto = UserDto.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .phone("+9999999999")
                .monthlyIncome(6000.0)
                .monthlyExpenses(3500.0)
                .creditScore(800)
                .employmentStatus("SELF_EMPLOYED")
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .firstName("John Updated")
                .lastName("Doe Updated")
                .email("john.doe@example.com")
                .phone("+9999999999")
                .documentNumber("12345678")
                .monthlyIncome(6000.0)
                .monthlyExpenses(3500.0)
                .creditScore(800)
                .employmentStatus("SELF_EMPLOYED")
                .createdAt(validUser.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        Optional<UserDto> result = userService.updateUser(1L, updateDto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John Updated");
        assertThat(result.get().getLastName()).isEqualTo("Doe Updated");
        assertThat(result.get().getPhone()).isEqualTo("+9999999999");
        assertThat(result.get().getMonthlyIncome()).isEqualTo(6000.0);
        assertThat(result.get().getMonthlyExpenses()).isEqualTo(3500.0);
        assertThat(result.get().getCreditScore()).isEqualTo(800);
        assertThat(result.get().getEmploymentStatus()).isEqualTo("SELF_EMPLOYED");

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should update user with partial data")
    void shouldUpdateUserWithPartialData() {
        // Given
        UserDto partialUpdateDto = UserDto.builder()
                .firstName("John Updated")
                .creditScore(800)
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .firstName("John Updated")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .documentNumber("12345678")
                .monthlyIncome(5000.0)
                .monthlyExpenses(3000.0)
                .creditScore(800)
                .employmentStatus("EMPLOYED")
                .createdAt(validUser.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        Optional<UserDto> result = userService.updateUser(1L, partialUpdateDto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John Updated");
        assertThat(result.get().getLastName()).isEqualTo("Doe");
        assertThat(result.get().getCreditScore()).isEqualTo(800);

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should return empty when updating non-existent user")
    void shouldReturnEmptyWhenUpdatingNonExistentUser() {
        // Given
        UserDto updateDto = UserDto.builder()
                .firstName("John")
                .build();

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.updateUser(999L, updateDto);

        // Then
        assertThat(result).isEmpty();

        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    // ==========================================
    // 5. USER DELETE TESTS
    // ==========================================

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        boolean result = userService.deleteUser(1L);

        // Then
        assertThat(result).isTrue();

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent user")
    void shouldReturnFalseWhenDeletingNonExistentUser() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = userService.deleteUser(999L);

        // Then
        assertThat(result).isFalse();

        verify(userRepository).existsById(999L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    private User createTestUser(Long id, String firstName, String lastName, String email) {
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone("+1234567890")
                .documentNumber("12345678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
