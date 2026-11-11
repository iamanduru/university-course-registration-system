
# University Course Registration and Academic Management System

## Relational Database Schema

**Database Name:** university_academic_db

**Database Management System:** PostgreSQL

**ORM:** Hibernate / Spring Data JPA

**Project:** Web Technology and Internet Individual Assignment

---

## 1. Introduction

This document defines the proposed relational database
schema for the University Course Registration and
Academic Management System.

The schema is derived from the project requirements
analysis and Entity Relationship Diagram (ERD).

It consists of six entity tables and one join table.

Hibernate will be used to generate the database
tables from the Java entity mappings.

---

## 2. Database Tables

### 2.1 departments

Purpose:
Stores academic departments and identifies the lecturer
serving as the head of each department.

| Column | PostgreSQL Type | Constraints |
|---|---|---|
| id | BIGINT | PRIMARY KEY |
| name | VARCHAR(150) | NOT NULL |
| code | VARCHAR(20) | NOT NULL, UNIQUE |
| head_lecturer_id | BIGINT | FOREIGN KEY, UNIQUE |

Primary Key:
- id

Foreign Key:
- head_lecturer_id REFERENCES lecturers(id)

The head lecturer must belong to the same department.

The head_lecturer_id column may temporarily be NULL
while a department and its lecturers are being created.
A completed department record must have a valid head.

Additional integrity enforcement is required to ensure
that the head belongs to the department and that a
completed department is not left without a head.

---

### 2.2 students

Purpose:
Stores student identification and academic
department membership information.

| Column | PostgreSQL Type | Constraints |
|---|---|---|
| id | BIGINT | PRIMARY KEY |
| student_number | VARCHAR(50) | NOT NULL, UNIQUE |
| first_name | VARCHAR(100) | NOT NULL |
| last_name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | NOT NULL |
| department_id | BIGINT | NOT NULL, FOREIGN KEY |

Primary Key:
- id

Foreign Key:
- department_id REFERENCES departments(id)

Relationship:
- Many students belong to one department.

---

### 2.3 student_profiles

Purpose:
Stores personal information belonging to an
individual student.

| Column | PostgreSQL Type | Constraints |
|---|---|---|
| id | BIGINT | PRIMARY KEY |
| date_of_birth | DATE | NOT NULL |
| gender | VARCHAR(50) | NOT NULL |
| phone_number | VARCHAR(30) | NOT NULL |
| residential_address | VARCHAR(255) | NOT NULL |
| student_id | BIGINT | NOT NULL, UNIQUE, FOREIGN KEY |

Primary Key:
- id

Foreign Key:
- student_id REFERENCES students(id)

Relationship:
- One profile belongs to one student.

The UNIQUE constraint on student_id prevents
multiple profiles from referencing the same student.

The mandatory existence of a profile for every
student requires an additional integrity rule.

---

### 2.4 lecturers

Purpose:
Stores information about lecturers employed by
university departments.

| Column | PostgreSQL Type | Constraints |
|---|---|---|
| id | BIGINT | PRIMARY KEY |
| staff_number | VARCHAR(50) | NOT NULL, UNIQUE |
| first_name | VARCHAR(100) | NOT NULL |
| last_name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | NOT NULL |
| department_id | BIGINT | NOT NULL, FOREIGN KEY |

Primary Key:
- id

Foreign Key:
- department_id REFERENCES departments(id)

Relationship:
- Many lecturers belong to one department.

A lecturer may also serve as the head of a department.

---

### 2.5 courses

Purpose:
Stores courses offered by the academic departments.

| Column | PostgreSQL Type | Constraints |
|---|---|---|
| id | BIGINT | PRIMARY KEY |
| course_code | VARCHAR(30) | NOT NULL, UNIQUE |
| course_name | VARCHAR(150) | NOT NULL |
| credits | INTEGER | NOT NULL |
| department_id | BIGINT | NOT NULL, FOREIGN KEY |

Primary Key:
- id

Foreign Key:
- department_id REFERENCES departments(id)

Relationship:
- Many courses belong to one department.

Proposed validation:
- credits must be greater than zero.

---

### 2.6 course_registrations

Purpose:
Records students registering for academic courses.

| Column | PostgreSQL Type | Constraints |
|---|---|---|
| id | BIGINT | PRIMARY KEY |
| student_id | BIGINT | NOT NULL, FOREIGN KEY |
| course_id | BIGINT | NOT NULL, FOREIGN KEY |
| registration_date | DATE | NOT NULL |
| academic_year | VARCHAR(9) | NOT NULL |
| semester | INTEGER | NOT NULL |
| status | VARCHAR(20) | NOT NULL |
| grade | VARCHAR(10) | NULL permitted |

Primary Key:
- id

Foreign Keys:
- student_id REFERENCES students(id)
- course_id REFERENCES courses(id)

Relationships:
- Many registrations belong to one student.
- Many registrations belong to one course.

Registration status values:
- PENDING
- APPROVED
- REJECTED

The status will be represented as a Java enum
and persisted as a string.

Proposed UNIQUE constraint:
- (student_id, course_id, academic_year, semester)

This prevents duplicate registrations for the same
student, course and academic period.

Grade is nullable because a student may register
before completing the course.

Proposed validation:
- semester must be greater than zero.
- status must be one of the permitted values.

---

