package strategy;

import java.io.Serializable;

public interface FineStrategy extends Serializable {
    double calculateFine(int daysLate);
}