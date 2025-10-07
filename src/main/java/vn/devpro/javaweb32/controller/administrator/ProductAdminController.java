package vn.devpro.javaweb32.controller.administrator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.common.constant.Jw32Contant;
import vn.devpro.javaweb32.dto.administrator.ProductSearch;
import vn.devpro.javaweb32.dto.customer.product.ProductDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;
import vn.devpro.javaweb32.service.administrator.CustomerAdminService;
import vn.devpro.javaweb32.service.administrator.ProductAdminService;

/**
 * Controller quản lý product (admin).
 * - Giữ controller nhẹ: map request <-> DTO và gọi ProductAdminService xử lý business + file.
 * - Không chứa logic lưu file hay rename.
 */
@Controller
@RequestMapping("/admin/product")
public class ProductAdminController extends BaseController implements Jw32Contant {

    @Autowired
    private ProductAdminService productService;

    @Autowired
    private CustomerAdminService customerService;

    @Autowired
    private CategoryAdminService categoryService;

    // -------------------------
    // Helpers
    // -------------------------
    private ProductSearch buildSearchModelFromRequest(HttpServletRequest request) {
        ProductSearch search = new ProductSearch();

        // default
        search.setStatus("In Order");

        String statusParam = request.getParameter("status");
        if (statusParam != null && !statusParam.isEmpty()) {
            // nếu client truyền số (0..5) theo UI cũ, map sang string
            try {
                int s = Integer.parseInt(statusParam);
                switch (s) {
                    case 0: search.setStatus("In Order"); break;
                    case 1: search.setStatus("Bestseller"); break;
                    case 2: search.setStatus("Out Of Stock"); break;
                    case 3: search.setStatus("On Sale"); break;
                    case 4: search.setStatus("Limited"); break;
                    case 5: search.setStatus("Just In"); break;
                    default: search.setStatus("In Order");
                }
            } catch (NumberFormatException nfe) {
                // nếu không phải số, dùng nguyên value
                search.setStatus(statusParam);
            }
        }

        // categoryId
        search.setCategoryId(0);
        String cat = request.getParameter("categoryId");
        if (cat != null && !cat.isEmpty()) {
            try {
                search.setCategoryId(Integer.parseInt(cat));
            } catch (NumberFormatException ignored) {}
        }

        // keyword
        String kw = request.getParameter("keyword");
        if (kw != null && !kw.isEmpty()) search.setKeyword(kw);

        // paging
        String cp = request.getParameter("currentPage");
        if (cp != null && !cp.isEmpty()) {
            try { search.setCurrentPage(Integer.parseInt(cp)); } catch (NumberFormatException ignored) {}
        }

        // dates (optional)
        // nếu DTO ProductSearch của bạn có beginDate/endDate kiểu Date, map tương ứng ở đây.
        return search;
    }

    private void prepareModelLists(Model model) {
        model.addAttribute("customers", customerService.findAdminUser());
        model.addAttribute("categories", categoryService.findAllActive());
    }

    // -------------------------
    // LIST / VIEW
    // -------------------------
    @GetMapping({"/view", "/list"})
    public String viewProducts(Model model, HttpServletRequest request) {
        ProductSearch search = buildSearchModelFromRequest(request);
        List<Product> products = productService.search(search);

        search.setTotalItems(products != null ? products.size() : 0);
        search.setItemOnPage(10);

        model.addAttribute("products", products);
        model.addAttribute("searchModel", search);
        model.addAttribute("categories", categoryService.findAllActive());
        return "administrator/product/product-list";
    }

    // -------------------------
    // ADD
    // -------------------------
    @GetMapping("/add")
    public String addProductForm(Model model) {
        // nếu view sử dụng modelAttribute="productDto" (JSP), cần đảm bảo tồn tại attribute này
        ProductDto dto = new ProductDto();
        dto.setFavourites(true);
        dto.setPrice(BigDecimal.ZERO);
        dto.setSalePrice(BigDecimal.ZERO);
        dto.setColors(new ArrayList<>());
        dto.setVariants(new ArrayList<>());

        model.addAttribute("productDto", dto);
        prepareModelLists(model);
        return "administrator/product/product-add";
    }

    @PostMapping("/add-save")
    public String saveNewProduct(@ModelAttribute("productDto") ProductDto productDto,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            // service phải validate categoryId; ném exception nếu thiếu
            Product saved = productService.saveProductFromDto(productDto);
            redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công");
            return "redirect:/admin/product/list";
        } catch (Exception ex) {
            // lỗi: trả lại form kèm error message và giữ productDto để render form lại
            model.addAttribute("error", "Lỗi khi lưu sản phẩm: " + ex.getMessage());
            model.addAttribute("productDto", productDto);
            prepareModelLists(model);
            return "administrator/product/product-add";
        }
    }

    // -------------------------
    // EDIT
    // -------------------------
    @GetMapping("/edit/{productId}")
    public String editProductForm(@PathVariable("productId") Long productId, Model model) {
        Product product = productService.getById(productId);
        if (product == null) {
            return "redirect:/admin/product/list";
        }

        // Map Product entity -> ProductDto (basic mapping)
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setDescription(product.getDescription());
        dto.setType(product.getType());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setSeo(product.getSeo());
        dto.setStatus(product.getStatus());
        dto.setFavourites(product.getFavourites());

        // Colors mapping: giữ preview filename nếu có
        if (product.getColors() != null) {
            for (ProductColor pc : product.getColors()) {
                ProductDto.ColorDto cd = new ProductDto.ColorDto();
                cd.setColorName(pc.getColorName());
                // existing preview path (relative) để front-end hiển thị
                cd.setExistingPreviewFilename(pc.getPreviewImage());
                // images (multipart) để null — khi edit front-end chỉ show preview, upload mới điền vào DTO khi submit
                dto.getColors().add(cd);
            }
        }

        // Variants mapping
        if (product.getVariants() != null) {
            // cần map color id -> index để variant biết colorIndex
            Map<Long, Integer> colorIndexMap = new HashMap<>();
            List<ProductColor> colorList = product.getColors() != null ? product.getColors() : new ArrayList<>();
            for (int i = 0; i < colorList.size(); i++) {
                if (colorList.get(i).getId() != null) {
                    colorIndexMap.put(colorList.get(i).getId(), i);
                }
            }
            for (ProductVariant pv : product.getVariants()) {
                ProductDto.VariantDto vd = new ProductDto.VariantDto();
                vd.setSize(pv.getSize());
                vd.setPrice(pv.getPrice());
                vd.setStock(pv.getStock());
                if (pv.getColor() != null && pv.getColor().getId() != null && colorIndexMap.containsKey(pv.getColor().getId())) {
                    vd.setColorIndex(colorIndexMap.get(pv.getColor().getId()));
                } else {
                    vd.setColorIndex(0); // fallback
                }
                dto.getVariants().add(vd);
            }
        }

        model.addAttribute("productDto", dto);
        prepareModelLists(model);
        return "administrator/product/product-edit";
    }

    @PostMapping("/edit-save")
    public String saveEditedProduct(@ModelAttribute("productDto") ProductDto productDto,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            Product saved = productService.saveProductFromDto(productDto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công");
            return "redirect:/admin/product/list";
        } catch (Exception ex) {
            model.addAttribute("error", "Lỗi khi cập nhật sản phẩm: " + ex.getMessage());
            model.addAttribute("productDto", productDto);
            prepareModelLists(model);
            return "administrator/product/product-edit";
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(productId);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm: " + ex.getMessage());
        }
        return "redirect:/admin/product/list";
    }
}
