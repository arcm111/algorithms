/**
 * A class containing a pair of points.
 * Used in closes-pair-of-points algorithm.
 * Provides useful information such as the distance between the points.
 */
public class Pair {
    public final Point p1;
    public final Point p2;

    public Pair(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public double distance() {
        return p1.distanceTo(p2);
    }

    public double squareDistance() {
        return p1.squareDistanceTo(p2);
    }

    @Override
    public String toString() {
        return "[" + p1 + ", " + p2 + "]";
    }
}
