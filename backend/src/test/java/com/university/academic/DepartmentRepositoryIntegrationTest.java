
package com.university.academic;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import com.university.academic.model.Department;
import com.university.academic.repository.DepartmentRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
    properties = "spring.jpa.hibernate.ddl-auto=validate"
)
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@Rollback
class DepartmentRepositoryIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void shouldSaveAndFindDepartmentByCode() {

        // Generate a unique code to avoid conflicts with existing department records.
        String code = "T" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        // Arrange=> Create a department.
        Department department = new Department(
            "Repository Test Department",
            code
        );

        // Act => Save the department to PostgreSQL.
        Department savedDepartment =
            departmentRepository.saveAndFlush(department);

        // Verify that PostgreSQL generated an ID.
        assertNotNull(savedDepartment.getId());

        // Act => Retrieve the saved department.
        Optional<Department> result =
            departmentRepository.findByCode(code);

        // Assert => The department exists.
        assertTrue(result.isPresent());

        // Assert=> The saved details are correct.
        assertEquals(
            "Repository Test Department",
            result.get().getName()
        );

        assertEquals(
            code,
            result.get().getCode()
        );

        // Verify our custom existence query.
        assertTrue(
            departmentRepository.existsByCode(code)
        );
    }
}
