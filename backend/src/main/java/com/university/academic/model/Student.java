
package com.university.academic.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "student_number",
        nullable = false,
        unique = true,
        length = 50
    )
    private String studentNumber;

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

    // RELATIONSHIP 1 => Each student belongs to one department.
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "department_id",
        nullable = false
    )
    private Department department;

    // RELATIONSHIP 4 => Each student must have exactly one profile. StudentProfile owns the foreign key.
    @OneToOne(
        mappedBy = "student",
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        }
    )
    private StudentProfile profile;

    // RELATIONSHIP 7 => One student can have multiple registrations.
    @OneToMany(mappedBy = "student")
    private List<CourseRegistration> registrations =
        new ArrayList<>();

    // Required by JPA
    public Student() {
    }

    // Constructor
    public Student(
        String studentNumber,
        String firstName,
        String lastName,
        String email
    ) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getStudentNumber() {
        return studentNumber;
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

    public StudentProfile getProfile() {
        return profile;
    }

    public List<CourseRegistration> getRegistrations() {
        return registrations;
    }

    // SETTERS

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
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

    public void setProfile(StudentProfile profile) {

        if (profile == null) {
            throw new IllegalArgumentException(
                "Student profile cannot be null"
            );
        }

        if (this.profile != null
                && this.profile != profile) {
            throw new IllegalStateException(
                "Student already has a profile"
            );
        }

        if (profile.getStudent() != null
                && profile.getStudent() != this) {
            throw new IllegalStateException(
                "Profile already belongs to another student"
            );
        }

        this.profile = profile;

        // Synchronize the owning side.
        profile.setStudent(this);
    }

    public void addRegistration(
        CourseRegistration registration
    ) {
        if (registration == null) {
            throw new IllegalArgumentException(
                "Course registration cannot be null"
            );
        }

        Student previousStudent =
            registration.getStudent();

        if (previousStudent != null
                && previousStudent != this) {
            previousStudent.getRegistrations()
                .remove(registration);
        }

        if (!registrations.contains(registration)) {
            registrations.add(registration);
        }

        registration.setStudent(this);
    }

    /* ENTITY LIFECYCLE VALIDATION

     Reject JPA inserts or updates if the student
     has no personal profile or the association
     is inconsistent.*/

    @PrePersist
    @PreUpdate
    private void validateMandatoryProfile() {

        if (profile == null) {
            throw new IllegalStateException(
                "Every student must have a personal profile"
            );
        }

        if (profile.getStudent() != this) {
            throw new IllegalStateException(
                "Student profile relationship is inconsistent"
            );
        }
    }
}
