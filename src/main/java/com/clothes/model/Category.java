package com.clothes.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    @Getter @Setter
    private Product idProduct;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {}
}
