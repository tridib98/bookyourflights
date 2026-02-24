package com.tridib04.bookyourflights.customerservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tridib04.bookyourflights.customerservice.entities.User;

public interface UserRepository extends JpaRepository<User,Long>{

    public Optional<User> findByUsername(String username);

}
