package vn.devpro.javaweb32.controller.customer;

import org.springframework.data.domain.Sort;
import vn.devpro.javaweb32.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.devpro.javaweb32.repository.ProductRepository;
import vn.devpro.javaweb32.service.ProductSort;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSort productSort;

    @GetMapping("/products")
    public String productList(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "featured") String sort,
             Model model) {

        List<Product> products;
        Sort sortOrder = productSort.sortedProducts(sort);
        if(category != null && !category.isEmpty()) {
            products = productRepository.findByCategory(category, sortOrder);
        } else {
            products = productRepository.findAll(sortOrder);
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("totalProducts",  productRepository.count());

        return "customer/product-list";
    }
}
