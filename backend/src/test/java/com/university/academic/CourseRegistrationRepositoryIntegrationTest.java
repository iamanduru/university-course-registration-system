
package com.university.academic;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
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
class CourseRegistrationRepositoryIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseRegistrationRepository registrationRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveCourseRegistration() {

        // unique test identifiers.
        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        Department department = new Department(
            "Registration Test Department",
            "D" + suffix
        );

        departmentRepository.saveAndFlush(department);

        Student student = new Student(
            "STU-" + suffix,
            "Jane",
            "Doe",
            "jane.registration@example.com"
        );

        department.addStudent(student);

        StudentProfile profile = new StudentProfile(
            LocalDate.of(2003, 5, 15),
            "Female",
            "+250788123456",
            "Kigali, Rwanda"
        );

        student.setProfile(profile);

        Student savedStudent =
            studentRepository.saveAndFlush(student);

        Course course = new Course(
            "CS-" + suffix,
            "Database Management Systems",
            3
        );

        department.addCourse(course);

        Course savedCourse =
            courseRepository.saveAndFlush(course);

        CourseRegistration registration =
            new CourseRegistration(
                LocalDate.of(2026, 11, 13),
                "2026/2027",
                1,
                RegistrationStatus.PENDING,
                null
            );

        // Establish both required relationships.
        savedStudent.addRegistration(registration);
        savedCourse.addRegistration(registration);

        CourseRegistration savedRegistration =
            registrationRepository.saveAndFlush(registration);

        Long registrationId = savedRegistration.getId();
        Long studentId = savedStudent.getId();
        Long courseId = savedCourse.getId();

        assertNotNull(registrationId);

        entityManager.clear();

        List<CourseRegistration> studentRegistrations =
            registrationRepository.findByStudent_Id(
                studentId
            );

        assertTrue(
            studentRegistrations.stream()
                .anyMatch(r -> registrationId.equals(r.getId()))
        );

        List<CourseRegistration> courseRegistrations =
            registrationRepository.findByCourse_Id(
                courseId
            );

        assertTrue(
            courseRegistrations.stream()
                .anyMatch(r -> registrationId.equals(r.getId()))
        );

        CourseRegistration retrieved =
            registrationRepository.findById(registrationId)
                .orElseThrow();

        assertEquals(
            RegistrationStatus.PENDING,
            retrieved.getStatus()
        );

        assertNull(retrieved.getGrade());

        assertEquals(
            studentId,
            retrieved.getStudent().getId()
        );

        assertEquals(
            courseId,
            retrieved.getCourse().getId()
        );

        List<CourseRegistration> periodRegistrations =
            registrationRepository
                .findByStudent_IdAndAcademicYearAndSemester(
                    studentId,
                    "2026/2027",
                    1
                );

        assertTrue(
            periodRegistrations.stream()
                .anyMatch(r -> registrationId.equals(r.getId()))
        );

        assertTrue(
            registrationRepository
                .existsByStudent_IdAndCourse_IdAndAcademicYearAndSemester(
                    studentId,
                    courseId,
                    "2026/2027",
                    1
                )
        );

        List<CourseRegistration> pendingRegistrations =
            registrationRepository.findByStatus(
                RegistrationStatus.PENDING
            );

        assertTrue(
            pendingRegistrations.stream()
                .anyMatch(r -> registrationId.equals(r.getId()))
        );
    }
}
