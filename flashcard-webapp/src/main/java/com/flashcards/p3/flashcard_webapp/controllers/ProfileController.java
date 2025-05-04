package com.flashcards.p3.flashcard_webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import com.flashcards.p3.flashcard_webapp.services.FolderService;
import com.flashcards.p3.flashcard_webapp.services.SetService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.flashcards.p3.flashcard_webapp.dtos.SetCreateDto;
import com.flashcards.p3.flashcard_webapp.dtos.ViewUserDto;
import com.flashcards.p3.flashcard_webapp.models.Set;
import com.flashcards.p3.flashcard_webapp.models.User;
import com.flashcards.p3.flashcard_webapp.models.Folder;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final AccountService _accountService;
    private final SetService _setService;
    private final FolderService _folderService;

    @Autowired
    public ProfileController(AccountService accountService, SetService setService, FolderService folderService) {
        _accountService = accountService;
        _setService = setService;
        _folderService = folderService;
    }

    @GetMapping("/currentUser")
    public String profile(@RequestParam(value = "error", required = false) String error, Model model) {

        try {
            User user = _accountService.getLoggedInUser();
            List<Set> sets = _setService.currentUserSets();
            List<Folder> folders = _folderService.getUserFolders();

            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("setCount", sets.size());
            model.addAttribute("folders", folders);
            model.addAttribute("hasFolders", !folders.isEmpty());
            model.addAttribute("sets", sets);

        } catch (Exception e) {
            model.addAttribute("error", e.toString());
        }

        model.addAttribute("error", error);
        model.addAttribute("pageCss", "/css/profile.css");

        return "pages/profile";
    }

    @PostMapping("/create-set")
    public String createSet(
            @RequestParam String setName,
            @RequestParam(required = false) String setDescription,
            @RequestParam(required = false) String setCategory) throws UnsupportedEncodingException {
        var user = _accountService.getLoggedInUser();
        SetCreateDto set = new SetCreateDto(setName, setDescription, setCategory);

        try {
            _setService.addSet(set, user.getUserId());
        } catch (Exception e) {
            String message = URLEncoder
                    .encode("Error Adding Set: " + e.toString(), "UTF-8");
            return "redirect:/profile/currentUser?error=" + message;
        }
        return "redirect:/profile/currentUser";
    }

    @PostMapping("/create-folder")
    public String folderSet(@RequestParam String folderName) throws UnsupportedEncodingException {
        try {
            _folderService.addFolder(folderName);
        } catch (Exception e) {
            String message = URLEncoder
                    .encode("Error Adding Set: " + e.toString(), "UTF-8");
            return "redirect:/profile/currentUser?error=" + message;
        }
        return "redirect:/profile/currentUser";
    }

    @GetMapping("/viewUser")
    public String viewProfile(@RequestParam(name = "userId", required = true) String userId, Model model) {

        User user = _accountService.getLoggedInUser();

        if (userId.equals(user.getUserId())) {
            return "redirect:/profile/currentUser";
        }

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

        model.addAttribute("pageCss", "/css/profile.css");
        return "pages/view_profile";
    }
}
