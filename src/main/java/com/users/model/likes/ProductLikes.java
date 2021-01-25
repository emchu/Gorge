package com.users.model.likes;

import lombok.Getter;
import lombok.Setter;

public class ProductLikes implements GetProductLikes{

    @Getter @Setter
    public String storeName;

    @Getter @Setter
    public int likesCounter;

    public ProductLikes(String storeName, int likesCounter) {
        this.storeName = storeName;
        this.likesCounter = likesCounter;
    }

    @Override
    public int getCnt() {
        return likesCounter;
    }
}
