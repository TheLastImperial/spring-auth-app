package com.thelastimperial.auth.authapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.thelastimperial.auth.admin.config.security.customizers.AdminAuthorizeCustomizer;
import com.thelastimperial.auth.auth.config.security.customizers.AuthAuthorizeCustomizer;
import com.thelastimperial.auth.auth.config.security.customizers.FormLoginCustomizer;
import com.thelastimperial.auth.auth.config.security.customizers.LogoutCustomizer;
import com.thelastimperial.auth.auth.config.security.customizers.RememberMeCustomizer;
import com.thelastimperial.auth.authn.config.security.customizers.WebAuthnAuthorizeCustomizer;
import com.thelastimperial.auth.authn.config.security.customizers.WebAuthnCustomizer;
import com.thelastimperial.auth.oauthserver.config.security.customizers.OAuthServerAuthorizeCustomizer;
import com.thelastimperial.auth.oauthserver.config.security.customizers.OAuthServerExceptionHandlerCustomizer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class SecurityConfig {
    private WebAuthnAuthorizeCustomizer webAuthnAuthorizeCustomizer;
    private AuthAuthorizeCustomizer authAuthorizeCustomizer;
    private OAuthServerAuthorizeCustomizer oAuthServerAuthorizeCustomizer;
    private AdminAuthorizeCustomizer adminAuthorizeCustomizer;

    private WebAuthnCustomizer webAuthnCustomizer;
    private FormLoginCustomizer formLoginCustomizer;
    private RememberMeCustomizer rememberMeCustomizer;
    private LogoutCustomizer logoutCustomizer;
    private OAuthServerExceptionHandlerCustomizer oAuthServerExceptionHandlerCustomizer;
    @Bean
    @Order(1)
    public SecurityFilterChain oauth2FilterChain(HttpSecurity http) throws Exception {
		http
			.oauth2AuthorizationServer((authorizationServer) -> {
				http.securityMatcher(authorizationServer.getEndpointsMatcher());
				authorizationServer
					.oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
					.clientRegistrationEndpoint(Customizer.withDefaults());
			})
			.authorizeHttpRequests((authorize) ->
				authorize
					.anyRequest().authenticated()
			)
			// Redirect to the login page when not authenticated from the
			// authorization endpoint
			.exceptionHandling(
                oAuthServerExceptionHandlerCustomizer
			);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http
    ) {
        http
        .authorizeHttpRequests(auth -> {
            webAuthnAuthorizeCustomizer.customize(auth);
            authAuthorizeCustomizer.customize(auth);
            oAuthServerAuthorizeCustomizer.customize(auth);
            adminAuthorizeCustomizer.customize(auth);
            auth.anyRequest().authenticated();
        })
        .formLogin( login -> {
            formLoginCustomizer.customize(login);
            login.defaultSuccessUrl("/", false);
        })
        .rememberMe(
            rememberMeCustomizer
        )
        .logout(
            logoutCustomizer
        )
        .webAuthn(
            webAuthnCustomizer
        );
        return http.build();
    }
}
