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
            var credential = credentialRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

            User.UserBuilder builder = User.withUsername(credential.getEmail())
                    .password(credential.getPasswordHash())
                    .roles("USER", "ADMIN")
                    .disabled(!credential.isEnabled());

            // map "locked" -> accountLocked
            builder.accountLocked(credential.isLocked());

            return builder.build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService uds) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringAntMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/", "/auth/**", "/signup", "/css/**", "/js/**", "/images/**", "/videos/**", "/fonts/**", "/products", "/products/**", "/product-detail", "/product-detail/**", "/cart/**", "api/cart/**").permitAll()
                        .antMatchers("/cart", "/cart/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(uds);

        return http.build();
    }
}
