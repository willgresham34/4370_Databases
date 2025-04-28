package com.flashcards.p3.flashcard_webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("body", getBody("pages/home"));
        return "base";
    }

    private String getBody(String pagePath) {
        return "{{> " + pagePath + "}}";
    }

}
