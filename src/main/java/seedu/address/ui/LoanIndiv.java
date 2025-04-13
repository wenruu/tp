package seedu.address.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Loan;

/**
 * UI component that represents a single Loan in the UI.
 */
public class LoanIndiv extends UiPart<VBox> {
    private static final String FXML = "LoanCard.fxml";

    public final Loan loan;

    private final ObservableList<Loan> loans;

    @FXML
    private Label loanType;
    @FXML
    private Label overdue;
    @FXML
    private Label principal;
    @FXML
    private Label interest;
    @FXML
    private Label dateCreated;
    @FXML
    private Label dueDate;
    @FXML
    private Label remainder;
    @FXML
    private Label interestAccrued;
    @FXML
    private Label amountOwed;
    @FXML
    private Label monthlyInstallment;
    @FXML
    private Label lastPaid;
    @FXML
    private Label amtPaid;
    @FXML
    private Label monthsOverdue;
    @FXML
    private Label monthsUntilDue;

    /**
     * Create a loanindiv controller
     */
    public LoanIndiv(ObservableList<Loan> loans, Loan loan) {
        super(FXML);
        this.loan = loan;
        this.loans = loans;
        Platform.runLater(() -> {
            int index = this.loans.indexOf(loan) + 1;
            loanType.setText(index + ". " + "Loan Type: " + loan.getName());

            boolean isOverdue = loan.isOverDue();

            if (isOverdue) {
                overdue.setText("Overdue");
                overdue.setStyle("-fx-text-fill: red;"); // Set text color to red
            } else {
                overdue.setText("Not Overdue");
                overdue.setStyle("-fx-text-fill: green;"); // Default color
            }


            // 1st 4
            principal.setText("Principal: $" + String.format("%.2f", loan.getPrincipal()));
            interest.setText("Interest: " + String.format("%.2f", loan.getInterest()) + "%");
            dateCreated.setText("Date Created: " + loan.getDateCreated());
            dueDate.setText("Due Date: " + loan.getDueDate());

            // 2nd 4
            float remainingOwed = loan.getRemainingOwed();
            remainder.setText("Remaning Owed: $" + String.format("%.2f", remainingOwed));

            float loanCost = loan.getAmountOwed();
            amountOwed.setText("Total Loan Cost: $" + String.format("%.2f", loanCost));

            float monthlyInstalment = loan.getMonthlyInstalmentAmount();
            monthlyInstallment.setText("Monthly Instalment: $" + String.format("%.2f", monthlyInstalment));

            float accruedInterest = loan.getAmountOwed() - loan.getPrincipal();
            interestAccrued.setText("Total Interest: $" + String.format("%.2f", accruedInterest));

            //3rd 4
            lastPaid.setText("Last Paid: "
                + (loan.getDateLastPaid() != null ? loan.getDateLastPaid().toString() : "Not Paid Yet"));
            amtPaid.setText("Amount Paid: $" + String.format("%.2f", loan.getAmtPaid()));

            float paymentDifference = loan.getPaymentDifference();
            monthsOverdue.setText("Past instalments: "
                    + (paymentDifference > 0 ? "Missed $" + String.format("%.2f", paymentDifference)
                    : paymentDifference == 0 ? "All paid"
                    : "Overpaid $" + String.format("%.2f", -paymentDifference)));

            int monthsUntilDueDate = loan.getMonthsUntilDueDate();
            monthsUntilDue.setText(monthsUntilDueDate >= 0
                    ? "Months until due date: " + monthsUntilDueDate
                    : "Months overdue: " + Math.abs(monthsUntilDueDate));

        });
    }
}

