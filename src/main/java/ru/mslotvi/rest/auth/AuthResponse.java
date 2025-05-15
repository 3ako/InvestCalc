package ru.mslotvi.rest.auth;

import lombok.Data;

import java.util.List;

@Data
public class AuthResponse {
    private String username;
    private String token;
    private List<String> roles;
}