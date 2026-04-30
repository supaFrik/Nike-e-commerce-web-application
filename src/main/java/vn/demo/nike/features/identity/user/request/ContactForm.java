package vn.demo.nike.features.identity.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactForm {
    private String name;
    private String email;
    private String message;
}
