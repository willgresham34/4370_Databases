package com.flashcards.p3.flashcard_webapp.dtos;

public class SetCreateDto {

    private final String name;
    private final String desc;
    private final String category;

    public SetCreateDto(String name, String desc, String category) {
        this.name = name;
        this.desc = desc;
        this.category = category;
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
