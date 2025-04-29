package com.flashcards.p3.flashcard_webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final AccountService _accountService;

    @Autowired
    public ProfileController(AccountService accountService) {
        _accountService = accountService;
    }

    @GetMapping
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

}
