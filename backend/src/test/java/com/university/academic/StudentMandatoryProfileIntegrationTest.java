
package com.university.academic;

import java.time.LocalDate;
import java.util.UUID;

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
class StudentMandatoryProfileIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentProfileRepository profileRepository;

    // TEST 1 => A student without a personal profile must not be saved through JPA.
    @Test
    void shouldRejectStudentWithoutProfile() {

        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        Department department = new Department(
            "Profile Validation Test",
            "D" + suffix
        );

        departmentRepository.saveAndFlush(department);

        Student student = new Student(
            "STU-" + suffix,
            "Jane",
            "Doe",
            "jane@example.com"
        );

        department.addStudent(student);

        // Student has no profile. JPA lifecycle validation must reject persistence.
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> studentRepository.saveAndFlush(student)
        );

        // Verify that the exception chain contains our mandatory-profile validation message.
        boolean foundValidationMessage = false;
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof IllegalStateException
                    && cause.getMessage() != null
                    && cause.getMessage().contains(
                        "Every student must have a personal profile"
                    )) {
                foundValidationMessage = true;
                break;
            }

            cause = cause.getCause();
        }

        assertTrue(
            foundValidationMessage,
            "Expected mandatory student profile validation"
        );
    }

    // TEST 2 => A student with a valid personal profile must be saved successfully.
    @Test
    void shouldSaveStudentWithValidProfile() {

        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        Department department = new Department(
            "Valid Student Profile Test",
            "V" + suffix
        );

        departmentRepository.saveAndFlush(department);

        Student student = new Student(
            "STU-" + suffix,
            "John",
            "Smith",
            "john@example.com"
        );

        department.addStudent(student);

        StudentProfile profile = new StudentProfile(
            LocalDate.of(2002, 8, 20),
            "Male",
            "+250788123456",
            "Kigali, Rwanda"
        );

        student.setProfile(profile);

        Student savedStudent =
            studentRepository.saveAndFlush(student);

        // Verify successful persistence.
        assertNotNull(savedStudent.getId());

        assertNotNull(savedStudent.getProfile());

        // Verify the profile was persisted.
        assertTrue(
            profileRepository.existsByStudent_Id(
                savedStudent.getId()
            )
        );

        // Verify both sides of the relationship.
        assertEquals(
            savedStudent,
            savedStudent.getProfile().getStudent()
        );
    }
}
