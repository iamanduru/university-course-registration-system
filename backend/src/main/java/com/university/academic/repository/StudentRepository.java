
package com.university.academic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.academic.model.Student;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long> {

    // Find a student by their unique registration number.
    Optional<Student> findByStudentNumber(String studentNumber);

    // Check whether a registration number already exists.
    boolean existsByStudentNumber(String studentNumber);

    // Find all students belonging to a particular department.
    List<Student> findByDepartmentId(Long departmentId);
}
