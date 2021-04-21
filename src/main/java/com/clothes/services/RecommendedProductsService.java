package com.clothes.services;

import com.clothes.model.entitis.Product;
import com.clothes.repositories.CategoryRepository;
import com.clothes.repositories.ProductRepository;
import com.users.model.likes.CategoryLikes;
import com.users.model.likes.GetCategoryLikes;
import com.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RecommendedProductsService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Page<Product>> getRecommendedProducts(HttpServletRequest httpServletRequest,
                                                                Integer pageNo, Integer pageSize, String sortBy) {

        long userId = (Long) httpServletRequest.getSession(true).getAttribute("id_user");
        List<GetCategoryLikes> getCategoryLikesListInterface = userRepository.findCategoryLikes(userId);

        List<CategoryLikes> getProductLikesList = getCategoryLikesListInterface
                .stream()
                .map(gpt -> new CategoryLikes(gpt.getCategoryName(), gpt.getCnt()))
                .collect(Collectors.toList());

        List<Product> allRecommendedProducts = new ArrayList<>();

        getProductLikesList.forEach(temp -> {
            long idCategory = categoryRepository.getIdByName(temp.getCategoryName());
            allRecommendedProducts.addAll(productRepository.findCategoryLikes(idCategory, temp.getCnt()));
        });

        if(allRecommendedProducts.size()<60){
            int missingProductsAmount = 60 - allRecommendedProducts.size();
            List<Long> listOfProductIds = allRecommendedProducts
                    .stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
            List<Product> missingProducts = productRepository.findProductsNotInId(listOfProductIds, missingProductsAmount);
            allRecommendedProducts.addAll(missingProducts);
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = new PageImpl<>(allRecommendedProducts, paging, 60);

//        allRecommendedProducts.forEach(product -> System.out.println(product.getName()));
//        System.out.print(allRecommendedProducts.size());

        if(pagedResult.hasContent()) {
            return ResponseEntity.ok(pagedResult);
        } else {
            return ResponseEntity.badRequest().body(Page.empty());
        }
    }

}
