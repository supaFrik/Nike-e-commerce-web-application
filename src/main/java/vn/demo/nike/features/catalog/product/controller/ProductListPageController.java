package vn.demo.nike.features.catalog.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.demo.nike.features.catalog.category.service.CategoryService;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.service.ProductListService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProductListPageController {

    private final ProductListService productListService;
    private final CategoryService categoryService;

    @GetMapping("/products/list")
    public String list(Model model,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(defaultValue = "newest") String sort,
                       @RequestParam(defaultValue = "0") int page) {
        Page<ProductListItemView> productPage = productListService.getProductList(categoryId, sort, page);
        String selectedCategoryName = categoryService.getCategoryNameById(categoryId);

        model.addAttribute("sort", sort);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("hasNext", productPage.hasNext());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedCategoryName", selectedCategoryName);
        model.addAttribute("totalProducts", productPage.getTotalElements());

        return "user/product-list";
    }

    @GetMapping("/products/list/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listData(
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(defaultValue = "newest") String sort,
                       @RequestParam(defaultValue = "0") int page) {
        Page<ProductListItemView> productPage = productListService.getProductList(categoryId, sort, page);
        Map<String, Object> res = new HashMap<>();
        res.put("content", productPage.getContent());
        res.put("page", productPage.getNumber());
        res.put("size", productPage.getSize());
        res.put("totalPages", productPage.getTotalPages());
        res.put("totalElements", productPage.getTotalElements());
        res.put("hasNext", productPage.hasNext());
        res.put("hasPrevious", productPage.hasPrevious());
        return ResponseEntity.ok(res);
    }
}
