package it.website.backend.service;

import it.website.backend.exception.EmailNotVerifiedException;
import it.website.backend.exception.PasswordNotMatchingException;
import it.website.backend.exception.UsernameNotFoundException;
import it.website.backend.model.LoginRequest;
import it.website.backend.model.User;
import it.website.backend.repository.UserRepository;
import it.website.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Value("${app.secret-key}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtUtil jwtUtil;

    // Register a new user
    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return sendVerificationEmail(user);
    }

    // Authenticate user and generate JWT token
    public String authenticateUser(LoginRequest loginRequest) throws UsernameNotFoundException, PasswordNotMatchingException, EmailNotVerifiedException {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        User user = userRepository.findByUsername(username);
        logger.info("Attempting authentication for user: {}", username);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                if (user.getEmailVerified()) {
                    return jwtUtil.generateToken(user.getUsername());
                } else {
                    logger.info("User not verified email");
                    throw new EmailNotVerifiedException("Email not verified for user: " + username);
                }
            } else {
                throw new PasswordNotMatchingException("Password does not match for username: " + username);
            }
        } else {
            throw new UsernameNotFoundException("Username not found: " + username);
        }
    }

    // Generate a secure token and signed URL
    public String generateVerificationLink(String userId) {
        try {
            String token = UUID.randomUUID().toString();
            long expirationTime = Instant.now().plus(Duration.ofHours(1)).getEpochSecond();
            String signature = generateSignature(token, userId, expirationTime);

            return String.format("http://localhost:8080/api/auth/verifyEmail?userId=%s&token=%s&signature=%s&expires=%d",
                    userId, token, signature, expirationTime);
        } catch (Exception e) {
            logger.error("Error generating verification link", e);
            throw new RuntimeException("Error generating verification link", e);
        }
    }

    // Generate HMAC SHA-256 signature
    private String generateSignature(String token, String userId, long expirationTime) throws Exception {
        String data = String.format("%s|%s|%d", token, userId, expirationTime);
        Mac mac = Mac.getInstance("HmacSHA256");
        Key key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        mac.init(key);
        byte[] signatureBytes = mac.doFinal(data.getBytes());
        return Base64.getUrlEncoder().encodeToString(signatureBytes);
    }

    // Send email with verification link
    public String sendVerificationEmail(User user) {
        try {
            String verificationLink = generateVerificationLink(user.getId());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Email Verification");

            String htmlContent = "<html>" +
                    "<body>" +
                    "<p>To verify your email, please click the button below:</p>" +
                    "<a href=\"" + verificationLink + "\" " +
                    "style=\"display: inline-block; padding: 10px 20px; font-size: 16px; font-weight: bold; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px;\">Verify Email</a>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);

            return "Verification email sent!";
        } catch (MessagingException e) {
            logger.error("Failed to send verification email", e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    // Verify the email token
    public boolean verifyEmailToken(String token, String userId, String signature, long expires) {
        try {
            if (Instant.now().getEpochSecond() > expires) {
                return false; // Token expired
            }

            String expectedSignature = generateSignature(token, userId, expires);
            if (!signature.equals(expectedSignature)) {
                return false; // Invalid signature
            }

            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setEmailVerified(true);
                userRepository.save(user);
                return true;
            } else {
                return false; // User not found
            }
        } catch (Exception e) {
            logger.error("Failed to verify email token", e);
            throw new RuntimeException("Failed to verify email token: " + e.getMessage());
        }
    }
}