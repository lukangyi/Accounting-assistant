import java.io.*;
import java.time.LocalDate;
import java.util.*;

// I put data class and some management functions together, simpler this way
public class AccountData {
    // Use list to store data
    private ArrayList<Record> myRecords = new ArrayList<>();
    private String dataFile = "my_money.txt";
    private int nextId = 1;

    // Inner record class, simpler
    class Record {
        int id;
        LocalDate date;
        String item;
        double money;
        String reason;

        Record(int id, LocalDate d, String t, double m, String w) {
            this.id = id;
            date = d;
            item = t;
            money = m;
            reason = w;
        }

        // Method for display
        String show() {
            String sign = money > 0 ? "+" : "";
            return String.format("%03d %s %s %s%.2f %s",
                    id, date, item, sign, Math.abs(money), reason);
        }

        // For saving to file
        String toFile() {
            return String.format("%d,%s,%s,%.2f,%s",
                    id, date, item, money, reason.replace(",", " "));
        }
    }

    // Read file when starting
    public void start() {
        try {
            File f = new File(dataFile);
            if (f.exists()) {
                Scanner sc = new Scanner(f);
                while (sc.hasNextLine()) {
                    String s = sc.nextLine();
                    String[] p = s.split(",");
                    if (p.length >= 5) {
                        int i = Integer.parseInt(p[0]);
                        LocalDate d = LocalDate.parse(p[1]);
                        Record r = new Record(i, d, p[2],
                                Double.parseDouble(p[3]), p[4]);
                        myRecords.add(r);
                        if (i >= nextId) nextId = i + 1;
                    }
                }
                sc.close();
            }
        } catch (Exception e) {
            System.out.println("Problem reading file: " + e.getMessage());
        }
    }

    // Add record
    public void addNew(LocalDate d, String t, double m, String w) {
        Record r = new Record(nextId++, d, t, m, w);
        myRecords.add(r);
        saveFile();
        System.out.println("Recorded, ID " + r.id);
    }

    // Show all
    public void showAll() {
        if (myRecords.isEmpty()) {
            System.out.println("No records yet");
            return;
        }
        System.out.println("\n--- My Bills ---");
        for (Record r : myRecords) {
            System.out.println(r.show());
        }
        System.out.println("--- End ---");
    }

    // Find records
    public void find(String what) {
        boolean found = false;
        for (Record r : myRecords) {
            if (r.item.contains(what) || r.reason.contains(what)) {
                System.out.println(r.show());
                found = true;
            }
        }
        if (!found) System.out.println("Not found: " + what);
    }

    // Delete
    public void remove(int id) {
        for (int i = 0; i < myRecords.size(); i++) {
            if (myRecords.get(i).id == id) {
                myRecords.remove(i);
                saveFile();
                System.out.println("Deleted");
                return;
            }
        }
        System.out.println("ID not found");
    }

    // Calculate totals
    public void calc() {
        double in = 0, out = 0;
        for (Record r : myRecords) {
            if (r.money > 0) in += r.money;
            else out += Math.abs(r.money);
        }
        System.out.println("Income: " + String.format("%.2f", in));
        System.out.println("Expense: " + String.format("%.2f", out));
        System.out.println("Balance: " + String.format("%.2f", in - out));
    }

    // Save to file
    private void saveFile() {
        try {
            PrintWriter pw = new PrintWriter(dataFile);
            for (Record r : myRecords) {
                pw.println(r.toFile());
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("Problem saving file: " + e.getMessage());
        }
    }
}