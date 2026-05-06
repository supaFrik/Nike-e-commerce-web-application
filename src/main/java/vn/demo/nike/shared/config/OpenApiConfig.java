package vn.demo.nike.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI nikeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nike Ecommerce Web Application")
                        .version("v1")
                        .description("API documentation for NIKE")
                        .contact(new Contact()
                                .name("Nike Team")
                                .email("tqv2005tc@gmail.com")));
    }
}
