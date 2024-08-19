package it.website.backend.controller;

import it.website.backend.model.*;
import it.website.backend.service.UserService;
import it.website.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // Remove if not used anymore


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractSubject(token.substring(7)); // Remove "Bearer " prefix
        User user = userService.getUserByUsername(username);
        if (user != null) {
            userService.deleteUser(user);
            return ResponseEntity.ok("User successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractSubject(token.substring(7)); // Remove "Bearer " prefix
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        String username = jwtUtil.extractSubject(token.substring(7)); // Remove "Bearer " prefix
        if (userService.changeUserPassword(username, request.getCurrentPassword(), request.getNewPassword())) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid current password");
        }
    }

    @PostMapping("/checkUsername")
    public ResponseEntity<?> checkUsername(@Valid @RequestBody UsernameRequest usernameRequest) {
        User existingUser = userService.getUserByUsername(usernameRequest.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("UsernameAlreadyPresent");
        }
        return ResponseEntity.ok("UsernameAvailable");
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(@Valid @RequestBody EmailRequest emailRequest) {
        User existingUser = userService.getUserByEmail(emailRequest.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("EmailAlreadyPresent");
        }
        return ResponseEntity.ok("EmailAvailable");
    }

    @PostMapping("/checkTelephone")
    public ResponseEntity<?> checkTelephone(@Valid @RequestBody TelephoneRequest telephoneRequest) {
        User existingUser = userService.getUserByTelephone(telephoneRequest.getTelephone());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("TelephoneAlreadyPresent");
        }
        return ResponseEntity.ok("TelephoneAvailable");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder("Validation errors: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessages.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
    }
}
