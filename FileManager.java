/**
 * This class handles file persistence for transactions.
 * It provides utility methods to save transactions to a local text file
 * and load them back into the application using a delimiter-separated format.
 * 
 * File Name - represents the default data file (transactions.txt)
 * Delimiter - represents the field separator (;)
 * Date Format - uses dd/MM/yyyy for standard date representation
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    // Constants for file storage and formatting
    private static final String FILE_NAME = "transactions.txt";
    private static final String DELIMITER = ";";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    /**
     * Saves the provided list of transactions to the local text file.
     * Overwrites existing file content with the serialized transaction data.
     */
    public static void saveTransactions(List<Transaction> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Transaction t : transactions) {
                String line = String.join(
                    DELIMITER,
                    String.valueOf(t.getAmount()),
                    t.getCategory(),
                    t.getType().name(),
                    t.getDate().format(FORMATTER),
                    t.getComment() != null ? t.getComment() : ""
                );
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("File saving error: " + e.getMessage());
        }
    }


    /**
     * Loads transactions from the local text file.
     * Parses each line into a Transaction object and returns them as a list.
     * If the file does not exist, an empty list is returned.
     */
    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 5) {
                    continue;
                }

                try {
                    double amount = Double.parseDouble(parts[0]);
                    String category = parts[1];
                    TransactionType type = TransactionType.valueOf(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3], FORMATTER);
                    String comment = parts[4];

                    transactions.add(new Transaction(amount, category, type, date, comment));
                } catch (DateTimeParseException | IllegalArgumentException e) {
                    // Tek bir satır bozuksa sadece o satırı atla, diğer satırları okumaya devam et
                    System.err.println("Skipping corrupted row: " + line + " (" + e.getMessage() + ")");
                }
            }
        } catch (IOException e) {
            System.err.println("File reading error: " + e.getMessage());
        }

        return transactions;
    }
}