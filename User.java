import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String phoneNumber;
    private boolean isActive;
    private List<BankAccount> bankAccounts;
    
    public User(String name, String phoneNumber, boolean isActive) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.bankAccounts = new ArrayList<>();
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void addBankAccount(BankAccount account) {
        bankAccounts.add(account);
    }
}
