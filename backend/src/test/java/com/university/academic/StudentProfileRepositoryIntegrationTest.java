
package com.university.academic;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import com.university.academic.model.Department;
import com.university.academic.model.Student;
import com.university.academic.model.StudentProfile;

import com.university.academic.repository.DepartmentRepository;
import com.university.academic.repository.StudentRepository;
import com.university.academic.repository.StudentProfileRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
    properties = "spring.jpa.hibernate.ddl-auto=validate"
)
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@Rollback
class StudentProfileRepositoryIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentProfileRepository profileRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveStudentWithOnePersonalProfile() {

        // Generate unique test identifiers.
        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        // Create and save the department.
        Department department = new Department(
            "Student Profile Test Department",
            "D" + suffix
        );

        departmentRepository.saveAndFlush(department);

        // Create the student.
        String studentNumber = "STU-" + suffix;

        Student student = new Student(
            studentNumber,
            "Jane",
            "Doe",
            "jane.test@example.com"
        );

        // Assign the student to the department.
        department.addStudent(student);

        // Create the personal profile.
        StudentProfile profile = new StudentProfile(
            LocalDate.of(2003, 5, 15),
            "Female",
            "+250788123456",
            "Kigali, Rwanda"
        );

        // Establish the one-to-one relationship.
        student.setProfile(profile);

        // Save student and profile using cascade persist.
        Student savedStudent =
            studentRepository.saveAndFlush(student);

        Long studentId = savedStudent.getId();

        // Clear cached entities to verify a fresh database retrieval.
        entityManager.clear();

        // Retrieve the student from PostgreSQL.
        Optional<Student> studentResult =
            studentRepository.findByStudentNumber(
                studentNumber
            );

        assertTrue(studentResult.isPresent());

        // Retrieve the profile using the student ID.
        Optional<StudentProfile> profileResult =
            profileRepository.findByStudent_Id(
                studentId
            );

        assertTrue(profileResult.isPresent());

        // Verify the student-profile relationship.
        assertEquals(
            studentId,
            profileResult.get().getStudent().getId()
        );

        assertEquals(
            "Kigali, Rwanda",
            profileResult.get().getResidentialAddress()
        );

        assertEquals(
            LocalDate.of(2003, 5, 15),
            profileResult.get().getDateOfBirth()
        );

        // Verify that the student has an associated profile.
        assertNotNull(
            studentResult.get().getProfile()
        );

        // Verify the custom repository method.
        assertTrue(
            profileRepository.existsByStudent_Id(
                studentId
            )
        );
    }
}
