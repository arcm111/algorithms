public class FlowNetworkPath <E extends Number> {
    private LinkedList<Integer> vertices = new LinkedList<>();
    private LinkedList<Edge<E>> edges = new LinkedList<>();
    private int headVertex = -1;
    private int tailVertex = -1;

    public Iterable<Integer> getVertices() {
        return vertices;
    }

    public Iterable<Edge<E>> getEdges() {
        return edges;
    }

    public void addEdge(Edge<E> e) {
        addEdge(e.incidentFrom(), e.incidentTo(), e.getCapacity());
    }

    public void addEdge(int u, int v, NumericKey<E> c) {
        if (headVertex == -1) {
            headVertex = u;
            tailVertex = v;
            this.vertices.add(u);
            this.vertices.add(v);
        } else if (tailVertex != u) {
            System.out.println(tailVertex);
            System.out.println(u);
            throw new IllegalArgumentException("Invalid edge");
        } else {
            this.vertices.add(v);
            this.tailVertex = v;
        }
        this.edges.add(new Edge<E>(u, v, c));
    }

    public static class Edge<E extends Number> {
        private int incidentFrom;
        private int incidentTo;
        private NumericKey<E> capacity;

        public Edge(int u, int v, NumericKey<E> c) {
            this.incidentFrom = u;
            this.incidentTo = v;
            this.capacity = c;
        }

        public int incidentFrom() {
            return incidentFrom;
        }

        public int incidentTo() {
            return incidentTo;
        }

        public NumericKey<E> getCapacity() {
            return capacity;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Edge<E> e : edges) {
            builder.append(e.incidentFrom() + "--" + e.getCapacity());
            builder.append("-->" + e.incidentTo());
            builder.append(" ");
        }
        return builder.toString();
    }
}
