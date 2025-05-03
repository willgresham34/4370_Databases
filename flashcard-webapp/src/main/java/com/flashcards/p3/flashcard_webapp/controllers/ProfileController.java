package com.flashcards.p3.flashcard_webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import com.flashcards.p3.flashcard_webapp.services.SetService;
import java.util.List;

import com.flashcards.p3.flashcard_webapp.dtos.ViewUserDto;
import com.flashcards.p3.flashcard_webapp.models.Set;
import com.flashcards.p3.flashcard_webapp.models.User;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final AccountService _accountService;
    private final SetService _setService;

    @Autowired
    public ProfileController(AccountService accountService, SetService setService) {
        _accountService = accountService;
        _setService = setService;
    }

    @GetMapping("/currentUser")
    public String profile(Model model) {
        try {
            User user = _accountService.getLoggedInUser();
            List<Set> sets = _setService.currentUserSets();

            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("username", user.getUserName());
            model.addAttribute("sets", sets);

        } catch (Exception e) {
            model.addAttribute("error", e.toString());
        }

        return "pages/profile";
    }

    @GetMapping("/viewUser")
    public String viewProfile(@RequestParam(name = "userId") String userId, Model model) {

        try {
            // User userObj = _accountService.getUserById(userId);
            // ViewUserDto user = new ViewUserDto(userObj.getUserId(),
            // userObj.getFullName(), userObj.getUserName());

            // List<Set> sets = _setService.currentUserSets();

            // model.addAttribute("user", user);
            // model.addAttribute("sets", sets);

        } catch (Exception e) {
            model.addAttribute("error", e.toString());
        }

        return "pages/profile";
    }

}
