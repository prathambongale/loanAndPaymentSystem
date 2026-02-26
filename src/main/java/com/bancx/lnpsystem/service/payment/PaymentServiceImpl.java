package com.bancx.lnpsystem.service.payment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancx.lnpsystem.constants.ApplicationConstants;
import com.bancx.lnpsystem.dto.CreatePaymentRequestDto;
import com.bancx.lnpsystem.enums.StatusEnum;
import com.bancx.lnpsystem.exceptions.CustomeException;
import com.bancx.lnpsystem.model.Loan;
import com.bancx.lnpsystem.model.Payment;
import com.bancx.lnpsystem.repository.LoanRepository;
import com.bancx.lnpsystem.repository.PaymentRepository;
import com.bancx.lnpsystem.util.UtilCO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public non-sealed class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;
    private final UtilCO utilCO;

    @Value("${spring.datasource.url}")
    public String DB_URL;

    @Value("${spring.datasource.username}")
    public String USERNAME;

    @Value("${spring.datasource.password}")
    public String PASSWORD;

    private final String QUERY = "SELECT MAX(LOAN_ID) FROM PAYMENTS";

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, LoanRepository loanRepository, UtilCO utilCO) {
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;
        this.utilCO = utilCO;
    }

    /**
     * Creates a payment record for a given loan.
     * 
     * This method processes a payment request by validating the loan existence,
     * performing business validations on the payment amount against the loan balance,
     * and updating both the payment and loan records.
     *
     * @param createPaymentRequestDto the payment request containing loan ID and payment amount
     * @return {@link ApplicationConstants#SUCCESS} if payment is created successfully,
     *         {@link ApplicationConstants#NOT_FOUND} if the loan does not exist,
     *         error message from {@link CustomeException} if business validation fails,
     *         or null if an unexpected exception occurs
     * 
     * @throws CustomeException if business validations fail (e.g., payment amount exceeds balance)
     * 
     * @see CreatePaymentRequestDto
     * @see Loan
     * @see ApplicationConstants
     */
    @Override
    public String createPayment(CreatePaymentRequestDto createPaymentRequestDto) {
        try {

            var loanId = createPaymentRequestDto.loandId();
            var paymentAmount = createPaymentRequestDto.paymentAmt();

            Optional<Loan> loanDetails = loanRepository.getLoanDetails(loanId);

            if (loanDetails.isEmpty()) {
                return ApplicationConstants.NOT_FOUND;
            }

            double balanceAmount = loanDetails.get().getBalanceAmount();

            doBusinessValidations(paymentAmount, balanceAmount);

            long paymentId = generatePaymentId();

            createPaymentRecord(paymentId, loanId, paymentAmount);

            updateLoanRecord(loanDetails.get().getLoanAmount(), loanDetails.get().getPaymentCollected(), paymentAmount,
                    loanId);

            return ApplicationConstants.SUCCESS;

        } catch (CustomeException customeException) {
            log.error(String.format("Business Validation error occured : %s", customeException.getMessage()));
            return customeException.getMessage();
        } catch (Exception e) {
            log.error(String.format("Exception encountered due to : %s", e.getMessage()));
            return null;
        }
    }

    private void doBusinessValidations(double paymentAmount, double balanceAmount) throws CustomeException {
        double roundedBalanceAmount = utilCO.roundValues(balanceAmount);

        if (roundedBalanceAmount == 0) {
            throw new CustomeException("The loan is already paid in full.");
        }

        if (paymentAmount > roundedBalanceAmount) {
            throw new CustomeException(
                    String.format("Amount paid is greater than balanced loan amount. Pay %f or less.",
                            roundedBalanceAmount));
        }
    }

    private Long generatePaymentId() {

        long nextId = 1;

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY)) {

            if (resultSet.next()) {
                var currentMaxLoanId = resultSet.getLong(1);
                nextId = currentMaxLoanId + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextId;
    }

    private void createPaymentRecord(Long paymentId, Long loanId, double paymentAmount) {
        Payment payment = new Payment(
                paymentId,
                loanId,
                paymentAmount,
                new Date());

        paymentRepository.createPayment(
                payment.getPaymentId(),
                payment.getLoanId(),
                payment.getPaymentAmount(),
                payment.getPaymentDate());
    }

    private void updateLoanRecord(double loanAmount, double paymentCollected, double paymentAmount, long loanId) {
        var newPaymentCollected = paymentCollected + paymentAmount;
        var newBalanceAmount = loanAmount - newPaymentCollected;

        if (newBalanceAmount == 0) {
            loanRepository.updateLoanRecordWithStatus(loanId, newPaymentCollected, newBalanceAmount, StatusEnum.SETTLED.toString());
        } else {
            loanRepository.updateLoanRecord(loanId, newPaymentCollected, newBalanceAmount);
        }
    }
}
