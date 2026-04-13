//package com.crms.courseregistration.controller;
//
//import com.crms.courseregistration.model.User;
//import com.crms.courseregistration.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class UserRegistrationController {
//
//    @Autowired
//    private UserRepository userRepo;
//
//    @GetMapping("/register")
//    public String registerPage(@RequestParam(required = false) String role, Model model) {
//        model.addAttribute("role", role);
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String registerUser(@RequestParam String name,
//                               @RequestParam String email,
//                               @RequestParam String password,
//                               @RequestParam String role,
//                               Model model) {
//
//        if (userRepo.findByEmail(email) != null) {
//            model.addAttribute("error", "Email already registered");
//            model.addAttribute("role", role);
//            return "register";
//        }
//
//        User newUser = new User();
//        newUser.setName(name);
//        newUser.setEmail(email);
//        newUser.setPassword(password);
//        newUser.setRole(role);
//
//        userRepo.save(newUser);
//
//        return "redirect:/login?role=" + role;
//    }
//}
