package com.diagnocare.hdms.repository;

import com.diagnocare.hdms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByHealthId(String healthId);
    Optional<User> findByResetToken(String resetToken);
    Boolean existsByEmail(String email);
    List<User> findByRoleAndStatus(User.Role role, User.Status status);
    List<User> findByRole(User.Role role);
    long countByRole(User.Role role);
}
