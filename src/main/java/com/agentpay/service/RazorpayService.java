package com.agentpay.service;

import com.agentpay.model.OrderResponse;
import com.agentpay.model.PaymentVerification;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Value("${razorpay.currency:INR}")
    private String currency;

    // Creates an official Razorpay Order
    public OrderResponse createOrder(double amountInRupees) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        // Razorpay expects amount in smallest currency unit (paise: ₹1 = 100 paise)
        long amountInPaise = Math.round(amountInRupees * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());

        Order order = client.orders.create(orderRequest);

        return new OrderResponse(
                order.get("id").toString(),
                amountInRupees,
                currency,
                keyId
        );
    }

    // Verifies cryptographic HMAC SHA256 signature to prevent payment fraud
    public boolean verifySignature(PaymentVerification verification) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", verification.getRazorpayOrderId());
            options.put("razorpay_payment_id", verification.getRazorpayPaymentId());
            options.put("razorpay_signature", verification.getRazorpaySignature());

            return Utils.verifyPaymentSignature(options, keySecret);
        } catch (Exception e) {
            System.err.println("Signature verification failed: " + e.getMessage());
            return false;
        }
    }
}