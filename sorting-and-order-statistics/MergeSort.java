import java.util.Arrays;

public class MergeSort {
    /**
     * Merge sort.
     * Runtime <em>O(nlgn)</em>.
     * Space complexity <em>O(n)</em>. It's not O(nlng) because we do not add
     * up all the extra space used for merging, rather we only need to calculate
     * the maximum space used in any stack of a single recursive call which is
     * less than 3n.
     * @param a the array to be sorted
     */
    public static <E extends Comparable<E>> void sort(E[] A) {
        sort(A, 0, A.length - 1);
    }

    private static <E extends Comparable<E>> void sort(E[] A, int p, int r) {
        if (p < r) {
            int q = (p + r) / 2;
            sort(A, p, q);
            sort(A, q + 1, r);
            merge(A, p, q, r);
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Comparable<E>> 
            void merge(E[] A, int p, int q, int r) {
        int n1 = q - p + 1;
        int n2 = r - q;
        E[] L = (E[]) new Comparable[n1];
        E[] R = (E[]) new Comparable[n2];
        for (int i = 0; i < n1; i++) L[i] = A[p + i];
        for (int i = 0; i < n2; i++) R[i] = A[q + 1 + i];
        int i = 0;
        int j = 0;
        for (int k = p; k <= r; k++) {
            if (i >= n1) {
                A[k] = R[j++];
            } else if (j >= n2) {
                A[k] = L[i++];
            } else if (L[i].compareTo(R[j]) <= 0) {
                A[k] = L[i++];
            } else {
                A[k] = R[j++];
            }
        }
    }

    public static void main(String[] args) {
        Integer[] a = InsertionSort.randomArray(16);
        System.out.println(Arrays.toString(a));
        MergeSort.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
