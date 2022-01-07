package com.springboot.blog.repository;

import java.util.Optional;
import com.springboot.blog.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByName(final String name);
}
