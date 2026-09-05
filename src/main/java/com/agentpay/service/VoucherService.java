package com.agentpay.service;

import com.agentpay.model.Voucher;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    private final List<Voucher> voucherStore = Collections.synchronizedList(new ArrayList<>());
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public Voucher issueVoucher(String phone, String brandId, String brandName, double faceValue, double paidAmount) {
        String code = generateCode(brandId.substring(0, Math.min(3, brandId.length())).toUpperCase());
        String pin = String.valueOf(1000 + random.nextInt(9000));
        double savedAmount = faceValue - paidAmount;

        Voucher voucher = new Voucher(
            "VOUCH_" + System.currentTimeMillis(),
            phone,
            brandId,
            brandName,
            faceValue,
            paidAmount,
            savedAmount,
            code,
            pin
        );

        voucherStore.add(voucher);
        return voucher;
    }

    public List<Voucher> getVouchersForUser(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return voucherStore.stream()
                .filter(v -> phone.equals(v.getUserPhone()))
                .collect(Collectors.toList());
    }

    private String generateCode(String prefix) {
        return prefix + "-" + randomSegment(4) + "-" + randomSegment(4) + "-" + randomSegment(4);
    }

    private String randomSegment(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}