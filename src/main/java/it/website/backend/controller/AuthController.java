package it.website.backend.controller;

import it.website.backend.exception.EmailNotVerifiedException;
import it.website.backend.exception.PasswordNotMatchingException;
import it.website.backend.exception.UsernameNotFoundException;
import it.website.backend.model.JwtResponse;
import it.website.backend.model.LoginRequest;
import it.website.backend.model.User;
import it.website.backend.service.AuthService;
import it.website.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil; // Remove if not used anymore

    @GetMapping("/generateTemporaryToken")
    public ResponseEntity<?> generateTemporaryToken(HttpSession session) {
        String sessionId = session.getId();
        String temporaryToken = jwtUtil.generateTemporaryToken(sessionId);
        return ResponseEntity.ok(new JwtResponse(temporaryToken));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        String registrationStatus = authService.registerUser(user);
        return ResponseEntity.ok(registrationStatus);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found: " + e.getMessage());
        } catch (PasswordNotMatchingException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (EmailNotVerifiedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/verifyEmail")
    public void verifyEmail(@RequestParam("userId") String userId,
                            @RequestParam("token") String token,
                            @RequestParam("signature") String signature,
                            @RequestParam("expires") long expires,
                            HttpServletResponse response) throws IOException {
        try {
            boolean isVerified = authService.verifyEmailToken(token, userId, signature, expires);
            if (isVerified) {
                response.sendRedirect("http://localhost:3000/login?verified=true"); // Consider making this configurable
            } else {
                response.sendRedirect("http://localhost:3000/registration?verified=false"); // Consider making this configurable
            }
        } catch (Exception e) {
            logger.error("Error during email verification", e);
            response.sendRedirect("http://localhost:3000/registration?verified=false");
        }
    }
}

