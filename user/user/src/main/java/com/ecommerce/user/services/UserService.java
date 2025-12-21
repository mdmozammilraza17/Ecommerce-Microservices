package com.ecommerce.user.services;

import com.ecommerce.user.dto.AddressDto;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.models.Address;
import com.ecommerce.user.models.User;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final KeyCloakAdminService keyCloakAdminService;

    public UserService(UserRepository userRepository,
                       KeyCloakAdminService keyCloakAdminService) {
        this.userRepository = userRepository;
        this.keyCloakAdminService = keyCloakAdminService;
    }

    // ================= GET ALL USERS =================
    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    // ================= CREATE USER =================
    @Transactional
    public void addUser(UserRequest userRequest) {

        String token = keyCloakAdminService.getAdminAccessToken();

        String keycloakUserId =
                keyCloakAdminService.createUser(token, userRequest);

        User user = new User();
        updateUserFromRequest(user, userRequest);

        user.setKeycloakId(keycloakUserId);

        userRepository.save(user);
    }

    // ================= GET USER =================
    public Optional<UserResponse> fetchUser(String id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    // ================= UPDATE USER =================
    public boolean updateUser(String id, UserRequest updatedUserRequest) {

        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    // ================= MAPPER =================
    private void updateUserFromRequest(User user, UserRequest userRequest) {

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if (userRequest.getAddress() != null) {
            Address address = user.getAddress() != null
                    ? user.getAddress()
                    : new Address();

            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipcode(userRequest.getAddress().getZipcode());

            user.setAddress(address);
        }
    }

    private UserResponse mapToUserResponse(User user) {

        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setKeyCloakId(user.getKeycloakId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if (user.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setStreet(user.getAddress().getStreet());
            addressDto.setCity(user.getAddress().getCity());
            addressDto.setState(user.getAddress().getState());
            addressDto.setCountry(user.getAddress().getCountry());
            addressDto.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDto);
        }
        return response;
    }
}
