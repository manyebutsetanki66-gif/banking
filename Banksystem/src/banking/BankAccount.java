package banking;

// Abstract superclass
public abstract class BankAccount {

    // Encapsulation
    private final String accountNumber;
    private final String accountHolder;
    private double balance;

    // Constructor
    public BankAccount(String accountNumber, String accountHolder, double openingBalance) {
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }

        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = openingBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    // Subclasses can change the balance while other classes cannot change it directly.
    protected void setBalance(double balance) {
        this.balance = balance;
    }

    // Abstract methods
    public abstract boolean deposit(double amount);

    public abstract boolean withdraw(double amount);

    public abstract double calculateInterest();

    // Displays information about account types.
    public void displayDetails() {
        System.out.println("Account number: " + accountNumber);
        System.out.println("Account holder: " + accountHolder);
        System.out.printf("Balance: M%.2f%n", balance);
    }
}