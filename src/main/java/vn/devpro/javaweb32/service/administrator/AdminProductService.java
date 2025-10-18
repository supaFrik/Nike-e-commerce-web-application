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
import vn.devpro.javaweb32.dto.administrator.ProductCreateRequest;
import vn.devpro.javaweb32.entity.product.ProductImage;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.*;
import java.util.Base64;

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

    // ============================= NEW PUBLIC API (extracted from controller) =============================

    public Product createProduct(ProductCreateRequest request, String uploadRoot) throws java.io.IOException {
        validateCreateRequest(request);
        Product product = new Product();
        applySimpleFields(product, request);
        product.setColors(new ArrayList<>());
        product.setVariants(new ArrayList<>());
        product.setImages(new ArrayList<>());
        product = saveOrUpdate(product); // to have ID
        List<ProductColor> createdColors = buildColorsAndVariants(product, request);
        product = saveOrUpdate(product);
        ProductImage main = processAndAttachImages(product, request, createdColors, uploadRoot);
        if (main != null) product.setMainImage(main);
        return saveOrUpdate(product);
    }

    @Transactional
    public Product fullUpdateProduct(Long id, ProductCreateRequest request, String uploadRoot) throws java.io.IOException {
        Product product = getById(id);
        if (product == null) throw new IllegalArgumentException("Product not found");
        validateCreateRequest(request);
        if (isSameColorStructure(product, request)) {
            applySimpleFields(product, request);
            product.setUpdateDate(new Date());
            return saveOrUpdate(product);
        }
        applySimpleFields(product, request);
        if (product.getColors() != null) product.getColors().clear(); else product.setColors(new ArrayList<>());
        if (product.getVariants() != null) product.getVariants().clear(); else product.setVariants(new ArrayList<>());
        if (product.getImages() != null) product.getImages().clear(); else product.setImages(new ArrayList<>());
        product = saveOrUpdate(product);
        List<ProductColor> createdColors = buildColorsAndVariants(product, request);
        product = saveOrUpdate(product);
        ProductImage main = processAndAttachImages(product, request, createdColors, uploadRoot);
        if (main != null) product.setMainImage(main);
        return saveOrUpdate(product);
    }

    @Transactional
    public Product partialUpdateProduct(Long id, Map<String,Object> body) {
        Product p = getById(id);
        if (p == null) throw new IllegalArgumentException("Product not found");
        if (body.containsKey("name")) {
            String name = asString(body.get("name"));
            if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
            p.setName(name.trim());
        }
        if (body.containsKey("description")) {
            String d = asString(body.get("description"));
            if (d != null && d.length() > 1000) d = d.substring(0, 1000);
            p.setDescription(d);
        }
        if (body.containsKey("price")) {
            BigDecimal price = toBigDecimal(body.get("price"));
            if (price == null || price.doubleValue() <= 0) throw new IllegalArgumentException("Price must be > 0");
            p.setPrice(price);
            if (p.getSalePrice() == null) p.setSalePrice(price);
        }
        if (body.containsKey("salePrice")) {
            BigDecimal sale = toBigDecimal(body.get("salePrice"));
            if (sale != null && sale.doubleValue() > 0) p.setSalePrice(sale);
        }
        if (body.containsKey("type")) {
            String t = asString(body.get("type"));
            if (t != null) {
                t = t.trim().toUpperCase();
                if (!t.isBlank()) {
                    if (!t.equals("MEN") && !t.equals("WOMEN") && !t.equals("UNISEX")) {
                        t = "UNISEX";
                    }
                    p.setType(t);
                }
            }
        }
        if (body.containsKey("categoryId")) {
            Long catId = toLong(body.get("categoryId"));
            if (catId != null) {
                var cat = categoryAdminService.getById(catId);
                if (cat == null) throw new IllegalArgumentException("Category not found");
                p.setCategory(cat);
            }
        }
        if (body.containsKey("stock")) {
            Integer stock = toInt(body.get("stock"));
            if (stock != null && stock >= 0) allocateStock(p, stock);
        }
        p.setUpdateDate(new Date());
        return saveOrUpdate(p);
    }

    public Map<String,Object> buildSummary(Product p) {
        if (p == null) return Map.of("error", "null-product");
        LinkedHashMap<String,Object> m = new LinkedHashMap<>();
        String name = p.getName();
        if (name == null || name.isBlank()) name = "(Unnamed)";
        String description = p.getDescription();
        if (description != null && description.length() > 4000) description = description.substring(0,4000);
        String imageUrl = null; try { imageUrl = p.getImageUrl(); } catch (Exception ignored) {}
        BigDecimal displayPrice = (p.getSalePrice()!=null ? p.getSalePrice() : (p.getPrice()!=null? p.getPrice(): BigDecimal.ZERO));
        Integer totalStock = null; try { totalStock = p.getTotalStock(); } catch (Exception ignored) {}
        String catName = (p.getCategory()!=null? p.getCategory().getName(): null);
        if (catName != null && catName.isBlank()) catName = null;
        String catKey = catName != null ? slug(catName) : null;
        m.put("id", p.getId());
        m.put("name", name);
        m.put("description", description);
        m.put("imageUrl", imageUrl);
        m.put("hasImage", imageUrl != null && !imageUrl.isBlank());
        m.put("displayPrice", displayPrice);
        m.put("stock", totalStock != null ? totalStock : 0);
        m.put("category", catName);
        m.put("categoryKey", catKey);
        m.put("status", p.getStatus());
        m.put("productStatus", p.getProductStatus() != null ? p.getProductStatus().name() : null);
        return m;
    }

    private void applySimpleFields(Product product, ProductCreateRequest request){
        product.setName(request.getName());
        String desc = request.getDescription();
        if (desc != null && desc.length() > 1000) desc = desc.substring(0, 1000);
        product.setDescription(desc);
        product.setPrice(request.getPrice());
        product.setSalePrice(request.getSalePrice() != null ? request.getSalePrice() : request.getPrice());
        product.setType(request.getType() != null ? request.getType() : "UNISEX");
        product.setSeo(request.getSeo());
        product.setStatus("ACTIVE");
        product.setProductStatus(ProductStatus.ACTIVE);
        if (request.getCategoryId() != null) {
            var cat = categoryAdminService.getById(request.getCategoryId());
            if (cat != null) product.setCategory(cat);
        }
    }

    private List<ProductColor> buildColorsAndVariants(Product product, ProductCreateRequest request) {
        Map<String,Integer> colorSlugCounts = new HashMap<>();
        List<ProductColor> createdColors = new ArrayList<>();
        if (request.getColors() == null) return createdColors;
        for (ProductCreateRequest.ColorData c : request.getColors()) {
            if (c == null) continue;
            ProductColor pc = new ProductColor();
            pc.setColorName(c.getName());
            pc.setProduct(product);
            String baseSlug = simpleSlug(c.getName());
            int count = colorSlugCounts.getOrDefault(baseSlug, 0);
            colorSlugCounts.put(baseSlug, count + 1);
            String uniqueSlug = count == 0 ? baseSlug : baseSlug + "-" + (count + 1);
            pc.setFolderPath(uniqueSlug);
            pc.setBaseImage(simpleSlug(product.getName()) + "-" + uniqueSlug);
            product.getColors().add(pc);
            createdColors.add(pc);
            List<String> sizes = c.getSizes();
            if (sizes != null && !sizes.isEmpty()) {
                List<Integer> sizeStocks = c.getSizeStocks();
                for (int i = 0; i < sizes.size(); i++) {
                    String rawSize = sizes.get(i);
                    if (rawSize == null || rawSize.isBlank()) continue;
                    ProductVariant pv = new ProductVariant();
                    pv.setProduct(product);
                    pv.setColor(pc);
                    pv.setSizeLabel(rawSize.trim());
                    Integer stock = null;
                    if (sizeStocks != null && i < sizeStocks.size()) stock = sizeStocks.get(i);
                    if (stock == null) stock = c.getDefaultStock();
                    if (stock == null) stock = request.getDefaultStock();
                    if (stock == null) stock = 0;
                    pv.setStock(stock);
                    product.getVariants().add(pv);
                }
            }
        }
        return createdColors;
    }

    private ProductImage processAndAttachImages(Product product, ProductCreateRequest request, List<ProductColor> createdColors, String uploadRoot) throws java.io.IOException {
        ProductImage chosenMainImage = null;
        if (request.getColors() == null) return null;
        for (int cIdx = 0; cIdx < request.getColors().size(); cIdx++) {
            ProductCreateRequest.ColorData c = request.getColors().get(cIdx);
            if (c == null || c.getImages() == null) continue;
            if (cIdx >= createdColors.size()) continue;
            ProductColor pc = null;
            if (product.getColors() != null && cIdx < product.getColors().size()) {
                pc = product.getColors().get(cIdx);
            } else {
                pc = createdColors.get(cIdx);
            }
            int idx = 0;
            Integer defaultIdx = c.getDefaultImageIndex();
            for (String dataUrl : c.getImages()) {
                String relativePath;
                if (dataUrl != null && dataUrl.startsWith("data:")) {
                    relativePath = saveBase64Image(dataUrl, product, pc, idx, uploadRoot);
                } else {
                    if (dataUrl == null) { idx++; continue; }
                    if (dataUrl.startsWith("http")) {
                        int pos = dataUrl.indexOf("/images/");
                        relativePath = (pos >=0 ? dataUrl.substring(pos) : dataUrl);
                    } else {
                        relativePath = dataUrl;
                    }
                }
                ProductImage img = new ProductImage();
                img.setPath(relativePath);
                img.setProduct(product);
                img.setColor(pc);
                img.setStatus("ACTIVE");
                product.getImages().add(img);
                if (defaultIdx != null && defaultIdx == idx && chosenMainImage == null) {
                    chosenMainImage = img;
                }
                idx++;
            }
        }
        if (chosenMainImage == null && !product.getImages().isEmpty()) {
            chosenMainImage = product.getImages().get(0);
        }
        return chosenMainImage;
    }

    private void allocateStock(Product p, int stock) {
        if (p.getVariants() != null && !p.getVariants().isEmpty()) {
            int variants = p.getVariants().size();
            int base = variants == 0 ? stock : stock / variants;
            int remainder = variants == 0 ? 0 : stock % variants;
            for (int i = 0; i < p.getVariants().size(); i++) {
                ProductVariant v = p.getVariants().get(i);
                int val = base + (i == 0 ? remainder : 0);
                v.setStock(val);
            }
        } else {
            ProductVariant v = new ProductVariant();
            v.setProduct(p);
            v.setSizeLabel("DEFAULT");
            v.setStock(stock);
            List<ProductVariant> list = new ArrayList<>();
            list.add(v);
            p.setVariants(list);
        }
    }

    private void validateCreateRequest(ProductCreateRequest r) {
        if (r.getName() == null || r.getName().isBlank()) throw new IllegalArgumentException("Name required");
        if (r.getPrice() == null || r.getPrice().doubleValue() <= 0) throw new IllegalArgumentException("Price must be > 0");
        if (r.getColors() == null || r.getColors().isEmpty()) throw new IllegalArgumentException("At least one color required");
        boolean anyImage = r.getColors().stream().anyMatch(c -> c.getImages() != null && !c.getImages().isEmpty());
        if (!anyImage) throw new IllegalArgumentException("At least one image required");
        for (var color : r.getColors()) {
            if (color == null) continue;
            var sizes = color.getSizes();
            var sizeStocks = color.getSizeStocks();
            if (sizeStocks != null && !sizeStocks.isEmpty() && sizes != null && sizeStocks.size() != sizes.size()) {
                throw new IllegalArgumentException("sizeStocks length must match sizes length for color " + color.getName());
            }
        }
    }

    private boolean isSameColorStructure(Product product, ProductCreateRequest req){
        if (req.getColors() == null) return product.getColors()==null || product.getColors().isEmpty();
        if (product.getColors() == null) return false;
        Map<String,Integer> existing = new HashMap<>();
        for(ProductColor pc: product.getColors()){ existing.merge(pc.getColorName()==null?"":pc.getColorName(),1,Integer::sum); }
        Map<String,Integer> incoming = new HashMap<>();
        for(var c : req.getColors()){ if(c==null) continue; incoming.merge(c.getName()==null?"":c.getName(),1,Integer::sum); }
        return existing.equals(incoming);
    }

    private String saveBase64Image(String dataUrl, Product product, ProductColor color, int index, String uploadRoot) throws java.io.IOException {
        if (dataUrl == null || !dataUrl.startsWith("data:")) throw new IllegalArgumentException("Invalid image data");
        int comma = dataUrl.indexOf(',');
        if (comma < 0) throw new IllegalArgumentException("Invalid data URL");
        String meta = dataUrl.substring(5, comma);
        String base64 = dataUrl.substring(comma + 1);
        String[] metaParts = meta.split(";");
        String mime = metaParts.length > 0 ? metaParts[0] : "image/avif";
        String ext = switch (mime) {
            case "image/avif" -> ".avif";
            case "image/webp" -> ".webp";
            case "image/png" -> ".png";
            case "image/jpeg", "image/jpg" -> ".jpg";
            default -> ".avif";
        };
        String productDirRaw = (product.getName() == null ? "product" : product.getName().trim());
        String productDir = productDirRaw;
        String folderPath = (color.getFolderPath() != null ? color.getFolderPath() : (color.getColorName() == null ? "default" : color.getColorName()));
        String baseImageRaw = (color.getBaseImage() != null ? color.getBaseImage() : (productDirRaw + "-" + folderPath));
        String baseImage = sanitize(baseImageRaw);
        String fname = baseImage + "-" + (index + 1) + ext;
        Path targetDir = Paths.get(uploadRoot).resolve(productDir).resolve(folderPath);
        Files.createDirectories(targetDir);
        byte[] bytes = Base64.getDecoder().decode(base64);
        Files.write(targetDir.resolve(fname), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        try {
            String encProduct = java.net.URLEncoder.encode(productDir, java.nio.charset.StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String encFolder = java.net.URLEncoder.encode(folderPath, java.nio.charset.StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String encFname = java.net.URLEncoder.encode(fname, java.nio.charset.StandardCharsets.UTF_8.toString()).replace("+", "%20");
            return "/images/products/" + encProduct + "/" + encFolder + "/" + encFname;
        } catch (Exception e) {
            // fallback: return raw path (shouldn't happen)
            return "/images/products/" + productDir + "/" + folderPath + "/" + fname;
        }
    }

    private String sanitize(String in) { return in == null ? "n-a" : in.trim().replaceAll("[^a-zA-Z0-9_-]", "").toLowerCase(); }
    private String asString(Object o){ return o==null? null : o.toString(); }
    private Long toLong(Object o){ try { return o==null? null : Long.valueOf(o.toString()); } catch(Exception e){ return null; } }
    private Integer toInt(Object o){ try { return o==null? null : Integer.valueOf(o.toString()); } catch(Exception e){ return null; } }
    private BigDecimal toBigDecimal(Object o){ try { return o==null? null : new BigDecimal(o.toString()); } catch(Exception e){ return null; } }
    private String simpleSlug(String in) {
        if (in == null) return "n-a";
        return Normalizer.normalize(in, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+","-")
                .replaceAll("^-+|-+$", "");
    }
    private static String slug(String in) {
        if (in == null) return null;
        return Normalizer.normalize(in, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("&", " and ")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
