package vn.demo.nike.features.catalog.category.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category with ID - " + id + " not found");
    }
}
