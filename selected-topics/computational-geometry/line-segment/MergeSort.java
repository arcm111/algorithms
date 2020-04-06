public class MergeSort {
    public static <T extends Comparable<T>> void sort(T[] A) {
        sort(A, 0, A.length - 1);
    }

    private static <T extends Comparable<T>> void sort(T[] A, int p, int r) {
        if (p < r) {
            int q = (p + r) / 2;
            sort(A, p, q);
            sort(A, q + 1, r);
            merge(A, p, q, r);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> 
            void merge(T[] A, int p, int q, int r) {
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
            } else if (L[i].compareTo(R[j]) <= 0) {
                A[k] = L[i++];
            } else {
                A[k] = R[j++];
            }
        }
    }

    private static <T extends Comparable<T>> void printArray(T[] a) {
        for (T x : a) System.out.print(x + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] a = {1, 2, 4, 9, 7, 3, 6, 5};
        printArray(a);
        MergeSort.sort(a);
        printArray(a);
    }
}
