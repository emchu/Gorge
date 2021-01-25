package com.clothes.repositories;

import com.clothes.model.entitis.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;


@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findById(long id);

    @Query(value = "SELECT MAX(price) FROM price", nativeQuery = true)
    public BigDecimal findByMaxPrice();

    @Query(value = "SELECT MIN(price) FROM price", nativeQuery = true)
    public BigDecimal findByMinPrice();
}
