
import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Transaction {
    String type; // income or expense
    String category;
    double amount;
    LocalDate date;

    Transaction(String type, String category, double amount, LocalDate date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
}

public class ExpenseTracker {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Load from file");
            System.out.println("4. Show Monthly Summary");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1 -> addTransaction("income");
                case 2 -> addTransaction("expense");
                case 3 -> loadFromFile();
                case 4 -> showMonthlySummary();
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTransaction(String type) {
        System.out.print("Enter date (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter category (" + (type.equals("income") ? "Salary/Business" : "Food/Rent/Travel") + "): ");
        String category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // clear newline
        transactions.add(new Transaction(type, category, amount, date));
        System.out.println("Transaction added successfully.");
    }

    private static void loadFromFile() {
        System.out.print("Enter file path: ");
        String path = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                String category = parts[1];
                double amount = Double.parseDouble(parts[2]);
                LocalDate date = LocalDate.parse(parts[3], formatter);
                transactions.add(new Transaction(type, category, amount, date));
            }
            System.out.println("File loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    private static void showMonthlySummary() {
        System.out.print("Enter year-month (yyyy-mm): ");
        String input = scanner.nextLine();
        YearMonth month = YearMonth.parse(input);
        double incomeTotal = 0;
        double expenseTotal = 0;

        Map<String, Double> incomeCategories = new HashMap<>();
        Map<String, Double> expenseCategories = new HashMap<>();

        for (Transaction t : transactions) {
            if (YearMonth.from(t.date).equals(month)) {
                if (t.type.equals("income")) {
                    incomeTotal += t.amount;
                    incomeCategories.put(t.category, incomeCategories.getOrDefault(t.category, 0.0) + t.amount);
                } else {
                    expenseTotal += t.amount;
                    expenseCategories.put(t.category, expenseCategories.getOrDefault(t.category, 0.0) + t.amount);
                }
            }
        }

        System.out.println("\n--- Monthly Summary for " + month + " ---");
        System.out.println("Total Income: " + incomeTotal);
        for (String cat : incomeCategories.keySet()) {
            System.out.println("  " + cat + ": " + incomeCategories.get(cat));
        }

        System.out.println("\nTotal Expense: " + expenseTotal);
        for (String cat : expenseCategories.keySet()) {
            System.out.println("  " + cat + ": " + expenseCategories.get(cat));
        }

        System.out.println("\nNet Savings: " + (incomeTotal - expenseTotal));
    }
}
