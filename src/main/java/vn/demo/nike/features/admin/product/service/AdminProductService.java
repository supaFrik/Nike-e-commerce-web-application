package vn.demo.nike.features.admin.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.dto.AdminCreatedProductResponse;
import vn.demo.nike.features.admin.product.dto.AdminProductCategoryOptionResponse;
import vn.demo.nike.features.admin.product.dto.AdminProductColorRequest;
import vn.demo.nike.features.admin.product.dto.AdminProductCreateRequest;
import vn.demo.nike.features.admin.product.dto.AdminProductFormResponse;
import vn.demo.nike.features.admin.product.dto.AdminProductImageRequest;
import vn.demo.nike.features.admin.product.dto.AdminProductVariantRequest;
import vn.demo.nike.features.admin.product.exception.InvalidFileMappingException;
import vn.demo.nike.features.admin.product.exception.InvalidProductColorException;
import vn.demo.nike.features.admin.product.exception.InvalidSalePriceException;
import vn.demo.nike.features.admin.product.exception.InvalidUploadedImageException;
import vn.demo.nike.features.admin.product.exception.ProductColorNotFoundException;
import vn.demo.nike.features.admin.product.exception.ProductNotFoundException;
import vn.demo.nike.features.catalog.category.domain.Category;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.domain.ProductVariant;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.shared.util.ProductImageUrlResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageStorageService productImageStorageService;

    @Transactional
    public AdminCreatedProductResponse createProduct(AdminProductCreateRequest request,
                                                     List<MultipartFile> files,
                                                     List<String> fileClientKeys) {
        Map<String, MultipartFile> uploadedFiles = buildFileMap(files, fileClientKeys);
        validateCreateOrUpdateRequest(request, uploadedFiles, Map.of());

        Category category = findCategory(request.getCategoryId());
        Product product = new Product();
        applyProductFields(product, request, category);
        rebuildColorBlocks(product, request, uploadedFiles, Map.of());

        Product savedProduct = productRepository.save(product);
        return mapResponse(savedProduct);
    }

    @Transactional
    public AdminCreatedProductResponse updateProduct(Long productId,
                                                     AdminProductCreateRequest request,
                                                     List<MultipartFile> files,
                                                     List<String> fileClientKeys) {
        Product product = findProduct(productId);
        Map<String, MultipartFile> uploadedFiles = buildFileMap(files, fileClientKeys);
        Map<Long, ProductImage> existingImages = indexExistingImages(product);

        validateCreateOrUpdateRequest(request, uploadedFiles, existingImages);

        Category category = findCategory(request.getCategoryId());
        List<String> pathsToDelete = collectPathsToDelete(product, request, existingImages);

        applyProductFields(product, request, category);
        product.getColors().clear();
        rebuildColorBlocks(product, request, uploadedFiles, existingImages);

        Product savedProduct = productRepository.save(product);
        scheduleDeleteAfterCommit(pathsToDelete);
        return mapResponse(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = findProduct(productId);
        List<String> pathsToDelete = collectAllImagePaths(product);
        productRepository.delete(product);
        scheduleDeleteAfterCommit(pathsToDelete);
    }

    @Transactional
    public AdminProductFormResponse getProductForm(Long productId) {
        Product product = findProduct(productId);

        List<AdminProductFormResponse.AdminColorFormItem> colors = product.getColors().stream()
                .sorted(Comparator.comparing(color -> defaultOrder(color.getDisplayOrder())))
                .map(color -> new AdminProductFormResponse.AdminColorFormItem(
                        color.getColorName(),
                        color.getHexCode(),
                        color.getDisplayOrder(),
                        color.getImages().stream()
                                .sorted(Comparator.comparing(image -> defaultOrder(image.getOrderIndex())))
                                .map(image -> new AdminProductFormResponse.AdminImageFormItem(
                                        image.getId(),
                                        ProductImageUrlResolver.toPublicUrl(image.getPath()),
                                        image.getTitle(),
                                        image.getAltText(),
                                        image.getOrderIndex(),
                                        image.getIsMainForColor()
                                ))
                                .toList(),
                        color.getVariants().stream()
                                .sorted(Comparator.comparing(ProductVariant::getSize))
                                .map(variant -> new AdminProductFormResponse.AdminVariantFormItem(
                                        variant.getSku(),
                                        variant.getSize(),
                                        variant.getStock(),
                                        variant.getActive()
                                ))
                                .toList()
                ))
                .toList();

        return new AdminProductFormResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getType(),
                product.getCategory().getId(),
                product.getProductStatus(),
                product.getPrice(),
                product.getSalePrice(),
                colors
        );
    }

    @Transactional
    public List<AdminProductCategoryOptionResponse> getCategoryOptions() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(category -> new AdminProductCategoryOptionResponse(category.getId(), category.getName()))
                .toList();
    }

    private void applyProductFields(Product product, AdminProductCreateRequest request, Category category) {
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription().trim());
        product.setType(request.getType().trim());
        product.setPrice(request.getPrice());
        product.setSalePrice(request.getSalePrice());
        product.setProductStatus(request.getProductStatus());
        product.setCategory(category);
    }

    private void rebuildColorBlocks(Product product,
                                    AdminProductCreateRequest request,
                                    Map<String, MultipartFile> uploadedFiles,
                                    Map<Long, ProductImage> existingImages) {
        for (AdminProductColorRequest colorRequest : request.getColors()) {
            ProductColor color = new ProductColor();
            color.setColorName(colorRequest.getColorName().trim());
            color.setHexCode(colorRequest.getHexCode());
            color.setDisplayOrder(colorRequest.getDisplayOrder());
            color.setProduct(product);

            for (AdminProductVariantRequest variantRequest : colorRequest.getVariants()) {
                ProductVariant variant = new ProductVariant();
                variant.setSku(variantRequest.getSku().trim());
                variant.setSize(variantRequest.getSize().trim());
                variant.setStock(variantRequest.getStock());
                variant.setActive(variantRequest.getActive());
                variant.setColor(color);
                color.getVariants().add(variant);
            }

            for (AdminProductImageRequest imageRequest : colorRequest.getImages()) {
                ProductImage image = new ProductImage();
                image.setPath(resolveImagePath(imageRequest, uploadedFiles, existingImages, request.getName(), colorRequest.getColorName()));
                image.setTitle(imageRequest.getTitle());
                image.setAltText(imageRequest.getAltText());
                image.setOrderIndex(imageRequest.getOrderIndex());
                image.setIsMainForColor(imageRequest.getIsMainForColor());
                image.setColor(color);
                color.getImages().add(image);
            }

            product.getColors().add(color);
        }
    }

    private String resolveImagePath(AdminProductImageRequest imageRequest,
                                    Map<String, MultipartFile> uploadedFiles,
                                    Map<Long, ProductImage> existingImages,
                                    String productName,
                                    String colorName) {
        if (imageRequest.getExistingImageId() != null) {
            ProductImage existingImage = existingImages.get(imageRequest.getExistingImageId());
            if (existingImage == null) {
                throw new InvalidProductColorException("Existing image does not belong to this product: " + imageRequest.getExistingImageId());
            }
            return existingImage.getPath();
        }

        String clientKey = normalize(imageRequest.getClientKey());
        MultipartFile file = uploadedFiles.get(clientKey);
        if (file == null) {
            throw new InvalidProductColorException("Image key '" + imageRequest.getClientKey() + "' not found in uploaded files.");
        }
        return productImageStorageService.store(file, productName, colorName);
    }

    private void validateCreateOrUpdateRequest(AdminProductCreateRequest request,
                                               Map<String, MultipartFile> uploadedFiles,
                                               Map<Long, ProductImage> existingImages) {
        validateSalePrice(request);
        validateDuplicateColor(request.getColors());
        validateColorBlocks(request.getColors(), uploadedFiles, existingImages);
    }

    private void validateSalePrice(AdminProductCreateRequest request) {
        if (request.getSalePrice() != null && request.getSalePrice().compareTo(request.getPrice()) > 0) {
            throw new InvalidSalePriceException();
        }
    }

    private void validateDuplicateColor(List<AdminProductColorRequest> colors) {
        if (colors == null || colors.isEmpty()) {
            throw new ProductColorNotFoundException();
        }

        Set<String> existingColorNames = new HashSet<>();
        for (AdminProductColorRequest color : colors) {
            String normalizedName = normalize(color.getColorName());
            if (!existingColorNames.add(normalizedName)) {
                throw new InvalidProductColorException("Duplicate color name: " + normalizedName);
            }
        }
    }

    private void validateColorBlocks(List<AdminProductColorRequest> colors,
                                     Map<String, MultipartFile> uploadedFiles,
                                     Map<Long, ProductImage> existingImages) {
        if (colors == null || colors.isEmpty()) {
            throw new ProductColorNotFoundException();
        }

        Set<String> referencedClientKeys = new HashSet<>();
        Set<Long> referencedExistingImageIds = new HashSet<>();

        for (AdminProductColorRequest color : colors) {
            if (color.getImages() == null || color.getImages().isEmpty()) {
                throw new InvalidProductColorException("Each color must have at least one image.");
            }
            if (color.getVariants() == null || color.getVariants().isEmpty()) {
                throw new InvalidProductColorException("Each color must have at least one variant.");
            }

            long mainImageCount = color.getImages().stream()
                    .filter(image -> Boolean.TRUE.equals(image.getIsMainForColor()))
                    .count();
            if (mainImageCount != 1) {
                throw new InvalidProductColorException("Color " + color.getColorName() + " must have exactly one main image.");
            }

            Set<String> existingSizes = new HashSet<>();
            for (AdminProductVariantRequest variant : color.getVariants()) {
                String size = normalize(variant.getSize());
                if (!existingSizes.add(size)) {
                    throw new InvalidProductColorException("Duplicate size '" + variant.getSize() + "' in color " + color.getColorName());
                }
            }

            for (AdminProductImageRequest image : color.getImages()) {
                validateImageReference(image, uploadedFiles, existingImages, referencedClientKeys, referencedExistingImageIds);
            }
        }

        for (String uploadedKey : uploadedFiles.keySet()) {
            if (!referencedClientKeys.contains(uploadedKey)) {
                throw new InvalidProductColorException("Uploaded file '" + uploadedKey + "' is not referenced by any colorway.");
            }
        }
    }

    private void validateImageReference(AdminProductImageRequest image,
                                        Map<String, MultipartFile> uploadedFiles,
                                        Map<Long, ProductImage> existingImages,
                                        Set<String> referencedClientKeys,
                                        Set<Long> referencedExistingImageIds) {
        Long existingImageId = image.getExistingImageId();
        String clientKey = normalize(image.getClientKey());

        boolean hasExistingImage = existingImageId != null;
        boolean hasClientKey = !clientKey.isBlank();

        if (hasExistingImage == hasClientKey) {
            throw new InvalidProductColorException("Each image must reference exactly one source: existingImageId or clientKey.");
        }

        if (hasExistingImage) {
            if (!existingImages.containsKey(existingImageId)) {
                throw new InvalidProductColorException("Existing image does not belong to this product: " + existingImageId);
            }
            if (!referencedExistingImageIds.add(existingImageId)) {
                throw new InvalidProductColorException("Duplicate existing image reference: " + existingImageId);
            }
            return;
        }

        if (!uploadedFiles.containsKey(clientKey)) {
            throw new InvalidProductColorException("Image key '" + image.getClientKey() + "' not found in uploaded files.");
        }
        if (!referencedClientKeys.add(clientKey)) {
            throw new InvalidProductColorException("Duplicate uploaded image reference: " + image.getClientKey());
        }
    }

    private Map<String, MultipartFile> buildFileMap(List<MultipartFile> files, List<String> fileClientKeys) {
        List<MultipartFile> safeFiles = files == null ? List.of() : files;
        List<String> safeKeys = fileClientKeys == null ? List.of() : fileClientKeys;

        if (safeFiles.size() != safeKeys.size()) {
            throw new InvalidUploadedImageException("files and fileClientKeys must have same size");
        }

        Map<String, MultipartFile> uploadedFiles = new LinkedHashMap<>();
        for (int i = 0; i < safeFiles.size(); i++) {
            String clientKey = normalize(safeKeys.get(i));
            if (clientKey.isBlank()) {
                throw new InvalidFileMappingException("File client key at index " + i + " must not be empty");
            }

            MultipartFile previous = uploadedFiles.put(clientKey, safeFiles.get(i));
            if (previous != null) {
                throw new InvalidFileMappingException("File client key at index " + i + " already exists");
            }
        }
        return uploadedFiles;
    }

    private Map<Long, ProductImage> indexExistingImages(Product product) {
        Map<Long, ProductImage> existingImages = new HashMap<>();
        for (ProductColor color : product.getColors()) {
            for (ProductImage image : color.getImages()) {
                existingImages.put(image.getId(), image);
            }
        }
        return existingImages;
    }

    private List<String> collectPathsToDelete(Product product,
                                              AdminProductCreateRequest request,
                                              Map<Long, ProductImage> existingImages) {
        Set<Long> retainedExistingImageIds = request.getColors().stream()
                .flatMap(color -> color.getImages().stream())
                .map(AdminProductImageRequest::getExistingImageId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return existingImages.entrySet().stream()
                .filter(entry -> !retainedExistingImageIds.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(ProductImage::getPath)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<String> collectAllImagePaths(Product product) {
        return product.getColors().stream()
                .flatMap(color -> color.getImages().stream())
                .map(ProductImage::getPath)
                .filter(Objects::nonNull)
                .toList();
    }

    private void scheduleDeleteAfterCommit(Collection<String> pathsToDelete) {
        if (pathsToDelete == null || pathsToDelete.isEmpty()) {
            return;
        }

        List<String> immutablePaths = new ArrayList<>(pathsToDelete);
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            deletePathsBestEffort(immutablePaths);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                deletePathsBestEffort(immutablePaths);
            }
        });
    }

    private void deletePathsBestEffort(List<String> pathsToDelete) {
        pathsToDelete.forEach(path -> {
            try {
                productImageStorageService.delete(path);
            } catch (RuntimeException ex) {
                log.warn("Failed to delete orphan product image {}", path, ex);
            }
        });
    }

    private Product findProduct(Long productId) {
        return productRepository.findDetailById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private AdminCreatedProductResponse mapResponse(Product savedProduct) {
        return new AdminCreatedProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getCategory().getName(),
                savedProduct.getProductStatus()
        );
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    private Integer defaultOrder(Integer value) {
        return value == null ? Integer.MAX_VALUE : value;
    }
}
