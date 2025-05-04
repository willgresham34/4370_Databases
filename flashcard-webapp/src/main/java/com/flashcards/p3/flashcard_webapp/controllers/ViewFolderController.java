package com.flashcards.p3.flashcard_webapp.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.flashcards.p3.flashcard_webapp.services.AccountService;
import com.flashcards.p3.flashcard_webapp.services.FolderService;
import com.flashcards.p3.flashcard_webapp.models.*;
import com.flashcards.p3.flashcard_webapp.dtos.*;

@Controller
public class ViewFolderController {

    private final AccountService _accountService;
    private final FolderService _folderService;

    @Autowired
    public ViewFolderController(FolderService folderService, AccountService accountService) {
        _accountService = accountService;
        _folderService = folderService;
    }

    @GetMapping("/view-folder")
    public String viewFolder(@RequestParam(value = "folderId", required = true) String folderId, Model model) {

        try {
            Folder folder = _folderService.getFolderById(folderId);

            if (_accountService.isAuthenticated()) {
                User user = _accountService.getLoggedInUser();
                if (!folder.getUser().getUserId().equals(user.getUserId())) {
                    String message = URLEncoder
                            .encode("You do not own that set", "UTF-8");
                    return "redirect:/profile/currentUser/" + message;
                }
            }

            model.addAttribute("sets", folder.getSets());
            model.addAttribute("setCount", folder.getNumSets());
            model.addAttribute("folderName", folder.getFolderName());
            model.addAttribute("folderId", folder.getFolderId());

        } catch (Exception e) {
            model.addAttribute("error", e.toString());
        }

        model.addAttribute("pageCss", "/css/view-folder.css");

        return "pages/view_folder";
    }

    @PostMapping("/delete-folder")
    public String deleteFolder(@RequestParam(required = true) String folderId) throws UnsupportedEncodingException {

        try {
            boolean success = _folderService.deleteFolder(folderId);
            if (success) {
                return "redirect:/profile/currentUser";
            }

        } catch (Exception e) {
            String message = URLEncoder
                    .encode(e.getMessage(), "UTF-8");
            return "redirect:/view-folder?folderId=" + folderId + "&error=" + message;
        }
        return "redirect:/view-folder?folderId=" + folderId + "&error=" + "Error%20Deleting%20Folder";
    }

    @PostMapping("/update-folder")
    public String updateFolder(@RequestParam(required = true) String folderId,
            @RequestParam(required = true) String folderName) throws UnsupportedEncodingException {

        try {
            FolderUpdateDto folder = new FolderUpdateDto(folderId, folderName);
            boolean success = _folderService.updateFolder(folder);
            if (success) {
                return "redirect:/profile/currentUser";
            }

        } catch (Exception e) {
            String message = URLEncoder
                    .encode(e.getMessage(), "UTF-8");
            return "redirect:/view-folder?folderId=" + folderId + "&error=" + message;
        }
        return "redirect:/view-folder?folderId=" + folderId + "&error=" + "Error%20Deleting%20Folder";
    }

}
