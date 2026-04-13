package com.crms.courseregistration.controller;

import com.crms.courseregistration.model.User;
import com.crms.courseregistration.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    // Home page with role selection
    @GetMapping({"", "/", "/home"})
    public String homePage() {
        return "index";
    }

    // Show login page for both roles
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String role, Model model) {
        model.addAttribute("role", role);
        return "login";
    }

    // Student login page shortcut
    @GetMapping("/student/login")
    public String studentLoginPage(Model model) {
        model.addAttribute("role", "student");
        return "login";
    }

    // Staff login page shortcut
    @GetMapping("/staff/login")
    public String staffLoginPage(Model model) {
        model.addAttribute("role", "staff");
        return "login";
    }

    // Login form POST for both roles
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String role,
                        Model model,
                        HttpSession session) {

        User user = userRepo.findByEmailAndPassword(email, password);

        if (user != null) {
            // Role check
            if (!role.equalsIgnoreCase(user.getRole())) {
                model.addAttribute("error", "Role does not match your account type.");
                model.addAttribute("role", role);
                return "login";
            }

            // Store user in session
            session.setAttribute("user_email", user.getEmail());

            // Redirect based on role
            if ("admin".equalsIgnoreCase(user.getRole()) || "staff".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/student/dashboard";
            }
        } else {
            model.addAttribute("error", "Invalid Credentials");
            model.addAttribute("role", role);
            return "login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
