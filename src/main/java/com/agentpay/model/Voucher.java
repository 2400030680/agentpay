package com.agentpay.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brandId;
    private String brandName;
    private String voucherCode;
    private String voucherPin;
    private Double faceValue;
    private Double paidAmount;
    private Double savedAmount;
    private String phone;
    private String email;
    private String paymentId;
    private LocalDateTime createdAt;

    // 1. Default No-Arg Constructor (Required by JPA/Hibernate)
    public Voucher() {
        this.createdAt = LocalDateTime.now();
    }

    // 2. All-Args Constructor (Required by VoucherService)
    public Voucher(String customId, String phone, String brandId, String brandName, 
                   Double faceValue, Double paidAmount, Double savedAmount, 
                   String voucherCode, String voucherPin) {
        this.phone = phone;
        this.brandId = brandId;
        this.brandName = brandName;
        this.faceValue = faceValue;
        this.paidAmount = paidAmount;
        this.savedAmount = savedAmount;
        this.voucherCode = voucherCode;
        this.voucherPin = voucherPin;
        this.createdAt = LocalDateTime.now();
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getBrandId() { 
        return brandId; 
    }
    public void setBrandId(String brandId) { 
        this.brandId = brandId; 
    }

    public String getBrandName() { 
        return brandName; 
    }
    public void setBrandName(String brandName) { 
        this.brandName = brandName; 
    }

    public String getVoucherCode() { 
        return voucherCode; 
    }
    public void setVoucherCode(String voucherCode) { 
        this.voucherCode = voucherCode; 
    }

    public String getVoucherPin() { 
        return voucherPin; 
    }
    public void setVoucherPin(String voucherPin) { 
        this.voucherPin = voucherPin; 
    }

    public Double getFaceValue() { 
        return faceValue; 
    }
    public void setFaceValue(Double faceValue) { 
        this.faceValue = faceValue; 
    }

    public Double getPaidAmount() { 
        return paidAmount; 
    }
    public void setPaidAmount(Double paidAmount) { 
        this.paidAmount = paidAmount; 
    }

    public Double getSavedAmount() { 
        return savedAmount; 
    }
    public void setSavedAmount(Double savedAmount) { 
        this.savedAmount = savedAmount; 
    }

    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    // Alias methods for VoucherService compatibility
    public String getUserPhone() { 
        return phone; 
    }
    public void setUserPhone(String userPhone) { 
        this.phone = userPhone; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getUserEmail() { 
        return email; 
    }
    public void setUserEmail(String userEmail) { 
        this.email = userEmail; 
    }

    public String getPaymentId() { 
        return paymentId; 
    }
    public void setPaymentId(String paymentId) { 
        this.paymentId = paymentId; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}