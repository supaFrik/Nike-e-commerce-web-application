package vn.devpro.javaweb32.service.administrator;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.devpro.javaweb32.common.base.BaseService;
import vn.devpro.javaweb32.entity.customer.Customer;

@Service
public class CustomerAdminService extends BaseService<Customer> {

	@Override
	public Class<Customer> clazz() {

		return Customer.class;
	}

	public List<Customer> findAllActive () {
		String sql = "SELECT * FROM customer WHERE status=1";
		return executeNativeSql (sql);
	}

	public List<Customer> findAdminUser () {
		String sql = "SELECT * FROM customer u, customer_role ur, role r WHERE u.id=ur.customer_id AND ur.role_id = r.id AND r.name='ADMIN'";
		return super.executeNativeSql (sql);
	}
}