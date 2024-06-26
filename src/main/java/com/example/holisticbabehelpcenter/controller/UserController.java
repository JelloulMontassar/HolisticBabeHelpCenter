package com.example.holisticbabehelpcenter.controller;

import com.example.holisticbabehelpcenter.dto.ConfirmationResponse;
import com.example.holisticbabehelpcenter.dto.RegisterRequest;
import com.example.holisticbabehelpcenter.dto.RegisterResponse;
import com.example.holisticbabehelpcenter.enumeration.Role;
import com.example.holisticbabehelpcenter.exceptions.UserException;
import com.example.holisticbabehelpcenter.model.Notification;
import com.example.holisticbabehelpcenter.model.User;
import com.example.holisticbabehelpcenter.service.NotificationService;
import com.example.holisticbabehelpcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;
    Map<String, String> response = new HashMap<>();
    @PutMapping("/disableAccount/{userId}")
    public ResponseEntity<Map<String, String>> disableAccount(@PathVariable Long userId) {
        try {
            Optional<User> user = userService.findById(userId);
            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setEnabled(false);
                userService.updateUser(existingUser);
                response.put("message", "Account disabled successfully");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("message", "User Account not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/enableAccount/{userId}")
    public ResponseEntity<Map<String, String>> confirmUser(@PathVariable Long userId) {
        try {
            Optional<User> user = userService.findById(userId);
            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setEnabled(true);
                userService.updateUser(existingUser);
                response.put("message", "Account enabled successfully");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("message", "User Account not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PostMapping("/registerAdmin")
    public ResponseEntity<RegisterResponse> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse  = new RegisterResponse();
        try {
            userService.registerAccount(registerRequest, Role.ROLE_ADMIN);
            registerResponse.setEmailResponse(registerRequest.getEmail());
            registerResponse.setMessageResponse("Account Created");
            return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
        }catch (UserException e) {
            registerResponse.setMessageResponse(e.getMessage());
            registerResponse.setEmailResponse(registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessageResponse("An error occurred while registering user.");
            registerResponse.setEmailResponse(registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(registerResponse);
        }
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        try {
            List<User> users = userService.getAllUsers();

            return ResponseEntity.status(HttpStatus.CREATED).body(users);
        }catch (Exception e) {
            String errorMessage = "An error occurred while fetching users.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getMyNotifications(Authentication authentication) {
        User user  = userService.getUserByEmail(authentication.getName());
        System.out.println(user.getEmail());
        List<Notification> notifications = notificationService.getLatestNotifications(user);
        System.out.println(notifications.size());
        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }

}