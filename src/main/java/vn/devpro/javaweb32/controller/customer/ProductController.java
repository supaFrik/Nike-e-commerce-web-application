package vn.devpro.javaweb32.controller.customer;

import vn.devpro.javaweb32.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String productList(@RequestParam(required = false) String category, Model model) {
        List<Product> products;

        if(category != null && !category.isEmpty()) {
            products = productRepository.findByCategory(category);
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("totalProducts",  productRepository.count());

        return "customer/product-list";
    }
}
