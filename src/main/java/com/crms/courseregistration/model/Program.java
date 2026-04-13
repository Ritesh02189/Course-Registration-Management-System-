package com.crms.courseregistration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "program")
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;   // Program name (e.g., "B.Tech CSE")
    private String title;  // Optional description or long title

    // One program -> many courses
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();

    // ---------- Getters & Setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Course> getCourses() {
        if (courses == null) {
            courses = new ArrayList<>();
        }
        return courses;
    }

    public void setCourses(List<Course> courses) { this.courses = courses; }

    // Helper method used by your Thymeleaf template
    public String getProgramName() {
        return name;
    }
}
