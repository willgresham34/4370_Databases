package com.flashcards.p3.flashcard_webapp.models;
import java.util.ArrayList;


public class Set {
    /**
     * Unique identifier for the set
     */

    private final String userId;
    private final String setId;
    private final String name;
    private final String desc;
    private final String category;
    private final ArrayList <Flashcard> cards;


    public Set(String userId, String setId, String name, String desc, String category, ArrayList <Flashcard> cards) {
        this.userId = userId;
        this.setId = setId;
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.cards = cards;
    }

    public String getSetId() {
        return setId;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getCategory() {
        return category;
    }


}
