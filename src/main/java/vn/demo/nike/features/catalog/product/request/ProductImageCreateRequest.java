package vn.demo.nike.features.catalog.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductImageCreateRequest {
    @NotBlank(message = "Image URL must not be blank")
    @Size(max = 1024)
    String imgUrl;

    @Size(max = 255)
    String title;

    @Size(max = 512)
    String altText;

    @NotNull
    Boolean isMainForColor;

    @NotNull
    @Min(0)
    Integer orderIndex;
}
