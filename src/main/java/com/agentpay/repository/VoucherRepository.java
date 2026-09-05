package com.agentpay.repository;

import com.agentpay.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    
    // Search vouchers by customer phone number
    List<Voucher> findByPhone(String phone);
    
    // Search vouchers by customer email
    List<Voucher> findByEmail(String email);
}