public class FlowNetworkPath <T extends VertexInterface, 
        E extends Number> {
    private LinkedList<T> vertices = new LinkedList<T>();
    private LinkedList<FlowNetworkEdge<T, E>> edges = new LinkedList<>();
    private T headVertex;
    private T tailVertex;

    public Iterable<T> getVertices() {
        return vertices;
    }

    public Iterable<FlowNetworkEdge<T, E>> getEdges() {
        return edges;
    }

    public void addEdge(FlowNetworkEdge<T, E> e) {
        if (headVertex == null) {
            headVertex = e.sourceVertex();
            tailVertex = e.destinationVertex();
            this.vertices.add(headVertex);
            this.vertices.add(tailVertex);
            this.edges.add(e);
        } else if (tailVertex.getVertex() != e.incidentFrom()) {
            System.out.println(tailVertex);
            System.out.println(e);
            throw new IllegalArgumentException("Invalid edge");
        } else {
            this.tailVertex = e.destinationVertex();
            this.vertices.add(tailVertex);
            this.edges.add(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (FlowNetworkEdge<T, E> e : edges) {
            builder.append(e.incidentFrom() + "--" + e.getCapacity());
            builder.append("-->" + e.incidentTo());
            builder.append(" ");
        }
        return builder.toString();
    }
}
