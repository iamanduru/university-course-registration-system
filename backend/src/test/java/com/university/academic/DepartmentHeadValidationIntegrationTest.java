
package com.university.academic;

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
class DepartmentHeadValidationIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void shouldRejectDepartmentUpdateWithoutHeadLecturer() {

        // Generate a unique department code.
        String suffix = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        // STEP 1 => Create a department without a head.

        Department department = new Department(
            "Initial Department",
            "D" + suffix
        );

        departmentRepository.saveAndFlush(department);

        assertNotNull(department.getId());

        // STEP 2 => Attempt to update the department without assigning a head lecturer.
        department.setName("Updated Department");

        // STEP 3 => The @PreUpdate callback must reject it.
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> departmentRepository.flush()
        );

        // STEP 4 => Verify that our validation caused
        // the rejection, not an unrelated database error.
        boolean validationTriggered = false;

        Throwable cause = exception;

        while (cause != null) {

            if (cause instanceof IllegalStateException
                    && cause.getMessage() != null
                    && cause.getMessage().contains(
                        "Department must have a head lecturer"
                    )) {

                validationTriggered = true;
                break;
            }

            cause = cause.getCause();
        }

        assertTrue(
            validationTriggered,
            "Expected mandatory department-head validation"
        );
    }
}
