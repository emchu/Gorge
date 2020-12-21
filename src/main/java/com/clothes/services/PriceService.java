package com.clothes.services;

import com.clothes.repositories.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public BigDecimal findMaxPrice() {
        return  priceRepository.findByMaxPrice();
    }

    public BigDecimal findMimPrice() {
        return  priceRepository.findByMinPrice();
    }

}
