package vn.demo.nike.features.catalog.product.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.demo.nike.features.catalog.product.domain.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "Product name must not be blank")
    @Size(max = 255)
    String name;

    @NotNull
    @DecimalMin(value = "0.0", message = "Price must be >= 0")
    @Digits(integer = 17, fraction = 2)
    BigDecimal price;

    @DecimalMin(value = "0.0")
    @Digits(integer = 17, fraction = 2)
    BigDecimal salePrice;

    @NotBlank
    String description;

    @NotBlank
    String type;

    @NotNull(message = "Category is required")
    Long categoryId;

    @NotNull(message = "Product status is required")
    ProductStatus productStatus;

    @NotNull
    @NotEmpty(message = "Product must have at least one color")
    @Valid
    List<ProductColorCreateRequest> colors;
}