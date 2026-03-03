package com.diagnocare.hdms.repository;

import com.diagnocare.hdms.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findByIsActiveTrue();
    List<Test> findByCategory(String category);
}
