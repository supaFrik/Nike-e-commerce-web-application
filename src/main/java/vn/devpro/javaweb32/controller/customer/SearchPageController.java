package vn.devpro.javaweb32.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.dto.customer.product.ProductSearchCriteria;
import vn.devpro.javaweb32.service.customer.ProductSearchService;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;

@Controller
public class SearchPageController extends BaseController {

    private final ProductSearchService productSearchService;
    private final CategoryAdminService categoryAdminService;

    public SearchPageController(ProductSearchService productSearchService, CategoryAdminService categoryAdminService) {
        this.productSearchService = productSearchService;
        this.categoryAdminService = categoryAdminService;
    }

    private static final int DEFAULT_PAGE_SIZE = 24;
    private static final int MAX_PAGE_SIZE = 200;

    @GetMapping("/search")
    public String searchPage(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sale", required = false) Boolean sale,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "minPrice", required = false) java.math.BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) java.math.BigDecimal maxPrice,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "24") Integer pageSize,
            Model model) {

        ProductSearchCriteria c = new ProductSearchCriteria();
        c.setQ(q);
        c.setCategory(category);
        c.setSaleOnly(sale);
        c.setSize(size);
        c.setMinPrice(minPrice);
        c.setMaxPrice(maxPrice);
        c.setSort(sort);
        c.setPage(normalizePage(page));
        c.setPageSize(normalizePageSize(pageSize, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE));

        var result = productSearchService.search(c);

        model.addAttribute("query", q);
        model.addAttribute("category", category);
        model.addAttribute("sale", sale);
        model.addAttribute("size", size);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);
        model.addAttribute("searchResult", result);
        model.addAttribute("products", result.getItems());
        model.addAttribute("categories", categoryAdminService.findAllActive());
        return "customer/search";
    }
}
