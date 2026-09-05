package com.agentpay.model;

public class OrderRequest {
    private String productId;
    private String couponCode;
    private Double totalAmount; // Supports multi-item cart total

    public OrderRequest() {}

    public OrderRequest(String productId, String couponCode, Double totalAmount) {
        this.productId = productId;
        this.couponCode = couponCode;
        this.totalAmount = totalAmount;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}