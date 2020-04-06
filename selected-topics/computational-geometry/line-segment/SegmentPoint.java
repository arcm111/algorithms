/**
 * A segment end-point class.
 * Contians the point coordinates and an whether it is the left end-point of 
 * the segment of the right end-point.
 * It also has a reference to the parent segement.
 */
public class SegmentPoint implements Comparable<SegmentPoint> {
    public final Segment parentSegment;
    public final double x;
    public final double y;
    public final boolean e;

    public SegmentPoint(Segment parentSegment, Point p, boolean e) {
        this.parentSegment = parentSegment;
        this.x = p.x;
        this.y = p.y;
        this.e = e;
    }

    public boolean isLeftEndpoint() {
        return e;
    }

    public boolean isRightEndpoint() {
        return !e;
    }

    /**
     * Sorts segmentPoints from left to right, breaking ties by putting left
     * end-points before right end-points and breaking further ties by putting
     * points with lower y-coordinates first.
     * @param that the segment point to compare to
     * @return the order of this segment-point compared to the other one
     */
    public int compareTo(SegmentPoint that) {
        if (this.x == that.x && this.y == that.y) return 0; // identical
        if (this.x < that.x) {
            return -1;
        } else if (this.x > that.x) {
            return 1;
        } else { 
            // break ties by putting left endpoints before right endpoints
            if (this.isLeftEndpoint() && that.isRightEndpoint()) {
                return -1;
            } else if (this.isRightEndpoint() && that.isLeftEndpoint()) {
                return 1;
            } else {
                // break further ties by putting points with lower
                // y-coordinates first
                if (this.y < that.y) {
                    return -1;
                } else if (this.y > that.y) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        SegmentPoint that = (SegmentPoint) obj;
        return (this.x == that.x && this.y == that.y && this.e == that.e);
    }

    @Override
    public String toString() {
        String d = isLeftEndpoint() ? "[Left]" : "[Right]";
        return d + " " + x + ":" + y;
    }
}

