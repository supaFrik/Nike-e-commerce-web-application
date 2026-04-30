package vn.demo.nike.features.catalog.product.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.demo.nike.features.catalog.product.service.ProductDetailPageService;

@Controller
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailPageService productDetailPageService;

    @GetMapping("/product-detail")
    public String getProductDetail(@RequestParam Long id, Model model) {
        model.addAttribute("product", productDetailPageService.getProductDetailPage(id));
        return "user/product-detail";
    }
}
