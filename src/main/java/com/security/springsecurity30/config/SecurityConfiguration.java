package com.security.springsecurity30.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{

        http.authorizeHttpRequests(authConfig -> {
            authConfig.requestMatchers(HttpMethod.GET, "/").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/user/**").hasRole("USER");
            authConfig.requestMatchers(HttpMethod.GET ,"/admin/**").hasRole("ADMIN");
            authConfig.anyRequest().authenticated();

        })  .csrf(AbstractHttpConfigurer::disable)
            .headers().frameOptions().disable()
            .and()
                //default configuration
            .formLogin(Customizer.withDefaults()) //Login with the browser and forms
            .httpBasic(Customizer.withDefaults()); //Login using postman
        return http.build();
    }

    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        return jdbcUserDetailsManager;
    }

    @Bean
    DataSource dataSource(){
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:testdb")
                .username("sa")
                .password("")
                .build();
    }

}
