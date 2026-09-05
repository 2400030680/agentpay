package com.agentpay.service;

import org.springframework.stereotype.Service;

@Service
public class AgentService {

    public String processQuery(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hey there! 👋 How can I help you today?";
        }

        String msg = userMessage.toLowerCase().trim();

        // 1. Strict Pure Greetings
        if (msg.equals("hi") || msg.equals("hello") || msg.equals("hey") || msg.equals("good morning")) {
            return "Hey there! 👋 How can I help you today?";
        }

        // 2. Movies & Cinema Tickets
        if (msg.contains("movie") || msg.contains("bookmyshow") || msg.contains("pvr") || msg.contains("cinema")) {
            return "🎟️ **Cinema Offers Available:**\n" +
                   "• **BookMyShow:** Instant 10% OFF on all movie vouchers.\n" +
                   "• **PVR INOX:** Instant 12% OFF on tickets and F&B combos.\n" +
                   "Pay with Razorpay on the Brands tab to claim your instant voucher code!";
        }

        // 3. Food & Dining
        if (msg.contains("food") || msg.contains("zomato") || msg.contains("swiggy") || msg.contains("snack")) {
            return "🍕 **Food & Dining Discounts:**\n" +
                   "• **Zomato:** 8% Instant Discount\n" +
                   "• **Swiggy:** 10% Instant Discount\n" +
                   "• **Domino's Pizza:** 10% Instant Discount\n" +
                   "Voucher codes are credited instantly to your account upon Razorpay settlement!";
        }

        // 4. Shopping & Tech
        if (msg.contains("amazon") || msg.contains("flipkart") || msg.contains("apple") || msg.contains("myntra")) {
            return "🛍️ **E-Commerce Partner Rates:**\n" +
                   "• **Amazon Pay:** 10% Instant Discount\n" +
                   "• **Flipkart:** 10% Instant Discount\n" +
                   "• **Myntra:** 12% Instant Discount\n" +
                   "• **Apple Store:** 8% Instant Discount with No-Cost EMI!";
        }

        // 5. Cabs & Travel
        if (msg.contains("uber") || msg.contains("ola") || msg.contains("makemytrip") || msg.contains("travel")) {
            return "🚗 **Travel & Mobility Vouchers:**\n" +
                   "• **Uber Rides:** 8% Instant Discount\n" +
                   "• **MakeMyTrip:** 10% Instant Discount on flights & hotels.";
        }

        // Default Intelligent Help
        return "⚡ I can assist you with discounts across 35+ partner brands like **BookMyShow, Amazon, Zomato, and Uber**. Type any brand name or category to see active Razorpay rates!";
    }
}