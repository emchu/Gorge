package com.site;

import com.clothes.model.entitis.Category;
import com.clothes.model.entitis.Product;
import com.clothes.model.entitis.Store;
import com.clothes.repositories.ProductRepository;
import com.clothes.repositories.StoreRepository;
import com.clothes.services.CategoryService;
import com.clothes.services.ProductService;
import com.clothes.services.StoreService;
import com.security.payload.LoginRequest;
import com.users.model.registration.ChangePassword;
import com.users.model.registration.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class SiteController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/api/auth/product/{id}")
    public String showProduct(@PathVariable("id") long id, Model model,
                              HttpServletRequest httpServletRequest,
                              @ModelAttribute LoginRequest loginRequest) {
//        if (true) {
//            throw new BadRequestException("eloszka");
//        }
        model.addAttribute("httpServletRequest", httpServletRequest);
        model.addAttribute("loginRequest", loginRequest);

        Product product = productRepository.findById(id).orElse(new Product());
        model.addAttribute("product", product);
        return "fragments/product";
    }

    @GetMapping(value = "/api/auth/topBar")
    public String topBar(Model model) {
        return "navbar";
    }

    @GetMapping(value = "/api/auth/")
    public String mainPage(Model model,
                           @RequestParam(defaultValue = "0") Integer pageNo,
                           @RequestParam(defaultValue = "12") Integer pageSize,
                           @RequestParam(defaultValue = "id") String sortBy,
                           @RequestParam(defaultValue = "") String category,
                           @RequestParam(defaultValue = "") String gender,
                           @RequestParam(defaultValue = "") String store,
                           @RequestParam(defaultValue = "0") String startPrice,
                           @RequestParam(defaultValue = "") String endPrice,
                           @RequestParam(defaultValue = "") String name) {
//
        model.addAttribute("pageNoVal", pageNo);
        model.addAttribute("pageSizeVal", pageSize);
        model.addAttribute("sortByVal", sortBy);
        model.addAttribute("categoryVal", category);
        model.addAttribute("genderVal", gender);
        model.addAttribute("storeVal", store);
        model.addAttribute("startPriceVal", startPrice);
        model.addAttribute("endPriceVal", endPrice);
        model.addAttribute("name", name);

        ResponseEntity<Page<Product>> products = productService
                .getProducts(pageNo, pageSize, sortBy, category, gender, store, startPrice, endPrice, name);
        Page<Product> productPage = products.getBody();

        List<Category> categories = categoryService.getCategory();
        List<Store> stores = storeService.getStores();
        model.addAttribute("productPage", productPage);
        model.addAttribute("categories", categories);
        model.addAttribute("stores", stores);

        return "index";
    }

    @GetMapping(value = "/page")
    public String productsPage(Model model) {
//        Product product =
        return "productSimple";
    }

    @GetMapping(value = "/registration")
    public String registration(@ModelAttribute RegisterUser registerUser,
                               Model model) {
        model.addAttribute("registerUser", registerUser);
        return "register";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/password/change")
    public String changePassword(HttpServletRequest httpServletRequest,
                                 @ModelAttribute ChangePassword changePassword,
                                 Model model) {
        model.addAttribute("changePassword", changePassword);
        return "change_password";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/favourites")
    public String favourites(Model model,
                             HttpServletRequest httpServletRequest,
                             @RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "12") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy) {

        model.addAttribute("pageNoVal", pageNo);
        model.addAttribute("pageSizeVal", pageSize);
        model.addAttribute("sortByVal", sortBy);

        ResponseEntity<Page<Product>> products = productService
                .getFavouriteProducts(httpServletRequest, pageNo, pageSize, sortBy);
        Page<Product> productPage = products.getBody();

        model.addAttribute("productPage", productPage);

        return "favourites";
    }
}
