package com.tridib04.bookyourflights.customerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tridib04.bookyourflights.customerservice.repository.UserRepository;
import com.tridib04.bookyourflights.customerservice.entities.*;


@Service
public class MyUserDetailsService implements UserDetailsService{

    @Autowired
    private final UserRepository repo;
    
    public MyUserDetailsService(UserRepository repo){
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User u = repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    // Directly map enum to Spring Security authority
    String authority = "ROLE_" + u.getRole().name(); // e.g., ROLE_ADMIN, ROLE_VIEWER, ROLE_REGULATORY_OFFICER

    return org.springframework.security.core.userdetails.User
            .withUsername(u.getUsername())
            .password(u.getPassword())
            .authorities(new SimpleGrantedAuthority(authority))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }

}
