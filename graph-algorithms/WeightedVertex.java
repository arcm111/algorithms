/**
 * Used in {@code WeightedGraph} and {@code WeightedDirectedGraph} to store
 * weights of the edges.
 */
public class WeightedVertex <T extends VertexInterface, E extends Number>
       implements Comparable<WeightedVertex<T, E>> {
    public static final NumericKey.INF POSITIVE_INFINITY = 
            NumericKey.INF.POSITIVE;
    public static final NumericKey.INF NEGATIVE_INFINITY = 
            NumericKey.INF.NEGATIVE;
    public static final NumericKey.INF ZERO = NumericKey.INF.ZERO;
    public static final int NIL = -1;
    public NumericKey.INF infinity = NumericKey.INF.NONE; 
    public final T vertex; // original vertex
    public int parent; // parent vertex
    public NumericKey<E> weight; // weight

    /**
     * Constructor.
     * @param vertex the vertex to store
     */
    public WeightedVertex(T vertex) {
        this.vertex = vertex;
        this.weight = new NumericKey<>();
    }

    /**
     * Constructor.
     * @param vertex the vertex to store
     * @param parent the index of the parent vertex
     */
    public WeightedVertex(T vertex, int parent) {
        this.vertex = vertex;
        this.parent = parent;
        this.weight = new NumericKey<>();
    }

    /**
     * Constructor.
     * @param vertex the vertex to store
     * @param weight the weight of the vertex
     */
    public WeightedVertex(T vertex, E key) {
        this.vertex = vertex;
        this.weight = new NumericKey<>(key);
    }

    /**
     * Constructor.
     * @param vertex the vertex to store
     * @param parent the index of the parent vertex
     * @param weight the weight of the vertex
     */
    public WeightedVertex(T vertex, int parent, E key) {
        this.vertex = vertex;
        this.parent = parent;
        this.weight = new NumericKey<>(key);
    }

    public int getVertex() {
        return vertex.getVertex();
    }

    public NumericKey<E> getKey() {
        return weight;
    }

    public E getWeight() {
        return weight.key;
    }

    public void setWeight(E key) {
        this.infinity = NumericKey.INF.NONE;
        this.weight.setKey(key);
    }

    public void setWeight(NumericKey.INF infinity) {
        this.infinity = infinity;
        this.weight = null;
    }

    @Override
    public int compareTo(WeightedVertex<T, E> that) {
        return this.weight.compareTo(that.weight);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("v: " + getVertex());
        s.append(" -> p: " + parent);
        s.append(" -> key: " + weight);
        return s.toString();
    }
}

