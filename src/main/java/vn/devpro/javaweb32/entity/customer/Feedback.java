package vn.devpro.javaweb32.entity.customer;

import vn.devpro.javaweb32.common.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contact_messages")
public class Feedback extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Size(max = 80)
    @Column(name = "name", length = 80)
    private String name;

    @Email
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = true)
    private String email;

    @NotBlank
    @Size(max = 500)
    @Column(name = "message", length = 500, nullable = false)
    private String message;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
