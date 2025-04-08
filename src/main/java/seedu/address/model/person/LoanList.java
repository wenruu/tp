package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Messages;

/**
 * Represents a loan list containing the loans a person has.
 */
public class LoanList {
    public static final String EMPTY_STRING = "EMPTY";

    //private final ArrayList<Loan> loanList = new ArrayList<>();

    private final ObservableList<Loan> internalList = FXCollections.observableArrayList();
    private final FilteredList<Loan> filteredList = new FilteredList<>(internalList);
    private final ObservableList<Loan> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(filteredList);

    /**
     * Initializes a LoanList.
     */
    public LoanList() {}

    /**
     * Adds a loan to the loan list.
     */
    public void add(Loan loan) {
        internalList.add(loan);
    }

    /**
     * filters loanlist
     */
    public void filter(LoanPredicate pred) {
        if (pred == null) {
            filteredList.setPredicate(null);
        } else {
            filteredList.setPredicate(loan -> pred.test(loan));
        }
    }

    /**
     * Returns an immutable loan list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public ObservableList<Loan> getLoans() {
        return internalUnmodifiableList;
    }

    /**
     * Removes a loan from loan list.
     */
    public void remove(Loan loan) {
        internalList.remove(loan);
    }

    public void remove(int zeroBasedIndex) {
        internalList.remove(zeroBasedIndex);
    }

    /**
     * Pays a loan and removes it if it is fully paid.
     */
    public void payLoan(int index, float amount) throws IllegalValueException {
        Loan loanToPay = internalList.get(index);
        loanToPay.pay(amount);

        if (loanToPay.isPaid()) {
            internalList.remove(index); // Automatically notifies ListView
        } else {
            internalList.set(index, loanToPay); // Replaces with same object (but triggers update)
        }
    }

    /**
     * Returns most overdue loan amount in float(months)
    */
    public float getMostOverdueMonths() {
        float overdue = Float.NEGATIVE_INFINITY;

        for (Loan loan : internalList) {
            float loanOverdue = loan.getMissedInstalmentsMonthsPrecise();
            if (loanOverdue > overdue) {
                overdue = loanOverdue;
            }
        }
        return overdue;
    }

    /**
     * Returns total value of all loans
    */
    public float getTotalLoanOwed() {
        float owed = 0;

        for (Loan loan : internalList) {
            owed += loan.getAmountOwed();
        }
        return owed;
    }

    /**
     * Returns a list of loans filtered by their paid status.
     *
     * @param isPaid If true, returns loans that are marked as paid;
     *               if false, returns loans that are not fully paid.
     * @return A list of loans matching the specified paid status.
     */
    public List<Loan> filterLoansByPaidStatus(boolean isPaid) {
        List<Loan> filteredLoans = new ArrayList<>();
        for (Loan loan : internalList) {
            if (loan.isPaid() == isPaid) {
                filteredLoans.add(loan);
            }
        }
        return filteredLoans;
    }

    /**
     * creates a loan list string where each loan string is sepearated by ','
    */
    public String toSaveString() {
        if (this.internalList.isEmpty()) {
            return EMPTY_STRING;
        }

        StringBuilder loanList = new StringBuilder();
        for (Loan loan : this.internalList) {
            loanList.append(loan.toSaveString()).append(',');
        }

        // remove the trailing comma
        if (!loanList.isEmpty()) {
            loanList.deleteCharAt(loanList.length() - 1);
        }

        return loanList.toString();
    }

    // might not be needed
    public static boolean isValidLoanListString(String loanListStr) {
        return loanListStr != null;
    }

    /**
     * parses loan list string to LoanList object
    */
    public static LoanList stringToLoanList(String loanListStr) {
        LoanList loanList = new LoanList();
        if (loanListStr == null || loanListStr.equals(EMPTY_STRING) || loanListStr.trim().isEmpty()) {
            return loanList;
        }
        String[] loans = loanListStr.split(",");
        for (String loanStr : loans) {
            try {
                Loan loan = Loan.stringToLoan(loanStr);
                if (loan != null) {
                    loan.updateIsPaid();
                    loanList.add(loan);
                }
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        return loanList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (internalList.isEmpty()) {
            builder.append("No loans found. \n");
        } else {
            for (int i = 0; i < internalList.size(); i++) {
                Loan loan = internalList.get(i);
                builder.append(String.format((i + 1) + ". "));
                builder.append(Messages.format(loan));
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LoanList otherLoanList)) {
            return false;
        }
        return otherLoanList.getLoans().equals(getLoans());
    }

    public Stream<Loan> stream() {
        return internalList.stream();
    }
}
