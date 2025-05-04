package com.flashcards.p3.flashcard_webapp.dtos;

public class FolderUpdateDto {

    private final String folderId;
    private final String folderName;

    public FolderUpdateDto(String folderId, String folderName) {
        this.folderId = folderId;
        this.folderName = folderName;

    }

    public String getFolderId() {
        return folderId;
    }

    public String getFolderName() {
        return folderName;
    }

}
