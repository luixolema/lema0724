package com.lema.test.components.tools.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ToolRepository extends JpaRepository<Tool, Long> {
    Optional<Tool> findByToolCode(String toolCode);
}
