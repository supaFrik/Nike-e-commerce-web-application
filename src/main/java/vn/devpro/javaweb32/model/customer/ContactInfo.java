package vn.devpro.javaweb32.model.customer;

import vn.devpro.javaweb32.common.base.BaseEntity;
import vn.devpro.javaweb32.model.customer.enums.ContactType;

import javax.persistence.*;

@Entity
@Table(name = "contact")
public class ContactInfo extends BaseEntity {

    @Column(length = 20)
    String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ContactType contactType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    //Default Constructor
    public ContactInfo() {}

    //All-args Constructor
    public ContactInfo(String phone, ContactType contactType, Customer customer) {
        this.phone = phone;
        this.contactType = contactType;
        this.customer = customer;
    }

    //Getters And Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
            return phone + ", "
                    + contactType + " "
                    + customer + " ";
    }
}
