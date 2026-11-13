
package com.university.academic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.academic.model.StudentProfile;

@Repository
public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long> {

    // Find a personal profile using the student's ID.
    Optional<StudentProfile> findByStudent_Id(Long studentId);

    // Check whether a student already has a profile.
    boolean existsByStudent_Id(Long studentId);
}
