import java.util.Arrays;
import java.util.Comparator;

/**
 * A convex-hull of a set of points is the smallest convex polygon for which 
 * each point in the set is either on the boundary of the polygon or inside it.
 */
public class ConvexHull {
    /**
     * Graham's scan algorithm for solving convex-hull problem.
     * Maintains a stack of candidate points and remove all points that are 
     * not on the convex-hull one at a time until the stack contains only the
     * convex-hull points in counter-clockwise order.
     * Running time is <em>O(nlgn)</em>
     * @param Q the set of points to find its convex-hull
     * @return convex-hull polygon points in counter-clockwise order
     * @throws IllegalArgumentException if the there isn't enough points
     */
    public static Iterable<Point> grahamScan(Point[] Q) {
        MergeSort.sort(Q, new Point.PositionComparator());
        Point p0 = Q[0];
        Point[] sorted = Arrays.copyOfRange(Q, 1, Q.length);
        MergeSort.sort(sorted, new Point.PolarComparator(p0));
        Point[] filtered = filterCollinearPoints(sorted, p0);
        if (filtered.length < 2) {
            throw new IllegalArgumentException("convex hull is empty");
        }
        GrahamStack<Point> S = new GrahamStack<>();
        S.push(p0);
        S.push(filtered[0]);
        S.push(filtered[1]);
        for (int i = 2; i < filtered.length; i++) {
            Point pi = filtered[i];
            while (nonleftTurn(S.nextToTop(), S.top(), pi)) {
                S.pop();
            }
            S.push(pi);
        }
        return S;
    }

    /**
     * Jarvis's march algorithm for solving convex-hull problem.
     * Finds the convex-hull using a technique called "package wrapping", 
     * stating with the lowest point in the set, it finds the point with the
     * smallest polar angle and add it to the convex-hull, then it finds the 
     * point with smallest polar angle with this new point and add it to the
     * convex-hull and so on until all points in convex-hull are discovered.
     * Running time is <em>O(nh)</em> where h is the number of points in the
     * convex hull.
     * @param Q the set of points to find its convex-hull
     * @return convex-hull polygon points in counter-clockwise order
     * @throws IllegalArgumentException if Q contains less than 3 points
     */
    public static Iterable<Point> jarvisMarch(Point[] Q) {
        if (Q.length < 3) {
            throw new IllegalArgumentException("not enough points");
        }
        int lowestInd = 0;
        Comparator<Point> cmp = new Point.PositionComparator();
        for (int i = 1; i < Q.length; i++) {
            if (cmp.compare(Q[i], Q[lowestInd]) < 0) {
                lowestInd = i;
            }
        }
        int pInd = lowestInd;
        Stack<Point> CH = new Stack<>();
        do {
            Comparator<Point> cmp2 = new Point.PolarComparator(Q[pInd]);
            int j = 0; // index of point with smallest polar angle
            for (int k = 1; k < Q.length; k++) {
                if (k == pInd) continue;
                if (cmp2.compare(Q[k], Q[j]) < 0) {
                    j = k;
                } else if (cmp2.compare(Q[k], Q[j]) == 0) {
                    double d1 = Q[pInd].squareDistanceTo(Q[k]);
                    double d2 = Q[pInd].squareDistanceTo(Q[j]);
                    if (d1 > d2) {
                        j = k;
                    }
                }
            }
            pInd = j;
            CH.push(Q[pInd]);
        } while (pInd != lowestInd);
        return CH;
    }

    /**
     * Checks whether a segment is colinear or clockwise from another one.
     * Two segments are colinear if their cross-product is 0 and the second
     * segment is clockwise from the first segment if the product is negative.
     * Running time is <em>O(1)</em>.
     * @param p0 origin point
     * @param p1 end-point of the first segment
     * @param p2 end-point of the second segment
     * @return true if p0p2 is colinear or clockwise from p0p1
     */
    private static boolean nonleftTurn(Point p0, Point p1, Point p2) {
        return Point.crossProduct(p0, p1, p2) >= 0;
    }

    /**
     * Finds points with same polar angle with p0 and removes them all except 
     * the one that is farthest from p0.
     * The points are sorted by polar angle corresponding to p0, therefore,
     * if there exist collinear points they must be next to each other
     * in the input array.
     * Running time is <em>Theta(n)</em>.
     * @param a the input array of sorted points
     * @param p0 the source point which all other points are sorted around
     * @return the filtered points
     */
    private static Point[] filterCollinearPoints(Point[] a, Point p0) {
        Comparator<Point> cmp = new Point.PolarComparator(p0);
        int n = a.length;
        int j = 0;
        for (int i = 0; i < n; i++) {
            Point p1 = a[i];
            while (i < n - 1 && cmp.compare(a[i], a[i + 1]) == 0) {
                Point p2 = a[i + 1];
                if (p0.squareDistanceTo(p2) > p0.squareDistanceTo(p1)) {
                    p1 = p2;
                    i++;
                }
            }
            a[j++] = p1;
        }
        return Arrays.copyOf(a, j);
    }

    /**
     * A modified stack data-structue which allows peeking into the top item in
     * the stack and the one below it without removing them from the stack.
     */
    private static class GrahamStack<T> extends Stack<T> {
        public T top() {
            return top.item;
        }

