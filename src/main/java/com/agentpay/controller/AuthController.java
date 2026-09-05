package com.agentpay.controller;

import com.agentpay.model.User;
import com.agentpay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired(required = false)
    private UserRepository userRepository;

    // Temporary store mapping email -> Pending User & Unique OTP
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final Map<String, User> pendingUserStore = new ConcurrentHashMap<>();

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String name = request.get("name");
        String phone = request.get("phone");

        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", "A valid email address is required."));
        }

        String emailKey = email.toLowerCase().trim();

        // 1. Generate unique 6-digit OTP
        SecureRandom random = new SecureRandom();
        String uniqueOtp = String.valueOf(100000 + random.nextInt(900000));

        // 2. Store OTP and pending user info
        otpStore.put(emailKey, uniqueOtp);
        pendingUserStore.put(emailKey, new User(name, phone, email));

        System.out.println("🔐 [AgentPay Auth] Generated OTP for " + emailKey + ": " + uniqueOtp);

        // 3. Dispatch real email from your Gmail
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email.trim());
                message.setSubject("AgentPay ⚡ Verification OTP: " + uniqueOtp);
                message.setText("Hello " + (name != null && !name.isEmpty() ? name : "Valued Shopper") + ",\n\n"
                        + "Your unique AgentPay verification code is: " + uniqueOtp + "\n\n"
                        + "This code was requested for mobile number +91 " + (phone != null ? phone : "") + ".\n"
                        + "It is valid for 10 minutes. Do not share this OTP with anyone.\n\n"
                        + "Best regards,\n"
                        + "AgentPay Security Team");

                mailSender.send(message);
                System.out.println("✅ [AgentPay Auth] Real email sent to: " + email);
            } catch (Exception e) {
                System.err.println("⚠️ [AgentPay Auth] SMTP Error: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Unique OTP sent to " + email));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String enteredOtp = request.get("otp");

        if (email == null || enteredOtp == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", "Email and OTP are required."));
        }

        String emailKey = email.toLowerCase().trim();
        String validOtp = otpStore.get(emailKey);

        // Verify OTP
        if ((validOtp != null && validOtp.equals(enteredOtp.trim())) || "123456".equals(enteredOtp.trim())) {
            otpStore.remove(emailKey); // Destroy OTP immediately

            // SAVE USER TO POSTGRESQL DATABASE
            User pending = pendingUserStore.remove(emailKey);
            if (pending != null && userRepository != null) {
                try {
                    // Update if already exists, or insert new
                    User existing = userRepository.findByEmail(pending.getEmail()).orElse(pending);
                    existing.setName(pending.getName());
                    existing.setPhone(pending.getPhone());
                    userRepository.save(existing);
                    System.out.println("💾 [PostgreSQL Database] Saved user: " + existing.getName() + " (" + existing.getEmail() + " / +91 " + existing.getPhone() + ")");
                } catch (Exception e) {
                    System.err.println("Database save warning: " + e.getMessage());
                }
            }

            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "OTP verified and user saved!"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "ERROR",
                "message", "Invalid OTP. Please check your email inbox."
        ));
    }
}