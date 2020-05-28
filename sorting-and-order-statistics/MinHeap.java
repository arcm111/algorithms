import java.util.Arrays;

public class MinHeap<E extends Comparable<E>> extends Heap<E> {
    public MinHeap(E[] items) {
        super(items);
    }

    @Override
    protected boolean shouldExchange(E a, E b) {
        return a.compareTo(b) < 0;
    }

    private static <T extends Comparable<T>> void sort(T[] items) {
        MinHeap<T> heap = new MinHeap<>(items);
        heap.sort();
    }

    public static void main(String[] args) {
        Integer[] items = new Integer[] {7, 2, 4, 1, 9, 8, 5, 3, 6};
        System.out.println(Arrays.toString(items));
        MinHeap.sort(items);
        System.out.println(Arrays.toString(items));
    }
}
