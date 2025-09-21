package vn.devpro.javaweb32.controller.administrator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.common.constant.Jw32Contant;
import vn.devpro.javaweb32.dto.administrator.ProductSearch;
import vn.devpro.javaweb32.entity.product.Category;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;
import vn.devpro.javaweb32.service.administrator.ProductAdminService;
import vn.devpro.javaweb32.service.administrator.CustomerAdminService;

@Controller
@RequestMapping("/admin/product/")
public class ProductAdminController extends BaseController implements Jw32Contant{

    @Autowired
    ProductAdminService ps;

    @Autowired
    CustomerAdminService us;

    @Autowired
    CategoryAdminService cs;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String view(final Model model, final HttpServletRequest request) {
        ProductSearch productSearch = new ProductSearch();
        productSearch.setStatus(2); // default - search all

        String str = request.getParameter("status");
        if(str != null && !str.isEmpty()) {
            productSearch.setStatus(Integer.parseInt(str));
        }

        productSearch.setCategoryId(0); // default - search all
        str = request.getParameter("categoryId");
        if(str != null && !str.isEmpty()) {
            productSearch.setCategoryId(Integer.parseInt(str));
        }

        productSearch.setKeyword(null);
        str = request.getParameter("keyword");
        if(str != null && !str.isEmpty()) {
            productSearch.setKeyword(str);
        }

        List<Product> products = ps.search(productSearch);
        productSearch.setTotalItems(products.size());
        productSearch.setCurrentPage(1);
        productSearch.setItemOnPage(10);

        model.addAttribute("products", products);
        List<Category> categories = cs.findAllActive();
        model.addAttribute("categories", categories);
        model.addAttribute("searchModel", productSearch);
        return "administrator/product/product-list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(final Model model) {
        Product product = new Product();
        List<Category> categories = cs.findAllActive();
        List<Customer> customers = us.findAdminUser();

        product.setCreateDate(new Date());
        product.setUpdateDate(new Date());
        if(!product.getFavourites()) {
            product.setFavourites(true);
        }
        model.addAttribute("product", product);
        model.addAttribute("customers", customers);
        model.addAttribute("categories", categories);
        return "administrator/product/product-add";
    }

    // Helper methods
    public boolean isUploadFile(MultipartFile file) {
        return file != null && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty();
    }

