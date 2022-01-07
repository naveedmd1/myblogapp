package com.springboot.blog.repository;

import java.util.Optional;
import com.springboot.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(final String email);

  Optional<User> findByUsernameOrEmail(final String username, final String email);

  Optional<User> findByUsername(final String username);

  Boolean existsByUsername(final String username);

  Boolean existsByEmail(final String email);
}
