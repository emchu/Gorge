package com.clothes.repositories;

import com.clothes.model.entitis.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findById(long id);
    Optional<Price> findByValue(double value);

    @Query(value = "SELECT MAX(price) FROM price", nativeQuery = true)
    public double findByMaxPrice();

    @Query(value = "SELECT MIN(price) FROM price", nativeQuery = true)
    public double findByMinPrice();
}
