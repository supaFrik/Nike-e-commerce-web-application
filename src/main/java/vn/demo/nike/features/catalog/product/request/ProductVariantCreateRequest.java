package vn.demo.nike.features.catalog.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductVariantCreateRequest {
    @NotBlank(message = "Each variant must have sku")
    private final String sku;

    @NotBlank
    private final String size;

    @NotNull
    @Min(0)
    private final Integer stock;

    @NotNull
    private final Boolean active;
}
