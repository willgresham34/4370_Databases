package com.flashcards.p3.flashcard_webapp.models;

import java.util.List;

public class FullSet extends Set {

    private final List <Flashcard> cards;

    public FullSet(String setId, User user, String name, String desc, String category, int numCards, List<Flashcard> cards) {
        super(setId, user, name, desc, category, numCards);
        this.cards = cards;
    }

    public List <Flashcard> getCards() {
        return List.copyOf(cards);
    }
    
}
