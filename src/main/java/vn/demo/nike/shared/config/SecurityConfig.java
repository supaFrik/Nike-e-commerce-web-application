package vn.demo.nike.shared.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import vn.demo.nike.infras.security.jwt.converter.NikeJwtAuthenticationConverter;
import vn.demo.nike.infras.security.jwt.properties.JwtProperties;
import vn.demo.nike.infras.security.oauth.service.NikeOidcUserService;
import vn.demo.nike.infras.security.oauth.service.NikeOAuth2UserService;
import vn.demo.nike.features.user.repository.UserRepository;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableConfigurationProperties({JwtProperties.class})
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final NikeOAuth2UserService nikeOAuth2UserService;
    private final NikeOidcUserService nikeOidcUserService;
    private final NikeJwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    @Order(2)
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/api/v1/**"))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/oauth2/google", "/api/v1/products/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll()

                        .requestMatchers("/", "/login", "/signup", "/api/auth/signup-verification-code", "/api/auth/check-duplicate", "/api/v1/auth/verification-codes", "/api/v1/auth/availability", "/error", "/favicon.ico", "/slick/**", "/products/**", "/product-detail", "/api/v1/products/**", "/api/payments/vnpay/return", "/api/payments/vnpay/ipn", "/css/**", "/js/**", "/images/**", "/videos/**", "/fonts/**", "/static/**", "/assets/**", "/uploads/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", false)
                        .permitAll())

                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(nikeOAuth2UserService)
                                .oidcUserService(nikeOidcUserService))
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login?oauthError=true")
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())

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
    @Order(1)
    @Profile("dev")
    SecurityFilterChain managementDev(HttpSecurity http) throws Exception {
        http
                .securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    @Order(1)
    @Profile({"prod", "bootstrap"})
    SecurityFilterChain managementProd(HttpSecurity http) throws Exception {
        http
                .securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtProperties jwtProperties) {
        SecretKey key = jwtSecretKey(jwtProperties);
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    public JwtDecoder jwtDecoder(JwtProperties jwtProperties) {
        return NimbusJwtDecoder
                .withSecretKey(jwtSecretKey(jwtProperties))
                .build();
    }

    private SecretKey jwtSecretKey(JwtProperties jwtProperties) {
        String secret = jwtProperties.secret();
        if (secret == null || secret.isEmpty() || secret.length() < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 characters");
        }
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }
}
