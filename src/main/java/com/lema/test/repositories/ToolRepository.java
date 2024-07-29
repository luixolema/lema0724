package com.lema.test.repositories;

import com.lema.test.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToolRepository extends JpaRepository<Tool, Long> {
    Optional<Tool> findByToolCode(String toolCode);
}
