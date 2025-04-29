package com.flashcards.p3.flashcard_webapp.controllers;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalHeaderAdvice {

    private final AccountService _accountService;

    @Autowired
    public GlobalHeaderAdvice(AccountService accountService) {
        _accountService = accountService;
    }

}
