package com.carnegie.springsecurity.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwkSetUri}")
    private String jwkSetUri;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(
                        authrizeConfig->{
                            authrizeConfig.requestMatchers("/public").permitAll();
                            authrizeConfig.requestMatchers("/logout").permitAll();
                            authrizeConfig.anyRequest().authenticated();
                        }
                )
                //.formLogin(Customizer.withDefaults()) // add the default form. It is optional. Specially, when using microservices
                .oauth2Login(withDefaults())
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(
//                        //        jwtDecoder()
//                        NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()
//                )))
                .oauth2ResourceServer(conf->conf.jwt(withDefaults()))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

}
