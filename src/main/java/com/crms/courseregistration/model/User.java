package com.crms.courseregistration.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;  // e.g. "student", "staff", "admin"

    @Column(nullable = false)
    private String name;

    // Many-to-Many relationship with courses
    @ManyToMany
    @JoinTable(
            name = "user_course", // join table name in DB
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> registeredCourses = new HashSet<>();

    // ---------- Constructors ----------
    public User() {}

    public User(String email, String password, String role, String name) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    // ---------- Getters & Setters ----------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    public void setRegisteredCourses(Set<Course> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }

    // ---------- Helper Methods ----------

    // Register for a new course
    public void registerCourse(Course course) {
        registeredCourses.add(course);
    }

    // Unregister (drop) a course
    public void unregisterCourse(Course course) {
        registeredCourses.remove(course);
    }

    // Optional: Simple check
    public boolean isRegisteredFor(Course course) {
        return registeredCourses.contains(course);
    }
}
