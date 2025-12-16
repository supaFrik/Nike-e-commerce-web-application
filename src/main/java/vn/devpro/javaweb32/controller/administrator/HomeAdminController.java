package vn.devpro.javaweb32.controller.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;

/**
 * Controller for Administrator Home/Dashboard
 */
@Controller
@RequestMapping("/admin")
public class HomeAdminController extends BaseController {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Display the admin dashboard home page with best-selling products
     */
    @GetMapping(value = {"", "/", "/home"})
    public String home(Model model) {
        Long categoryId = 3L;

        List<Product> bestSellingProducts = productRepository.findByCategory_IdAndProductStatus(
            categoryId,
            ProductStatus.ACTIVE,
            Sort.by(Sort.Direction.DESC, "createDate")
        );

        // Limit to 8 products for the carousel
        if (bestSellingProducts.size() > 8) {
            bestSellingProducts = bestSellingProducts.subList(0, 8);
        }

        model.addAttribute("bestSellingProducts", bestSellingProducts);


        return "administrator/home";
    }
}

