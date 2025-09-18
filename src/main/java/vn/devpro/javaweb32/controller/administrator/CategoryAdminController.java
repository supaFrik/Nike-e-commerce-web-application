//package vn.devpro.javaweb32.controller.administrator;
//
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import vn.devpro.javaweb32.controller.BaseController;
//import vn.devpro.javaweb32.entity.Category;
//import vn.devpro.javaweb32.service.administrator.CategoryService;
//
//@Controller
//@RequestMapping("/admin/category/")
//public class CategoryAdminController extends BaseController{
//
//	@Autowired
//    CategoryService cs;
//	// Thieu CustomerAdminService
//
//	@RequestMapping(value = "list", method = RequestMethod.GET)
//	public String list(Model model) {
//		List<Category> categories = cs.findAll();	// chua sua loi BaseService // CAP NHAT: Da sua
//		model.addAttribute("categories", categories);
//		return "administrator/category/category-list";
//	}
//
//	@RequestMapping(value = "add", method = RequestMethod.GET)
//	public String add(Model model) {
//		Category category = new Category();
//		category.setCreateDate(new Date());
//
//		model.addAttribute("category", category);
//		return "administrator/category/category-add";
//	}
//
//	@RequestMapping(value = "add-save", method = RequestMethod.POST)
//	public String addSave(@ModelAttribute("category") Category category) {
//		category.setCreateDate(new Date());
//		category.setStatus(Boolean.TRUE);
//
//		cs.saveOrUpdate(category);
//
//		return "redirect:/admin/category/list";
//	}
//
//	@RequestMapping(value = "edit/{categoryId}", method = RequestMethod.GET)
//	public String viewEdit(
//			@PathVariable("categoryId") int categoryId,
//			Model model) {
//		// Lay du lieu trong DB
//		// Lay category bang id
//		Category category = cs.getById(categoryId);
//		model.addAttribute("category", category);
//
//		return "administrator/category/category-edit";
//	}
//
//	@RequestMapping(value = "edit-save", method = RequestMethod.POST)
//	public String editSave(@ModelAttribute("category") Category category) {
//		category.setUpdateDate(new Date());
//
//		cs.saveOrUpdate(category);
//
//		return "redirect:/admin/category/list";
//	}
//
//	@RequestMapping(value = "delete/{categoryId}", method = RequestMethod.GET)
//	public String delete(@PathVariable("categoryId") int categoryId) {
//		Category category = cs.getById(categoryId);
//		category.setStatus(Boolean.FALSE);
//
//		cs.saveOrUpdate(category);
//
//		return "redirect:/admin/category/list";
//	}
//}