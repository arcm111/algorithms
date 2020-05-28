import java.util.Arrays;

public class QuickSort {
    /**
     * Sorts an array using quick-sort algorithm.
     * Running time is <em>O(n^2)</em> in worst case, however in practice it 
     * is much faster and the expected running time is <em>O(nlgn)</em>.
     * Space complexity is <em>O(n)</em> which is the maximum possible depth
     * of the stack of any recursive call (n in case of already sorted array).
     * @param a the array to be sorted
     */
    public static <E extends Comparable<E>> void sort(E[] a) {
        sort(a, 0, a.length - 1);
    }

    /**
     * Tail-recursive variant of quick-sort algorithm.
     */
    public static <E extends Comparable<E>> void sortTailRecursive(E[] a) {
        sortTailRecursive(a, 0, a.length - 1);
    }

    /**
     * Quick-sort algorithm using Hoare's partitioning instead of Lomuto's.
     * Space complexity is <em>O(lgn)</em>.
     */
    public static <E extends Comparable<E>> void sortHoarePartitioning(E[] a) {
        sortHoarePartitioning(a, 0, a.length - 1);
    }

    /**
     * Sorts the subarray between p and r.
     * @param a the array to be sorted
     * @param p the first index (inclusive)
     * @param r the last index (inclusive)
     */
    private static <E extends Comparable<E>> void sort(E[] a, int p, int r) {
        if (p < r) {
            int q = partition(a, p, r);
            sort(a, p, q - 1);
            sort(a, q + 1, r);
        }
    }

    /**
     * Leumoto partition scheme for quick-sort.
     * Partitions an array to two subarrays sepearted by the pivot a[r], 
     * elements in left subarray are smaller than the pivot, while elements in 
     * the right subarray are larger.
     * @param a the array to partition
     * @param p the start index (inclusive)
     * @param r the end index (inclusive)
     * @return the new index of the pivot after partitioning
     */
    private static <E extends Comparable<E>> int 
            partition(E[] a, int p, int r) {
        E x = a[r];
        int i = p;
        for (int j = p; j < r; j++) {
            if (a[j].compareTo(x) < 0) {
                exchange(a, i, j);
                i++;
            }
        }
        exchange(a, i, r);
        return i;
    }

    private static <E extends Comparable<E>> void 
            sortHoarePartitioning(E[] a, int p, int r) {
        if (p < r) {
            int q = hoarePartition(a, p, r);
            sortHoarePartitioning(a, p, q);
            sortHoarePartitioning(a, q + 1, r);
        }
    }

    /**
     * Hoare partition scheme for quick-sort.
     * Partiions an array into two subarrays with elements in left subarray 
     * smaller than a[p] and elements in right subarray larger than a[p].
     * It is more efficient than Lomuto's partitioning especially on corner
     * cases such as already sorted and all equal elements. It never swaps on
     * a sorted array, while Lomuto's does n/2 swaps. In an all equal elements
     * array, the two pointers always meet in the middle, making the running 
     * time O(nlgn) instead of O(n^2) as compared to Lomuto's.
     * @param a the array to partition
     * @param p the start index (inclusive)
     * @param r the end index (inclusive)
     * @return the index of the last element in the left partition
     */
    private static <E extends Comparable<E>> int 
            hoarePartition(E[] a, int p, int r) {
        E x = a[p];
        int i = p - 1;
        int j = r + 1;
        while (true) {
            do i++; while (a[i].compareTo(x) < 0);
            do j--; while (a[j].compareTo(x) > 0);
            if (i >= j) return j;
            exchange(a, i, j);
        }
    }

    /**
     * Tail recursive variant of quick-sort.
     * Recurse over the left partition and iterate over the right one.
     */
    private static <E extends Comparable<E>> void 
            sortTailRecursive(E[] a, int p, int r) {
        while (p < r) {
            int q = partition(a, p, r);
            sortTailRecursive(a, p, q - 1);
            p = q + 1;
        }
    }

    private static <E extends Comparable<E>> void 
            exchange(E[] a, int i, int j) {
        E tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        System.out.println("Quicksort with Lomuto's partitioning: ");
        Integer[] a = InsertionSort.randomArray(16);
        System.out.println(Arrays.toString(a));
        sort(a);
        System.out.println(Arrays.toString(a));

        System.out.println("Quicksort with Hoare's partitioning: ");
        a = InsertionSort.randomArray(16);
        System.out.println(Arrays.toString(a));
        sortHoarePartitioning(a);
        System.out.println(Arrays.toString(a));

        System.out.println("Tail-recursive quicksort: ");
        a = InsertionSort.randomArray(16);
        System.out.println(Arrays.toString(a));
        sortTailRecursive(a);
        System.out.println(Arrays.toString(a));
    }
}
