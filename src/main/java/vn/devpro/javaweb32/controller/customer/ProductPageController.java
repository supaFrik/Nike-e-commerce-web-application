package vn.devpro.javaweb32.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.dto.customer.product.ProductResponseDto;
import vn.devpro.javaweb32.dto.customer.product.ProductSearchCriteria;
import vn.devpro.javaweb32.entity.product.Category;
import vn.devpro.javaweb32.service.customer.CustomerProductService;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProductPageController extends BaseController {

    private final CustomerProductService productService;
    private final CategoryAdminService categoryService;

    public ProductPageController(CustomerProductService productService, CategoryAdminService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    private static final int DEFAULT_PAGE_SIZE = 24;
    private static final int MAX_PAGE_SIZE = 200;

    @GetMapping("/products")
    public String productsPage(@RequestParam(value = "category", required = false) String categoryName,
                               @RequestParam(value = "sale", required = false) Boolean saleOnly,
                               @RequestParam(value = "size", required = false) String size,
                               @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                               @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "24") Integer pageSize,
                               Model model) {

        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setCategory(categoryName);
        criteria.setSaleOnly(saleOnly);
        criteria.setSize(size);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setSort(sort);
        criteria.setPage(normalizePage(page));
        criteria.setPageSize(normalizePageSize(pageSize, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE));

        List<ProductResponseDto> products = fetchBaseProducts(categoryName, model);

        products = applyFilters(products, criteria);
        applySorting(products, criteria.getSort());

        int pageSizeNormalized = criteria.getPageSize();
        List<ProductResponseDto> pageItems = paginate(products, criteria.getPage(), pageSizeNormalized, model);

        model.addAttribute("products", pageItems);
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("criteria", criteria);

        return "customer/product-list";
    }

    private List<ProductResponseDto> fetchBaseProducts(String categoryName, Model model) {
        if (categoryName != null && !categoryName.isBlank()) {
            Category cat = categoryService.findByName(categoryName.trim().toLowerCase());
            if (cat != null) {
                model.addAttribute("selectedCategory", categoryName);
                return productService.getProductsByCategory(cat.getId());
            }
            model.addAttribute("selectedCategory", categoryName);
            return Collections.emptyList();
        }
        return productService.getAllProducts();
    }

    private List<ProductResponseDto> applyFilters(List<ProductResponseDto> products, ProductSearchCriteria criteria) {
        if (products == null || products.isEmpty()) return Collections.emptyList();
        return products.stream()
                .filter(p -> criteria.getSaleOnly() == null || !criteria.getSaleOnly() || p.hasSale())
                .filter(p -> criteria.getSize() == null || criteria.getSize().isBlank() || (p.getVariants() != null && p.getVariants().stream().anyMatch(v -> criteria.getSize().equalsIgnoreCase(v.getSize()))))
                .filter(p -> criteria.getMinPrice() == null || (p.getEffectivePrice() != null && p.getEffectivePrice().compareTo(criteria.getMinPrice()) >= 0))
                .filter(p -> criteria.getMaxPrice() == null || (p.getEffectivePrice() != null && p.getEffectivePrice().compareTo(criteria.getMaxPrice()) <= 0))
                .collect(Collectors.toList());
    }

    private void applySorting(List<ProductResponseDto> products, String sort) {
        if (sort == null || products == null || products.isEmpty()) return;
        switch (sort) {
            case "price_asc":
                products.sort(Comparator.comparing(ProductResponseDto::getEffectivePrice, Comparator.nullsLast(BigDecimal::compareTo)));
                break;
            case "price_desc":
                products.sort(Comparator.comparing(ProductResponseDto::getEffectivePrice, Comparator.nullsLast(BigDecimal::compareTo)).reversed());
                break;
            case "newest":
            default:
                products.sort(Comparator.comparing(ProductResponseDto::getCreateDate, Comparator.nullsLast(Date::compareTo)).reversed());
                break;
        }
    }

    @GetMapping("/product-list")
    public String legacyProductListRedirect() {
        return "redirect:/products";
    }
}
