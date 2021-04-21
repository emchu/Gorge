package com.clothes.repositories;

import com.clothes.model.entitis.Category;
import com.clothes.model.entitis.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(long id);
    Optional<Category> findByName(String name);

    @Transactional
    @Query(value="select category.id_category from category " +
            "where name = :name",
            nativeQuery = true)
    long getIdByName(@Param("name") String name);
}
