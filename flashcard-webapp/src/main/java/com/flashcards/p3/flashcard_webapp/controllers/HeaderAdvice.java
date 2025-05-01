package com.flashcards.p3.flashcard_webapp.controllers;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class HeaderAdvice {

    private final AccountService accountService;

    @Autowired
    public HeaderAdvice(AccountService accountService) {
        this.accountService = accountService;
    }

    @ModelAttribute("authenticated")
    public boolean authenticated() {
        return accountService.isAuthenticated();
    }
}
