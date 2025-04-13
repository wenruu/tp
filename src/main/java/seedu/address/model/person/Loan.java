package seedu.address.model.person;

//import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.AppUtil.checkFloat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person's loan in the address book.
 */
public abstract class Loan {

    public static final String DATE_MESSAGE_CONSTRAINTS = "The due date of a Loan has to be a valid date string"
        + " greater than the current date.";

    public static final String INTEREST_MESSAGE_CONSTRAINTS = "The interest must be a valid float"
        + " and be greater than or equals to 0";
    public static final String AMOUNT_MESSAGE_CONSTRAINTS = "The amount must be a valid float"
        + " and be greater than or equals to 0";

    public static final int MONTHLY_DUE_DATE = 1; // to signifies the 1 day of every month

    public static final String UNAVAILABLE_DATE = "NA";

    public static final int NUMBER_OF_FIELDS = 8; // add 1 for loan type

    public static final String VALIDATION_REGEX = "^(\\d+(\\.\\d{1,2})?)"; // allows floats up to 2 d.p.

    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
        DateTimeFormatter.ISO_LOCAL_DATE, // yyyy-MM-dd
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        //DateTimeFormatter.ofPattern("MM/dd/yyyy"), disable american style
        DateTimeFormatter.ofPattern("yyyyMMdd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        //DateTimeFormatter.ofPattern("MM-dd-yyyy"),
        DateTimeFormatter.ofPattern("EEE MMM d yyyy", Locale.ENGLISH) // Wed Dec 27 2023
    );

    public final float principal;
    private float amountOwed;
    private float amtPaid;
    private float interest;
    private LocalDate dueDate;
    private LocalDate dateLastPaid;
    private final LocalDate dateCreated;
    private Boolean isPaid = false;

    /**
     * Constructs an {@code a loan}.
     *
     * @param strPrincipal cost of loan.
     * @param strInterest % of yearly interest, >= 0, 1 represents 1% interest
     * @param dueDate date which loan should be completely paid off
     */
    public Loan(String strPrincipal, String strInterest, String dueDate) {
        // check interest is valid float
        checkArgument(strInterest.matches(VALIDATION_REGEX), INTEREST_MESSAGE_CONSTRAINTS);
        this.interest = Float.parseFloat(strInterest);
        checkFloat(this.interest, INTEREST_MESSAGE_CONSTRAINTS);

        // check amount is valid float
        checkArgument(strPrincipal.matches(VALIDATION_REGEX), AMOUNT_MESSAGE_CONSTRAINTS);
        this.principal = Float.parseFloat(strPrincipal);
        checkFloat(this.principal, AMOUNT_MESSAGE_CONSTRAINTS);

        // check interest
        checkArgument(this.interest >= 0, INTEREST_MESSAGE_CONSTRAINTS);

        // check amount
        checkArgument(this.principal > 0, AMOUNT_MESSAGE_CONSTRAINTS);

        // check due date string
        LocalDate currentDate = LocalDate.now();
        LocalDate date = Loan.toValidLocalDate(dueDate);

        checkArgument(Loan.isValidDueDate(date, currentDate), DATE_MESSAGE_CONSTRAINTS);
        this.dueDate = date;
        this.dateLastPaid = null;
        this.dateCreated = currentDate;
    }

    /**
     * Constructs an {@code a loan}.
     *
     * @param strPrincipal cost of loan.
     * @param strAmtPaid amount paid of loan
     * @param strInterest % of yearly interest, >= 0, 1 represents 1% interest
     * @param strDueDate date which loan should be completely paid off
     * @param strDateLastPaid date which loan was last paid
     * @param strDateCreated date which loan was created
     * @param strIsPaid boolean if loan is paid
     *
    */
    public Loan(String strPrincipal, String strAmtPaid, String strInterest, String strDueDate, String strDateLastPaid,
                String strDateCreated, String strIsPaid) {
        // check interest is valid float
        checkArgument(strInterest.matches(VALIDATION_REGEX));
        this.interest = Float.parseFloat(strInterest);

        // check amount is valid float
        checkArgument(strPrincipal.matches(VALIDATION_REGEX));
        this.principal = Float.parseFloat(strPrincipal);

        // check strAmtPaid is valid float
        checkArgument(strAmtPaid.matches(VALIDATION_REGEX));
        this.amtPaid = Float.parseFloat(strAmtPaid);

        // check interest
        checkArgument(this.interest >= 0);

        // check amount
        checkArgument(this.principal > 0);

        // check amtPaid
        checkArgument(this.amtPaid >= 0);

        LocalDate currentDate = LocalDate.now();

        // check date created
        this.dateCreated = Loan.toValidLocalDate(strDateCreated);

        // date created cannot be null and cannot be in the future
        checkArgument(this.dateCreated != null
            && (this.dateCreated.isBefore(currentDate) || this.dateCreated.isEqual(currentDate)));

        // check due date
        this.dueDate = Loan.toValidLocalDate(strDueDate);
        // due date cannot be null and cannot be before date created
        checkArgument(this.dueDate != null && this.dueDate.isAfter(dateCreated));


        // check date last paid
        if (strDateLastPaid.equals(UNAVAILABLE_DATE)) { // date is unavailable
            this.dateLastPaid = null;
        } else {
            this.dateLastPaid = Loan.toValidLocalDate(strDateLastPaid);
            checkArgument(this.dateLastPaid != null
                && (dateLastPaid.isAfter(dateCreated) || this.dateCreated.isEqual(currentDate)));
        }

        // check strIsPaid
        checkArgument(strIsPaid.equals("1") || strIsPaid.equals("0"));
        if (strIsPaid.equals("1")) {
            this.isPaid = true;
        } else {
            this.isPaid = false;
        }
    }

