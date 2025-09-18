package vn.devpro.javaweb32.controller.administrator;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import vn.devpro.javaweb32.entity.product.ProductImage;

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
        // Xu ly cac thong tin lien quan den tim kiem
        productSearch.setStatus(2); // mac dinh la tim tat ca

        String str = request.getParameter("status");

        if(str != null && !StringUtils.isEmpty(str)) {
            productSearch.setStatus(Integer.parseInt(str));
        }

        productSearch.setCategoryId(0); // mac dinh la tim tat ca

        str = request.getParameter("categoryId");
        if(str != null && !StringUtils.isEmpty(str)) {
            productSearch.setCategoryId(Integer.parseInt(str));
        }

        productSearch.setKeyword(null);

        str = request.getParameter("keyword");
        if(str != null && !StringUtils.isEmpty(str)) {
            productSearch.setKeyword(str);
        }

        List<Product> products = ps.search(productSearch);

        productSearch.setTotalItems(products.size());
        productSearch.setCurrentPage(1);
        productSearch.setItemOnPage(10); // Set default items per page

        model.addAttribute("products", products);
        List<Category> categories = cs.findAllActive();
        model.addAttribute("categories", categories);
        model.addAttribute("searchModel", productSearch);
        return "administrator/product/product-list"; // chua them vao WEBPAGE
    }

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String viewProducts(final Model model, final HttpServletRequest request) {
        ProductSearch productSearch = new ProductSearch();
        // Xu ly cac thong tin lien quan den tim kiem
        productSearch.setStatus(2); // mac dinh la tim tat ca

        String str = request.getParameter("status");

        if(str != null && !StringUtils.isEmpty(str)) {
            productSearch.setStatus(Integer.parseInt(str));
        }

        productSearch.setCategoryId(0); // mac dinh la tim tat ca
        str = request.getParameter("categoryId");
        if(str != null && !StringUtils.isEmpty(str)) {
            productSearch.setCategoryId(Integer.parseInt(str));
        }
        System.out.println("Category ID: " + productSearch.getCategoryId());

        productSearch.setKeyword(null);
        str = request.getParameter("keyword");
        if(str != null && !StringUtils.isEmpty(str)) {
            productSearch.setKeyword(str);
        }

        productSearch.setBeginDate(null);
        productSearch.setEndDate(null);

        String beginDateStr = request.getParameter("beginDate");
        String endDateStr = request.getParameter("endDate");

        if(beginDateStr != null && !StringUtils.isEmpty(beginDateStr)
                && endDateStr != null && !StringUtils.isEmpty(endDateStr)) {
            productSearch.setBeginDate(beginDateStr);
            productSearch.setEndDate(endDateStr);
        }

        List<Product> products = ps.search(productSearch);

        // Initialize pagination values for the JSP template
        productSearch.setTotalItems(products.size());
        productSearch.setCurrentPage(1);
        productSearch.setItemOnPage(10); // Set default items per page

        model.addAttribute("products", products);
        List<Category> categories = cs.findAllActive();
        model.addAttribute("categories", categories);
        model.addAttribute("searchModel", productSearch);
        return "administrator/product/product-list"; // chua them vao WEBPAGE
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(final Model model) {
        Product product = new Product();
        List<Category> categories = cs.findAllActive();
        List<Customer> customers = us.findAdminUser();

        product.setCreateDate(new Date());

        model.addAttribute("product", product);
        model.addAttribute("customers", customers);
        model.addAttribute("categories", categories);
        return "administrator/product/product-add"; // chua them vao WEBPAGE
    }

    //Kiem tra file co duoc upload hay khong
    public boolean isUploadFile(MultipartFile file) {
        if(file != null && !StringUtils.isEmpty(file.getOriginalFilename())) {
            return true;
        }
        return false;
    }
    //Kiem tra danh sach file anh
    public boolean isUploadImages(MultipartFile[] images) {
        if(images != null && images.length > 0) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "add-save", method = RequestMethod.POST)
    public String save(
            @ModelAttribute("product") Product product,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            @RequestParam("imageFiles") MultipartFile[] imageFiles)
            throws IOException {

        // Xu ly file avatar -> cho vao thu muc UploadFiles/Product/Avatar va cho vao DB
        if(isUploadFile(avatarFile)) {
            // 1. Luu file vao thu muc
            String path = FOLDER_UPLOAD + "Product/Avatar/" +
                    avatarFile.getOriginalFilename();
            File file = new File(path);
            avatarFile.transferTo(file);

            // 2. Luu duong dan vao DB
            product.setAvatar("Product/Avatar/" + avatarFile.getOriginalFilename());
        }

        // Xu ly danh sach anh
        if(isUploadImages(imageFiles)) {
            for(MultipartFile image : imageFiles) {
                if(isUploadFile(image)) {
                    // 1. Luu file vao thu muc
                    String path = FOLDER_UPLOAD + "Product/Image/" +
                            image.getOriginalFilename();
                    File file = new File(path);
                    image.transferTo(file);

                    // 2. Luu duong dan vao DB
                    ProductImage pImage = new ProductImage();	// THIEU MODEL ProductImage
                    pImage.setCreateDate(new Date());
                    pImage.setUrl("Product/Image/" + image.getOriginalFilename());
                    pImage.setStatus("Available");

                    // Dung phuong thuc addRelation... cua Product de add image vao bang product_image
                    pImage.setProduct(product);
                    product.addRelationalProductImage(pImage);
                }
            }
        }
        ps.saveOrUpdate(product);

        return "redirect:/admin/product/add";
    }

    @RequestMapping(value = "edit/{productId}", method = RequestMethod.GET)
    public String edit(
            final Model model,
            @PathVariable("productId") Long productId) {

        // Lay product trong DB
        Product product = ps.getById(productId);
        List<Category> categories = cs.findAllActive();
        List<Customer> customers = us.findAdminUser();

        product.setUpdateDate(new Date());

        model.addAttribute("product", product);
        model.addAttribute("customers", customers);
        model.addAttribute("categories", categories);
        return "administrator/product/product-edit"; // chua them vao WEBPAGE
    }

    @RequestMapping(value = "edit-save", method = RequestMethod.POST)
    public String update(
            @ModelAttribute("product") Product product,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            @RequestParam("imageFiles") MultipartFile[] imageFiles)
            throws IOException {


        // Xu ly file avatar -> cho vao thu muc UploadFiles/Product/Avatar va cho vao DB
        if(isUploadFile(avatarFile)) {
            // 1. Xoa avatar cu (neu co)
            if(product.getAvatar() != null &&
                    !StringUtils.isEmpty(product.getAvatar())) {
                String path = FOLDER_UPLOAD + product.getAvatar();
                File file = new File(path);
                file.delete();
            }

            // 2. Them avatar moi
            String path = FOLDER_UPLOAD + "Product/Avatar/" + avatarFile.getOriginalFilename();
            File file = new File(path);
            avatarFile.transferTo(file);

            // Luu duong dan vao DB
            product.setAvatar("Product/Avatar/" + avatarFile.getOriginalFilename());
        }else {
            Product dbProduct = ps.getById(product.getId());
            product.setAvatar(dbProduct.getAvatar());
        }

        // Xu ly danh sach anh
        if(isUploadImages(imageFiles)) {
            for(MultipartFile image : imageFiles) {
                if(isUploadFile(image)) {
                    // 1. Luu file vao thu muc
                    String path = FOLDER_UPLOAD + "Product/Image/" +
                            image.getOriginalFilename();
                    File file = new File(path);
                    image.transferTo(file);

                    // 2. Luu duong dan vao DB
                    ProductImage pImage = new ProductImage();	// THIEU MODEL ProductImage
                    pImage.setCreateDate(new Date());
                    pImage.setUrl("Product/Image/" + image.getOriginalFilename());
                    pImage.setStatus("Available");
                    // Dung phuong thuc addRelation... cua Product de add image vao bang product_image
                    pImage.setProduct(product);
                    product.addRelationalProductImage(pImage);
                }
            }
        }
        ps.saveOrUpdate(product);

        return "redirect:/admin/product/list";
    }

    @RequestMapping (value = "delete/{productId} ", method = RequestMethod.GET)
    public String delete (
            @PathVariable ("productId") Long productId) {
        //Lay du lieu trong database
        //Lay category bang id

        Product product = ps.getById (productId) ;
        product.setStatus("Unavailable") ;

        ps.saveOrUpdate (product) ;

        return "redirect:/admin/product/list";
    }
}