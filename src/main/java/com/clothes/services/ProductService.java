package com.clothes.services;

import com.clothes.model.GetProduct;
import com.clothes.model.entitis.Category;
import com.clothes.model.entitis.Price;
import com.clothes.model.entitis.Product;
import com.clothes.model.entitis.Store;
import com.clothes.repositories.ProductRepository;
import com.security.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> getProductById(GetProduct getProduct) {
        long productId = getProduct.getId();
        Product product = productRepository.findById(productId)
                .orElse(new Product(0,"","","",
                        new Price(), new ArrayList<>(), new Store(), new Category()));
        boolean productExists = !product.getName().equals("");

        if(productExists) {
            return ResponseEntity.ok(product);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Product doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAllProducts(Integer pageNo, Integer pageSize, String sortBy,
                                            String category, String sex, String store) {

        boolean filterByCategory = !category.equals("");
        boolean filterBySex = !sex.equals("");
        boolean filterByStore = !store.equals("");

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult;

        if (filterByCategory && filterBySex && filterByStore) {
            pagedResult =
                    productRepository.findByIdCategoryNameContainingAndSexAndIdStoreName(category, sex, store, paging);
        } else if (filterByCategory && filterBySex) {
            pagedResult = productRepository.findByIdCategoryNameContainingAndSex(category, sex, paging);
        } else if (filterByCategory && filterByStore) {
            pagedResult = productRepository.findByIdCategoryNameContainingAndIdStoreName(category, store, paging);
        } else if (filterBySex && filterByStore) {
            pagedResult = productRepository.findBySexAndIdStoreName(sex, store, paging);
        } else if (filterByCategory) {
            pagedResult = productRepository.findByIdCategoryNameContaining(category, paging);
        } else if (filterBySex) {
            pagedResult = productRepository.findBySex(sex, paging);
        } else if (filterByStore) {
            pagedResult = productRepository.findByIdStoreName(store, paging);
        } else {
            pagedResult = productRepository.findAll(paging);
        }

        if(pagedResult.hasContent()) {
            return ResponseEntity.ok(pagedResult.getContent());
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "No more products"),
                    HttpStatus.BAD_REQUEST);
        }
    }


}
