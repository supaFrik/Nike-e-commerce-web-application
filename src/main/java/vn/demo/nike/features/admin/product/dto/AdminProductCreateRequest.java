package vn.demo.nike.features.admin.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import vn.demo.nike.features.catalog.product.domain.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class AdminProductCreateRequest {
    @NotBlank(message = "Product name must not be blank")
    @Size(max = 255)
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", message = "Price must be >= 0")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.0")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal salePrice;

    @NotBlank
    private String description;

    @NotBlank
    private String type;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Product status is required")
    private ProductStatus productStatus;

    @NotNull
    @NotEmpty(message = "Product must have at least one color")
    @Valid
    private List<AdminProductColorRequest> colors;
}
