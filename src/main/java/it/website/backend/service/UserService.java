package it.website.backend.service;

import it.website.backend.model.User;
import it.website.backend.repository.UserRepository;
import it.website.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Value("${app.secret-key}")
    private String secretKey;  // Injected secret key for signature generation

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtUtil jwtUtil;

    // Change user password
    public boolean changeUserPassword(String username, String currentPassword, String newPassword) {
        User user = getUserByUsername(username);
        if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // Find user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Find user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find user by telephone
    public User getUserByTelephone(String telephone) {
        return userRepository.findByTelephone(telephone);
    }

    // Delete a user
    public void deleteUser(User user) {
        userRepository.delete(user);
        logger.info("User {} deleted successfully", user.getUsername());
    }
}