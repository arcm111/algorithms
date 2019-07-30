import java.util.Random;

/**
 * vEB Node.
 * It is basically a vEB tree of size u.
 * The constructor is recursive, it will create the main tree
 * and all sub-trees, until it reaches the base-case vEB tree, where
 * it will set the {@code summary} and {@code cluster} to null and exit.
 * The constructor will also recursively create the summary trees and
 * sub-trees in the same way it creats the vEB trees.
 *
 * Each vEB tree(node) contains a cluster of size {@code u_sqrt} vEB trees,
 * each cluster tree has a universe size of {@code l_sqrt}.
 * As well as clusters, a vEB tree(node) has A summary vEB node(tree) with a 
 * universe size of {@code u_sqrt}.
 */
public class VEBNode {
    private static final int NIL = -1;
    public final int u; // universe size
    public int min = NIL;
    public int max = NIL;
    public final VEBNode summary;
    public final VEBNode[] cluster;

    private final int u_sqrt; // upper square root of u
    private final int l_sqrt; // upper square root of u

    public VEBNode(int u) {
        this.u = u;
        this.u_sqrt = upperSqaureRoot(u);
        this.l_sqrt = lowerSquareRoot(u);
        if (u == 2) {
            this.cluster = null;
            this.summary = null;
        } else {
            this.cluster = new VEBNode[u_sqrt];
            int l_sqrt = lowerSquareRoot(u);
            for (int i = 0; i < u_sqrt; i++) {
                this.cluster[i] = new VEBNode(l_sqrt);
            }
            this.summary = new VEBNode(u_sqrt);
        }
    }

    private int upperSqaureRoot(int u) {
        double lg_u = Math.log10(u) / Math.log10(2);
        return (int) Math.pow(2, Math.ceil(lg_u / 2));
    }

    private int lowerSquareRoot(int u) {
        double lg_u = Math.log10(u) / Math.log10(2);
        return (int) Math.pow(2, Math.floor(lg_u / 2));
    }

    /**
     * Determins the most significant [lgu/2]-bits of an int x.
     *
     * @param x an integer with a maximum length of lgu-bits.
     * @return the most significant [lgu/2] bits.
     */
    public int high(int x) {
        return x / l_sqrt;
    }

    /**
     * Determins the least significant [lgu/2]-bits of an int x.
     *
     * @param x an integer with a maximum length of lgu-bits.
     * @return the least significant [lgu/2] bits.
     */
    public int low(int x) {
        return x % l_sqrt;
    }

    /**
     * Reconstructs original integer from its most significant
     * and least significant bits values.
     *
     * @param high most significant bits.
     * @param low  least significant bits.
     * @return original integer x.
     */
    public int index(int high, int low) {
        return high * l_sqrt + low;
    }

    /**
     * Returns the size of the cluster (number of child nodes).
     */
    public int size() {
        return u_sqrt;
    }

    public void unit_test() {
        System.out.println("u = " + u);
        System.out.println("upper square root = " + u_sqrt);
        System.out.println("lower square root = " + l_sqrt);
        Random rand = new Random();
        int x = rand.nextInt(l_sqrt) * l_sqrt + rand.nextInt(l_sqrt);
        System.out.println("x = " + x);
        System.out.println("high(x) = " + high(x));
        System.out.println("low(x) = " + low(x));
        System.out.println("x index test: " + index(high(x), low(x)));
    }

    public static void main(String[] args) {
        // Perform unit testing.
        VEBNode x = new VEBNode(32);
        x.unit_test();
    }
}
