package vn.demo.nike.features.admin.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.demo.nike.features.catalog.product.domain.enums.ProductStatus;

@Getter
@AllArgsConstructor
public class AdminCreatedProductResponse {
    private Long productId;
    @NotBlank(message = "Product name must not be blank")
    @Size(max = 255)
    private String name;
    @NotNull(message = "Category is required")
    private String categoryName;
    @NotNull(message = "Product status is required")
    private ProductStatus productStatus;
}
