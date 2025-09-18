package vn.devpro.javaweb32.service.administrator;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.devpro.javaweb32.common.base.BaseService;
import vn.devpro.javaweb32.dto.administrator.ProductSearch;
import vn.devpro.javaweb32.entity.product.Product;

/**
 * Service class for Product entity operations.
 */

@Service
public class ProductAdminService extends BaseService<Product> {
    @Override
    public Class<Product> clazz() {
        return Product.class;
    }

    /**
     * Finds all active products (status = 1).
     * @return List of active products
     */
    @SuppressWarnings("unchecked")
    public List<Product> findAllActive() {
        String sql = "SELECT * FROM products WHERE (status='ACTIVE' OR status='1' OR status IS NULL)";
        return executeNativeSql(sql);
    }

    /**
     * Finds all products without any filters - useful for debugging
     * @return List of all products
     */
    @SuppressWarnings("unchecked")
    public List<Product> findAllProducts() {
        String sql = "SELECT * FROM products";
        return executeNativeSql(sql);
    }

    public List<Product> search(ProductSearch productSearch) {
        String sql = "SELECT * FROM products p WHERE 1=1";

        if(productSearch.getStatus() != 2) {
            sql += " AND p.status=" + productSearch.getStatus();
        }

        if(productSearch.getCategoryId() != 0) {
            sql += " AND p.category_id=" + productSearch.getCategoryId();
        }

        if(productSearch.getKeyword() != null && !productSearch.getKeyword().isEmpty()) {
            String keyword = productSearch.getKeyword().toLowerCase();
            keyword = keyword.replace("'", "''");
            sql += " AND (LOWER(p.name) LIKE '%" + keyword + "%' OR LOWER(p.description) LIKE '%" + keyword + "%')";
        }

        String beginDateStr = productSearch.getBeginDate();
        String endDateStr = productSearch.getEndDate();
        if(beginDateStr != null && endDateStr != null) {
        	sql += " AND p.created_at BETWEEN '" + beginDateStr + "' AND '" + endDateStr + "'";
        }

        System.out.println("sql = " + sql);
        return executeNativeSql(sql);
    }
}