package dre.userservice.service.impl;

import dre.userservice.dto.UserDTO;
import dre.userservice.dto.auth.LoginRequest;
import dre.userservice.dto.auth.RegistrationRequest;
import dre.userservice.dto.auth.RegistrationResponse;
import dre.userservice.exception.EntityNotFound;
import dre.userservice.exception.UserAlreadyExistsException;
import dre.userservice.model.User;
import dre.userservice.model.UserRole;
import dre.userservice.model.UserStatus;
import dre.userservice.repository.UserRepository;
import dre.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public RegistrationResponse registerUser(RegistrationRequest request) {
        log.info("Registering new user with username: {}", request.username());

        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        User user = User.builder()
                .username(request.username())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(hashPassword(request.password()))
                .phoneNumber(request.phoneNumber())
                .accessCardId(request.accessCardId())
                .role(UserRole.MEMBER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .address(request.address())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return RegistrationResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .phoneNumber(savedUser.getPhoneNumber())
                .accessCardId(savedUser.getAccessCardId())
                .role(savedUser.getRole())
                .build();
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public String authenticateUser(LoginRequest request) {
        log.info("Authenticating user with identifier: {}", request.identifier());

        try {
            User user = findUserByIdentifier(request.identifier());

            if (user.getStatus() != UserStatus.ACTIVE) {
                log.warn("Authentication failed: User is not active");
                return null;
            }

            String hashedPassword = hashPassword(request.password());
            if (user.getPassword().equals(hashedPassword)) {
                log.info("User authenticated successfully");
                return user.getId().toString(); // Convert Long to String for authentication
            } else {
                log.warn("Authentication failed: Invalid password");
                return null;
            }
        } catch (Exception e) {
            log.warn("Authentication failed: User not found");
            return null;
        }
    }

    private User findUserByIdentifier(String identifier) {
        if (identifier.contains("@")) {
            return userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new EntityNotFound("User not found"));
        } else if (identifier.matches("\\d+")) {
            return userRepository.findByPhoneNumber(identifier)
                    .orElseThrow(() -> new EntityNotFound("User not found"));
        } else {
            return userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new EntityNotFound("User not found"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    return new EntityNotFound("User not found");
                });
        return mapToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    return new EntityNotFound("User not found");
                });

        if (userDTO.username() != null) {
            usernameExist(userDTO.username());  // Check if the new username is already taken
            user.setUsername(userDTO.username());
        }
        if (userDTO.firstName() != null) {
            user.setFirstName(userDTO.firstName());
        }
        if (userDTO.lastName() != null) {
            user.setLastName(userDTO.lastName());
        }
        if (userDTO.email() != null && !userDTO.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.email())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(userDTO.email());
        }
        if (userDTO.phoneNumber() != null) {
            user.setPhoneNumber(userDTO.phoneNumber());
        }
        if (userDTO.accessCardId() != null) {
            user.setAccessCardId(userDTO.accessCardId());
        }
        if (userDTO.address() != null) {
            user.setAddress(userDTO.address());
        }
        if (userDTO.status() != null) {
            user.setStatus(userDTO.status());
        }

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", id);
        return mapToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found with ID: {}", id);
            throw new EntityNotFound("User not found");
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    private void usernameExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username is already in use");
        }
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .accessCardId(user.getAccessCardId())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .address(user.getAddress())
                .status(user.getStatus())
                .build();
    }
}
