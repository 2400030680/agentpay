package com.agentpay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        Map<String, String> response = new HashMap<>();

        if (message == null || message.trim().isEmpty()) {
            response.put("message", "Hey there! 👋 How can I help you today?");
            return ResponseEntity.ok(response);
        }

        String msg = message.toLowerCase().trim();

        // 1. Pure Greetings
        if (msg.equals("hi") || msg.equals("hello") || msg.equals("hey") || msg.equals("good morning") || msg.equals("good evening")) {
            response.put("message", "Hey there! 👋 How can I help you today? Ask me about discounts on BookMyShow, Amazon, Zomato, or Uber!");
            return ResponseEntity.ok(response);
        }

        // 2. Gratitude & Thank You Handling
        if (msg.contains("thank") || msg.contains("thx") || msg.contains("tq") || msg.equals("ok thanks") || msg.equals("okay thanks") || msg.equals("k thanks")) {
            response.put("message", "No problem at all! 😊 Always here to help you save. Let me know if you have any further queries or want to explore more brand deals!");
            return ResponseEntity.ok(response);
        }

        // 3. Movies & Cinema Tickets
        if (msg.contains("movie") || msg.contains("bookmyshow") || msg.contains("pvr") || msg.contains("cinema") || msg.contains("cinepolis")) {
            response.put("message", "🎟️ **Cinema Offers Available:**\n• **BookMyShow:** 1-10% Instant Discount\n• **PVR INOX:** 2-15% Instant Discount\n• **Cinépolis:** 2-12% Instant Discount\nSelect any cinema voucher on the directory to pay via Razorpay!");
            return ResponseEntity.ok(response);
        }

        // 4. Food & Dining
        if (msg.contains("food") || msg.contains("zomato") || msg.contains("swiggy") || msg.contains("dominos") || msg.contains("pizza") || msg.contains("starbucks")) {
            response.put("message", "🍕 **Dining Discounts:**\n• **Zomato:** 2-10% OFF\n• **Swiggy:** 2-12% OFF\n• **Domino's Pizza:** 3-12% OFF\n• **Starbucks:** 2-8% OFF\nVoucher codes are instantly issued upon checkout!");
            return ResponseEntity.ok(response);
        }

        // 5. Shopping & E-Commerce
        if (msg.contains("amazon") || msg.contains("flipkart") || msg.contains("myntra") || msg.contains("apple") || msg.contains("croma") || msg.contains("shopping")) {
            response.put("message", "🛍️ **Shopping & Tech Partner Rates:**\n• **Amazon:** 1-12% OFF\n• **Flipkart:** 2-12% OFF\n• **Myntra:** 4-13% OFF\n• **Apple Store:** 2-8% OFF\n• **Croma:** 2-10% OFF\nAll vouchers come with instant PINs and 1-year validity!");
            return ResponseEntity.ok(response);
        }

        // 6. Travel & Cabs
        if (msg.contains("uber") || msg.contains("ola") || msg.contains("makemytrip") || msg.contains("flight") || msg.contains("travel") || msg.contains("cab")) {
            response.put("message", "🚗 **Travel & Mobility Vouchers:**\n• **Uber Rides:** 2-8% OFF\n• **Ola Cabs:** 2-8% OFF\n• **MakeMyTrip:** 2-12% OFF on flights & hotels\nPay with Razorpay to claim your travel pass!");
            return ResponseEntity.ok(response);
        }

        // 7. Default Helpful Response
        response.put("message", "⚡ I can assist you with discounts across 42+ partner brands like **BookMyShow, Amazon, Zomato, and Uber**. Type any brand name to see active rates!");
        return ResponseEntity.ok(response);
    }
}