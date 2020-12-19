package com.clothes.services;

import com.clothes.model.entitis.Store;
import com.clothes.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public List<Store> getStores() {
        return storeRepository.findAll();
    }
}
