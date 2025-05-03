package com.flashcards.p3.flashcard_webapp.models;

public class User {
    private final String userId;
    private final String firstName;
    private final String lastName;
    private final String username;

    public User(String userId, String firstName, String lastName, String username) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return username;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
