# Finance Manager App

A desktop-based personal finance management application built with Java and Swing. The application allows users to track their incomes and expenses, view balance summaries, and persist data locally in a text file.

## Features

- **Transaction Management:** Add new income or expense records with category, amount, date, and comments.
- **Financial Summary:** View real-time calculations of total income, total expenses, and net balance with dynamic color indicators.
- **Transaction History:** Display all transactions in a structured table with the ability to delete selected records.
- **Data Persistence:** Automatically saves all transactions to a local text file (`transactions.txt`) and restores them on launch.

## Project Structure

```text
├── Transaction.java         # Model class representing a single financial record
├── TransactionType.java     # Enum defining transaction types (INCOME, EXPENSE)
├── FileManager.java         # Handles reading and writing data to transactions.txt
├── FinanceManager.java      # Core business logic for calculations and list operations
└── FinanceManagerGUI.java   # Swing-based desktop user interface