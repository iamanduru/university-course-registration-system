
package com.university.academic;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import com.university.academic.model.Department;
import com.university.academic.model.Lecturer;
import com.university.academic.model.Course;

import com.university.academic.repository.DepartmentRepository;
import com.university.academic.repository.LecturerRepository;
import com.university.academic.repository.CourseRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
    properties = "spring.jpa.hibernate.ddl-auto=validate"
)
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@Rollback
class LecturerCourseRepositoryIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveLecturerCourseAssignment() {


        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);


        Department department = new Department(
            "Software Engineering Test",
            "D" + suffix
        );

        departmentRepository.saveAndFlush(department);


        Lecturer lecturer = new Lecturer(
            "LEC-" + suffix,
            "Jane",
            "Doe",
            "jane.lecturer@example.com"
        );

        department.addLecturer(lecturer);

        Lecturer savedLecturer =
            lecturerRepository.saveAndFlush(lecturer);


        Course course = new Course(
            "CS-" + suffix,
            "Java Programming",
            3
        );

        department.addCourse(course);

        Course savedCourse =
            courseRepository.saveAndFlush(course);


        savedLecturer.assignCourse(savedCourse);


        lecturerRepository.flush();

        Long lecturerId = savedLecturer.getId();
        Long courseId = savedCourse.getId();


        entityManager.clear();


        List<Lecturer> lecturers =
            lecturerRepository.findByCourses_Id(courseId);

        assertEquals(1, lecturers.size());

        assertEquals(
            lecturerId,
            lecturers.get(0).getId()
        );


        List<Course> courses =
            courseRepository.findByLecturers_Id(lecturerId);

        assertEquals(1, courses.size());

        assertEquals(
            courseId,
            courses.get(0).getId()
        );


        assertEquals(
            "Java Programming",
            courses.get(0).getCourseName()
        );
    }
}
