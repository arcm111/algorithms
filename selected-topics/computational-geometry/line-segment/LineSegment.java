/**
 * Computaional-geometric algorithms based on line-segement properties.
 */
public class LineSegment {
    /**
     * Checks if two segments intersect each other.
     * @param s1 first segment
     * @param s2 second segment
     * @return true if the two segments intersect, otherwise false
     */
    public static boolean segmentsIntersect(Segment s1, Segment s2) {
        return segmentsIntersect(s1.p1, s1.p2, s2.p1, s2.p2);
    }

    /**
     * Checks if two segments intersect.
     * Two segments intersect either if each segment straddle the line
     * containing the other segment, or if one end-point of one segment lies on
     * the other segment.
     * A segment straddles the line containing another segment if its end-points
     * lies on opposite sides of that line.
     * Running time is <em>O(1)</em>.
     * @param p1 end-point of first segment
     * @param p2 the other end-point of the first segment
     * @param p3 end-point of second segment
     * @param p4 the other end-point of the second segment
     * @return true if the two segments intersect, otherwise false
     */
    public static boolean 
            segmentsIntersect(Point p1, Point p2, Point p3, Point p4) {
        int d1 = direction(p3, p4, p1);
        int d2 = direction(p3, p4, p2);
        int d3 = direction(p1, p2, p3);
        int d4 = direction(p1, p2, p4);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && 
                ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            return true;
        } else if (d1 == 0 && onSegment(p3, p4, p1)) {
            return true;
        } else if (d2 == 0 && onSegment(p3, p4, p2)) {
            return true;
        } else if (d3 == 0 && onSegment(p1, p2, p3)) {
            return true;
        } else if (d4 == 0 && onSegment(p1, p2, p4)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if any pair of segments intersect in a given set of segments.
     * Uses a technique called <i>sweeping</i>, in which an imaginary vertical
     * sweep line passes through all segments' end-points ordered from left to
     * right. When it encounters a left end-point, it adds its segment to the
     * sweep-line-status and whenever it encounters a right end-point, it 
     * removes its segment from the sweep-line-status. Whenever two segments
     * first become consecutive in the total preorder, it checks whether they
     * intersect or not.
     * The relations above or below of two segments comparable at some line x
     * is a total preorder relations (total preorder = total relation that 
     * is reflexive and transitive but not symmetric or anti-symmetric).
     * Assumes that no three segments intersect and no vertical segments exist.
     * Can not find all intersections and the the intersections it finds are
     * not in left-to-right order.
     * Uses red-black-tree to maintain above/below order of segments because
     * red-black-trees are efficient for insert, delete, predecessor and 
     * seccessor operations which all cost only O(lgn) time each.
     * Running time is <em>O(nlgn)</em>.
     * @param segements the set of segments
     * @return true if an intersecting pair was found, otherwise false
     * @throws IllegalArgumentException if input set contains vertical segements
     */
    public static boolean anySegmentsIntersect(Segment[] segments) {
        SegmentPoint[] points = new SegmentPoint[segments.length * 2];
        for (int i = 0; i < segments.length; i++) {
            Segment s = segments[i];
            if (s.isVertical()) {
                throw new IllegalArgumentException("vertical segments!");
            }
            points[2 * i] = s.leftEndpoint();
            points[2 * i + 1] = s.rightEndpoint();
        }
        MergeSort.sort(points);
        System.out.println("sorted: ");
        for (SegmentPoint p : points) System.out.println(p);
        RedBlackTree<Segment> T = new RedBlackTree<>();
        for (SegmentPoint p : points) {
            System.out.println("p: " + p);
            Segment s = p.parentSegment;
            System.out.println("s: " + s);
            if (p.isLeftEndpoint()) {
                T.insert(s);
                Segment above = T.successor(s);
                Segment below = T.predecessor(s);
                if (above != null && segmentsIntersect(s, above)) {
                    System.out.println("Intersectoin found between s, above: ");
                    System.out.println(s);
                    System.out.println(above);
                    return true;
                }
                if (below != null && segmentsIntersect(s, below)) {
                    System.out.println("Intersectoin found between s, below: ");
                    System.out.println(s);
                    System.out.println(below);
                    return true;
                }
            }
            if (p.isRightEndpoint()) {
                Segment above = T.successor(s);
                Segment below = T.predecessor(s);
                if (above != null && below != null) {
                    if (segmentsIntersect(above, below)) {
                        System.out.println("above, below intersection: ");
                        System.out.println(above);
                        System.out.println(below);
                        return true;
                    }
                }
                T.delete(s);
            }
            System.out.println(T);
        }
        return false;
    }

    /**
     * Check whether two consequtive segments turn left or right.
     * Depending on the value of the cross product of pipj and pipk, pipj is 
     * either clockwise from pipk when its positive, counter-clockwise if 
     * negative or colinear if 0.
     * @param pi common end-point of both segments
     * @param pj the other end-point of first segment
     * @param pk the other end-point of second segment
     * @return -1 if clockwise, 1 if counter-clockwise or 0 if colinear
     */
    private static int direction(Point pi, Point pj, Point pk) {
        double r = crossProduct(pi, pj, pk);
        if (r < 0) return -1;
        else if (r > 0) return 1;
        return 0;
    }

    /**
     * Checks if a point lies on a given segement.
     * Assumes that point pk is colinear with segment pipj.
     * @param pi left end-point of the segement
     * @param pj right end-point of the segment
     * @param pk the point to check if it is on the segement or not
     * @return true if pk is on pipj, otherwise false
     */
    private static boolean onSegment(Point pi, Point pj, Point pk) {
        if (Math.min(pi.x, pj.x) <= pk.x && pk.x <= Math.max(pi.x, pj.x) &&
                Math.min(pi.y, pj.y) <= pk.y && pk.y <= Math.max(pi.y, pj.y)) {
            return true;
        }
        return false;
    }

    /**
     * Computes the cross-products of two segements.
     * @param p0 common end-point of both segments
     * @param p1 the other end-point of first segment
     * @param p2 the other end-point of second segment
     */
    private static double crossProduct(Point p0, Point p1, Point p2) {
        return ((p1.x - p0.x) * (p2.y - p0.y) - (p2.x - p0.x) * (p1.y - p0.y));
    }

    /**
     * Unit tests.
     */
    public static void main(String[] args) {
        // Testing segmentsIntersect
        // p1p2 and p3p4 intersect
        Point p1 = new Point(2, 6);
        Point p2 = new Point(5, 1);
        Point p3 = new Point(1, 2);
        Point p4 = new Point(6, 7);
        boolean r = segmentsIntersect(p1, p2, p3, p4);
        System.out.println("p1p2 intersects p3p4 => " + r);
        // p1p2 and p3p4 do not intersect
        p1 = new Point(3, 3);
        r = segmentsIntersect(p1, p2, p3, p4);
        System.out.println("p1p2 intersects p3p4 => " + r);
        // p3 is conlinear to p1p2 and on the segment
        p1 = new Point(1, 1);
        p2 = new Point(8, 8);
        p3 = new Point(4, 4);
        p4 = new Point(7, 3);
        r = segmentsIntersect(p1, p2, p3, p4);
        System.out.println("p1p2 intersects p3p4 => " + r);

        // Testing anySegmentsIntersect
        Segment[] segments = new Segment[6];
        segments[0] = new Segment(new Point(1, 7), new Point(6, 5));
        segments[1] = new Segment(new Point(2, 1), new Point(18, 6));
        segments[2] = new Segment(new Point(3, 3), new Point(10, 4));
        segments[3] = new Segment(new Point(4, 7), new Point(17, 3));
        segments[4] = new Segment(new Point(8, 7), new Point(16, 4));
        segments[5] = new Segment(new Point(11, 3), new Point(15, 2));
        anySegmentsIntersect(segments);
    }
}
