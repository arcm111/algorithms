import java.util.Arrays;

public class BubbleSort {
    public static <E extends Comparable<E>> void sort(E[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j].compareTo(a[j + 1]) > 0) exchange(a, j, j + 1);
            }
        }
    }

    private static <E extends Comparable<E>> void 
            exchange(E[] a, int i, int j) {
        E tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        Integer[] a = InsertionSort.randomArray(16);
        System.out.println(Arrays.toString(a));
        sort(a);
        System.out.println(Arrays.toString(a));
    }
}
