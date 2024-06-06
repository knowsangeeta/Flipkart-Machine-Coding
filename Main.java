import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        try {
            UPIService upiService = new UPIService();

            // Add banks
            upiService.addBank("Bank A", true);
            upiService.addBank("Bank B", true);

            // Onboard users
            upiService.onboardUser("User1", "1234567890", true);
            upiService.onboardUser("User2", "0987654321", true);

            // Link bank accounts
            upiService.linkBankAccount("1234567890", "Bank A", "111", 1000.0, true);
            upiService.linkBankAccount("0987654321", "Bank B", "222", 500.0, true);

            // Make a payment
            System.out.println("Making a payment from User1 to User2:");
            upiService.makePayment("1234567890", "0987654321", 200.0);
            System.out.println("Payment successful!");

            // View transaction history for User1
            System.out.println("\nTransaction history for User1:");
            List<Transaction> User1Transactions = upiService.getTransactionHistory("1234567890");
            for (Transaction transaction : User1Transactions) {
                System.out.println("Payer: " + transaction.getPayer().getName() + ", Payee: " + transaction.getPayee().getName() +
                        ", Amount: " + transaction.getAmount() + ", Status: " + transaction.getStatus() + ", Date: " + transaction.getDate());
            }

            // View transaction history for User2
            System.out.println("\nTransaction history for User2:");
            List<Transaction> User2Transactions = upiService.getTransactionHistory("0987654321");
            for (Transaction transaction : User2Transactions) {
                System.out.println("Payer: " + transaction.getPayer().getName() + ", Payee: " + transaction.getPayee().getName() +
                        ", Amount: " + transaction.getAmount() + ", Status: " + transaction.getStatus() + ", Date: " + transaction.getDate());
            }



            // to make a payment with insufficient funds
            try {
                System.out.println("\nAttempting to make a payment with insufficient funds:");
                upiService.makePayment("0987654321", "1234567890", 600.0);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            // to make a payment to an inactive account
            try {
                System.out.println("\nAttempting to make a payment to an inactive account:");
                upiService.onboardUser("User3", "1122334455", false);
                upiService.linkBankAccount("1122334455", "Bank A", "333", 100.0, true);
                upiService.makePayment("1234567890", "1122334455", 50.0);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            // Deactivate a user account and try to make a payment from it
            try {
                System.out.println("\nDeactivating User1's account and attempting a payment:");
                User User1 = upiService.getUser("1234567890");
                User1.setActive(false);
                upiService.makePayment("1234567890", "0987654321", 50.0);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            // Try to make a payment when bank server is down
            try {
                System.out.println("\nAttempting to make a payment when the bank server is down:");
                Bank bankA = upiService.getBank("Bank A");
                bankA.setServerUp(false);
                upiService.makePayment("1234567890", "0987654321", 50.0);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = sdf.parse("2024-06-06");
                Date endDate = sdf.parse("2024-06-06");
                String status = "COMPLETED"; 
                List<Transaction> searchedTransactions = upiService.searchTransactions(startDate, endDate, status);
                System.out.println("Searched Transactions:");
                for (Transaction transaction : searchedTransactions) {
                    System.out.println(transaction); 
                }
                upiService.handlePendingTransactions();
                System.out.println("Pending transactions processed.");
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
