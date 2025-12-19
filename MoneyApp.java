import java.time.LocalDate;
import java.util.Scanner;

// Main program, handles user input
public class MoneyApp {
    private static AccountData data = new AccountData();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Simple Account Book ===");
        data.start();  // Read data

        boolean running = true;
        while (running) {
            System.out.println("\nWhat to do?");
            System.out.println("1. Record");
            System.out.println("2. View");
            System.out.println("3. Search");
            System.out.println("4. Delete");
            System.out.println("5. Calculate");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine();

            if (choice.equals("1")) {
                add();
            } else if (choice.equals("2")) {
                data.showAll();
            } else if (choice.equals("3")) {
                System.out.print("Search what: ");
                String what = sc.nextLine();
                data.find(what);
            } else if (choice.equals("4")) {
                System.out.print("Delete which (ID): ");
                try {
                    int id = Integer.parseInt(sc.nextLine());
                    data.remove(id);
                } catch (Exception e) {
                    System.out.println("Invalid ID");
                }
            } else if (choice.equals("5")) {
                data.calc();
            } else if (choice.equals("0")) {
                running = false;
                System.out.println("Bye");
            } else {
                System.out.println("Don't understand");
            }
        }
        sc.close();
    }

    // Record function
    private static void add() {
        System.out.println("\n--- Record one ---");

        // Date
        LocalDate d = null;
        while (d == null) {
            System.out.print("Date (YYYY-MM-DD, press Enter for today): ");
            String ds = sc.nextLine();
            if (ds.isEmpty()) {
                d = LocalDate.now();
            } else {
                try {
                    d = LocalDate.parse(ds);
                } catch (Exception e) {
                    System.out.println("Invalid date, try again");
                }
            }
        }

        // Item
        System.out.print("Item (food, transport, etc): ");
        String item = sc.nextLine();
        if (item.isEmpty()) item = "Other";

        // Amount
        double money = 0;
        while (money == 0) {
            System.out.print("Amount (positive for income, negative for expense): ");
            try {
                money = Double.parseDouble(sc.nextLine());
                if (money == 0) System.out.println("0 yuan no need to record");
            } catch (Exception e) {
                System.out.println("Not a number");
            }
        }

        // Reason
        System.out.print("Note (optional): ");
        String why = sc.nextLine();

        // Confirm
        System.out.print("Confirm to record? (y/n): ");
        String ok = sc.nextLine();
        if (ok.equalsIgnoreCase("y")) {
            data.addNew(d, item, money, why);
        } else {
            System.out.println("Cancelled");
        }
    }
}