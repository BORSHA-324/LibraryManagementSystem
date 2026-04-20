package strategy;

public class FixedFineStrategy implements FineStrategy {
    private double amount;

    public FixedFineStrategy(double amount) {
        this.amount = amount;
    }

    @Override
    public double calculateFine(int daysLate) {
        if (daysLate > 0) {
            return amount;
        } else {
            return 0.0;
        }
    }
}