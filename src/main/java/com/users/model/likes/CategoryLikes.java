package com.users.model.likes;

import lombok.Getter;
import lombok.Setter;

public class CategoryLikes implements GetCategoryLikes {

    @Getter
    @Setter
    public String categoryName;

    @Getter
    @Setter
    public int likesCounter;

    public CategoryLikes(String categoryName, int likesCounter) {
        this.categoryName = categoryName;
        this.likesCounter = likesCounter;
    }

    @Override
    public int getCnt() {
        return likesCounter;
    }
}

