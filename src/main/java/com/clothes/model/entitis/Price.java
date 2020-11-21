package com.clothes.model.entitis;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "price", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "id_price"
        })
})
public class Price {

    @Column(name = "id_price", nullable = false)
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "price", nullable = false)
    @Getter @Setter
    private String value;

    @OneToMany(mappedBy = "idPrice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKey(name = "id_product_price")
    @JsonBackReference
    @Getter
    @Setter
    private List<Product> productList = new ArrayList<>();

    public Price(long id, String value, List<Product> productList) {
        this.id = id;
        this.value = value;
        this.productList = productList;
    }

    public Price() {}
}
