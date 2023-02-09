package com.security.springsecurity30;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager, DataSource dataSource){
		return args -> {
			UserDetails user = User.builder()
					.username("user")
					.password("password")
					.roles("USER")
					.build();

			UserDetails admin = User.builder()
					.username("admin")
					.password("password")
					.roles("USER, ADMIN")
					.build();

			JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

			users.createUser(user);
			users.createUser(admin);
		};
	}

}
