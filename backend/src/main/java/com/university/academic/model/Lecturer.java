
package com.university.academic.model;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lecturers")
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "staff_number",
        nullable = false,
        unique = true,
        length = 50
    )
    private String staffNumber;

    @Column(
        name = "first_name",
        nullable = false,
        length = 100
    )
    private String firstName;

    @Column(
        name = "last_name",
        nullable = false,
        length = 100
    )
    private String lastName;

    @Column(
        name = "email",
        nullable = false,
        length = 150
    )
    private String email;

    // RELATIONSHIP 2:
    // Many lecturers belong to one department.
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "department_id",
        nullable = false
    )
    private Department department;

    // RELATIONSHIP 6:
    // Lecturers can teach many courses.
    // Courses can have many lecturers.
    // This is the owning side of the relationship.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "lecturer_courses",
        joinColumns = @JoinColumn(
            name = "lecturer_id",
            referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "course_id",
            referencedColumnName = "id"
        )
    )
    private Set<Course> courses = new LinkedHashSet<>();

    // Required by JPA
    public Lecturer() {
    }

    // Constructor
    public Lecturer(
        String staffNumber,
        String firstName,
        String lastName,
        String email
    ) {
        this.staffNumber = staffNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Department getDepartment() {
        return department;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    // SETTERS

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // RELATIONSHIP MANAGEMENT

    // Assign a course to this lecturer.
    public void assignCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException(
                "Course cannot be null"
            );
        }

        if (courses.add(course)) {
            // Synchronize the inverse side.
            course.getLecturers().add(this);
        }
    }

    // Remove a lecturer's course assignment.
    public void unassignCourse(Course course) {
        if (course != null && courses.remove(course)) {
            course.getLecturers().remove(this);
        }
    }
}
