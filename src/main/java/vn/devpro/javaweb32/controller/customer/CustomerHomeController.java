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
}
