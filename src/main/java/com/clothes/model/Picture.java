package com.clothes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "picture", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "id_picture"
        })
})
public class Picture {

    @Column(name = "id_picture", nullable = false)
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "link", nullable = false)
    @Getter @Setter
    private String link;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_product", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter @Setter
    private Product idProduct;

    public Picture(long id, String link, Product idProduct) {
        this.id = id;
        this.link = link;
        this.idProduct = idProduct;
    }

    public Picture() {}
}
