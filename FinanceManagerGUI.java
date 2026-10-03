/**
 * This class provides a graphical user interface (GUI) for the personal
 * finance management application using Java Swing.
 * It allows users to view financial summaries, add new transactions,
 * delete selected records from a table, and persist changes to a file.
 * 
 * Summary Panel - displays total income, total expense, and net balance
 * Form Panel - provides input fields to enter new transaction details
 * Table Panel - shows transaction history and allows row deletion
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class FinanceManagerGUI extends JFrame {

    // Core business logic manager and date formatter
    private FinanceManager financeManager = new FinanceManager();
    private JTextField txtAmount;
    private JTextField txtCategory;
    private JComboBox<String> comboType;
    private JTextField txtDate;
    private JTextField txtComment;
    private JLabel lblIncome;
    private JLabel lblExpense;
    private JLabel lblBalance;
    private JTable tableTransactions;
    private DefaultTableModel tableModel;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Initializes the GUI components, sets window properties, and loads initial data.
    public FinanceManagerGUI() {
        setTitle("Personal Finance Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel summaryPanel = createSummaryPanel();
        add(summaryPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.WEST);

        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        loadInitialData();
    }

    // Creates and returns the summary panel showing total income, expense, and net balance.
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Financial Summary"));

        lblIncome = new JLabel("Total income: 0.00 TL", SwingConstants.CENTER);
        lblExpense = new JLabel("Total expense: 0.00 TL", SwingConstants.CENTER);
        lblBalance = new JLabel("Net Balance: 0.00 TL", SwingConstants.CENTER);

        lblIncome.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblExpense.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblBalance.setFont(new Font("SansSerif", Font.BOLD, 14));

        lblIncome.setForeground(new Color(34, 139, 34));
        lblExpense.setForeground(new Color(178, 34, 34));

        panel.add(lblIncome);
        panel.add(lblExpense);
        panel.add(lblBalance);
        return panel;
    }

    // Creates and returns the form panel for adding new transactions.
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Transaction"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Amount:"), gbc);
        txtAmount = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Category:"), gbc);
        txtCategory = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Transaction Type:"), gbc);
        comboType = new JComboBox<>(new String[]{"EXPENSE", "INCOME"});
        gbc.gridx = 1;
        panel.add(comboType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Date (DD/MM/YYYY):"), gbc);
        txtDate = new JTextField(LocalDate.now().format(FORMATTER));
        gbc.gridx = 1;
        panel.add(txtDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Comment:"), gbc);
        txtComment = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtComment, gbc);

        JButton btnAdd = new JButton("Save the action");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(btnAdd, gbc);
        btnAdd.addActionListener(e -> addTransactionAction());

        return panel;
    }

    // Creates and returns the table panel displaying transaction history and delete action.
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Transaction History"));

        String[] columns = {"Amount", "Category", "Type", "Date", "Comment"};
        tableModel = new DefaultTableModel(columns, 0);
        tableTransactions = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableTransactions);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnDelete = new JButton("Delete Selected Transaction");
        btnDelete.addActionListener(e -> deleteTransactionAction());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnDelete);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Loads existing transaction data from the file and populates the table and summary.
    private void loadInitialData() {
        List<Transaction> savedTransactions = FileManager.loadTransactions();
        for (Transaction t : savedTransactions) {
            financeManager.addTransaction(t);
            tableModel.addRow(new Object[]{
                String.format("%.2f TL", t.getAmount()),
                t.getCategory(),
                t.getType(),
                t.getDate().format(FORMATTER),
                t.getComment()
            });
        }
        updateSummary();
    }

    // Handles adding a new transaction from user inputs, saves to file, and refreshes the view.
    private void addTransactionAction() {
        try {
            double amount = Double.parseDouble(txtAmount.getText().trim());
            String category = txtCategory.getText().trim().toLowerCase();
            if (category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a category.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TransactionType type = comboType.getSelectedIndex() == 1 ? TransactionType.INCOME : TransactionType.EXPENSE;
            LocalDate date = LocalDate.parse(txtDate.getText().trim(), FORMATTER);
            String comment = txtComment.getText().trim();

            Transaction transaction = new Transaction(amount, category, type, date, comment);
            financeManager.addTransaction(transaction);
            FileManager.saveTransactions(financeManager.getTransactions());

            tableModel.addRow(new Object[]{
                String.format("%.2f TL", amount),
                category,
                type,
                date.format(FORMATTER),
                comment
            });
            updateSummary();

            txtAmount.setText("");
            txtCategory.setText("");
            txtComment.setText("");
            txtDate.setText(LocalDate.now().format(FORMATTER));

            JOptionPane.showMessageDialog(this, "The transaction has been added successfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount! Please enter a numerical value.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "The date format is incorrect! Example:" + LocalDate.now().format(FORMATTER), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handles deleting the selected transaction from the table, list, and file storage.
    private void deleteTransactionAction() {
        int selectedRow = tableTransactions.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction from the table to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the selected transaction?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            financeManager.removeTransaction(selectedRow);
            tableModel.removeRow(selectedRow);
            FileManager.saveTransactions(financeManager.getTransactions());
            updateSummary();
            JOptionPane.showMessageDialog(this, "Transaction deleted successfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Recalculates and updates income, expense, and net balance labels with proper colors.
    private void updateSummary() {
        lblIncome.setText(String.format("Total Income: %.2f TL", financeManager.getTotalIncome()));
        lblExpense.setText(String.format("Total Expense: %.2f TL", financeManager.getTotalExpense()));

        double net = financeManager.getNetBalance();
        lblBalance.setText(String.format("Net Balance: %.2f TL", net));

        if (net < 0.0) {
            lblBalance.setForeground(new Color(178, 34, 34));
        } else {
            lblBalance.setForeground(new Color(34, 139, 34));
        }
    }

    // Launches the GUI application window.
    public static void main(String[] args) {
        new FinanceManagerGUI().setVisible(true);
    }
}