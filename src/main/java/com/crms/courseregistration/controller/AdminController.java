package com.crms.courseregistration.controller;

import com.crms.courseregistration.model.Course;
import com.crms.courseregistration.model.Program;
import com.crms.courseregistration.model.User;
import com.crms.courseregistration.repository.CourseRepository;
import com.crms.courseregistration.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private CourseRepository courseRepository;

    // 1️⃣ Admin Dashboard → Show all programs & all courses
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Program> programs = programRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("programs", programs);
        model.addAttribute("courses", courses);
        return "admin_dashboard";
    }

    // 2️⃣ Show form to add new Program
    @GetMapping("/program/add")
    public String addProgramForm(Model model) {
        model.addAttribute("program", new Program());
        return "add_program";
    }

    // 3️⃣ Save Program
    @PostMapping("/program/save")
    public String saveProgram(@ModelAttribute Program program) {
        programRepository.save(program);
        return "redirect:/admin/dashboard";
    }

    // 4️⃣ View all Programs
    @GetMapping("/program/list")
    public String listPrograms(Model model) {
        model.addAttribute("programs", programRepository.findAll());
        return "list_programs";
    }

    // 5️⃣ Add Course (Direct from Dashboard)
    @GetMapping("/add_course")
    public String addCourseFormDirect(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("programs", programRepository.findAll()); // For dropdown
        return "add_course";
    }

    // 6️⃣ Add Course under specific Program
    @GetMapping("/program/{id}/course/add")
    public String addCourseForm(@PathVariable Long id, Model model) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));
        Course course = new Course();
        course.setProgram(program);
        model.addAttribute("course", course);
        model.addAttribute("programs", programRepository.findAll()); // Keep dropdown working
        return "add_course";
    }

    // 7️⃣ Save Course
    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute Course course) {
        if (course.getProgram() != null && course.getProgram().getId() != null) {
            Program program = programRepository.findById(course.getProgram().getId())
                    .orElseThrow(() -> new RuntimeException("Program not found"));
            course.setProgram(program);
        }
        courseRepository.save(course);
        return "redirect:/admin/dashboard";
    }

    // 8️⃣ Edit Course
    @GetMapping("/edit-course/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        return courseRepository.findById(id)
                .map(course -> {
                    model.addAttribute("course", course);
                    model.addAttribute("programs", programRepository.findAll());
                    return "edit_course";
                })
                .orElseGet(() -> {
                    model.addAttribute("errorMessage", "Course with ID " + id + " not found!");
                    return "error_page"; // Create error_page.html
                });
    }

    // 9️⃣ Update Course
    @PostMapping("/update-course")
    public String updateCourse(@ModelAttribute Course course) {
        if (course.getProgram() != null && course.getProgram().getId() != null) {
            Program program = programRepository.findById(course.getProgram().getId())
                    .orElseThrow(() -> new RuntimeException("Program not found"));
            course.setProgram(program);
        }
        courseRepository.save(course);
        return "redirect:/admin/dashboard";
    }

    // 🔟 Delete Course
    @GetMapping("/delete-course/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    // 1️⃣1️⃣ View Students in a Course
    @GetMapping("/course/{id}/students")
    public String viewRegisteredStudents(@PathVariable Long id, Model model) {

        Course course = courseRepository.findById(id).orElse(null);

        if (course == null) {
            model.addAttribute("errorMessage", "Course not found or deleted.");
            return "error_page"; // Create a simple error.html page
        }

        // ✅ Get registered students (Set<User>)
        Set<User> students = course.getRegisteredStudents();

        // Optional: convert to list if you want to sort or iterate in order
        List<User> studentList = new ArrayList<>(students);

        if (studentList.isEmpty()) {
            model.addAttribute("warningMessage", "No students have registered for this course yet.");
        }

        model.addAttribute("course", course);
        model.addAttribute("students", studentList);

        return "view_course_students"; // ✅ Create this Thymeleaf template
    }
}
