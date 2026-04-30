package vn.demo.nike.shared.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfException;
import vn.demo.nike.features.identity.user.repository.UserRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll()

                        .requestMatchers("/", "/login", "/signup", "/error", "/favicon.ico", "/products/**", "/api/v1/products/**", "/css/**", "/js/**", "/images/**", "/videos/**", "/fonts/**", "/static/**", "/assets/**").permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())

                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?expired=true"))

                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    if (accessDeniedException instanceof CsrfException) {
                        if (request.getSession(false) != null) {
                            request.getSession(false).invalidate();
                        }
                        response.addCookie(new jakarta.servlet.http.Cookie("JSESSIONID", ""));
                        response.setStatus(HttpServletResponse.SC_FOUND);
                        response.setHeader("Location", request.getContextPath() + "/login?expired=true");
                        return;
                    }
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
