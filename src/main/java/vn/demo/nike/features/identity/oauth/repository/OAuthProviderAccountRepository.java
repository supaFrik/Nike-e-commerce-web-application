package vn.demo.nike.features.identity.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.features.identity.oauth.entity.OAuthProviderAccount;

import java.util.Optional;

public interface OAuthProviderAccountRepository extends JpaRepository<OAuthProviderAccount, Long> {
    Optional<OAuthProviderAccount> findByProviderAndProviderSubject(String provider, String providerSubject);
    boolean existsByProviderAndProviderSubject(String provider, String providerSubject);
}
