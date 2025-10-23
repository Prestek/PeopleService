package com.prestek.people.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prestek.people.dto.UserDto;
import com.prestek.people.model.User;
import com.prestek.people.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDto> getUserById(String id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public Optional<UserDto> getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }
    
    public UserDto createUser(UserDto userDto) {
        log.info("Creating new user with email: {}", userDto.getEmail());
        
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with email already exists: " + userDto.getEmail());
        }
        
        if (userRepository.existsByDocumentNumber(userDto.getDocumentNumber())) {
            throw new IllegalArgumentException("User with document number already exists: " + userDto.getDocumentNumber());
        }
        
        User user = convertToEntity(userDto);
        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        
        return convertToDto(savedUser);
    }
    
    public Optional<UserDto> updateUser(String id, UserDto userDto) {
        log.info("Updating user with id: {}", id);
        
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFields(existingUser, userDto);
                    User updatedUser = userRepository.save(existingUser);
                    log.info("User updated successfully with id: {}", updatedUser.getId());
                    return convertToDto(updatedUser);
                });
    }
    
    public boolean deleteUser(String id) {
        log.info("Deleting user with id: {}", id);
        
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User deleted successfully with id: {}", id);
            return true;
        }
        
        log.warn("User not found for deletion with id: {}", id);
        return false;
    }
    
    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .documentNumber(user.getDocumentNumber())
                .monthlyIncome(user.getMonthlyIncome())
                .monthlyExpenses(user.getMonthlyExpenses())
                .creditScore(user.getCreditScore())
                .employmentStatus(user.getEmploymentStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    private User convertToEntity(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .documentNumber(userDto.getDocumentNumber())
                .monthlyIncome(userDto.getMonthlyIncome())
                .monthlyExpenses(userDto.getMonthlyExpenses())
                .creditScore(userDto.getCreditScore())
                .employmentStatus(userDto.getEmploymentStatus())
                .build();
    }
    
    private void updateUserFields(User user, UserDto userDto) {
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getMonthlyIncome() != null) {
            user.setMonthlyIncome(userDto.getMonthlyIncome());
        }
        if (userDto.getMonthlyExpenses() != null) {
            user.setMonthlyExpenses(userDto.getMonthlyExpenses());
        }
        if (userDto.getCreditScore() != null) {
            user.setCreditScore(userDto.getCreditScore());
        }
        if (userDto.getEmploymentStatus() != null) {
            user.setEmploymentStatus(userDto.getEmploymentStatus());
        }
    }
}