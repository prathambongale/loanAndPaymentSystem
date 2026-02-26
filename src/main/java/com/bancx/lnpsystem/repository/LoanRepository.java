package com.bancx.lnpsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bancx.lnpsystem.model.Loan;

import jakarta.transaction.Transactional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO LOANS (loan_id, status, loan_amount, term_in_months, total_payment_collected, balance_amount) VALUES (:loanId, :status, :loanAmount, :termInMonths, :paymentCollected, :balanceAmount)", nativeQuery = true)
        void createLaon(@Param("loanId") Long loanId, @Param("status") String status,
                        @Param("loanAmount") Double loanAmount, @Param("termInMonths") Integer termInMonths,
                        @Param("paymentCollected") Double paymentCollected,
                        @Param("balanceAmount") Double balanceAmount);

        @Query(value = "SELECT * FROM LOANS WHERE loan_id = :loanId", nativeQuery = true)
        Optional<Loan> getLoanDetails(@Param("loanId") Long loanId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE LOANS SET total_payment_collected = :paymentCollected, balance_amount = :balanceAmount WHERE loan_id = :loanId", nativeQuery = true)
        void updateLoanRecord(@Param("loanId") Long loanId,
                        @Param("paymentCollected") Double paymentCollected,
                        @Param("balanceAmount") Double balanceAmount);

        @Modifying
        @Transactional
        @Query(value = "UPDATE LOANS SET total_payment_collected = :paymentCollected, balance_amount = :balanceAmount, status = :status WHERE loan_id = :loanId", nativeQuery = true)
        void updateLoanRecordWithStatus(@Param("loanId") Long loanId,
                        @Param("paymentCollected") Double paymentCollected,
                        @Param("balanceAmount") Double balanceAmount, 
                        @Param("status") String status);
}
