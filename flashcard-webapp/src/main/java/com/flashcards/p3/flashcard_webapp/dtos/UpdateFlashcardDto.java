package com.flashcards.p3.flashcard_webapp.dtos;

public class UpdateFlashcardDto {

    private final String cardId;
    private final String term;
    private final String cardDesc;

    public UpdateFlashcardDto(String cardId, String term, String cardDesc) {
        this.cardId = cardId;
        this.term = term;
        this.cardDesc = cardDesc;
    }

    public String getCardId() {
        return cardId;
    }

    public String getTerm() {
        return term;
    }

    public String getcardDesc() {
        return cardDesc;
    }
}
