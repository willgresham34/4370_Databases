package com.flashcards.p3.flashcard_webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import com.flashcards.p3.flashcard_webapp.services.SetService;

@Controller
public class HomeController {

    private final AccountService _accountService;
    private final SetService _setService;

    @Autowired
    public HomeController(AccountService accountService, SetService setService) {
        _accountService = accountService;
        _setService = setService;
    }

    @GetMapping("/")
    public String home(Model model) {

        // get sets
        // model.addAttribute("sets", sets);
        return "pages/home";
    }

}
