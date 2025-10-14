package vn.devpro.javaweb32.service.mapper;

import org.springframework.stereotype.Component;
import vn.devpro.javaweb32.dto.customer.product.*;
import vn.devpro.javaweb32.entity.product.*;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomerProductMapper {

    public ProductColorResponseDto toColorResponse(ProductColor color) {
        if (color == null) return null;
        ProductColorResponseDto dto = new ProductColorResponseDto();
        dto.setId(color.getId());
        dto.setColorName(color.getColorName());
        dto.setHexCode(color.getHexCode());
        dto.setActive(color.getActive());
        dto.setSwatchUrl(color.getSwatchPath());
        dto.setFolderPath(color.getFolderPath());
        dto.setBaseImage(color.getBaseImage());
        dto.setImageUrl(color.getImageUrl());
        if (color.getProduct() != null && color.getProduct().getImages() != null && !color.getProduct().getImages().isEmpty() && color.getProduct().getImages().get(0) != null) {
            dto.setPreviewImage(color.getProduct().getImages().get(0).getPath());
        }
        return dto;
    }

    private ProductVariantResponseDto toVariantResponse(ProductVariant variant) {
        if (variant == null) return null;
        ProductVariantResponseDto dto = new ProductVariantResponseDto();
        dto.setId(variant.getId());
        dto.setSize(variant.getSizeLabel());
        if (variant.getProduct() != null) {
            if (variant.getProduct().getSalePrice() != null) {
                dto.setPrice(variant.getProduct().getSalePrice());
            } else {
                dto.setPrice(variant.getProduct().getPrice());
            }
        }
        dto.setStock(variant.getStock());
        if (variant.getColor() != null) {
            dto.setColorId(variant.getColor().getId());
            dto.setColorName(variant.getColor().getColorName());
        }
        return dto;
    }

    private ProductImageResponseDto toImageResponse(ProductImage image) {
        if (image == null) return null; // null guard
        ProductImageResponseDto dto = new ProductImageResponseDto();
        dto.setId(image.getId());
        dto.setImageUrl(image.getPath());
        if (image.getColor() != null) {
            dto.setColorId(image.getColor().getId());
        }
        return dto;
    }

    public ProductResponseDto toProductResponse(Product product) {
        if (product == null) return null; // safety
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setDescription(product.getDescription());
        dto.setAvatarUrl(product.getAvatar());
        dto.setSeo(product.getSeo());
        if (product.getProductStatus() != null) {
            dto.setStatus(humanize(product.getProductStatus()));
        }
        dto.setType(product.getType());
        dto.setCreateDate(product.getCreateDate());
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }
        if (product.getColors() != null) {
            dto.setColors(product.getColors().stream()
                .filter(Objects::nonNull)
                .map(this::toColorResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        }
        if (product.getVariants() != null) {
            dto.setVariants(product.getVariants().stream()
                .filter(Objects::nonNull)
                .map(this::toVariantResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        }
        if (product.getImages() != null) {
            dto.setImages(product.getImages().stream()
                .filter(Objects::nonNull)
                .map(this::toImageResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        }
        return dto;
    }
    public String humanize(ProductStatus status) {
        if(status == null) return "";
        switch (status) {
            case ACTIVE: return "In Order";
            case DISCONTINUED: return "Unavailable";
            case FEW_LEFT: return "Left Order";
            case OUT_OF_STOCK: return "Out of Stock";
//            case DRAFT: return "Comming soon";
            default:
                String s = status.name().toLowerCase().replaceAll("_", " ");
                return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }
}
