package com.online.busbooking.otp_service.repository;

import com.online.busbooking.otp_service.entity.OTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTPEntity,Integer> {
    OTPEntity findByReferenceNumber(String referenceNumber);
}
