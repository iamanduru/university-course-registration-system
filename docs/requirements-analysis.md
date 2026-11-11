# University Course Registration and Academic Management System

**Course:** Web Technology and Internet  
**Assessment:** Individual Assignment  
**Student Name:** Mitchelle Anduru  
**Registration Number:** 24752  
**Submission Deadline:** 16 November 2026, 6:00 PM  
**Submission Method:** GitHub Repository Link

---

## 1. Project Overview

### 1.1 Introduction

The University Course Registration and Academic Management System is a database-driven application designed to support the management of academic departments, students, lecturers, courses, and student course registrations.

The system addresses challenges associated with manually maintained academic records, including difficulties in organizing departmental information, managing students and lecturers, assigning courses, and tracking course registrations.

The implementation uses Java, Spring Boot, Spring Data JPA, Hibernate, and PostgreSQL.

### 1.2 Problem Statement

A university currently maintains much of its academic information manually. This makes it difficult to manage student records, organize courses, assign lecturers, and track student registrations.

The university requires a structured academic management system that represents relationships between departments, students, lecturers, courses, and registrations.

The system must ensure that:

1. A university has multiple academic departments.
2. Each department offers multiple courses and employs several lecturers.
3. Each department has one lecturer serving as its head.
4. Every student belongs to one department.
5. Every student has exactly one personal profile, belonging exclusively to that student.
6. Each course belongs to one department.
7. Lecturers can teach multiple courses, and courses can have multiple lecturers.
8. Students can register for multiple courses, and courses can have multiple students.
9. Each course registration records registration date, academic year, semester, registration status, and grade.

### 1.3 Project Objectives

**General Objective**

To design and implement the model and repository layers of a University Course Registration and Academic Management System using Spring Boot, Hibernate, and PostgreSQL.

**Specific Objectives**

1. Identify the entities, attributes, and relationships required by the system.
2. Determine the cardinality of each relationship.
3. Identify appropriate primary keys, foreign keys, join tables, and join entities.
4. Design an Entity Relationship Diagram (ERD).
5. Prepare a relational database schema.
6. Implement entity classes using Java and JPA annotations.
7. Create Spring Data JPA repositories for the entities.
8. Configure PostgreSQL database connectivity.
9. Verify Hibernate entity mappings and database table generation.
10. Document and submit the project through GitHub.

### 1.4 Project Scope

The assignment requires implementing only two application layers:

**Model Layer**

Contains Java entity classes representing the academic data structures, including their attributes, constraints, and relationships.

**Repository Layer**

Contains Spring Data JPA repository interfaces responsible for database persistence operations.

The assignment does not require implementing a frontend, REST controllers, service layer, authentication system, or complete user interface.

### 1.5 Technology Stack

| Component | Technology |
|---|---|
| Programming Language | Java 17 or later |
| Framework | Spring Boot |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Database Name | university_academic_db |
| Build Tool | Maven |
| IDE | Visual Studio Code |
| Version Control | Git and GitHub |
| Project Generation | Spring Initializr |

---

## 2. Requirements Analysis

### 2.1 Academic Department Requirements

- The university contains several academic departments.
- Each department offers multiple courses.
- Each department employs several lecturers.
- Every department has one lecturer designated as its head.

### 2.2 Student Requirements

- Students are admitted into the university.
- Every student belongs to a particular department.
- Every student has exactly one personal profile.
- Every personal profile belongs exclusively to one student.

### 2.3 Student Profile Requirements

Every student's profile must contain:

- Date of birth
- Gender
- Phone number
- Residential address

### 2.4 Lecturer Requirements

- Each lecturer is employed by a department.
- Lecturers can teach multiple courses.
- A course can be taught by multiple lecturers.
- The lecturer-course assignment must be implemented without creating a separate entity class.

### 2.5 Course Requirements

- Every course belongs to one academic department.
- A department can offer several courses.
- Courses can have multiple lecturers.
- Students can register for multiple courses.

### 2.6 Course Registration Requirements

Every course registration must record:

- Registration date
- Academic year
- Semester
- Registration status
- Grade obtained by the student

The supported registration statuses are:

- PENDING
- APPROVED
- REJECTED

