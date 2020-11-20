package com.clothes.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    @Getter @Setter
    private Product idProduct;
}
