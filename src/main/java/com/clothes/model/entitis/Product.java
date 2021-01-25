package com.clothes.model.entitis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.users.model.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
    private String desc;

    @Column(name = "link", nullable = false)
    @Getter @Setter
    private String link;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonManagedReference
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

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_store", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Store idStore;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_category", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Category idCategory;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_product_likes",
            joinColumns = @JoinColumn(name = "id_product"),
            inverseJoinColumns = @JoinColumn(name = "id_user"))
    @Setter @Getter
    Set<User> likes = new HashSet<User>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_product_favourites",
            joinColumns = @JoinColumn(name = "id_product"),
            inverseJoinColumns = @JoinColumn(name = "id_user"))
    @Setter @Getter
    Set<User> favorites = new HashSet<User>();

    public Product(long id, String name, String sex, String descr, String link,
                   Price idPrice, List<Picture> pictureList, Store idStore, Category idCategory) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.desc = descr;
        this.link = link;
        this.idPrice = idPrice;
        this.pictureList = pictureList;
        this.idStore = idStore;
        this.idCategory = idCategory;
    }

    public Product() {}

    public boolean favouriteExists(long idProduct) {
        Set<Long> ifContains = favorites.stream().map(f -> f.getId()).collect(Collectors.toSet());
        return ifContains.contains(idProduct);
    }

    public boolean likeExists(long idProduct) {
        Set<Long> ifContains = likes.stream().map(f -> f.getId()).collect(Collectors.toSet());
        return ifContains.contains(idProduct);
    }
}
