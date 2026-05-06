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
        if (props.getCloudName() == null ||
                props.getApiKey() == null ||
                props.getApiSecret() == null) {

            throw new IllegalStateException("Missing Cloudinary config (resolved via Spring)");
        }

        return new Cloudinary(Map.of(
                "cloud_name", props.getCloudName(),
                "api_key", props.getApiKey(),
                "api_secret", props.getApiSecret()
        ));
    }
}
