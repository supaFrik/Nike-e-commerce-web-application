package vn.devpro.javaweb32.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import vn.devpro.javaweb32.common.constant.Jw32Contant;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer, Jw32Contant {
	@Bean
	public ViewResolver viewResolver() {
		// Xu ly va tra ve doi tuong view thong qua ten
		InternalResourceViewResolver bean = 
				new InternalResourceViewResolver();
		bean.setViewClass(JstlView.class);
		bean.setPrefix("/WEB-INF/views/");
		bean.setSuffix(".jsp");
		return bean;
	}
	@Override
	public void addResourceHandlers(
			ResourceHandlerRegistry registry) {
		registry.addResourceHandler(
			"/customer/**").addResourceLocations(
					"classpath:/customer/");
		registry.addResourceHandler(
				"/administrator/**").addResourceLocations(
						"classpath:/administrator/");
        String uploadLocation = "file:" + FOLDER_UPLOAD.replace("\\", "/");
        if (!uploadLocation.endsWith("/")) {
            uploadLocation += "/";
        }
        registry.addResourceHandler("/images/products/**").addResourceLocations(uploadLocation);
	}

}