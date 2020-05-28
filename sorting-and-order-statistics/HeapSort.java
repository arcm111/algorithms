import java.util.Arrays;

public class HeapSort {
    public static <E extends Comparable<E>> void sort(E[] a) {
        Heap<E> heap = new MaxHeap<>(a);
        heap.sort();
    }

    public static void main(String[] args) {
        Integer[] a = InsertionSort.randomArray(16);
        System.out.println(Arrays.toString(a));
        sort(a);
        System.out.println(Arrays.toString(a));
    }
}
