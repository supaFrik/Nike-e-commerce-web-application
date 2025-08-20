package vn.devpro.javaweb32.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.devpro.javaweb32.controller.BaseController;

@Controller
public class CustomerHomeController extends BaseController {

	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/customer/index", method = RequestMethod.GET)
	public String customerHome() {
		return "index";
	}

	// Cart page
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String cart() {
		return "customer/cart";
	}

	// Product detail page
	@RequestMapping(value = "/product-detail", method = RequestMethod.GET)
	public String productDetail() {
		return "customer/product-detail";
	}

	// Authentication page
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public String auth() {
		return "customer/auth";
	}

	// Checkout page
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkout() {
		return "customer/checkout";
	}
}
