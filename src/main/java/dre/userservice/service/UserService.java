package dre.userservice.service;

import dre.userservice.dto.UserDTO;
import dre.userservice.dto.auth.LoginRequest;
import dre.userservice.dto.auth.RegistrationRequest;
import dre.userservice.dto.auth.RegistrationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    RegistrationResponse registerUser(RegistrationRequest request);

    String authenticateUser(LoginRequest request);

    UserDTO getUserById(Long id);

    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
}
