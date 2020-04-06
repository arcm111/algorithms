import java.util.Comparator;

/**
 * Point class for closes-pair-of-points algorithm.
 */
public class Point implements Comparable<Point> {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double squareDistanceTo(Point that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;
        return dx * dx + dy * dy;
    }

    public double distanceTo(Point that) {
        return Math.sqrt(squareDistanceTo(that));
    }

    /**
     * Sorts points by x coordinates.
     */
    public static class XPositionComparator implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {
            if (a.x > b.x) return 1;
            if (a.x < b.x) return -1;
            return 0;
        }
    }

    /**
     * Sorts points by y coordinates.
     */
    public static class YPositionComparator implements Comparator<Point> {
        @Override
        public int compare(Point a, Point b) {
            if (a.y > b.y) return 1;
            if (a.y < b.y) return -1;
            return 0;
        }
    }

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
