/**
 * A weighted directed graph-edge class.
 */
public class WeightedEdge<T extends VertexInterface, E extends Number> 
        implements Comparable {
    private final int u; // incident from
    private final int v; // incident to
    private final E w; // weight
    private final T sourceVertex;
    private final T dstVertex;

    public WeightedEdge(T sourceVertex, T dstVertex, E w) {
        this.sourceVertex = sourceVertex;
        this.dstVertex = dstVertex;
        this.u = sourceVertex.getVertex();
        this.v = dstVertex.getVertex();
        this.w = w;
    }

    public int incidentFrom() {
        return u;
    }

    public int incidentTo() {
        return v;
    }

    public T getSourceVertex() {
        return sourceVertex;
    }

    public T getDstVertex() {
        return dstVertex;
    }

    public E getWeight() {
        return w;
    }

    @Override
    public int compareTo(WeightedEdge that) {
        return this.getWeight() - that.getWeight();
    }

    @Override
    public String toString() {
        return "[" + u + "->" + v + "]:[" + w + "]";
    }
}
