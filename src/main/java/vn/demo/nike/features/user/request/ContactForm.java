package vn.demo.nike.features.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactForm {
    private String name;
    private String email;
    private String message;
}
