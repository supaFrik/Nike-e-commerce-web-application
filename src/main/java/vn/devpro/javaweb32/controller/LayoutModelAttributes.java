package vn.devpro.javaweb32.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import vn.devpro.javaweb32.entity.customer.Feedback;

@ControllerAdvice
public class LayoutModelAttributes {
    @ModelAttribute
    public void addContactForm(Model model) {
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new Feedback());
        }
    }
}
