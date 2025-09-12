package com.prestek.people.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prestek.people.dto.UserDto;
import com.prestek.people.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserController covering three critical cases:
 * 1. User Creation via REST API
 * 2. User Query Operations via REST API
 * 3. Error Handling in REST API
 */
@WebMvcTest(UserController.class)
@DisplayName("UserController Unit Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto validUserDto;
    private UserDto createdUserDto;

    @BeforeEach
    void setUp() {
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

        createdUserDto = UserDto.builder()
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
    // 1. USER CREATION TESTS (REST API)
    // ==========================================

    @Test
    @DisplayName("Should create user successfully via POST /api/users")
    void shouldCreateUserSuccessfully() throws Exception {
        // Given
        when(userService.createUser(any(UserDto.class))).thenReturn(createdUserDto);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("+1234567890"))
                .andExpect(jsonPath("$.documentNumber").value("12345678"))
                .andExpect(jsonPath("$.monthlyIncome").value(5000.0))
                .andExpect(jsonPath("$.monthlyExpenses").value(3000.0))
                .andExpect(jsonPath("$.creditScore").value(750))
                .andExpect(jsonPath("$.employmentStatus").value("EMPLOYED"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());

        verify(userService).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should return 400 when creating user with duplicate email")
    void shouldReturn400WhenCreatingUserWithDuplicateEmail() throws Exception {
        // Given
        when(userService.createUser(any(UserDto.class)))
                .thenThrow(new IllegalArgumentException("User with email already exists: john.doe@example.com"));

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDto)))
                .andExpect(status().isBadRequest());

        verify(userService).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should return 400 when creating user with invalid JSON")
    void shouldReturn400WhenCreatingUserWithInvalidJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should create user even with missing optional fields")
    void shouldCreateUserEvenWithMissingOptionalFields() throws Exception {
        // Given
        UserDto userWithMissingFields = UserDto.builder()
                .firstName("John")
                // Missing lastName, email, phone, documentNumber
                .build();

        UserDto createdUser = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName(null)
                .email(null)
                .phone(null)
                .documentNumber(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithMissingFields)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist());

        verify(userService).createUser(any(UserDto.class));
    }

    // ==========================================
    // 2. USER QUERY TESTS (REST API)
    // ==========================================

    @Test
    @DisplayName("Should retrieve all users successfully via GET /api/users")
    void shouldRetrieveAllUsersSuccessfully() throws Exception {
        // Given
        UserDto user1 = createUserDto(1L, "John", "Doe", "john.doe@example.com");
        UserDto user2 = createUserDto(2L, "Jane", "Smith", "jane.smith@example.com");
        List<UserDto> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].email").value("jane.smith@example.com"));

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("Should retrieve user by ID successfully via GET /api/users/{id}")
    void shouldRetrieveUserByIdSuccessfully() throws Exception {
        // Given
        when(userService.getUserById(1L)).thenReturn(Optional.of(createdUserDto));

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Should return 404 when user not found by ID")
    void shouldReturn404WhenUserNotFoundById() throws Exception {
        // Given
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(999L);
    }

    @Test
    @DisplayName("Should retrieve user by email successfully via GET /api/users/email/{email}")
    void shouldRetrieveUserByEmailSuccessfully() throws Exception {
        // Given
        when(userService.getUserByEmail("john.doe@example.com")).thenReturn(Optional.of(createdUserDto));

        // When & Then
        mockMvc.perform(get("/api/users/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(userService).getUserByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Should return 404 when user not found by email")
    void shouldReturn404WhenUserNotFoundByEmail() throws Exception {
        // Given
        when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/users/email/nonexistent@example.com"))
                .andExpect(status().isNotFound());

        verify(userService).getUserByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should return empty array when no users exist")
    void shouldReturnEmptyArrayWhenNoUsersExist() throws Exception {
        // Given
        when(userService.getAllUsers()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userService).getAllUsers();
    }

    // ==========================================
    // 3. ERROR HANDLING TESTS (REST API)
    // ==========================================

    @Test
    @DisplayName("Should handle service exceptions gracefully")
    void shouldHandleServiceExceptionsGracefully() throws Exception {
        // Given
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        // The controller doesn't have global exception handling, so the exception
        // propagates
        // This test verifies that the service is called and the exception is thrown
        assertThatThrownBy(() -> mockMvc.perform(get("/api/users")))
                .hasCauseInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database connection failed");

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("Should handle invalid user ID format")
    void shouldHandleInvalidUserIdFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/invalid-id"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(anyLong());
    }

    @Test
    @DisplayName("Should handle null request body")
    void shouldHandleNullRequestBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should handle malformed JSON in request body")
    void shouldHandleMalformedJsonInRequestBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"John\", \"lastName\":}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should handle unsupported media type")
    void shouldHandleUnsupportedMediaType() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.TEXT_PLAIN)
                .content("some text"))
                .andExpect(status().isUnsupportedMediaType());

        verify(userService, never()).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Should handle very large request body")
    void shouldHandleVeryLargeRequestBody() throws Exception {
        // Given
        StringBuilder largeJson = new StringBuilder();
        largeJson.append(
                "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phone\": \"+1234567890\", \"documentNumber\": \"12345678\"");

        // Add a very large string to make the request body large
        largeJson.append(", \"largeField\": \"");
        for (int i = 0; i < 10000; i++) {
            largeJson.append("x");
        }
        largeJson.append("\"}");

        UserDto createdUser = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .documentNumber("12345678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(largeJson.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).createUser(any(UserDto.class));
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    private UserDto createUserDto(Long id, String firstName, String lastName, String email) {
        return UserDto.builder()
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
