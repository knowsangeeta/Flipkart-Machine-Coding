public class Bank {
    private String name;
    private boolean isServerUp;

    public Bank(String name, boolean isServerUp) {
        this.name = name;
        this.isServerUp = isServerUp;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public boolean isServerUp() {
        return isServerUp;
    }

    public void setServerUp(boolean serverUp) {
        isServerUp = serverUp;
    }
}
