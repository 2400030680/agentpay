package com.agentpay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    // In-memory OTP storage
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody Map<String, String> payload) {
        String phone = payload.get("phone");
        Map<String, Object> res = new HashMap<>();

        if (phone == null || phone.length() < 10) {
            res.put("status", "ERROR");
            res.put("message", "Invalid mobile number");
            return ResponseEntity.badRequest().body(res);
        }

        // Demo OTP is 1234
        String otp = "1234";
        otpStore.put(phone, otp);

        res.put("status", "SUCCESS");
        res.put("message", "OTP sent successfully");
        res.put("phone", phone);
        res.put("demoOtp", "1234");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> payload) {
        String phone = payload.get("phone");
        String otp = payload.get("otp");
        String name = payload.getOrDefault("name", "Dileep Kumar");
        Map<String, Object> res = new HashMap<>();

        String expectedOtp = otpStore.getOrDefault(phone, "1234");

        if (expectedOtp.equals(otp) || "1234".equals(otp)) {
            res.put("status", "SUCCESS");
            res.put("message", "Authentication successful");

            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("phone", phone);
            user.put("memberTier", "Razorpay Plus");
            res.put("user", user);

            return ResponseEntity.ok(res);
        } else {
            res.put("status", "ERROR");
            res.put("message", "Invalid OTP entered");
            return ResponseEntity.badRequest().body(res);
        }
    }
}