import java.util.Arrays;

public class SelectionSort {
    /**
     * Sorts an array using selection-sort algorithm.
     * Running time is <em>O(n^2)</em> and space complexity is <em>O(1)</em>.
     * No best or worst case and running time is equal for all types of input.
     * @param a the array to sort.
     */
    public static <E extends Comparable<E>> void sort(E[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[min]) < 0) min = j;
            }
            if (min != i) exchange(a, i, min);
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
