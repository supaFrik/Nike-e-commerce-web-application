package vn.devpro.javaweb32.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.model.customer.Customer;
import vn.devpro.javaweb32.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerApiController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("")
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }
}
