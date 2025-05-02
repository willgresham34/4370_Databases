package com.flashcards.p3.flashcard_webapp.models;


public class Set {
    /**
     * Unique identifier for the set
     */

    private final String setId;
    private final User user;
    private final String name;
    private final String desc;
    private final String category;
    private final int numCards;


    public Set(String setId, User user, String name, String desc, String category, int numCards) {
        this.setId = setId;
        this.user = user;
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.numCards = numCards;
    }

    public String getSetId() {
        return setId;
    }

    public User getUser() {
        return user;
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

    public int getNumCards() {
        return numCards;
    }


}
