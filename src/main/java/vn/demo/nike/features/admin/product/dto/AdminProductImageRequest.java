package vn.demo.nike.features.admin.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdminProductImageRequest {
    private Long existingImageId;

    @NotNull
    private Boolean isMainForColor;

    @NotNull
    @Min(0)
    private Integer orderIndex;

    @Size(max = 255)
    private String title;

    @Size(max = 512)
    private String altText;

    private String clientKey;
}
