package com.example.holisticbabehelpcenter.dto;

import com.example.holisticbabehelpcenter.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String messageResponse;
    private Role role;
    private String email;
}
