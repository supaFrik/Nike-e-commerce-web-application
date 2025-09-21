package vn.devpro.javaweb32.service.administrator;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.devpro.javaweb32.common.base.BaseService;
import vn.devpro.javaweb32.entity.product.Category;

@Service
public class CategoryAdminService extends BaseService<Category> {

	@Override
	public Class<Category> clazz() {
		return Category.class;
	}

	public List<Category> findAllActive(){
		// Updated to use ACTIVE status string instead of numeric 1
		String sql = "SELECT * FROM category WHERE status = 'ACTIVE' OR status IS NULL";
		return executeNativeSql(sql);
	}

	public List<Category> findAll(){
		String sql = "SELECT * FROM category ORDER BY name ASC";
		return executeNativeSql(sql);
	}

	public Category findByName(String name) {
		String sql = "SELECT * FROM category WHERE name = '" + name + "'";
		List<Category> categories = executeNativeSql(sql);
		return categories.isEmpty() ? null : categories.get(0);
	}
}
