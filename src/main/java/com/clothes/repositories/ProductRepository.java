package com.clothes.repositories;

import com.clothes.model.entitis.Category;
import com.clothes.model.entitis.Product;
import com.clothes.model.entitis.Store;
import com.users.model.likes.GetCategoryLikes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(long id);
    Optional<Product> findByName(String name);
    Optional<List<Product>> findBySex(String name);
    Optional<List<Product>> findByIdCategory(Category category);
    Optional<List<Product>> findByIdStore(Store store);

    Page<Product> findByNameContaining(String name, Pageable pageable);

    Page<Product> findByIdCategoryNameContaining(String categoryName, Pageable pageable);
    Page<Product> findBySex(String sex, Pageable pageable);
    Page<Product> findByIdStoreName(String storeName, Pageable pageable);

    Page<Product> findByIdCategoryNameContainingAndSexAndIdStoreName(
            String categoryName, String sex, String storeName, Pageable pageable);

    Page<Product> findByIdCategoryNameContainingAndSex(
            String categoryName, String sex, Pageable pageable);
    Page<Product> findByIdCategoryNameContainingAndIdStoreName(
            String categoryName, String storeName, Pageable pageable);
    Page<Product> findBySexAndIdStoreName(String sex, String storeName, Pageable pageable);

    Page<Product> findByIdPriceValueLessThan(BigDecimal price, Pageable pageable);
    Page<Product> findByIdPriceValueLessThanEqual(BigDecimal price, Pageable pageable);

    Page<Product> findByIdPriceValueGreaterThan(BigDecimal price, Pageable pageable);
    Page<Product> findByIdPriceValueGreaterThanEqual(BigDecimal price, Pageable pageable);

    //main page
    Page<Product> findByIdPriceValueBetween(BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);

    Page<Product> findByIdCategoryNameContainingAndSexAndIdStoreNameAndIdPriceValueBetween(
            String categoryName, String sex, String storeName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);

    Page<Product> findByIdCategoryNameContainingAndIdPriceValueBetween(
            String categoryName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);
    Page<Product> findBySexAndIdPriceValueBetween(
            String sex, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);
    Page<Product> findByIdStoreNameAndIdPriceValueBetween(
            String storeName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);

    Page<Product> findByIdCategoryNameContainingAndSexAndIdPriceValueBetween(
            String categoryName, String sex, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);
    Page<Product> findByIdCategoryNameContainingAndIdStoreNameAndIdPriceValueBetween(
            String categoryName, String storeName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);
    Page<Product> findBySexAndIdStoreNameAndIdPriceValueBetween(
            String sex, String storeName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);

    //favourites
    Page<Product> findByFavoritesId(long idUser, Pageable pageable);
    Page<Product> findByFavoritesIdAndIdPriceValueBetween(long idUser, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);

    Page<Product> findByFavoritesIdAndIdCategoryNameContainingAndSexAndIdStoreNameAndIdPriceValueBetween(
            long idUser, String categoryName, String sex, String storeName, BigDecimal startPrice, BigDecimal endPrice,
            Pageable pageable);

    Page<Product> findByFavoritesIdAndIdCategoryNameContainingAndIdPriceValueBetween(
            long idUser, String categoryName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);
    Page<Product> findByFavoritesIdAndSexAndIdPriceValueBetween(
            long idUser, String sex, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);
    Page<Product> findByFavoritesIdAndIdStoreNameAndIdPriceValueBetween(
            long idUser, String storeName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);

    Page<Product> findByFavoritesIdAndIdCategoryNameContainingAndSexAndIdPriceValueBetween(
            long idUser, String categoryName, String sex, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);
    Page<Product> findByFavoritesIdAndIdCategoryNameContainingAndIdStoreNameAndIdPriceValueBetween(
            long idUser, String categoryName, String storeName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);
    Page<Product> findByFavoritesIdAndSexAndIdStoreNameAndIdPriceValueBetween(
            long idUser, String sex, String storeName, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable);

    //recommended
    @Transactional
    @Query(value="SELECT * FROM product " +
            "WHERE product.id_category = :id_category " +
            "ORDER BY RANDOM() " +
            "LIMIT :amount",
            nativeQuery = true)
    List<Product> findCategoryLikes(@Param("id_category") long idCategory, @Param("amount") int amount);

    @Transactional
    @Query(value="select * " +
            "from product " +
            "where product.id_product not in (:listOfProductIds) " +
            "order by RANDOM() " +
            "limit :amount",
            nativeQuery = true)
    List<Product> findProductsNotInId(@Param("listOfProductIds") List<Long> listOfProductIds, @Param("amount") int amount);

}
