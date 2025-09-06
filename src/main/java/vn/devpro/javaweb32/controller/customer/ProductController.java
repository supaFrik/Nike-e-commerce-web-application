package vn.devpro.javaweb32.controller.customer;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.devpro.javaweb32.dto.product.ProductDetailDto;
import vn.devpro.javaweb32.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.devpro.javaweb32.repository.ProductRepository;
import vn.devpro.javaweb32.service.ProductService;
import vn.devpro.javaweb32.service.ProductSort;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSort productSort;

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String productList(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "featured") String sort,
             Model model) {

        List<Product> products;
        Sort sortOrder = productSort.sortedProducts(sort);
        if(category != null && !category.isEmpty()) {
            products = productRepository.findByCategory_NameIgnoreCase(category, sortOrder);
        } else {
            products = productRepository.findAll(sortOrder);
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("totalProducts",  productRepository.count());

        return "customer/product-list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        ProductDetailDto product = productService.getDetail(id);
        model.addAttribute("product", product);
        return "customer/product-detail";
    }

    @GetMapping("/product-detail")
    public String productDetailById(@RequestParam("id") Long id, Model model) {
        ProductDetailDto product = productService.getDetail(id);
        model.addAttribute("product", product);
        return "customer/product-detail";
    }
}
