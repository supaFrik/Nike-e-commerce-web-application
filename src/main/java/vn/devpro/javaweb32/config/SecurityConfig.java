package vn.devpro.javaweb32.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.devpro.javaweb32.repository.CredentialRepository;

import org.springframework.security.core.userdetails.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                        .antMatchers("/", "/auth/**", "/signup", "/css/**", "/js/**", "/images/**", "/videos/**", "/fonts/**", "/slick/**").permitAll()
                        .antMatchers("/products", "/products/**", "/product-detail", "/product-detail/**").permitAll()
                        .antMatchers("/admin/**").permitAll()
                        .antMatchers("/api/cart/**").authenticated()
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
                // Handle authentication exceptions for API requests
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response,
                                                 org.springframework.security.core.AuthenticationException authException) throws IOException {
                                String requestURI = request.getRequestURI();
                                if (requestURI.startsWith("/api/")) {
                                    // For API requests, return JSON error instead of redirecting
                                    response.setStatus(401);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\":\"Authentication required\",\"success\":false}");
                                } else {
                                    // For web requests, redirect to login page
                                    response.sendRedirect(request.getContextPath() + "/auth");
                                }
                            }
                        })
                )
                .userDetailsService(uds);

        return http.build();
    }
}
