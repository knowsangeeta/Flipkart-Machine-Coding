public class PSPService {
    public boolean processPayment(Transaction transaction) {
        // payment processing delay
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Randomly determine if transaction is successful or pending
        double random = Math.random();
        if (random < 0.7) {
            transaction.setStatus("SUCCESS");
            return true;
        } else if (random < 0.9) {
            transaction.setStatus("PENDING");
            return false;
        } else {
            transaction.setStatus("FAILED");
            return false;
        }
    }
}
