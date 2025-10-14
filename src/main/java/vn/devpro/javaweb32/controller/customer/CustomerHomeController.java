package vn.devpro.javaweb32.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.dto.customer.product.ProductResponseDto;
import vn.devpro.javaweb32.entity.product.Category;
import vn.devpro.javaweb32.service.customer.CustomerProductService;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;

@Controller
public class CustomerHomeController extends BaseController {

    private static final String RUNNING_CATEGORY_NAME = "running"; // slug/name used to resolve running products

    @Autowired
    private CustomerProductService productService; // renamed to existing service

    @Autowired
    private CategoryAdminService categoryService;

    // Home
    @GetMapping({"/", "/index"})
    public String home(Model model) {
        List<ProductResponseDto> runningProducts = fetchRunningProducts();
        model.addAttribute("activeProducts", runningProducts);
        return "customer/index";
    }

    private List<ProductResponseDto> fetchRunningProducts() {
        Category running = categoryService.findByName(RUNNING_CATEGORY_NAME);
        if (running == null) {
            return Collections.emptyList();
        }
        return productService.getProductsByCategory(running.getId());
    }
}
