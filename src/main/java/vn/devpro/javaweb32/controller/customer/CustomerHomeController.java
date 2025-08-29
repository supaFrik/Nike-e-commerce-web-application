package vn.devpro.javaweb32.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.service.ProductService;

@Controller
public class CustomerHomeController extends BaseController {

	@Autowired
	private ProductService productService;

	// Cart page
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String cart() {
		return "customer/cart";
	}

	// Checkout page
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkout() {
		return "customer/checkout";
	}

    //Home
    @GetMapping({"/", "/index"})
    public String home(Model model) {
        List<Product> runningProducts = productService.getProductsByCategory("running");
        model.addAttribute("activeProducts", runningProducts);
        return "customer/index";
    }
}
