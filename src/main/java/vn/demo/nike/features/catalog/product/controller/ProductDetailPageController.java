package vn.demo.nike.features.catalog.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.demo.nike.features.catalog.product.service.ProductDetailService;

/**
 * Return HTTP request for product-detail page
 */

@Controller
@RequiredArgsConstructor
public class ProductDetailPageController {

    private final ProductDetailService productDetailService;

    @GetMapping("/product-detail")
    public String getProductDetail(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            return "redirect:/products/list";
        }
        model.addAttribute("product", productDetailService.getProductDetail(id));
        return "user/product-detail";
    }

    @GetMapping("/products/{id}")
    public String getProductDetailByPath(@PathVariable Long id, Model model) {
        model.addAttribute("product", productDetailService.getProductDetail(id));
        return "user/product-detail";
    }
}
