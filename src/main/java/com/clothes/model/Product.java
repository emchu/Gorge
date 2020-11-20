package com.clothes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "id_product"
        })
})
public class Product {

    @Column(name = "id_product", nullable = false)
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Column(name = "sex", nullable = false)
    @Getter @Setter
    private String sex;

    @Column(name = "descr", nullable = false)
    @Getter @Setter
    private String descr;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_price", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter @Setter
    private Price idPrice;

    @OneToMany(mappedBy = "idProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKey(name = "id_product_picture")
    @JsonManagedReference
    @Getter
    @Setter
    private List<Picture> pictureList = new ArrayList<>();

    @OneToOne(mappedBy = "idProduct", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    @Getter
    private Store idStore;

    @OneToOne(mappedBy = "idProduct", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    @Getter
    private Category idCategory;

    public Product() {}

    public void setIdStore(Store store) {
        if (store == null) {
            if (this.idStore != null) {
                this.idStore.setIdProduct(null);
            }
        }
        else {
            store.setIdProduct(this);
        }
        this.idStore = store;
    }

    public void setIdCategory(Category category) {
        if (category == null) {
            if (this.idCategory != null) {
                this.idCategory.setIdProduct(null);
            }
        }
        else {
            category.setIdProduct(this);
        }
        this.idCategory = category;
    }
}
