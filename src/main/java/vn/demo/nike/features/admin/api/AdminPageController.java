package vn.demo.nike.features.admin.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String dashboard() {
        return "administrator/dashboard";
    }

    @GetMapping("/admin/product/list")
    public String productList() {
        return "administrator/product/list";
    }

    @GetMapping("/admin/product/add")
    public String productAdd() {
        return "administrator/product/add";
    }

    @GetMapping("/admin/product/edit/{productId}")
    public String productEdit(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "administrator/product/edit";
    }

    @GetMapping("/admin/category/list")
    public String categoryList() {
        return "administrator/category/list";
    }

    @GetMapping("/admin/category/add")
    public String categoryAdd() {
        return "administrator/category/add";
    }

    @GetMapping("/admin/category/edit")
    public String categoryEdit() {
        return "administrator/category/edit";
    }

    @GetMapping("/admin/order/list")
    public String orderList() {
        return "administrator/order/list";
    }
}
