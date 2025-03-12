package org.wildcodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.MyBlog.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findUserByRoles(Set<String> roles);
}