Because registration contains additional academic information, it must be represented as a separate entity rather than as a simple many-to-many join table.

---

## 3. Identification of Entities

Six entities have been identified from the problem statement.

| No. | Entity | Description |
|---|---|---|
| 1 | Department | Represents an academic department within the university. |
| 2 | Student | Represents an admitted university student. |
| 3 | StudentProfile | Stores personal information associated with a student. |
| 4 | Lecturer | Represents a lecturer employed by a department. |
| 5 | Course | Represents an academic course offered by a department. |
| 6 | CourseRegistration | Represents the registration of a student for a course, including academic details. |

### 3.1 Justification for Entity Selection

**Department:** Required because departments organize university courses, employ lecturers, and admit students into academic units.

**Student:** Required to represent students and associate them with departments, personal profiles, and course registrations.

**StudentProfile:** Required to represent the individual personal information belonging exclusively to a student.

**Lecturer:** Required to represent academic staff, course teaching assignments, and departmental leadership.

**Course:** Required to represent the academic subjects offered by departments and taught by lecturers.

**CourseRegistration:** Required because the relationship between students and courses contains additional attributes such as academic year, semester, registration date, status, and grade.

The lecturer-course teaching association does not require an additional entity because the assignment explicitly specifies that this relationship must be maintained without creating a separate entity.

---

## 4. Entity Attributes

The attributes below include those explicitly required by the assignment and additional proposed fields needed to identify and describe records.

### 4.1 Department Entity

**Purpose:** Represents a university academic department.

| Attribute | Java Type | Description |
|---|---|---|
| id | Long | Primary key |
| name | String | Department name |
| code | String | Unique department code |

**Example:** Department of Computer Science — CS

**Design justification:** The generated identifier uniquely identifies each department, while the proposed department code provides a recognizable academic identifier.

### 4.2 Student Entity

**Purpose:** Stores identifying information about admitted students.

| Attribute | Java Type | Description |
|---|---|---|
| id | Long | Primary key |
| studentNumber | String | Unique student registration number |
| firstName | String | Student's first name |
| lastName | String | Student's surname |
| email | String | Student's email address |

**Design justification:** A generated primary key provides stable database identity. The proposed student number serves as a university-specific identifier.

The student's department and profile will be represented through entity relationships.

### 4.3 StudentProfile Entity

**Purpose:** Stores personal information belonging to exactly one student.

| Attribute | Java Type | Description |
|---|---|---|
| id | Long | Primary key |
| dateOfBirth | LocalDate | Student's date of birth |
| gender | String | Student's gender |
| phoneNumber | String | Student's contact number |
| residentialAddress | String | Student's residential address |

**Design justification:** Separating personal information into its own entity allows the system to represent the required one-to-one association between a student and their profile.

`LocalDate` is suitable for dates without time information.

`String` is suitable for phone numbers because they may contain leading zeros, country codes, and other formatting characters.

### 4.4 Lecturer Entity

**Purpose:** Represents academic staff employed by university departments.

| Attribute | Java Type | Description |
|---|---|---|
| id | Long | Primary key |
| staffNumber | String | Unique staff identifier |
| firstName | String | Lecturer's first name |
| lastName | String | Lecturer's surname |
| email | String | Lecturer's email address |

**Design justification:** Lecturers require distinct identities because they may teach several courses and one lecturer may serve as the head of a department.

### 4.5 Course Entity

**Purpose:** Represents courses offered by academic departments.

| Attribute | Java Type | Description |
|---|---|---|
| id | Long | Primary key |
| courseCode | String | Unique course code |
| courseName | String | Course title |
| credits | Integer | Number of academic credits |

**Example:** SENG8323 — Software Modelling and Design

**Design justification:** Course codes distinguish courses within the academic catalogue, while course titles and credit values describe their academic characteristics.

The course's department and lecturers will be represented through relationships.

### 4.6 CourseRegistration Entity

**Purpose:** Records a student's registration for an academic course.

| Attribute | Java Type | Description |
|---|---|---|
| id | Long | Primary key |
| registrationDate | LocalDate | Date of course registration |
| academicYear | String | Academic year |
| semester | Integer | Semester number |
| status | RegistrationStatus | Registration status |
| grade | String | Grade awarded |

