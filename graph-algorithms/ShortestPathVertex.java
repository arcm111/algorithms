/**
 * Helper class for {@code ShortestPath} which stores the distances of each
 * vertex from the source.
 * <p>The supported data-types of stored distances are: {@code Integer},
 * {@code Double}, {@code Float}, {@code Long} and {@code Short}.
 * <p>It also implements +INF, -INF, 0 as generic values for all supported
 *  data types.
 * <p>The class implements {@code Comparable} interface allowing two instances
 * to be compared together even if their values are set to +/-INF.
 *
 * Depends on {@code NumberUtility} class.
 */
public class ShortestPathVertex<E extends Number> implements VertexInterface,
       Comparable<ShortestPathVertex<E>> {
    public static final INF POSITIVE_INFINITY = INF.POSITIVE;
    public static final INF NEGATIVE_INFINITY = INF.NEGATIVE;
    public static final INF ZERO = INF.ZERO;
    public static final int NIL = -1;
    public int vertex; // vertex
    public int parent; // parent vertex
    public Key d; // weight

    private static enum INF {
        POSITIVE,
        NEGATIVE,
        ZERO,
        NONE
    }

    public ShortestPathVertex(int vertex) {
        this.vertex = vertex;
        this.parent = NIL;
        this.d = new Key();
    }

    public ShortestPathVertex(int vertex, int parent) {
        this.vertex = vertex;
        this.parent = parent;
        this.d = new Key();
    }

    public ShortestPathVertex(int vertex, E d) {
        this.vertex = vertex;
        this.d = new Key(d);
    }

    public ShortestPathVertex(int vertex, int parent, E d) {
        this.vertex = vertex;
        this.parent = parent;
        this.d = new Key(d);
    }

    @Override
    public int getVertex() {
        return vertex;
    }

    public Key getKey() {
        return d;
    }

    public E getDistance() {
        return d.value;
    }

    public void setDistance(E value) {
        this.d.setValue(value);
    }

    public void setDistance(INF infinity) {
        this.d.setValue(infinity);
    }

    public void setDistance(Key u) {
        this.d = u;
    }

    @Override
    public int compareTo(ShortestPathVertex<E> that) {
        return this.d.compareTo(that.d);
    }

    // Helper Key class
    public class Key implements Comparable<Key> {
        public INF infinity = INF.NONE;
        public E value = null;

        public Key() {
        }

        public Key(E value) {
            this.value = value;
        }

        public Key(INF infinity) {
            this.infinity = infinity;
        }

        public void setValue(INF infinity) {
            this.infinity = infinity;
            this.value = null;
        }

        public void setValue(E value) {
            this.infinity = INF.NONE;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        public Key plus(E val) {
            if (val == null) {
                throw new IllegalArgumentException("val can't be null");
            }
            Key result = new Key();
            INF k = this.infinity;
            if (k == POSITIVE_INFINITY || k == NEGATIVE_INFINITY) {
                result.setValue(k);
            } else if (k == ZERO) {
                result.setValue(val);
            } else {
                result.setValue((E) NumberUtility.add(this.value, val));
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        public Key plus(Key that) {
            INF k1 = this.infinity;
            INF k2 = that.infinity;
            Key sum = new Key();
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
                sum.value = (E) NumberUtility.add(this.value, that.value);
            }
            return sum;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compareTo(Key that) {
            INF k1 = this.infinity;
            INF k2 = that.infinity;
            if (k1 != INF.NONE && k2 != INF.NONE && k1 == k2) return 0;
            if (k1 == POSITIVE_INFINITY || k2 == NEGATIVE_INFINITY) return 1;
            if (k1 == NEGATIVE_INFINITY || k2 == POSITIVE_INFINITY) return -1;
            E a = this.value;
            E b = that.value;
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
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[" + getVertex());
        s.append("][" + parent);
        String k;
        if (d.infinity == POSITIVE_INFINITY) k = "positive infinity";
        else if (d.infinity == NEGATIVE_INFINITY) k = "negative infinity";
        else if (d.infinity == ZERO) k = "zero";
        else if (d.value == null) k = "null";
        else k = d.value.toString();
        s.append("][" + k + "]");
        return s.toString();
    }
}

