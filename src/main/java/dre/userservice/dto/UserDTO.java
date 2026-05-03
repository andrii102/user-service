package dre.userservice.dto;

import dre.userservice.model.UserRole;
import dre.userservice.model.UserStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDTO(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String accessCardId,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String address,
        UserStatus status
) {
}
