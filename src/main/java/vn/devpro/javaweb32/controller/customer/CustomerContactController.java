package vn.devpro.javaweb32.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.devpro.javaweb32.controller.BaseController;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.customer.Feedback;
import vn.devpro.javaweb32.service.customer.FeedbackService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/contact")
public class CustomerContactController extends BaseController {

    private final FeedbackService feedbackService;

    public CustomerContactController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String contactPage() {
        return "redirect:/#footer-contact";
    }

    @PostMapping
    public String submitContact(@Valid @ModelAttribute("contactForm") Feedback form,
                                BindingResult result,
                                HttpServletRequest request,
                                RedirectAttributes ra) {
        String referer = request.getHeader("Referer");
        String serverName = request.getServerName();
        if (referer == null || referer.isBlank() || (referer.startsWith("http") && !referer.contains(serverName))) {
            referer = "/";
        }
        String target = referer.contains("#footer-contact") ? referer : referer + "#footer-contact";

        if (result.hasErrors()) {
            ra.addFlashAttribute("contactForm", form);
            ra.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "contactForm", result);
            return "redirect:" + target;
        }
        form.setMessage(form.getMessage() != null ? form.getMessage().trim() : null);

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object custObj = session.getAttribute("customer");
            if (custObj instanceof Customer) {
                form.setCustomer((Customer) custObj);
            }
        }
        form.setStatus("NEW");
        feedbackService.save(form);
        ra.addFlashAttribute("successMessage", "Your message has been sent.");
        return "redirect:" + target;
    }
}
