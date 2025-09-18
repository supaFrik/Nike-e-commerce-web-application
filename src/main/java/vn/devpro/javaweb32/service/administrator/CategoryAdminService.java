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
		String sql = "SELECT * FROM category WHERE status = 1";
		return executeNativeSql(sql);
	}

}
