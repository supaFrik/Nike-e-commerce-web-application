package vn.demo.nike.infras.storage.cloudinary.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryProperties {
    private String url;
    private String cloudName;
    private String apiKey;
    private String apiSecret;
}
