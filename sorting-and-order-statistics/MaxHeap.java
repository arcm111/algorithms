import java.util.Arrays;

public class MaxHeap<E extends Comparable<E>> extends Heap<E> {
    public MaxHeap(E[] items) {
        super(items);
    }

    @Override
    protected boolean shouldExchange(E a, E b) {
        return a.compareTo(b) > 0;
    }

    private static <T extends Comparable<T>> void sort(T[] items) {
        MaxHeap<T> heap = new MaxHeap<>(items);
        heap.sort();
    }

    public static void main(String[] args) {
        Integer[] items = new Integer[] {7, 2, 4, 1, 9, 8, 5, 3, 6};
        System.out.println(Arrays.toString(items));
        MaxHeap.sort(items);
        System.out.println(Arrays.toString(items));
    }
}
