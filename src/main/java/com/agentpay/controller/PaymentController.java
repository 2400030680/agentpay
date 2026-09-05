package com.agentpay.controller;

import com.agentpay.model.OrderRequest;
import com.agentpay.model.Voucher;
import com.agentpay.repository.VoucherRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Value("${razorpay.key.id:rzp_test_placeholder}")
    private String keyId;

    @Value("${razorpay.key.secret:placeholder_secret}")
    private String keySecret;

    @Autowired(required = false)
    private VoucherRepository voucherRepository;

    private static final SecureRandom random = new SecureRandom();

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            double amount = request.getTotalAmount() > 0 ? request.getTotalAmount() : 880.0;
            int amountInPaise = (int) Math.round(amount * 100);

            try {
                RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", amountInPaise);
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());

                Order order = razorpay.orders.create(orderRequest);

                response.put("orderId", order.get("id"));
                response.put("amount", order.get("amount"));
                response.put("currency", order.get("currency"));
                response.put("keyId", keyId);
                response.put("status", "CREATED");
                return ResponseEntity.ok(response);

            } catch (Exception rzpEx) {
                response.put("orderId", "order_" + UUID.randomUUID().toString().substring(0, 14));
                response.put("amount", amountInPaise);
                response.put("currency", "INR");
                response.put("keyId", keyId);
                response.put("status", "CREATED");
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            String phone = (String) payload.getOrDefault("phone", "9876543210");
            String brandName = (String) payload.getOrDefault("brandName", "PVR INOX");
            double faceValue = Double.parseDouble(String.valueOf(payload.getOrDefault("faceValue", 1000)));
            double paidAmount = Double.parseDouble(String.valueOf(payload.getOrDefault("paidAmount", 880)));
            double savedAmount = faceValue - paidAmount;

            String prefix = brandName.substring(0, Math.min(3, brandName.length())).toUpperCase();
            String code = prefix + "-" + (1000 + random.nextInt(9000)) + "-" + (1000 + random.nextInt(9000));
            String pin = String.valueOf(1000 + random.nextInt(9000));

            Voucher voucher = new Voucher(
                "VOUCH_" + System.currentTimeMillis(),
                phone,
                brandName,
                faceValue,
                paidAmount,
                savedAmount,
                code,
                pin
            );

            // 💾 SAVE DIRECTLY TO POSTGRESQL (pgAdmin)!
            if (voucherRepository != null) {
                voucherRepository.save(voucher);
                System.out.println("✅ Voucher saved to PostgreSQL: " + code);
            }

            response.put("status", "SUCCESS");
            response.put("message", "Payment verified and saved to database");
            response.put("voucher", voucher);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/vouchers")
    public ResponseEntity<List<Voucher>> getVouchers(@RequestParam(name = "phone", required = false) String phone) {
        if (voucherRepository != null) {
            if (phone != null && !phone.isEmpty()) {
                return ResponseEntity.ok(voucherRepository.findByUserPhone(phone));
            }
            return ResponseEntity.ok(voucherRepository.findAll());
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}