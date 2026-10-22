import banking.BankAccount;
import banking.SavingsAccount;
import banking.CurrentAccount;
import banking.Loanable;
import banking.TransactionLogger;

import java.util.Scanner;

// Main class
public class BankingSystemMain {

    private static final Scanner input = new Scanner(System.in);

    // BankAccount array stores different account subclasses.
    private static final BankAccount[] accounts = {
        new SavingsAccount("SA001", "Thabo", 1000.00),
        new CurrentAccount("CA001", "Lerato", 500.00)
    };

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("Welcome to the Banking System");
        //switch
        while (running) {
            displayMenu();

            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1:
                        displayAllAccounts();
                        break;

                    case 2:
                        depositMoney();
                        break;

                    case 3:
                        withdrawMoney();
                        break;

                    case 4:
                        transferMoney();
                        break;

                    case 5:
                        calculateAccountInterest();
                        break;

                    case 6:
                        requestAccountLoan();
                        break;

                    case 7:
                        displayAccountDetails();
                        break;

                    case 8:
                        running = false;
                        System.out.println("Thank you for using the Banking System.");
                        break;

                    default:
                        System.out.println("Invalid menu option.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        input.close();
    }

    // Displays menu
    private static void displayMenu() {
        System.out.println("\nBANKING MENU");
        System.out.println("1. Display all accounts");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Calculate interest");
        System.out.println("6. Request loan");
        System.out.println("7. Account details");
        System.out.println("8. Exit");
    }

    // handles invalid numeric input.
    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // rejects non-numeric input.
    private static double readAmount(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Double.parseDouble(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    // Displays all accounts 
    private static void displayAllAccounts() {
        for (BankAccount account : accounts) {
            account.displayDetails();
        }
    }

    // Finds an account using its account number.
    private static BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return account;
            }
        }

        return null;
    }

    // existing account and reports an invalid account number.
    private static BankAccount selectAccount() {
        System.out.print("Enter account number: ");
        String number = input.nextLine().trim();

        BankAccount account = findAccount(number);

        if (account == null) {
            System.out.println("Account not found.");
        }

        return account;
    }

    private static void depositMoney() {
        BankAccount account = selectAccount();

        if (account == null) {
            return;
        }

        double amount = readAmount("Enter deposit amount: M");
        boolean success = account.deposit(amount);

        String result = success
                ? "Deposit successful"
                : "Rejected: amount must be positive";

        System.out.println(result);

        // Log successful and rejected deposit attempts.
        TransactionLogger.logTransaction(
                account.getAccountNumber(),
                "Deposit",
                amount,
                account.getBalance(),
                result
        );
    }

    private static void withdrawMoney() {
        BankAccount account = selectAccount();

        if (account == null) {
            return;
        }

        double amount = readAmount("Enter withdrawal amount: M");
        boolean success = account.withdraw(amount);

        String result = success
                ? "Withdrawal successful"
                : "Rejected: invalid amount or insufficient available funds";

        System.out.println(result);

        // Log the attempt even when the withdrawal is rejected.
        TransactionLogger.logTransaction(
                account.getAccountNumber(),
                "Withdrawal",
                amount,
                account.getBalance(),
                result
        );
    }

    private static void transferMoney() {
        System.out.println("Source account:");
        BankAccount source = selectAccount();

        if (source == null) {
            return;
        }

        System.out.println("Destination account:");
        BankAccount destination = selectAccount();

        if (destination == null) {
            TransactionLogger.logTransaction(
                    source.getAccountNumber(),
                    "Transfer",
                    0,
                    source.getBalance(),
                    "Rejected: destination account not found"
            );
            return;
        }

        if (source == destination) {
            System.out.println("Source and destination must be different accounts.");

            TransactionLogger.logTransaction(
                    source.getAccountNumber(),
                    "Transfer",
                    0,
                    source.getBalance(),
                    "Rejected: same source and destination account"
            );
            return;
        }

        double amount = readAmount("Enter transfer amount: M");

        // The transfer must be positive and the source must allow the withdrawal.
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");

            TransactionLogger.logTransaction(
                    source.getAccountNumber(),
                    "Transfer",
                    amount,
                    source.getBalance(),
                    "Rejected: amount must be positive"
            );
            return;
        }

        if (!source.withdraw(amount)) {
            System.out.println("Transfer rejected: insufficient available funds.");

            TransactionLogger.logTransaction(
                    source.getAccountNumber(),
                    "Transfer",
                    amount,
                    source.getBalance(),
                    "Rejected: insufficient available funds or account rules"
            );
            return;
        }

        // Deposit into the destination only after the source withdrawal succeeds.
        if (!destination.deposit(amount)) {
            // Restore the source balance if the destination deposit fails.
            source.deposit(amount);

            System.out.println("Transfer failed: destination rejected the amount.");

            TransactionLogger.logTransaction(
                    source.getAccountNumber(),
                    "Transfer",
                    amount,
                    source.getBalance(),
                    "Rejected: destination deposit failed"
            );
            return;
        }

        System.out.println("Transfer successful.");

        // Record the transfer against both accounts.
        TransactionLogger.logTransaction(
                source.getAccountNumber(),
                "Transfer Sent",
                amount,
                source.getBalance(),
                "Transfer successful"
        );

        TransactionLogger.logTransaction(
                destination.getAccountNumber(),
                "Transfer Received",
                amount,
                destination.getBalance(),
                "Transfer successful"
        );
    }

    private static void calculateAccountInterest() {
        BankAccount account = selectAccount();

        if (account == null) {
            return;
        }

        // the subclass implementation runs for the selected account.
        double interest = account.calculateInterest();

        String result;

        if (interest > 0) {
            result = "Interest added successfully";
            System.out.printf("Interest added: M%.2f%n", interest);
        } else {
            result = "No interest: current accounts do not earn interest";
            System.out.println(result);
        }

        TransactionLogger.logTransaction(
                account.getAccountNumber(),
                "Interest",
                interest,
                account.getBalance(),
                result
        );
    }

    private static void requestAccountLoan() {
        BankAccount account = selectAccount();

        if (account == null) {
            return;
        }

        double amount = readAmount("Enter loan amount: M");

        // Interface reference both account types implement Loanable.
        Loanable loanAccount = (Loanable) account;

        boolean success = loanAccount.requestLoan(amount);

        String result = success
                ? "Loan approved"
                : "Loan rejected: amount must be positive and no more than M10000";

        System.out.println(result);

        TransactionLogger.logTransaction(
                account.getAccountNumber(),
                "Loan Request",
                amount,
                account.getBalance(),
                result
        );
    }

    private static void displayAccountDetails() {
        BankAccount account = selectAccount();

        if (account != null) {
            account.displayDetails();
        }
    }
}