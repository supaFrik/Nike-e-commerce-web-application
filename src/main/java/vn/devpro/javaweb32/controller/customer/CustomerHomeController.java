package vn.devpro.javaweb32.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.devpro.javaweb32.controller.BaseController;

@Controller
public class CustomerHomeController extends BaseController {

	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String home() {
		return "customer/index";
	}
	
	@RequestMapping(value = "/customer/index", method = RequestMethod.GET)
	public String customerHome() {
		return "customer/index";
	}
	
	@RequestMapping(value = "/customer/product-list", method = RequestMethod.GET)
	public String customerProductList() {
		return "customer/product-list";
	}
	
	@RequestMapping(value = "/customer/auth", method = RequestMethod.GET) 
	public String customerAuth() {
		return "customer/auth";
	}
	
	@RequestMapping(value = "/product-detail", method = RequestMethod.GET)
	public String productDetail() {
		return "customer/product-detail";
	}
	
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String customerCheckout() {
		return "customer/checkout";
	}
	
	@RequestMapping(value="/cart", method = RequestMethod.GET)
	public String customerCart() {
		return "customer/cart";
	}
	
	@RequestMapping(value="/order", method = RequestMethod.GET)
	public String customerOrder() {
		return "customer/order";
	}
	
	@RequestMapping(value="/terms", method = RequestMethod.GET)
	public String customerTerms() {
		return "customer/terms";
	}
}