package com.users.model;

import com.clothes.model.entitis.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User {

    @Column(name = "ID_USER", nullable = false)
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "EMAIL", nullable = false)
    @Getter @Setter
    @Email
    private String email;

    @Column(name = "HASH", nullable = false)
    @Getter @Setter
    private String hash;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    @Setter @Getter
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_product_likes",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_product"))
    @Setter @Getter
    Set<Product> likedProducts = new HashSet<Product>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_product_favourites",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_product"))
    @Setter @Getter
    Set<Product> favoriteProducts = new HashSet<Product>();

    public User(String email, String hash) {
        this.email = email;
        this.hash = hash;
    }

    public User() {}

    public void addProductToLikes(Product product){
        this.likedProducts.add(product);
//        product.getLikes().add(this);
    }

    public void removeProductLike(Product product){
        this.likedProducts.remove(product);
        product.getLikes().remove(this);
    }

    public void addProductToFavourites(Product product){
        this.favoriteProducts.add(product);
//        product.getFavorites().add(this);
    }

    public void removeProductFavourite(Product product){
        this.favoriteProducts.remove(product);
        product.getFavorites().remove(this);
    }
}
