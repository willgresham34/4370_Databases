package com.flashcards.p3.flashcard_webapp.dtos;

public class FlashcardCreateDto {

    private final String setId;
    private final String term;
    private final String cardDesc;

    public FlashcardCreateDto(String setId, String term, String cardDesc) {
        this.setId = setId;
        this.term = term;
        this.cardDesc = cardDesc;
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
