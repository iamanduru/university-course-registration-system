
package com.university.academic.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "course_registrations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_registration_student_course_period",
            columnNames = {
                "student_id",
                "course_id",
                "academic_year",
                "semester"
            }
        )
    }
)
public class CourseRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "registration_date",
        nullable = false
    )
    private LocalDate registrationDate;

    @Column(
        name = "academic_year",
        nullable = false,
        length = 9
    )
    private String academicYear;

    @Column(
        name = "semester",
        nullable = false
    )
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 20
    )
    private RegistrationStatus status;

    @Column(
        name = "grade",
        length = 10
    )
    private String grade;

    // RELATIONSHIP 7:
    // Many registrations belong to one student.
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "student_id",
        nullable = false
    )
    private Student student;

    // RELATIONSHIP 8:
    // Many registrations belong to one course.
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "course_id",
        nullable = false
    )
    private Course course;

    // Required by JPA
    public CourseRegistration() {
    }

    // Constructor
    public CourseRegistration(
        LocalDate registrationDate,
        String academicYear,
        Integer semester,
        RegistrationStatus status,
        String grade
    ) {
        this.registrationDate = registrationDate;
        this.academicYear = academicYear;
        this.semester = semester;
        this.status = status;
        this.grade = grade;
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public Integer getSemester() {
        return semester;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public String getGrade() {
        return grade;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    // SETTERS

    public void setRegistrationDate(
        LocalDate registrationDate
    ) {
        this.registrationDate = registrationDate;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
