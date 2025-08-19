package vn.devpro.javaweb32.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // This configuration enables JPA auditing for @CreatedDate and @LastModifiedDate annotations
    // If you want to enable @CreatedBy and @LastModifiedBy, you would need to implement AuditorAware<Long>
}
