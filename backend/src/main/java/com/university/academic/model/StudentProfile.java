
package com.university.academic.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "date_of_birth",
        nullable = false
    )
    private LocalDate dateOfBirth;

    @Column(
        name = "gender",
        nullable = false,
        length = 50
    )
    private String gender;

    @Column(
        name = "phone_number",
        nullable = false,
        length = 30
    )
    private String phoneNumber;

    @Column(
        name = "residential_address",
        nullable = false,
        length = 255
    )
    private String residentialAddress;

    // RELATIONSHIP 4:
    // Each profile belongs to exactly one student.
    // This is the owning side of the relationship.
    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "student_id",
        nullable = false,
        unique = true
    )
    private Student student;

    // Required by JPA
    public StudentProfile() {
    }

    // Constructor
    public StudentProfile(
        LocalDate dateOfBirth,
        String gender,
        String phoneNumber,
        String residentialAddress
    ) {
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.residentialAddress = residentialAddress;
    }

    // GETTERS

    public Long getId() {
        return id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public Student getStudent() {
        return student;
    }

    // SETTERS

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    // Package-private: association is managed through Student.setProfile().
    void setStudent(Student student) {
        this.student = student;
    }
}