### 2.7 lecturer_courses

Purpose:
Represents the many-to-many teaching relationship
between lecturers and courses.

| Column | PostgreSQL Type | Constraints |
|---|---|---|
| lecturer_id | BIGINT | PRIMARY KEY, FOREIGN KEY |
| course_id | BIGINT | PRIMARY KEY, FOREIGN KEY |

Composite Primary Key:
- (lecturer_id, course_id)

Foreign Keys:
- lecturer_id REFERENCES lecturers(id)
- course_id REFERENCES courses(id)

Relationship:
- One lecturer can teach multiple courses.
- One course can be taught by multiple lecturers.

No separate Java entity will be created for
this join table.

Hibernate will manage the relationship through
@ManyToMany and @JoinTable annotations.

---

## 3. Primary Key Summary

| Table | Primary Key |
|---|---|
| departments | id |
| students | id |
| student_profiles | id |
| lecturers | id |
| courses | id |
| course_registrations | id |
| lecturer_courses | lecturer_id, course_id |

All six entity tables use generated BIGINT primary keys.

The lecturer_courses table uses a composite primary key.

---

## 4. Foreign Key Summary

| Table | Foreign Key | Referenced Table |
|---|---|---|
| departments | head_lecturer_id | lecturers |
| students | department_id | departments |
| student_profiles | student_id | students |
| lecturers | department_id | departments |
| courses | department_id | departments |
| course_registrations | student_id | students |
| course_registrations | course_id | courses |
| lecturer_courses | lecturer_id | lecturers |
| lecturer_courses | course_id | courses |

The database uses nine foreign keys.

---

## 5. Unique Constraints

The following uniqueness constraints are proposed:

1. departments.code
2. departments.head_lecturer_id
3. students.student_number
4. student_profiles.student_id
5. lecturers.staff_number
6. courses.course_code
7. course_registrations:
   (student_id, course_id, academic_year, semester)
8. lecturer_courses:
   (lecturer_id, course_id), as the composite primary key

These constraints protect data integrity and
prevent invalid duplicate records.

---

## 6. Relationship Implementation Strategy

| Relationship | Cardinality | Implementation |
|---|---|---|
| Department - Student | 1:N | @OneToMany / @ManyToOne |
| Department - Lecturer | 1:N | @OneToMany / @ManyToOne |
| Department - Course | 1:N | @OneToMany / @ManyToOne |
| Student - StudentProfile | 1:1 | @OneToOne |
| Department - Head Lecturer | N:1 reference | @ManyToOne or @OneToOne based on navigation |
| Lecturer - Course | M:N | @ManyToMany / @JoinTable |
| Student - CourseRegistration | 1:N | @OneToMany / @ManyToOne |
| Course - CourseRegistration | 1:N | @OneToMany / @ManyToOne |

The head lecturer reference will be unique in
the proposed database design.

Student-Course is a conceptual many-to-many
relationship implemented through CourseRegistration.

---

## 7. Important Integrity Considerations

### 7.1 Student Profile Completeness

A UNIQUE, NOT NULL foreign key on student_profiles
ensures that every profile belongs to exactly one
student and prevents duplicate profiles.

However, it does not independently guarantee that
every student has a profile.

The system must enforce complete student-profile
creation through an additional integrity strategy.

### 7.2 Department Head Integrity

Each completed department must have a lecturer
serving as its head.

The head lecturer must belong to the department.

The circular reference between departments
and lecturers requires special handling.

A nullable head reference during initial creation
is a practical implementation option, provided
a separate rule ensures completed records have
a valid department head.

Stronger database enforcement may require additional
PostgreSQL constraints or transactional strategies.

### 7.3 Registration Integrity

The combination of student, course, academic year,
and semester is proposed to be unique.

A student may register for the same course in
a different academic year or semester.

### 7.4 Referential Integrity

Foreign key constraints ensure that related
records reference existing parent records.

Database relationships will be tested after
Hibernate generates the schema.

---

## 8. Hibernate Table Generation

The database will be created in PostgreSQL
before starting the Spring Boot application.

Database name:

university_academic_db

Spring Boot will connect to PostgreSQL through
the configured datasource.

Hibernate will use the JPA entity mappings
to generate the required database tables.

The generated tables will be verified using
pgAdmin.

The implemented mappings and generated database
schema must be compared against this design.

Any differences must be documented and reconciled
with the ERD and requirements analysis.

---

## 9. Verification Plan

The following checks will be performed:

1. Verify PostgreSQL database connectivity.
2. Start the Spring Boot application successfully.
3. Confirm that seven database tables exist.
4. Verify all entity primary keys.
5. Verify foreign key relationships.
6. Verify unique constraints.
7. Verify the lecturer_courses join table.
8. Verify the course_registrations join entity.
9. Test repository persistence operations.
10. Test that invalid or duplicate relationships
    are rejected where constraints apply.
11. Capture pgAdmin screenshots.
12. Document the test results in the repository.

---

## 10. Conclusion

The relational database schema defines the seven
tables required for the University Course Registration
and Academic Management System.

It includes primary keys, foreign keys, uniqueness
constraints, attributes, data types, and relationships.

Six tables correspond to JPA entities, while
lecturer_courses represents the many-to-many
teaching association.

This document, together with the ERD and requirements
analysis, will guide the Spring Boot and Hibernate
implementation.
