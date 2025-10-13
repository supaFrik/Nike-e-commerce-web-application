package vn.devpro.javaweb32.controller.administrator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import vn.devpro.javaweb32.dto.administrator.ProductCreateRequest;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductImage;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;
import vn.devpro.javaweb32.service.administrator.AdminProductService;
import vn.devpro.javaweb32.common.constant.Jw32Contant;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Base64;
import java.text.Normalizer;

@RestController
@RequestMapping("/admin/api/products")
public class ProductAdminApiController implements Jw32Contant {

    private static final Logger log = LoggerFactory.getLogger(ProductAdminApiController.class);
    private static final int MAX_DESCRIPTION_LEN = 1000;

    @Autowired
    private AdminProductService productService;

    @Autowired
    private CategoryAdminService categoryService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest request) {
        try {
            validate(request);
            Product product = new Product();
            product.setName(request.getName());
            String desc = request.getDescription();
            if (desc != null && desc.length() > MAX_DESCRIPTION_LEN) desc = desc.substring(0, MAX_DESCRIPTION_LEN);
            product.setDescription(desc);
            product.setPrice(request.getPrice());
            BigDecimal salePrice = request.getSalePrice() != null ? request.getSalePrice() : request.getPrice();
            product.setSalePrice(salePrice);
            product.setType(request.getType() != null ? request.getType() : "UNISEX");
            product.setSeo(request.getSeo());
            product.setStatus("ACTIVE");
            product.setProductStatus(ProductStatus.ACTIVE);

            if (request.getCategoryId() != null) {
                var cat = categoryService.getById(request.getCategoryId());
                if (cat == null) return bad("Category not found");
                product.setCategory(cat);
            }

            product.setColors(new ArrayList<>());
            product.setVariants(new ArrayList<>());
            product.setImages(new ArrayList<>());

            product = productService.saveOrUpdate(product);

            // color & variants
            if (request.getColors() != null) {
                for (ProductCreateRequest.ColorData c : request.getColors()) {
                    if (c == null) continue;
                    ProductColor pc = new ProductColor();
                    pc.setColorName(c.getName());
                    pc.setProduct(product);
                    product.getColors().add(pc);
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
                            if (sizeStocks != null && i < sizeStocks.size()) {
                                stock = sizeStocks.get(i);
                            }
                            if (stock == null) stock = c.getDefaultStock();
                            if (stock == null) stock = request.getDefaultStock();
                            if (stock == null) stock = 0;
                            pv.setStock(stock);
                            product.getVariants().add(pv);
                        }
                    }
                }
            }
            product = productService.saveOrUpdate(product);

            Map<String, ProductColor> colorMap = new HashMap<>();
            for (ProductColor pc : product.getColors()) { colorMap.put(pc.getColorName(), pc); }

            ProductImage chosenMainImage = null;
            if (request.getColors() != null) {
                for (ProductCreateRequest.ColorData c : request.getColors()) {
                    if (c.getImages() == null) continue;
                    ProductColor pc = colorMap.get(c.getName());
                    int idx = 0;
                    Integer defaultIdx = c.getDefaultImageIndex();
                    for (String dataUrl : c.getImages()) {
                        String relativePath = saveBase64Image(dataUrl, product, pc, idx);
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
            }
            if (chosenMainImage == null && !product.getImages().isEmpty()) {
                chosenMainImage = product.getImages().get(0);
            }
            if (chosenMainImage != null) {
                product.setMainImage(chosenMainImage);
            }
            product = productService.saveOrUpdate(product);

            Map<String, Object> response = new HashMap<>();
            response.put("id", product.getId());
            response.put("redirectUrl", "/admin/product/list");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return bad(ex.getMessage());
        } catch (IOException io) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to store images", "message", io.getMessage()));
        }
    }

    @GetMapping(value = "/check-name", produces = "application/json")
    public ResponseEntity<?> checkName(@RequestParam(name = "name", required = false) String name,
                                       @RequestParam(name = "excludeId", required = false) Long excludeId) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.ok(java.util.Map.of(
                    "conflict", false,
                    "suggestion", null,
                    "normalized", null
            ));
        }
        boolean conflict = productService.existsNameConflict(name, excludeId);
        String suggestion = conflict ? productService.generateNameSuggestion(name, excludeId) : name;
        String normalized = name.toLowerCase().replaceAll("\\s+", "");
        return ResponseEntity.ok(java.util.Map.of(
                "conflict", conflict,
                "suggestion", suggestion,
                "normalized", normalized
        ));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> listProducts(@RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "categoryId", required = false) Long categoryId,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                          @RequestParam(value = "sort", required = false, defaultValue = "name") String sort) {
        try {
            var paged = productService.searchPaged(keyword, categoryId, page, size, sort);
            java.util.List<java.util.Map<String,Object>> items = new java.util.ArrayList<>();
            for (Product p : paged.items) {
                try {
                    items.add(summary(p));
                } catch (Exception inner) {
                    log.warn("Failed to summarize product id={}: {}", p != null ? p.getId() : null, inner.toString());
                    items.add(fallbackSummary(p));
                }
            }
            return ResponseEntity.ok(new java.util.LinkedHashMap<String,Object>() {{
                put("page", paged.page);
                put("pageSize", paged.pageSize);
                put("totalItems", paged.totalItems);
                put("totalPages", paged.totalPages);
                put("items", items);
            }});
        } catch (Exception ex) {
            log.error("/admin/api/products failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new java.util.LinkedHashMap<String,Object>() {{
                        put("error", "Server error fetching products");
                        put("message", ex.getClass().getSimpleName()+": "+ex.getMessage());
                    }});
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody java.util.Map<String,Object> body) {
        Product p = productService.getById(id);
        if (p == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("error", "Product not found"));
        try {
            if (body.containsKey("name")) {
                String name = asString(body.get("name"));
                if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
                p.setName(name.trim());
            }
            if (body.containsKey("description")) {
                String d = asString(body.get("description"));
                if (d != null && d.length() > MAX_DESCRIPTION_LEN) d = d.substring(0, MAX_DESCRIPTION_LEN);
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
            if (body.containsKey("categoryId")) {
                Long catId = toLong(body.get("categoryId"));
                if (catId != null) {
                    var cat = categoryService.getById(catId);
                    if (cat == null) throw new IllegalArgumentException("Category not found");
                    p.setCategory(cat);
                }
            }
            if (body.containsKey("stock")) {
                Integer stock = toInt(body.get("stock"));
                if (stock != null && stock >= 0) {
                    if (p.getVariants() != null && !p.getVariants().isEmpty()) {
                        int variants = p.getVariants().size();
                        int base = variants == 0 ? stock : stock / variants;
                        int remainder = variants == 0 ? 0 : stock % variants;
                        for (int i = 0; i < p.getVariants().size(); i++) {
                            ProductVariant v = p.getVariants().get(i);
                            int val = base + (i == 0 ? remainder : 0);
                            v.setStock(val);
                        }
                    } else { // create default variant
                        ProductVariant v = new ProductVariant();
                        v.setProduct(p);
                        v.setSizeLabel("DEFAULT");
                        v.setStock(stock);
                        List<ProductVariant> list = new ArrayList<>();
                        list.add(v);
                        p.setVariants(list);
                    }
                }
            }
            p.setUpdateDate(new Date());
            productService.saveOrUpdate(p);
            return ResponseEntity.ok(summary(p));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // Delete product endpoint
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        Product p = productService.getById(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found"));
        }
        try {
            productService.deleteById(id);
            return ResponseEntity.ok(Map.of("deleted", true, "id", id));
        } catch (Exception ex) {
            log.error("Failed to delete product id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete product", "message", ex.getMessage()));
        }
    }

    // ---------------- Helper Methods (re-added) ----------------
    private Map<String,Object> summary(Product p) {
        if (p == null) return fallbackSummary(null);
        LinkedHashMap<String,Object> m = new LinkedHashMap<>();
        String name = p.getName();
        if (name == null || name.isBlank()) name = "(Unnamed)";
        String description = p.getDescription();
        if (description != null && description.length() > 4000) description = description.substring(0,4000);
        String imageUrl = null;
        try { imageUrl = p.getImageUrl(); } catch (Exception ignored) {}
        BigDecimal displayPrice = (p.getSalePrice()!=null ? p.getSalePrice() : (p.getPrice()!=null? p.getPrice(): BigDecimal.ZERO));
        Integer totalStock = null;
        try { totalStock = p.getTotalStock(); } catch (Exception ignored) {}
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
        m.put("productStatus", p.getProductStatus() != null ? p.getProductStatus().name() : null); // enum name
        return m;
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

    private Map<String,Object> fallbackSummary(Product p){
        LinkedHashMap<String,Object> fm = new LinkedHashMap<>();
        fm.put("id", p!=null? p.getId(): null);
        fm.put("name", p!=null && p.getName()!=null? p.getName(): "(Unknown)");
        fm.put("error", "summary-failed");
        return fm;
    }

    private void validate(ProductCreateRequest r) {
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

    private String saveBase64Image(String dataUrl, Product product, ProductColor color, int index) throws IOException {
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
        String productDir = (product.getName() == null ? "product" : product.getName().trim());
        String folderPath = sanitize(color.getFolderPath() != null ? color.getFolderPath() : color.getColorName());
        String baseImage = sanitize(color.getBaseImage() != null ? color.getBaseImage() : (productDir + "-" + folderPath));
        String fname = baseImage + "-" + (index + 1) + ext;
        Path targetDir = Paths.get(FOLDER_UPLOAD).resolve(productDir).resolve(folderPath);
        Files.createDirectories(targetDir);
        byte[] bytes = Base64.getDecoder().decode(base64);
        Files.write(targetDir.resolve(fname), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return "/images/products/" + productDir + "/" + folderPath + "/" + fname;
    }

    private String sanitize(String in) { return in == null ? "n-a" : in.trim().replaceAll("[^a-zA-Z0-9_-]", "").toLowerCase(); }
    private ResponseEntity<Map<String,Object>> bad(String msg){ return ResponseEntity.badRequest().body(Map.of("error", msg)); }
    private String asString(Object o){ return o==null? null : o.toString(); }
    private Long toLong(Object o){ try { return o==null? null : Long.valueOf(o.toString()); } catch(Exception e){ return null; } }
    private Integer toInt(Object o){ try { return o==null? null : Integer.valueOf(o.toString()); } catch(Exception e){ return null; } }
    private BigDecimal toBigDecimal(Object o){ try { return o==null? null : new BigDecimal(o.toString()); } catch(Exception e){ return null; } }
}
