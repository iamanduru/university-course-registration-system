
package com.university.academic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.academic.model.Course;

@Repository
public interface CourseRepository
        extends JpaRepository<Course, Long> {

    // Find a course using its unique course code.
    Optional<Course> findByCourseCode(String courseCode);

    // Check whether a course code already exists.
    boolean existsByCourseCode(String courseCode);

    // Find courses offered by a particular department.
    List<Course> findByDepartment_Id(Long departmentId);

    // Find courses assigned to a particular lecturer.
    List<Course> findByLecturers_Id(Long lecturerId);
}
