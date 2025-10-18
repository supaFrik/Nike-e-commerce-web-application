package vn.devpro.javaweb32.controller;

import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

public class BaseController {

    protected int normalizePage(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    protected int normalizePageSize(Integer pageSize, int defaultSize, int maxSize) {
        int size = (pageSize == null || pageSize < 1) ? defaultSize : pageSize;
        return Math.min(size, maxSize);
    }

    protected <T> List<T> paginate(List<T> items, int page, int pageSize, Model model) {
        if (items == null || items.isEmpty()) {
            model.addAttribute("totalProducts", 0);
            model.addAttribute("page", 1);
            model.addAttribute("totalPages", 1);
            model.addAttribute("pageSize", pageSize);
            return Collections.emptyList();
        }
        int total = items.size();
        int totalPages = (int) Math.ceil(total / (double) pageSize);
        if (totalPages == 0) totalPages = 1;
        int currentPage = Math.min(Math.max(1, page), totalPages);
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<T> sub = fromIndex >= total ? Collections.emptyList() : items.subList(fromIndex, toIndex);
        model.addAttribute("totalProducts", total);
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        return sub;
    }
}
