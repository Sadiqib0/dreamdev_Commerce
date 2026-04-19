package org.dreamcommerce.dreamCommerce.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean verified;
    private LocalDateTime createdAt;
}
