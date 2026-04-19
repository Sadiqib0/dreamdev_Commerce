package org.dreamcommerce.dreamCommerce.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import org.dreamcommerce.dreamCommerce.enums.Role;

@Getter
@Setter
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
