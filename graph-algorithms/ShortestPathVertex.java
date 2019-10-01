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
    public static final NumericKey.INF POSITIVE_INFINITY = 
            NumericKey.INF.POSITIVE;
    public static final NumericKey.INF NEGATIVE_INFINITY = 
            NumericKey.INF.NEGATIVE;
    public static final NumericKey.INF ZERO = NumericKey.INF.ZERO;
    public static final int NIL = -1;
    public int vertex; // vertex
    public int parent; // parent vertex
    public NumericKey<E> d; // weight

    /**
     * Constructor.
     * @param vertex the vertex index
     */
    public ShortestPathVertex(int vertex) {
        this.vertex = vertex;
        this.parent = NIL;
        this.d = new NumericKey<E>();
    }

    /**
     * Constructor.
     * @param vertex the vertex index
     * @param parent the index of the parent vertex
     */
    public ShortestPathVertex(int vertex, int parent) {
        this.vertex = vertex;
        this.parent = parent;
        this.d = new NumericKey<E>();
    }

    /**
     * Constructor.
     * @param vertex the vertex index
     * @param d the distance of the vertex from the source
     */
    public ShortestPathVertex(int vertex, E d) {
        this.vertex = vertex;
        this.d = new NumericKey<E>(d);
    }

    /**
     * Constructor.
     * @param vertex the vertex index
     * @param parent the index of the parent vertex
     * @param d the distance of the vertex from the source
     */
    public ShortestPathVertex(int vertex, int parent, E d) {
        this.vertex = vertex;
        this.parent = parent;
        this.d = new NumericKey<E>(d);
    }

    /**
     * Gets the index of stored vertex.
     * @return the index as an integer.
     */
    @Override
    public int getVertex() {
        return vertex;
    }

    public NumericKey<E> getKey() {
        return d;
    }

    public E getDistance() {
        return d.key;
    }

    public void setDistance(E value) {
        this.d.setKey(value);
    }

    public void setDistance(NumericKey.INF infinity) {
        this.d.setKey(infinity);
    }

    public void setDistance(NumericKey<E> u) {
        this.d = u;
    }

    @Override
    public int compareTo(ShortestPathVertex<E> that) {
        return this.d.compareTo(that.d);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[" + getVertex());
        s.append("][" + parent);
        s.append("][" + d + "]");
        return s.toString();
    }
}

