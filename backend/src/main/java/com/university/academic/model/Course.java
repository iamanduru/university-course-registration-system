
package com.university.academic.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "course_code",
        nullable = false,
        unique = true,
        length = 30
    )
    private String courseCode;

    @Column(
        name = "course_name",
        nullable = false,
        length = 150
    )
    private String courseName;

    @Column(
        name = "credits",
        nullable = false
    )
    private Integer credits;

    // RELATIONSHIP 3:
    // Many courses belong to one department.
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
    // A course can be taught by several lecturers.
    // Lecturer is the owning side.
    @ManyToMany(
        mappedBy = "courses",
        fetch = FetchType.LAZY
    )
    private Set<Lecturer> lecturers =
        new LinkedHashSet<>();

    // RELATIONSHIP 8:
    // One course can have many registrations.
    // CourseRegistration owns the foreign key.
    @OneToMany(mappedBy = "course")
    private List<CourseRegistration> registrations =
        new ArrayList<>();

    // Required by JPA
    public Course() {
    }

    // Constructor
    public Course(
        String courseCode,
        String courseName,
        Integer credits
    ) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public Integer getCredits() {
        return credits;
    }

    public Department getDepartment() {
        return department;
    }

    public Set<Lecturer> getLecturers() {
        return lecturers;
    }

    public List<CourseRegistration> getRegistrations() {
        return registrations;
    }

    // SETTERS

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // RELATIONSHIP MANAGEMENT

    // Assign a lecturer to this course.
    public void addLecturer(Lecturer lecturer) {
        if (lecturer == null) {
            throw new IllegalArgumentException(
                "Lecturer cannot be null"
            );
        }

        lecturer.assignCourse(this);
    }

    // Remove a lecturer from this course.
    public void removeLecturer(Lecturer lecturer) {
        if (lecturer != null) {
            lecturer.unassignCourse(this);
        }
    }

    // Add a registration to this course.
    public void addRegistration(
        CourseRegistration registration
    ) {
        if (registration == null) {
            throw new IllegalArgumentException(
                "Course registration cannot be null"
            );
        }

        // Remove from the previous course's
        // in-memory collection if reassigned.
        Course previousCourse =
            registration.getCourse();

        if (previousCourse != null
                && previousCourse != this) {
            previousCourse.getRegistrations()
                          .remove(registration);
        }

        if (!registrations.contains(registration)) {
            registrations.add(registration);
        }

        // Update the owning side.
        registration.setCourse(this);
    }
}
