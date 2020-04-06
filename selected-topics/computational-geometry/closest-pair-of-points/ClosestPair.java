import java.util.Arrays;

public class ClosestPair {
    /**
     * Finds the pair of points with shortest distance from a set.
     * Uses divide and conquer technique.
     * Running time is <em>O(nlgn)</em>.
     * @param Q the set of points
     * @return the closes pair of points
     */
    public static Pair find(Point[] Q) {
        Point[] X = Arrays.copyOf(Q, Q.length);
        Point[] Y = Arrays.copyOf(Q, Q.length);
        MergeSort.sort(X, new Point.XPositionComparator());
        MergeSort.sort(Y, new Point.YPositionComparator());
        return findRecursive(X, Y, 0, Q.length);
    }

    /**
     * Recursive invocation of the Closest-pair algorithm which carries out
     * the divide-and-conquer paradigm.
     * Finds the closest pair of points in X[p...q-1] and Y[p...q-1].
     * @param X the set of points monotically increasing by x coordinates
     * @param Y the set of points monotically increasing by y coordinates
     * @param p the first index of the subset in X and Y (inclusive)
     * @param q the last index of the subset in X and Y (exclusive)
     * @return the closest pair in the subset
     */
    private static Pair findRecursive(Point[] X, Point[] Y, int p, int q) {
        int n = q - p;
        System.out.println(p + ":" + q);
        if (n <= 3) {
            return bruteForceFind(X, p, q);
        }

        int mid = p + n / 2;
        Pair pL = findRecursive(X, Y, p, mid);
        Pair pR = findRecursive(X, Y, mid, q);
        double delta = pL.distance();
        Pair closestPair = pL;
        if (pR.distance() < delta) {
            closestPair = pR;
            delta = pR.distance();
        }

        int m = 0;
        double centerX = X[mid].x;
        Point[] YPrime = new Point[n];
        // find points in the vertical strip around the central line
        for (int i = 0; i < n; i++) {
            if (Y[i].x > (centerX - delta) && Y[i].x < (centerX + delta)) {
                YPrime[m++] = Y[i];
            }
        }

        // At most 8 points in the vertical strip can have distances less than
        // or equal to delta, therefore, at most 7 comparisions.
        for (int i = 0; i < m; i++) {
            int k = Math.min(i + 8, m);
            for (int j = i + 1; j < k; j++) {
                if (YPrime[i].distanceTo(YPrime[j]) < delta) {
                    closestPair = new Pair(YPrime[i], YPrime[j]);
                    delta = closestPair.distance();
                }
            }
        }

        return closestPair;
    }


    /**
     * Brute force algorithm for finding closest pair of points in a set.
     * Compares all pairs of points in a set.
     * Running time is <em>Theta(n^2)</em> (binomial (n 2) "n choose 2").
     * @param P an array of points containing the subset to inspect
     * @param p the start index of the subset (inclusive)
     * @param q the last index of the subset (execlusive)
     * @return the closes pair of points in the subset
     */
    public static Pair bruteForceFind(Point[] P, int p, int q) {
        Pair closestPair = null;
        double d2 = Double.POSITIVE_INFINITY;
        for (int i = p; i < q; i++) {
            for (int j = i + 1; j < q; j++) {
                if (P[i].distanceTo(P[j]) < d2) {
                    closestPair = new Pair(P[i], P[j]);
                    d2 = closestPair.distance();
                }
            }
        }
        return closestPair;
    }

    public static void main(String[] args) {
        Point[] a = {new Point(2, 2), new Point(3, 1), new Point(5, 2)};
        System.out.println(bruteForceFind(a, 0, a.length));
        Point[] points = new Point[20];
        points[0] = new Point(-13, -15);
        points[1] = new Point(-2, 19);
        points[2] = new Point(20, 13);
        points[3] = new Point(-1, 3);
        points[4] = new Point(-18, -9);
        points[5] = new Point(9, 17);
        points[6] = new Point(-11, 2);
        points[7] = new Point(13, -13);
        points[8] = new Point(-18, -4);
        points[9] = new Point(7, 7);
        points[10] = new Point(13, -8);
        points[11] = new Point(-9, -16);
        points[12] = new Point(1, 10);
        points[13] = new Point(-6, -14);
        points[14] = new Point(10, -10);
        points[15] = new Point(-4, -15);
        points[16] = new Point(-1, 14);
        points[17] = new Point(0, 10);
        points[18] = new Point(6, -7);
        points[19] = new Point(20, -10);
        System.out.println(ClosestPair.find(points));
    }
}
