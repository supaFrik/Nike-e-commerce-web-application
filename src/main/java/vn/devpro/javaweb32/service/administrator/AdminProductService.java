package vn.devpro.javaweb32.service.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.devpro.javaweb32.common.base.BaseService;
import vn.devpro.javaweb32.dto.customer.product.ProductDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;
import vn.devpro.javaweb32.dto.administrator.ProductSearch;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class AdminProductService extends BaseService<Product> {

    @Autowired
    private CategoryAdminService categoryAdminService;

    @Override
    public Class<Product> clazz() { return Product.class; }

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

    @Transactional
    public Product saveProductFromDto(ProductDto dto) {
        if (dto == null) throw new IllegalArgumentException("productDto is null");
        if (dto.getCategoryId() == null) throw new IllegalArgumentException("Category is required");
        if (dto.getName() == null || dto.getName().isBlank()) throw new IllegalArgumentException("Name is required");

        // Name conflict check (ignoring case + spaces)
        if (existsNameConflict(dto.getName(), dto.getId())) {
            throw new IllegalArgumentException("A product with the same name already exists");
        }

        Product product = (dto.getId() == null) ? new Product() : getById(dto.getId());
        if (product == null) product = new Product();

        if (product.getCreateDate() == null) product.setCreateDate(new java.util.Date());
        product.setUpdateDate(new java.util.Date());

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setType(dto.getType());
        product.setSeo(dto.getSeo());
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : "ACTIVE"); // BaseEntity.status
        product.setProductStatus(mapStatusToEnum(dto.getStatus())); // enum

        var category = categoryAdminService.getById(dto.getCategoryId());
        if (category == null) throw new IllegalArgumentException("Category id not found: " + dto.getCategoryId());
        product.setCategory(category);

        // Reset collections if editing
        if (product.getColors() == null) product.setColors(new java.util.ArrayList<>()); else product.getColors().clear();
        if (product.getVariants() == null) product.setVariants(new java.util.ArrayList<>()); else product.getVariants().clear();

        // Colors
        if (dto.getColors() != null) {
            for (ProductDto.ColorDto c : dto.getColors()) {
                if (c == null || c.getColorName() == null || c.getColorName().isBlank()) continue;
                ProductColor pc = new ProductColor();
                pc.setColorName(c.getColorName().trim());
                pc.setHexCode(c.getHexCode());
                pc.setProduct(product);
                product.getColors().add(pc);
            }
        }

        Integer defaultStock = dto.getStock();

        // Variants
        if (dto.getVariants() != null) {
            for (ProductDto.VariantDto v : dto.getVariants()) {
                if (v == null || v.getSize() == null || v.getSize().isBlank()) continue;
                ProductVariant pv = new ProductVariant();
                pv.setProduct(product);
                pv.setSizeLabel(v.getSize());
                Integer variantStock = (v.getStock() != null ? v.getStock() : defaultStock);
                pv.setStock(variantStock != null ? variantStock : 0);
                if (v.getColorIndex() != null && v.getColorIndex() >= 0 && v.getColorIndex() < product.getColors().size()) {
                    pv.setColor(product.getColors().get(v.getColorIndex()));
                }
                product.getVariants().add(pv);
            }
        }
        if (product.getVariants().isEmpty() && defaultStock != null) {
            ProductVariant pv = new ProductVariant();
            pv.setProduct(product);
            pv.setSizeLabel("DEFAULT");
            pv.setStock(defaultStock);
            product.getVariants().add(pv);
        }
        return saveOrUpdate(product);
    }

    public ProductSearch buildSearchModel(HttpServletRequest request) {
        ProductSearch ps = new ProductSearch();
        String statusParam = request.getParameter("status");
        if (statusParam == null || statusParam.isEmpty()) {
            ps.setStatus("In Order");

        } else { ps.setStatus(mapStatusIntToString(statusParam)); }
        String categoryParam = request.getParameter("categoryId");

        if (categoryParam != null && !categoryParam.isEmpty()) {
            ps.setCategoryId(Integer.parseInt(categoryParam));

        } else { ps.setCategoryId(0); }

        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.isEmpty()) ps.setKeyword(keyword.trim());
        String currentPageParam = request.getParameter("currentPage");

        if (currentPageParam != null && !currentPageParam.isEmpty()) {
            ps.setCurrentPage(Integer.parseInt(currentPageParam));

        } else { ps.setCurrentPage(1); }

        return ps;
    }

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
        } catch (NumberFormatException e) { return statusParam; }
    }

    private ProductStatus mapStatusToEnum(String status) {
        if (status == null) return ProductStatus.ACTIVE;
        String s = status.trim().toUpperCase().replace(' ', '_');
        try { return ProductStatus.valueOf(s); } catch (IllegalArgumentException ex) { return ProductStatus.ACTIVE; }
    }

    public boolean existsNameConflict(String name, Long excludeId) {
        if (name == null) return false;
        // Normalize: lowercase + remove ALL whitespace characters so spacing differences don't bypass uniqueness
        String norm = name.toLowerCase().replaceAll("\\s+", "");
        try {
            var query = em().createNativeQuery("SELECT id FROM products WHERE REPLACE(LOWER(name),' ','') = ?1");
            query.setParameter(1, norm);
            @SuppressWarnings("unchecked")
            var results = (java.util.List<Object[]>) query.getResultList();
            for (Object rowObj : results) {
                Object[] row = (rowObj instanceof Object[])? (Object[]) rowObj : new Object[]{rowObj};
                Long id = null;
                if (row.length > 0 && row[0] instanceof Number) {
                    id = ((Number) row[0]).longValue();
                }
                if (id != null && excludeId != null && id.equals(excludeId)) continue;
                return true; // exact (normalized) duplicate exists
            }
        } catch (Exception ex) {
            System.err.println("Name conflict exact check failed: " + ex.getMessage());
        }
        return false;
    }

    public String generateNameSuggestion(String desiredName, Long excludeId) {
        if (desiredName == null || desiredName.isBlank()) return null;
        String base = desiredName.trim();
        // If no conflict, just return original
        if (!existsNameConflict(base, excludeId)) return base;
        // Strip trailing incremental suffix pattern " (n)" if already present to avoid double stacking
        base = base.replaceAll("\\s*\\(\\d+\\)$", "").trim();
        int counter = 1;
        while (counter < 1000) { // hard upper bound safety
            String candidate = base + " (" + counter + ")";
            if (!existsNameConflict(candidate, excludeId)) return candidate;
            counter++;
        }
        // Fallback: timestamp suffix
        return base + "-" + System.currentTimeMillis();
    }

    public static class PagedProducts {
        public List<Product> items; public int page; public int pageSize; public long totalItems; public int totalPages;
    }

    public PagedProducts searchPaged(String keyword, Long categoryId, int page, int size, String sortKey) {
        if (page < 1) page = 1; if (size < 1) size = 20; if (size > 200) size = 200;
        // Reuse existing search model logic (in-memory pagination for simplicity)
        vn.devpro.javaweb32.dto.administrator.ProductSearch ps = new vn.devpro.javaweb32.dto.administrator.ProductSearch();
        ps.setKeyword(keyword);
        if (categoryId != null && categoryId > 0) ps.setCategoryId(categoryId.intValue());
        ps.setStatus("In Order");
        List<Product> all = search(ps);
        // Sorting in-memory
        if (sortKey != null) {
            switch (sortKey) {
                case "price" -> all.sort(java.util.Comparator.comparing(p -> p.getSalePrice() != null ? p.getSalePrice() : p.getPrice()));
                case "stock" -> all.sort((a,b) -> {
                    Integer sa = a.getTotalStock(); Integer sb = b.getTotalStock();
                    int va = sa == null ? 0 : sa; int vb = sb == null ? 0 : sb; return Integer.compare(vb, va); // desc stock
                });
                default -> all.sort(java.util.Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
            }
        } else {
            all.sort(java.util.Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
        }
        long total = all.size();
        int from = (page - 1) * size;
        if (from >= all.size()) from = 0; // reset if overflow
        int to = Math.min(from + size, all.size());
        List<Product> slice = all.subList(from, to);
        PagedProducts result = new PagedProducts();
        result.items = slice;
        result.page = page;
        result.pageSize = size;
        result.totalItems = total;
        result.totalPages = (int) Math.max(1, Math.ceil(total / (double) size));
        return result;
    }
}
