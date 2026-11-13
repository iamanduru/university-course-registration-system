
package com.university.academic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.academic.model.Department;

@Repository
public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    // Find a department using its unique code.
    Optional<Department> findByCode(String code);

    // Check whether a department code already exists.
    boolean existsByCode(String code);
}
