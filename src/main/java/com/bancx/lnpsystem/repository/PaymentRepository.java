package com.bancx.lnpsystem.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bancx.lnpsystem.model.Payment;

import jakarta.transaction.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PAYMENTS (payment_id, loan_id, payment_amount, payment_date) VALUES (:paymentId, :loanId, :paymentAmount, :paymentDate)", nativeQuery = true)
    void createPayment(@Param("paymentId") Long paymentId, @Param("loanId") Long loanId,
            @Param("paymentAmount") Double paymentAmount, @Param("paymentDate") Date paymentDate);

    @Query(value = "SELECT * FROM LOANS WHERE loan_id = ?", nativeQuery = true)
    Optional<List<Payment>> getAllPaymentByLoanId(@Param("laonId") Long laonId);
}
