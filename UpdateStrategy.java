import java.util.List;

public class UpdateStrategy<T> implements DataOperationStrategy<T> {
    @Override
    public void execute(T item, List<T> list, int index) {
        if (index >= 0 && index < list.size()) {
            list.set(index, item);
        }
    }
}