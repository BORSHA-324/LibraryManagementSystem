import java.util.List;

public interface DataOperationStrategy<T> {
    void execute(T item, List<T> list, int index);
}