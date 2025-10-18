package vn.devpro.javaweb32.controller.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.dto.customer.product.ProductResponseDto;
import vn.devpro.javaweb32.dto.customer.product.ProductVariantResponseDto;
import vn.devpro.javaweb32.service.customer.CustomerProductService;
import vn.devpro.javaweb32.repository.ProductRepository;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductDetailPageController extends BaseController {

    private final CustomerProductService productService;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductDetailPageController(CustomerProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/product-detail")
    public String productDetail(@RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "seo", required = false) String seo,
                                Model model) {
        ProductResponseDto dto;
        try {
            dto = resolveProductDto(id, seo);
        } catch (EntityNotFoundException ex) {
            return "redirect:/products?error=notfound";
        }

        if (dto.getColors() != null) {
            dto.setColors(dto.getColors().stream()
                    .sorted(Comparator.comparing(c -> c.getColorName() == null ? "" : c.getColorName().toLowerCase()))
                    .collect(Collectors.toList()));
        }

        model.addAttribute("product", dto);
        model.addAttribute("colors", dto.getColors());
        model.addAttribute("variantsJson", toVariantsJson(dto.getVariants()));
        return "customer/product-detail";
    }

    private ProductResponseDto resolveProductDto(Long id, String seo) {
        if (id != null) {
            return productService.getProductById(id);
        }
        if (seo != null && !seo.isBlank()) {
            Product product = productRepository.findBySeo(seo)
                    .filter(p -> p.getProductStatus() == ProductStatus.ACTIVE)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            return productService.getProductById(product.getId());
        }
        throw new EntityNotFoundException("Product identifier missing");
    }

    private String toVariantsJson(List<ProductVariantResponseDto> variants) {
        if (variants == null || variants.isEmpty()) return "[]";
        var minimal = variants.stream().map(v -> new VariantJson(
                v.getId(), v.getSize(), v.getPrice(), v.getStock(), v.getColorName()
        )).collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(minimal);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    static class VariantJson {
        public Long id; public String size; public java.math.BigDecimal price; public Integer stock; public String colorName;
        public VariantJson(Long id, String size, java.math.BigDecimal price, Integer stock, String colorName) {
            this.id = id; this.size = size; this.price = price; this.stock = stock; this.colorName = colorName;
        }
    }
}
