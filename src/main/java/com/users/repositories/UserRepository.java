package com.users.repositories;

import com.users.model.User;
import com.users.model.likes.GetCategoryLikes;
import com.users.model.likes.GetProductLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
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

    @Transactional
    @Query(value="select s.store_name as storeName, count(l.*) AS cnt from user_product_likes l, product p, store s " +
                "where l.id_product = p.id_product and p.id_store=s.id_store " +
                "and l.id_user = :id_user group by s.id_store order by count(l.*) desc;",
            nativeQuery = true)
    List<GetProductLikes> findLikes(@Param("id_user") long idUser);

    Boolean existsByEmail(String email);

    @Transactional
    @Query(value="select cat.name as categoryName, count(l.*) AS cnt from user_product_likes l, product p, category cat " +
            "where l.id_product = p.id_product and p.id_category=cat.id_category " +
            "and l.id_user = :id_user group by cat.id_category order by count(l.*) desc;",
            nativeQuery = true)
    List<GetCategoryLikes> findCategoryLikes(@Param("id_user") long idUser);


}