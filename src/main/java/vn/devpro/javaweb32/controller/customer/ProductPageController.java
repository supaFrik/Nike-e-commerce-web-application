package vn.devpro.javaweb32.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CustomerProductService productService;

    @Autowired
    private CategoryAdminService categoryService;

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
        criteria.setPage(page != null ? page : 1);
        criteria.setPageSize(pageSize != null ? pageSize : 24);

        List<ProductResponseDto> products;
        if (categoryName != null && !categoryName.isBlank()) {
            Category cat = categoryService.findByName(categoryName.trim().toLowerCase());
            if (cat != null) {
                products = productService.getProductsByCategory(cat.getId());
                model.addAttribute("selectedCategory", categoryName);
            } else {
                products = Collections.emptyList();
                model.addAttribute("selectedCategory", categoryName);
            }
        } else {
            products = productService.getAllProducts();
        }

        // Additional filtering: sale, size, price range
        products = products.stream()
                .filter(p -> criteria.getSaleOnly() == null || !criteria.getSaleOnly() || p.hasSale())
                .filter(p -> criteria.getSize() == null || criteria.getSize().isBlank() || (p.getVariants() != null && p.getVariants().stream().anyMatch(v -> criteria.getSize().equalsIgnoreCase(v.getSize()))))
                .filter(p -> criteria.getMinPrice() == null || (p.getEffectivePrice() != null && p.getEffectivePrice().compareTo(criteria.getMinPrice()) >= 0))
                .filter(p -> criteria.getMaxPrice() == null || (p.getEffectivePrice() != null && p.getEffectivePrice().compareTo(criteria.getMaxPrice()) <= 0))
                .collect(Collectors.toList());

        // Sorting
        if (criteria.getSort() != null && !products.isEmpty()) {
            switch (criteria.getSort()) {
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

        int totalProducts = products.size();
        int sizePerPage = Math.max(1, Math.min(criteria.getPageSize(), 200));
        int totalPages = (int) Math.ceil(totalProducts / (double) sizePerPage);
        int currentPage = Math.min(Math.max(1, criteria.getPage()), Math.max(1, totalPages == 0 ? 1 : totalPages));
        int fromIndex = (currentPage - 1) * sizePerPage;
        int toIndex = Math.min(fromIndex + sizePerPage, totalProducts);
        List<ProductResponseDto> pageItems = totalProducts == 0 ? Collections.emptyList() : products.subList(fromIndex, toIndex);

        List<Category> categories = categoryService.findAllActive();

        model.addAttribute("products", pageItems);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", sizePerPage);
        model.addAttribute("categories", categories);
        model.addAttribute("criteria", criteria);

        return "customer/product-list";
    }

    @GetMapping("/product-list")
    public String legacyProductListRedirect() {
        return "redirect:/products";
    }
}
