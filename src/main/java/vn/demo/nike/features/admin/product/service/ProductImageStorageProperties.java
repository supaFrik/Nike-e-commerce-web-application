package vn.demo.nike.features.admin.product.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.storage.product-images")
public class ProductImageStorageProperties {
    private Path rootDirectory;
}
