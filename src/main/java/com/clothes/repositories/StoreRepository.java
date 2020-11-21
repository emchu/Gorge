package com.clothes.repositories;

import com.clothes.model.entitis.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findById(long id);
    Optional<Store> findByName(String name);
}
