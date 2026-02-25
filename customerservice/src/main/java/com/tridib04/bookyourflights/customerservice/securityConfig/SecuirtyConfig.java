package com.tridib04.bookyourflights.customerservice.securityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.tridib04.bookyourflights.customerservice.security.JwtAuthFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecuirtyConfig {
    @Autowired
    private final JwtAuthFilter jwtAuthFilter;
    @Autowired
    private final UserDetailsService userDetailsService;

    public SecuirtyConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // disable CSRF globally (needed for Postman/JS)
            .csrf(csrf -> csrf.disable())

            // allow login + H2 console without authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/h2-console").permitAll()
                .requestMatchers("/", "/login", "/auth/**", "/h2-console/**", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )

            
            // allow H2 console to work in browser
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            //.headers(headers -> headers.frameOptions(frame -> frame.disable()))
            // JWT → stateless
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // register JWT filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
     /*@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
