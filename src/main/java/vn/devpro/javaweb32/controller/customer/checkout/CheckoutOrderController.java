package vn.devpro.javaweb32.controller.customer.checkout;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.order.Order;
import vn.devpro.javaweb32.entity.order.enums.ShippingMethod;
import vn.devpro.javaweb32.repository.CredentialRepository;
import vn.devpro.javaweb32.service.customer.CartService;
import vn.devpro.javaweb32.service.order.OrderService;
import vn.devpro.javaweb32.dto.customer.order.OrderSummary;
import org.springframework.transaction.annotation.Transactional;
import vn.devpro.javaweb32.repository.CustomerRepository;
import vn.devpro.javaweb32.entity.customer.Address;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping
public class CheckoutOrderController {

    private static final String CHECKOUT_COMPLETED_FLAG = "CHECKOUT_COMPLETED";

    private final CartService cartService;
    private final CredentialRepository credentialRepository;
    private final OrderService orderService;
    private final CustomerRepository customerRepository;

    public CheckoutOrderController(CartService cartService, CredentialRepository credentialRepository, OrderService orderService, CustomerRepository customerRepository) {
        this.cartService = cartService;
        this.credentialRepository = credentialRepository;
        this.orderService = orderService;
        this.customerRepository = customerRepository;
    }

    private Customer getCurrentCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return credentialRepository.findByEmail(email).map(Credential::getCustomer).orElse(null);
    }

    private double resolveShippingCost(ShippingMethod method) {
        return method == null ? ShippingMethod.STANDARD.getCost().doubleValue() : method.getCost().doubleValue();
    }

    private ShippingMethod parseShippingMethod(String raw) {
        return ShippingMethod.safeValueOf(raw);
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) return "redirect:/auth";
        ShippingMethod shippingMethod = ShippingMethod.STANDARD; // default pre-selection
        OrderSummary summary;
        try {
            summary = orderService.buildOrderSummary(customer, shippingMethod);
        } catch (IllegalStateException ex) {
            return "redirect:/cart?empty";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("orderAccessible", false);
        model.addAttribute("hasItems", true);
        model.addAttribute("items", summary.getItems());
        model.addAttribute("subtotal", summary.getSubtotal());
        model.addAttribute("shippingCost", summary.getShippingCost());
        model.addAttribute("discount", summary.getDiscount());
        model.addAttribute("total", summary.getTotal());
        model.addAttribute("currentStep", 2);
        model.addAttribute("shippingMethod", summary.getShippingMethod().name());
        model.addAttribute("shippingMethods", ShippingMethod.available());
        return "customer/checkout";
    }

    @PostMapping("/checkout/complete")
    @Transactional
    public String completeCheckout(HttpSession session,
                                   @RequestParam(name = "shippingMethod", required = false) String shippingMethodRaw,
                                   @RequestParam(name = "firstName", required = false) String firstName,
                                   @RequestParam(name = "lastName", required = false) String lastName,
                                   @RequestParam(name = "email", required = false) String email,
                                   @RequestParam(name = "phone", required = false) String phone,
                                   @RequestParam(name = "address", required = false) String line1,
                                   @RequestParam(name = "address2", required = false) String line2,
                                   @RequestParam(name = "city", required = false) String city,
                                   @RequestParam(name = "state", required = false) String province,
                                   @RequestParam(name = "zipCode", required = false) String postalCode,
                                   @RequestParam(name = "country", required = false) String country) {
        Customer customer = getCurrentCustomer();
        if (customer == null) return "redirect:/auth";
        if (shippingMethodRaw == null) shippingMethodRaw = ShippingMethod.STANDARD.name();
        boolean credentialUpdated = false;
        if (email != null && !email.isBlank() && customer.getCredential() != null) {
            String newEmail = email.trim().toLowerCase();
            if (!newEmail.equalsIgnoreCase(customer.getCredential().getEmail())) {
                boolean exists = credentialRepository.findByEmail(newEmail).isPresent();
                if (!exists) {
                    customer.getCredential().setEmail(newEmail);
                    credentialUpdated = true;
                }
            }
        }
        if (customerRepository != null) {
            boolean hasAddressData = line1 != null || line2 != null || city != null || province != null || postalCode != null || country != null || phone != null;
            if (hasAddressData) {
                Address addr = customer.getAddress();
                if (addr == null) {
                    addr = new Address();
                    addr.setPrimaryAddress(true);
                    addr.setCustomer(customer);
                    if (customer.getAddresses() == null) {
                        customer.setAddresses(new ArrayList<>());
                    }
                    customer.getAddresses().add(addr);
                }
                if (firstName != null || lastName != null) {
                    String recipient = (firstName == null ? "" : firstName.trim()) + (lastName == null ? "" : (" "+ lastName.trim()));
                    recipient = recipient.trim();
                    if (!recipient.isEmpty()) addr.setRecipientName(recipient);
                }
                if (line1 != null && !line1.isBlank()) addr.setLine1(line1.trim());
                if (line2 != null && !line2.isBlank()) addr.setLine2(line2.trim());
                if (city != null && !city.isBlank()) addr.setCity(city.trim());
                if (province != null && !province.isBlank()) addr.setProvince(province.trim());
                if (country != null && !country.isBlank()) addr.setCountry(country.trim());
                if (postalCode != null && !postalCode.isBlank()) addr.setPostalCode(postalCode.trim());
                if (phone != null && !phone.isBlank()) addr.setPhone(phone.trim());
                customerRepository.save(customer);
                credentialUpdated = false;
            } else if (credentialUpdated) {
                credentialRepository.save(customer.getCredential());
            }
        }
        ShippingMethod method = ShippingMethod.safeValueOf(shippingMethodRaw);
        Order order;
        try {
            order = orderService.createOrderFromCart(customer, method);
        } catch (IllegalStateException ex) {
            return "redirect:/cart?empty";
        }
        session.setAttribute("LAST_ORDER_ID", order.getId());
        session.setAttribute(CHECKOUT_COMPLETED_FLAG, Boolean.TRUE);
        return "redirect:/order/" + order.getId();
    }

    @GetMapping("/order")
    public String order(HttpSession session) {
        Long orderId = (Long) session.getAttribute("LAST_ORDER_ID");
        if (orderId != null) {
            return "redirect:/order/" + orderId;
        }
        return "redirect:/checkout";
    }

    @GetMapping("/order/{id}")
    public String orderById(@PathVariable("id") String idRaw, HttpSession session, Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) return "redirect:/auth";
        Long id = parseLongSafe(idRaw);
        if (id == null) return "redirect:/checkout";
        Order orderEntity = orderService.findById(id);
        if (orderEntity == null || !orderEntity.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/checkout";
        }
        session.setAttribute("LAST_ORDER_ID", id);
        model.addAttribute("orderAccessible", true);
        model.addAttribute("hasItems", orderEntity.getItems() != null && !orderEntity.getItems().isEmpty());
        model.addAttribute("currentStep", 3);
        model.addAttribute("order", orderEntity);
        return "customer/order";
    }

    private Long parseLongSafe(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;
        while (s.startsWith("+")) {
            s = s.substring(1);
        }
        if (s.isEmpty()) return null;
        if (!s.matches("-?\\d+")) return null;
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
