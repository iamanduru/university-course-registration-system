
package com.university.academic;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;

import com.university.academic.model.Course;
import com.university.academic.model.CourseRegistration;
import com.university.academic.model.Department;
import com.university.academic.model.RegistrationStatus;
import com.university.academic.model.Student;
import com.university.academic.model.StudentProfile;

import com.university.academic.repository.CourseRepository;
import com.university.academic.repository.CourseRegistrationRepository;
import com.university.academic.repository.DepartmentRepository;
import com.university.academic.repository.StudentRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
    properties = "spring.jpa.hibernate.ddl-auto=validate"
)
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@Rollback
class DuplicateRegistrationIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseRegistrationRepository registrationRepository;

    @Test
    void shouldRejectDuplicateRegistrationInSameAcademicPeriod() {

        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        Department department = new Department(
            "Duplicate Registration Test",
            "D" + suffix
        );

        departmentRepository.saveAndFlush(department);

        Student student = new Student(
            "STU-" + suffix,
            "Jane",
            "Doe",
            "jane.duplicate@example.com"
        );

        department.addStudent(student);

        StudentProfile profile = new StudentProfile(
            LocalDate.of(2003, 5, 15),
            "Female",
            "+250788123456",
            "Kigali, Rwanda"
        );

        student.setProfile(profile);

        studentRepository.saveAndFlush(student);

        Course course = new Course(
            "CS-" + suffix,
            "Database Systems",
            3
        );

        department.addCourse(course);

        courseRepository.saveAndFlush(course);

        CourseRegistration firstRegistration =
            new CourseRegistration(
                LocalDate.of(2026, 11, 13),
                "2026/2027",
                1,
                RegistrationStatus.PENDING,
                null
            );

        student.addRegistration(firstRegistration);
        course.addRegistration(firstRegistration);

        CourseRegistration saved =
            registrationRepository.saveAndFlush(
                firstRegistration
            );

        assertNotNull(saved.getId());

        CourseRegistration duplicateRegistration =
            new CourseRegistration(
                LocalDate.of(2026, 11, 13),
                "2026/2027",
                1,
                RegistrationStatus.APPROVED,
                null
            );

        student.addRegistration(duplicateRegistration);
        course.addRegistration(duplicateRegistration);

        assertThrows(
            DataIntegrityViolationException.class,
            () -> registrationRepository.saveAndFlush(
                duplicateRegistration
            )
        );
    }
}
