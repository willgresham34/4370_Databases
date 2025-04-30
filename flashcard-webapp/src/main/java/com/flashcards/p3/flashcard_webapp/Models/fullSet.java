package com.flashcards.p3.flashcard_webapp.models;

import java.util.List;

public class fullSet extends Set {

    private final List <Flashcard> cards;

    public fullSet(String setId, String name, String desc, String category, List <Flashcard> cards) {
        super(setId, name, desc, category);
        this.cards = cards;
    }

    public List <Flashcard> getCards() {
        return List.copyOf(cards);
    }
}