**Example academic year:** 2026/2027

**Supported registration statuses:**

- PENDING
- APPROVED
- REJECTED

**Design justification:** CourseRegistration is a separate entity because registrations contain additional data that cannot be represented adequately by a simple join table.

The `RegistrationStatus` enum restricts registration statuses to the permitted values.

A nullable grade allows a student to register before the course has been completed or assessed.

### 4.7 Attribute Design Decisions

The following decisions will guide implementation:

1. Entity primary keys will use `Long` with `@Id` and `@GeneratedValue`.
2. Dates without time components will use `LocalDate`.
3. Registration status will use a Java enum.
4. Academic years will use `String` to accommodate values such as 2026/2027.
5. Telephone numbers will use `String`.
6. Grades will use `String` to support letter grades.
7. Unique academic identifiers will be considered for departments, students, lecturers, and courses.
8. Entity relationships will be implemented using JPA annotations and database foreign keys.
9. Relationships requiring additional attributes will use join entities.
10. Relationships that do not require additional attributes may use join tables.

These implementation choices will be checked during database relationship design.

---


## 5. Relationship Analysis

This section identifies the relationships between the six
entities and defines their cardinalities.

### 5.1 Department and Student

Relationship: One-to-Many (1:N)

- One department can have many students.
- Each student belongs to exactly one department.

Justification:
Students must be assigned to a particular academic
department, as required by the problem statement.

### 5.2 Department and Lecturer

Relationship: One-to-Many (1:N)

- One department employs several lecturers.
- Each lecturer belongs to one department.

Justification:
The relationship represents the employment of academic
staff within university departments.

### 5.3 Department and Course

Relationship: One-to-Many (1:N)

- One department offers multiple courses.
- Each course belongs to exactly one department.

Justification:
Courses must be organized under their respective
academic departments.

### 5.4 Student and StudentProfile

Relationship: One-to-One (1:1)

- Every student must have exactly one profile.
- Every profile belongs to exactly one student.

Justification:
The problem statement requires a unique personal
profile for every student.

The implementation must prevent multiple profiles
from being assigned to the same student and ensure
that students receive their required profiles.

### 5.5 Department and Head Lecturer

Relationship: Many-to-One (N:1) at the reference level.

- Every department must have one head lecturer.
- The head lecturer must belong to that department.
- The same lecturer may also teach courses.
- The design will restrict a lecturer to heading
  at most their own department.

Justification:
Departmental leadership is distinct from ordinary
lecturer employment.

The implementation must ensure that the selected
department head is a lecturer employed by that
department.

### 5.6 Lecturer and Course

Relationship: Many-to-Many (M:N)

- One lecturer can teach multiple courses.
- One course can be taught by multiple lecturers.

Justification:
Lecturers and courses have a many-to-many
teaching relationship.

The assignment explicitly requires this relationship
to be implemented without creating a separate
entity class.

A join table will represent the association.

### 5.7 Student and CourseRegistration

Relationship: One-to-Many (1:N)

- One student can have multiple registrations.
- Each registration belongs to exactly one student.

Justification:
Students can register for multiple courses during
their academic studies.

A registration records information about a particular
student enrolling in a course.

### 5.8 Course and CourseRegistration

Relationship: One-to-Many (1:N)

- One course can have multiple registrations.
- Each registration refers to exactly one course.

Justification:
Many students may register for the same course.

Each registration contains additional academic
information, including registration date,
academic year, semester, status, and grade.

### 5.9 Student and Course: Conceptual Relationship

Relationship: Many-to-Many (M:N)

- A student can register for multiple courses.
- A course can have multiple students.

This relationship is implemented through the
CourseRegistration join entity, rather than through
a simple many-to-many join table.

The join entity allows each registration to store
its own academic information.

### 5.10 Relationship Summary

| Relationship | Cardinality |
|---|---|
| Department - Student | 1:N |
| Department - Lecturer | 1:N |
| Department - Course | 1:N |
| Student - StudentProfile | 1:1 |
| Department - Head Lecturer | N:1 reference |
| Lecturer - Course | M:N |
| Student - CourseRegistration | 1:N |
| Course - CourseRegistration | 1:N |

