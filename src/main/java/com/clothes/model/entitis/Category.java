package com.clothes.model.entitis;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "category", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        })
})
public class Category {

    @Column(name = "id_category", nullable = false)
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @OneToMany(mappedBy = "idCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKey(name = "id_product_category")
    @JsonBackReference
    @Getter
    @Setter
    private List<Product> productList = new ArrayList<>();

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {}
}