    public boolean isUploadImages(MultipartFile[] images) {
        if(images != null) {
            for(MultipartFile image : images) {
                if(isUploadFile(image)) {
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping(value = "add-save", method = RequestMethod.POST)
    public String save(
            @ModelAttribute("product") Product product,
            @RequestParam("avatarFile") MultipartFile mainImage,
            @RequestParam(value = "colorNames", required = false) List<String> colorNames,
            @RequestParam(value = "colorBaseImages", required = false) MultipartFile[] colorBaseImages,
            @RequestParam(value = "variantSizes", required = false) List<String> variantSizes,
            @RequestParam(value = "variantPrices", required = false) List<String> variantPrices,
            @RequestParam(value = "variantStocks", required = false) List<Integer> variantStocks,
            @RequestParam(value = "variantColorIndex", required = false) List<Integer> variantColorIndex)
            throws IOException {

        // Initialize null lists
        if(colorNames == null) colorNames = new ArrayList<>();
        if(variantSizes == null) variantSizes = new ArrayList<>();
        if(variantPrices == null) variantPrices = new ArrayList<>();
        if(variantColorIndex == null) variantColorIndex = new ArrayList<>();
        if(variantStocks == null) variantStocks = new ArrayList<>();

        // Set dates
        product.setCreateDate(new Date());
        product.setUpdateDate(new Date());

        // Handle main product image (avatar) - for product list preview
        if(isUploadFile(mainImage)) {
            String safeName = mainImage.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
            String path = FOLDER_UPLOAD + "Product/" + product.getName() + "/Main/" + safeName;
            File file = new File(path);
            if(file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            mainImage.transferTo(file);
            product.setAvatar("Product/" + product.getName() + "/Main/" + safeName);
        }

        // Save product first to get the ID
        ps.saveOrUpdate(product);

        // ===== COLOR ROW HANDLING =====
        // Process color variants - each color's base image is its primary image
        List<ProductColor> productColors = processColorVariants(product, colorNames, colorBaseImages);

        // ===== PRODUCT VARIANT HANDLING =====
        if(!variantSizes.isEmpty() && !variantPrices.isEmpty() && !variantStocks.isEmpty() && !variantColorIndex.isEmpty()) {
            processProductVariants(product, productColors, variantSizes, variantPrices, variantStocks, variantColorIndex);
        }

        // Final save with all relations
        ps.saveOrUpdate(product);
        return "redirect:/admin/product/list";
    }

    @RequestMapping(value = "edit/{productId}", method = RequestMethod.GET)
    public String edit(final Model model, @PathVariable("productId") Long productId) {
        Product product = ps.getById(productId);
        List<Category> categories = cs.findAllActive();
        List<Customer> customers = us.findAdminUser();

        model.addAttribute("product", product);
        model.addAttribute("customers", customers);
        model.addAttribute("categories", categories);
        return "administrator/product/product-edit";
    }

    @RequestMapping(value = "edit-save", method = RequestMethod.POST)
    public String editSave(
            @ModelAttribute("product") Product product,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            @RequestParam(value = "colorNames", required = false) List<String> colorNames,
            @RequestParam(value = "colorBaseImages", required = false) MultipartFile[] colorBaseImages,
            @RequestParam(value = "variantSizes", required = false) List<String> variantSizes,
            @RequestParam(value = "variantPrices", required = false) List<String> variantPrices,
            @RequestParam(value = "variantStocks", required = false) List<Integer> variantStocks,
            @RequestParam(value = "variantColorIndex", required = false) List<Integer> variantColorIndex)
            throws IOException {

        product.setUpdateDate(new Date());

        // Handle main avatar update (for product list preview)
        if(isUploadFile(avatarFile)) {
            String safeName = avatarFile.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
            String path = FOLDER_UPLOAD + "Product/" + product.getName() + "/Main/" + safeName;
            File file = new File(path);
            if(file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            avatarFile.transferTo(file);
            product.setAvatar("Product/" + product.getName() + "/Main/" + safeName);
        }

        // Update color variants if provided - each color's base image serves as its primary image
        if(colorNames != null && !colorNames.isEmpty()) {
            product.getColors().clear();
            product.getVariants().clear();

            List<ProductColor> productColors = processColorVariants(product, colorNames, colorBaseImages);

            if(variantSizes != null && !variantSizes.isEmpty()) {
                processProductVariants(product, productColors, variantSizes, variantPrices, variantStocks, variantColorIndex);
            }
        }

        // Final save with updated relations
        ps.saveOrUpdate(product);
        return "redirect:/admin/product/list";
    }

    @RequestMapping(value = "delete/{productId}", method = RequestMethod.GET)
    public String delete(@PathVariable("productId") Long productId) {
        Product product = ps.getById(productId);
        if(product != null) {
            ps.delete(product);
        }
        return "redirect:/admin/product/list";
    }

    // ===== HELPER METHODS FOR COLOR ROW HANDLING =====

    /**
     * Process color variants and create ProductColor entities
     * Each color's base image serves as its primary image (e.g., air max dn8-1, air max dn8-2)
     */
    private List<ProductColor> processColorVariants(Product product, List<String> colorNames, MultipartFile[] colorBaseImages) throws IOException {
        List<ProductColor> productColors = new ArrayList<>();

        if(colorNames != null && !colorNames.isEmpty()) {
            for(int i = 0; i < colorNames.size(); i++) {
                String colorName = colorNames.get(i);
                if(colorName != null && !colorName.trim().isEmpty()) {
                    ProductColor productColor = new ProductColor();
                    productColor.setColorName(colorName.trim());
                    productColor.setProduct(product);

                    // Set folder path for this color
                    String colorFolderName = colorName.replaceAll("[^a-zA-Z0-9]", "_");
                    productColor.setFolderPath(product.getName() + "/colors/" + colorFolderName);

                    // Handle color base image if provided - this serves as the primary image for this color
                    if(colorBaseImages != null && i < colorBaseImages.length && isUploadFile(colorBaseImages[i])) {
                        String baseImageName = saveColorBaseImage(product.getName(), colorFolderName, colorBaseImages[i]);
                        productColor.setBaseImage(baseImageName);
                    } else {
                        productColor.setBaseImage("default_color.jpg");
                    }

                    productColors.add(productColor);
                    product.getColors().add(productColor);
                }
            }
        }

        return productColors;
    }

    /**
     * Process product variants (size/color combinations)
     */
    private void processProductVariants(Product product, List<ProductColor> productColors,
            List<String> variantSizes, List<String> variantPrices,
            List<Integer> variantStocks, List<Integer> variantColorIndex) {

        if(variantSizes == null) variantSizes = new ArrayList<>();
        if(variantPrices == null) variantPrices = new ArrayList<>();
        if(variantStocks == null) variantStocks = new ArrayList<>();
        if(variantColorIndex == null) variantColorIndex = new ArrayList<>();

        for(int i = 0; i < variantSizes.size(); i++) {
            if(i < variantPrices.size() && i < variantStocks.size() && i < variantColorIndex.size()) {
                ProductVariant variant = new ProductVariant();
                variant.setProduct(product);
                variant.setSize(variantSizes.get(i));

                // Parse and set price
                try {
                    variant.setPrice(new java.math.BigDecimal(variantPrices.get(i)));
                } catch(NumberFormatException e) {
                    variant.setPrice(product.getPrice()); // fallback to product price
                }

                variant.setStock(variantStocks.get(i));

                // Link to ProductColor
                int colorIndex = variantColorIndex.get(i);
                if(colorIndex >= 0 && colorIndex < productColors.size()) {
                    ProductColor selectedColor = productColors.get(colorIndex);
                    variant.setColor(selectedColor);
                    variant.setColorName(selectedColor.getColorName());
                }

                product.getVariants().add(variant);
            }
        }
    }

    /**
     * Save color base image and return the filename
     * Each color's base image is stored in its own folder (e.g., air max dn8-1.jpg, air max dn8-2.jpg)
     */
    private String saveColorBaseImage(String productName, String colorFolderName, MultipartFile baseImage) throws IOException {
        String safeImageName = baseImage.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
        String colorImagePath = FOLDER_UPLOAD + productName + "/colors/" + colorFolderName + "/" + safeImageName;
        File colorImageFile = new File(colorImagePath);
        if(colorImageFile.getParentFile() != null) {
            colorImageFile.getParentFile().mkdirs();
        }
        baseImage.transferTo(colorImageFile);
        return safeImageName;
    }

    /**
     * Get available colors for a product (for dropdown/selection purposes)
     */
    public List<ProductColor> getAvailableColors(Long productId) {
        Product product = ps.getById(productId);
        return product != null ? product.getColors() : new ArrayList<>();
    }

    /**
     * Get variants by color for a product
     */
    public List<ProductVariant> getVariantsByColor(Long productId, Long colorId) {
        Product product = ps.getById(productId);
        if(product == null) return new ArrayList<>();

        return product.getVariants().stream()
            .filter(variant -> variant.getColor() != null && variant.getColor().getId().equals(colorId))
            .collect(java.util.stream.Collectors.toList());
    }
}
