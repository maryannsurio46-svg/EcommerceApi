package com.ws101.surio.EcommerceApi.repository;
import com.ws101.surio.EcommerceApi.model.User;

// 2. Define the UserRepository
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}