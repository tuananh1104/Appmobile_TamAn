package com.tta.backend_btl_mobile_app.repository;

import com.tta.backend_btl_mobile_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'user' AND u.isActive = true")
    long countActiveRegularUsers();
}
