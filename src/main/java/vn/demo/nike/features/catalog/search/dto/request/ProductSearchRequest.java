package vn.demo.nike.features.catalog.search.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.IMessage;

@Getter
@Setter
public class ProductSearchRequest {
    @Size(max = 100, message = "Query tối đa 100 kí tự")
    private String query;

    @Size(max = 50, message = "Category tối đa 50 kí tự")
    private String category;

    private String sort;

    @Min(value = 1, message = "Page phải lớn hơn hoặc bằng 1")
    private Integer page;

    @Min(value = 1, message = "PageSize phải lớn hơn hoặc bằng 1")
    @Max(value = 100, message = "PageSize phải nhỏ hơn hoặc bằng 100")
    private Integer pageSize;
}