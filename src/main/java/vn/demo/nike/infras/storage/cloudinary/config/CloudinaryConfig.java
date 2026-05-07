package vn.demo.nike.infras.storage.cloudinary.config;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.demo.nike.infras.storage.cloudinary.properties.CloudinaryProperties;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {

    private final CloudinaryProperties props;

    @Bean
    public Cloudinary cloudinary() {
        if (hasText(props.getUrl())) {
            return new Cloudinary(props.getUrl());
        }

        if (!hasText(props.getCloudName()) ||
                !hasText(props.getApiKey()) ||
                !hasText(props.getApiSecret())) {

            throw new IllegalStateException("Missing Cloudinary config. Set CLOUDINARY_URL or CLOUDINARY_CLOUD_NAME/CLOUDINARY_API_KEY/CLOUDINARY_API_SECRET");
        }

        return new Cloudinary(Map.of(
                "cloud_name", props.getCloudName(),
                "api_key", props.getApiKey(),
                "api_secret", props.getApiSecret()
        ));
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
