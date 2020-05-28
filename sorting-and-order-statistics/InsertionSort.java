import java.lang.System;
import java.util.Arrays;
import java.util.Random;

/**
 * Insertion sort algorithm.
 * Works well on small arrays (because it has a small overhead), as well as, 
 * partially sorted arrays (due to small number of comparisions and swaps).
 * Ideal in situations where elements arrive one at a time or when new elments 
 * are added to the sorted array, in such cases it takes linear time to sort the
 * new arrays.
 * Makes excellent use of system caches and uses minimal amount of memory.
 * Has low overhead.
 * Stable.
 * In-place.
 * Running time is <em>O(n^2)</em>.
 * Best case running time is <em>O(n)</em> when the array is already sorted.
 * Space complexity is <em>O(1)</em>.
 */
public class InsertionSort {
    /**
     * Sorts an array using insertion-sort algorithm.
     * @param a the list to be sorted
     */
    private static <E extends Comparable<E>> void sort(E[] a) {
        for (int j = 1; j < a.length; j++) {
            E key = a[j];
            int i = j - 1;
            while (i >= 0 && a[i].compareTo(key) > 0) {
                a[i + 1] = a[i];
                i--;
            }
            a[i + 1] = key;
        }
    }

    public static Integer[] randomArray(int n) {
        Random rand = new Random();
        Integer[] a = new Integer[n];
        for (int i = 0; i < a.length; i++) a[i] = rand.nextInt(n * 2);
        return a;
    }

    public static void main(String[] args) {
        Integer[] a = randomArray(16);
        System.out.println("Unsorted: " + Arrays.toString(a));
        sort(a);
        System.out.println("Sorted: " + Arrays.toString(a));
    }
}
