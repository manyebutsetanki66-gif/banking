package banking;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// final prevents this utility class from being extended.
public final class TransactionLogger {

    // Name of the file where transaction records will be stored.
    private static final String FILE_NAME = "transactions.txt";

    // Private constructor prevents unnecessary objects from being created.
    private TransactionLogger() {
    }

    // Records the details of a transaction in the text file.
    public static void logTransaction(
            String accountNumber,
            String transactionType,
            double amount,
            double balance,
            String result) {

        // Get the current date and time in the required format.
        String dateTime = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        // Combine the transaction details into one readable record.
        String record = dateTime
            + " | Account: " + accountNumber
            + " | Type: " + transactionType
            + " | Amount: M" + amount
            + " | Balance: M" + balance
            + " | Result: " + result;

        // Open the file in append mode so existing records are preserved.
        // try-with-resources automatically closes the file after writing.
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {

            // Write the record and move to a new line.
            writer.write(record + System.lineSeparator());

        } catch (IOException e) {

            // Handle errors that occur while writing to the file.
            System.out.println(
                "Could not write transaction log: " + e.getMessage()
            );
        }
    }
}
