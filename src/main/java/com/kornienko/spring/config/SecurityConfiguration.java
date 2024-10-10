package com.kornienko.spring.config;

import com.kornienko.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import static com.kornienko.spring.database.entity.Role.ADMIN;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private static final String LOGIN = "/login";
    private final UserService userService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
//                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .requestMatchers(LOGIN, "/users/signup", "/v3/api/docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/{\\d+}/delete")).hasAuthority(ADMIN.getAuthority())
                        .requestMatchers("/admin/**").hasAuthority(ADMIN.getAuthority())
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout") // default behavior
                        .logoutSuccessUrl(LOGIN) // default behavior
                        .deleteCookies("JSESSIONID") // default behavior
                )
                .formLogin(login -> login
                        .loginPage(LOGIN)
                        .defaultSuccessUrl("/users"))
//                .oauth2Login(config -> config
//                        .loginPage(LOGIN)
//                        .defaultSuccessUrl("/users")
//                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService())))
                .build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");
            // TODO: 06.10.2024 create user userService.create
            UserDetails userDetails = userService.loadUserByUsername(email);
//            OidcUser oidcUser = new OidcUserService().loadUser(userRequest);
            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());

            Set<Method> userDetailsMethods = Set.of(UserDetails.class.getMethods());
            return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                    new Class[] {UserDetails.class, OidcUser.class},
                    (proxy, method, args) -> userDetailsMethods.contains(method)
                            ? method.invoke(userDetails, args)
                            : method.invoke(oidcUser, args));
        };
    }
}
