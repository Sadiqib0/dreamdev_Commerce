package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.CreateUserRequest;
import org.dreamcommerce.dreamCommerce.dtos.requests.UpdateUserRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse getUser(Long id);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
    UserResponse verifyVendor(Long id);
}
