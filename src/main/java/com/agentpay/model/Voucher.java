package com.agentpay.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "vouchers")
public class Voucher {

    @Id
    private String id;
    private String userPhone;
    private String brandId;
    private String brandName;
    private double faceValue;
    private double paidAmount;
    private double savedAmount;
    private String voucherCode;
    private String voucherPin;
    private String status;
    private String createdAt;

    public Voucher() {}

    // 9-argument Constructor (Matches VoucherService.java)
    public Voucher(String id, String userPhone, String brandId, String brandName, double faceValue, 
                   double paidAmount, double savedAmount, String voucherCode, String voucherPin) {
        this.id = id;
        this.userPhone = userPhone;
        this.brandId = brandId;
        this.brandName = brandName;
        this.faceValue = faceValue;
        this.paidAmount = paidAmount;
        this.savedAmount = savedAmount;
        this.voucherCode = voucherCode;
        this.voucherPin = voucherPin;
        this.status = "ACTIVE";
        this.createdAt = LocalDate.now().toString();
    }

    // 8-argument Constructor (Matches PaymentController.java)
    public Voucher(String id, String userPhone, String brandName, double faceValue, 
                   double paidAmount, double savedAmount, String voucherCode, String voucherPin) {
        this(id, userPhone, "brand", brandName, faceValue, paidAmount, savedAmount, voucherCode, voucherPin);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public String getBrandId() { return brandId; }
    public void setBrandId(String brandId) { this.brandId = brandId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public double getFaceValue() { return faceValue; }
    public void setFaceValue(double faceValue) { this.faceValue = faceValue; }

    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }

    public double getSavedAmount() { return savedAmount; }
    public void setSavedAmount(double savedAmount) { this.savedAmount = savedAmount; }

    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    public String getVoucherPin() { return voucherPin; }
    public void setVoucherPin(String voucherPin) { this.voucherPin = voucherPin; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}