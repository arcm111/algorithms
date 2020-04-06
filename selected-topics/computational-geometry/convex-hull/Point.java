import java.util.Comparator;

/**
 * Convex-Hull Point class.
 * Stores coordinates and can be sorted position or polar angle with an origin.
 */
public class Point implements Comparable<Point> {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * If segments p1p2 and p2p3 have the same slopes then p1, p2 and p3 are
     * colinear.
     * Slope(p1p2) = (y2-y1)/(x2-x1), slope(p1p3) = (y3-y1)/(x3-x1),
     * therefore, if slope(p1p2) == slope(p1p3), then:
     * (y2-y1)*(x3-x1) == (y3-y1)*(x2-x1). We use this formula instead of
     * comparing slopes because it avoids divisions by 0.
     *
     * Another way is to check the cross product of p1p2 and p2p3 which should
     * equal 0 when p1, p2 and p3 are colinear:
     * (x2-x1)(y3-y1) - (x3-x1)(y2-y1) = 0
     */
    public static boolean colinear(Point p1, Point p2, Point p3) {
        return (p2.y - p1.y) * (p3.x - p1.x) == (p3.y - p1.y) * (p2.x - p1.x);
    }

    public double squareDistanceTo(Point that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;
        return dx * dx + dy * dy;
    }

    /**
     * Computes the cross product of two segments p0p1, p0p2.
     * <p>The cross product represents the area of the paralleogram determined
     * by the two segments.
     * <p>The length of the cross product is equal to |p0p1|*|p0p2|*sin(theta), 
     * where theta is the angle between p0p1 and p0p2.
     * <p>If the cross product p1xp2 is negative then p2 is clockwise from p1
     * with respect to the origin p0 and p2 makes a right turn, if the cross 
     * product is positive, however, then p2 is counter-clockwise from p1 and 
     * p2 makes a left turn. Otherwise if the cross product is zero then p0, 
     * p1 and p2 are colinear.
     * @param p0 the origin point
     * @param p1 the first point
     * @param p2 the second point
     * @return the cross product p0p1 x p0p2
     */
    public static double crossProduct(Point p0, Point p1, Point p2) {
        return (p1.x - p0.x) * (p2.y - p0.y) - (p2.x - p0.x) * (p1.y - p0.y);
    }

    /**
     * Sorts points by increasing y coordinates and break ties by putting 
     * left point before right point.
     */
    public static class PositionComparator implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {
            if (a.y < b.y) return -1;
            if (a.y > b.y) return 1;
            if (a.x < b.x) return -1;
            if (a.x > b.x) return 1;
            return 0;
        }
    }

    /**
     * Sorts points by their polar angle with a given origin point.
     * It does not use polar angle to compare two points, instead it computes 
     * the cross products of these two consecutive segments joined at the origin
     * and puts the point whose segment is clockwise from the other before 
     * the other segment.
     * @param p0 the origin point
     */
    public static class PolarComparator implements Comparator<Point> {
        private final Point p0;

        public PolarComparator(Point p0) {
            this.p0 = p0;
        }

        @Override
        public int compare(Point p1, Point p2) {
            double v = crossProduct(p0, p1, p2);
            if (v < 0) return -1;
            if (v > 0) return 1;
            return 0;
        }
    }

    /**
     * Not needed Comparator.
     * PositionComparator and PolarComparator are used instead depending on
     * requirement.
     */
    @Override
    public int compareTo(Point that) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        Point that = (Point) obj;
        return (this.x == that.x && this.y == that.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
