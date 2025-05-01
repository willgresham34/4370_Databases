package com.flashcards.p3.flashcard_webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import com.flashcards.p3.flashcard_webapp.services.SetService;
import com.flashcards.p3.flashcard_webapp.models.Set;

@Controller
public class HomeController {

    private final SetService _setService;

    @Autowired
    public HomeController(SetService setService) {
        _setService = setService;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        try {
            // get sets
            List<Set> sets = _setService.getNewestSets();
            int pageSize = 5;
            int total = sets.size();
            int totalPages = (int) Math.ceil(total / (double) pageSize);

            int from = page * pageSize;
            int to = Math.min(from + pageSize, total);
            List<Set> slice = sets.subList(from, to);

            model.addAttribute("sets", slice);
            model.addAttribute("currentPage", page);
            model.addAttribute("currentPageDisplay", page + 1);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("hasPrevious", page > 0);
            model.addAttribute("hasNext", page < totalPages - 1);
            model.addAttribute("previousPage", page - 1);
            model.addAttribute("nextPage", page + 1);

        } catch (Exception e) {
            model.addAttribute("error", e.toString());
        }

        model.addAttribute("pageCss", "/css/home.css");
        return "pages/home";
    }

}
