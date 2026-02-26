package com.bancx.lnpsystem.service.loan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancx.lnpsystem.constants.ApplicationConstants;
import com.bancx.lnpsystem.dto.CreateLoanRequestDto;
import com.bancx.lnpsystem.dto.GetLoanDetailsDto;
import com.bancx.lnpsystem.enums.StatusEnum;
import com.bancx.lnpsystem.model.Loan;
import com.bancx.lnpsystem.repository.LoanRepository;
import com.bancx.lnpsystem.repository.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public non-sealed class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;

    @Value("${spring.datasource.url}")
    public String DB_URL;

    @Value("${spring.datasource.username}")
    public String USERNAME;

    @Value("${spring.datasource.password}")
    public String PASSWORD;

    private final String QUERY = "SELECT MAX(LOAN_ID) FROM LOANS";

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, PaymentRepository paymentRepository) {
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Creates a new loan record with the provided loan details.
     * 
     * @param createLoanRequestDto the data transfer object containing loan creation request details
     *                             including loanAmount and termInMonths
     * @return {@link ApplicationConstants#SUCCESS} if the loan is created successfully,
     *         null if an exception occurs during the creation process
     * 
     * @implNote This method generates a unique loan ID, initializes the loan with ACTIVE status,
     *           and persists it to the repository. Payment collected and balance amount are 
     *           initialized to 0.0.
     * 
     * @throws Logs error message if an exception occurs during loan creation
     */
    @Override
    public String creatLoan(CreateLoanRequestDto createLoanRequestDto) {

        try {
            long loanId = generateLoanId();
            Loan loan = new Loan(loanId, StatusEnum.ACTIVE.toString(), createLoanRequestDto.loanAmount(),
                    createLoanRequestDto.termInMonths(), 0.0, 0.0);

            loanRepository.createLaon(loan.getLoanId(), loan.getStatus(), loan.getLoanAmount(), loan.getTermInMonths(),
                    loan.getPaymentCollected(), loan.getBalanceAmount());

            return ApplicationConstants.SUCCESS;
        } catch (Exception e) {
            log.error(String.format("Exception encountered due to : %s", e.getMessage()));
            return null;
        }
    }

    /**
     * Retrieves loan details for a given loan ID.
     * 
     * <p>This method fetches loan information from the repository and returns it as a generic type.
     * If the loan is not found, it returns a NOT_FOUND constant. In case of any exception during
     * retrieval, the exception is printed and null is returned.</p>
     * 
     * @param <T> the generic return type for loan details
     * @param loanId the unique identifier of the loan to retrieve
     * @return an object of type T containing loan details (GetLoanDetailsDto), 
     *         ApplicationConstants.NOT_FOUND if loan not found, 
     *         or null if an exception occurs
     * 
     * @note Consider using a more specific return type instead of generic T to improve type safety
     *       and avoid unchecked cast warnings. Consider also implementing proper exception handling
     *       instead of catching generic Exception and returning null.
     */
    @Override
    public <T> T getLoanDetails(Long loanId) {
        try {
            Optional<Loan> loanDetails = loanRepository.getLoanDetails(loanId);

            if (loanDetails.isEmpty()) {
                return (T) ApplicationConstants.NOT_FOUND;
            }

            return (T) new GetLoanDetailsDto(loanDetails.get().getLoanId(), loanDetails.get().getStatus(),
                    loanDetails.get().getLoanAmount(), loanDetails.get().getBalanceAmount(),
                    loanDetails.get().getTermInMonths(), loanDetails.get().getPaymentCollected());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generates the next unique loan ID by querying the database for the maximum existing loan ID.
     */
    private Long generateLoanId() {

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

}
