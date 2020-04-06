import java.util.Comparator;

public class MergeSort {
    public static <T extends Comparable<T>> void sort(T[] A) {
        sort(A, 0, A.length - 1, null);
    }

    public static <T extends Comparable<T>, E extends Comparator<T>> 
            void sort(T[] A, E cmp) {
        sort(A, 0, A.length - 1, cmp);
    }

    private static <T extends Comparable<T>, E extends Comparator<T>> 
            void sort(T[] A, int p, int r, E cmp) {
        if (p < r) {
            int q = (p + r) / 2;
            sort(A, p, q, cmp);
            sort(A, q + 1, r, cmp);
            merge(A, p, q, r, cmp);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>, E extends Comparator<T>> 
            void merge(T[] A, int p, int q, int r, E cmp) {
        int n1 = q - p + 1;
        int n2 = r - q;
        T[] L = (T[]) new Comparable[n1];
        for (int i = 0; i < n1; i++) {
            L[i] = A[p + i];
        }
        T[] R = (T[]) new Comparable[n2];
        for (int i = 0; i < n2; i++) {
            R[i] = A[q + 1 + i];
        }
        int i = 0;
        int j = 0;
        for (int k = p; k <= r; k++) {
            if (i >= n1) {
                A[k] = R[j++];
            } else if (j >= n2) {
                A[k] = L[i++];
            } else if (compare(L[i], R[j], cmp) <= 0) {
                A[k] = L[i++];
            } else {
                A[k] = R[j++];
            }
        }
    }

    private static <T extends Comparable<T>> 
            int compare(T a, T b, Comparator<T> cmp) {
        if (cmp == null) return a.compareTo(b);
        return cmp.compare(a, b);
    }

    public static <T extends Comparable<T>> void printArray(T[] a) {
        for (T x : a) System.out.print(x + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] a = {1, 2, 4, 9, 7, 3, 6, 5};
        Integer[] b = {1, 2, 4, 9, 7, 3, 6, 5};
        printArray(a);
        MergeSort.sort(a);
        printArray(a);

        Comparator<Integer> reverseComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                if (a > b) return -1;
                if (b > a) return 1;
                return 0;
            }
        };

        MergeSort.sort(b, reverseComparator);
        printArray(b);
    }
}
