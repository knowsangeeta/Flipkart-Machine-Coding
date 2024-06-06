import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UPIService {
    private Map<String, User> users;
    private Map<String, Bank> banks;
    private List<Transaction> transactions;
    private PSPService pspService;

    public UPIService() {
        users = new ConcurrentHashMap<>();
        banks = new ConcurrentHashMap<>();
        transactions = Collections.synchronizedList(new ArrayList<>());
        pspService = new PSPService();
    }

    // User onboarding
    public void onboardUser(String name, String phoneNumber, boolean isActive) throws Exception {
        if (users.containsKey(phoneNumber)) {
            throw new Exception("Phone number already linked to another user.");
        }
        User user = new User(name, phoneNumber, isActive);
        users.put(phoneNumber, user);
    }

    // Add bank
    public void addBank(String bankName, boolean isServerUp) {
        Bank bank = new Bank(bankName, isServerUp);
        banks.put(bankName, bank);
    }

    // Link bank account
    public void linkBankAccount(String phoneNumber, String bankName, String accountNumber, double balance, boolean isPrimary) throws Exception {
        if (!banks.containsKey(bankName)) {
            throw new Exception("Bank is not registered.");
        }
        if (!users.containsKey(phoneNumber)) {
            throw new Exception("User not found.");
        }

        User user = users.get(phoneNumber);
        for (BankAccount account : user.getBankAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                throw new Exception("Bank account already linked to another user.");
            }
        }

        BankAccount account = new BankAccount(bankName, accountNumber, balance, isPrimary);
        user.addBankAccount(account);
    }

    // Make payment
    public void makePayment(String payerPhoneNumber, String payeePhoneNumber, double amount) throws Exception {
        User payer = users.get(payerPhoneNumber);
        User payee = users.get(payeePhoneNumber);

        if (payer == null || payee == null) {
            throw new Exception("Payer or payee not found.");
        }

        if (!payer.isActive() || !payee.isActive()) {
            throw new Exception("Either payer or payee account is not active.");
        }

        BankAccount primaryAccount = null;
        for (BankAccount account : payer.getBankAccounts()) {
            if (account.isPrimary()) {
                primaryAccount = account;
                break;
            }
        }

        if (primaryAccount == null) {
            throw new Exception("No primary bank account found for payer.");
        }

        if (primaryAccount.getBalance() < amount) {
            throw new Exception("Insufficient balance.");
        }

        Bank payerBank = banks.get(primaryAccount.getBankName());
        if (!payerBank.isServerUp()) {
            throw new Exception("Payer's bank server is down.");
        }

        Transaction transaction = new Transaction(payer, payee, amount, new Date(), "PENDING");
        boolean paymentStatus = pspService.processPayment(transaction);
        if (paymentStatus) {
            primaryAccount.setBalance(primaryAccount.getBalance() - amount);
            for (BankAccount account : payee.getBankAccounts()) {
                if (account.isPrimary()) {
                    account.setBalance(account.getBalance() + amount);
                    break;
                }
            }
        }
        transactions.add(transaction);
    }

    // Get transaction history
    public List<Transaction> getTransactionHistory(String phoneNumber) throws Exception {
        if (!users.containsKey(phoneNumber)) {
            throw new Exception("User not found.");
        }
        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getPayer().getPhoneNumber().equals(phoneNumber) || transaction.getPayee().getPhoneNumber().equals(phoneNumber)) {
                userTransactions.add(transaction);
            }
        }
        return userTransactions;
    }
    public User getUser(String phoneNumber) throws Exception {
        if (!users.containsKey(phoneNumber)) {
            throw new Exception("User not found.");
        }
        return users.get(phoneNumber);
    }
    public Bank getBank(String bankName) throws Exception {
    if (!banks.containsKey(bankName)) {
        throw new Exception("Bank not found.");
    }
    return banks.get(bankName);
    }
    public List<Transaction> searchTransactions(Date startDate, Date endDate, String status) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getDate().after(startDate) && transaction.getDate().before(endDate)
                    && transaction.getStatus().equalsIgnoreCase(status)) {
                result.add(transaction);
            }
        }
        return result;
    }

    // Handling pending payments transactions with retry mechanism
    public void handlePendingTransactions() {
        List<Transaction> pendingTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getStatus().equalsIgnoreCase("PENDING")) {
                pendingTransactions.add(transaction);
            }
        }

        for (Transaction transaction : pendingTransactions) {
            int retryCount = 0;
            while (retryCount < 3) { // Retry for a maximum of 3 times
                boolean paymentStatus = pspService.processPayment(transaction);
                if (paymentStatus) {
                    transaction.setStatus("COMPLETED");
                    break; // Transaction processed successfully, exit loop
                } else {
                    try {
                        Thread.sleep(40000); // Wait for 40 seconds before retrying
                        retryCount++;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if (retryCount == 3) {
                
                transaction.setStatus("FAILED");
            }
        }
    }
}


    


