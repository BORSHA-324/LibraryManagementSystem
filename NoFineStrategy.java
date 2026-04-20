package strategy;

public class NoFineStrategy implements FineStrategy {
    @Override
    public double calculateFine(int daysLate) {
        return 0.0; // Teacher-der kono fine nei
    }
}