The Student-Course M:N relationship is resolved
through CourseRegistration.

The department head constraint requires an
additional integrity rule to ensure that the
selected lecturer belongs to the department.


### 5.11 Foreign Key Identification

Foreign keys establish referential integrity between
related tables.

The following foreign keys are required:

| Table | Foreign Key | References |
|---|---|---|
| students | department_id | departments(id) |
| lecturers | department_id | departments(id) |
| courses | department_id | departments(id) |
| student_profiles | student_id | students(id) |
| departments | head_lecturer_id | lecturers(id) |
| course_registrations | student_id | students(id) |
| course_registrations | course_id | courses(id) |
| lecturer_courses | lecturer_id | lecturers(id) |
| lecturer_courses | course_id | courses(id) |

The location of each foreign key is determined by
the relationship cardinality.

For one-to-many relationships, the foreign key is
placed on the many side.

For the Student-StudentProfile one-to-one
relationship, the student_id foreign key is stored
in student_profiles and must be unique and non-null.

The Department-Head Lecturer relationship requires
a reference to the selected lecturer.

### 5.12 Identification of Join Tables

The Lecturer-Course relationship is many-to-many.

A lecturer may teach several courses, and a course
may be taught by several lecturers.

A join table named lecturer_courses will represent
this relationship.

Proposed table: lecturer_courses

Attributes:
- lecturer_id (FK)
- course_id (FK)

Primary Key:
- Composite primary key (lecturer_id, course_id)

Foreign Keys:
- lecturer_id references lecturers(id)
- course_id references courses(id)

No separate Java entity will be created for this
join table, as required by the assignment.

The relationship will use the appropriate
JPA @ManyToMany and @JoinTable annotations.

### 5.13 Identification of Join Entities

The Student-Course relationship is conceptually
many-to-many.

However, each registration must contain:

- Registration date
- Academic year
- Semester
- Registration status
- Grade

Therefore, the relationship requires a join entity
named CourseRegistration.

Proposed table: course_registrations

Attributes:
- id (PK)
- student_id (FK)
- course_id (FK)
- registration_date
- academic_year
- semester
- status
- grade

Foreign Keys:
- student_id references students(id)
- course_id references courses(id)

The CourseRegistration entity connects Student
and Course through two many-to-one associations.

This allows registration information to be
stored independently.

### 5.14 Proposed Data Integrity Constraints

The following constraints are proposed:

1. Every entity will have a generated primary key.

2. Department codes, student numbers, lecturer
   staff numbers, and course codes will be unique.

3. Each StudentProfile must reference exactly
   one Student.

4. student_profiles.student_id must be unique
   and non-null.

5. Every Student must have exactly one profile.
   This requirement needs an additional creation
   or integrity rule beyond a unique foreign key.

6. Every Lecturer, Student, and Course must
   reference an existing Department.

7. Each Department must have one head lecturer.
   The head must belong to that department.

8. The selected head lecturer will be unique
   across departments.

9. Each CourseRegistration must reference an
   existing Student and Course.

10. The combination of student_id, course_id,
    academic_year, and semester should be unique
    to prevent duplicate registrations in the
    same academic period.

11. Registration status must be one of:
    PENDING, APPROVED, or REJECTED.

12. Registration grades may be null when the
    student has not yet received a grade.

13. The lecturer_courses table must prevent
    duplicate lecturer-course assignments.

14. Referential integrity must be maintained
    through foreign key constraints.

### 5.15 Database Design Summary

The proposed relational database consists of
seven tables:

1. departments
2. students
3. student_profiles
4. lecturers
5. courses
6. course_registrations
7. lecturer_courses

Six tables correspond to JPA entities.

The lecturer_courses table is a join table
managed through the Lecturer-Course relationship,
not through a separate Java entity.

The final database schema and JPA implementation
will reflect these design decisions.

Special attention will be given to ensuring
that department heads belong to their departments,
that every student has a profile, and that circular
department-head references are handled correctly.

---


## 6. Entity Relationship Diagram (ERD)

The database design consists of six JPA entities
and one additional join table.

The ERD identifies the primary keys, foreign keys,
entity attributes, relationship cardinalities,
and database associations.

