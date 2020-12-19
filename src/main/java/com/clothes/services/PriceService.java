package com.clothes.services;

import com.clothes.repositories.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public double findMaxPrice() {
        return  priceRepository.findByMaxPrice();
    }

    public double findMimPrice() {
        return  priceRepository.findByMinPrice();
    }

}
