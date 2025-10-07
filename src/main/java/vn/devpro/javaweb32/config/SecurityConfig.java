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

            String role = credential.getRole(); //USER hoặc ADMIN

            User.UserBuilder builder = User.withUsername(credential.getEmail())
                    .password(credential.getPasswordHash())
                    .roles(role)
                    .disabled(!credential.isEnabled());

            builder.accountLocked(credential.isLocked());

            return builder.build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService uds) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringAntMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(
                                "/",
                                "/auth/**",
                                "/signup",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/videos/**",
                                "/fonts/**",
                                "/slick/**"
                        ).permitAll()
                        .antMatchers(
                                "/products",
                                "/products/**",
                                "/product-detail",
                                "/product-detail/**"
                        ).permitAll()
                        // Public AJAX endpoints for pre-signup validation
                        .antMatchers(
                                "/api/auth/email-exists",
                                "/api/auth/check-duplicate",
                                "/api/auth/check-duplicate/**"
                        ).permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
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
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                            if (isAdmin) {
                                response.sendRedirect("/admin/home");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
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
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response,
                                                 org.springframework.security.core.AuthenticationException authException) throws IOException {
                                String requestURI = request.getRequestURI();
                                if (requestURI.startsWith("/api/")) {
                                    response.setStatus(401);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\":\"Authentication required\",\"success\":false}");
                                } else {
                                    response.sendRedirect(request.getContextPath() + "/auth");
                                }
                            }
                        })
                )
                .userDetailsService(uds);

        return http.build();
    }
}
