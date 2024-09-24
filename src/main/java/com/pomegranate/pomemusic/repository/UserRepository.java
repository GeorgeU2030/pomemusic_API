package com.pomegranate.pomemusic.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pomegranate.pomemusic.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
