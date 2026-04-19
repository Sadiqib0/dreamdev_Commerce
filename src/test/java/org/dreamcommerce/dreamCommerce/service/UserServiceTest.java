package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.CreateUserRequest;
import org.dreamcommerce.dreamCommerce.dtos.requests.UpdateUserRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.UserResponse;
import org.dreamcommerce.dreamCommerce.enums.Role;
import org.dreamcommerce.dreamCommerce.models.User;
import org.dreamcommerce.dreamCommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCanCreateUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("secret");
        request.setRole(Role.CUSTOMER);

        User savedUser = buildUser(1L, "John Doe", "john@example.com", Role.CUSTOMER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testCanGetUser() {
        User user = buildUser(1L, "Jane Doe", "jane@example.com", Role.VENDOR);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUser(1L);

        assertNotNull(response);
        assertThat(response.getRole()).isEqualTo(Role.VENDOR);
    }

    @Test
    void testGetUserThrowsWhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void testCanGetAllUsers() {
        User u1 = buildUser(1L, "Alice", "alice@example.com", Role.CUSTOMER);
        User u2 = buildUser(2L, "Bob", "bob@example.com", Role.VENDOR);
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserResponse> responses = userService.getAllUsers();

        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void testCanUpdateUser() {
        User existing = buildUser(1L, "Old Name", "old@example.com", Role.CUSTOMER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(existing);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("New Name");

        UserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
        verify(userRepository).save(existing);
    }

    @Test
    void testCanDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testCanVerifyVendor() {
        User vendor = buildUser(1L, "Vendor Joe", "vendor@example.com", Role.VENDOR);
        when(userRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(userRepository.save(any(User.class))).thenReturn(vendor);

        UserResponse response = userService.verifyVendor(1L);

        assertNotNull(response);
        verify(userRepository).save(vendor);
        assertThat(vendor.isVerified()).isTrue();
    }

    @Test
    void testVerifyVendorThrowsForNonVendor() {
        User customer = buildUser(1L, "Customer", "customer@example.com", Role.CUSTOMER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> userService.verifyVendor(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User is not a vendor");
    }

    private User buildUser(Long id, String name, String email, Role role) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        return user;
    }
}
