package vn.devpro.javaweb32.controller.administrator;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.common.constant.Jw32Contant;
import vn.devpro.javaweb32.dto.customer.product.ProductDto;
import vn.devpro.javaweb32.dto.administrator.ProductSearch;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;
import vn.devpro.javaweb32.service.administrator.CustomerAdminService;
import vn.devpro.javaweb32.service.administrator.ProductAdminService;
import vn.devpro.javaweb32.dto.customer.product.ProductDto;

@Controller
@RequestMapping("/admin/product")
public class ProductAdminController extends BaseController implements Jw32Contant {

    @Autowired
    private ProductAdminService productService;

    @Autowired
    private CustomerAdminService customerService;

    @Autowired
    private CategoryAdminService categoryService;

    // ========== LIST VIEW ==========
    @GetMapping({"/view", "/list"})
    public String viewProducts(Model model, HttpServletRequest request) {

        ProductSearch searchModel = productService.buildSearchModel(request);
        List<Product> products = productService.search(searchModel);

        searchModel.setTotalItems(products.size());
        searchModel.setItemOnPage(10);

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("searchModel", searchModel);

        return "administrator/product/product-list";
    }

    // ========== ADD ==========
    @GetMapping("/add")
    public String addProductForm(Model model) {
        vn.devpro.javaweb32.dto.customer.product.ProductDto dto = new vn.devpro.javaweb32.dto.customer.product.ProductDto();
        dto.setFavourites(true);
        dto.setPrice(null);

        model.addAttribute("productDto", dto);
        model.addAttribute("customers", customerService.findAdminUser());
        model.addAttribute("categories", categoryService.findAllActive());
        return "administrator/product/product-add";
    }



    @PostMapping("/add-save")
    public String saveNewProduct(@ModelAttribute("productDto") ProductDto productDto, BindingResult br, Model model) {
        if (productDto.getCategoryId() == null) {
            model.addAttribute("error", "You must choose category");
            model.addAttribute("categories", categoryService.findAllActive());
            return "administrator/product/product-add";
        }
        try {
            productService.saveProductFromDto(productDto);
            return "redirect:/admin/product/list";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("categories", categoryService.findAllActive());
            return "administrator/product/product-add";
        }
    }


    // ========== EDIT ==========
    @GetMapping("/edit/{productId}")
    public String editProductForm(@PathVariable Long productId, Model model) {
        Product product = productService.getById(productId);
        if (product == null) {
            return "redirect:/admin/product/list";
        }

        // map Product -> ProductDto (basic mapping)
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

        // colors -> ColorDto (keep existing preview filename)
        if (product.getColors() != null) {
            for (vn.devpro.javaweb32.entity.product.ProductColor pc : product.getColors()) {
                ProductDto.ColorDto cd = new vn.devpro.javaweb32.dto.customer.product.ProductDto.ColorDto();
                cd.setColorName(pc.getColorName());
                cd.setExistingPreviewFilename(pc.getPreviewImage());
                // images (multipart) left null because we only show existing preview
                dto.getColors().add(cd);
            }
        }

        // variants -> VariantDto
        if (product.getVariants() != null) {
            java.util.Map<Long,Integer> colorIndexMap = new java.util.HashMap<>();
            List<vn.devpro.javaweb32.entity.product.ProductColor> colorList = product.getColors();
            for (int i=0;i<colorList.size();i++) {
                if (colorList.get(i).getId() != null) colorIndexMap.put(colorList.get(i).getId(), i);
            }
            for (vn.devpro.javaweb32.entity.product.ProductVariant pv : product.getVariants()) {
                vn.devpro.javaweb32.dto.customer.product.ProductDto.VariantDto vd = new vn.devpro.javaweb32.dto.customer.product.ProductDto.VariantDto();
                vd.setSize(pv.getSize());
                vd.setPrice(pv.getPrice());
                vd.setStock(pv.getStock());
                if (pv.getColor() != null && pv.getColor().getId() != null && colorIndexMap.containsKey(pv.getColor().getId())) {
                    vd.setColorIndex(colorIndexMap.get(pv.getColor().getId()));
                } else {
                    vd.setColorIndex(0);
                }
                dto.getVariants().add(vd);
            }
        }

        model.addAttribute("productDto", dto);
        model.addAttribute("customers", customerService.findAdminUser());
        model.addAttribute("categories", categoryService.findAllActive());
        return "administrator/product/product-edit";
    }

    @PostMapping("/edit-save")
    public String saveEditedProduct(@ModelAttribute("productDto") ProductDto productDto, Model model) {
        try {
            productService.saveProductFromDto(productDto);
            return "redirect:/admin/product/list";
        } catch (Exception ex) {
            model.addAttribute("error", "Lỗi khi cập nhật sản phẩm: " + ex.getMessage());
            model.addAttribute("productDto", productDto);
            model.addAttribute("categories", categoryService.findAllActive());
            return "administrator/product/product-edit";
        }
    }

    // ========== DELETE ==========
    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        productService.deleteById(productId);
        return "redirect:/admin/product/list";
    }
}
