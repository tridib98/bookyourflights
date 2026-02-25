package com.tridib04.bookyourflights.customerservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public content - no token required";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String viewerEndpoint() {
        return "Admin content";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/home")
    public String regulatoryEndpoint() {
        return "Customer  Content";
    }

    @PreAuthorize("hasRole('ENGINEER')")
    @GetMapping("/engineer")
    public String adminEndpoint() {
        return "Engineer content";
    }
}
