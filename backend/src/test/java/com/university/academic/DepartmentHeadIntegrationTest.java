
package com.university.academic;

import java.util.UUID;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import com.university.academic.model.Department;
import com.university.academic.model.Lecturer;

import com.university.academic.repository.DepartmentRepository;
import com.university.academic.repository.LecturerRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
    properties = "spring.jpa.hibernate.ddl-auto=validate"
)
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@Rollback
class DepartmentHeadIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldAssignHeadFromSameDepartment() {

        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        Department computerScience = new Department(
            "Computer Science Test",
            "C" + suffix
        );

        Department business = new Department(
            "Business Studies Test",
            "B" + suffix
        );

        departmentRepository.saveAndFlush(computerScience);
        departmentRepository.saveAndFlush(business);

        Lecturer csLecturer = new Lecturer(
            "CS-" + suffix,
            "Jane",
            "Doe",
            "jane@example.com"
        );

        computerScience.addLecturer(csLecturer);
        lecturerRepository.saveAndFlush(csLecturer);

        Lecturer businessLecturer = new Lecturer(
            "BS-" + suffix,
            "John",
            "Smith",
            "john@example.com"
        );

        business.addLecturer(businessLecturer);
        lecturerRepository.saveAndFlush(businessLecturer);

        computerScience.setHeadLecturer(csLecturer);

        departmentRepository.saveAndFlush(computerScience);

        Long departmentId = computerScience.getId();
        Long lecturerId = csLecturer.getId();


        assertThrows(
            IllegalArgumentException.class,
            () -> computerScience.setHeadLecturer(
                businessLecturer
            )
        );

        entityManager.clear();

        Department savedDepartment =
            departmentRepository.findById(departmentId)
                .orElseThrow();

        assertNotNull(savedDepartment.getHeadLecturer());

        assertEquals(
            lecturerId,
            savedDepartment.getHeadLecturer().getId()
        );

        assertEquals(
            departmentId,
            savedDepartment.getHeadLecturer()
                .getDepartment()
                .getId()
        );
    }
}
