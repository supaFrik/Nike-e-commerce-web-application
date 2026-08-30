package vn.demo.nike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NikeApplication {

	public static void main(String[] args) {
		SpringApplication start =
				new SpringApplication(NikeApplication.class);

		start.run(args);
	}

	// test grafana firing alert
//	@GetMapping("/monitor")
//	public String monitor() {
//		try {
//			boolean condition = true;
//			while (condition) {
//				Runnable r = () -> {
//					while (true) {
//
//					}
//				};
//				new Thread(r).start();
//				Thread.sleep(5000);
//			}
//		} catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return "Hello World";
    }
}
