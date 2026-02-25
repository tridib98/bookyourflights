package com.tridib04.bookyourflights.customerservice.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tridib04.bookyourflights.customerservice.model.AuthRequest;
import com.tridib04.bookyourflights.customerservice.security.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {
     private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService uds;

    public AuthController(AuthenticationManager authManager, JwtService jwtService, UserDetailsService uds) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.uds = uds;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            System.out.println("=====================================");
            System.out.println("In login section");
            System.out.println("=====================================");
            UserDetails userDetails = uds.loadUserByUsername(request.getUsername());
            String token = jwtService.generateToken(userDetails);

            // return token and basic user info
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", userDetails.getUsername(),
                    "authorities", userDetails.getAuthorities()
            ));
        } catch (BadCredentialsException ex) {
            System.out.println("=====================================");
            System.out.println("Error In Authenticating"+ex);
            System.out.println("=====================================");
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}
