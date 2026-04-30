package vn.demo.nike.features.catalog.product.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductColorCreateRequest {
    @NotBlank(message = "Color name must not be blank")
    @Size(max = 100)
    String colorName;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Invalid hex color")
    String hexCode;

    @NotNull
    @Min(0)
    Integer displayOrder;

    @NotNull
    @NotEmpty(message = "Each colors must have at least one image")
    @Valid
    List<ProductImageCreateRequest> images;

    @NotNull
    @NotEmpty(message = "Each color must have at least one variant")
    @Valid
    List<ProductVariantCreateRequest> variants;
}
