package com.flashcards.p3.flashcard_webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.flashcards.p3.flashcard_webapp.dtos.*;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;

@Controller
public class AuthController {

    private final AccountService _accountService;

    @Autowired
    public AuthController(AccountService accountService) {
        _accountService = accountService;
    }

    @GetMapping("/login")
    public String getLogin(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("title", "Login");
        model.addAttribute("body", "{{> pages/login}}");
        model.addAttribute("error",  error != null ? "Invalid username or password." : null;
        return "base";
    }

    @PostMapping("/login")
    public String postLogin(@RequestBody LoginUserDto user) {

        if (user.getUsername() == null || user.getUsername().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank()) {
            String message = URLEncoder.encode("Username and Password cannot be blank", "UTF-8");
            return "redirect:/login?error=" + message;
        }

        try {
            boolean success = _accountService.loginUser(user);

            if (!success) {
                String message = URLEncoder
                        .encode("Login failed. Please try different Credentials", "UTF-8");
                return "redirect:/login?error=" + message;
            }

            return "redirect:/";
        } catch (Exception e) {
            String message = URLEncoder
                    .encode("An error occurred: " + e.getMessage(), "UTF-8");
            return "redirect:/login?error=" + message;
        }
    }

    @GetMapping("/register")
    public String getRegister(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("title", "Register");
        model.addAttribute("body", "{{> pages/register}}");
        model.addAttribute("error", error != null ? "Invalid username or password." : null);
        return "base";
    }

    @PostMapping("/register")
    public String postRegister(@RequestBody RegisterUserDto user) {
        if (user.getPassword().trim().length() < 8) {
            String message = URLEncoder.encode("Passwords should be at least 8 characters long.", "UTF-8");
            return "redirect:/register?error=" + message;
        }

        if (!user.getPassword().equals(user.getPasswordRepeat())) {
            String message = URLEncoder
                    .encode("Passwords must match.", "UTF-8");
            return "redirect:/register?error=" + message;
        }

        try {
            boolean success = _accountService.registerUser(user);

            if (!success) {
                String message = URLEncoder
                        .encode("Registration failed. Please try again.", "UTF-8");
                return "redirect:/register?error=" + message;
            }

            return "redirect:/login";
        } catch (Exception e) {
            String message = URLEncoder
                    .encode("An error occurred: " + e.getMessage(), "UTF-8");
            return "redirect:/register?error=" + message;
        }

    }

}
