import java.util.Arrays;

/**
 * Counting sort algorithm.
 * Works on positive integers only between 0 and k.
 * Stable.
 * Not in-place.
 * Running time <em>O(n+k)</em>.
 * Space complexity <em>O(n+k)</em>.
 */
public class CountingSort {
    /**
     * Sorts an array of integers using counting-sort algorithm.
     * @param a the array to be sorted
     */
    public static void sort(int[] a) {
        int k = max(a);
        int[] b = copy(a);
        sort(a, b, k);
    }

    /**
     * Sorts an array of integers using counting-sort algorithm.
     * @param a the array to be sorted
     * @param k the range of integers
     */
    public static void sort(int[] a, int k) {
        int[] b = copy(a);
        sort(a, b, k);
    }

    private static void sort(int[] a, int[] b, int k) {
        int[] c = new int[k];
        for (int x : a) c[x]++;
        for (int i = 1; i < c.length; i++) c[i] += c[i - 1];
        // Reverse order iteration is important to acheive stable sorting.
        // If done the other way around then equal elements will appear in
        // reversed order in the output array.
        for (int j = b.length - 1; j >= 0; j--) {
            a[c[b[j]] - 1] = b[j];
            c[b[j]]--;
        }
    }

    /**
     * Sort integers on their nth digit using counting sort.
     * @param a the array to sort
     * @param d the digit index to sort on
     */
    public static void digitSort(int[] a, int d) {
        int radix = 10;
        int[] b = copy(a);
        int[] c = new int[radix]; // 0-9 digits
        for (int x : a) c[getDigit(x, d)]++;
        for (int i = 1; i < c.length; i++) c[i] += c[i - 1];
        for (int j = b.length - 1; j >= 0; j--) {
            int digit = getDigit(b[j], d);
            a[c[digit] - 1] = b[j];
            c[digit]--;
        }
    }

    private static int getDigit(int a, int d) {
        int base = 10;
        int shift = (int) Math.pow(base, d);
        return (a / shift) % base;
    }

    private static int[] copy(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) b[i] = a[i];
        return b;
    }

    private static int max(int[] a) {
        int k = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > k) k = a[i];
        }
        return k + 1;
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        Integer[] t = InsertionSort.randomArray(16);
        int[] a = new int[t.length];
        for (int i = 0; i < t.length; i++) a[i] = t[i];
        int[] b = copy(a);
        System.out.println(Arrays.toString(a));
        System.out.println("k: " + max(a));
        sort(a);
        System.out.println(Arrays.toString(a));

        int x = 23;
        System.out.println(x + ": " + getDigit(x, 0) + " " + getDigit(x, 1));
        System.out.println(Arrays.toString(b));
        for (int d = 0; d < 2; d++) {
            digitSort(b, d);
            System.out.println(Arrays.toString(b));
        }
    }
}
