package banking;

// Interface: defines the method required for requesting a loan.
public interface Loanable {

    // Returns true if the loan is approved, otherwise false.
    boolean requestLoan(double amount);
}