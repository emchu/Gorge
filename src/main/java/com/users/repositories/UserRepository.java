package com.users.repositories;

import com.clothes.model.entitis.Product;
import com.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);
    Optional<User> findByEmail(String mail);

    @Query(value = "select users.hash from users where users.email = :email", nativeQuery = true)
    String findHashByEmail(@Param("email") String email);

    @Query(value = "select u.email from users u where u.email = :email",
            nativeQuery = true)
    String findMail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value="delete from user_product_favourites where id_product = :id_product and id_user = :id_user",
            nativeQuery = true)
    void deleteFavourite(@Param("id_product") long idProduct, @Param("id_user") long idUser);

    @Modifying
    @Transactional
    @Query(value="delete from user_product_likes where id_product = :id_product and id_user = :id_user",
            nativeQuery = true)
    void deleteLike(@Param("id_product") long idProduct, @Param("id_user") long idUser);

    Boolean existsByEmail(String email);

}