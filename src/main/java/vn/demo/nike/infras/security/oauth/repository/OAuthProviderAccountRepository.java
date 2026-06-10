package vn.demo.nike.infras.security.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.demo.nike.infras.security.oauth.entity.OAuthProviderAccount;

import java.util.Optional;

public interface OAuthProviderAccountRepository extends JpaRepository<OAuthProviderAccount, Long> {
    Optional<OAuthProviderAccount> findByProviderAndProviderSubject(String provider, String providerSubject);
    boolean existsByProviderAndProviderSubject(String provider, String providerSubject);
}
