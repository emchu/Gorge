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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PriceService priceService;


    public Product getProductById(long productId) {
        return productRepository.findById(productId)
                .orElse(new Product(0,"","","","",
                        new Price(), new ArrayList<>(), new Store(), new Category()));
    }

    public ResponseEntity<?> getProductById(GetProduct getProduct) {
        long productId = getProduct.getId();
        Product product = productRepository.findById(productId)
                .orElse(new Product(0,"","","","",
                        new Price(), new ArrayList<>(), new Store(), new Category()));
        boolean productExists = !product.getName().equals("");

        if(productExists) {
            return ResponseEntity.ok(product);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Product doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Page<Product>> getProducts(Integer pageNo, Integer pageSize, String sortBy,
                                                     String category, String gender, String store,
                                                     String startPrice, String endPrice,
                                                     String name) {
        if(name.equals("")) {
            return getAllProducts(pageNo, pageSize, sortBy, category, gender, store, startPrice, endPrice);
        } else {
            return getSearchedProducts(pageNo, pageSize, sortBy, name);
        }
    }

    public ResponseEntity<Page<Product>> getSearchedProducts(Integer pageNo, Integer pageSize, String sortBy,
                                                             String name) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findByNameContaining(name, paging);

        if(pagedResult.hasContent()) {
            return ResponseEntity.ok(pagedResult);
        } else {
            return ResponseEntity.badRequest().body(Page.empty());
        }
    }

    public ResponseEntity<Page<Product>> getAllProducts(Integer pageNo, Integer pageSize, String sortBy,
                                            String category, String gender, String store,
                                            String startPriceString, String endPriceString) {

        List<BigDecimal> priceList = assignPrice( startPriceString,  endPriceString);
        BigDecimal startPrice = priceList.get(0);
        BigDecimal endPrice = priceList.get(1);

        List<Boolean> booleanList = filterBooleans(category, gender, store);

        boolean filterByCategory = booleanList.get(0);
        boolean filterByGender =  booleanList.get(1);
        boolean filterByStore =  booleanList.get(2);

        boolean isCategoryGender =  booleanList.get(3);
        boolean isCategoryStore =  booleanList.get(4);
        boolean isGenderStore =  booleanList.get(5);

        boolean isCategoryGenderStore = booleanList.get(6);

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
                                                   BigDecimal startPrice, BigDecimal endPrice) {
        return  productRepository.findByIdCategoryNameContainingAndSexAndIdStoreNameAndIdPriceValueBetween(
                category, gender, store, startPrice, endPrice, paging
        );
    }

    public Page<Product> filterCategoryGenderStore(long idUser, Pageable paging, String category, String gender, String store,
                                                   BigDecimal startPrice, BigDecimal endPrice) {
        return  productRepository.findByFavoritesIdAndIdCategoryNameContainingAndSexAndIdStoreNameAndIdPriceValueBetween(
                idUser, category, gender, store, startPrice, endPrice, paging
        );
    }

    public Page<Product> filterCategoryGender(Pageable paging, String category, String gender,
                                              BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByIdCategoryNameContainingAndSexAndIdPriceValueBetween(
                        category, gender, startPrice, endPrice, paging);
    }

    public Page<Product> filterCategoryGender(long idUser, Pageable paging, String category, String gender,
                                              BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByFavoritesIdAndIdCategoryNameContainingAndSexAndIdPriceValueBetween(
                        idUser, category, gender, startPrice, endPrice, paging);
    }

    public Page<Product> filterCategoryStore(Pageable paging, String category, String store,
                                             BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByIdCategoryNameContainingAndIdStoreNameAndIdPriceValueBetween(
                        category, store, startPrice, endPrice, paging);
    }

    public Page<Product> filterCategoryStore(long idUser, Pageable paging, String category, String store,
                                             BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByFavoritesIdAndIdCategoryNameContainingAndIdStoreNameAndIdPriceValueBetween(
                        idUser, category, store, startPrice, endPrice, paging);
    }

    public Page<Product> filterGenderStore(Pageable paging, String gender, String store,
                                           BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findBySexAndIdStoreNameAndIdPriceValueBetween(
                        gender, store, startPrice, endPrice, paging);
    }

    public Page<Product> filterGenderStore(long idUser, Pageable paging, String gender, String store,
                                           BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByFavoritesIdAndSexAndIdStoreNameAndIdPriceValueBetween(
                        idUser, gender, store, startPrice, endPrice, paging);
    }

    public Page<Product> filterCategory(Pageable paging, String category,
                                        BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByIdCategoryNameContainingAndIdPriceValueBetween(
                        category, startPrice, endPrice, paging);
    }

    public Page<Product> filterCategory(long idUser, Pageable paging, String category,
                                        BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByFavoritesIdAndIdCategoryNameContainingAndIdPriceValueBetween(
                        idUser, category, startPrice, endPrice, paging);
    }

    public Page<Product> filterGender(Pageable paging, String gender,
                                      BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findBySexAndIdPriceValueBetween(
                        gender, startPrice, endPrice, paging);
    }

    public Page<Product> filterGender(long idUser, Pageable paging, String gender,
                                      BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByFavoritesIdAndSexAndIdPriceValueBetween(
                        idUser, gender, startPrice, endPrice, paging);
    }

    public Page<Product> filterStore(Pageable paging, String store,
                                     BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByIdStoreNameAndIdPriceValueBetween(
                        store, startPrice, endPrice, paging);
    }

    public Page<Product> filterStore(long idUser, Pageable paging, String store,
                                     BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByFavoritesIdAndIdStoreNameAndIdPriceValueBetween(
                        idUser, store, startPrice, endPrice, paging);
    }

    public Page<Product> filterDefault(Pageable paging,
                                       BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByIdPriceValueBetween(
                        startPrice, endPrice, paging);
    }

    public Page<Product> filterFavourites(long idUser, Pageable paging,
                                       BigDecimal startPrice, BigDecimal endPrice) {
        return productRepository
                .findByFavoritesIdAndIdPriceValueBetween(
                        idUser, startPrice, endPrice, paging);
    }

    public ResponseEntity<Page<Product>> getFavouriteProducts(HttpServletRequest httpServletRequest,
                                                              Integer pageNo, Integer pageSize, String sortBy,
                                                              String category, String gender, String store,
                                                              String startPriceString, String endPriceString) {

        List<BigDecimal> priceList = assignPrice( startPriceString,  endPriceString);
        BigDecimal startPrice = priceList.get(0);
        BigDecimal endPrice = priceList.get(1);

        List<Boolean> booleanList = filterBooleans(category, gender, store);

        boolean filterByCategory = booleanList.get(0);
        boolean filterByGender =  booleanList.get(1);
        boolean filterByStore =  booleanList.get(2);

        boolean isCategoryGender =  booleanList.get(3);
        boolean isCategoryStore =  booleanList.get(4);
        boolean isGenderStore =  booleanList.get(5);

        boolean isCategoryGenderStore = booleanList.get(6);

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult;

        long idUser = (Long) httpServletRequest.getSession(true).getAttribute("id_user");

        if (isCategoryGenderStore) {
            pagedResult = filterCategoryGenderStore(idUser, paging, category, gender, store, startPrice, endPrice);
        } else if (isCategoryGender) {
            pagedResult = filterCategoryGender(idUser, paging, category, gender, startPrice, endPrice);
        } else if (isCategoryStore) {
            pagedResult = filterCategoryStore(idUser, paging, category, store, startPrice, endPrice);
        } else if (isGenderStore) {
            pagedResult = filterGenderStore(idUser, paging, gender, store, startPrice, endPrice);
        } else if (filterByCategory) {
            pagedResult = filterCategory(idUser, paging, category, startPrice, endPrice);
        } else if (filterByGender) {
            pagedResult = filterGender(idUser, paging, gender, startPrice, endPrice);
        } else if (filterByStore) {
            pagedResult = filterStore(idUser, paging, store, startPrice, endPrice);
        } else {
            pagedResult = filterFavourites(idUser, paging, startPrice, endPrice);
        }

        if(pagedResult.hasContent()) {
            return ResponseEntity.ok(pagedResult);
        } else {
            return ResponseEntity.badRequest().body(Page.empty());
        }
    }

    private List<BigDecimal> assignPrice(String startPriceString, String endPriceString) {

        BigDecimal startPrice;
        BigDecimal endPrice;

        if (StringUtils.isNumeric(startPriceString)) {
            startPrice = new BigDecimal(startPriceString);
        } else {
            startPrice = BigDecimal.valueOf(0);
        }

        if (StringUtils.isNumeric(endPriceString)) {
            endPrice = new BigDecimal(endPriceString);
        } else {
            endPrice = priceService.findMaxPrice();
        }
        List<BigDecimal> priceList = new ArrayList<>();

        priceList.add(startPrice);
        priceList.add(endPrice);

        return priceList;
    }

    private List<Boolean> filterBooleans(String category, String gender, String store) {

        boolean filterByCategory = !category.equals("");
        boolean filterByGender = !gender.equals("");
        boolean filterByStore = !store.equals("");

        boolean isCategoryGender = filterByCategory && filterByGender;
        boolean isCategoryStore = filterByCategory && filterByStore;
        boolean isGenderStore = filterByGender && filterByStore;

        boolean isCategoryGenderStore = filterByCategory && filterByGender && filterByStore;

        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(filterByCategory);
        booleanList.add(filterByGender);
        booleanList.add(filterByStore);
        booleanList.add(isCategoryGender);
        booleanList.add(isCategoryStore);
        booleanList.add(isGenderStore);
        booleanList.add(isCategoryGenderStore);

        return booleanList;
    }

    public boolean isFilter(String endPrice, String startPrice, String store,
                            String gender, String category) {
        return endPrice.equals("") && startPrice.equals("") && store.equals("") &&
                gender.equals("") && category.equals("");
    }
}
