
package com.university.academic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.academic.model.Lecturer;

@Repository
public interface LecturerRepository
        extends JpaRepository<Lecturer, Long> {

    // Find a lecturer using their unique staff number.
    Optional<Lecturer> findByStaffNumber(String staffNumber);

    // Check whether a lecturer's staff number exists.
    boolean existsByStaffNumber(String staffNumber);

    // Find all lecturers employed by a department.
    List<Lecturer> findByDepartment_Id(Long departmentId);

    // Find lecturers assigned to teach a particular course.
    List<Lecturer> findByCourses_Id(Long courseId);
}
