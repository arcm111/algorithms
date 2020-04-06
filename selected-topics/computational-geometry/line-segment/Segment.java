/**
 * Line-Segment class.
 */
public class Segment implements Comparable<Segment> {
    public Point p1;
    public Point p2;
    private final double slope;

    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.slope = (p2.y - p1.y) / (p2.x - p1.x);
    }

    /**
     * Returns left end point of the segment.
     */
    public SegmentPoint leftEndpoint() {
        if (p1.x < p2.x) {
            return new SegmentPoint(this, p1, true);
        }
        return new SegmentPoint(this, p2, false);
    }

    /**
     * Returns right end point of the segment.
     */
    public SegmentPoint rightEndpoint() {
        if (p1.x > p2.x) {
            return new SegmentPoint(this, p1, true);
        }
        return new SegmentPoint(this, p2, false);
    }

    /**
     * For any point on the line extending the line-segment find its y 
     * coordinate using its x coordinate.
     * @param x the x coordinate
     * @return y coordinate
     */
    public double getYCoordinate(double x) {
        return slope * (x - p1.x) + p1.y;
    }

    public boolean isVertical() {
        return p1.x == p2.x;
    }

    /**
     * Checks whether this segment is above or below another segment at the
     * sweep line passing through the left end-point of this segment.
     * Assumes that both segments are compareable at x coordinates of the left
     * end-point.
     * @param that the other segment to compare to
     * @return the order of this segment compared to the other one
     */
    @Override
    public int compareTo(Segment that) {
        if (this.equals(that)) return 0; // identical
        double y = that.getYCoordinate(this.p1.x);
        if (this.p1.y > y) return 1;
        if (this.p1.y < y) return -1;
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        Segment that = (Segment) obj;
        return this.p1.equals(that.p1) && this.p2.equals(that.p2);
    }

    @Override
    public String toString() {
        return p1 + " -> " + p2;
    }
}

