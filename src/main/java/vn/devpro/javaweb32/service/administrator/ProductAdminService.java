package vn.devpro.javaweb32.service.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.devpro.javaweb32.common.base.BaseService;
import vn.devpro.javaweb32.dto.customer.product.ProductDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductImage;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.dto.administrator.ProductSearch;
import vn.devpro.javaweb32.service.FileStorageService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductAdminService extends BaseService<Product> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CategoryAdminService categoryAdminService;

    @Override
    public Class<Product> clazz() {
        return Product.class;
    }

    @SuppressWarnings("unchecked")
    public List<Product> search(ProductSearch productSearch) {
        String sql = "SELECT * FROM products p WHERE 1=1";

        if (productSearch.getStatus() != null && !productSearch.getStatus().equals("In Order")) {
            String status = productSearch.getStatus().replace("'", "''");
            sql += " AND p.status='" + status + "'";
        }
        if (productSearch.getCategoryId() != null && productSearch.getCategoryId() != 0) {
            sql += " AND p.category_id=" + productSearch.getCategoryId();
        }
        if (productSearch.getKeyword() != null && !productSearch.getKeyword().isEmpty()) {
            String keyword = productSearch.getKeyword().toLowerCase().replace("'", "''");
            sql += " AND (LOWER(p.name) LIKE '%" + keyword + "%' OR LOWER(p.description) LIKE '%" + keyword + "%')";
        }
        Date beginDate = productSearch.getBeginDate();
        Date endDate = productSearch.getEndDate();
        if (beginDate != null && endDate != null) {
            sql += " AND p.create_date BETWEEN '" + beginDate + "' AND '" + endDate + "'";
        }

        return executeNativeSql(sql);
    }

    private String sanitizeForFilename(String input) {
        if (input == null) return "";
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        normalized = normalized.replaceAll("[\\\\/]+", "");
        normalized = normalized.replaceAll("[^A-Za-z0-9]+", "+");
        normalized = normalized.replaceAll("^\\++|\\++$", "");
        if (normalized.isEmpty()) normalized = UUID.randomUUID().toString();
        if (normalized.length() > 80) normalized = normalized.substring(0, 80);
        return normalized.toUpperCase();
    }

    private String getExtensionFromTemp(Path temp) {
        String n = temp.getFileName().toString();
        int i = n.lastIndexOf('.');
        return (i >= 0) ? n.substring(i) : ".avif";
    }

    @Transactional
    public Product saveProductFromDto(ProductDto dto) throws IOException {
        System.out.println("=== DEBUGGING PRODUCT SAVE ===");

        if (dto == null) throw new IllegalArgumentException("productDto is null");
        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException("Category is required");
        }

        System.out.println("DTO received - Name: " + dto.getName());
        System.out.println("DTO Colors count: " + (dto.getColors() != null ? dto.getColors().size() : "null"));

        // Debug
        if (dto.getColors() != null) {
            for (int i = 0; i < dto.getColors().size(); i++) {
                var color = dto.getColors().get(i);
                System.out.println("Color " + i + ": " + (color != null ? color.getColorName() : "null"));
                if (color != null && color.getImages() != null) {
                    System.out.println("  - Images count: " + color.getImages().length);
                }
            }
        }

        Product product;
        boolean isNew = (dto.getId() == null);
        if (isNew) {
            product = new Product();
            product.setCreateDate(new Date());
        } else {
            product = getById(dto.getId());
            if (product == null) {
                product = new Product();
                product.setCreateDate(new Date());
            }
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setType(dto.getType());
        product.setSeo(dto.getSeo());
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : "ACTIVE");
        product.setFavourites(dto.isFavourites());
        product.setUpdateDate(new Date());

        // Set category
        var category = categoryAdminService.getById(dto.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("Category id not found: " + dto.getCategoryId());
        }
        product.setCategory(category);

        if (product.getColors() == null) {
            product.setColors(new ArrayList<>());
        }
        if (product.getVariants() == null) {
            product.setVariants(new ArrayList<>());
        }

        System.out.println("Saving basic product...");
        Product saved = saveOrUpdate(product);
        System.out.println("Product saved with ID: " + saved.getId());

        if (dto.getColors() != null && !dto.getColors().isEmpty()) {
            System.out.println("Processing " + dto.getColors().size() + " colors...");

            saved.getColors().clear();

            int colorCount = 0;
            for (ProductDto.ColorDto cDto : dto.getColors()) {
                if (cDto == null || cDto.getColorName() == null || cDto.getColorName().isBlank()) {
                    System.out.println("Skipping null/empty color");
                    continue;
                }

                System.out.println("Creating ProductColor for: " + cDto.getColorName());

                ProductColor pc = new ProductColor();
                pc.setColorName(cDto.getColorName().trim());
                pc.setProduct(saved);

                String safeProductName = sanitizeForFilename(saved.getName());
                String safeColorName = sanitizeForFilename(cDto.getColorName());
                String folderPath = safeProductName + "/" + safeColorName;
                pc.setFolderPath(folderPath);
                pc.setBaseImage(safeColorName + "+" + safeProductName);

                pc.setPreviewImage("default.jpg");

                System.out.println("  - Folder path: " + folderPath);
                System.out.println("  - Base image: " + pc.getBaseImage());

                // Process images if any
                List<ProductImage> savedImages = new ArrayList<>();
                MultipartFile[] files = cDto.getImages();

                if (files != null && files.length > 0) {
                    System.out.println("  - Processing " + files.length + " image files");
                    List<MultipartFile> validFiles = new ArrayList<>();
                    for (MultipartFile file : files) {
                        if (file != null && !file.isEmpty()) {
                            validFiles.add(file);
                            System.out.println("    - Valid file: " + file.getOriginalFilename());
                        }
                    }

                    if (!validFiles.isEmpty()) {
                        try {
                            savedImages = fileStorageService.saveTempFile(validFiles, folderPath, pc.getBaseImage());
                            System.out.println("  - Saved " + savedImages.size() + " images to temp");

                            // Link images to product and color
                            for (ProductImage img : savedImages) {
                                img.setProduct(saved);
                                img.setColor(pc);
                            }

                            // Update preview image if we have uploads
                            if (!savedImages.isEmpty()) {
                                String previewUrl = savedImages.get(0).getUrl();
                                if (previewUrl.contains("/")) {
                                    pc.setPreviewImage(previewUrl.substring(previewUrl.lastIndexOf('/') + 1));
                                } else {
                                    pc.setPreviewImage(previewUrl);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error processing images: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("  - No images to process");
                }

                // Handle existing preview for edit mode
                if (cDto.getExistingPreviewFilename() != null && !cDto.getExistingPreviewFilename().isBlank()) {
                    ProductImage existingPreview = new ProductImage();
                    existingPreview.setUrl(cDto.getExistingPreviewFilename());
                    existingPreview.setStatus("ACTIVE");
                    existingPreview.setProduct(saved);
                    existingPreview.setColor(pc);
                    savedImages.add(0, existingPreview);
                }

                pc.setImages(savedImages);

                // CRITICAL: Add to parent collection for cascade
                saved.getColors().add(pc);
                colorCount++;

                System.out.println("  - ProductColor created and added to product");
            }

            System.out.println("Total colors processed: " + colorCount);
            System.out.println("Product colors collection size: " + saved.getColors().size());
        } else {
            System.out.println("No colors to process (DTO colors is null or empty)");
        }

        // variants
        if (dto.getVariants() != null && !dto.getVariants().isEmpty()) {
            System.out.println("Processing " + dto.getVariants().size() + " variants...");
            saved.getVariants().clear();

            for (ProductDto.VariantDto v : dto.getVariants()) {
                if (v == null) continue;

                ProductVariant pv = new ProductVariant();
                pv.setProduct(saved);
                pv.setSize(v.getSize());
                pv.setPrice(v.getPrice() != null ? v.getPrice() : saved.getPrice());
                pv.setStock(v.getStock() != null ? v.getStock() : 0);

                // Link to color if specified
                if (v.getColorIndex() != null && v.getColorIndex() >= 0 &&
                    saved.getColors() != null && v.getColorIndex() < saved.getColors().size()) {
                    ProductColor linkedColor = saved.getColors().get(v.getColorIndex());
                    pv.setColor(linkedColor);
                    pv.setColorName(linkedColor.getColorName());
                }

                saved.getVariants().add(pv);
            }
            System.out.println("Variants processed: " + saved.getVariants().size());
        }

        System.out.println("Final save with colors and variants...");
        System.out.println("About to save product with " + saved.getColors().size() + " colors");

        // Final save with all relations
        saved = saveOrUpdate(saved);

        System.out.println("Flushing to database...");
        entityManager.flush();

        System.out.println("=== SAVE COMPLETE ===");
        System.out.println("Final product ID: " + saved.getId());
        System.out.println("Final colors count: " + (saved.getColors() != null ? saved.getColors().size() : "null"));

        // Move temp files to final location
        if (saved.getColors() != null && !saved.getColors().isEmpty()) {
            try {
                fileStorageService.moveTempToProductFolder(saved);
                System.out.println("File move completed");
            } catch (Exception e) {
                System.err.println("Warning: File move failed: " + e.getMessage());
            }
        }

        return saved;
    }

    public ProductSearch buildSearchModel(HttpServletRequest request) {
        ProductSearch productSearch = new ProductSearch();

        // Default
        String statusParam = request.getParameter("status");
        if (statusParam == null || statusParam.isEmpty()) {
            productSearch.setStatus("In Order");
        } else {
            productSearch.setStatus(mapStatusIntToString(statusParam));
        }

        // Category
        String categoryParam = request.getParameter("categoryId");
        if (categoryParam != null && !categoryParam.isEmpty()) {
            productSearch.setCategoryId(Integer.parseInt(categoryParam));
        } else {
            productSearch.setCategoryId(0);
        }

        // Keyword
        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            productSearch.setKeyword(keyword.trim());
        }

        // Current page
        String currentPageParam = request.getParameter("currentPage");
        if (currentPageParam != null && !currentPageParam.isEmpty()) {
            productSearch.setCurrentPage(Integer.parseInt(currentPageParam));
        } else {
            productSearch.setCurrentPage(1);
        }

        return productSearch;
    }

    /**
     * Chuyển status code (0,1,2,...) thành String
     */
    private String mapStatusIntToString(String statusParam) {
        try {
            int statusInt = Integer.parseInt(statusParam);
            switch (statusInt) {
                case 0: return "In Order";
                case 1: return "Bestseller";
                case 2: return "Out Of Stock";
                case 3: return "On Sale";
                case 4: return "Limited";
                case 5: return "Just In";
                default: return "In Order";
            }
        } catch (NumberFormatException e) {
            return statusParam;
        }
    }
}
