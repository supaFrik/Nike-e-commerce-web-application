package vn.devpro.javaweb32.controller.administrator;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.entity.product.Category;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController extends BaseController {

    private final CategoryAdminService categoryService;

    // View templates
    private static final String VIEW_LIST = "administrator/category/category-list";
    private static final String VIEW_ADD = "administrator/category/category-add";
    private static final String VIEW_EDIT = "administrator/category/category-edit";

    // Redirect targets
    private static final String REDIRECT_LIST = "redirect:/admin/category/list";
    private static final String REDIRECT_ADD = "redirect:/admin/category/add";

    public CategoryAdminController(CategoryAdminService categoryService) {
        this.categoryService = categoryService;
    }

    /* -------------------------------------------------- UTILITIES -------------------------------------------------- */
    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private String redirectEdit(Long id) { return "redirect:/admin/category/edit/" + id; }

    private void flashCategory(RedirectAttributes ra, Category category) {
        ra.addFlashAttribute("category", category);
    }

    private void flashError(RedirectAttributes ra, String message) {
        ra.addFlashAttribute("error", message);
    }

    private boolean nameExistsForOther(Category candidate) {
        if (candidate == null || isBlank(candidate.getName())) return false;
        Category existing = categoryService.findByName(candidate.getName());
        return existing != null && !existing.getId().equals(candidate.getId());
    }

    private void copyEditableFields(Category source, Category target) {
        target.setName(source.getName().trim());
        if (!isBlank(source.getStatus())) {
            target.setStatus(source.getStatus());
        }
    }

    /* -------------------------------------------------- LIST -------------------------------------------------- */
    @GetMapping({"/list", "/"})
    public String list(@RequestParam(value = "showAll", required = false, defaultValue = "false") boolean showAll,
                        @RequestParam(value = "message", required = false) String message,
                        @RequestParam(value = "error", required = false) String error,
                        Model model) {
        List<Category> categories = showAll ? categoryService.findAll() : categoryService.findActiveOrdered();
        model.addAttribute("categories", categories);
        model.addAttribute("showAll", showAll);
        if (message != null) model.addAttribute("message", message);
        if (error != null) model.addAttribute("error", error);
        return VIEW_LIST;
    }

    /* -------------------------------------------------- ADD -------------------------------------------------- */
    @GetMapping("/add")
    public String add(Model model, @RequestParam(value = "error", required = false) String error) {
        if (!model.containsAttribute("category")) { // preserve previously entered data on redirect
            Category category = new Category();
            category.setCreateDate(new Date());
            model.addAttribute("category", category);
        }
        if (error != null) model.addAttribute("error", error);
        return VIEW_ADD;
    }

    @PostMapping("/add-save")
    public String addSave(@ModelAttribute("category") Category category, RedirectAttributes ra) {
        if (isBlank(category.getName())) {
            flashError(ra, "Category name is required");
            flashCategory(ra, category);
            return REDIRECT_ADD;
        }
        if (categoryService.existsByName(category.getName())) {
            flashError(ra, "Category name already exists");
            flashCategory(ra, category);
            return REDIRECT_ADD;
        }
        category.setStatus(CategoryAdminService.STATUS_ACTIVE);
        categoryService.create(category);
        ra.addAttribute("message", "Category created successfully");
        return REDIRECT_LIST;
    }

    /* -------------------------------------------------- EDIT -------------------------------------------------- */
    @GetMapping("/edit/{categoryId}")
    public String viewEdit(@PathVariable("categoryId") Long categoryId,
                           @RequestParam(value = "error", required = false) String error,
                           Model model, RedirectAttributes ra) {
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            ra.addAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", category);
        }
        if (error != null) model.addAttribute("error", error);
        return VIEW_EDIT;
    }

    @PostMapping("/edit-save")
    public String editSave(@ModelAttribute("category") Category category, RedirectAttributes ra) {
        if (category.getId() == null) {
            ra.addAttribute("error", "Missing category ID");
            return REDIRECT_LIST;
        }
        Category existingDb = categoryService.getById(category.getId());
        if (existingDb == null) {
            ra.addAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        if (isBlank(category.getName())) {
            flashError(ra, "Category name is required");
            flashCategory(ra, category);
            return redirectEdit(category.getId());
        }
        if (nameExistsForOther(category)) {
            flashError(ra, "Category name already exists");
            flashCategory(ra, category);
            return redirectEdit(category.getId());
        }
        copyEditableFields(category, existingDb);
        categoryService.update(existingDb);
        ra.addAttribute("message", "Category updated successfully");
        return REDIRECT_LIST;
    }

    /* -------------------------------------------------- DELETE (SOFT) -------------------------------------------------- */
    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable("categoryId") Long categoryId, RedirectAttributes ra) {
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            ra.addAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        categoryService.softDelete(categoryId);
        ra.addAttribute("message", "Category deleted (inactivated) successfully");
        return REDIRECT_LIST;
    }

    /* -------------------------------------------------- ACTIVATE -------------------------------------------------- */
    @GetMapping("/activate/{categoryId}")
    public String activate(@PathVariable("categoryId") Long categoryId, RedirectAttributes ra) {
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            ra.addAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        category.setStatus(CategoryAdminService.STATUS_ACTIVE);
        categoryService.update(category);
        ra.addAttribute("message", "Category activated successfully");
        return REDIRECT_LIST;
    }
}

