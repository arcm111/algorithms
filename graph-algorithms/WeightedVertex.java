/**
 * Used in {@code WeightedGraph} and {@code WeightedDirectedGraph} to store
 * weights of the edges.
 */
public class WeightedVertex <T extends VertexInterface, E extends Number>
       implements Comparable<WeightedVertex<T, E>> {
    public static final INF POSITIVE_INFINITY = INF.POSITIVE;
    public static final INF NEGATIVE_INFINITY = INF.NEGATIVE;
    public static final INF ZERO = INF.ZERO;
    public static final int NIL = -1;
    public INF infinity = INF.NONE; // if weight is +inf, -inf or in between
    public final T vertex; // original vertex
    public int parent; // parent vertex
    public E key; // weight

    private static enum INF {
        POSITIVE,
        NEGATIVE,
        ZERO,
        NONE
    }

    public WeightedVertex(T vertex) {
        this.vertex = vertex;
    }

    public WeightedVertex(T vertex, int parent) {
        this.vertex = vertex;
        this.parent = parent;
    }

    public WeightedVertex(T vertex, E key) {
        this.vertex = vertex;
        this.key = key;
    }

    public WeightedVertex(T vertex, int parent, E key) {
        this.vertex = vertex;
        this.parent = parent;
        this.key = key;
    }

    public int getVertex() {
        return vertex.getVertex();
    }

    public E getKey() {
        return key;
    }

    public void setKey(E key) {
        this.infinity = INF.NONE;
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public void setKey(INF key) {
        this.infinity = key;
        this.key = null;
    }

    @Override
    public int compareTo(WeightedVertex<T, E> that) {
        INF k1 = this.infinity;
        INF k2 = that.infinity;
        if (k1 == POSITIVE_INFINITY && k2 == POSITIVE_INFINITY) return 0;
        if (k1 == NEGATIVE_INFINITY && k2 == NEGATIVE_INFINITY) return 0;
        if (k1 == POSITIVE_INFINITY || k2 == NEGATIVE_INFINITY) return 1;
        if (k1 == NEGATIVE_INFINITY || k2 == POSITIVE_INFINITY) return -1;
        return NumberUtility.compare(this.key, that.key);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("v: " + getVertex());
        s.append(" -> p: " + parent);
        String k;
        if (infinity == POSITIVE_INFINITY) k = "positive infinity";
        else if (infinity == NEGATIVE_INFINITY) k = "negative infinity";
        else if (infinity == ZERO) k = "zero";
        else k = key.toString();
        s.append(" -> key: " + k);
        return s.toString();
    }
}

