package com.flashcards.p3.flashcard_webapp.controllers;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final AccountService _accountService;

    @Autowired
    public GlobalControllerAdvice(AccountService accountService) {
        _accountService = accountService;
    }

    @ModelAttribute("authenticated")
    public boolean authenticated() {
        return _accountService.isAuthenticated();
    }

}
