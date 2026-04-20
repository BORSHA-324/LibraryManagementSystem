package strategy;

public class PerDayFineStrategy implements FineStrategy {
    private double rate;

    public PerDayFineStrategy(double rate) {
        this.rate = rate;
    }

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * rate;
    }
}