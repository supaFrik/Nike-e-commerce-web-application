package vn.demo.nike.features.identity.user.domain;

import jakarta.persistence.*;
import lombok.*;
import vn.demo.nike.shared.domain.BaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_messages")
public class Contact extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name", length = 80)
    private String name;

    @Column(name = "email", length = 100, nullable = true)
    private String email;

    @Column(name = "message", length = 500, nullable = false)
    private String message;
}
