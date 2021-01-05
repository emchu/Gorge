package com.users.model.likes;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"idProduct" })
public class GetLike {

    @Setter @Getter
    private long idProduct;
}
