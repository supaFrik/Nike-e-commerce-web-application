package vn.demo.nike.features.admin.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminProductVariantRequest {
    @NotBlank(message = "Each variant must have sku")
    private String sku;

    @NotBlank
    private String size;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    private Boolean active;
}
