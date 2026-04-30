package vn.demo.nike.features.identity.user.domain;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String recipientName;
    private String line1;
    private String line2;
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private String phone;
    private boolean primaryAddress;
}
