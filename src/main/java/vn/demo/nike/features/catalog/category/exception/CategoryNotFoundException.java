package vn.demo.nike.features.catalog.category.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category with ID - " + id + " not found");
    }

    public CategoryNotFoundException(String name) {
        super("Category with name - " + name + " not found");
    }
}
