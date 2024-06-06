import java.util.Date;

public class Transaction {
    private User payer;
    private User payee;
    private double amount;
    private Date date;
    private String status; // could be "SUCCESS", "FAILED", "PENDING"

    public Transaction(User payer, User payee, double amount, Date date, String status) {
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    // Getters and Setters

    public User getPayer() {
        return payer;
    }

    public User getPayee() {
        return payee;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
