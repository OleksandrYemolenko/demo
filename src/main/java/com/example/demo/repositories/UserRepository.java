package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for work with user
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by its guid.
     *
     * @param guid
     * @return the user with the given guid or {@literal Optional#empty()} if none found
     */
    Optional<User> findByGuid(UUID guid);


    /**
     * Retrieves a user by its guid.
     *
     * @param email
     * @return the user with the given email
     */
    Optional<User> findByEmail(String email);

}
