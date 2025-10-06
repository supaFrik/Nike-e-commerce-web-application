package vn.devpro.javaweb32.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.customer.Feedback;
import vn.devpro.javaweb32.repository.FeedbackRepository;
import vn.devpro.javaweb32.service.customer.FeedbackService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CustomerFeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/contact")
    public String saveFeedback(@RequestParam String email,
                                 @RequestParam String message,
                                 HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        Feedback feedback = new Feedback();
        feedback.setEmail(email);
        feedback.setMessage(message);
        if(customer != null) feedback.setCustomer(customer);
        else feedback.setCustomer(new Customer());
        feedbackService.save(feedback);

        feedback.setCreateDate(new Date());
        feedback.setStatus("NEW");

        return "redirect:/contact?success=true";
    }
}
