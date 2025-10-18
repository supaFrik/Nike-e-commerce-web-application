package vn.devpro.javaweb32.controller.administrator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.dto.administrator.ProductSearch;
import vn.devpro.javaweb32.dto.customer.product.ProductDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;
import vn.devpro.javaweb32.service.administrator.CustomerAdminService;
import vn.devpro.javaweb32.service.administrator.AdminProductService;

/**
 * Controller quản lý product (admin).
 * - Giữ controller nhẹ: map request <-> DTO và gọi AdminProductService xử lý business + file.
 * - Không chứa logic lưu file hay rename.
 */
@Controller
@RequestMapping("/admin/product")
public class ProductAdminController extends BaseController {

    private static final int DEFAULT_PAGE_SIZE = 6; // trang quản trị đang hiển thị 6 item / page

    private final AdminProductService productService;
    private final CustomerAdminService customerService;
    private final CategoryAdminService categoryService;

    @Autowired
    public ProductAdminController(AdminProductService productService,
                                  CustomerAdminService customerService,
                                  CategoryAdminService categoryService) {
        this.productService = productService;
        this.customerService = customerService;
        this.categoryService = categoryService;
    }

    // -------------------------
    // Helpers
    // -------------------------
    private String resolveStatus(String statusParam) {
        if (statusParam == null || statusParam.isEmpty()) return ProductStatus.ACTIVE.name();
        try {
            return switch (Integer.parseInt(statusParam)) {
                case 0 -> ProductStatus.ACTIVE.name();
                case 1 -> ProductStatus.FEW_LEFT.name();
                case 2 -> ProductStatus.OUT_OF_STOCK.name();
                case 3 -> ProductStatus.DISCONTINUED.name();
                default -> ProductStatus.ACTIVE.name();
            };
        } catch (NumberFormatException ex) {
            try { return ProductStatus.valueOf(statusParam.toUpperCase().replace(' ', '_')).name(); }
            catch (IllegalArgumentException iae) { return ProductStatus.ACTIVE.name(); }
        }
    }

    private String toUiStatus(ProductStatus status) {
        if (status == null) return "Active";
        return switch (status) {
            case ACTIVE -> "Active";
            case DRAFT -> "Draft";
            case FEW_LEFT -> "Few Left";
            case OUT_OF_STOCK -> "Out Of Stock";
            case DISCONTINUED -> "Discontinued";
        };
    }

    private void prepareModelLists(Model model) {
        model.addAttribute("customers", customerService.findAdminUser());
        model.addAttribute("categories", categoryService.findAllActive());
    }

    private ProductDto mapProductToDto(Product product) {
        if (product == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setDescription(product.getDescription());
        dto.setType(product.getType());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setSeo(product.getSeo());
        dto.setStatus(toUiStatus(product.getProductStatus()));

        if (product.getColors() != null) {
            for (ProductColor pc : product.getColors()) {
                ProductDto.ColorDto cd = new ProductDto.ColorDto();
                cd.setColorName(pc.getColorName());
                cd.setHexCode(pc.getHexCode());
                dto.getColors().add(cd);
            }
        }
        if (product.getVariants() != null && product.getColors() != null) {
            Map<Long, Integer> colorIndexMap = new HashMap<>();
            List<ProductColor> colorList = product.getColors();
            for (int i = 0; i < colorList.size(); i++) {
                if (colorList.get(i).getId() != null) colorIndexMap.put(colorList.get(i).getId(), i);
            }
            for (ProductVariant pv : product.getVariants()) {
                ProductDto.VariantDto vd = new ProductDto.VariantDto();
                vd.setSize(pv.getSizeLabel());
                vd.setStock(pv.getStock());
                vd.setColorIndex(pv.getColor() != null && pv.getColor().getId() != null ?
                        colorIndexMap.getOrDefault(pv.getColor().getId(), 0) : 0);
                dto.getVariants().add(vd);
            }
        }
        return dto;
    }

    // ------------------------- LIST / VIEW -------------------------
    @GetMapping({"/view", "/list"})
    public String viewProducts(@ModelAttribute ProductSearch search,
                               @RequestParam(value = "status", required = false) String statusParam,
                               Model model) {
        if (search == null) search = new ProductSearch();
        if (statusParam != null) search.setStatus(resolveStatus(statusParam));
        else if (search.getStatus() == null) search.setStatus(ProductStatus.ACTIVE.name());
        search.setItemOnPage(DEFAULT_PAGE_SIZE);
        List<Product> products = productService.search(search);
        if (products == null) products = new ArrayList<>();
        search.setTotalItems(products.size());
        model.addAttribute("products", products);
        model.addAttribute("searchModel", search);
        model.addAttribute("categories", categoryService.findAllActive());
        return "administrator/product/product-list";
    }

    // ------------------------- ADD -------------------------
    @GetMapping("/add")
    public String addProductForm(Model model) {
        if (!model.containsAttribute("productDto")) {
            ProductDto dto = new ProductDto();
            dto.setPrice(BigDecimal.ZERO);
            dto.setSalePrice(BigDecimal.ZERO);
            dto.setColors(new ArrayList<>());
            dto.setVariants(new ArrayList<>());
            model.addAttribute("productDto", dto);
        }
        prepareModelLists(model);
        return "administrator/product/product-add";
    }

    @PostMapping("/add-save")
    public String saveNewProduct(@ModelAttribute("productDto") ProductDto productDto,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            productService.saveProductFromDto(productDto);
            redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công");
            return "redirect:/admin/product/list";
        } catch (Exception ex) {
            model.addAttribute("error", "Lỗi khi lưu sản phẩm: " + ex.getMessage());
            prepareModelLists(model);
            return "administrator/product/product-add";
        }
    }

    // ------------------------- EDIT -------------------------
    @GetMapping("/edit/{productId}")
    public String editProductForm(@PathVariable("productId") Long productId, Model model) {
        Product product = productService.getById(productId);
        if (product == null) return "redirect:/admin/product/list";
        model.addAttribute("productDto", mapProductToDto(product));
        model.addAttribute("product", product);
        prepareModelLists(model);
        return "administrator/product/product-edit";
    }

    @GetMapping("/product-edit")
    public String blankProductEdit(Model model) {
        prepareModelLists(model);
        return "administrator/product/product-edit";
    }

    @PostMapping("/edit-save")
    public String saveEditedProduct(@ModelAttribute("productDto") ProductDto productDto,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        try {
            productService.saveProductFromDto(productDto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công");
            return "redirect:/admin/product/list";
        } catch (Exception ex) {
            model.addAttribute("error", "Lỗi khi cập nhật sản phẩm: " + ex.getMessage());
            prepareModelLists(model);
            return "administrator/product/product-edit";
        }
    }

    // ------------------------- DELETE -------------------------
    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId, RedirectAttributes redirectAttributes) {
        try { productService.deleteById(productId); }
        catch (Exception ex) { redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm: " + ex.getMessage()); }
        return "redirect:/admin/product/list";
    }
}
