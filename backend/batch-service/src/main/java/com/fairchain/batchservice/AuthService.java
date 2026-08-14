package com.fairchain.batchservice;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * DEMO-GRADE auth. Token is just Base64("username:role:issuedAt"),
 * not a signed/verifiable JWT. Anyone can forge one by hand.
 * Section 4.4 explicitly allows "a simplified token issued at login"
 * for the hackathon demo scope; this is that, taken at face value.
 * Real JWT signing + verification is real follow-up work, not done here.
 */
@Service
public class AuthService {

    private final AccountRepository repository;

    public AuthService(AccountRepository repository) {
        this.repository = repository;
    }

    public AuthResponse register(RegisterRequest request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameTakenException(request.getUsername());
        }

        Account account = new Account(
                request.getUsername(),
                PasswordUtil.hash(request.getPassword()),
                request.getRole(),
                request.getDisplayName(),
                Instant.now()
        );
        Account saved = repository.save(account);

        return buildAuthResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        Account account = repository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        String hashedAttempt = PasswordUtil.hash(request.getPassword());
        if (!hashedAttempt.equals(account.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return buildAuthResponse(account);
    }

    private AuthResponse buildAuthResponse(Account account) {
        String rawToken = account.getUsername() + ":" + account.getRole() + ":" + Instant.now().toEpochMilli();
        String token = Base64.getEncoder().encodeToString(rawToken.getBytes(StandardCharsets.UTF_8));
        return new AuthResponse(token, account.getUsername(), account.getRole(), account.getDisplayName());
    }
}
