package com.flashcards.p3.flashcard_webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.flashcards.p3.flashcard_webapp.models.FullSet;
import com.flashcards.p3.flashcard_webapp.services.SetService;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SetDetailsController {

    private final SetService _setService;

    @Autowired
    public SetDetailsController(SetService setService) {
        _setService = setService;
    }

    @GetMapping("/set-details")
    public String setDetails(@RequestParam(name = "setId") String setId, Model model) {

        try {

            FullSet set = _setService.constructFullSet(setId);

            model.addAttribute("name", set.getName());
            model.addAttribute("description", set.getDesc());
            model.addAttribute("category", set.getCategory());
            model.addAttribute("flashcards", set.getCards());
            model.addAttribute("fullName", set.getUser().getFullName());
        } catch (Exception e) {
            model.addAttribute("error", e.toString());
        }

        model.addAttribute("pageCss", "css/set-details.css");
        return "pages/set_details";
    }

}
