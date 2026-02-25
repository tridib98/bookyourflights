package com.tridib04.bookyourflights.customerservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Configuration
public class UserPasswordMigrationRunner {

    @Bean
    CommandLineRunner hashPasswords(DataSource dataSource, PasswordEncoder encoder) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {

                // Find users with NON-hashed passwords
                ResultSet rs = conn.prepareStatement(
                    "SELECT id, password FROM users WHERE password NOT LIKE '$2a$%'"
                ).executeQuery();

                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String rawPassword = rs.getString("password");

                    String hashed = encoder.encode(rawPassword);

                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE users SET password = ? WHERE id = ?"
                    );
                    ps.setString(1, hashed);
                    ps.setLong(2, id);
                    ps.executeUpdate();
                }
            }
        };
    }
}