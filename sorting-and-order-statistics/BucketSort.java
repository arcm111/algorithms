import java.util.Random;

/**
 * Bucket-sort algorithm.
 * Assumes that the input is drawn from a uniform distribution (uniformly
 * and independently randomized input elements over a range of [0, 1)).
 * Works on doubles between 0 (inclusive) and 1 (execlusive).
 * Running time is <em>O(n)</em>.
 * Space complexity is <em>O(n)</em>.
 */
public class BucketSort {
    /**
     * Sorts a list of double between [0, 1) using bucket-sort algorithm.
     * @param a the list to be sorted
     */
    @SuppressWarnings("unchecked")
    public static void sort(double[] a) {
        int n = a.length;
        LinkedList<Double>[] b = (LinkedList<Double>[]) new LinkedList[n];
        for (int i = 0; i < n; i++) {
            b[i] = new LinkedList<Double>();
        }
        for (double x : a) b[(int) (n * x)].add(x);
        for (LinkedList<Double> list : b) list.insertionSort();
        int i = 0;
        for (LinkedList<Double> list : b) {
            for (Double x : list) a[i++] = x;
        }
    }

    private static void printArray(double[] a) {
        System.out.print("[");
        for (int i = 0; i < a.length - 1; i++) {
            System.out.print(String.format("%.2f, ", a[i]));
        }
        System.out.println(String.format("%.2f", a[a.length - 1]) + "]");
    }

    public static void main(String[] args) {
        Random rand = new Random();
        double[] a = new double[10];
        for (int i = 0; i < a.length; i++) a[i] = rand.nextDouble();
        printArray(a);
        sort(a);
        printArray(a);
    }
}