    /**
     * Convert date string to LocalDate, return null if date string is not valid
    */
    public static LocalDate toValidLocalDate(String dateString) {
        // handle null or empty input
        if (dateString == null) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                continue;
            }
        }

        return null;
    }

    /**
     * Checks if due date is valid
    */
    public static boolean isValidDueDate(LocalDate dueDate, LocalDate currentDate) {
        if (dueDate == null) {
            return false;
        }

        return dueDate.isAfter(currentDate);
    }

    /**
     * Returns true if a loan is valid.
    */
    public static boolean isValidLoan(Loan loan) {
        return loan.getPrincipal() > 0 && loan.getInterest() >= 0;
    }

    /**
     * Deducts the amount from the amount owed.
     * @throws IllegalValueException if loan amount is negative or more than remaining owed.
     */
    public abstract void pay(float amt) throws IllegalValueException;

    /**
     * Deducts an amount equivalent to multiple months from the amount owed.
     *
     * @param months The number of months the loanee is paying for
     */
    public void payByMonths(int months) throws IllegalValueException {
        float totalAmount = getMonthlyInstalmentAmount() * months;

        if (totalAmount > getRemainingOwed()) {
            String paidTooMuchMessage = "Payment exceeds the remaining owed!";
            throw new IllegalValueException(paidTooMuchMessage);
        }

        this.incrementAmtPaid(totalAmount);
        this.setDateLastPaid(LocalDate.now());

        if (getRemainingOwed() == 0.0) {
            this.setIsPaid(true);
        }
    }

    /**
     * Compares amount paid with amount owed in preceding months.
     * @return a positive number if client paid less than owed in previous months, negative if client paid more.
     */
    public abstract float getPaymentDifference();

    public abstract float getLoanValue();

    /**
     * Returns whether loan is past due date.
     */
    public boolean isOverDue() {
        return LocalDate.now().isAfter(this.dueDate);
    }

    /**
     * Returns whether the client has missed instalments
     */
    public boolean missedInstalments() {
        return this.getMissedInstalmentsMonths() > 0;
    };

    /**
     * Returns months' worth of instalments missed, rounded up.
     */
    public int getMissedInstalmentsMonths() {
        return (int) Math.ceil(this.getMissedInstalmentsMonthsPrecise());
    };

    /**
     * Returns precise number of months' worth of instalments missed.
     */
    public float getMissedInstalmentsMonthsPrecise() {
        float moneyOwed = this.getPaymentDifference();

        if (moneyOwed <= 0) { // loan is not overdue
            return 0;
        }

        return moneyOwed / this.getMonthlyInstalmentAmount();
    };

    public float getMonthlyAveragePrincipal() {
        return principal / this.getLoanLengthMonths();
    }

    public int getDaysSinceLastPaid() {
        if (dateLastPaid == null) {
            return -1;
        }

        // get the current date
        LocalDate currentDate = LocalDate.now();

        // get the due day of the current month
        LocalDate dueDayOfMonth = currentDate.withDayOfMonth(Loan.MONTHLY_DUE_DATE);

        // Calculate the difference in days (safe to cast as days will never overflow)
        int daysBetween = (int) ChronoUnit.DAYS.between(dateLastPaid, dueDayOfMonth);

        return daysBetween;
    }

    // when using be careful, months could be 0, in which case, you should show days
    public int getMonthsSinceLastPaid() {
        if (dateLastPaid == null) {
            return -1;
        }

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // get the due day of the current month
        LocalDate dueDayOfMonth = currentDate.withDayOfMonth(Loan.MONTHLY_DUE_DATE);

        // Calculate the difference in months (safe to cast as months will never overflow)
        int monthsBetween = (int) ChronoUnit.MONTHS.between(dateLastPaid, dueDayOfMonth);

        return monthsBetween;
    }

    /**
     * Gets number of months since loan, not rounded up.
     */
    public int getMonthsSinceLoan() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // get the due day of the current month
        LocalDate dueDayOfMonth = currentDate.withDayOfMonth(Loan.MONTHLY_DUE_DATE);

        // Calculate the difference in months (safe to cast as months will never overflow)
        int monthsBetween = (int) ChronoUnit.MONTHS.between(dateCreated, dueDayOfMonth);

        return monthsBetween;
    }

    /**
     * Gets months until due date, rounded to 1 if below a month.
     * Assumes monthly due date is on the 1st.
     */
    public int getMonthsUntilDueDate() {
        LocalDate currentDate = LocalDate.now();

        LocalDate dueDayOfMonth = currentDate.withDayOfMonth(Loan.MONTHLY_DUE_DATE);

        // We don't want a negative value for this
        return (int) ChronoUnit.MONTHS.between(dueDayOfMonth, dueDate.withDayOfMonth(Loan.MONTHLY_DUE_DATE));
    }

    public int getLoanLengthMonths() {
        // Calculate the difference in months (safe to cast as months will never overflow)
        int months = (int) ChronoUnit.MONTHS.between(dateCreated, dueDate);
        if (months == 0) {
            return 1; // case where due date is not a full month after date created
        } else {
            return months;
        }
    }

    public int getLoanLengthDays() {
        // Calculate the difference in days
        return (int) ChronoUnit.DAYS.between(dateCreated, dueDate);
    }

    private static String dateToString(LocalDate date) {
        if (date == null) {
            return UNAVAILABLE_DATE;
        }
        return date.format(DATE_FORMATTERS.get(0)); // change to enum
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("amount", principal)
                .add("amtPaid", amtPaid)
                .add("interest", interest)
                .add("dueDate", Loan.dateToString(dueDate))
                .add("dateLastPaid", Loan.dateToString(dateLastPaid))
                .add("dateCreated", Loan.dateToString(dateCreated))
                .add("isPaid", isPaid)
                .toString();
    }

    /**
     * Converts a loan to a save string for storage
     */
    public String toSaveString() { // creates a string where fields are seperated by '/'
        return String.format("%.2f", principal) + '/'
                + String.format("%.2f", amtPaid) + '/'
                + String.format("%.2f", interest) + '/'
                + Loan.dateToString(dueDate) + '/'
                + Loan.dateToString(dateLastPaid) + '/'
                + Loan.dateToString(dateCreated) + '/'
                + this.getName() + '/'
                + (this.isPaid ? "1" : "0");
    }

    /**
     * Converts a save string to a loan
    */
    public static Loan stringToLoan(String loanStr) {
        String[] fields = loanStr.split("/");

        if (fields.length != NUMBER_OF_FIELDS) {
            return null;
        }
        String strAmount = fields[0];
        String strAmtPaid = fields[1];
        String strInterest = fields[2];
        String strDueDate = fields[3];
        String strDateLastPaid = fields[4];
        String strDateCreated = fields[5];
        String loanType = fields[6];
        String strIsPaid = fields[7];

        if (loanType.equals(SimpleInterestLoan.LOAN_TYPE)) {
            return new SimpleInterestLoan(strAmount, strAmtPaid, strInterest, strDueDate, strDateLastPaid,
                strDateCreated, strIsPaid);
        } else if (loanType.equals(CompoundInterestLoan.LOAN_TYPE)) {
            return new CompoundInterestLoan(strAmount, strAmtPaid, strInterest, strDueDate, strDateLastPaid,
                strDateCreated, strIsPaid);
        } else {
            return null;
        }
    }

    /**
     * Returns true if a loan string is valid.
    */
    public static boolean isValidLoanString(String loanStr) {
        try {
            Loan loan = Loan.stringToLoan(loanStr);
            return loan != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(principal, amtPaid, interest, dueDate, dateLastPaid, dateCreated, isPaid, this.getName());
    }

    /**
     * Returns total amount of money paid.
     */
    public float getAmtPaid() {
        return this.amtPaid;
    }

    public float getInterest() {
        return this.interest;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public LocalDate getDateLastPaid() {
        return this.dateLastPaid;
    }

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public boolean isPaid() {
        return this.isPaid;
    }

    public float getPrincipal() {
        return this.principal;
    }

    public void incrementAmtPaid(float pay) {
        this.amtPaid += pay;
    }

    protected void setDateLastPaid(LocalDate dateLastPaid) {
        this.dateLastPaid = dateLastPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public float getAmountOwed() {
        return this.amountOwed;
    }

    public float getRemainingOwed() {
        return Math.round((amountOwed - amtPaid) * 100) / 100f;
    }

    protected void setAmountOwed(float amountOwed) {
        this.amountOwed = amountOwed;
    }

    // Test method
    protected void setDueDate(String dueDate) {
        this.dueDate = LocalDate.parse(dueDate);
    };

    /**
     * Updates the isPaid status
    */
    public void updateIsPaid() {
        this.isPaid = getRemainingOwed() == 0f;
    }

    public abstract String getName();

    public abstract float getMonthlyInstalmentAmount();
}
