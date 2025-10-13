package vn.devpro.javaweb32.service.mapper;

import org.springframework.stereotype.Component;
import vn.devpro.javaweb32.dto.administrator.*;
import vn.devpro.javaweb32.entity.product.*;

import java.util.stream.Collectors;

@Component
public class AdminProductMapper {

    public Product toEntity(ProductCreateDto dto, Category category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setSeo(dto.getSeo());
        product.setStatus(dto.getStatus());
        product.setType(dto.getType());
        // Ava sẽ được set riêng sau khi Upload File
        product.setCategory(category);
        return product;
    }

    public void updateEntity(Product product, ProductCreateDto dto, Category category) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setSeo(dto.getSeo());
        product.setStatus(dto.getStatus());
        product.setType(dto.getType());
        product.setCategory(category);
    }

    public ProductColor toColorEntity(ProductColorInputDto dto, Product product) {
        ProductColor color = new ProductColor();
        color.setColorName(dto.getColorName());
        color.setHexCode(dto.getHexCode());
        color.setActive(dto.getActive());
        color.setProduct(product);
        return color;
    }
}
