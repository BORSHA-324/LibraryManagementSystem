import java.util.List;

public class AddStrategy<T> implements DataOperationStrategy<T> {
    @Override
    public void execute(T item, List<T> list, int index) {
        list.add(item);
    }
}