        public T nextToTop() {
            return top.next.item;
        }
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        Point[] pts = new Point[18];
        pts[0] = new Point(-7, 8);
        pts[1] = new Point(-4, 6);
        pts[2] = new Point(2, 6);
        pts[3] = new Point(6, 4);
        pts[4] = new Point(8, 6);
        pts[5] = new Point(7, -2);
        pts[6] = new Point(4, -6);
        pts[7] = new Point(8, -7);
        pts[8] = new Point(0, 0);
        pts[9] = new Point(3, -2);
        pts[10] = new Point(6, -10);
        pts[11] = new Point(0, -6);
        pts[12] = new Point(-9, -5);
        pts[13] = new Point(-8, -2);
        pts[14] = new Point(-8, 0);
        pts[15] = new Point(-10, 3);
        pts[16] = new Point(-2, 2);
        pts[17] = new Point(-10, 4);
        System.out.println("Convex hull[Graham]: ");
        for (Point p : grahamScan(pts)) {
            System.out.println(p);
        }

        pts = new Point[100];
        pts[0] = new Point(9230, 13137);
        pts[1] = new Point(4096, 24064);
        pts[2] = new Point(8192, 26112);
        pts[3] = new Point(22016, 9344);
        pts[4] = new Point(4440, 8028);
        pts[5] = new Point(6505, 31422);
        pts[6] = new Point(28462, 32343);
        pts[7] = new Point(17152, 19200);
        pts[8] = new Point(9561, 11599);
        pts[9] = new Point(4096, 20992);
        pts[10] = new Point(21538, 2430);
        pts[11] = new Point(21903, 23677);
        pts[12] = new Point(17152, 16128);
        pts[13] = new Point(7168, 25088);
        pts[14] = new Point(10162, 18638);
        pts[15] = new Point(822, 32301);
        pts[16] = new Point(16128, 12032);
        pts[17] = new Point(18989, 3797);
        pts[18] = new Point(8192, 28160);
        pts[19] = new Point(16128, 20224);
        pts[20] = new Point(14080, 20224);
        pts[21] = new Point(26112, 7296);
        pts[22] = new Point(20367, 20436);
        pts[23] = new Point(7486, 422);
        pts[24] = new Point(17835, 2689);
        pts[25] = new Point(22016, 3200);
        pts[26] = new Point(22016, 5248);
        pts[27] = new Point(24650, 16886);
        pts[28] = new Point(15104, 20224);
        pts[29] = new Point(25866, 4204);
        pts[30] = new Point(13056, 15104);
        pts[31] = new Point(13662, 10301);
        pts[32] = new Point(17152, 20224);
        pts[33] = new Point(15104, 12032);
        pts[34] = new Point(6144, 20992);
        pts[35] = new Point(26112, 3200);
        pts[36] = new Point(6144, 29184);
        pts[37] = new Point(13056, 12032);
        pts[38] = new Point(8128, 20992);
        pts[39] = new Point(5076, 19172);
        pts[40] = new Point(17152, 17152);
        pts[41] = new Point(823, 15895);
        pts[42] = new Point(25216, 3200);
        pts[43] = new Point(6071, 29161);
        pts[44] = new Point(5120, 20992);
        pts[45] = new Point(10324, 22176);
        pts[46] = new Point(29900, 9390);
        pts[47] = new Point(27424, 7945);
        pts[48] = new Point(4096, 23040);
        pts[49] = new Point(12831, 27971);
        pts[50] = new Point(29860, 12437);
        pts[51] = new Point(28668, 2061);
        pts[52] = new Point(1429, 12561);
        pts[53] = new Point(29413, 596);
        pts[54] = new Point(17152, 18176);
        pts[55] = new Point(8192, 27136);
        pts[56] = new Point(5120, 29184);
        pts[57] = new Point(22016, 11392);
        pts[58] = new Point(1444, 10362);
        pts[59] = new Point(32011, 3140);
        pts[60] = new Point(15731, 32661);
        pts[61] = new Point(26112, 4224);
        pts[62] = new Point(13120, 20224);
        pts[63] = new Point(30950, 2616);
        pts[64] = new Point(4096, 22016);
        pts[65] = new Point(4096, 25088);
        pts[66] = new Point(24064, 3200);
        pts[67] = new Point(26112, 5248);
        pts[68] = new Point(4862, 30650);
        pts[69] = new Point(5570, 8885);
        pts[70] = new Point(21784, 18853);
        pts[71] = new Point(23164, 32371);
        pts[72] = new Point(4160, 29184);
        pts[73] = new Point(13056, 13056);
        pts[74] = new Point(8192, 29184);
        pts[75] = new Point(23040, 7296);
        pts[76] = new Point(5120, 25088);
        pts[77] = new Point(22016, 7296);
        pts[78] = new Point(7168, 29184);
        pts[79] = new Point(25216, 7296);
        pts[80] = new Point(23040, 3200);
        pts[81] = new Point(4718, 4451);
        pts[82] = new Point(14080, 16128);
        pts[83] = new Point(7168, 20992);
        pts[84] = new Point(19546, 17728);
        pts[85] = new Point(13056, 16128);
        pts[86] = new Point(17947, 17017);
        pts[87] = new Point(26112, 6272);
        pts[88] = new Point(20658, 1204);
        pts[89] = new Point(23553, 13965);
        pts[90] = new Point(13056, 14080);
        pts[91] = new Point(14080, 12032);
        pts[92] = new Point(24064, 7296);
        pts[93] = new Point(21377, 26361);
        pts[94] = new Point(17088, 12032);
        pts[95] = new Point(16128, 16128);
        pts[96] = new Point(30875, 28560);
        pts[97] = new Point(2542, 26201);
        pts[98] = new Point(8192, 25088);
        pts[99] = new Point(11444, 16973);
        System.out.println("Convex hull[Jarvis]: ");
        for (Point p : jarvisMarch(pts)) {
            System.out.println(p);
        }
    }
}
