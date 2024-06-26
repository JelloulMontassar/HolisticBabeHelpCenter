package com.example.holisticbabehelpcenter.controller;
import com.example.holisticbabehelpcenter.dto.AuthenticationRequest;
import com.example.holisticbabehelpcenter.dto.AuthenticationResponse;
import com.example.holisticbabehelpcenter.dto.ForgotPasswordResponse;
import com.example.holisticbabehelpcenter.dto.CforgotPasswordRequest;
import com.example.holisticbabehelpcenter.exceptions.UserException;
import com.example.holisticbabehelpcenter.model.User;
import com.example.holisticbabehelpcenter.service.ForgotPasswordService;
import com.example.holisticbabehelpcenter.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.security.Key;
import java.security.SecureRandom;
import java.util.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {
    @Value("${jwt.secret}")
    private  String SECRET_KEY;
    private static final long DEFAULT_EXPIRATION_TIME_MILLIS = 604800000;
    private final UserService userService;
    private final ForgotPasswordService forgotPasswordService;
    private static final String CONFIRMATION_URL = "";
    public static Set<String> onlineUsers = new HashSet<>();


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        onlineUsers.remove(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }
    @GetMapping("/onlineUsers")
    public List<User> getOnlineUsers() {
        List<User> usersByEmail = new ArrayList<>();
        for (String email : onlineUsers) {
            usersByEmail.add(userService.getUserByEmail(email));
        }
        return usersByEmail;
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<ForgotPasswordResponse> forgetPassword(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        ForgotPasswordResponse response = new ForgotPasswordResponse(user.getEmail());
         long resetToken = Long.parseLong(generateActivationCode(6));
        System.out.println(resetToken);
        response.setMessageResponse("A Confirmation Reset Token has been sent to your email");
        user.setResetToken(resetToken);
        String jwtToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_EXPIRATION_TIME_MILLIS))
                .claim("resetToken", resetToken)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        userService.updateUser(user);
        try {
            forgotPasswordService.sendEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    "resetpwd",
                    String.format(CONFIRMATION_URL, jwtToken),
                    String.valueOf(resetToken)
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());//0..9
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
    @PostMapping("/forgot-password/")
    public ResponseEntity<ForgotPasswordResponse> CforgetPassword(@RequestBody CforgotPasswordRequest CRequest) {
        User user = userService.getUserByEmail(CRequest.getEmail());
        long cReset  = Long.parseLong(CRequest.getResetToken());
        if (Objects.equals(user.getResetToken() ,cReset)&&user.getEmail().equals(CRequest.getEmail())){
            String newPassword = CRequest.getNewPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            user.setResetToken(Long.parseLong(generateActivationCode(6)));
            userService.updateUser(user);
            return  ResponseEntity.ok(new ForgotPasswordResponse(CRequest.getEmail(),"Password Updated Successfully !"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ForgotPasswordResponse.builder()
                        .email(CRequest.getEmail())
                        .messageResponse("An error occurred during password reset")
                        .build());
    }
    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = userService.authenticate(request);
            onlineUsers.add(response.getEmail());
            return ResponseEntity.ok(response);
        } catch (UserException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().equals("Bad credentials")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        AuthenticationResponse.builder()
                                .messageResponse("User not found")
                                .build());
            } else if (e.getMessage().equals("User is disabled")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        AuthenticationResponse.builder()
                                .messageResponse("User account is not active. Please confirm your email.")
                                .build());
            }

            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        AuthenticationResponse.builder()
                                .messageResponse("An error occurred during authentication.")
                                .build());
            }
        }
    }
}



