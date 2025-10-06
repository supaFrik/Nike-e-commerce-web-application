package vn.devpro.javaweb32.service.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import vn.devpro.javaweb32.common.base.BaseService;
import vn.devpro.javaweb32.dto.customer.product.ProductDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductImage;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.dto.administrator.ProductSearch;
import vn.devpro.javaweb32.service.administrator.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductAdminService extends BaseService<Product> {

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
        Product product;
        if (dto.getId() != null) {
            product = getById(dto.getId());
            if (product == null) product = new Product();
        } else {
            product = new Product();
        }

        product.setName(dto.getName());
        product.setPrice(dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO);
        product.setSalePrice(dto.getSalePrice());
        product.setDescription(dto.getDescription());
        product.setType(dto.getType());
        product.setSeo(dto.getSeo());
        product.setStatus(dto.getStatus());
        product.setFavourites(dto.isFavourites());

        // colors
        List<ProductColor> colorEntities = new ArrayList<>();
        String safeProductName = sanitizeForFilename(product.getName());

        List<Path> tempToRemoveIfFail = new ArrayList<>(); // cleanup if exception before commit

        for (int i = 0; i < dto.getColors().size(); i++) {
            ProductDto.ColorDto c = dto.getColors().get(i);
            if (c.getColorName() == null || c.getColorName().trim().isEmpty()) continue;

            String safeColor = sanitizeForFilename(c.getColorName());
            String folderPath = safeProductName + "/" + safeColor;
            String baseImagePrefix = safeColor + "+" + safeProductName;
            if (baseImagePrefix.length() > 80) baseImagePrefix = baseImagePrefix.substring(0, 80);

            ProductColor pc = new ProductColor();
            pc.setColorName(c.getColorName().trim());
            pc.setProduct(product);
            pc.setFolderPath(folderPath);
            pc.setBaseImage(baseImagePrefix);

            List<ProductImage> images = new ArrayList<>();

            MultipartFile[] uploaded = c.getImages();
            if (uploaded != null) {
                for (MultipartFile mf : uploaded) {
                    if (mf == null || mf.isEmpty()) continue;
                    // Lưu temp file ảnh
                    Path temp = fileStorageService.saveTempFile(mf);
                    tempToRemoveIfFail.add(temp);

                    // Ext: Tạo UUID theo file ảnh
                    String ext = getExtensionFromTemp(temp);
                    String finalFilename = baseImagePrefix + "-" + UUID.randomUUID() + ext;
                    Path finalAbs = fileStorageService.getProductBaseDir().resolve(folderPath).resolve(finalFilename);

                    fileStorageService.registerMoveOnCommit(temp, finalAbs);

                    ProductImage pi = new ProductImage();
                    String publicUrl = "/images/products/" + folderPath + "/" + finalFilename;
                    pi.setUrl(publicUrl);
                    pi.setStatus("ACTIVE");
                    pi.setProduct(product);
                    images.add(pi);
                }
            }

            for (ProductImage pi : images) {
                pc.addRelationalProductImage(pi);
            }

            if (c.getExistingPreviewFilename() != null && !c.getExistingPreviewFilename().isBlank()) {
                String existing = c.getExistingPreviewFilename().trim();
                if (!existing.contains("..") && !existing.contains("/") && !existing.contains("\\")) {
                    pc.setPreviewImage(existing);
                }
            } else if (!images.isEmpty()) {
                pc.setPreviewImage(images.get(0).getUrl().substring(images.get(0).getUrl().lastIndexOf('/') + 1));
            } else {
                pc.setPreviewImage(null);
            }

            colorEntities.add(pc);
        }

        List<ProductVariant> variants = new ArrayList<>();
        for (int i = 0; i < dto.getVariants().size(); i++) {
            ProductDto.VariantDto v = dto.getVariants().get(i);
            ProductVariant pv = new ProductVariant();
            pv.setProduct(product);
            pv.setSize(v.getSize());
            pv.setPrice(v.getPrice() != null ? v.getPrice() : BigDecimal.ZERO);
            pv.setStock(v.getStock() != null ? v.getStock() : 0);
            if (v.getColorIndex() != null && v.getColorIndex() >= 0 && v.getColorIndex() < colorEntities.size()) {
                ProductColor linked = colorEntities.get(v.getColorIndex());
                pv.setColor(linked);
                pv.setColorName(linked.getColorName());
            }
            variants.add(pv);
        }

        product.getColors().clear();
        product.getColors().addAll(colorEntities);

        product.getVariants().clear();
        product.getVariants().addAll(variants);

        // timestamps
        Date now = new Date();
        if (product.getCreateDate() == null) product.setCreateDate(now);
        product.setUpdateDate(now);
        super.saveOrUpdate(product);

        return product;
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
            return statusParam; // fallback nếu param đã là text
        }
    }
}