The Lecturer-Course relationship uses a join table,
while the Student-Course relationship is implemented
through the CourseRegistration join entity.

The complete editable ERD is available in
[Entity Relationship Diagram](erd.md).

Additional integrity rules, including student profile
completeness and department-head membership, will be
addressed during database implementation.


---


## 7. Relational Database Schema

The University Course Registration and Academic
Management System uses a PostgreSQL database named
`university_academic_db`.

The relational database contains seven tables:

1. departments
2. students
3. student_profiles
4. lecturers
5. courses
6. course_registrations
7. lecturer_courses

Six tables correspond to Java JPA entities.

The lecturer_courses table is a join table used
to implement the many-to-many relationship
between lecturers and courses.

The schema defines the primary keys, foreign keys,
data types, uniqueness constraints, and relationships
required by the system.

The complete schema documentation is available in
[Relational Database Schema](database-schema.md).

Hibernate will generate the database tables from
the Java entity mappings.

The generated schema will be verified against
the documented design during implementation.


---

## 8. Technical Implementation Requirements

The implementation must:

1. Use a Spring Boot project generated through Spring Initializr.
2. Connect to the PostgreSQL database `university_academic_db`.
3. Configure Spring Data JPA and Hibernate.
4. Create a `model` package containing the required entities.
5. Create a `repository` package containing repositories for the entities.
6. Implement entity relationships using suitable JPA annotations.
7. Use `@Id` and `@GeneratedValue` for entity primary keys.
8. Use appropriate Java data types.
9. Use `@JoinColumn` and `@JoinTable` where appropriate.
10. Test entity mappings and verify successful database table generation.

---

## 9. Software Engineering and Quality Standards

In addition to the assignment requirements, the project will follow appropriate engineering practices.

These are supplementary practices, not additional lecturer requirements.

### 9.1 Version Control

- Maintain a GitHub repository.
- Use `main` as the primary branch.
- Create focused feature branches.
- Commit related changes with descriptive messages.
- Use pull requests to review completed features.

### 9.2 Continuous Integration

Use GitHub Actions to automate suitable checks, including:

- Maven build verification
- Java compilation
- Automated tests

### 9.3 Security

- Avoid committing passwords and secrets.
- Use environment variables where appropriate.
- Maintain a suitable `.gitignore`.
- Provide security documentation where useful.
- Apply sensible database constraints.

### 9.4 Documentation

Maintain appropriate project documentation, including:

- README.md
- Requirements analysis
- Entity Relationship Diagram
- Relational database schema
- Setup instructions
- Testing instructions
- Evidence screenshots

---

## 10. Deliverables

The GitHub repository must contain:

1. Problem analysis and identified entities.
2. Entity Relationship Diagram.
3. Relational database schema.
4. Complete Spring Boot project source code.
5. Evidence of PostgreSQL tables and relationships.
6. Screenshots demonstrating successful table generation and entity mappings.

The lecturer must be able to access the repository before the submission deadline.

---

## 11. Development Progress

| Stage | Activity | Status |
|---|---|---|
| 1 | Identify entities | Completed |
| 2 | Identify entity attributes | Completed |
| 3 | Identify relationships | Pending |
| 4 | Determine cardinalities | Pending |
| 5 | Identify foreign keys | Pending |
| 6 | Identify join tables | Pending |
| 7 | Identify join entities | Pending |
| 8 | Draw ERD | Pending |
| 9 | Create relational schema | Pending |
| 10 | Configure Spring Boot and PostgreSQL | Pending |
| 11 | Implement JPA entities | Pending |
| 12 | Implement repositories | Pending |
| 13 | Test entity mappings | Pending |
| 14 | Configure CI and documentation | Pending |
| 15 | Final GitHub submission | Pending |

---

## 12. Conclusion

The requirements analysis has identified six entities necessary for the University's Course Registration and Academic Management System.

The design distinguishes ordinary entities, one-to-one relationships, one-to-many relationships, and many-to-many relationships requiring different database representations.

The next development stage is to analyze the entity relationships, determine their cardinalities, and identify the appropriate database constraints before implementing the system with Spring Boot and Hibernate.