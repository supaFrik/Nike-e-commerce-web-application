package vn.devpro.javaweb32.controller;

import org.springframework.beans.factory.annotation.Autowired;
import vn.devpro.javaweb32.service.CustomerService;

public class BaseController {
    @Autowired
    protected CustomerService customerService;

}
