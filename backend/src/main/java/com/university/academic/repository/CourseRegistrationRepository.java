
package com.university.academic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.academic.model.CourseRegistration;
import com.university.academic.model.RegistrationStatus;

@Repository
public interface CourseRegistrationRepository
        extends JpaRepository<CourseRegistration, Long> {

    // Find all registrations belonging to a student.
    List<CourseRegistration> findByStudent_Id(Long studentId);

    // Find all registrations for a particular course.
    List<CourseRegistration> findByCourse_Id(Long courseId);

    // Find registrations by their approval status.
    List<CourseRegistration> findByStatus(
        RegistrationStatus status
    );

    // Find a student's registrations for a specific academic year and semester.
    List<CourseRegistration>
        findByStudent_IdAndAcademicYearAndSemester(
            Long studentId,
            String academicYear,
            Integer semester
        );

    // Check whether a student is already registered for a course during the same academic period.
    boolean
        existsByStudent_IdAndCourse_IdAndAcademicYearAndSemester(
            Long studentId,
            Long courseId,
            String academicYear,
            Integer semester
        );
}
