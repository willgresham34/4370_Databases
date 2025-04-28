package com.flashcards.p3.flashcard_webapp.models;


public class Flashcard{

    private final String cardId;
    private final String setId;
    private final String term;
    private final String cardDesc;

    public Flashcard(String cardId, String setId, String term, String cardDesc) {
        this.cardId = cardId;
        this.setId = setId;
        this.term = term;
        this.cardDesc = cardDesc;
    }

    public String getCardId() {
        return cardId;
    }

    public String getSetId() {
        return setId;
    }

    public String getTerm() {
        return term;
    }

    public String getcardDesc() {
        return cardDesc;
    }
}
