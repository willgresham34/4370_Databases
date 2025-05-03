package com.flashcards.p3.flashcard_webapp.dtos;

public class ViewUserDto {
    public final String userId;
    public final String username;
    public final String fullName;

    public ViewUserDto(String userId, String fullName, String username) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
    }
}
