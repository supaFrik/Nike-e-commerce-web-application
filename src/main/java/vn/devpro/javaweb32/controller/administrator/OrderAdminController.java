package vn.devpro.javaweb32.controller.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.entity.order.Order;
import vn.devpro.javaweb32.repository.OrderRepository;
import vn.devpro.javaweb32.service.order.OrderService;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class OrderAdminController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderAdminController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(@RequestParam(value = "q", required = false) String q,
                             @RequestParam(value = "status", required = false) String status,
                             Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("totalItems", 0);
        model.addAttribute("totalReturns", 0);
        model.addAttribute("totalFulfilled", 0);
        return "/administrator/order/order-management";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "/administrator/order/order-detail"; // Ensure this JSP exists; otherwise adjust
    }

    @GetMapping("/create")
    public String createOrderForm(Model model) {
        // Placeholder: show create order page or redirect if not implemented
        return "/administrator/order/order-create"; // Ensure JSP exists; placeholder
    }

    @GetMapping("/delete")
    public String deleteOrder(@RequestParam("id") Long id) {
        orderRepository.deleteById(id);
        return "redirect:/admin/orders";
    }
}

