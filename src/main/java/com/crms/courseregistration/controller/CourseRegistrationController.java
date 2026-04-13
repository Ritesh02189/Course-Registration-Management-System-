//package com.crms.courseregistration.controller;
//
//import com.crms.courseregistration.model.Course;
//import com.crms.courseregistration.model.Program;
//import com.crms.courseregistration.model.User;
//import com.crms.courseregistration.repository.CourseRepository;
//import com.crms.courseregistration.repository.ProgramRepository;
//import com.crms.courseregistration.repository.UserRepository;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Controller
//@RequestMapping("/student")
//public class CourseRegistrationController {
//
//    @Autowired
//    private UserRepository userRepo;
//
//    @Autowired
//    private CourseRepository courseRepo;
//
//    @Autowired
//    private ProgramRepository programRepo;
//
//    // ✅ Student Dashboard (shows all programs + their courses)
//    @GetMapping("/dashboard")
//    public String studentDashboard(HttpSession session, Model model) {
//        String email = (String) session.getAttribute("user_email");
//
//        if (email == null) {
//            return "redirect:/login?role=student";
//        }
//
//        User user = userRepo.findByEmail(email);
//        if (user == null) {
//            return "redirect:/login?role=student";
//        }
//
//        // Fetch all programs with their courses
//        List<Program> programs = programRepo.findAll();
//
//        // Collect IDs of registered courses for this user
//        Set<Long> registeredIds = user.getRegisteredCourses()
//                .stream()
//                .map(Course::getId)
//                .collect(Collectors.toSet());
//
//        model.addAttribute("programs", programs);
//        model.addAttribute("registeredIds", registeredIds);
//        model.addAttribute("user", user);
//
//        return "student_dashboard";  // ✅ matches your HTML
//    }
//
//    // ✅ Register for a Course
//    @GetMapping("/register-course")
//    public String registerCourse(@RequestParam Long courseId, HttpSession session) {
//        String email = (String) session.getAttribute("user_email");
//
//        if (email == null) {
//            return "redirect:/login?role=student";
//        }
//
//        User user = userRepo.findByEmail(email);
//        if (user == null) {
//            return "redirect:/login?role=student";
//        }
//
//        Course course = courseRepo.findById(courseId).orElse(null);
//        if (course == null) {
//            return "redirect:/student/dashboard";
//        }
//
//        // Only register if seats are available
//        if (!user.getRegisteredCourses().contains(course) && course.getSeats() > 0) {
//            user.getRegisteredCourses().add(course);
//            course.setSeats(course.getSeats() - 1);
//            courseRepo.save(course);
//            userRepo.save(user);
//        }
//
//        return "redirect:/student/dashboard";
//    }
//
//    // ✅ Unregister from a Course
//    @GetMapping("/unregister-course")
//    public String unregisterCourse(@RequestParam Long courseId, HttpSession session) {
//        String email = (String) session.getAttribute("user_email");
//
//        if (email == null) {
//            return "redirect:/login?role=student";
//        }
//
//        User user = userRepo.findByEmail(email);
//        if (user == null) {
//            return "redirect:/login?role=student";
//        }
//
//        Course course = courseRepo.findById(courseId).orElse(null);
//        if (course == null) {
//            return "redirect:/student/dashboard";
//        }
//
//        if (user.getRegisteredCourses().contains(course)) {
//            user.getRegisteredCourses().remove(course);
//            course.setSeats(course.getSeats() + 1);
//            courseRepo.save(course);
//            userRepo.save(user);
//        }
//
//        return "redirect:/student/dashboard";
//    }
//
//    // ✅ My Registered Courses Page
//    @GetMapping("/registered-courses")
//    public String viewRegisteredCourses(HttpSession session, Model model) {
//        String email = (String) session.getAttribute("user_email");
//
//        if (email == null) {
//            return "redirect:/login?role=student";
//        }
//
//        User user = userRepo.findByEmail(email);
//        if (user == null) {
//            return "redirect:/login?role=student";
//        }
//
//        Set<Course> registeredCourses = user.getRegisteredCourses();
//
//        model.addAttribute("registeredCourses", registeredCourses);
//        model.addAttribute("user", user);
//
//        return "registered_courses"; // ✅ this matches your second HTML
//    }
//}
