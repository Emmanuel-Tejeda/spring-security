package com.security.springsecurity30.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{

        http.authorizeHttpRequests(authConfig -> {
            authConfig.requestMatchers(HttpMethod.GET, "/").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/user").hasAnyAuthority("ROLE_USER");
            authConfig.requestMatchers(HttpMethod.GET ,"/admin/**").hasRole("ADMIN");
            authConfig.anyRequest().authenticated();

        })  .csrf(AbstractHttpConfigurer::disable)
            .headers().frameOptions().disable()
            .and()
                //default configuration
            .formLogin(Customizer.withDefaults()) //Login with the browser and forms
            .httpBasic(Customizer.withDefaults());


        return http.build();
    }


    @Bean
    UserDetailsService myUSerDetailsService(){
        return new MyUserDetailsService();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    /*
    Listens successful login attempt meassages
     */
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successEVent(){
        return event -> {
            System.err.println("Success Login " + event.getAuthentication().getClass().getName() + " - " + event.getAuthentication().getName());
        };
    }

    /*
    Listens for bad log in attemps
     */
    @Bean
    public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureEvent(){
        return event -> {
            System.err.println("Bad Credentials Login " + event.getAuthentication().getClass().getName() + " - " + event.getAuthentication());
        };
    }



}
