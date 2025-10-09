package vn.devpro.javaweb32.service.administrator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.devpro.javaweb32.common.base.BaseService;
import vn.devpro.javaweb32.entity.product.Category;

import java.util.Date;
import java.util.List;

@Service
public class CategoryAdminService extends BaseService<Category> {

    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_INACTIVE = "Inactive";

    @Override
    public Class<Category> clazz() {
        return Category.class;
    }

    /**
     * Find all product
     */
    public List<Category> findAllActive() {
        return findActiveOrdered();
    }

    /**
     * Find a category by exact (case-insensitive) name.
     */
    public Category findByName(String name) {
        if (name == null) return null;
        String jpql = "SELECT c FROM Category c WHERE lower(c.name) = :name";
        List<Category> results = em().createQuery(jpql, Category.class)
                .setParameter("name", name.trim().toLowerCase())
                .setMaxResults(1)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Check if any other category already uses the given name.
     */
    public boolean existsByName(String name) {
        return findByName(name) != null;
    }

    /**
     * Return active (or null status) categories ordered by name.
     */
    public List<Category> findActiveOrdered() {
        String jpql = "SELECT c FROM Category c WHERE c.status IS NULL OR lower(c.status) = :st ORDER BY lower(c.name) ASC";
        return em().createQuery(jpql, Category.class)
                .setParameter("st", STATUS_ACTIVE.toLowerCase())
                .getResultList();
    }

    /**
     * Create a new category (sets create date and active status if not already set).
     */
    @Transactional
    public Category create(Category category) {
        if (category == null) return null;
        if (category.getCreateDate() == null) {
            category.setCreateDate(new Date());
        }
        if (category.getStatus() == null) {
            category.setStatus(STATUS_ACTIVE);
        }
        return saveOrUpdate(category);
    }

    /**
     * Update an existing category (sets update date).
     */
    @Transactional
    public Category update(Category category) {
        if (category == null) return null;
        category.setUpdateDate(new Date());
        return saveOrUpdate(category);
    }

    /**
     * Soft delete: mark category inactive.
     */
    @Transactional
    public void softDelete(Long categoryId) {
        if (categoryId == null) return;
        Category category = getById(categoryId);
        if (category != null) {
            category.setStatus(STATUS_INACTIVE);
            update(category);
        }
    }
}
