package com.users.model.likes;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"idProduct", "idUser" })
public class GetLike {

    @Setter @Getter
    private long idUser;

    @Setter @Getter
    private long idProduct;
}
