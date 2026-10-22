package banking;

// CurrentAccount inherits from BankAccount.
// It implements Loanable and overrides the required methods.
public class CurrentAccount extends BankAccount implements Loanable {

    private static final double OVERDRAFT_LIMIT = 2000.00;
    private static final double MAXIMUM_LOAN = 10000.00;

    public CurrentAccount(
            String accountNumber,
            String accountHolder,
            double openingBalance) {

        super(accountNumber, accountHolder, openingBalance);
    }

    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }

        setBalance(getBalance() + amount);
        return true;
    }

    // The balance may become negative, but cannot go below -M2000.
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }

        if (getBalance() - amount < -OVERDRAFT_LIMIT) {
            return false;
        }

        setBalance(getBalance() - amount);
        return true;
    }

    // Current accounts do not earn interest.
    @Override
    public double calculateInterest() {
        return 0.00;
    }

    @Override
    public boolean requestLoan(double amount) {
        if (amount <= 0 || amount > MAXIMUM_LOAN) {
            return false;
        }

        setBalance(getBalance() + amount);
        return true;
    }

    @Override
    public void displayDetails() {
        System.out.println("\nCurrent Account ");
        super.displayDetails();
        System.out.println("Overdraft limit: M" + OVERDRAFT_LIMIT);
        System.out.println("Interest earned: M0.00");
    }
}