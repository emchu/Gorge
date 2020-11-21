package com.clothes.repositories;

import com.clothes.model.entitis.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(long id);
    Optional<Category> findByName(String name);
}
