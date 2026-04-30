package vn.demo.nike.shared.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.demo.nike.features.admin.product.service.ProductImageStorageProperties;

import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
public class StaticResourceConfig implements WebMvcConfigurer {

    private final ProductImageStorageProperties productImageStorageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path rootDirectory = productImageStorageProperties.getRootDirectory();

        if (rootDirectory == null) {
            return;
        }

        String location = rootDirectory
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations(location);
    }
}
