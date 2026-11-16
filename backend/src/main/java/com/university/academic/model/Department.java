
package com.university.academic.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "name",
        nullable = false,
        length = 150
    )
    private String name;

    @Column(
        name = "code",
        nullable = false,
        unique = true,
        length = 20
    )
    private String code;

    // One department has many students.
    @OneToMany(mappedBy = "department")
    private List<Student> students = new ArrayList<>();

    // One department employs many lecturers.
    @OneToMany(mappedBy = "department")
    private List<Lecturer> lecturers = new ArrayList<>();

    // One department offers many courses.
    @OneToMany(mappedBy = "department")
    private List<Course> courses = new ArrayList<>();

    // One lecturer can head at most one department. Nullable during initial department creation.
    @OneToOne(
        fetch = FetchType.LAZY,
        optional = true
    )
    @JoinColumn(
        name = "head_lecturer_id",
        unique = true,
        nullable = true
    )
    private Lecturer headLecturer;

    // Required by JPA
    public Department() {
    }

    public Department(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Lecturer getHeadLecturer() {
        return headLecturer;
    }

    // SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // RELATIONSHIP MANAGEMENT

    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException(
                "Student cannot be null"
            );
        }

        Department previousDepartment =
            student.getDepartment();

        if (previousDepartment != null
                && previousDepartment != this) {
            previousDepartment.getStudents()
                .remove(student);
        }

        if (!students.contains(student)) {
            students.add(student);
        }

        student.setDepartment(this);
    }

    public void addLecturer(Lecturer lecturer) {
        if (lecturer == null) {
            throw new IllegalArgumentException(
                "Lecturer cannot be null"
            );
        }

        Department previousDepartment =
            lecturer.getDepartment();

        if (previousDepartment != null
                && previousDepartment != this) {

            if (previousDepartment.getHeadLecturer()
                    == lecturer) {
                throw new IllegalStateException(
                    "Reassign the department head before " +
                    "transferring this lecturer"
                );
            }

            previousDepartment.getLecturers()
                .remove(lecturer);
        }

        if (!lecturers.contains(lecturer)) {
            lecturers.add(lecturer);
        }

        lecturer.setDepartment(this);
    }

    public void addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException(
                "Course cannot be null"
            );
        }

        Department previousDepartment =
            course.getDepartment();

        if (previousDepartment != null
                && previousDepartment != this) {
            previousDepartment.getCourses()
                .remove(course);
        }

        if (!courses.contains(course)) {
            courses.add(course);
        }

        course.setDepartment(this);
    }

    // DEPARTMENT HEAD MANAGEMENT

    public void setHeadLecturer(Lecturer lecturer) {
        if (lecturer == null) {
            throw new IllegalArgumentException(
                "Head lecturer cannot be null"
            );
        }

        if (lecturer.getDepartment() != this) {
            throw new IllegalArgumentException(
                "Head lecturer must belong to this department"
            );
        }

        this.headLecturer = lecturer;
    }

    /* JPA LIFECYCLE VALIDATION

     During initial creation, the head may be null
     because the department must exist before
     its lecturers can be persisted. */
    @PrePersist
    private void validateInitialDepartment() {
        if (headLecturer != null) {
            validateHeadMembership();
        }
    }

    // Once a department is being updated, require a valid head lecturer.
    @PreUpdate
    private void validateDepartmentUpdate() {
        if (headLecturer == null) {
            throw new IllegalStateException(
                "Department must have a head lecturer before update"
            );
        }

        validateHeadMembership();
    }

    // Check that the selected head belongs to this department.
    private void validateHeadMembership() {
        if (headLecturer != null
                && headLecturer.getDepartment() != this) {
            throw new IllegalStateException(
                "Department head must belong to the same department"
            );
        }
    }
}
