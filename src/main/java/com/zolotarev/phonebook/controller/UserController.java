package com.zolotarev.phonebook.controller;

import com.zolotarev.phonebook.dto.AuthRequest;
import com.zolotarev.phonebook.dto.AuthResponse;
import com.zolotarev.phonebook.dto.UserRequest;
import com.zolotarev.phonebook.dto.UserResponse;
import com.zolotarev.phonebook.entities.User;
import com.zolotarev.phonebook.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        Long userId = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created with id: " + userId);
    }

    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "JWT Bearer")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    Authentication authentication) {
        User user = userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted with id: " + user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = userService.login(authRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }
}
