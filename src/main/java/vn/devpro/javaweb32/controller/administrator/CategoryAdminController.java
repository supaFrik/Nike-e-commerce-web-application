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

    private static final String VIEW_LIST = "administrator/category/category-list";
    private static final String VIEW_ADD = "administrator/category/category-add";

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
                        Model model) {
        List<Category> categories = showAll ? categoryService.findAll() : categoryService.findActiveOrdered();
        model.addAttribute("categories", categories);
        model.addAttribute("showAll", showAll);
        return VIEW_LIST;
    }

    /* -------------------------------------------------- ADD -------------------------------------------------- */
    @GetMapping("/add")
    public String add(Model model) {
        if (!model.containsAttribute("category")) {
            Category category = new Category();
            category.setCreateDate(new Date());
            model.addAttribute("category", category);
        }
        return VIEW_ADD;
    }

    /* -------------------------------------------------- SAVE (create or update) -------------------------------------------------- */
    @PostMapping("/save")
    public String save(@ModelAttribute("category") Category category, RedirectAttributes ra) {
        if (category == null) {
            ra.addFlashAttribute("error", "Invalid category data");
            return REDIRECT_LIST;
        }
        String name = category.getName();
        if (isBlank(name)) {
            flashError(ra, "Category name is required");
            flashCategory(ra, category);
            if (category.getId() == null) return REDIRECT_ADD;
            return redirectEdit(category.getId());
        }
        name = name.trim();
        category.setName(name);
        // If creating
        if (category.getId() == null) {
            if (categoryService.existsByName(name)) {
                flashError(ra, "Category name already exists");
                flashCategory(ra, category);
                return REDIRECT_ADD;
            }
            category.setStatus(CategoryAdminService.STATUS_ACTIVE);
            categoryService.create(category);
            ra.addFlashAttribute("message", "Category created successfully");
            return REDIRECT_LIST;
        }
        // Updating existing
        Category existingDb = categoryService.getById(category.getId());
        if (existingDb == null) {
            ra.addFlashAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        if (nameExistsForOther(category)) {
            flashError(ra, "Category name already exists");
            flashCategory(ra, category);
            return redirectEdit(category.getId());
        }
        copyEditableFields(category, existingDb);
        categoryService.update(existingDb);
        ra.addFlashAttribute("message", "Category updated successfully");
        return REDIRECT_LIST;
    }

    /* -------------------------------------------------- EDIT -------------------------------------------------- */
    @GetMapping("/edit/{categoryId}")
    public String viewEdit(@PathVariable("categoryId") Long categoryId,
                           Model model, RedirectAttributes ra) {
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            ra.addFlashAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", category);
        }
        return VIEW_ADD;
    }

    @PostMapping("/edit-save")
    public String editSave(@ModelAttribute("category") Category category, RedirectAttributes ra) {
        if (category.getId() == null) {
            ra.addFlashAttribute("error", "Missing category ID");
            return REDIRECT_LIST;
        }
        Category existingDb = categoryService.getById(category.getId());
        if (existingDb == null) {
            ra.addFlashAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        String name = category.getName();
        if (isBlank(name)) {
            flashError(ra, "Category name is required");
            flashCategory(ra, category);
            return redirectEdit(category.getId());
        }
        name = name.trim();
        category.setName(name);
        if (nameExistsForOther(category)) {
            flashError(ra, "Category name already exists");
            flashCategory(ra, category);
            return redirectEdit(category.getId());
        }
        copyEditableFields(category, existingDb);
        categoryService.update(existingDb);
        ra.addFlashAttribute("message", "Category updated successfully");
        return REDIRECT_LIST;
    }

    /* -------------------------------------------------- DELETE (SOFT) -------------------------------------------------- */
    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable("categoryId") Long categoryId, RedirectAttributes ra) {
        if (categoryService.getById(categoryId) == null) {
            ra.addFlashAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        categoryService.softDelete(categoryId);
        ra.addFlashAttribute("message", "Category deleted successfully");
        return REDIRECT_LIST;
    }

    /* -------------------------------------------------- ACTIVATE -------------------------------------------------- */
    @GetMapping("/activate/{categoryId}")
    public String activate(@PathVariable("categoryId") Long categoryId, RedirectAttributes ra) {
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            ra.addFlashAttribute("error", "Category not found");
            return REDIRECT_LIST;
        }
        category.setStatus(CategoryAdminService.STATUS_ACTIVE);
        categoryService.update(category);
        ra.addFlashAttribute("message", "Category activated successfully");
        return REDIRECT_LIST;
    }
}
