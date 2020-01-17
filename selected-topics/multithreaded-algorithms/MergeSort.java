public class MergeSort {
    private static <T extends Comparable<T>> 
            void serialMergesort(T[] A, int p, int r) {
        if (p < r) {
            int q = (p + r) / 2;
            serialMergesort(A, p, q);
            serialMergesort(A, q + 1, r);
            serialMerge(A, p, q, r);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> 
            void serialMerge(T[] A, int p, int q, int r) {
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

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>>
            void parallelMergesort(final T[] A, final int p, final int r, 
                    final T[] B, final int s) {
        final int n = r - p + 1;
        if (n == 1) {
            B[s] = A[p];
        } else {
            final int q = (p + r) / 2;
            final int q2 = q - p;
            final T[] C = (T[]) new Comparable[n];
            Thread t = new Thread() {
                @Override
                public void run() {
                    parallelMergesort(A, p, q, C, 0);
                }
            };
            t.start();
            parallelMergesort(A, q + 1, r, C, q2 + 1);
            try {
                t.join();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            parallelMerge(C, 0, q2, q2 + 1, n - 1, B, s);
        }
    }

    private static <T extends Comparable<T>> 
            void parallelMerge(final T[] C, int p1, int r1, int p2, int r2, 
                    final T[] B, final int p3) {
        int n1 = r1 - p1 + 1;
        int n2 = r2 - p2 + 1;
        if (n1 < n2) {
            int _p = p1;
            int _r = r1;
            int _n = n1;
            p1 = p2;
            r1 = r2;
            n1 = n2;
            p2 = _p;
            r2 = _r;
            n2 = _n;
        }
        if (n1 == 0) return;
        final int q1 = (p1 + r1) / 2;
        final int q2 = binarySearch(C[q1], C, p2, r2);
        final int _p1 = p1;
        final int _p2 = p2;
        int q3 = p3 + (q1 - p1) + (q2 - p2);
        B[q3] = C[q1];
        Thread t = new Thread() {
            @Override
            public void run() {
                parallelMerge(C, _p1, q1 - 1, _p2, q2 - 1, B, p3);
            }
        };
        t.start();
        parallelMerge(C, q1 + 1, r1, q2, r2, B, q3 + 1);
        try {
            t.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static <T extends Comparable<T>> 
            int binarySearch(T x, T[] C, int p, int r) {
        int low = p;
        int high = (p > r + 1) ? p : r + 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (x.compareTo(C[mid]) <= 0) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return high;
    }

    public static <T extends Comparable<T>> void serialMergesort(T[] A) {
        serialMergesort(A, 0, A.length - 1);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> void parallelMergesort(T[] A) {
        T[] B = (T[]) new Comparable[A.length];
        parallelMergesort(A, 0, A.length - 1, B, 0);
        for (int i = 0; i < B.length; i++) {
            A[i] = B[i];
        }
    }

    private static <T extends Comparable<T>> void printArray(T[] a) {
        for (T x : a) System.out.print(x + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] a = {1, 2, 4, 9, 7, 3, 6, 5};
        System.out.println("Serial: ");
        printArray(a);
        MergeSort.serialMergesort(a);
        printArray(a);

        System.out.println();

        a = new Integer[]{1, 2, 4, 9, 7, 3, 6, 5};
        System.out.println("Parallel: ");
        printArray(a);
        MergeSort.parallelMergesort(a);
        printArray(a);
    }
}
