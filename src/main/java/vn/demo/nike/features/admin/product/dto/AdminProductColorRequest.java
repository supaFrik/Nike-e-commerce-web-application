package vn.demo.nike.features.admin.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminProductColorRequest {
    @NotBlank(message = "Color name must not be blank")
    @Size(max = 100)
    private String colorName;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Invalid hex color")
    private String hexCode;

    @NotNull
    @Min(0)
    private Integer displayOrder;

    @NotNull
    @NotEmpty(message = "Each colors must have at least one image")
    @Valid
    private List<AdminProductImageRequest> images;

    @NotNull
    @NotEmpty(message = "Each color must have at least one variant")
    @Valid
    private List<AdminProductVariantRequest> variants;
}
