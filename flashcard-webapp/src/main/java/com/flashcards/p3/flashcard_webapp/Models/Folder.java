package com.flashcards.p3.flashcard_webapp.models;

import java.util.List;

/**
 * Represents a folder holding basic sers within the flashcard platform.
 */
public class Folder {
    
    /**
     * Unique identifier for the folder.
     */
    private final String folderId;

    /**
     * Name of the folder.
     */
    private final String folderName;

    /**
     * User who created the folder.
     */
    private final User user;

    /**
     * A list of basic sets contained in the folder.
     */
    private final List<Set> sets;

    /**
     * Constructs a folder with specified details.
     * 
     * @param folderId      the unique identifier of the folder
     * @param folderName    the name of the folder
     * @param user          the user who created the folder
     * @param sets          the list of sets within the folder
     */
    public Folder(String folderId, String folderName, User user, List<Set> sets) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.user = user;
        this.sets = sets;
    }

    
    public String getFolderId() {
        return folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public User getUser() {
        return user;
    }


    public List<Set> getSets() {
        return List.copyOf(sets);
    }
}
