package com.clothes.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@JsonPropertyOrder({ "id"})
public class GetProduct {

    @Getter
    @Setter
    private long id;
}
