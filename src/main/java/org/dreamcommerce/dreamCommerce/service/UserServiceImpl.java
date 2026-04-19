package org.dreamcommerce.dreamCommerce.service;

import lombok.RequiredArgsConstructor;
import org.dreamcommerce.dreamCommerce.dtos.requests.CreateUserRequest;
import org.dreamcommerce.dreamCommerce.dtos.requests.UpdateUserRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.UserResponse;
import org.dreamcommerce.dreamCommerce.enums.Role;
import org.dreamcommerce.dreamCommerce.models.User;
import org.dreamcommerce.dreamCommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        ModelMapper mapper = new ModelMapper();
        User user = mapper.map(request, User.class);
        return mapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    public UserResponse getUser(Long id) {
        ModelMapper mapper = new ModelMapper();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        ModelMapper mapper = new ModelMapper();
        return userRepository.findAll().stream()
                .map(user -> mapper.map(user, UserResponse.class))
                .toList();
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        ModelMapper mapper = new ModelMapper();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        mapper.map(request, user);
        return mapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse verifyVendor(Long id) {
        ModelMapper mapper = new ModelMapper();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.VENDOR) {
            throw new RuntimeException("User is not a vendor");
        }
        user.setVerified(true);
        return mapper.map(userRepository.save(user), UserResponse.class);
    }
}
