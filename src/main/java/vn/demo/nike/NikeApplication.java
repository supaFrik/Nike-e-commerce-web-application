package vn.demo.nike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NikeApplication {

	public static void main(String[] args) {
		SpringApplication start =
				new SpringApplication(NikeApplication.class);

		start.run(args);
	}
}
