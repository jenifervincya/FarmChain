package com.fairchain.batchservice;

public class AuthResponse {
    private final String token;
    private final String username;
    private final String role;
    private final String displayName;

    public AuthResponse(String token, String username, String role, String displayName) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.displayName = displayName;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getDisplayName() { return displayName; }
}
