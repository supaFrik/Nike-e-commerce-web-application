package vn.devpro.javaweb32.controller.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.devpro.javaweb32.controller.BaseController;


@Controller
public class HomeAdminController extends BaseController {
	
	@RequestMapping(value = "/admin/home")
	public String list(Model model) {
		return "administrator/home";
	}
}
