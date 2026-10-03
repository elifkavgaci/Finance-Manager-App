/**
 * This class represents a financial transaction with attributes
 * such as amount, category, type, date, and comment.
 * Amount - represents the monetary value of the transaction
 * Category - represents the category of the transaction (e.g., Food, Rent, Salary)
 * Type - represents the type of the transaction (INCOME or EXPENSE)
 * Date - represents the date of the transaction
 * Comment - represents any additional notes or comments about the transaction
 */

import java.time.LocalDate;

public class Transaction {

    // Attributes of the transaction
    private double amount;
    private String category;
    private TransactionType type;
    private LocalDate date;
    private String comment;

    // Constructor to initialize the transaction object with the provided attributes
    public Transaction(double amount, String category, TransactionType type, LocalDate date, String comment) {
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
        this.comment = comment;
    }

    // Getter and setter methods for each attribute of the transaction
    public double getAmount() {
        return this.amount;
    }

    public String getCategory() {
        return this.category;
    }

    public TransactionType getType() {
        return this.type;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getComment() {
        return this.comment;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
