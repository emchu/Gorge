package com.users.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Column(name = "id_role", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    @Setter @Getter
    private RoleName name;

    public Role() {}

    public Role(RoleName name) {
        this.name = name;
    }
}
