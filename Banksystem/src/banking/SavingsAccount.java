package banking;

// SavingsAccount inherits common features from BankAccount.
// It also implements the Loanable interface.
public class SavingsAccount extends BankAccount implements Loanable {

    private static final double MINIMUM_BALANCE = 100.00;
    private static final double ANNUAL_INTEREST_RATE = 0.05;
    private static final double MAXIMUM_LOAN = 10000.00;

    public SavingsAccount(
            String accountNumber,
            String accountHolder,
            double openingBalance) {

        super(accountNumber, accountHolder, openingBalance);
    }

    // Deposits must be positive.
    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }

        setBalance(getBalance() + amount);
        return true;
    }

    // A savings withdrawal must leave at least M100 in the account.
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }

        if (getBalance() - amount < MINIMUM_BALANCE) {
            return false;
        }

        setBalance(getBalance() - amount);
        return true;
    }

    // Calculates 5% of the current balance and adds it to the account.
    @Override
    public double calculateInterest() {
        double interest = getBalance() * ANNUAL_INTEREST_RATE;
        setBalance(getBalance() + interest);
        return interest;
    }

    // Interface implementation: checks the loan rules before approving.
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
        System.out.println("\nSavings Account");
        super.displayDetails();
        System.out.println("Minimum balance: M" + MINIMUM_BALANCE);
        System.out.println("Annual interest rate: 5%");
    }
}