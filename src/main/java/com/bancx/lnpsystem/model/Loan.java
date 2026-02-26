package com.bancx.lnpsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "loans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Loan {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "loan_id") 
    Long loanId;
    
    @Column(name = "status") 
    String status; 
    
    @Column(name = "loan_amount") 
    double loanAmount;
                
    @Column(name = "term_in_months") 
    int termInMonths;

    @Column(name = "total_payment_collected")
    double paymentCollected;

    @Column(name = "balance_amount")
    double balanceAmount;
}
