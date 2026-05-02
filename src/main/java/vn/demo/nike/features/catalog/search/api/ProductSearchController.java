package vn.demo.nike.features.catalog.search.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.demo.nike.features.catalog.category.service.CategoryService;
import vn.demo.nike.features.catalog.search.dto.ProductSearchCriteria;
import vn.demo.nike.features.catalog.search.service.ProductSearchService;

@Controller
@RequiredArgsConstructor
public class ProductSearchController {
    private final ProductSearchService productSearchService;
    private final CategoryService categoryService;

    @GetMapping("/search")
    public String search(ProductSearchCriteria criteria, Model model) {
        var result = productSearchService.search(criteria);
        model.addAttribute("query", criteria.getQuery());
        model.addAttribute("category", criteria.getCategory());
        model.addAttribute("sort",  criteria.getSort());
        model.addAttribute("products", result.getItems());
        model.addAttribute("searchResult", result);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "user/search";
    }
}
