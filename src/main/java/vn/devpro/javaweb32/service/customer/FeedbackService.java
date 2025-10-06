package vn.devpro.javaweb32.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.customer.Feedback;
import vn.devpro.javaweb32.repository.CustomerRepository;
import vn.devpro.javaweb32.repository.FeedbackRepository;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Customer getById(long id){
        return customerRepository.findById(id).orElse(null);
    }

    public Feedback save(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
}
