package com.tickets.ticketingsystem.config;

import com.tickets.ticketingsystem.model.Role;
import com.tickets.ticketingsystem.model.User;
import com.tickets.ticketingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Checking for initial user data...");

        // Check for and create ADMIN User if they don't exist
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin user created.");
        }

        // Check for and create SUPPORT_AGENT User if they don't exist
        if (userRepository.findByEmail("support@example.com").isEmpty()) {
            User support = new User();
            support.setName("Support User");
            support.setEmail("support@example.com");
            support.setPassword(passwordEncoder.encode("support"));
            support.setRole(Role.SUPPORT_AGENT);
            userRepository.save(support);
            System.out.println("Default support agent user created.");
        }

        // Check for and create REGULAR User if they don't exist
        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User user = new User();
            user.setName("Regular User");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRole(Role.USER);
            userRepository.save(user);
            System.out.println("Default regular user created.");
        }
        
        System.out.println("Initial user data check complete.");
    }
}
