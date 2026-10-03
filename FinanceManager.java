/**
 * This class manages the collection of financial transactions and
 * provides business logic operations such as adding, removing, and summarizing transactions.
 * It also calculates financial summaries including total income, total expense,
 * net balance, and category-based expense distributions.
 * 
 * Transaction List - stores all active financial transaction records
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinanceManager {

    // List storing the transaction records
    private List<Transaction> transactionList = new ArrayList<>();


    // Adds a new transaction to the manager.
    public void addTransaction(Transaction transaction) {
        this.transactionList.add(transaction);
    }

    /**
     * Removes the specified transaction from the list.
     * Returns true if the transaction was found and removed.
     */
    public boolean removeTransaction(Transaction transaction) {
        return this.transactionList.remove(transaction);
    }

    /**
     * Groups and calculates the sum of expenses by their category.
     * Returns a map containing categories as keys and total amounts as values.
     */
    public Map<String, Double> transactionGroups() {
        HashMap<String, Double> map = new HashMap<>();
        for (Transaction transaction : this.transactionList) {
            if (transaction.getType() == TransactionType.EXPENSE){
                map.put(transaction.getCategory(), map.getOrDefault(transaction.getCategory(), 0.0) + transaction.getAmount());
            }
        }
        return map;
    }

    // Calculates the total sum of all income transactions.
    public double getTotalIncome() {
        double income = 0.0;
        for (Transaction transaction : this.transactionList) {
            if (transaction.getType() == TransactionType.INCOME){
                income += transaction.getAmount();
            }
        }
        return income;
    }

    // Calculates the total sum of all expense transactions.
    public double getTotalExpense() {
        double expense = 0.0;
        for (Transaction transaction : this.transactionList) {
            if (transaction.getType() == TransactionType.EXPENSE){
                expense += transaction.getAmount();
            }
        }
        return expense;
    }

    // Calculates the net balance by subtracting total expenses from total income.
    public double getNetBalance() {
        return this.getTotalIncome() - this.getTotalExpense();
    }

    // Returns the full list of transactions.
    public List<Transaction> getTransactions() {
        return this.transactionList;
    }

    // Removes a transaction at the specified index if within valid bounds.
    public void removeTransaction(int index) {
        if (index >= 0 && index < this.transactionList.size()) {
            this.transactionList.remove(index);
        }
    }
}