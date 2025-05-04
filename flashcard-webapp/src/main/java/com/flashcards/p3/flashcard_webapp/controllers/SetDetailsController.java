package com.flashcards.p3.flashcard_webapp.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.flashcards.p3.flashcard_webapp.dtos.FlashcardCreateDto;
import com.flashcards.p3.flashcard_webapp.models.*;
import com.flashcards.p3.flashcard_webapp.services.AccountService;
import com.flashcards.p3.flashcard_webapp.services.FolderService;
import com.flashcards.p3.flashcard_webapp.services.SetService;

import org.springframework.ui.Model;
import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SetDetailsController {

    private final SetService _setService;
    private final AccountService _accountService;
    private final FolderService _folderService;

    @Autowired
    public SetDetailsController(SetService setService, AccountService accountService, FolderService folderService) {
        _setService = setService;
        _accountService = accountService;
        _folderService = folderService;
    }

    @GetMapping("/set-details")
    public String setDetails(@RequestParam(required = true) String setId, @RequestParam(required = false) String error,
            Model model) {

        try {

            FullSet set = _setService.constructFullSet(setId);
            boolean owner = false;
            boolean auth = _accountService.isAuthenticated();
            List<Folder> folders = new ArrayList<Folder>();
            if (auth) {
                String userId = _accountService.getLoggedInUser().getUserId();
                owner = set.getUser().getUserId().equals(userId);
                folders = _folderService.getUserFolders();
            }

            model.addAttribute("name", set.getName());
            model.addAttribute("description", set.getDesc());
            model.addAttribute("category", set.getCategory());
            model.addAttribute("flashcards", set.getCards());
            model.addAttribute("fullName", set.getUser().getFullName());
            model.addAttribute("userId", set.getUser().getUserId());
            model.addAttribute("setId", setId);
            model.addAttribute("owner", owner);
            model.addAttribute("auth", auth);
            model.addAttribute("folders", folders);
        } catch (Exception e) {
            model.addAttribute("error", e.toString());
        }
        model.addAttribute("error", error);
        model.addAttribute("pageCss", "css/set-details.css");
        return "pages/set_details";
    }

    @PostMapping("/create-card")
    public String createCard(
            @RequestParam String setId,
            @RequestParam String term,
            @RequestParam String cardDesc) throws UnsupportedEncodingException {

        try {
            FlashcardCreateDto card = new FlashcardCreateDto(setId, term, cardDesc);
            boolean success = _setService.addFlashcard(card);

            if (success) {
                return "redirect:/set-details?setId=" + setId;
            }

        } catch (Exception e) {
            String message = URLEncoder
                    .encode("Error Adding Set: " + e.toString(), "UTF-8");
            return "redirect:/set-details?setId=" + setId + "&error=" + message;
        }
        return "redirect:/set-details?setId=" + setId + "&error=" + "Error Adding Card";
    }

    @PostMapping("/delete-card")
    public String deleteCard(@RequestParam(required = true) String cardId,
            @RequestParam(required = true) String setId) throws UnsupportedEncodingException {

        try {
            boolean success = _setService.deleteFlashcard(cardId);
            if (success) {
                return "redirect:/set-details?setId=" + setId;
            }

        } catch (Exception e) {
            String message = URLEncoder
                    .encode(e.getMessage(), "UTF-8");
            return "redirect:/set-details?setId=" + setId + "&error=" + message;
        }
        return "redirect:/set-details?setId=" + setId + "&error=" + "Error Deleting Card";
    }

    @PostMapping("/update-card")
    public String updateCard(
            @RequestParam String setId,
            @RequestParam String cardId,
            @RequestParam String term,
            @RequestParam String cardDesc) throws UnsupportedEncodingException {

        try {
            // Flashcard card = new Flashcard(cardId, term, cardDesc);
            // boolean success = _setService.updateFlashcard(card);

            // if (success) {
            // return "redirect:/set-details?setId=" + setId;
            // }

        } catch (Exception e) {
            String message = URLEncoder
                    .encode("Error Updating Set: " + e.toString(), "UTF-8");
            return "redirect:/set-details?setId=" + setId + "&error=" + message;
        }
        return "redirect:/set-details?setId=" + setId + "&error=" + "Error Adding Card";
    }

    @PostMapping("/add-to-folder")
    public String addToFolder(
            @RequestParam String setId,
            @RequestParam String folderId) throws UnsupportedEncodingException {

        try {
            boolean success = _folderService.addSetToFolder(setId, folderId);

            if (success) {
                return "redirect:/set-details?setId=" + setId + "&error=" +
                        "Added%20to%20folder%20" + folderId;
            }

        } catch (Exception e) {
            String message = URLEncoder
                    .encode(e.getMessage(), "UTF-8");
            return "redirect:/set-details?setId=" + setId + "&error=" + message;
        }
        return "redirect:/set-details?setId=" + setId + "&error=" + "Error Adding Card";
    }

}
