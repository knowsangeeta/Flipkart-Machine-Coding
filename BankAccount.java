public class BankAccount {
    private String bankName;
    private String accountNumber;
    private double balance;
    private boolean isPrimary;

    public BankAccount(String bankName, String accountNumber, double balance, boolean isPrimary) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.isPrimary = isPrimary;
    }

    // Getters and Setters

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
