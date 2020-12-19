package com.clothes.services;

import com.clothes.model.GetProduct;
import com.clothes.model.entitis.Category;
import com.clothes.model.entitis.Price;
import com.clothes.model.entitis.Product;
import com.clothes.model.entitis.Store;
import com.clothes.repositories.ProductRepository;
import com.security.payload.ApiResponse;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private PriceService priceService;


    public Product getProductById(long productId) {
        return productRepository.findById(productId)
                .orElse(new Product(0,"","","",
                        new Price(), new ArrayList<>(), new Store(), new Category()));
    }

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

    public ResponseEntity<Page<Product>> getAllProducts(Integer pageNo, Integer pageSize, String sortBy,
                                            String category, String gender, String store,
                                            String startPriceString, String endPriceString) {
        double startPrice;
        double endPrice;

        if (StringUtils.isNumeric(startPriceString)) {
             startPrice = Long.parseLong(startPriceString);
        } else {
            startPrice = 0;
        }

        if (StringUtils.isNumeric(endPriceString)) {
            endPrice = Long.parseLong(endPriceString);
        } else {
            endPrice = priceService.findMaxPrice();
        }

        boolean filterByCategory = !category.equals("");
        boolean filterByGender = !gender.equals("");
        boolean filterByStore = !store.equals("");

        boolean isCategoryGender = filterByCategory && filterByGender;
        boolean isCategoryStore = filterByCategory && filterByStore;
        boolean isGenderStore = filterByGender && filterByStore;

        boolean isCategoryGenderStore = filterByCategory && filterByGender && filterByStore;

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult;

        if (isCategoryGenderStore) {
            pagedResult = filterCategoryGenderStore(paging, category, gender, store, startPrice, endPrice);
        } else if (isCategoryGender) {
            pagedResult = filterCategoryGender(paging, category, gender, startPrice, endPrice);
        } else if (isCategoryStore) {
            pagedResult = filterCategoryStore(paging, category, store, startPrice, endPrice);
        } else if (isGenderStore) {
            pagedResult = filterGenderStore(paging, gender, store, startPrice, endPrice);
        } else if (filterByCategory) {
            pagedResult = filterCategory(paging, category, startPrice, endPrice);
        } else if (filterByGender) {
            pagedResult = filterGender(paging, gender, startPrice, endPrice);
        } else if (filterByStore) {
            pagedResult = filterStore(paging, store, startPrice, endPrice);
        } else {
            pagedResult = filterDefault(paging, startPrice, endPrice);
        }

        if(pagedResult.hasContent()) {
            return ResponseEntity.ok(pagedResult);
        } else {
            return ResponseEntity.badRequest().body(Page.empty());
        }
    }

    public Page<Product> filterCategoryGenderStore(Pageable paging, String category, String gender, String store,
                                                   double startPrice, double endPrice) {
        return  productRepository.findByIdCategoryNameContainingAndSexAndIdStoreNameAndIdPriceValueBetween(
                category, gender, store, startPrice, endPrice, paging
        );
    }

    public Page<Product> filterCategoryGender(Pageable paging, String category, String gender,
                                              double startPrice, double endPrice) {
        return productRepository
                .findByIdCategoryNameContainingAndSexAndIdPriceValueBetween(
                        category, gender, startPrice, endPrice, paging);
    }

    public Page<Product> filterCategoryStore(Pageable paging, String category, String store,
                                             double startPrice, double endPrice) {
        return productRepository
                .findByIdCategoryNameContainingAndIdStoreNameAndIdPriceValueBetween(
                        category, store, startPrice, endPrice, paging);
    }

    public Page<Product> filterGenderStore(Pageable paging, String gender, String store,
                                           double startPrice, double endPrice) {
        return productRepository
                .findBySexAndIdStoreNameAndIdPriceValueBetween(
                        gender, store, startPrice, endPrice, paging);
    }

    public Page<Product> filterCategory(Pageable paging, String category,
                                        double startPrice, double endPrice) {
        return productRepository
                .findByIdCategoryNameContainingAndIdPriceValueBetween(
                        category, startPrice, endPrice, paging);
    }

    public Page<Product> filterGender(Pageable paging, String gender,
                                      double startPrice, double endPrice) {
        return productRepository
                .findBySexAndIdPriceValueBetween(
                        gender, startPrice, endPrice, paging);
    }

    public Page<Product> filterStore(Pageable paging, String store,
                                     double startPrice, double endPrice) {
        return productRepository
                .findByIdStoreNameAndIdPriceValueBetween(
                        store, startPrice, endPrice, paging);
    }

    public Page<Product> filterDefault(Pageable paging,
                                       double startPrice, double endPrice) {
        return productRepository
                .findByIdPriceValueBetween(
                        startPrice, endPrice, paging);
    }

    public ResponseEntity<Page<Product>> getFavouriteProducts(Integer pageNo, Integer pageSize, String sortBy,
                                                              long idUser) {

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findByFavoritesId(idUser, paging);

        if(pagedResult.hasContent()) {
            return ResponseEntity.ok(pagedResult);
        } else {
            return ResponseEntity.badRequest().body(Page.empty());
        }
    }
}
