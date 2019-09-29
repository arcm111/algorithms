/**
 * Used in {@code WeightedGraph} and {@code WeightedDirectedGraph} to store
 * weights of the edges.
 */
public class ShortestPathVertex<E extends Number> implements VertexInterface,
       Comparable<ShortestPathVertex<E>> {
    public static final INF POSITIVE_INFINITY = INF.POSITIVE;
    public static final INF NEGATIVE_INFINITY = INF.NEGATIVE;
    public static final INF ZERO = INF.ZERO;
    public static final int NIL = -1;
    public INF infinity = INF.NONE; 
    public int vertex; // vertex
    public int parent; // parent vertex
    public E d; // weight

    private static enum INF {
        POSITIVE,
        NEGATIVE,
        ZERO,
        NONE
    }

    public ShortestPathVertex(int vertex) {
        this.vertex = vertex;
        this.parent = NIL;
    }

    public ShortestPathVertex(int vertex, int parent) {
        this.vertex = vertex;
        this.parent = parent;
    }

    public ShortestPathVertex(int vertex, E d) {
        this.vertex = vertex;
        this.d = d;
    }

    public ShortestPathVertex(int vertex, int parent, E d) {
        this.vertex = vertex;
        this.parent = parent;
        this.d = d;
    }

    @Override
    public int getVertex() {
        return vertex;
    }

    public E getDistance() {
        return d;
    }

    public void setDistance(E d) {
        this.infinity = INF.NONE;
        this.d = d;
    }

    public void setDistance(ShortestPathVertex<E> u) {
        this.infinity = u.infinity;
        this.d = u.getDistance();
    }

    public void setDistance(INF d) {
        this.infinity = d;
        this.d = null;
    }

    /**
     * Create new instance of this vertex after adding the new distance key
     * to current distance.
     * @param w the extra distance to be added
     * @return new copy of current vertex after adding w to old distance d
     * @throws IllegalArgumentException if the extra distance is null
     */
    @SuppressWarnings("unchecked")
    public <E extends Number> ShortestPathVertex<E> 
            sumDistances(E w) {
        if (w == null) throw new IllegalArgumentException("w can't be null");
        ShortestPathVertex<E> v = new ShortestPathVertex<>(vertex, parent);
        if (infinity == POSITIVE_INFINITY || infinity == NEGATIVE_INFINITY) {
            v.setDistance(infinity);
        } else if (infinity == ZERO) {
            v.setDistance(w);
        } else {
            v.setDistance((E) NumberUtility.add(getDistance(), w));
        }
        return v;
    }

    @SuppressWarnings("unchecked")
    public void addDistance(E w) {
        if (w == null) throw new IllegalArgumentException("w can't be null");
        if (infinity == ZERO) {
            setDistance(w);
        } else if (infinity == INF.NONE) {
            setDistance((E) NumberUtility.add(getDistance(), w));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(ShortestPathVertex<E> that) {
        INF k1 = this.infinity;
        INF k2 = that.infinity;
        if (k1 != INF.NONE && k2 != INF.NONE && k1 == k2) return 0;
        if (k1 == POSITIVE_INFINITY || k2 == NEGATIVE_INFINITY) return 1;
        if (k1 == NEGATIVE_INFINITY || k2 == POSITIVE_INFINITY) return -1;
        E a = this.d;
        E b = that.d;
        if (k1 == ZERO) {
            a = (E) NumberUtility.zero(b);
        } else if (k2 == ZERO) {
            b = (E) NumberUtility.zero(a);
        }
        if (a == null || b == null) {
            throw new IllegalArgumentException("keys cannot be null");
        }
        return NumberUtility.compare(a, b);
    }

    // Helper Key class
    public static class Key<W extends Number> implements Comparable<Key<W>> {
        public INF infinity = INF.NONE;
        public W value = null;

        public void setKey(INF infinity) {
            this.infinity = infinity;
            this.value = null;
        }

        public void setKey(W value) {
            this.infinity = INF.NONE;
            this.value = value;
        }

        public Key<W> addKey(Key<W> that) {
            INF k1 = this.infinity;
            INF k2 = that.infinity;
            Key<W> sum = new Key<>();
            if (k1 == k2 && k1 != INF.NONE && k2 != INF.NONE) {
                sum.infinity = k1;
            } else if (k1 == POSITIVE_INFINITY && k2 == NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("Indeterminate");
            } else if (k1 == NEGATIVE_INFINITY && k2 == POSITIVE_INFINITY) {
                throw new IllegalArgumentException("Indeterminate");
            } else if (k1 == POSITIVE_INFINITY || k2 == POSITIVE_INFINITY) {
                sum.infinity = POSITIVE_INFINITY;
            } else if (k1 == NEGATIVE_INFINITY || k2 == NEGATIVE_INFINITY) {
                sum.infinity = NEGATIVE_INFINITY;
            } else if (k2 == ZERO) {
                sum.infinity = INF.NONE;
                sum.value = this.value;
            } else if (k1 == ZERO) {
                sum.infinity = INF.NONE;
                sum.value = that.value;
            } else {
                sum.infinity = INF.NONE;
                sum.value = NumberUtility.add(this.value, that.value);
            }
            return sum;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compareTo(Key<W> that) {
            INF k1 = this.infinity;
            INF k2 = that.infinity;
            if (k1 != INF.NONE && k2 != INF.NONE && k1 == k2) return 0;
            if (k1 == POSITIVE_INFINITY || k2 == NEGATIVE_INFINITY) return 1;
            if (k1 == NEGATIVE_INFINITY || k2 == POSITIVE_INFINITY) return -1;
            W a = this.value;
            W b = that.value;
            if (k1 == ZERO) {
                a = (W) NumberUtility.zero(b);
            } else if (k2 == ZERO) {
                b = (W) NumberUtility.zero(a);
            }
            if (a == null || b == null) {
                throw new IllegalArgumentException("keys cannot be null");
            }
            return NumberUtility.compare(a, b);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[" + getVertex());
        s.append("][" + parent);
        String k;
        if (infinity == POSITIVE_INFINITY) k = "positive infinity";
        else if (infinity == NEGATIVE_INFINITY) k = "negative infinity";
        else if (infinity == ZERO) k = "zero";
        else if (d == null) k = "null";
        else k = d.toString();
        s.append("][" + k + "]");
        return s.toString();
    }
}

