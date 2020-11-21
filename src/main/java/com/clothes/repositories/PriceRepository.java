package com.clothes.repositories;

import com.clothes.model.entitis.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findById(long id);
    Optional<Price> findByValue(String value);
}
