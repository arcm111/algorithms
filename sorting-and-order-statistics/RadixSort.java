import java.util.Arrays;
import java.util.Random;

/**
 * Radix sort algorithm.
 * Requries a stable digit sorting algorithm in order to work correctly.
 * Not stable.
 * Not in place.
 * Does not make effective use of hardware caches like quicksort.
 * Running time <em>O(d(n+k))</em> where d is the number of digits, k is the
 * Space complexity <em>O(n+k)</em> (allocated during counting-sort).
 * k is the radix and n is the size of the array.
 */
public class RadixSort {
    /**
     * Radix sort implementation using counting-sort for digit sorting.
     * @param a the array to sort
     */
    public static void sort(int[] a) {
        int largest = 0;
        int digits = 0;
        for (int x : a) largest = Math.max(x, largest);
        for (int k = largest; k > 0; k /= 10) digits++;
        for (int d = 0; d < digits; d++) {
            CountingSort.digitSort(a, d);
        }
    }

    public static void main(String[] args) {
        int[] a = new int[16];
        Random rand = new Random();
        for (int i = 0; i < a.length; i++) a[i] = rand.nextInt(1024);
        System.out.println(Arrays.toString(a));
        sort(a);
        System.out.println(Arrays.toString(a));
    }
}
