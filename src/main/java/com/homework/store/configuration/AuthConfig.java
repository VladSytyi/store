package com.homework.store.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class AuthConfig {

    @Autowired
    private Environment environment;


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("admin realm");
        return entryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry.requestMatchers("/actuator/health").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint()))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        String user = environment.getProperty("auth.user");
        String pass = environment.getProperty("auth.password");
        User.UserBuilder builder = User.withUsername(user).password("{noop}" + pass).roles("USER");
        return new InMemoryUserDetailsManager(builder.build());
    }
}
