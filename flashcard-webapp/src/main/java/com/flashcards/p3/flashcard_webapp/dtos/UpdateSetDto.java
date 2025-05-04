package com.flashcards.p3.flashcard_webapp.dtos;

public class UpdateSetDto {

    private final String setId;
    private final String name;
    private final String desc;
    private final String category;

    public UpdateSetDto(String setId, String name, String desc, String category) {
        this.setId = setId;
        this.name = name;
        this.desc = desc;
        this.category = category;
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
