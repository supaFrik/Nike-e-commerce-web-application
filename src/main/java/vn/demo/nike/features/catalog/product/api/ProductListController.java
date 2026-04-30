package vn.demo.nike.features.catalog.product.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.demo.nike.features.catalog.category.dto.CategoryView;
import vn.demo.nike.features.catalog.category.service.CategoryService;
import vn.demo.nike.features.catalog.product.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.service.ProductListService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductListController {

    private final ProductListService productListService;
    private final CategoryService categoryService;

    @GetMapping("/products/list")
    public String list(Model model,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(defaultValue = "newest") String sort) {
        List<ProductListItemView> productList = productListService.getAllProductItems(categoryId, sort);
        String selectedCategoryName = categoryService.getCategoryNameById(categoryId);

        model.addAttribute("sort", sort);
        model.addAttribute("products", productList);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedCategoryName", selectedCategoryName);
        model.addAttribute("totalProducts", productList.size());

        return "user/product-list";
    }
}
