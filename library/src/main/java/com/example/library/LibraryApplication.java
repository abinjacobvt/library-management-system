package com.example.library;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	// ADD THIS BELOW
	@Bean
	CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
		return args -> {
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(encoder.encode("admin123"));
			admin.setRole("ROLE_ADMIN");
			repo.save(admin);
		};
	}
}