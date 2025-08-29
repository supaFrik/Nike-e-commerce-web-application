package vn.devpro.javaweb32.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vn.devpro.javaweb32.entity.customer.Credential;
import vn.devpro.javaweb32.repository.CredentialRepository;

import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(CredentialRepository credentialRepository) {
        return username -> {
            Credential credential = credentialRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            return User.withUsername(credential.getEmail())
                    .password(credential.getPasswordHash())
                    .roles("USER")
                    .disabled(!credential.isEnabled())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/", "/auth/**", "/signup", "/css/**", "/js/**", "/images/**", "/videos/**", "/fonts/**" ,"/products", "/products/**", "/images/products/**" , "/product-detail", "/product-detail/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth") // Trang chứa Form
                        .loginProcessingUrl("/login") //Url để spring xử lý Post Login
                        .usernameParameter("username")  // Form name cho email/username
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth?logout=true")
                )
                .userDetailsService(userDetailsService);
        return http.build();
    }
}
