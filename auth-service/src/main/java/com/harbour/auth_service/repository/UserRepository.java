package com.harbour.auth_service.repository;

import com.harbour.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>
{
   /**
    * Finds a User by their unique username. Essential for Spring Security's
    * loadUserByUsername method.
    * @param username The username to search for.
    * @return An Optional containing the User if found.
    */
   Optional<User> findByUsername(String username);

   /**
    * Checks if a user with the given username already exists.
    * Used during the registration process.
    * @param username The username to check.
    * @return true if the username exists, false otherwise.
    */
   boolean existsByUsername(String username);
}
