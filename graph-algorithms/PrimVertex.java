public class PrimVertex <T extends VertexInterface, E extends Comparable<E>>
       implements Comparable<PrimVertex<T, E>> {
    public static final INF POSITIVE_INFINITY = INF.POSITIVE;
    public static final INF NEGATIVE_INFINITY = INF.NEGATIVE;
    public static final int NIL = -1;
    public INF infinity = INF.NONE;
    public final T vertex;
    public int parent;
    public E key;

    private static enum INF {
        POSITIVE,
        NEGATIVE,
        NONE
    }

    public PrimVertex(T vertex) {
        this.vertex = vertex;
    }

    public int getVertex() {
        return vertex.getVertex();
    }

    public void setKey(E key) {
        this.infinity = INF.NONE;
        this.key = key;
    }

    public void setKey(INF key) {
        this.infinity = key;
        this.key = null;
    }

    @Override
    public int compareTo(PrimVertex<T, E> that) {
        INF k1 = this.infinity;
        INF k2 = that.infinity;
        if (k1 == POSITIVE_INFINITY && k2 == POSITIVE_INFINITY) return 0;
        if (k1 == NEGATIVE_INFINITY && k2 == NEGATIVE_INFINITY) return 0;
        if (k1 == POSITIVE_INFINITY) return 1;
        if (k1 == NEGATIVE_INFINITY) return -1;
        if (k2 == POSITIVE_INFINITY) return -1;
        if (k2 == NEGATIVE_INFINITY) return 1;
        return this.key.compareTo(that.key);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("v: " + getVertex());
        s.append(" -> p: " + parent);
        String k;
        if (infinity == POSITIVE_INFINITY) k = "positive infinity";
        else if (infinity == NEGATIVE_INFINITY) k = "negative infinity";
        else k = key.toString();
        s.append(" -> key: " + k + "\n");
        s.append(vertex + "\n");
        return s.toString();
    }
}

