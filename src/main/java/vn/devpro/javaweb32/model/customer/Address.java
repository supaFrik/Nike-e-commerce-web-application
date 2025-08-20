package vn.devpro.javaweb32.model.customer;

import vn.devpro.javaweb32.common.base.BaseEntity;

import javax.persistence.*;

@Entity(name = "CustomerAddress")
@Table(name = "addresses")
public class Address extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String street;

    @Column(length = 50, nullable = false)
    private String city;

    @Column(length = 50, nullable = false)
    private String state;

    @Column(length = 50, nullable = false)
    private String zip;

    @Column(length = 50, nullable = false)
    private String country;

    @Column(name = "is_default", nullable = false)
    private Boolean is_default = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Address() {
        //Default Constructor
    }

    //All-args Constructor
    public Address(String street, String city, String state, String zip, String country, Customer customer, Boolean is_default) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.customer = customer;
        this.is_default = is_default;
    }

    //Getters And Setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zip + ", " + country;
    }
}
