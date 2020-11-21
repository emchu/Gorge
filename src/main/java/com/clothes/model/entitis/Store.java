package com.clothes.model.entitis;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "store", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "id_store"
        })
})
public class Store {

    @Column(name = "id_store", nullable = false)
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "store_name", nullable = false)
    @Getter @Setter
    private String name;

    @OneToMany(mappedBy = "idStore", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKey(name = "id_product_store")
    @JsonBackReference
    @Getter
    @Setter
    private List<Product> productList = new ArrayList<>();

    public Store() {}
}
