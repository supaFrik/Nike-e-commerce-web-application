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

	public List<Customer> findAllActive() {
		// Updated to use ACTIVE status string instead of numeric 1
		String sql = "SELECT * FROM customer WHERE status = 'ACTIVE' OR status IS NULL";
		return executeNativeSql(sql);
	}

	public List<Customer> findAdminUser() {
		// Simplified query to get all customers for now
		// You can enhance this later when role management is properly implemented
		String sql = "SELECT * FROM customer WHERE (status = 'ACTIVE' OR status IS NULL) ORDER BY username ASC";
		return executeNativeSql(sql);
	}

	public List<Customer> findAll() {
		String sql = "SELECT * FROM customer ORDER BY username ASC";
		return executeNativeSql(sql);
	}

	public Customer findByUsername(String username) {
		String sql = "SELECT * FROM customer WHERE username = '" + username + "'";
		List<Customer> customers = executeNativeSql(sql);
		return customers.isEmpty() ? null : customers.get(0);
	}
}