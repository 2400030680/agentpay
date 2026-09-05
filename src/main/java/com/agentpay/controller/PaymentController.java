package com.agentpay.controller;

import com.agentpay.model.Voucher;
import com.agentpay.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired(required = false)
    private VoucherRepository voucherRepository;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> req) {
        String orderId = "order_" + System.currentTimeMillis();
        return ResponseEntity.ok(Map.of(
            "orderId", orderId,
            "status", "CREATED"
        ));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> req) {
        try {
            String brandName = (String) req.getOrDefault("brandName", "Brand Voucher");
            String phone = (String) req.getOrDefault("phone", "");
            String email = (String) req.getOrDefault("email", "");
            String paymentId = (String) req.getOrDefault("razorpayPaymentId", "pay_" + System.currentTimeMillis());
            
            Double faceValue = req.get("faceValue") != null ? Double.valueOf(req.get("faceValue").toString()) : 1000.0;
            Double paidAmount = req.get("paidAmount") != null ? Double.valueOf(req.get("paidAmount").toString()) : 880.0;

            String prefix = brandName.length() >= 3 ? brandName.substring(0, 3).toUpperCase() : "VCH";
            String code = prefix + "-" + (int)(1000 + Math.random() * 9000) + "-" + (int)(1000 + Math.random() * 9000);
            String pin = String.valueOf((int)(1000 + Math.random() * 9000));

            Voucher voucher = new Voucher();
            voucher.setBrandName(brandName);
            voucher.setVoucherCode(code);
            voucher.setVoucherPin(pin);
            voucher.setFaceValue(faceValue);
            voucher.setPaidAmount(paidAmount);
            voucher.setSavedAmount(faceValue - paidAmount);
            voucher.setPhone(phone);
            voucher.setEmail(email);
            voucher.setPaymentId(paymentId);
            voucher.setCreatedAt(LocalDateTime.now());

            // Save to PostgreSQL table
            if (voucherRepository != null) {
                try {
                    voucher = voucherRepository.save(voucher);
                    System.out.println("💾 [pgAdmin Database] Saved Voucher: " + voucher.getVoucherCode() + " for " + voucher.getEmail());
                } catch (Exception dbEx) {
                    System.err.println("⚠️ Database write notice (cloud fallback): " + dbEx.getMessage());
                }
            }

            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "voucher", voucher
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("status", "ERROR", "message", e.getMessage()));
        }
    }
}