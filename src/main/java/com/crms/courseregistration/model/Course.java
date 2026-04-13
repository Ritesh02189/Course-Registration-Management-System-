package com.crms.courseregistration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String courseCode;

    private String title;
    private String description;
    private int seats;

    // Each course belongs to one program
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "program_id", nullable = false)
    @JsonIgnore
    private Program program;

    // Many students can register for a course
    @ManyToMany(mappedBy = "registeredCourses", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> registeredStudents = new HashSet<>();

    // ---------- Getters & Setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public Program getProgram() { return program; }
    public void setProgram(Program program) { this.program = program; }

    public Set<User> getRegisteredStudents() { return registeredStudents; }
    public void setRegisteredStudents(Set<User> registeredStudents) { this.registeredStudents = registeredStudents; }
}
