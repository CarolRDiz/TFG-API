package com.portoflio.api.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.portoflio.api.config.RsaKeyProperties;
import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.models.Product;
import com.portoflio.api.security.guards.ProductAuthorizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigJWT {
    @Autowired
    @Qualifier("myUserDetailsService")
    MyUserDetailsService myUserDetailsService;
    @Autowired
    ProductRepository productRepository;

    private static final String[] WHITE_LIST_URL = {
            "/api-docs/**",
            "/api/v1/auth/**",
            "/v3/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger**",
            "/webjars/**",
            "/swagger-ui.html"};

    @Bean
    public AuthenticationManager authenticationManager(MyUserDetailsService myUserDetailsService) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    private ProductAuthorizationManager productAuthorizationManager;

    @Bean
    public UserDetailsService users() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password("$2a$10$5DXFN7E0r9iOsHJv8eNq/OpDgClwDFQiea/jtVxI/JbBOcXK.Ddr2")
                        .authorities("SCOPE_ADMIN")
                        .build()
        );
    }


    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }
    */
    private final RsaKeyProperties jwtConfigProperties;
    public SecurityConfigJWT(RsaKeyProperties jwtConfigProperties) {
        this.jwtConfigProperties = jwtConfigProperties;
    }

    AuthorizationManager<RequestAuthorizationContext> productAuthManager() {
        return new AuthorizationManager<RequestAuthorizationContext>() {
            @Override
            public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
                boolean admin = false;
                //return new AuthorizationDecision(true);
                for (final GrantedAuthority ga : authentication.get().getAuthorities()) {
                    if (ga.getAuthority().equals("SCOPE_ADMIN")) {
                        admin = true;
                    }
                }
                Long id = Long.valueOf(object.getVariables().get("id"));
                Optional<Product> oProduct = productRepository.findById(id);
                if (oProduct.isPresent()) {
                    if (admin==false && oProduct.get().getVisibility() == false) return new AuthorizationDecision(false);
                    return new AuthorizationDecision(true);
                } return new AuthorizationDecision(false);
            }
        };
    }

    @Value("${allowedOrigin}")
    private String allowed_origin;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //.cors().configurationSource(corsConfigurationSource()).and()
                //.cors(Customizer.withDefaults())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:4200", allowed_origin));
                    configuration.setAllowedMethods(List.of("GET","POST","PATCH", "DELETE"));
                    configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(myUserDetailsService)
                // authorization of preflight requests (OPTIONS)
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(WHITE_LIST_URL).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/token").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/registration/").permitAll())

                //ILLUSTRATIONS
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST,"/illustrations/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,"/illustrations/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PUT,"/illustrations/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE,"/illustrations/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/illustrations/**").permitAll())
                //PRODUCTS
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST,"/products/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,"/products/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PUT,"/products/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE,"/products/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/products/{id}/").access(productAuthManager()))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/products/**").permitAll())
                //CATEGORIES
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST,"/categories/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH,"/categories/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PUT,"/categories/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE,"/categories/**").hasAuthority("SCOPE_ADMIN"))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/categories/**").permitAll())

                .authorizeHttpRequests(auth -> auth.requestMatchers("/productCategories/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/order/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/orderItems/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/videos/stream/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/images/**").permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                // enables JWT encoded bearer token support
                .exceptionHandling(
                        (ex) -> ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .build();
    }

    //
    //	This was added via PR (thanks to @ch4mpy)
    //	This will allow the /token endpoint to use basic auth and everything else uses the SFC above
    //
    /*
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public SecurityFilterChain tokenSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(corsConfigurationSource()).and()
                .securityMatcher("/token**")
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS).permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                //todas las peticiones requieren que el usuario se autentifique
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // “stateless“, is a guarantee that the application won't create any session at all
                //  These more strict control mechanisms have the direct implication that cookies
                //  are not used, and so each and every request needs to be re-authenticated.
                //  This stateless architecture plays well with REST APIs and their Statelessness constraint.
                //  They also work well with authentication mechanisms such as Basic and Digest Authentication.
                .csrf(AbstractHttpConfigurer::disable)
                //.httpBasic(withDefaults())
                // Autentificarse con username y password
                .build();
    }
    */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(jwtConfigProperties.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(jwtConfigProperties.publicKey()).privateKey(jwtConfigProperties.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }


    /*
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", allowed_origin));
        configuration.setAllowedMethods(List.of("GET","POST","PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
     */

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}