package vn.demo.nike.features.admin.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.dto.response.AdminCreatedProductResponse;
import vn.demo.nike.features.admin.product.dto.response.AdminProductCategoryOptionResponse;
import vn.demo.nike.features.admin.product.dto.request.AdminProductCreateRequest;
import vn.demo.nike.features.admin.product.dto.response.AdminProductFormResponse;
import vn.demo.nike.features.admin.product.exception.ProductNotFoundException;
import vn.demo.nike.features.catalog.cart.repository.CartItemRepository;
import vn.demo.nike.features.catalog.category.entity.Category;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductColor;
import vn.demo.nike.features.catalog.product.entity.ProductImage;
import vn.demo.nike.features.catalog.product.entity.ProductVariant;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.shared.util.ProductImageUrlResolverUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductValidationService productValidationService;
    private final ProductCleanupService productCleanupService;
    private final ProductBuilderService productBuilderService;
    private final ProductFileMapService productFileMapService;

    @Transactional
    public AdminCreatedProductResponse createProduct(AdminProductCreateRequest request,
                                                     List<MultipartFile> files,
                                                     List<String> fileClientKeys) {
        Map<String, MultipartFile> uploadedFiles = productFileMapService.buildFileMap(files, fileClientKeys);
        productValidationService.validateCreateOrUpdateRequest(request, uploadedFiles, Map.of());

        Category category = findCategory(request.getCategoryId());
        Product product = new Product();
        productBuilderService.applyProductFields(product, request, category);
        productBuilderService.rebuildColorBlocks(product, request, uploadedFiles, Map.of());

        Product savedProduct = productRepository.save(product);
        return mapResponse(savedProduct);
    }

    @Transactional
    public AdminCreatedProductResponse updateProduct(Long productId,
                                                     AdminProductCreateRequest request,
                                                     List<MultipartFile> files,
                                                     List<String> fileClientKeys) {
        Product product = findProduct(productId);
        Map<String, MultipartFile> uploadedFiles = productFileMapService.buildFileMap(files, fileClientKeys);
        Map<Long, ProductImage> existingImages = indexExistingImages(product);
        List<Long> existingVariantIds = collectVariantIds(product);

        productValidationService.validateCreateOrUpdateRequest(request, uploadedFiles, existingImages);

        Category category = findCategory(request.getCategoryId());
        List<String> pathsToDelete = productCleanupService.collectProviderIdsToDelete(product, request, existingImages);

        if (!existingVariantIds.isEmpty()) {
            cartItemRepository.deleteByVariant_IdIn(existingVariantIds);
        }

        productBuilderService.applyProductFields(product, request, category);
        product.getColors().clear();
        productBuilderService.rebuildColorBlocks(product, request, uploadedFiles, existingImages);

        Product savedProduct = productRepository.save(product);
        productCleanupService.scheduleDeleteAfterCommit(pathsToDelete);
        return mapResponse(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = findProduct(productId);
        List<String> pathsToDelete = productCleanupService.collectAllProviderIds(product);
        List<Long> variantIds = collectVariantIds(product);

        if (!variantIds.isEmpty()) {
            cartItemRepository.deleteByVariant_IdIn(variantIds);
        }
        productRepository.delete(product);
        productCleanupService.scheduleDeleteAfterCommit(pathsToDelete);
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
                                        ProductImageUrlResolverUtil.toPublicUrl(image.getUrl()),
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

    private Map<Long, ProductImage> indexExistingImages(Product product) {
        Map<Long, ProductImage> existingImages = new HashMap<>();
        for (ProductColor color : product.getColors()) {
            for (ProductImage image : color.getImages()) {
                existingImages.put(image.getId(), image);
            }
        }
        return existingImages;
    }

    private List<Long> collectVariantIds(Product product) {
        return product.getColors().stream()
                .flatMap(color -> color.getVariants().stream())
                .map(ProductVariant::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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

    private Integer defaultOrder(Integer value) {
        return value == null ? Integer.MAX_VALUE : value;
    }
}
