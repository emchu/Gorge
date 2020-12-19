package com.clothes.controllers;


import com.clothes.model.GetProduct;
import com.clothes.model.entitis.Product;
import com.clothes.services.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Log4j2
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/api/auth/get-product-by-id")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProductById(@RequestBody @NotNull GetProduct getProduct) {
        return productService.getProductById(getProduct);
    }

    @GetMapping("/api/auth/get-products")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "") String category,
                                                        @RequestParam(defaultValue = "") String sex,
                                                        @RequestParam(defaultValue = "") String store,
                                                        @RequestParam(defaultValue = "0") String startPrice,
                                                        @RequestParam(defaultValue = "") String endPrice) {
        return productService.getAllProducts(pageNo, pageSize, sortBy, category, sex, store, startPrice, endPrice);
    }

//    http://localhost:8080/employees?pageSize=5
//    http://localhost:8080/employees?pageSize=5&pageNo=1
//    http://localhost:8080/employees?pageSize=5&pageNo=1&sortBy=email

}
