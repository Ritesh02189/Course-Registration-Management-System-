//package com.crms.courseregistration.controller;
//
//import com.crms.courseregistration.model.Course;
//import com.crms.courseregistration.model.User;
//import com.crms.courseregistration.repository.CourseRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin")
//public class CourseController {
//
//    @Autowired
//    private CourseRepository courseRepo;
//
//    // Show all courses
//    @GetMapping("/dashboard")
//    public String adminDashboard(Model model) {
//        model.addAttribute("courses", courseRepo.findAll());
//        return "admin_dashboard";
//    }
//
//    // Show Add Course Form
//    @GetMapping("/add-course")
//    public String addCourseForm(Model model) {
//        model.addAttribute("course", new Course());
//        return "add_course";
//    }
//
//    // Save New Course
//    @PostMapping("/add-course")
//    public String addCourseSubmit(@ModelAttribute Course course) {
//        courseRepo.save(course);
//        return "redirect:/admin/dashboard";
//    }
//
//    // Show Edit Course Form
//    @GetMapping("/edit-course/{id}")
//    public String editCourseForm(@PathVariable Long id, Model model) {
//        Course course = courseRepo.findById(id).orElse(null);
//        if (course == null) {
//            return "redirect:/admin/dashboard";
//        }
//        model.addAttribute("course", course);
//        return "edit_course";
//    }
//
//    //  Fixed: Use same URL for GET/POST with a hidden ID field in form
//    @PostMapping("/edit-course")
//    public String editCourseSubmit(@ModelAttribute Course course) {
//        courseRepo.save(course);
//        return "redirect:/admin/dashboard";
//    }
//
//    // Delete a course
//    @GetMapping("/delete-course/{id}")
//    public String deleteCourse(@PathVariable Long id) {
//        courseRepo.deleteById(id);
//        return "redirect:/admin/dashboard";
//    }
//
//    // View Registered Students
//    @GetMapping("/course/{id}/students")
//    public String viewRegisteredStudents(@PathVariable Long id, Model model) {
//        Course course = courseRepo.findById(id).orElse(null);
//        if (course == null) {
//            return "redirect:/admin/dashboard";
//        }
//        List<User> students = course.getRegisteredStudents();
//        model.addAttribute("course", course);
//        model.addAttribute("students", students);
//        return "view_students"; // create this page
//    }
//}
