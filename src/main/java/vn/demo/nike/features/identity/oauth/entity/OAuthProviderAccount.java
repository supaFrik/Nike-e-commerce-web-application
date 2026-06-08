package vn.demo.nike.features.identity.oauth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.demo.nike.features.identity.user.entity.User;
import vn.demo.nike.shared.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "oauth_provider_accounts", uniqueConstraints = @UniqueConstraint(
        name = "uk_oauth_provider_subject" ,
        columnNames = {"provider", "provider_subject"}
))
public class OAuthProviderAccount extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(name = "provider_subject", nullable = false)
    private String providerSubject;

    @Column(name = "email_at_link_item", nullable = false)
    private String emailAtLinkItem;
}
