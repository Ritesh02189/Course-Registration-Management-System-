package com.crms.courseregistration.controller;

import com.crms.courseregistration.model.Course;
import com.crms.courseregistration.model.Program;
import com.crms.courseregistration.model.User;
import com.crms.courseregistration.repository.CourseRepository;
import com.crms.courseregistration.repository.ProgramRepository;
import com.crms.courseregistration.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private ProgramRepository programRepo;

    // =========================================================
    // 🔹 STUDENT REGISTRATION
    // =========================================================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("role", "student");
        return "register"; // register.html
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  Model model) {

        if (userRepo.findByEmail(email) != null) {
            model.addAttribute("error", "Email already registered!");
            model.addAttribute("role", "student");
            return "register";
        }

        User student = new User();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password); // Note: Hash password in production!
        student.setRole("student");
        userRepo.save(student);

        return "redirect:/login?role=student";
    }

    // =========================================================
    // 🔹 STUDENT DASHBOARD — View All Programs & Courses
    // =========================================================
    @GetMapping("/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("user_email");
        if (email == null) {
            return "redirect:/login?role=student";
        }

        User user = userRepo.findByEmail(email);
        if (user == null) {
            return "redirect:/login?role=student";
        }

        // Fetch all programs (each has courses)
        List<Program> programs = programRepo.findAll();

        // Registered course IDs
        Set<Long> registeredIds = user.getRegisteredCourses()
                .stream()
                .map(Course::getId)
                .collect(Collectors.toSet());

        model.addAttribute("user", user);
        model.addAttribute("programs", programs);
        model.addAttribute("registeredIds", registeredIds);

        return "student_dashboard";
    }

    // =========================================================
    // 🔹 REGISTER COURSE
    // =========================================================
    @GetMapping("/register-course")
    public String registerCourse(@RequestParam Long courseId, HttpSession session) {
        String email = (String) session.getAttribute("user_email");

        if (email == null) {
            return "redirect:/login?role=student";
        }

        User user = userRepo.findByEmail(email);
        Course course = courseRepo.findById(courseId).orElse(null);

        if (user == null || course == null) {
            return "redirect:/student/dashboard";
        }

        // Register only if not already and seats available
        if (!user.getRegisteredCourses().contains(course) && course.getSeats() > 0) {
            user.getRegisteredCourses().add(course);
            course.setSeats(course.getSeats() - 1);
            userRepo.save(user);
            courseRepo.save(course);
        }

        return "redirect:/student/dashboard";
    }

    // =========================================================
    // 🔹 UNREGISTER COURSE
    // =========================================================
    @GetMapping("/unregister-course")
    public String unregisterCourse(@RequestParam Long courseId, HttpSession session) {
        String email = (String) session.getAttribute("user_email");
        if (email == null) return "redirect:/login?role=student";

        User user = userRepo.findByEmail(email);
        if (user == null) return "redirect:/login?role=student";

        Course course = courseRepo.findById(courseId).orElse(null);
        if (course != null && user.getRegisteredCourses().contains(course)) {

            // ✅ Remove course from user's list
            user.getRegisteredCourses().remove(course);

            // ✅ Update seats safely without saving entire entity
            courseRepo.updateSeatsOnUnregister(course.getId());

            // ✅ Save only the user
            userRepo.save(user);
        }

        return "redirect:/student/registered-courses";
    }



    // =========================================================
    // 🔹 VIEW REGISTERED COURSES
    // =========================================================
    @GetMapping("/registered-courses")
    public String viewRegisteredCourses(HttpSession session, Model model) {
        String email = (String) session.getAttribute("user_email");

        if (email == null) {
            return "redirect:/login?role=student";
        }

        User user = userRepo.findByEmail(email);
        if (user == null) {
            return "redirect:/login?role=student";
        }

        Set<Course> registeredCourses = user.getRegisteredCourses();

        model.addAttribute("user", user);
        model.addAttribute("registeredCourses", registeredCourses);

        return "registered_courses";
    }

    // =========================================================
    // 🔹 VIEW TIMETABLE
    // =========================================================
    @GetMapping("/timetable")
    public String viewTimetable(HttpSession session, Model model) {
        String email = (String) session.getAttribute("user_email");
        if (email == null) {
            return "redirect:/login?role=student";
        }

        User user = userRepo.findByEmail(email);
        if (user == null) {
            return "redirect:/login?role=student";
        }

        model.addAttribute("user", user);
        model.addAttribute("registeredCourses", user.getRegisteredCourses());

        return "student_timetable";
    }
}
